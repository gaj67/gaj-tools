package gaj.analysis.curves;

import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.numeric.vector.impl.VectorFactory;

/**
 * Provides methods for fitting, using and interpreting cubic curves of the form
 * f(x) = ax^3+bx^2+cx+d.
 */
public abstract class Cubics {

    private Cubics() {}

    /**
     * Fits the cubic curve f(x)=ax^3+bx^2+cx+d through two points (u,f(u)) and
     * (v,f(v)) with known gradients f'(u) and f'(v).
     * 
     * @param u
     *            The value of one point, u.
     * @param fu
     *            The value of f(u).
     * @param dfu
     *            The value of f'(u).
     * @param v
     *            The value of another point, v.
     * @param fv
     *            The value of f(v).
     * @param dfv
     *            The value of f'(v).
     * @return The [a,b,c,d] array of cubic coefficients.
     */
    public static double[] cubicFit(double u, double fu, double dfu, double v, double fv, double dfv) {
        /*
         * The relevant equations are: 
         *    f(u)  =  au^3 +  bu^2 + cu + d 
         *    f(v)  =  av^3 +  bv^2 + cv + d
         *    f'(u) = 3au^2 + 2bu   + c
         *    f'(v) = 3av^2 + 2bv   + c
         * By the mean value theorem, 
         *    f'(xi) = [f(v)-f(u)]/(v-u)
         *           = [a(v^3-u^3) + b(v^2-u^2) + c(v-u)]/(v-u) 
         *           = a(u^2+uv+v^2) + b(u+v) + c 
         * => f'(xi)-f'(u) = a(v^2+uv-2u^2) + b(v-u)
         *    f'(v)-f'(xi) = a(2v^2-uv-u^2) + b(v-u)
         * => f'(v)+f'(u)-2f'(xi) = a(v^2-2uv+u^2) = a(v-u)^2
         * => a => [f'(v)+f'(u)-2f'(xi)]/(v-u)^2
         * => b = [f'(xi)-f'(u)]/(v-u) - a(v+2u)
         * => c = f'(u) - 3au^2 - 2bu
         * => d = f(u) - au^3 - bu^2 - cu.
         */
        final double delta = v - u;
        final double dfxi = (fv - fu) / delta;
        final double a = (dfv + dfu - 2 * dfxi) / (delta * delta);
        final double b = (dfxi - dfu) / delta - a * (v + 2 * u);
        final double c = dfu - u * (3 * a * u + 2 * b);
        final double d = fu - u * (c + u * (b + u * a));
        return new double[] { a, b, c, d };
    }

    /**
     * Determines the local maximum turning point of the cubic f(x) =
     * ax^3+bx^2+cx+d.
     * 
     * @param coeffs
     *            - The [a,b,c,d] array of cubic coefficients.
     * @return The position x* of the local maximum turning point, or a value of
     *         +infinity if no such point exists.
     */
    public static double cubicMaximum(double[] coeffs) {
        /*
         * Now, f(x) = ax^3+bx^2+cx+d
         * =>  f'(x) = 3ax^2+2bx+c.
         * Let f'(x*) = 0
         * => x* = (-B +/- sqrt{B^2-4AC})/(2A)
         *       = (-(2b) +/- sqrt{4b^2-12ac})/(6a)
         *       = (-b +/- sqrt{b^2-3ac})/(3a).
         * Note: a > 0 (or a < 0) means local maximum is left (or right) of -b/(3a).
         */
        final double discrim = coeffs[1] * coeffs[1] - 3 * coeffs[0] * coeffs[2]; // b^2-3ac.
        if (discrim < 0)
            return Double.POSITIVE_INFINITY;
        return (-coeffs[1] - Math.sqrt(discrim)) / (3 * coeffs[0]);
    }

    /**
     * Determines the local minimum turning point of the cubic f(x) =
     * ax^3+bx^2+cx+d.
     * 
     * @param coeffs
     *            - The [a,b,c,d] array of cubic coefficients.
     * @return The position x* of the local minimum turning point, or a value of
     *         -infinity if no such point exists.
     */
    public static double cubicMinimum(double[] coeffs) {
        /*
         * Now, f(x) = ax^3+bx^2+cx+d
         * =>  f'(x) = 3ax^2+2bx+c.
         * Let f'(x*) = 0
         * => x* = (-B +/- sqrt{B^2-4AC})/(2A)
         *       = (-(2b) +/- sqrt{4b^2-12ac})/(6a)
         *       = (-b +/- sqrt{b^2-3ac})/(3a).
         * Note: a > 0 (or a < 0) means local minimum is right (or left) of -b/(3a).
         */
        final double discrim = coeffs[1] * coeffs[1] - 3 * coeffs[0] * coeffs[2]; // b^2-3ac.
        if (discrim < 0)
            return Double.NEGATIVE_INFINITY;
        return (-coeffs[1] + Math.sqrt(discrim)) / (3 * coeffs[0]);
    }

