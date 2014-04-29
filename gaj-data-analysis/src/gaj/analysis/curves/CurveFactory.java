package gaj.analysis.curves;

import gaj.analysis.numeric.NumericFactory;
import gaj.data.numeric.DataObject;

public abstract class CurveFactory {

	private CurveFactory() {}
	
	/**
	 * Fits a quadratic curve through
	 * two gradients, g0 and g1, at unknown
	 * points x0 and x1=x0+r0*g0. 
	 * Estimates the scalar distance r
	 * needed to reach the turning point
	 * at x=x0+r*g0.
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
	 * 
	 * @param r0 - The initial gradient scaling.
	 * @param g0 - The slope at x0.
	 * @param g1 - The slope at x1=x0+r0*g0.
	 * @return The optimal step-size, r.
	 */
	public static double quadraticOptimum(
			double r0, DataObject g0, DataObject g1) 
	{
		double g0g0 = NumericFactory.dot(g0, g0);
		double g0g1 = NumericFactory.dot(g0, g1);
		return -r0*g0g0/(g0g1-g0g0);
	}

}
