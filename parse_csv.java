package parse_csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class parse_csv {
	
	/**
	 * 	parse .csv and export XML file
	 * 
	 * @author jaw00168
	 * @version 13/04/2021
	 *
	 */

	public static void main(String[] args) throws ParserConfigurationException, TransformerException {

		/**
		 * 	@params args[0] path to csv file.
		 * 
		 * @throws ParserConfigurationException
		 * @throws TransformerException
		 */
		
		// create and instantiate DOM objects
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document document = dBuilder.newDocument();
		
		// create a root element and append to document.
		Element root = document.createElement("survey");
		document.appendChild(root);
		
		// read in the csv file using try with resources
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(args[0]))) {
			
			// Assume the header is the first line.
			reader.readLine();
			// Start at Line 1
			String line = reader.readLine();
			
			while (line != null) {
				
				// read in each line and split by delimiter
				String[] data = line.split(",");
				// create a node from each line and append to root
				root.appendChild(createNode(document, data));
				line = reader.readLine();
			}
			
			// output to XML
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			
			DOMSource source = new DOMSource(document);
			
			File outputFile = new File("./survey.xml");
	
			StreamResult file = new StreamResult(outputFile);
		
			transformer.transform(source, file);

		} catch (IOException e) {
			System.err.printf("The file could not be found. Check the path and try again \n %s \n\n", e.getMessage());
		}

	} // end main
	
	/**
	 * 	Create a response node with attributes and children in XML Structure
	 * @param document	
	 * @param data		Array containing node values
	 * @return Node with child elements and values.
	 */

	private static Node createNode(Document document, String[] data) {

		Element response = document.createElement("response");

		response.setAttribute("id", data[0]);
		response.appendChild(createNodeElement(document, "month", data[1]));
		response.appendChild(createNodeElement(document, "device", data[2]));
		response.appendChild(createNodeElement(document, "team", data[3]));
		response.appendChild(createNodeElement(document, "level", data[4]));
		response.appendChild(createNodeElement(document, "pokemon", data[5]));
		response.appendChild(createNodeElement(document, "medal", data[6]));

		return response;
	} 
	
	/**
	 * 	Create a child element and assign its value 
	 * @param document	
	 * @param name		Name of Element
	 * @param value		Its Value
	 * @return 
	 */

	private static Node createNodeElement(Document document, String name, String value) {

		Element node = document.createElement(name);
		node.appendChild(document.createTextNode(value));

		return node;
	} 

} // end class parse_csv
