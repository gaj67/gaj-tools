package gaj.text.test.loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CheckFreeDictionaryProperties {

	private static final String START_OF_BLOCK = "<div ";
	private static final String END_OF_BLOCK = "</div>";
	private static final String START_OF_CONTENT = "<div class=\"content-holder\">";
	private static final String START_OF_WORD = "<h1>";
	private static final String END_OF_WORD = "</h1>";
	private static final String START_OF_SECTION = "<section ";
	private static final String END_OF_SECTION = "</section>";
	private static final String START_OF_SECTION_TYPE = "data-src=";
	private static final String END_OF_SECTION_TYPE = ">";

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Arguments: <path-to-definition(s)>+");
		} else {
			for (String arg : args) {
				File path = new File(arg);
				if (path.isFile()) {
					analyseFile(path);
				} //TODO analyseDirectory(path);
			}
		}
	}

	private static void analyseFile(File file) {
		try {
			StringBuilder content = readContent(file);
			System.out.printf("Content=%s%n", content);
			String word = getWord(content);
			System.out.printf("Word=%s%n", word);
			List<String> sections = getSections(content);
			System.out.printf("Sections=%s%n", sections);
			for (String section : sections) {
				analyseSection(section);
			}
		} catch (IOException | RuntimeException e) {
			System.out.printf("Failed to analyse file: %s%n", file);
		}
	}

	private static void analyseSection(String section) {
		String sectionType = getSectionType(section);
		switch (sectionType) {
		case "hm":
			analyseHMSection(section);
			break;
		case "hc_dict":
			analyseHCSection(section);
			break;
		case "rHouse":
		case "ologies":
		case "hcUsage":
			// Ignore these sections.
			break;
		default:
			throw new IllegalStateException("Unknown section type: " + sectionType);
		}
	}

	private static void analyseHMSection(String section) {
		// TODO look for <div class="pseg">		
	}

	private static void analyseHCSection(String section) {
		// TODO look for <div>
	}

	private static String getSectionType(String section) {
		int sidx = section.indexOf(START_OF_SECTION_TYPE);
		if (sidx < 0) {
			throw new IllegalStateException("Unknown type of section");
		}
		sidx += START_OF_SECTION_TYPE.length();
		int eidx = section.indexOf(END_OF_SECTION_TYPE, sidx);
		if (eidx < 0) {
			throw new IllegalStateException("Missing end of section type");
		}
		if (section.charAt(sidx) == '"') sidx++;
		if (section.charAt(eidx - 1) == '"') eidx--;
		return section.substring(sidx, eidx);
	}

	private static List<String> getSections(StringBuilder content) {
		List<String> sections = new ArrayList<>();
		int eidx = -1;
		while (true) {
			int sidx = (eidx < 0) ? content.indexOf(START_OF_SECTION) 
					: content.indexOf(START_OF_SECTION, eidx);
			if (sidx < 0) break;
			eidx = content.indexOf(END_OF_SECTION, sidx + START_OF_SECTION.length());
			if (eidx < 0) {
				throw new IllegalStateException("Missing end of section");
			}
			eidx += END_OF_SECTION.length();
			sections.add(content.substring(sidx, eidx));
		}
		return sections;
	}

	private static String getWord(StringBuilder content) {
		int sidx = content.indexOf(START_OF_WORD);
		if (sidx >= 0) {
			sidx += START_OF_WORD.length();
			int eidx = content.indexOf(END_OF_WORD, sidx);
			if (eidx >= 0) {
				return content.substring(sidx, eidx);
			}
		}
		throw new IllegalStateException("Unable to locate word");
	}

	private static StringBuilder readContent(File file) throws IOException {
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			while (true) {
				String line = in.readLine();
				if (line == null) {
					throw new IllegalStateException("Unexpected end of file");
				}
				if (line.indexOf(START_OF_CONTENT) >= 0) {
					return getContent(in, line);
				}
			}
		}

	}

	private static StringBuilder getContent(BufferedReader in, String line) throws IOException {
		StringBuilder buf = new StringBuilder();
		int blockCounter = 0;
		while (true) {
			int idx = line.indexOf(START_OF_BLOCK);
			while (idx >= 0) {
				blockCounter++;
				idx += START_OF_BLOCK.length();
				idx = line.indexOf(START_OF_BLOCK, idx);
			}
			idx = line.indexOf(END_OF_BLOCK);
			while (idx >= 0) {
				blockCounter--;
				idx += END_OF_BLOCK.length();
				idx = line.indexOf(END_OF_BLOCK, idx);
			}
			buf.append(line.trim());
			if (blockCounter <= 0) return buf;
			line = in.readLine();
			if (line == null) {
				throw new IllegalStateException("Unexpected end of file");
			}
		}
	}

}
