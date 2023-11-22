package utilities;

import encryption.Encryption;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static java.lang.System.*;

public class ConnectionsXmlReader {
    public static void main(String[] args) throws SQLException {
        getDbConnection();

    }
    /*public static Connection getDbConnection() {
        String databaseURL = ConnectionsXmlReader.getDatabaseURL();
        String dbName = ConnectionsXmlReader.getDatabaseName();
        String username = ConnectionsXmlReader.getUsername();
        String password = ConnectionsXmlReader.getPassword();

        Connection connection = null;

        try {

            if (databaseURL.endsWith("/")) {
                databaseURL = databaseURL.substring(0, databaseURL.length() - 1);
            }

            connection = DriverManager.getConnection(databaseURL + "/" + dbName, username, password);
            System.out.println("Connected to " + dbName);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }*/
    public static Connection getDbConnection() throws SQLException {
        String databaseURL = ConnectionsXmlReader.getDatabaseURL();
        String dbName = ConnectionsXmlReader.getDatabaseName();
        String username = ConnectionsXmlReader.getUsername();
        String password = ConnectionsXmlReader.getPassword();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(databaseURL + dbName, username, password);
            System.out.println("Connected to " + dbName);
        } catch (SQLException e) {
            e.printStackTrace();
        }/*finally {
            connection.close();
            System.out.println("connection closed in " + dbName);
        }*/

        return connection;
    }

    public static String getPortRest() {
        try {
            File connectionsXmlFile = new File("connections/connections.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(connectionsXmlFile);

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();

            String expression = "/CONFIG/API/UNDERTOW/PORT[@REST]"; //predicates
            Node portRestNode = (Node) xpath.evaluate(expression, document, XPathConstants.NODE);

            if (portRestNode != null) {
                return portRestNode.getAttributes().getNamedItem("REST").getNodeValue();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getHostRest() {
        try {
            File connectionsXmlFile = new File("connections/connections.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(connectionsXmlFile);

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();

            String expression = "/CONFIG/API/UNDERTOW/HOST[@REST]";
            Node hostRestNode = (Node) xpath.evaluate(expression, document, XPathConstants.NODE);

            if (hostRestNode != null) {
                return hostRestNode.getAttributes().getNamedItem("REST").getNodeValue();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getBasePathRest() {
        try {
            File connectionsXmlFile = new File("connections/connections.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(connectionsXmlFile);

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();

            String expression = "/CONFIG/API/UNDERTOW/BASE_PATH[@REST]";
            Node basePathRestNode = (Node) xpath.evaluate(expression, document, XPathConstants.NODE);

            if (basePathRestNode != null) {
                return basePathRestNode.getAttributes().getNamedItem("REST").getNodeValue();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getDatabaseName() {
        try {
            File connectionsXmlFile = new File("connections/connections.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(connectionsXmlFile);

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();

            String expression = "/CONFIG/DATABASE/DATABASE_NAME";
            Node databaseNameNode = (Node) xpath.evaluate(expression, document, XPathConstants.NODE);

            if (databaseNameNode != null) {
                String databaseName;
                databaseName = databaseNameNode.getTextContent();

                /*Checking if  TYPE = "CLEARTEXT"*/
                return getString(connectionsXmlFile, document, (Element) databaseNameNode, databaseName);

            } else {
                out.println("Database Name not found.");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getString(File connectionsXmlFile, Document document, Element databaseNameNode, String databaseName) throws TransformerException {
        Element databaseNameElement = databaseNameNode;
        String typeAttribute = databaseNameElement.getAttribute("TYPE");
       // System.out.println("The TYPE property is : " + typeAttribute);
        if ("CLEARTEXT".equalsIgnoreCase(typeAttribute)) {
            /*Encrypting && Updating connections.xml file*/
            String encryptedDatabaseName = Encryption.encrypt(databaseName);
            databaseNameElement.setTextContent(encryptedDatabaseName);
            databaseNameElement.setAttribute("TYPE", "ENCRYPTED");

            /*Updating*/

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(connectionsXmlFile);
            transformer.transform(source, result);

            return encryptedDatabaseName;
        } else if ("ENCRYPTED".equalsIgnoreCase(typeAttribute)) {
            String decryptedDatabaseName = Encryption.decrypt(databaseName);
            return decryptedDatabaseName;
        } else {

            return null;
        }
    }

    public static String getUsername() {
        try {
            File connectionsXmlFile = new File("connections/connections.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(connectionsXmlFile);

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();

            String expression = "/CONFIG/DATABASE/USERNAME";
            Node usernameNode = (Node) xpath.evaluate(expression, document, XPathConstants.NODE);

            if (usernameNode != null) {
                String username = usernameNode.getTextContent();
                /*Check if  TYPE = "CLEARTEXT"*/
                return getString(connectionsXmlFile, document, (Element) usernameNode, username);
                } else {
                out.println("⚠\uFE0F Username not found.");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getPassword() {
        try {
            File connectionsXmlFile = new File("connections/connections.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(connectionsXmlFile);

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();

            String expression = "/CONFIG/DATABASE/PASSWORD";
            Node passwordNode = (Node) xpath.evaluate(expression, document, XPathConstants.NODE);

            if (passwordNode != null) {
                String password = passwordNode.getTextContent();

                /*Check if  TYPE = "CLEAR-TEXT"*/
                return getString(connectionsXmlFile, document, (Element) passwordNode, password);
            } else {
                out.println("Error ⚠\uFE0F No password was found.");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDatabaseURL() {
        try {
            File connectionsXmlFile = new File("connections/connections.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(connectionsXmlFile);

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();

            String expression = "/CONFIG/DATABASE/DATABASE_URL";
            Node databaseURLNode = (Node) xpath.evaluate(expression, document, XPathConstants.NODE);

            if (databaseURLNode != null) {
                String databaseURL = databaseURLNode.getTextContent().trim();
               // System.out.println("Database URL: " + databaseURL);


                if (!databaseURL.endsWith("/")) {
                    databaseURL += "/";
                }

                return databaseURL;
            } else {
                System.out.println("DATABASE_URL not found");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}