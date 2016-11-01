package gaj.analysis.curves;

import gaj.data.vector.DataVector;
import gaj.impl.vector.VectorFactory;

public abstract class CurveFactory {

    private CurveFactory() {
    }

    /**
     * Fits a quadratic curve through
     * two known gradients, g0 and g1, at unknown
     * points x0 and x1, respectively,
     * where it is known that x1 = x0 + d.
     * <p/>
     * Computes the scalar distance s needed to reach the estimated turning point at x1* = x0 + s*d.
     * 
     * @param g0 - The slope at x0.
     * @param g1 - The slope at x1 = x0 + d.
     * @param d - The directed distance, x1 - x0.
     * @return The optimal step-size, s.
     */
    public static double quadraticOptimumScaling(
            DataVector g0, DataVector g1, DataVector d)
    {
        /*
         * <p/>Consider a curve y=f(x), such that
         * y0=f(x0), g0=f'(x0), H0=f''(x0), etc.
         * Let x1=x0+d. Then (Taylor series)
         * f(x1) = f(x0)+f'(x0).(x1-x0)+1/2 f''(x0):(x1-x0)^2 + O(||x1-x0||^3),
         * => y1 = y0+g0.d+1/2*d.H0.d + O(||d||^3).
         * Also,
         * f'(x1) = f'(x0)+(x1-x0).f''(x0) + O(||x1-x0||^2).
         * => g1 = g0+d.H0 + O(||d||^2) => d.H0.d = (g1-g0).d + O(||d||^3).
         * Now, optimum occurs at x1*=x0+s*d with zero slope, i.e.
         * 0 = f'(x1*) = f'(x0)+(x1*-x0).f''(x0) + O(||x1*-x0||^2)
         * => 0 = g0+s*d.H0 + O(||d||^2)
         * => 0 = g0.d+s*d.H0.d + O(||d||^3)
         * => s = -g0.d/d.H0.d + O(||d||^3)
         * = -g0.d/(g1-g0).d + O(||d||^3).
         */
        double g0d = VectorFactory.dot(g0, d);
        double g1d = VectorFactory.dot(g1, d);
        return -g0d / (g1d - g0d); // NB: Invariant to scaling of d.
    }

    /**
     * Fits a quadratic curve through
     * two gradients, g0 and g1, at unknown
     * points x0 and x1, where it is known that x1=x0+d.
     * Estimates the directed distance d*
     * needed to reach the turning point
     * at x2*=x1+d*.
     * Note that d* is projected onto g1 if d and g1 are close,
     * otherwise d* is a scaling of d.
     * 
     * @param g0 - The slope at x0.
     * @param g1 - The slope at x1 = x0 + d.
     * @param d - The directed distance, x1 - x0.
     * @param c - The closeness measure, cos(theta_max).
     * @return The optimal directed distance, d*.
     */
    public static DataVector quadraticOptimumDisplacement(
            DataVector g0, DataVector g1, DataVector d, double c)
    {
        double g0d = VectorFactory.dot(g0, d);
        double g1d = VectorFactory.dot(g1, d);
        double s = -g0d / (g1d - g0d) - 1; // Allow for displacement to x1=x0+d.
        double g1n = g1.norm();
        if (Math.abs(g1d) > c * d.norm() * g1n) {
            // angle(g1,d) < theta_max; project onto g1.
            s *= g1d / (g1n * g1n);
            return VectorFactory.scale(g1, s);
        } else {
            return VectorFactory.scale(d, s);
        }
    }

