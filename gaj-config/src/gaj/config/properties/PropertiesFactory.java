package gaj.config.properties;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import config.SerialisationFactory.InvalidSerialisationException;
import config.SerialisationFactory.Serialiser;
import config.SerialisationFactory.StringSerialiser;

/**
 * This factory deals with the specification and maintenance of
 * user-defined properties, e.g. key/value pairs.
 * There are two basic kinds of configuration:
 * <ol>
 * <li>A collection of properties;</li>
 * <li>A collection of groups of properties.</li>
 * </ol>
 * The guiding principle here is one of parsimony - use the simplest
 * representation possible that does not lose information. Thus:
 * <ol>
 * <li>A GroupedProperties configuration containing no defined groups
 * may be replaced by its global Properties.</li>
 * <li>An empty configuration may be replaced by null.</li>
 * <li>A defined group must continue to exist, even if its configuration is null.</li>
 * </ol>
 */
public class PropertiesFactory {

   /**
    * Creates a new instance of a properties configuration.
    * 
    * @return An empty container of properties.
    */
   public static <T> Properties<T> newProperties() {
      return new PropertiesImpl<T>();
   }

   /**
    * Creates a new instance of a grouped properties configuration.
    * 
    * @return An empty container of properties.
    */
   public static <T> GroupedProperties<T> newGroupedProperties() {
      return new GroupedPropertiesImpl<T>();
   }

   /**
    * Creates a new instance of properties configuration.
    * @param isGrouped - A boolean flag.
    * @return A GroupedProperties instance if isGrouped is true,
    * or a Properties instance if it is not.
    */
   public static <T> Configuration<T> newConfiguration(boolean isGrouped) {
      return isGrouped ? new GroupedPropertiesImpl<T>() : new PropertiesImpl<T>();
   }

   // Assumes neither collection is null or empty.
   private static void _addProperties(GroupedProperties toProperties, Configuration fromProperties) {
      if (fromProperties.isGrouped()) {
         GroupedProperties gproperties = (GroupedProperties)fromProperties;
         toProperties.addGlobalProperties(gproperties.getGlobalProperties());
         for (String groupName : gproperties.getGroupNames())
            toProperties.addGroupProperties(groupName, gproperties.getGroupProperties(groupName));
      } else {
         toProperties.addGlobalProperties((Properties)fromProperties);
      }
   }



   public static BufferedReader newBufferedReader(File file) throws IOException {
      FileInputStream fis = new FileInputStream(file);
      try {
         InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
         return new BufferedReader(isr);
      } catch (IOException e) {
         fis.close();
         throw e;
      }
   }

   public static BufferedWriter newBufferedWriter(File file) throws IOException {
      FileOutputStream fos = new FileOutputStream(file);
      try {
         OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
         return new BufferedWriter(osr);
      } catch (IOException e) {
         fos.close();
         throw e;
      }
   }

   /**
    * Creates a new instance of a properties manager, linked to a
    * default serialisation manager.
    * @return A new properties manager.
    */
   public static PropertiesManager newManager() {
      return new GroupedPropertiesManager(null, null);
   }

   /**
    * Creates a new instance of a properties manager, linked to a
    * copy of the given serialisation manager.
    * @param serialiser - A serialiser capable of serialising
    * and deserialising property values.
    * @return A new properties manager.
    */
   public static PropertiesManager newManager(Serialiser serialiser) {
      return new GroupedPropertiesManager(serialiser, null);
   }

   /**
    * Creates a new instance of a properties manager, linked to a
    * copy of the given serialisation manager.
    * @param serialiser - A serialiser capable of serialising
    * and deserialising property values.
    * @param translator - A group name translator.
    * @return A new properties manager.
    */
   public static PropertiesManager newManager(
         Serialiser serialiser, GroupNameTranslator translator)
   {
      return new GroupedPropertiesManager(serialiser, translator);
   }

