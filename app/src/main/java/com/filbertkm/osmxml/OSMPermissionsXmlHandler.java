package com.filbertkm.osmxml;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class OSMPermissionsXmlHandler extends DefaultHandler {

    private List permissions = new ArrayList<>();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName == "permission") {
            if (attributes != null) {
                Integer length = attributes.getLength();

                for (int i = 0; i < length; i++) {
                    if (attributes.getQName(i) == "name") {
                        permissions.add(attributes.getValue(i));
                    }
                }
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {

    }

    @Override
    public void endElement(String uri, String localName, String qName) {

    }

    public List<String> getPermissions() {
        return this.permissions;
    }

}