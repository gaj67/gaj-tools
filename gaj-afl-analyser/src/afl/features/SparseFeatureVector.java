package afl.features;



public class SparseFeatureVector implements FeatureVector {

	private final int length;
	private final int[] indices;
	private final double[] values;
	private int index_pos = 0;
	private int index = 0;

	/**
	 * Constructs a sparse representation of the features.
	 * @param features - A dense array of features.
	 */
	public SparseFeatureVector(final double[] features) {
		length = features.length;
		int numNonSparse = 0;
		for (int i = 0; i < length; i++)
			if (features[i] != 0.0) numNonSparse++;
		indices = new int[numNonSparse];
		values = new double[numNonSparse];
		int idx = 0;
		for (int i = 0; i < features.length; i++) {
			final double v = features[i];
			if (v != 0.0) {
				indices[idx] = i;
				values[idx++] = v;
			}
		}
		if (numNonSparse > 0) {
		   index_pos = 0;
		   index = indices[index_pos];
		}
	}

	/**
	 * Constructs a sparse representation of the features.
	 * @param indices - The array of sparse indices.
	 * @param values - The array of sparse values.
	 */
	public SparseFeatureVector(final int length, final int[] indices, final double[] values) {
		this.length = length;
		this.indices = indices;
		this.values = values;
		if (indices.length > 0) {
		   index_pos = 0;
		   index = indices[index_pos];
		}
	}

	public SparseFeatureVector(FeatureVector... features) {
		int length = 0;
		int sparseLen = 0;
		for (FeatureVector f : features) {
			length += f.length();
			if (f instanceof SparseFeatureVector) {
				sparseLen += ((SparseFeatureVector)f).values.length;
			} else {
				for (int i = 0; i < f.length(); i++)
					if (f.get(i) != 0.0) sparseLen++;
			}
		}
		this.length = length;
		this.indices = new int[sparseLen];
		this.values = new double[sparseLen];
		int idx = 0, offset = 0;
		for (FeatureVector f : features) {
			if (f.isSparse()) {
				SparseFeatureVector sf = (SparseFeatureVector) f;
				for (int i = 0; i < sf.values.length; i++) {
					this.indices[idx] = offset + sf.indices[i];
					this.values[idx++] = sf.values[i];
				}
			} else {
				for (int i = 0; i < f.length(); i++) {
					double v = f.get(i);
					if (v == 0.0) continue;
					this.indices[idx] = offset + i;
					this.values[idx++] = v;
				}
			}
			offset += f.length();
		}
		index_pos = 0;
		index = indices[index_pos];
	}

	/**
	 * Creates an all-zero feature vector.
	 * @param length - The full length of the equivalent
	 * dense feature vector.
	 */
	public SparseFeatureVector(int length) {
		this.length = length;
		indices = new int[0];
		values = new double[0];
	}

	public double get(int index) {
		/*
		for (int i = 0; i < indices.length; i++)
			if (indices[i] == index)
				return values[i];
			else if (indices[i] > index)
				break;
		return 0.0;
		*/
		if (index < 0 || index >= length)
			throw new ArrayIndexOutOfBoundsException(""+index);
		while (index > this.index && index_pos < indices.length-1)
			this.index = indices[++index_pos];
		if (index > this.index) return 0.0; // Sparse on right.
		while (index < this.index && index_pos > 0)
			this.index = indices[--index_pos];
		if (index == this.index) return values[index_pos];
		return 0.0; // Sparse region.
	}

	public boolean isSparse() {
		return true;
	}

	public int length() {
		return length;
	}

	public FeatureVector scale(double multiplier) {
		if (multiplier == 0.0)
			return new SparseFeatureVector(length);
		double[] newValues = new double[values.length];
		for (int i = 0; i < values.length; i++)
			newValues[i] = values[i] * multiplier;
		return new SparseFeatureVector(length, indices, newValues);
	}

	public double[] dense() {
		double[] features = new double[length];
		for (int i = 0; i < indices.length; i++)
			features[indices[i]] = values[i];
		return features;
	}

	public double dot(FeatureVector vector) {
		assert length == vector.length();
		double dot = 0.0;
		if (vector instanceof SparseFeatureVector) {
			SparseFeatureVector svector = (SparseFeatureVector)vector;
			int i1 = 0, i2 = 0;
			while (i1 < indices.length && i2 < svector.indices.length) {
				if (indices[i1] == svector.indices[i2])
					dot += values[i1++] * svector.values[i2++];
				else if (indices[i1] > svector.indices[i2])
					i2++;
				else
					i1++;
			}
		} else {
			final double[] features = vector.dense();
			for (int i = 0; i < indices.length; i++)
				dot += features[indices[i]] * values[i];
		}
		return dot;
	}