    /**
     * Fits a cubic curve through two known function values,
     * y0 and y1, and
     * two known function gradients, g0 and g1, at unknown
     * points x0 and x1, respectively,
     * where it is known that x1 = x0 + r*d.
     * <p/>
     * Computes the scalar distance s needed to reach the estimated turning point at x1* = x0 + s*r*d.
     * 
     * @param y0 - The value f(x0).
     * @param g0 - The slope f'(x0).
     * @param y1 - The value f(x1).
     * @param g1 - The slope f'(x1).
     * @param r - The step-size.
     * @param d - The directed distance, (x1 - x0) / r.
     * @return The optimal step-size, s.
     */
    public static double cubicOptimumScaling(
            double y0, DataVector g0,
            double y1, DataVector g1,
            double r, DataVector d)
    {
        /*
         * <p/>Consider a curve y=f(x), such that
         * y0=f(x0), g0=f'(x0), H0=f''(x0), T0=f'''(x0), etc.
         * Let x1=x0+d. Then (Taylor series)
         * f(x1) = f(x0)+f'(x0).(x1-x0)
         * +1/2 f''(x0):(x1-x0)^2
         * +1/6 f'''(x0):(x1-x0)^3 + O(||x1-x0||^4),
         * => y1 = y0+g0.d+1/2*d.H0.d+1/6*T0:d^3 + O(||d||^4).
         * Also,
         * f'(x1) = f'(x0)+(x1-x0).f''(x0)
         * + 1/2 f'''(x):(x1-x0)^2 + O(||x1-x0||^3)
         * => g1 = g0+d.H0+1/2*T0:d^2 + O(||d||^3)
         * => d.H0.d = (g1-g0).d-1/2*T0:d^3 + O(||d||^4).
         * Hence, y1-y0 = 1/2*(g1+g0).d-1/12*T0:d^3 + O(||d||^4).
         * 
         * Now, let x1* = x0 + s*d, such that f'(x1*) = 0 and f''(x1*) < 0,
         * i.e. x1* is a local maximum turning point.
         * Then f'(x1*) = 0 = f'(x0) + (x1*-x0).f''(x0)
         * + 1/2 f'''(x0):(x1*-x0)^2 + O(||x1*-x0||^3)
         * => 0 = g0 + s*d.H0 + 1/2*s^2*T0:d^2 + O(||d||^3)
         * => 1/2*T0:d^3*s^2 + d.H0.d*s + g0.d = 0
         * => s = (-d.H0.d - sqrt{(d.H0.d)^2-2*T0:d^3*g0.d}) / T0:d^3.
         */
        double g0d = r * VectorFactory.dot(g0, d);
        double g1d = r * VectorFactory.dot(g1, d);
        double T0d3 = 6 * (g1d + g0d) - 3 * (y1 - y0);
        double H0d2 = (g1d - g0d) - 0.5 * T0d3;
        double D = H0d2 * H0d2 - 2 * T0d3 * g0d;
        return -(H0d2 + Math.sqrt(D)) / T0d3;
    }

    /**
     * Fits a quadratic curve through two points (u,f(u)) and (v,f(v)) with
     * known gradient f'(u).
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
     * @return The local turning point of the quadratic.
     */
    public static double quadraticOptimum(double u, double fu, double dfu, double v, double fv) {
        /*
         * Now, f(u) = au^2+bu+c; f(v) = av^2+bv+c
         * => f(v)-f(u) = a(v^2-u^2)+b(v-u) = a(v+u)(v-u)+b(v-u)
         * => [f(v)-f(u)]/(v-u) = a(v+u)+b.
         * Also, f'(u) = 2au+b
         * => [f(v)-f(u)]/(v-u) - f'(u) = a(v-u)
         * => a = {[f(v)-f(u)]/(v-u) - f'(u)}/(v-u).
         * And f'(u)/(2a) = u+b/(2a)
         * => x* = -b/(2a) = u-f'(u)/(2a).
         */
        final double delta = v - u;
        final double a = ((fv - fu) / delta - dfu) / delta;
        return u - 0.5 * dfu / a;
    }

    /**
     * Fits a quadratic curve through two x-values, u and v, with known
     * gradients f'(u) and f'(v).
     * 
     * @param u
     *            The value of one point, u.
     * @param dfu
     *            The value of f'(u).
     * @param v
     *            The value of another point, v.
     * @param dfv
     *            The value of f'(v).
     * @return The local turning point of the quadratic.
     */
    public static double quadraticOptimum(double u, double dfu, double v, double dfv) {
        /*
         * Now, f(u) = au^2+bu+c; f(v) = av^2+bv+c 
         * => f'(u) = 2au+b; f'(v) = 2av+b
         * => f'(v)-f'(u) = 2a(v-u)
         * => 2a = [f'(v)-f'(u)]/(v-u).
         * And f'(u)/(2a) = u+b/(2a)
         * => x* = -b/(2a) = u-f'(u)/(2a) = u-(v-u)*f'(u)/[f'(v)-f'(u)].
         */
        return u - (v - u) * dfu / (dfv - dfu);
    }

}
