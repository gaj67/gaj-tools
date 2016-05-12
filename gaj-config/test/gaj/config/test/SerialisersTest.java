package gaj.config.test;

import static org.junit.Assert.*;
import gaj.config.serialisers.Serialiser;
import gaj.config.serialisers.Serialisers;

import org.junit.Test;

public class SerialisersTest {

	private static final String NULL_MARKER = "<null>";
	private static final Boolean[] BOOLEAN_VALUES = new Boolean[] {
		true, false
	};
	private static final Double[] DOUBLE_VALUES = new Double[] {
		0.0, 1.0, -3.141, 3141.59, 1e10
	};
	private static final Float[] FLOAT_VALUES = new Float[] {
		0.0f, 1.0f, -3.141f, 3141.59f, 1e5f
	};
	private static final Integer[] INTEGER_VALUES = new Integer[] {
		0, 1, -3, 3141, 100000
	};
	private static final Long[] LONG_VALUES = new Long[] {
		0L, 1L, -3L, 3141592658L, 100_000_000L
	};
	private static final Short[] SHORT_VALUES = new Short[] {
		0, 1, -3, 31415, 10000
	};
	private static final String[] STRING_VALUES = new String[] {
		"3.141592658", "sdldfiljdfljbnvbnsks", ""
	};

	@Test
	public void testSerialisers() {
		testSerialiserValues(Boolean.class, BOOLEAN_VALUES);
		testSerialiserValues(Double.class, DOUBLE_VALUES);
		testSerialiserValues(Float.class, FLOAT_VALUES);
		testSerialiserValues(Integer.class, INTEGER_VALUES);
		testSerialiserValues(Long.class, LONG_VALUES);
		testSerialiserValues(Short.class, SHORT_VALUES);
		testSerialiserValues(String.class, STRING_VALUES);
	}

	private <T> void testSerialiserValues(Class<T> type, T[] values) {
		Serialiser<T> serialiser = Serialisers.newSerialiser(type, NULL_MARKER);
		testGenericSerialiser(serialiser);
		for (T value : values) {
			assertEquals(value, serialiser.deserialise(serialiser.serialise(value)));
		}
	}

	private void testGenericSerialiser(Serialiser<?> serialiser) {
		assertNotNull(serialiser);
		assertEquals(NULL_MARKER, serialiser.serialise(null));
		assertNull(serialiser.deserialise(null));
		assertNull(serialiser.deserialise(NULL_MARKER));
	}

}