	public FeatureVector subtract(FeatureVector features) {
		if (length != features.length())
			throw new IllegalArgumentException(
				String.format("Expected length %d, got %d",
						      length, features.length()));
		if (features instanceof SparseFeatureVector) {
			SparseFeatureVector svec = (SparseFeatureVector)features;
			final int[] sindices = svec.indices;
			int common = 0, i1 = 0, i2 = 0;
			while (i1 < indices.length && i2 < sindices.length) {
				if (indices[i1] == sindices[i2]) {
					common++; i1++; i2++;
				} else if (indices[i1] < sindices[i2]) {
					i1++;
				} else {
					i2++;
				}
			}
			final int newLength = indices.length + sindices.length - common;
			final int[] newIndices = new int[newLength];
			final double[] newValues = new double[newLength];
			i1 = 0; i2 = 0;
			int i = 0;
			final double[] svalues = svec.values;
			while (i1 < indices.length && i2 < sindices.length) {
				if (indices[i1] == sindices[i2]) {
					newIndices[i] = indices[i1];
					newValues[i++] = values[i1++] - svalues[i2++];
				} else if (indices[i1] < sindices[i2]) {
					newIndices[i] = indices[i1];
					newValues[i++] = values[i1++];
				} else {
					newIndices[i] = sindices[i2];
					newValues[i++] = -svalues[i2++];
				}
			}
			for (; i1 < indices.length; i1++) {
				newIndices[i] = indices[i1];
				newValues[i++] = values[i1];
			}
			for (; i2 < sindices.length; i2++) {
				newIndices[i] = sindices[i2];
				newValues[i++] = -svalues[i2];
			}
			return new SparseFeatureVector(length, newIndices, newValues);
		} else {
			double[] newFeatures = dense();
			double[] otherFeatures = features.dense();
			for (int i = 0; i < length; i++)
				newFeatures[i] -= otherFeatures[i];
			return new DenseFeatureVector(newFeatures);
		}
	}

   public FeatureVector add(FeatureVector features) {
      if (length != features.length())
         throw new IllegalArgumentException(
            String.format("Expected length %d, got %d",
                        length, features.length()));
      if (features instanceof SparseFeatureVector) {
         SparseFeatureVector svec = (SparseFeatureVector)features;
         final int[] sindices = svec.indices;
         int common = 0, i1 = 0, i2 = 0;
         while (i1 < indices.length && i2 < sindices.length) {
            if (indices[i1] == sindices[i2]) {
               common++; i1++; i2++;
            } else if (indices[i1] < sindices[i2]) {
               i1++;
            } else {
               i2++;
            }
         }
         final int newLength = indices.length + sindices.length - common;
         final int[] newIndices = new int[newLength];
         final double[] newValues = new double[newLength];
         i1 = 0; i2 = 0;
         int i = 0;
         final double[] svalues = svec.values;
         while (i1 < indices.length && i2 < sindices.length) {
            if (indices[i1] == sindices[i2]) {
               newIndices[i] = indices[i1];
               newValues[i++] = values[i1++] + svalues[i2++];
            } else if (indices[i1] < sindices[i2]) {
               newIndices[i] = indices[i1];
               newValues[i++] = values[i1++];
            } else {
               newIndices[i] = sindices[i2];
               newValues[i++] = svalues[i2++];
            }
         }
         for (; i1 < indices.length; i1++) {
            newIndices[i] = indices[i1];
            newValues[i++] = values[i1];
         }
         for (; i2 < sindices.length; i2++) {
            newIndices[i] = sindices[i2];
            newValues[i++] = svalues[i2];
         }
         return new SparseFeatureVector(length, newIndices, newValues);
      } else {
         double[] newFeatures = dense();
         double[] otherFeatures = features.dense();
         for (int i = 0; i < length; i++)
            newFeatures[i] += otherFeatures[i];
         return new DenseFeatureVector(newFeatures);
      }
   }

   /**
    * @param length
    * @param index
    * @return A sparse feature vector consisting of a 1 at the given index,
    * with all the other values being zero.
    */
   public static FeatureVector oneOfN(int length, int index) {
      int[] indices = new int[1];
      indices[0] = index;
      double[] values = new double[1];
      values[0] = 1.0;
      return new SparseFeatureVector(length, indices, values);
   }

}
