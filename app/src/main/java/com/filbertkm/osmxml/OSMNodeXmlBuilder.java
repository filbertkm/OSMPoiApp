package com.filbertkm.osmxml;


import com.filbertkm.osmapp.model.Place;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringWriter;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class OSMNodeXmlBuilder {

    public String build(Place place) {

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

            Element nodeElement = document.createElement("node");

            nodeElement.setAttribute("id", place.getId().toString());

            Map<String,String> nodeAttributes = place.getAttributes();

            for (Map.Entry<String, String> entry : nodeAttributes.entrySet()) {
                nodeElement.setAttribute(entry.getKey(), entry.getValue());
            }

            nodeElement.setAttribute("lat", String.valueOf(place.getLocation().getLatitude()));
            nodeElement.setAttribute("lon", String.valueOf(place.getLocation().getLongitude()));

            Map<String,String> tags = place.getTags();

            for (Map.Entry<String, String> tagEntry : tags.entrySet()) {
                Element tagElement = document.createElement("tag");
                tagElement.setAttribute("k", tagEntry.getKey());
                tagElement.setAttribute("v", tagEntry.getValue());

                nodeElement.appendChild(tagElement);
            }

            return convertDocumentToString(document);
        } catch (ParserConfigurationException ex) {
            // omg!
        }

        return "string";
    }

    private static String convertDocumentToString(Document doc) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            // below code to remove XML declaration
            // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            String output = writer.getBuffer().toString();
            return output;
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return null;
    }

}
