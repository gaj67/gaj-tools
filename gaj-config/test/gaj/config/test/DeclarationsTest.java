package gaj.config.test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import gaj.config.annotations.Configurable;
import gaj.config.annotations.Default;
import gaj.config.annotations.Getter;
import gaj.config.annotations.Property;
import gaj.config.annotations.Required;
import gaj.config.annotations.Setter;
import gaj.config.declaration.Declaration;
import gaj.config.declaration.DeclarationManager;
import gaj.config.declaration.DeclarationMap;
import gaj.config.declaration.Declarations;

import org.junit.Test;

public class DeclarationsTest {

	@Configurable
	private static class ConfigurableClass {

		public double nonPropertyField = 0;

		@Required
		public int intField = (int) nonPropertyField;

		@Property("abc.value")
		public String stringField;

		@Default("null")
		public Object objField;

		public int getInt() {
			return 0;
		}

		@Getter
		public String getABCValue() {
			return "";
		}

		@Setter("int.field")
		public void setter(int value) {
			value += getInt();
		}

	}

	@Test
	public void testFieldDeclarations() throws NoSuchFieldException, SecurityException {
		Field nonPropertyField = ConfigurableClass.class.getField("nonPropertyField");
		Declaration nonPropertyFieldDec = Declarations.getDeclaration(nonPropertyField);
		assertNull(nonPropertyFieldDec);

		Field intField = ConfigurableClass.class.getField("intField");
		Declaration intFieldDec = Declarations.getDeclaration(intField);
		assertNotNull(intFieldDec);
		assertNull(intFieldDec.getKey());
		assertEquals(intField, intFieldDec.getField());

		Field stringField = ConfigurableClass.class.getField("stringField");
		Declaration stringFieldDec = Declarations.getDeclaration(stringField);
		assertNotNull(stringFieldDec);
		assertEquals("abc.value", stringFieldDec.getKey());

		Field objField = ConfigurableClass.class.getField("objField");
		Declaration objFieldDec = Declarations.getDeclaration(objField);
		assertNotNull(objFieldDec);
		assertNull(objFieldDec.getKey());
		assertEquals("null", objFieldDec.getValue());
	}

	@Test
	public void testMethodDeclarations() throws SecurityException, NoSuchMethodException {
		Method getIntMethod = ConfigurableClass.class.getMethod("getInt");
		Declaration getIntMethodDec = Declarations.getDeclaration(getIntMethod);
		assertNull(getIntMethodDec);

		Method getABCValueMethod = ConfigurableClass.class.getMethod("getABCValue");
		Declaration getABCValueMethodDec = Declarations.getDeclaration(getABCValueMethod);
		assertNotNull(getABCValueMethodDec);
		assertNull(getABCValueMethodDec.getKey());
		assertEquals(getABCValueMethod, getABCValueMethodDec.getGetter());
		assertEquals(String.class, getABCValueMethodDec.getType());

		Method setterMethod = ConfigurableClass.class.getMethod("setter", int.class);
		Declaration setterMethodDec = Declarations.getDeclaration(setterMethod);
		assertNotNull(setterMethodDec);
		assertEquals("int.field", setterMethodDec.getKey());
		assertEquals(setterMethod, setterMethodDec.getSetter());
		assertEquals(int.class, setterMethodDec.getType());
	}

	@Test
	public void testTranslatedDeclarations() throws NoSuchFieldException, SecurityException, NoSuchMethodException {
		DeclarationManager manager = DeclarationManager.newInstance(".");
		Field intField = ConfigurableClass.class.getField("intField");
		Declaration intFieldDec = manager.getDeclaration(intField);
		assertNotNull(intFieldDec);
		assertEquals("int.field", intFieldDec.getKey());

		Method setterMethod = ConfigurableClass.class.getMethod("setter", int.class);
		Declaration setterMethodDec = manager.getDeclaration(setterMethod);
		assertNotNull(setterMethodDec);
		assertEquals("int.field", setterMethodDec.getKey());

		Field stringField = ConfigurableClass.class.getField("stringField");
		Declaration stringFieldDec = manager.getDeclaration(stringField);
		assertNotNull(stringFieldDec);
		assertEquals("abc.value", stringFieldDec.getKey());

		Method getABCValueMethod = ConfigurableClass.class.getMethod("getABCValue");
		Declaration getABCValueMethodDec = manager.getDeclaration(getABCValueMethod);
		assertNotNull(getABCValueMethodDec);
		assertEquals("abc.value", getABCValueMethodDec.getKey());
	}

	@Test
	public void testMergedDeclarations() {
		DeclarationManager manager = DeclarationManager.newInstance(".");
		DeclarationMap declarations = manager.getDeclarationMap(ConfigurableClass.class);
		assertEquals(3, declarations.numKeys());
		assertNotNull(declarations.getDeclaration("int.field"));
		assertNotNull(declarations.getDeclaration("abc.value"));
		assertNotNull(declarations.getDeclaration("obj.field"));
	}

}
