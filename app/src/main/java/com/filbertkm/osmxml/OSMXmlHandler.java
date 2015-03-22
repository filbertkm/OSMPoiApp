package com.filbertkm.osmxml;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OSMXmlHandler extends DefaultHandler {

    private String currentElement;

    private List nodes = new ArrayList<OSMNode>();

    private OSMNode node;

    private Map<String,String> tags;

    @Override
    public void startElement(java.lang.String uri, java.lang.String localName, java.lang.String qName, Attributes attributes) {
        this.currentElement = qName;

        if(qName == "node") {
            this.node = new OSMNode();

            if (attributes != null) {
                Integer length = attributes.getLength();

                for (int i = 0; i < length; i++) {
                    String name = attributes.getQName(i);

                    if (name == "id") {
                        this.node.setId(attributes.getValue(i));
                    } else if (name == "lat") {
                        this.node.setLat(attributes.getValue(i));
                    } else if (name == "lon") {
                        this.node.setLon(attributes.getValue(i));
                    }
                }
            }
        }

        if(this.node != null && qName == "tag") {
            if (this.tags == null) {
                this.tags = new HashMap();
            }

            if(attributes != null) {
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

                this.tags.put(key,value);
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {

    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if ((this.node != null ) && (qName == "node")) {
            if(this.tags != null) {
                this.node.setTags(this.tags);
                this.tags = null;
            }

            nodes.add(this.node);
            this.node = null;
        }
    }

    public List<OSMNode> getNodes() {
        return this.nodes;
    }

}
