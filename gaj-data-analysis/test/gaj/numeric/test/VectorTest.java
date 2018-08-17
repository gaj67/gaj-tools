package gaj.numeric.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.Iterator;
import org.junit.Test;
import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.numeric.vector.impl.VectorFactory;

public class VectorTest {

    private static boolean isEqual(double x, double y) {
        return Math.abs(x - y) < 1e-13;
    }

    @Test
    public void testVector() {
        {
            DataVector vec = VectorFactory.newSparseVector(13, 0, 1, 3, 2, 11, 3, 12, 4);
            assertEquals(13, vec.size());
            assertTrue(isEqual(1, vec.get(0)));
            assertTrue(isEqual(0, vec.get(1)));
            assertTrue(isEqual(2, vec.get(3)));
            assertTrue(isEqual(0, vec.get(5)));
            assertTrue(isEqual(3, vec.get(11)));
            assertTrue(isEqual(4, vec.get(12)));
        }
        {
            DataVector vec = VectorFactory.newSparseVector(13, 3, 2, 11, 3);
            assertEquals(13, vec.size());
            assertTrue(isEqual(0, vec.get(0)));
            assertTrue(isEqual(0, vec.get(1)));
            assertTrue(isEqual(2, vec.get(3)));
            assertTrue(isEqual(0, vec.get(5)));
            assertTrue(isEqual(3, vec.get(11)));
            assertTrue(isEqual(0, vec.get(12)));
        }
        {
            DataVector vec1 = VectorFactory.newSparseVector(13, 3, 2, 11, 3);
            DataVector vec2 = VectorFactory.newVector(0., 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 3, 0);
            assertEquals(13, vec2.size());
            for (int i = 0; i < 13; i++)
                assertTrue(isEqual(vec1.get(i), vec2.get(i)));
            Iterator<Double> iter1 = VectorFactory.getIterator(vec1);
            Iterator<Double> iter2 = VectorFactory.getIterator(vec2);
            for (int i = 0; i < 13; i++)
                assertTrue(isEqual(iter1.next(), iter2.next()));
            System.out.printf("vec1 = [ ");
            int i = 0;
            for (; i < vec1.size(); i++) {
                System.out.printf("%f ", vec1.get(i));
            }
            System.out.println("]");
            assertEquals(13, i);
        }
    }

    @Test
    public void testDenseVectorScaling() {
        System.out.println("Testing dense vector scaling...");
        {
            DataVector vec = VectorFactory.newVector(0, 1, 2, 3);
            DataVector vecTimes2 = VectorFactory.scale(vec, 2);
            DataVector vecTimes2Divide2 = VectorFactory.scale(vecTimes2, 0.5);
            VectorFactory.display("Original:", vec, "\n");
            VectorFactory.display("Reconstructed:", vecTimes2Divide2, "\n");
            for (int i = 0; i < vec.size(); i++)
                assertTrue(isEqual(vec.get(i), vecTimes2Divide2.get(i)));
            for (int i = 0; i < vec.size(); i++)
                assertTrue(isEqual(vec.get(i), vecTimes2Divide2.get(i)));
            for (int i = 0; i < vecTimes2Divide2.size(); i++)
                assertTrue(isEqual(vecTimes2Divide2.get(i), vec.get(i)));
            System.out.printf("Norms - original: %f,  reconstructed: %f%n", vec.norm(), vecTimes2Divide2.norm());
            assertTrue(isEqual(vec.norm(), vecTimes2Divide2.norm()));
            DataVector vec2 = VectorFactory.newVector(1, 2, 3, 4);
            assertTrue(isEqual(vec.dot(vec2), vecTimes2Divide2.dot(vec2)));
            assertTrue(isEqual(vec2.dot(vec), vec2.dot(vecTimes2Divide2)));
        }
        System.out.println("Tested dense vector scaling!");
    }

    @Test
    public void testSparseVectorScaling() {
        System.out.println("Testing sparse vector scaling...");
        {
            DataVector vec = VectorFactory.newSparseVector(4, 1, 1, 2, 2, 3, 3);
            DataVector vecTimes2 = VectorFactory.scale(vec, 2);
            DataVector vecTimes2Divide2 = VectorFactory.scale(vecTimes2, 0.5);
            VectorFactory.display("Original:", vec, "\n");
            VectorFactory.display("Reconstructed:", vecTimes2Divide2, "\n");
            for (int i = 0; i < vec.size(); i++)
                assertTrue(isEqual(vec.get(i), vecTimes2Divide2.get(i)));
            for (int i = 0; i < vec.size(); i++)
                assertTrue(isEqual(vec.get(i), vecTimes2Divide2.get(i)));
            for (int i = 0; i < vecTimes2Divide2.size(); i++)
                assertTrue(isEqual(vecTimes2Divide2.get(i), vec.get(i)));
            System.out.printf("Norms - original: %f,  reconstructed: %f%n", vec.norm(), vecTimes2Divide2.norm());
            assertTrue(isEqual(vec.norm(), vecTimes2Divide2.norm()));
            DataVector vec2 = VectorFactory.newVector(1, 2, 3, 4);
            assertTrue(isEqual(vec.dot(vec2), vecTimes2Divide2.dot(vec2)));
            assertTrue(isEqual(vec2.dot(vec), vec2.dot(vecTimes2Divide2)));
        }
        System.out.println("Tested sparse vector scaling!");
    }

    @Test
    public void testCompoundVectorScaling() {
        System.out.println("Testing compound vector scaling...");
        {
            DataVector vec1 = VectorFactory.newSparseVector(4, 1, 1, 2, 2, 3, 3);
            DataVector vec2 = VectorFactory.newVector(0, 1, 2, 3);
            DataVector vec = VectorFactory.concatenate(vec1, vec2);
            DataVector vecTimes2 = VectorFactory.scale(vec, 2);
            DataVector vecTimes2Divide2 = VectorFactory.scale(vecTimes2, 0.5);
            VectorFactory.display("Original:", vec, "\n");
            VectorFactory.display("Reconstructed:", vecTimes2Divide2, "\n");
            for (int i = 0; i < vec.size(); i++)
                assertTrue(isEqual(vec.get(i), vecTimes2Divide2.get(i)));
            for (int i = 0; i < vec.size(); i++)
                assertTrue(isEqual(vec.get(i), vecTimes2Divide2.get(i)));
            for (int i = 0; i < vecTimes2Divide2.size(); i++)
                assertTrue(isEqual(vecTimes2Divide2.get(i), vec.get(i)));
            System.out.printf("Norms - original: %f,  reconstructed: %f%n", vec.norm(), vecTimes2Divide2.norm());
            assertTrue(isEqual(vec.norm(), vecTimes2Divide2.norm()));
            DataVector vec3 = VectorFactory.newVector(1, 2, 3, 4, 5, 6, 7, 8);
            assertTrue(isEqual(vec.dot(vec3), vecTimes2Divide2.dot(vec3)));
            assertTrue(isEqual(vec3.dot(vec), vec3.dot(vecTimes2Divide2)));
        }
        System.out.println("Tested compound vector scaling!");
    }

}
