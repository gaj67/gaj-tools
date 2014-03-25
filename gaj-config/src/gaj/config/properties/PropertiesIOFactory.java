package gaj.config.properties;

import gaj.config.serial.Serialiser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This factory deals with the reading and writing of
 * properties to and from IO, with the aid of a serialiser.
 */
public class PropertiesIOFactory<T> {

	private static final int READ_AHEAD_LIMIT = 1024;
	public static final char GROUP_NAME_PREFIX = '[';
	public static final char GROUP_NAME_SUFFIX = ']';
 	public static final String[] COMMENT_MARKERS = { "#", "//" };
	public static final String PROPERTY_SEPARATOR = "=";
	public static final String ENCODED_PROPERTY_SEPARATOR = "%3D";

	private final Serialiser<T> serialiser;
	private final /*@Nullable*/ GroupNameTranslator translator;

	private PropertiesIOFactory(Serialiser<T> serialiser, /*@Nullable*/ GroupNameTranslator translator) {
		this.serialiser = serialiser;
		this.translator = translator;
	}

	/**
	 * Creates a factory instance capable of dealing with non-grouped properties.
	 * 
	 * @param serialiser - A serialiser capable of serialising and deserialising
	 * properties values.
	 */
	public static <T> PropertiesIOFactory<T> newInstance(Serialiser<T> serialiser) {
		return new PropertiesIOFactory<T>(serialiser, null);
	}

	/**
	 * Creates a factory instance capable of dealing with both grouped and
	 * non-grouped properties.
	 * 
	 * @param serialiser - A serialiser capable of serialising and deserialising
	 * properties values.
	 * @param translator - A group-name translator, for dynamically
	 * translating each property group into nested groups.
	 */
	public static <T> PropertiesIOFactory<T> newInstance(Serialiser<T> serialiser, GroupNameTranslator translator) {
		return new PropertiesIOFactory<T>(serialiser, translator);
	}

	/**
	 * Encodes the given string in a suitable manner for it to
	 * be exported as text.
	 * 
	 * @param key - The string to be encoded.
	 * @return The encoded string.
	 */
	public static String encode(String key) {
		// TODO Encode leading and trailing blanks.
		return key.replace(PROPERTY_SEPARATOR, ENCODED_PROPERTY_SEPARATOR);
	}

	/**
	 * Decodes the given string in a suitable manner for it to
	 * be imported from text.
	 * 
	 * @param key - The string to be decoded.
	 * @return The decoded string.
	 */
	public static String decode(String key) {
		// TODO Decode leading and trailing blanks.
		return key.replace(ENCODED_PROPERTY_SEPARATOR, PROPERTY_SEPARATOR);
	}

	/**
	 * Reads ungrouped properties from the specified source and returns them
	 * as a collection of properties.
	 * 
	 * @param source - A buffered reader for the properties configuration, which is
	 * neither opened nor closed by this method.
	 * @return A new instance of properties configuration, with keys and values
	 * loaded from the specified source.
	 * @throws IOException if the source is unreadable.
	 * @throws InvalidPropertiesException if the contents of the configuration
	 * are invalid. In this case, the line causing the failure will
	 * be restored to the buffer.
	 */
	public Properties<T> readProperties(BufferedReader source) throws IOException
	{
		Properties<T> properties = new PropertiesImpl<T>();
		loop:
			while (true) {
				source.mark(READ_AHEAD_LIMIT);
				String currentLine = source.readLine();
				if (currentLine == null) break; // End-of-file.
				String text = currentLine.trim();
				if (text.isEmpty()) continue; // Ignore empty or blank lines.
				for (String commentMarker : COMMENT_MARKERS)
					if (text.startsWith(commentMarker)) continue loop; // Ignore comments.
				if (text.charAt(0) == GROUP_NAME_PREFIX) {
					// Not the right scope to handle these markers.
					source.reset(); // Restore buffer.
					throw new UnexpectedGroupPropertiesException("Not a property: " + text);
				}
				int idx = text.indexOf(PROPERTY_SEPARATOR);
				if (idx < 0) {
					// Cannot find separator.
					source.reset(); // Restore buffer.
					throw new InvalidPropertiesException("Not a property: " + text);
				}
				String key = text.substring(0, idx);
				String value = text.substring(idx+1);
				properties.set(
						decode(key),
						serialiser.deserialise(decode(value)));
			}
		return properties;
	}

