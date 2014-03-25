package afl.features;

import gaj.afl.data.ShortMonth;
import gaj.afl.datatype.MatchDateTime;
import gaj.afl.datatype.MatchLocation;

/**
 * Encapsulates known information about the environment
 * in which the match was or is to be played, such as when
 * and where, and also any prevailing weather conditions and possibly
 * the condition of the field.
 * <p>Currently, the date-time and location 
 * of the match are encoded as a concatenation of 1-of-N vectors. 
 */
public class EnvironmentFeatures {
	
	public final FeatureVector features;

	public EnvironmentFeatures(MatchDateTime date, MatchLocation location) {
		final int llen = MatchLocation.values().length; 
  	    final int mlen = ShortMonth.values().length; // Might indicate weather.
		final int dlen = MatchDateTime.Day.values().length;
		final int tlen = 2; // Allow for time values.
		final int length = llen + mlen + dlen + tlen;
		/*
		final double[] features = new double[length];
		features[location.ordinal()] = 1;
		int offset = llen;
		features[offset+date.month.ordinal()] = 1;
		offset += mlen;
		features[offset+date.dayOfWeek.ordinal()] = 1;
		offset += dlen;
		if (date.time.endsWith("pm"))
			features[offset] = 1;
		String[] hr = date.time.split(":", 2);
		features[++offset] = (Integer.valueOf(hr[0]) % 12) / 11.0;
		this.features = new SparseFeatureVector(features);
		*/
		int[] indices = new int[5];
		double[] values = new double[5];
		indices[0] = location.ordinal();
		values[0] = 1;
		int offset = llen;
		indices[1] = offset + date.month.ordinal();
		values[1] = 1;
		offset += mlen;
		indices[2] = offset + date.dayOfWeek.ordinal();
		values[2] = 1;
		offset += dlen;
		indices[3] = offset++;
		if (date.time.endsWith("pm")) values[3] = 1;
		indices[4] = offset;
		String[] hr = date.time.split(":", 2);
		values[4] = (Integer.valueOf(hr[0]) % 12) / 11.0;
		this.features = new SparseFeatureVector(length, indices, values);
	}
	
}
