package ru.bootdev.test.core.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class XmlHelper {

    private static final XmlMapper mapper = new XmlMapper();

    public static JsonNode getXmlNode(String xmlText) throws JsonProcessingException {
        return mapper.readTree(xmlText);
    }

    public static JsonNode getXmlNode(File xmlFile) throws IOException {
        return mapper.readTree(xmlFile);
    }

    public static JsonNode getXmlNode(URL url) throws IOException {
        return mapper.readTree(url);
    }

    public static <T> T xmlStringToObject(String xmlText, Class<T> clazz) throws JsonProcessingException {
        return mapper.readValue(xmlText, clazz);
    }

    public static <T> T xmlFileToObject(File xmlFile, Class<T> clazz) throws IOException {
        return mapper.readValue(xmlFile, clazz);
    }

    public static <T> T xmlUrlToObject(URL xmlUrl, Class<T> clazz) throws IOException {
        return mapper.readValue(xmlUrl, clazz);
    }

    public static String xmlNodeToString(Object xmlNode) throws JsonProcessingException {
        return mapper.writeValueAsString(xmlNode);
    }

    public static Document xmlStringToDocument(String xmlText) throws ParserConfigurationException, IOException, SAXException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(xmlText)));
    }

    public static Document xmlFileToDocument(File xmlFile) throws ParserConfigurationException, IOException, SAXException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
    }

    public static Document xmlUriToDocument(String uri) throws ParserConfigurationException, IOException, SAXException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(uri);
    }

    public static String xmlDocumentToString(Document xmlDocument) throws TransformerException {
        StringWriter writer = new StringWriter();
        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(xmlDocument), new StreamResult(writer));
        return writer.getBuffer().toString();
    }

    public static File xmlDocumentToFile(Document xmlDocument, File outputXmlFile) throws TransformerException {
        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(xmlDocument), new StreamResult(outputXmlFile));
        return outputXmlFile;
    }

    public static List<Node> xmlNodeList(Document xmlDocument, String tagName) {
        List<Node> nodes = new ArrayList<>();
        NodeList nodeList = xmlDocument.getElementsByTagName(tagName);
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE)
                nodes.add(nodeList.item(i));
        }
        return nodes;
    }

    public static void changeXmlTagsValue(Document xmlDocument, String tagName, Consumer<Node> lambda) {
        xmlNodeList(xmlDocument, tagName).forEach(lambda);
    }

    public static void changeXmlTagsValue(Document xmlDocument, String tagName, String value) {
        xmlNodeList(xmlDocument, tagName).forEach(node -> node.setTextContent(value));
    }

    public static void changeXmlTagsValue(Document xmlDocument, String tagName, Iterator<?> iterator) {
        xmlNodeList(xmlDocument, tagName).forEach(node -> node.setTextContent(iterator.next().toString()));
    }

    public static Boolean compareXmlDocuments(Document a, Document b) {
        return a.isEqualNode(b);
    }
}
