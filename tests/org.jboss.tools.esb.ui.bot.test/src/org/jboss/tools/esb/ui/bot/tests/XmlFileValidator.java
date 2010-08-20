package org.jboss.tools.esb.ui.bot.tests;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class XmlFileValidator {
	private static final Logger log = Logger.getLogger(XmlFileValidator.class);
	private final static DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	private final static XPathFactory xpFactory = XPathFactory.newInstance();
	private final Document doc;
	private final XPath xpath;
	
	public XmlFileValidator(String xmlContent) throws Exception {
		DocumentBuilder db = docFactory.newDocumentBuilder();
		doc = db.parse(new InputSource(new StringReader(xmlContent)));
		xpath = xpFactory.newXPath();
	}
	public Document getDocument() {
		return doc;
	}
	public XPath getXpath() {
		return xpath;
	}
	public boolean executeBoolean(String expr) {
		try {
			return (Boolean)xpath.evaluate(expr, getDocument(), XPathConstants.BOOLEAN);
		} catch (XPathExpressionException e) {
			log.error("Error evaluating xPath '"+expr+"'", e);
			return false;
		}
	}
	public String executeString(String expr) {
		try {
			return (String)xpath.evaluate(expr, getDocument(), XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			log.error("Error evaluating xPath '"+expr+"'", e);
			return null;
		}
	}
}