   /**
    * Wraps the given properties manager with synchronised methods,
    * if necessary. If the given manager is already synchronised,
    * then it is not wrapped.
    * @param manager - The given properties manager.
    * @return A synchronised properties manager.
    */
   public static PropertiesManager synchroniseManager(final PropertiesManager manager) {
      if (manager.isSynchronised()) return manager;
      return new PropertiesManager() {

         public boolean isSynchronised() {
            return true;
         }

         public void load(File config) throws IOException {
            synchronized (manager) {
               manager.load(config);
            }
         }

         public void save(File config) throws IOException {
            synchronized (manager) {
               manager.save(config);
            }
         }

         public void add(Configuration config) {
            synchronized (manager) {
               manager.add(config);
            }
         }

         public boolean exists(String key) {
            synchronized (manager) {
               return manager.exists(key);
            }
         }

         public Object get(String key) {
            synchronized (manager) {
               return manager.get(key);
            }
         }

         public void set(String key, Object value) {
            synchronized (manager) {
               manager.set(key, value);
            }
         }

         public void remove(String key) {
            synchronized (manager) {
               manager.remove(key);
            }
         }

         public boolean exists(String[] groups, String key) {
            synchronized (manager) {
               return manager.exists(groups, key);
            }
         }

         public Object get(String[] groups, String key) {
            synchronized (manager) {
               return manager.get(groups, key);
            }
         }

         public void set(String[] groups, String key, Object value) {
            synchronized (manager) {
               manager.set(groups, key, value);
            }
         }

         public void remove(String[] groups, String key) {
            synchronized (manager) {
               manager.remove(groups, key);
            }
         }

         public Properties accumulate(String[] groups) {
            synchronized (manager) {
               return manager.accumulate(groups);
            }
         }

      };
   }

   /**
    * Traverses the grouped properties configuration looking
    * for nested groups with the given names. Halts when the last
    * named group is reached, or any group in the array of names is undefined.
    * @param parent - The root grouped properties configuration.
    * @param groups - An array of nested group names.
    * @param create - Indicates if each named group of properties
    * should be created if it do not already exist. This includes
    * converting Properties to GroupedProperties if necessary.
    * @return The last named properties configuration,
    * or a null value if any group in the chain is unreachable.
    */
   public static Configuration getNestedConfiguration(GroupedProperties parent, final String[] groups, boolean create) {
      if (groups == null || groups.length == 0) return parent;
      // Traverse all but last group.
      for (int i = 0; i < groups.length-1; i++) {
         String groupName = groups[i];
         Configuration config = parent.getGroupProperties(groupName, create, true);
         if (config == null) return null; // Group not found.
         if (!config.isGrouped()) {
            if (!create) return null; // Can't reach next group.
            // Convert properties to grouped properties.
            GroupedProperties group = newGroupedProperties();
            group.setGlobalProperties((Properties)config);
            parent.setGroupProperties(groupName, group);
            parent = group;
         } else {
            parent = (GroupedProperties)config;
         }
      }
      // Get last group.
      String groupName = groups[groups.length-1];
      Configuration config = parent.getGroupProperties(groupName, create, false);
      return config;
   }

   /**
    * Traverses the grouped properties configuration looking
    * for nested groups with the given names. Halts when the last
    * named group is reached, or any group in the array of names is undefined.
    * If the final group is a GroupedProperties, then only its global
    * properties configuration is returned.
    * @param parent - The root grouped properties configuration.
    * @param groups - An array of nested group names.
    * @param create - Indicates if each named group of properties
    * should be created if it do not already exist. This includes
    * converting Properties to GroupedProperties if necessary.
    * @return The last named group of properties (or its global properties
    * if it is a grouped properties configuration),
    * or a null value if any group in the chain is unreachable.
    */
   public static Properties getNestedProperties(GroupedProperties parent, final String[] groups, boolean create) {
      Configuration config = getNestedConfiguration(parent, groups, create);
      if (config == null) return null;
      return (config.isGrouped())
             ? ((GroupedProperties)config).getGlobalProperties(create)
             : (Properties)config;
   }

   public static Configuration mergeConfigurations(Configuration... configs) {
      if (configs == null || configs.length == 0) return null;
      GroupedProperties properties = newGroupedProperties();
      for (Configuration config : configs)
         properties.addProperties(config);
      return compressConfiguration(properties);
   }

}
