package de.ptb.backend.BERT;


import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class TestDCCXML {
   
   public static void main(String[] args) {
	   
	   String XMLTestFile = "TestDCCFile.xml";	
	   String pathname = "TestFiles";

	   // https://www.tutorialspoint.com/java_xml/java_xpath_parse_document.htm#
      
      try {
         File inputFile = new File(pathname + "//" + XMLTestFile);
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder;

         dBuilder = dbFactory.newDocumentBuilder();

         Document doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize();

         XPath xPath =  XPathFactory.newInstance().newXPath();

         String expression = "/digitalCalibrationCertificate/measurementResults/measurementResult/results/result/data/quantity[@refType=\"measurementValue\"]/real";	        
         NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(
            doc, XPathConstants.NODESET);

         for (int i = 0; i < nodeList.getLength(); i++) {
            Node nNode = nodeList.item(i);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());
            
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement = (Element) nNode;
               // System.out.println("Student roll no :" + eElement.getAttribute("rollno"));
               System.out.println("Value : " 
                  + eElement
                  .getElementsByTagName("si:value")
                  .item(0)
                  .getTextContent());
               System.out.println("Unit : " 
                  + eElement
                  .getElementsByTagName("si:unit")
                  .item(0)
                  .getTextContent());
               System.out.println("Date Time : " 
                  + eElement
                  .getElementsByTagName("si:dateTime")
                  .item(0)
                  .getTextContent());
               System.out.println("Uncertainty : " 
                  + eElement
                  .getElementsByTagName("si:uncertainty")
                  .item(0)
                  .getTextContent());
               System.out.println("Coverage Factor : " 
                       + eElement
                       .getElementsByTagName("si:coverageFactor")
                       .item(0)
                       .getTextContent());
               System.out.println("Coverage Probability : " 
                       + eElement
                       .getElementsByTagName("si:coverageProbability")
                       .item(0)
                       .getTextContent());
            }
         }
      } catch (ParserConfigurationException e) {
         e.printStackTrace();
      } catch (SAXException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (XPathExpressionException e) {
         e.printStackTrace();
      }
   }
}
