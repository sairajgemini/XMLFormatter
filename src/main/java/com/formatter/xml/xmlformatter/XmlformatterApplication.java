package com.formatter.xml.xmlformatter;

import com.formatter.xml.xmlformatter.utility.XMLFileUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class XmlformatterApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlformatterApplication.class);
    public static void main(String[] args) throws IOException {
        SpringApplication.run(XmlformatterApplication.class, args);
        XMLFileUtility.XMLFormatterUtility.formatXMLFile();
    }
}
