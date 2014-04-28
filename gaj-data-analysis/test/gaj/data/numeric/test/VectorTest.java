package gaj.data.numeric.test;

import static org.junit.Assert.*;

import java.util.Iterator;

import gaj.analysis.vector.NumericDataFactory;
import gaj.data.vector.DataVector;

import org.junit.Test;

public class VectorTest {

	private static boolean isEqual(double x, double y) {
		return Math.abs(x - y) < 1e-13;
	}

	@Test
	public void testVector() {
		{
			DataVector vec = NumericDataFactory.newSparseVector(13, 0, 1, 3, 2, 11, 3, 12, 4);
			assertEquals(13, vec.length());
			assertTrue(isEqual(1, vec.get(0)));
			assertTrue(isEqual(0, vec.get(1)));
			assertTrue(isEqual(2, vec.get(3)));
			assertTrue(isEqual(0, vec.get(5)));
			assertTrue(isEqual(3, vec.get(11)));
			assertTrue(isEqual(4, vec.get(12)));
		}
		{
			DataVector vec = NumericDataFactory.newSparseVector(13, 3, 2, 11, 3);
			assertEquals(13, vec.length());
			assertTrue(isEqual(0, vec.get(0)));
			assertTrue(isEqual(0, vec.get(1)));
			assertTrue(isEqual(2, vec.get(3)));
			assertTrue(isEqual(0, vec.get(5)));
			assertTrue(isEqual(3, vec.get(11)));
			assertTrue(isEqual(0, vec.get(12)));
		}
		{
			DataVector vec1 = NumericDataFactory.newSparseVector(13, 3, 2, 11, 3);
			DataVector vec2 = NumericDataFactory.newDenseVector(0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 3, 0);
			assertEquals(13, vec2.length());
			for (int i = 0; i < 13; i++)
				assertTrue(isEqual(vec1.get(i), vec2.get(i)));
			Iterator<Double> iter1 = vec1.iterator();
			Iterator<Double> iter2 = vec2.iterator();
			for (int i = 0; i < 13; i++)
				assertTrue(isEqual(iter1.next(), iter2.next()));
			int i = 0;
			System.out.printf("vec1 = [ ");
			for (double value : vec1) {
				System.out.printf("%f ", value );
				i++;
			}
			System.out.println("]");
			assertEquals(13, i);
		}
	}

}
