package com.filbertkm.osmxml;

import android.util.Log;

import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;

import java.io.StringReader;

public class OSMXmlParser {

    public List<OSMNode> parse(String mapData) {
        Log.i("osmapp", "parsing xml");
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            InputSource is = new InputSource(new StringReader(mapData));
            is.setEncoding("UTF-8");

            OSMXmlHandler handler = new OSMXmlHandler();
            saxParser.parse(is, handler);

            return handler.getNodes();
        } catch (Exception e) {
            return null;
        }
    }

}
