package gaj.data.numeric;

/**
 * Marks a vector as being neither sparse nor dense, but something in between.
 */
public interface CompoundVector extends DataVector {
	
	/**
	 * Scales the compound vector by the given non-zero multiplier.
	 * 
	 * @param multiplier - The multiplier.
	 * @return The scaled compound vector.
	 */
	@Override
	CompoundVector scale(double multiplier);

}
