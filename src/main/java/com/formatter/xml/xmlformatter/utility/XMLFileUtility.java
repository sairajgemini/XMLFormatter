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

/**
 * @author saikatgupta
 * This Singleton Utility class will contain all utility methods of XML formatting
 */
public final class XMLFileUtility implements Serializable {
    protected XMLFileUtility() {}

    public static final String getXmlFilePath(final boolean isSrcFile) {
        Scanner in = new Scanner(System.in);
        if(isSrcFile) {
            System.out.print("Source XML filename: ");
        } else {
            System.out.print("Enter destination filename: ");
        }
        return in.nextLine();
    }

    public static final Path writeToFile(final String src, final Path dest) throws IOException {
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

    public static final class XMLFormatterUtility {
        // This REGEX searches all xml attributes without quotes
        static final String ATTR_EXTRACTION_REGEX = "(?<==)([^\\s\"<>]+)";
        static final String ACTIVE_BINARY_START_TAG = "<ActiveBinary>";
        static final String ACTIVE_BINARY_END_TAG = "</ActiveBinary>";
        static final String REPLACEMENT = "\"$1\"";
        static final Logger LOGGER = LoggerFactory.getLogger(XMLFormatterUtility.class);

        static boolean isActiveBinaryBlock = false;

        private XMLFormatterUtility() {}

        public static final String findAndFormatXmlInput(String xmlLineInput) {
            xmlLineInput = xmlLineInput.replaceAll(ATTR_EXTRACTION_REGEX, REPLACEMENT);

            if(xmlLineInput.endsWith("/\">")) {
                xmlLineInput = xmlLineInput.replaceFirst("/\">", "\"/>");
            }
            // Linearize Active Binary data
            if(xmlLineInput.contains(ACTIVE_BINARY_START_TAG)) {
                isActiveBinaryBlock = true;
            }

            if(!xmlLineInput.contains(ACTIVE_BINARY_END_TAG) && isActiveBinaryBlock) {
                return xmlLineInput;
            } else {
                isActiveBinaryBlock = false;
            }

            return xmlLineInput.concat("\n");
        }

        public static final void formatXMLFile() throws IOException {
            String srcFilePath = XMLFileUtility.getXmlFilePath(true);
            String destPath = XMLFileUtility.getXmlFilePath(false);
            Path destFile = Paths.get(destPath);
            if(Files.exists(destFile)) {
                Files.delete(destFile);
                LOGGER.info("Earlier destination file {} deleted successfully.", destPath);
            }

            destFile = Files.createFile(Paths.get(destPath));
            XMLFileUtility.writeToFile(srcFilePath, destFile);
        }
    }
}
