/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.batch.ui.bot.test.validation;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.junit.Test;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/**
 * Tests validity and availability of XSD schema used in configuration batch XML files
 * @author odockal
 *
 */
public class XSDValidationTest {

	@Test
	public void testXSDAvailabilityAndValidity() {
		this.checkXSDSchema("http://xmlns.jcp.org/xml/ns/javaee/jobXML_1_0.xsd");
		this.checkXSDSchema("http://xmlns.jcp.org/xml/ns/javaee/batchXML_1_0.xsd");
	}
	
	private Schema checkXSDSchema(String url) {
		Schema schema = null;
		try {
			URL xsdUrl = new URL(url);
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			schema = schemaFactory.newSchema(xsdUrl);
		} catch (SAXException e) {
			fail("SAX exception in XSD schema definition at " + url + " : " + e);
		} catch (MalformedURLException e) {
			fail("Malformed XSD schema URL (" + url + "): " + e);
		} catch (Exception e) {
			fail("Excetion occurs in retrieving XSD schema at " + url + ": " + e);
		}
		return schema;
	}
}