	/**
	 * Writes properties from the given configuration to the specified
	 * destination.
	 * 
	 * @param dest - A buffered writer for the configuration, which is
	 * neither opened nor closed by this method.
	 * @param properties - The configuration to be saved.
	 * @throws IOException if the configuration file is unwritable.
	 */
	public void writeProperties(BufferedWriter dest, Properties<T> properties) throws IOException {
		for (String key : properties.getKeys()) {
			dest.append(encode(key));
			dest.append(PROPERTY_SEPARATOR);
			T value = properties.get(key);
			dest.append(encode(serialiser.serialise(value)));
			dest.newLine();
		}
	}

	/**
	 * Reads properties from the specified configuration and returns them
	 * as a collection of properties.
	 * 
	 * @param source - A buffered reader for the configuration, which is
	 * neither opened nor closed by this method.
	 * @throws IOException if the configuration file is unreadable.
	 * @throws InvalidPropertiesException if the contents of the configuration
	 * file are invalid. In this case, the line causing the failure will
	 * be restored to the buffer.
	 * @return A collection of grouped properties.
	 */
	public GroupedProperties<T> readGroupedProperties(BufferedReader source) throws IOException {
		if (translator == null)
			throw new InvalidPropertiesException("A group-name translator is required");
		GroupedProperties<T> rootProperties = new GroupedPropertiesImpl<>();
		GroupedProperties<T> groupProperties = rootProperties;
		LinkedList<GroupedProperties<T>> ancestors = new LinkedList<>();
		while (true) {
			// Load optional global properties.
			Properties<T> globalProperties = null;
			try {
				globalProperties = readProperties(source);
				break; // End-of-file.
			} catch (UnexpectedGroupPropertiesException e) {
				// Error could be due to group properties definition - keep going.
			} finally {
				if (globalProperties != null && !globalProperties.isEmpty())
					groupProperties.set(translator.getGlobalGroupName(), globalProperties);
			}

			// Test for group properties definition.
			source.mark(READ_AHEAD_LIMIT);
			String text = source.readLine().trim();
			AtomicInteger groupDepth = new AtomicInteger(0);
			String groupName;
			int depthDiff;
			try {
				if (text.charAt(0) != GROUP_NAME_PREFIX)
					throw new InvalidPropertiesException("Expected valid property or group name: " + text);
				groupName = parseGroupName(text, groupDepth);
				depthDiff = groupDepth.get() - ancestors.size();
				if (depthDiff > 1)
					throw new InvalidPropertiesException("Nested depth too great: " + text);
			} catch (InvalidPropertiesException e) {
				source.reset();
				throw e;
			}

			// Load named group properties at required depth.
			// Either: Same depth - Sibling of current group, so get parent;
			// Or: Decreased depth: get ancestor of current group;
			// Or: Increased depth: create child of current group.
			while (depthDiff++ <= 0)
				groupProperties = ancestors.removeLast();
			// Create child of current group.
			String[] groups = translator.getNestedGroupNames(groupName);
			GroupedProperties<T> newProperties = getNestedGroupedProperties(groupProperties, groups, true);
			ancestors.addLast(groupProperties);
			groupProperties = newProperties;
		}
		return rootProperties;
	}

	private static String parseGroupName(String text, AtomicInteger groupDepth) {
		int nameStart = 0;
		final int length = text.length();
		while (nameStart < length && text.charAt(nameStart) == GROUP_NAME_PREFIX)
			nameStart++;
		String errMessage = "Invalid group name definition: " + text;
		if (nameStart >= length)
			throw new InvalidPropertiesException(errMessage);
		groupDepth.set(nameStart);
		int nameEnd = text.indexOf(GROUP_NAME_SUFFIX);
		if (nameEnd < 0)
			throw new InvalidPropertiesException(errMessage);
		if (nameStart != length - nameEnd)
			throw new InvalidPropertiesException(errMessage);
		String groupName = text.substring(nameStart, nameEnd).trim();
		if (groupName.isEmpty())
			throw new InvalidPropertiesException(errMessage);
		return groupName;
	}

	/**
	 * Traverses the grouped properties configuration looking
	 * for nested groups with the given names. Halts when the last
	 * named group is reached, or any group in the array of names is undefined.
	 * 
	 * @param parent - The root grouped properties configuration.
	 * @param groups - An array of nested group names.
	 * @param create - Indicates if each named group of properties
	 * should be created if it do not already exist. This includes
	 * converting Properties to GroupedProperties if necessary.
	 * @return The last named group of properties,
	 * or a null value if any group in the chain is unreachable.
	 */
	private GroupedProperties<T> getNestedGroupedProperties(GroupedProperties<T> parent, final String[] groups, boolean create) {
		// Traverse all groups.
		for (int i = 0; i < groups.length; i++) {
			String groupName = groups[i];
			Configuration<T> config = create ? parent.get(groupName, true) : parent.get(groupName);
			if (config == null) return null; // Group not found.
			if (!config.isGrouped()) {
				if (!create) return null; // Can't reach next group.
				// Convert properties to grouped properties.
				GroupedProperties<T> group = new GroupedPropertiesImpl<>();
				group.set(translator.getGlobalGroupName(), config);
				parent.set(groupName, group);
				parent = group;
			} else {
				parent = (GroupedProperties<T>)config;
			}
		}
		return parent;
	}

