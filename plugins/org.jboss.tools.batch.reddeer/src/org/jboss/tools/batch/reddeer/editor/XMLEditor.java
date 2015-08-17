package org.jboss.tools.batch.reddeer.editor;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLEditor {

	public String evaluateXPath(String xPathExpression){
		XPath xPath =  XPathFactory.newInstance().newXPath();
		
		try {
			Document doc = loadXMLFromString(new DefaultStyledText().getText());
			xPath.setNamespaceContext(new UniversalNamespaceResolver(doc));
			return xPath.compile(xPathExpression).evaluate(doc);
		} catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException e) {
			throw new EclipseLayerException("Cannot evaluate xPath " + xPath, e);
		}
	}
	
	public String evaluateXPath(String xPathExpression, HashMap<String, String> namespaces){
		XPath xPath =  XPathFactory.newInstance().newXPath();
		
		try {
			Document doc = loadXMLFromString(new DefaultStyledText().getText());
			xPath.setNamespaceContext(new SimpleNamespaceContext(namespaces));
			return xPath.compile(xPathExpression).evaluate(doc);
		} catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException e) {
			throw new EclipseLayerException("Cannot evaluate xPath " + xPath, e);
		}
	}
	
	private Document loadXMLFromString(String xml) throws ParserConfigurationException, SAXException, IOException {
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setNamespaceAware(true);
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xml));
	    return builder.parse(is);
	}
	
	public static class SimpleNamespaceContext implements NamespaceContext {

	    private final Map<String, String> PREF_MAP = new HashMap<String, String>();

	    public SimpleNamespaceContext(final Map<String, String> prefMap) {
	        PREF_MAP.putAll(prefMap);       
	    }

	    public String getNamespaceURI(String prefix) {
	        return PREF_MAP.get(prefix);
	    }

	    public String getPrefix(String uri) {
	        throw new UnsupportedOperationException();
	    }

	    public Iterator<?> getPrefixes(String uri) {
	        throw new UnsupportedOperationException();
	    }
	}
	
	public class UniversalNamespaceResolver implements NamespaceContext {

		private Document sourceDocument;

	    /**
	     * This constructor stores the source document to search the namespaces in
	     * it.
	     * 
	     * @param document
	     *            source document
	     */
	    public UniversalNamespaceResolver(Document document) {
	        sourceDocument = document;
	    }

	    /**
	     * The lookup for the namespace uris is delegated to the stored document.
	     * 
	     * @param prefix
	     *            to search for
	     * @return uri
	     */
	    public String getNamespaceURI(String prefix) {
	        if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
	            return sourceDocument.lookupNamespaceURI(null);
	        } else {
	            return sourceDocument.lookupNamespaceURI(prefix);
	        }
	    }

	    /**
	     * This method is not needed in this context, but can be implemented in a
	     * similar way.
	     */
	    public String getPrefix(String namespaceURI) {
	        return sourceDocument.lookupPrefix(namespaceURI);
	    }

	    public Iterator getPrefixes(String namespaceURI) {
	        // not implemented yet
	        return null;
	    }

	}
}
