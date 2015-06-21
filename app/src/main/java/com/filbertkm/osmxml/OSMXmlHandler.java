package com.filbertkm.osmxml;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OSMXmlHandler extends DefaultHandler {

    private List nodes = new ArrayList<>();

    private OSMNode node;

    private Map<String,String> tags;

    @Override
    public void startElement(java.lang.String uri, java.lang.String localName, java.lang.String qName, Attributes attributes) {
        if (qName == "node") {
            node = new OSMNode();

            if (attributes != null) {
                Integer length = attributes.getLength();

                for (int i = 0; i < length; i++) {
                    String name = attributes.getQName(i);

                    if (name == "id") {
                        node.setId(attributes.getValue(i));
                    } else if (name == "lat") {
                        node.setLat(attributes.getValue(i));
                    } else if (name == "lon") {
                        node.setLon(attributes.getValue(i));
                    } else {
                        node.setAttibute(name, attributes.getValue(i));
                    }
                }
            }
        }

        if (node != null && qName == "tag") {
            if (tags == null) {
                tags = new HashMap();
            }

            if (attributes != null) {
                Integer length = attributes.getLength();

                String key = null;
                String value = null;

                for (int i=0; i<length; i++) {
                    String name = attributes.getQName(i);

                    if(name == "k") {
                        key = attributes.getValue(i);
                    } else if(name == "v") {
                        value = attributes.getValue(i);
                    }
                }

                tags.put(key,value);
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {

    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if ((node != null ) && (qName == "node")) {
            if(tags != null) {
                node.setTags(tags);
                tags = null;
            }

            nodes.add(node);
            node = null;
        }
    }

    public List<OSMNode> getNodes() {
        return nodes;
    }

}
