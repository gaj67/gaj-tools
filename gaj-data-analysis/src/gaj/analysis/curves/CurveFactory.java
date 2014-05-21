package gaj.analysis.curves;

import gaj.analysis.numeric.NumericFactory;
import gaj.data.numeric.DataObject;

public abstract class CurveFactory {

	private CurveFactory() {}

	/**
	 * Fits a quadratic curve through
	 * two known gradients, g0 and g1, at unknown
	 * points x0 and x1, respectively, 
	 * where it is known that x1 = x0 + d. 
	 * <p/>Computes the scalar distance r
	 * needed to reach the estimated turning point
	 * at x1* = x0 + r*d.
	 * 
	 * @param g0 - The slope at x0.
	 * @param g1 - The slope at x1 = x0 + d.
	 * @param d - The directed distance, x1 - x0.
	 * @return The optimal step-size, r.
	 */
	public static double quadraticOptimum(
			DataObject g0, DataObject g1, DataObject d) 
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
		 * Now, optimum occurs at x1*=x0+r*d with zero slope, i.e.
		 *    0 = f'(x1*) = f'(x0)+(x1*-x0).f''(x0) + O(||x1*-x0||^2)
		 * => 0 = g0+r*d.H0 + O(||d||^2) 
		 * => 0 = g0.d+r*d.H0.d + O(||d||^3)
		 * => r = -g0.d/d.H0.d + O(||d||^3) 
		 *     = -g0.d/(g1-g0).d + O(||d||^3).
		 */
		double g0d = NumericFactory.dot(g0, d);
		double g1d = NumericFactory.dot(g1, d);
		return -g0d / (g1d - g0d);
	}


	/**
	 * Fits a quadratic curve through
	 * two gradients, g0 and g1, at unknown
	 * points x0 and x1=x0+r0*g0. 
	 * Estimates the scalar distance r
	 * needed to reach the turning point
	 * at x=x0+r*g0.
	 * 
	 * @param r0 - The initial gradient scaling.
	 * @param g0 - The slope at x0.
	 * @param g1 - The slope at x1=x0+r0*g0.
	 * @return The optimal step-size, r.
	 */
	public static double quadraticOptimum(
			double r0, DataObject g0, DataObject g1) 
	{
		/*
		 * <p/>Consider a curve y=f(x), such that
		 * y0=f(x0), g0=f'(x0), H0=f''(x0), etc.
		 * Let x1=x0+r0*g0. Then (Taylor series)
		 * f(x1) = f(x0)+f'(x0).(x1-x0)+1/2 f''(x0):(x1-x0)^2,
		 * => y1 = y0+r0*g0.g0+1/2*r0^2*g0.H0.g0.
		 * Also, 
		 * f'(x1) = f'(x0)+f''(x0).(x1-x0)
		 * => g1 = g0+r0*H0.g0 => g0.H0.g0 = g0.(g1-g0)/r0.
		 * Now, optimum occurs at x=x0+r*g0 with 0 slope, i.e.
		 *    0 = f'(x) = f'(x0)+f''(x0).(x-x0)
		 * => 0 = g0+r*H0.g0 
		 * => r = -g0.g0/g0.H0.g0 
		 *     = -r0*g0.g0/g0.(g1-g0).
		 */
		double g0g0 = NumericFactory.dot(g0, g0);
		double g0g1 = NumericFactory.dot(g0, g1);
		return -r0 * g0g0 / (g0g1 - g0g0);
	}

	/**
	 * Fits a quadratic curve through
	 * two gradients, g0 and g1, at unknown
	 * points x0 and x1=x0+r0*g0. 
	 * Estimates the scalar distance r
	 * needed to reach the turning point
	 * at x=x1+r*g1.
	 * 
	 * @param r0 - The initial gradient scaling.
	 * @param g0 - The slope at x0.
	 * @param g1 - The slope at x1=x0+r0*g0.
	 * @return The optimal step-size, r.
	 */
	public static double quadraticOptimum2(
			double r0, DataObject g0, DataObject g1) 
	{
		 /* Compute distance from x0 along g0
		 * (see quadraticOptimum), then
		 * correct for distance from x1=x0+r0*g0 along g0,
		 * then project onto g1.
		 * => r1 = (r-r0)*g0.g1/g1.g1
		 *       = [-r0*g0.g0/g0.(g1-g0)-r0]*g0.g1/g1.g1
		 *       = -r0 * g0.g1*g0.g1 / g0.(g1-g0)*g1.g1.
		 */
		double g0g0 = NumericFactory.dot(g0, g0);
		double g0g1 = NumericFactory.dot(g0, g1);
		double g1g1 = NumericFactory.dot(g1, g1);
		return - r0 * g0g1 * g0g1 / (g1g1 * (g0g1 - g0g0));
	}

}
