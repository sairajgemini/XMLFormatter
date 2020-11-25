package com.formatter.xml.xmlformatter;

import com.formatter.xml.xmlformatter.utility.XMLFileUtility;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class XmlformatterApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(XmlformatterApplication.class, args);
        XMLFileUtility.generateFormattedXMLFile();
    }
}
