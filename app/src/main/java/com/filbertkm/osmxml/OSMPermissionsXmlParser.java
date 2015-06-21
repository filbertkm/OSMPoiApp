package com.filbertkm.osmxml;

import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class OSMPermissionsXmlParser {

    public boolean parse(String response) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            InputSource is = new InputSource(new StringReader(response));
            is.setEncoding("UTF-8");

            OSMPermissionsXmlHandler handler = new OSMPermissionsXmlHandler();
            saxParser.parse(is, handler);

            List<String> permissions = handler.getPermissions();

            for(String permission : permissions) {
                if (permission.equals("allow_write_api")) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

}
