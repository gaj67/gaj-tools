package gaj.config.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import gaj.config.annotations.Annotations;
import gaj.config.configurators.Configuration;
import gaj.config.configurators.Configurations;
import gaj.config.configurators.Configurator;
import gaj.config.configurators.Configurators;
import gaj.config.declaration.DeclarationManager;
import gaj.config.serialisers.MultiSerialisers;
import gaj.config.serialisers.Serialiser;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ConfiguratorsTest {

	private static final String STRING_VALUE = "This is a string";
	private static final int INT_VALUE = 10;
	private static final int INT_VALUE2 = -13;

	@SuppressWarnings("serial")
	private static Map<String/*key-name*/, String/*serialised-value*/> LOCAL_MAP = 
			new HashMap<String, String>() {{
				put("int.field", "" + INT_VALUE + "<int>");
				put("abc.value", STRING_VALUE);
				put("obj.field", "<null>");
			}};
			
	@SuppressWarnings("serial")
	private static Map<String/*key-name*/, String/*serialised-value*/> GLOBAL_MAP = 
			new HashMap<String, String>() {{
				put("fred.int.field", "" + INT_VALUE2 + "<int>");
			}};

	@Test
	public void testLocalConfigurator() {
		DeclarationManager manager = DeclarationManager.newInstance(".");
		Serialiser<Object> serialiser = MultiSerialisers.newMultiSerialiser(true);
		Configurator configurator = Configurators.newConfigurator(manager, serialiser);
		Configuration config = Configurations.newConfiguration(LOCAL_MAP);
		ConfigurableClass instance = configurator.configure(ConfigurableClass.class, config, false);
		assertEquals(STRING_VALUE, instance.getABCValue());
		assertEquals(INT_VALUE, instance.intField);
		assertNull(instance.objField);
	}

	@Test
	public void testGlobalConfigurator() {
		assertEquals("fred", Annotations.getKeyName(ConfigurableClass.class));
		DeclarationManager manager = DeclarationManager.newInstance(".");
		Serialiser<Object> serialiser = MultiSerialisers.newMultiSerialiser(true);
		Configurator configurator = Configurators.newConfigurator(manager, serialiser);
		Configuration config = Configurations.newConfiguration(GLOBAL_MAP);
		ConfigurableClass instance = configurator.configure(ConfigurableClass.class, config, true);
		assertEquals(INT_VALUE2, instance.intField);
	}

}