    /**
     * Fits a cubic curve through two known function values, y0 and y1, and two
     * known function gradients, g0 and g1, at unknown points x0 and x1,
     * respectively, where it is known that x1 = x0 + r*d.
     * <p/>
     * Computes the scalar distance s needed to reach the estimated maximum
     * turning point at x1* = x0 + s*r*d.
     * 
     * @param y0
     *            - The value f(x0).
     * @param g0
     *            - The slope f'(x0).
     * @param y1
     *            - The value f(x1).
     * @param g1
     *            - The slope f'(x1).
     * @param r
     *            - The step-size.
     * @param d
     *            - The directed distance, (x1 - x0) / r.
     * @return The optimal step-size multiplier, s, of a value of +infinity if
     *         there is no maximum turning point.
     */
    public static double cubicMaximumScaling(
            double y0, DataVector g0,
            double y1, DataVector g1,
            double r, DataVector d)
    {
        /*
         * <p/>For simplicity, in what follows d is really r*d.
         * Consider a curve y=f(x), such that
         * y0=f(x0), g0=f'(x0), H0=f''(x0), T0=f'''(x0), etc.
         * Let x1=x0+d. Then (Taylor series)
         * f(x1) = f(x0)+f'(x0).(x1-x0)
         *         +1/2 f''(x0):(x1-x0)^2
         *         +1/6 f'''(x0):(x1-x0)^3 + O(||x1-x0||^4),
         * => y1 = y0+g0.d+1/2*d.H0.d+1/6*T0:d^3 + O(||d||^4).
         * Also,
         * f'(x1) = f'(x0)+(x1-x0).f''(x0)
         *          + 1/2 f'''(x):(x1-x0)^2 + O(||x1-x0||^3)
         * => g1 = g0+d.H0+1/2*T0:d^2 + O(||d||^3)
         * => d.H0.d = (g1-g0).d-1/2*T0:d^3 + O(||d||^4).
         * Hence, y1-y0 = 1/2*(g1+g0).d-1/12*T0:d^3 + O(||d||^4).
         * 
         * Now, let x1* = x0 + s*d, such that f'(x1*) = 0 and f''(x1*) < 0,
         * i.e. x1* is a local maximum turning point.
         * Then f'(x1*) = 0 = f'(x0) + (x1*-x0).f''(x0)
         *                    + 1/2 f'''(x0):(x1*-x0)^2 + O(||x1*-x0||^3)
         * => 0 = g0 + s*d.H0 + 1/2*s^2*T0:d^2 + O(||s*d||^3)
         * => 1/2*T0:d^3*s^2 + d.H0.d*s + g0.d = O(s^3||d||^4)
         * => hat{s} = (-d.H0.d - sqrt{(d.H0.d)^2-2*T0:d^3*g0.d}) / T0:d^3.
         * 
         * Alternatively, let phi(s) = f(x0+s*d) => phi'(s) = f'(x0+s*d).d.
         * Now let phi(s) = a*s^3+b*s^2+c*s+k => phi'(s) = 3a*s^2+2b*s+c.
         * Then y0   = f(x0) = phi(0) = k,
         *      y1   = f(x1) = phi(1) = a+b+c+k
         *      g0.d = f'(x0).d = phi'(0) = c
         *      g1.d = f'(x1).d = phi'(1) = 3a+2b+c
         * =>  a +  b = y1-y0 - g0.d
         *    3a + 2b = g1.d - g0.d
         * => a = g1.d + g0.d - 2(y1-y0)
         *    b = 3(y1-y0) - g1.d - 2g0.d
         */
        double g0d = r * VectorFactory.dot(g0, d);
        double g1d = r * VectorFactory.dot(g1, d);
        double T0d3 = 6 * ((g1d + g0d) - 2 * (y1 - y0)); // = 6a
        double H0d2 = (g1d - g0d) - 0.5 * T0d3; // = 2b
        double D = H0d2 * H0d2 - 2 * T0d3 * g0d; // = 4(b^2 - 3ac)
        if (D < 0) return Double.POSITIVE_INFINITY;
        return -(H0d2 + Math.sqrt(D)) / T0d3;
    }

    /**
     * Fits a cubic curve through two known function values, y0 and y1, and two
     * known function gradients, g0 and g1, at unknown points x0 and x1,
     * respectively, where it is known that x1 = x0 + r*d.
     * <p/>
     * Computes the scalar distance s needed to reach the estimated minimum
     * turning point at x1* = x0 + s*r*d.
     * 
     * @param y0
     *            - The value f(x0).
     * @param g0
     *            - The slope f'(x0).
     * @param y1
     *            - The value f(x1).
     * @param g1
     *            - The slope f'(x1).
     * @param r
     *            - The step-size.
     * @param d
     *            - The directed distance, (x1 - x0) / r.
     * @return The optimal step-size multiplier, s, or a value of -infinity if
     *         there is no minimum turning point.
     */
    public static double cubicMinimumScaling(
            double y0, DataVector g0, double y1, DataVector g1, 
            double r, DataVector d) 
    {
        double g0d = r * VectorFactory.dot(g0, d); // = c
        double g1d = r * VectorFactory.dot(g1, d);
        double T0d3 = 6 * ((g1d + g0d) - 2 * (y1 - y0)); // = 6a
        double H0d2 = (g1d - g0d) - 0.5 * T0d3; // = 2b
        double D = H0d2 * H0d2 - 2 * T0d3 * g0d; // = 4(b^2 - 3ac)
        if (D < 0) return Double.NEGATIVE_INFINITY;
        return (-H0d2 + Math.sqrt(D)) / T0d3;
    }

}
