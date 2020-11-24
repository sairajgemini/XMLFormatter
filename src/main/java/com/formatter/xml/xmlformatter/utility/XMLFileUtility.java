package com.formatter.xml.xmlformatter.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author saikatgupta
 * This Singleton Utility class will contain all utility methods of XML formatting
 */
public final class XMLFileUtility implements Serializable {

    static final String ATTR_EXTRACTION_REGEX = "(?<==)([^\\s\"<>=]+)";
    static final String ELEMENT_NEW_LINE_REGEX = "((?<=<\\/)\\w+(>))";
    static final String REPLACEMENT = "\"$1\"";
    static final String NEW_LINE_REPLACEMENT = "$1\n";
    static final String SLASH_PATTERN_REPLACEMENT = "\"/";

    static final Pattern ATTR_VALUE_SLASH_PATTERN_REGEX = Pattern.compile("(?<=\")(\\w+)(\\/\")");

    protected XMLFileUtility() {
    }

    public static String getXmlFilePath(final boolean isSrcFile) {
        Scanner in = new Scanner(System.in);
        if (isSrcFile) {
            System.out.print("Source XML filename: ");
        } else {
            System.out.print("Enter destination filename: ");
        }
        return in.nextLine();
    }

    public static Path writeToFile(final String src, final Path dest) throws IOException {
        try {
            // read all lines from source Path
            List<String> lines = Files.readAllLines(Paths.get(src));
            StringBuilder xmlData = new StringBuilder();
            // write all lines to the destination Path
            for (String line : lines) {
                if (!line.isEmpty()) {
                    xmlData.append(line);
                }
            }
            String xmlOutput = XMLFormatterUtility.formatXml(xmlData);
            Files.write(dest, xmlOutput.getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return dest;
    }

    public static final class XMLFormatterUtility {
        // This REGEX searches all xml attributes without quotes
        static final Logger LOGGER = LoggerFactory.getLogger(XMLFormatterUtility.class);

        private XMLFormatterUtility() {
        }

        public static String formatXml(final StringBuilder currentLine) {
            String xmlOutput = currentLine.toString();
            xmlOutput = xmlOutput.replaceAll(ATTR_EXTRACTION_REGEX, REPLACEMENT).trim()
                    .replaceAll(ELEMENT_NEW_LINE_REGEX, NEW_LINE_REPLACEMENT);

            xmlOutput = replaceAllValueInMatcherGroup(xmlOutput, ATTR_VALUE_SLASH_PATTERN_REGEX, 2,
                    SLASH_PATTERN_REPLACEMENT);
            return xmlOutput;
        }

        public static String replaceAllValueInMatcherGroup(String xmlOutput, Pattern pattern, int groupVal,
                                                           String replacement) {
            Matcher slashMatcher = pattern.matcher(xmlOutput);
            if (slashMatcher.find()) {
                String group = slashMatcher.group(groupVal);
                xmlOutput = xmlOutput.replaceAll(group, replacement);
            }
            return xmlOutput;
        }

        public static void formatXMLFile() throws IOException {
            String srcFilePath = XMLFileUtility.getXmlFilePath(true);
            String destPath = XMLFileUtility.getXmlFilePath(false);
            Path destFile = Paths.get(destPath);
            if (Files.exists(destFile)) {
                Files.delete(destFile);
                LOGGER.info("Earlier destination file {} deleted successfully.", destPath);
            }

            destFile = Files.createFile(Paths.get(destPath));
            XMLFileUtility.writeToFile(srcFilePath, destFile);
        }
    }
}