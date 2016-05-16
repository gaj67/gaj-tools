package gaj.config.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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

	@SuppressWarnings("serial")
	private static Map<String/*key-name*/, String/*serialised-value*/> MAP = 
			new HashMap<String, String>() {{
				put("int.field", "" + INT_VALUE + "<int>");
				put("abc.value", STRING_VALUE);
				put("obj.field", "<null>");
			}};
			
	@Test
	public void testConfigurator() {
		DeclarationManager manager = DeclarationManager.newInstance(".");
		Serialiser<Object> serialiser = MultiSerialisers.newMultiSerialiser(true);
		Configurator configurator = Configurators.newConfigurator(manager, serialiser);
		Configuration config = Configurations.newConfiguration(MAP);
		ConfigurableClass instance = configurator.configure(ConfigurableClass.class, config);
		assertEquals(STRING_VALUE, instance.getABCValue());
		assertEquals(INT_VALUE, instance.intField);
		assertNull(instance.objField);
	}

}
