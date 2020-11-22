package com.formatter.xml.xmlformatter;

import com.formatter.xml.xmlformatter.utility.XMLFileUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class XmlformatterApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlformatterApplication.class);
    public static void main(String[] args) throws IOException {
        SpringApplication.run(XmlformatterApplication.class, args);

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
