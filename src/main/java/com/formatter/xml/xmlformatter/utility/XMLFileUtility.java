package com.formatter.xml.xmlformatter.utility;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * @author saikatgupta
 * This Singleton Utility class will contain all utility methods of XML formatting
 */
public final class XMLFileUtility implements Serializable {
    protected XMLFileUtility() {
    }

    public static final String getXmlFilePath(boolean isSrcFile) {
        Scanner in = new Scanner(System.in);
        if(isSrcFile) {
            System.out.println("Source XML filename: ");
        } else {
            System.out.println("Enter destination filename: ");
        }
        return in.nextLine();
    }

    public static final Path writeToFile(String src, Path dest) throws IOException {
        try {
            // read all lines from source Path
            List<String> lines = Files.readAllLines(Paths.get(src));
            StringBuilder currentLine = new StringBuilder();
            // write all lines to the destination Path
            for (String line : lines) {
                if(!line.isEmpty()) {
                    currentLine.append(XMLFormatterUtility.findAndFormatXmlInput(line));
                }
            }
            Files.write(dest, currentLine.toString().getBytes());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return dest;
    }

    public static class XMLFormatterUtility {
        static final String ATTR_EXTRACTION_REGEX = "(?<==)([^\\s\"<>]+)";
        static final String ACTIVE_BINARY_START_TAG = "<ActiveBinary>";
        static final String ACTIVE_BINARY_END_TAG = "</ActiveBinary>";
        static final String REPLACEMENT = "\"$1\"";

        private XMLFormatterUtility() {}

        public static String surroundAttrWithQuotes(final String attrValue) {
            return "\"" + attrValue + "\"";
        }

        public static String findAndFormatXmlInput(String xmlLineInput) {
            xmlLineInput = xmlLineInput.replaceAll(ATTR_EXTRACTION_REGEX, REPLACEMENT);

            if(xmlLineInput.endsWith("/\">")) {
                xmlLineInput = xmlLineInput.replaceFirst("/\">", "\"/>");
            }

            return xmlLineInput.concat("\n");
        }
    }
}
