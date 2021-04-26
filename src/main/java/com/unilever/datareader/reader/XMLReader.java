package com.unilever.datareader.reader;

import com.unilever.datareader.exception.FileParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to read the specfied XML file
 */
public class XMLReader {

    private final InputStream inputStream;
    private static final Logger LOGGER = Logger.getLogger(com.unilever.datareader.reader.XMLReader.class.getName());

    /**
     * Constructor to create an instance of the class.
     * @param inputStream input data as stream
     */
    public XMLReader(InputStream inputStream){

        this.inputStream = inputStream;
    }

    /**
     * Gets the list of sub folder names for the specified folder.
     *
     * @param folderName the folder name to be searched
     * @return list of sub folder names of the specified folder
     */
    public List<String> getAllSubFolderNames(String folderName){

        try {
            final String folderPattern = "//folder[@name= '" + folderName + "']//folder";
            final XPathExpression expression = XPathFactory.newInstance().newXPath().compile(folderPattern);
            final NodeList nodeList = (NodeList) expression.evaluate(parseFile(), XPathConstants.NODESET);

            final List<String> folderNames = new ArrayList<>();
            for (int index = 0; index < nodeList.getLength(); index++) {
                folderNames.add(((Element) nodeList.item(index)).getAttribute("name"));
            }

            return folderNames;
        } catch (XPathExpressionException exception) {
            LOGGER.log(Level.SEVERE, exception.getMessage(), exception);
            throw new FileParserException("File could not be parsed !");
        }
    }

    /**
     * Parses the specified input data stream(from the XML file).
     * @return the parsed document object
     */
    private Document parseFile() {
        try{
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found!");
            }
            else {
                final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
                final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                final Transformer serializer = TransformerFactory.newInstance().newTransformer();
                //Set the property to remove the header from XML and read only the actual data
                serializer.setOutputProperty("omit-xml-declaration", "yes");
                final StreamResult result = new StreamResult(outputStream);
                serializer.transform(new DOMSource(document), result);

                return document;
            }
        }
        catch (ParserConfigurationException | SAXException|  TransformerException| IOException exception) {
            LOGGER.log(Level.SEVERE, exception.getMessage(), exception);
            throw new FileParserException("File could not be parsed !");
        }
    }
}