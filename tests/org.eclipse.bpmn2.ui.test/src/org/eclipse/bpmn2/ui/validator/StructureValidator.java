package org.eclipse.bpmn2.ui.validator;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Assert;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class StructureValidator {

	private Document document;
	
	/**
	 * 
	 */
	public StructureValidator() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 * @param xmlContent
	 */
	public void build(String xmlContent) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(true);
	
			DocumentBuilder builder = factory.newDocumentBuilder();
	
			// the "parse" method also validates XML, will throw an exception if misformatted.
			document = builder.parse(new InputSource(new ByteArrayInputStream(xmlContent.getBytes())));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * @param elementName
	 * @param index
	 * @return
	 */
	public Map<String, String> getElementAttributes(String elementName, int index) {
		Map<String, String> ret = new HashMap<String, String>();
		
		Node node = document.getElementsByTagName(elementName).item(index);
		NamedNodeMap attributes = node.getAttributes();
		
		if (attributes != null) {
			for (int i=0; i<attributes.getLength(); i++) {
				Node attribute = attributes.item(i);
				ret.put(attribute.getNodeName(), attribute.getTextContent());
			}
		}
		
		return ret;
	}
	
	/**
	 * 
	 * @param elementName
	 * @return
	 */
	public List<String> getElementPath(String elementName) {
		List<String> ret = new ArrayList<String>();
		
		Node node = document.getElementsByTagName(elementName).item(0);
		do {
			ret.add(0, node.getNodeName());
			node = node.getParentNode();
		} while (node != null);
		
		return ret;
	}
	
	/**
	 * 
	 * @param elementName
	 * @param atttibuteNames
	 * @param attributeValues
	 */
	public void assertElementAttributes(String elementName, int elementIndex, String[] atttibuteNames, String[] attributeValues) {
		Map<String, String> expectedAttributes = new HashMap<String, String>();
		for (int i=0; i<atttibuteNames.length; i++) {
			expectedAttributes.put(atttibuteNames[i], attributeValues[i]);
		}
		Assert.assertEquals("Attributes do not match", expectedAttributes, getElementAttributes(elementName, elementIndex));
	}
	
	/**
	 * 
	 * @param elementName
	 * @param expectedElementPath
	 */
	public void assertElementPath(String elementName, String[] expectedElementPath) {
		List<String> expectedPathElements = Arrays.asList(expectedElementPath);
		Assert.assertEquals("Element paths do not match", expectedPathElements, getElementPath(elementName));
	}
	
}
