package gaj.config.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import gaj.config.annotations.Annotations;

import org.junit.Test;

public class AnnotationsTest {

	@Test
	public void testBasicAnnotations() throws NoSuchFieldException, SecurityException, NoSuchMethodException {
		assertTrue(Annotations.isConfigurable(ConfigurableClass.class));
		assertTrue(Annotations.isSingleton(ConfigurableClass.class));
		assertTrue(Annotations.isRequired(ConfigurableClass.class.getField("intField")));
		assertTrue(Annotations.isGetter(ConfigurableClass.class.getMethod("getABCValue")));
		assertTrue(Annotations.isSetter(ConfigurableClass.class.getMethod("setter", int.class)));
		assertTrue(Annotations.isProperty(ConfigurableClass.class.getField("stringField")));
		assertTrue(Annotations.isProperty(ConfigurableClass.class.getField("objField")));
		assertTrue(Annotations.hasDefault(ConfigurableClass.class.getField("objField")));
		assertEquals("abc.value", Annotations.getKeyName(ConfigurableClass.class.getField("stringField")));
		assertNull(Annotations.getKeyName(ConfigurableClass.class.getField("objField")));
		assertEquals("null", Annotations.getDefaultValue(ConfigurableClass.class.getField("objField")));
		assertNull(Annotations.getDefaultValue(ConfigurableClass.class.getField("stringField")));
	}

}
