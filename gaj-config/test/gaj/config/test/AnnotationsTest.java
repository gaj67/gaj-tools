package gaj.config.test;

import static org.junit.Assert.*;
import gaj.config.annotations.Annotations;
import gaj.config.annotations.Configurable;
import gaj.config.annotations.Default;
import gaj.config.annotations.Getter;
import gaj.config.annotations.Property;
import gaj.config.annotations.Required;
import gaj.config.annotations.Setter;
import gaj.config.annotations.Singleton;

import org.junit.Test;

public class AnnotationsTest {

	@Singleton
	@Configurable
	private static class ConfigurableClass {
		@Required
		public Integer intField;
		
		@Property("string.field")
		public String stringField;
		
		@Property()
		@Default("null")
		public Object objField;
		
		@Getter
		public String getter() {
			return "";
		}
		
		@Setter
		public void setter(int value) {}
		
	}
	
	@Test
	public void testBasicAnnotations() throws NoSuchFieldException, SecurityException, NoSuchMethodException {
		assertTrue(Annotations.isConfigurable(ConfigurableClass.class));
		assertTrue(Annotations.isSingleton(ConfigurableClass.class));
		assertTrue(Annotations.isRequired(ConfigurableClass.class.getField("intField")));
		assertTrue(Annotations.isGetter(ConfigurableClass.class.getMethod("getter")));
		assertTrue(Annotations.isSetter(ConfigurableClass.class.getMethod("setter", int.class)));
		assertTrue(Annotations.isProperty(ConfigurableClass.class.getField("stringField")));
		assertTrue(Annotations.isProperty(ConfigurableClass.class.getField("objField")));
		assertEquals("string.field", Annotations.getKeyName(ConfigurableClass.class.getField("stringField")));
		assertNull(Annotations.getKeyName(ConfigurableClass.class.getField("objField")));
		assertEquals("null", Annotations.getDefaultValue(ConfigurableClass.class.getField("objField")));
		assertNull(Annotations.getDefaultValue(ConfigurableClass.class.getField("stringField")));
	}

}
