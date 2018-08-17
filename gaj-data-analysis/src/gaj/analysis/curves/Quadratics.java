package gaj.analysis.curves;

import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.numeric.vector.impl.VectorFactory;

/**
 * Provides methods for fitting, using and interpreting quadratic curves of the
 * form f(x) = ax^2+bx+c.
 */
public abstract class Quadratics {

    private Quadratics() {}

    /**
     * Fits the quadratic curve f(x)=ax^2+bx+c through two points (u,f(u)) and
     * (v,f(v)) with known gradient f'(u).
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
     * @return The [a,b,c] array of quadratic coefficients.
     */
    public static double[] quadraticFit(double u, double fu, double dfu, double v, double fv) {
        /*
         * The relevant equations are:
         *   f(u)  =  au^2 + bu + c
         *   f(v)  =  av^2 + bv + c
         *   f'(u) = 2au   + b
         * By the mean value theorem,
         *   f'(xi) = [f(v)-f(u)]/(v-u)
         *          = [a(v^2-u^2) + b(v-u)]/(v-u)
         *          = a(u+v) + b 
         * => [f'(xi)-f'(u)]/(v-u) = a
         * => f'(u) - 2au = b
         * => f(u) - au^2 - bu = c.
         */
        final double delta = v - u;
        final double dfxi = (fv - fu) / delta;
        final double a = (dfxi - dfu) / delta;
        final double b = dfu - 2 * a * u;
        final double c = fu - u * (a * u + b);
        return new double[]{a, b, c};
    }

    /**
     * Fits the quadratic curve f(x)=ax^2+bx+c through two x-values, u and v,
     * with known gradients f'(u) and f'(v).
     * 
     * @param u
     *            The value of one point, u.
     * @param dfu
     *            The value of f'(u).
     * @param v
     *            The value of another point, v.
     * @param dfv
     *            The value of f'(v).
     * @return The partial [a,b,0] array of quadratic coefficients.
     */
    public static double[] quadraticFit(double u, double dfu, double v, double dfv) {
        /*
         * The relevant equations are:
         *   f'(u)  =  2au + b
         *   f'(v)  =  2av + b
         * By the mean value theorem,
         *   f''(xi) = [f'(v)-f'(u)]/(v-u) = 2a
         * => f'(u) - f''(xi)u = b.
         */
        final double twoa = (dfv - dfu) / (v - u);
        final double b = dfu - twoa * u;
        return new double[] { 0.5*twoa, b, 0 };
    }

    /**
     * Fits the quadratic curve f(x)=ax^2+bx+c through two points (u,f(u)) and
     * (v,f(v)) with known gradient f'(u).
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
     * @return The local turning point, x* = -b/(2a), of the quadratic.
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
     * Fits the quadratic curve f(x)=ax^2+bx+c through two x-values, u and v,
     * with known gradients f'(u) and f'(v).
     * 
     * @param u
     *            The value of one point, u.
     * @param dfu
     *            The value of f'(u).
     * @param v
     *            The value of another point, v.
     * @param dfv
     *            The value of f'(v).
     * @return The local turning point, x* = -b/(2a), of the quadratic.
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
    public static double quadraticOptimumScaling(DataVector g0, DataVector g1, DataVector d) {
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
         *      = -g0.d/(g1-g0).d + O(||d||^3).
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
    public static DataVector quadraticOptimumDisplacement(DataVector g0, DataVector g1, DataVector d, double c) {
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

}