	/**
	 * Writes properties from the given configuration to the specified
	 * destination.
	 * 
	 * @param dest - A buffered writer for the configuration, which is
	 * neither opened nor closed by this method.
	 * @param properties - The configuration to be saved.
	 * @throws IOException if the configuration file is unwritable.
	 */
	public void writeGroupedProperties(BufferedWriter dest, GroupedProperties<T> properties) throws IOException {
		if (translator == null)
			throw new InvalidPropertiesException("A group-name translator is required");
		if (!properties.isEmpty())
			_writeGroupedProperties(dest, properties, 0);
	}

	private void _writeGroupedProperties(BufferedWriter dest, GroupedProperties<T> properties, int depth) throws IOException {
		Configuration<T> globalProperties = properties.get(translator.getGlobalGroupName());
		if (globalProperties != null && !globalProperties.isEmpty()) {
			_writeConfiguration(dest, globalProperties, depth);
		}
		depth++;
		for (String groupName : properties.getGroupNames()) {
			for (int i = 0; i < depth; i++) dest.append(GROUP_NAME_PREFIX);
			dest.append(groupName);
			for (int i = 0; i < depth; i++) dest.append(GROUP_NAME_SUFFIX);
			dest.newLine();
			Configuration<T> groupProperties = properties.get(groupName);
			if (groupProperties != null && !groupProperties.isEmpty())
				_writeConfiguration(dest, groupProperties, depth);
		}
	}

	private void _writeConfiguration(BufferedWriter dest, Configuration<T> properties, int depth) throws IOException {
		if (properties.isGrouped()) {
			_writeGroupedProperties(dest, (GroupedProperties<T>)properties, depth);
		} else {
			writeProperties(dest, (Properties<T>)properties);
		}
	}

	/**
	 * Writes properties from the given configuration to the specified
	 * destination.
	 * 
	 * @param dest - A buffered writer for the configuration, which is
	 * neither opened nor closed by this method.
	 * @param properties - The configuration to be saved.
	 * @throws IOException if the configuration file is unwritable.
	 */
	public void writeConfiguration(BufferedWriter dest, Configuration<T> properties) throws IOException {
		if (translator == null)
			throw new InvalidPropertiesException("A group-name translator is required");
		if (!properties.isEmpty())
			_writeConfiguration(dest, properties, 0);
	}

	/**
	 * Reads properties from the specified configuration and returns them
	 * as a collection of properties.
	 * 
	 * @param source - A buffered reader for the configuration file, which is
	 * neither opened nor closed by this method.
	 * @throws IOException if the configuration file is unreadable.
	 * @throws InvalidPropertiesException if the contents of the configuration
	 * file are invalid. In this case, the line causing the failure will
	 * be restored to the buffer.
	 * @return A collection of properties.
	 */
	public Configuration<T> readConfiguration(BufferedReader source) throws IOException {
		GroupedProperties<T> properties = readGroupedProperties(source);
		return compressConfiguration(properties);
	}

	/**
	 * Compresses the configuration structure by replacing empty containers
	 * with null, and reducing GroupedProperties with no groups to
	 * the global Properties.
	 * 
	 * @param properties - The collection of properties that will be modified.
	 * @return The simplest version of the supplied configuration which
	 * loses no information. The supplied configuration may be modified
	 * in the process, even if a different configuration instance is returned.
	 */
	private Configuration<T> compressConfiguration(/*@Nullable*/ Configuration<T> properties) {
		if (properties == null || properties.isEmpty()) return null;
		if (!properties.isGrouped()) return properties;
		GroupedProperties<T> groupedProperties = (GroupedProperties<T>)properties;
		Collection<String> groupNames = groupedProperties.getGroupNames();
		for (String groupName : groupNames)
			groupedProperties.set(
					groupName,
					compressConfiguration(groupedProperties.get(groupName)						)
			);
		if (groupNames.size() == 1 && groupNames.iterator().next().equals(translator.getGlobalGroupName()))
			return groupedProperties.get(translator.getGlobalGroupName());
		return groupedProperties;
	}
}
