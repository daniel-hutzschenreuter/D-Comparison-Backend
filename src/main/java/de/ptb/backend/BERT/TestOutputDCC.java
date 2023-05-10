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

public class TestOutputDCC {
	
	public String DKCRUniqueID = new String();
	public String DKCRTitle = new String();
	public String DKCRLeadNMI = new String();
	public String DKCRDimension = new String();
	public String DKCRUnit = new String();
	public String [] DKCRContributors = new String[11]; 
	
	
	public String xRef = new String();
	public String UiRef = new String();
	public String [] Ev = new String[11];
	double x = 10.0;
   
   // public static void main(String[] args) {
   
   public void Go()
   {
	   
	   String XMLTestFile = "DCCOutputXML.xml";	
	   String pathname = "TestFiles";

	   // https://www.tutorialspoint.com/java_xml/java_xpath_parse_document.htm#
      
      try {
         File inputFile = new File(pathname + "//" + XMLTestFile);
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder;

         dBuilder = dbFactory.newDocumentBuilder();

         Document doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize();

         
         XPath xPath4 =  XPathFactory.newInstance().newXPath();         
         String expression4 = "/digitalCalibrationCertificate/administrativeData/calibrationLaboratory";
         NodeList nodeList4 = (NodeList) xPath4.compile(expression4).evaluate(
                 doc, XPathConstants.NODESET); 
         for (int k = 0; k < nodeList4.getLength(); k++) {
             Node nNode4 = nodeList4.item(k);
          
             if (nNode4.getNodeType() == Node.ELEMENT_NODE) {
                 Element eElement = (Element) nNode4;
                 
                 DKCRLeadNMI = eElement
                         .getElementsByTagName("dcc:content")
                         .item(0)
                         .getTextContent(); 
                 
             }
             
         } 
         
         
         XPath xPath3 =  XPathFactory.newInstance().newXPath();         
         String expression3 = "/digitalCalibrationCertificate/administrativeData";
         NodeList nodeList3 = (NodeList) xPath3.compile(expression3).evaluate(
                 doc, XPathConstants.NODESET); 
         for (int k = 0; k < nodeList3.getLength(); k++) {
             Node nNode3 = nodeList3.item(k);
          
             if (nNode3.getNodeType() == Node.ELEMENT_NODE) {
                 Element eElement = (Element) nNode3;
                 
                 DKCRTitle = eElement
                         .getElementsByTagName("dcc:content")
                         .item(0)
                         .getTextContent(); 
                 
             }
             
         }   
         
         
         XPath xPath2 =  XPathFactory.newInstance().newXPath();         
         String expression2 = "/digitalCalibrationCertificate/administrativeData/coreData";
         NodeList nodeList2 = (NodeList) xPath2.compile(expression2).evaluate(
                 doc, XPathConstants.NODESET); 
         for (int k = 0; k < nodeList2.getLength(); k++) {
             Node nNode2 = nodeList2.item(k);
          
             if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                 Element eElement = (Element) nNode2;
                 
                 DKCRUniqueID = eElement
                         .getElementsByTagName("dcc:uniqueIdentifier")
                         .item(0)
                         .getTextContent(); 
                 
             }
             
         }
         
         XPath xPath5 =  XPathFactory.newInstance().newXPath();         
         String expression5 = "/digitalCalibrationCertificate/measurementResults/measurementResult/results/result/data/quantity[@refType=\"DimensionUnit\"]";	
         NodeList nodeList5 = (NodeList) xPath5.compile(expression5).evaluate(
                 doc, XPathConstants.NODESET); 
         for (int k = 0; k < nodeList5.getLength(); k++) {
             Node nNode5 = nodeList5.item(k);
          
             if (nNode5.getNodeType() == Node.ELEMENT_NODE) {
                 Element eElement = (Element) nNode5;
                 
                 DKCRDimension = eElement
                         .getElementsByTagName("dcc:content")
                         .item(0)
                         .getTextContent(); 
                 
                 DKCRUnit = eElement
                         .getElementsByTagName("si:unit")
                         .item(0)
                         .getTextContent(); 
                 
             }
             
         }
         
         
         XPath xPath1 =  XPathFactory.newInstance().newXPath();

         String expression1 = "/digitalCalibrationCertificate/measurementResults/measurementResult/results/result/data/quantity[@refType=\"xRef\"]/real";	        
         NodeList nodeList1 = (NodeList) xPath1.compile(expression1).evaluate(
            doc, XPathConstants.NODESET);       
         for (int j = 0; j < nodeList1.getLength(); j++) {
             Node nNode1 = nodeList1.item(j);       

             if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
                 Element eElement = (Element) nNode1;
                 // System.out.println("Student roll no :" + eElement.getAttribute("rollno"));
                 System.out.println("Value : " 
                    + eElement
                    .getElementsByTagName("si:value")
                    .item(0)
                    .getTextContent());   
                 
                 xRef = eElement
                         .getElementsByTagName("si:value")
                         .item(0)
                         .getTextContent(); 

                 UiRef = eElement
                         .getElementsByTagName("si:uncertainty")
                         .item(0)
                         .getTextContent(); 
            
                 
             }
         
         }
         
         
         XPath xPath =  XPathFactory.newInstance().newXPath();

         // String expression = "/digitalCalibrationCertificate/measurementResults/measurementResult/results/result/data/quantity[@refType=\"measurementValue\"]/real";	 
         String expression = "/digitalCalibrationCertificate/measurementResults/measurementResult/results/result/data/quantity[@refType=\"measurementValue\"]";	        
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
               
               
               
               Ev[i] = eElement
                       .getElementsByTagName("si:value")
                       .item(0)
                       .getTextContent();
               
               DKCRContributors[i] = eElement
                       .getElementsByTagName("dcc:content")
                       .item(0)
                       .getTextContent();
               
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

