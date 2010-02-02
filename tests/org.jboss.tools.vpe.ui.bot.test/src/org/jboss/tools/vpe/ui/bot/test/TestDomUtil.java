/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jboss.tools.common.model.util.XMLUtil;
import org.jboss.tools.vpe.editor.util.Constants;
import org.mozilla.interfaces.nsIDOMAttr;
import org.mozilla.interfaces.nsIDOMNamedNodeMap;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class TestDomUtil {
	/**
	 * Attributes names that will be skipped in attribute comparison.
	 */
	public static final Set<String> skippedAtributes = new HashSet<String>();
	static {
		// Add here all attributes names to be skipped (IN UPPER CASE!)		
		skippedAtributes.addAll(Arrays.asList("DIR"));//$NON-NLS-1$
	}
	
	final public static String ID_ATTRIBUTE = "id"; //$NON-NLS-1$

	final public static String ILLEGAL_ATTRIBUTES = "illegalAttributes"; //$NON-NLS-1$

	final public static String ILLEGAL_ATTRIBUTES_SEPARATOR = Constants.COMMA;

	final public static String START_REGEX = "/"; //$NON-NLS-1$

	final public static String END_REGEX = "/"; //$NON-NLS-1$

	public static Document getDocument(File file) throws FileNotFoundException {
		// create reader
		FileReader reader = new FileReader(file);

		// return document
		return XMLUtil.getDocument(reader);
	}

	public static Document getDocument(String content)
			throws FileNotFoundException {
		// create reader
		StringReader reader = new StringReader(content);

		// return document
		return XMLUtil.getDocument(reader);
	}

	/**
	 * 
	 * @param document
	 * @param elementId
	 * @return
	 */
	public static Element getElemenById(Document document, String elementId) {

		Element element = document.getDocumentElement();

		NodeList children = element.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if ((child.getNodeType() == Node.ELEMENT_NODE)
					&& elementId.equals(((Element) child)
							.getAttribute(ID_ATTRIBUTE)))
				return (Element) child;

		}

		return null;

	}

	/**
	 * 
	 * @param element
	 * @return
	 */
	public static Element getFirstChildElement(Element element) {

		if (element != null) {
			NodeList children = element.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);

				if (child.getNodeType() == Node.ELEMENT_NODE)
					return (Element) child;

			}
		}
		return null;

	}

	/**
	 * 
	 * @param vpeNode
	 * @param schemeNode
	 * @return
	 * @throws ComparisonException
	 */
	public static void compareNodes(nsIDOMNode vpeNode, Node modelNode)
			throws ComparisonException {

		if (!modelNode.getNodeName().equalsIgnoreCase(vpeNode.getNodeName())) {
			throw new ComparisonException("name of tag is \"" //$NON-NLS-1$
					+ vpeNode.getNodeName() + "\"but must be \"" //$NON-NLS-1$
					+ modelNode.getNodeName() + "\""); //$NON-NLS-1$
		}
		
		if ("#comment".equals(vpeNode.getNodeName())){ //$NON-NLS-1$
			String vpeNodeValue = vpeNode.getNodeValue();
			try {
				vpeNodeValue = clearStringFromRSymbols(vpeNodeValue);
			} catch (Exception e) {
			}
			if ((modelNode.getNodeValue() != null)
					&& (!modelNode.getNodeValue().trim().equalsIgnoreCase(
							vpeNodeValue.trim()))) {
				throw new ComparisonException("value of " + vpeNode.getNodeName() //$NON-NLS-1$
						+ " is \"" + vpeNodeValue.trim() //$NON-NLS-1$
						+ "\" but must be \"" + modelNode.getNodeValue().trim() //$NON-NLS-1$
						+ "\""); //$NON-NLS-1$
			}
		} else {
			if ((modelNode.getNodeValue() != null)
					&& (!modelNode.getNodeValue().trim().equalsIgnoreCase(
							vpeNode.getNodeValue().trim()))) {
				throw new ComparisonException("value of " + vpeNode.getNodeName() //$NON-NLS-1$
						+ " is \"" + vpeNode.getNodeValue().trim() //$NON-NLS-1$
						+ "\" but must be \"" + modelNode.getNodeValue().trim() //$NON-NLS-1$
						+ "\""); //$NON-NLS-1$
			}
		}
		// compare node's attributes
		if (modelNode.getNodeType() == Node.ELEMENT_NODE) {

			compareAttributes(modelNode.getAttributes(), vpeNode
					.getAttributes());
		}

		// compare children
		nsIDOMNodeList vpeChildren = vpeNode.getChildNodes();
		NodeList schemeChildren = modelNode.getChildNodes();
		int realCount = 0;
		for (int i = 0; i < schemeChildren.getLength(); i++) {

			Node schemeChild = schemeChildren.item(i);

			// leave out empty text nodes in test dom model
			if ((schemeChild.getNodeType() == Node.TEXT_NODE)
					&& ((schemeChild.getNodeValue() == null) || (schemeChild
							.getNodeValue().trim().length() == 0)))
				continue;

			nsIDOMNode vpeChild = vpeChildren.item(realCount++);

			if (null == vpeChild) {
				throw new ComparisonException(
						"Child of node \"" //$NON-NLS-1$
								+ vpeNode.getNodeName()
								+ "\" is \"null\", but should be \"" + schemeChild.getNodeName() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
			}

			// leave out empty text nodes in vpe dom model
			while (((vpeChild.getNodeType() == Node.TEXT_NODE) && ((vpeChild
					.getNodeValue() == null) || (vpeChild.getNodeValue().trim()
					.length() == 0)))) {
				vpeChild = vpeChildren.item(realCount++);
				if (null == vpeChild) {
					throw new ComparisonException(
							"Child of node \"" //$NON-NLS-1$
									+ vpeNode.getNodeName()
									+ "\" is \"null\", but should be \"" + schemeChild.getNodeName() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}

			compareNodes(vpeChild, schemeChild);

		}

	}

	/**
	 * get ids of tests
	 * 
	 * @param testDocument
	 * @return
	 */
	public static List<String> getTestIds(Document testDocument) {
		Element rootElement = testDocument.getDocumentElement();
		List<String> ids = new ArrayList<String>();
		NodeList children = rootElement.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE)
				ids.add(((Element) child).getAttribute(ID_ATTRIBUTE));

		}
		return ids;
	}

	private static void compareAttributes(NamedNodeMap modelAttributes,
			nsIDOMNamedNodeMap vpeAttributes) throws ComparisonException {

		for (int i = 0; i < modelAttributes.getLength(); i++) {
			Attr modelAttr = (Attr) modelAttributes.item(i);
			String name = modelAttr.getName();

			// if the attribute has to be skipped, then do it
			if ( name != null 
					&& skippedAtributes.contains(name.toUpperCase()) ) {
				continue;
			}

			// if there are limitation of attributes
			if (ILLEGAL_ATTRIBUTES.equals(name)) {

				String[] illegalAttributes = modelAttr.getNodeValue().split(
						ILLEGAL_ATTRIBUTES_SEPARATOR);

				for (String illegalAttributeName : illegalAttributes) {
					if (vpeAttributes.getNamedItem(illegalAttributeName.trim()) != null)
						throw new ComparisonException("illegal attribute :" //$NON-NLS-1$
								+ illegalAttributeName);
				}

			} else {

				
				if (vpeAttributes.getNamedItem(
						name) == null)
					throw new ComparisonException("there is not : \"" + name //$NON-NLS-1$
							+ "\" attribute"); //$NON-NLS-1$
				
				nsIDOMAttr vpeAttr = (nsIDOMAttr) vpeAttributes.getNamedItem(
						name).queryInterface(nsIDOMAttr.NS_IDOMATTR_IID);

					compareComplexStrings(modelAttr.getNodeValue().trim(),
							vpeAttr.getNodeValue().trim());

			}
		}
	}

	static private void compareComplexStrings(String modelString,
			String vpeString) throws ComparisonException {

		if (modelString.startsWith(START_REGEX)
				&& modelString.endsWith(END_REGEX)) {

			String regex = modelString.substring(START_REGEX.length(),
					modelString.length() - END_REGEX.length());

			Matcher matcher = Pattern.compile(regex).matcher(vpeString);
			if (!matcher.find()) {
				throw new ComparisonException("string is\"" + vpeString //$NON-NLS-1$
						+ "\" but pattern is \"" + regex + "\""); //$NON-NLS-1$ //$NON-NLS-2$
			}

		} else if (!modelString.equals(vpeString)) {
			throw new ComparisonException("string is\"" + vpeString //$NON-NLS-1$
					+ "\" but must be \"" + modelString + "\""); //$NON-NLS-1$ //$NON-NLS-2$
		}

	}
	
	private static String clearStringFromRSymbols(String string){
		int index = string.indexOf("\r"); //$NON-NLS-1$
		while (index!=-1) {
			String stringAfterSymbol = string.substring(index+1);
			String stringBeforeSymbol = string.substring(0,index);
			string = stringBeforeSymbol.concat(stringAfterSymbol);
			index = string.indexOf("\r"); //$NON-NLS-1$
		}
		return string;
	}
	
}
