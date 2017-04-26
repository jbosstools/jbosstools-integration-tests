/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.

 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.bot.test.beansxml.template;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jboss.reddeer.eclipse.jdt.ui.wizards.NewClassCreationWizard;
import org.jboss.reddeer.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.common.model.ui.editor.EditorPartWrapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class BeansXMLBeansEditorTemplate extends CDITestBase {
	
	@After
	public void cleanup(){
		deleteAllProjects();
	}
			
	@Test
	public void testClasses() {		
		NewClassCreationWizard jd = new NewClassCreationWizard();
		jd.open();
		new NewClassWizardPage().setName("Foo");
		jd.finish();
		
		jd.open();
		new NewClassWizardPage().setName("Bar");
		jd.finish();
		
		addClassItem(getPackageName() + ".Foo");
		addClassItem(getPackageName() + ".Bar");
		removeClassItem(getPackageName() + ".Foo");
		checkResult("alternatives", ".Bar");
	}
	
	@Test
	public void testInterceptors() {
		beansHelper.createIBinding("B2",getPackageName(),"TYPE", false, true);
		beansHelper.createIBinding("B4",getPackageName(),"TYPE", true, false);
		beansHelper.createInterceptor("I1", getPackageName(), "B2", false,false);
		beansHelper.createInterceptor("I2", getPackageName(), "B4", false, false);
		addInterceptorItem(getPackageName() + ".I1");
		removeInterceptorItem(getPackageName() + ".I1");
		addInterceptorItem(getPackageName() + ".I2");
		checkResult("interceptors", ".I2");
	}

	
	@Test
	public void testDecorators() {
		beansHelper.createDecorator("", getPackageName(), "java.util.Map", "field",
				false, false, true, true,false);
		beansHelper.createDecorator("", getPackageName(), "java.lang.Comparable", null,
				true, true, false, false,false);
		addDecoratorItem(getPackageName() + ".MapDecorator");
		addDecoratorItem(getPackageName() + ".ComparableDecorator");
		removeDecoratorItem(getPackageName() + ".ComparableDecorator");
		checkResult("decorators", ".MapDecorator");
	}
		
	
	@Test
	public void testStereotypes() {
		beansHelper.createStereotype("S1",getPackageName(),false,false,false,false,false);
		beansHelper.createStereotype("S2",getPackageName(),false,false,false,false,false);
		beansHelper.createStereotype("S3",getPackageName(),false,false,false,false,false);
		addStereotypeItem(getPackageName() + ".S2");
		addStereotypeItem(getPackageName() + ".S3");
		removeStereotypeItem(getPackageName() + ".S3");
		addStereotypeItem(getPackageName() + ".S1");
		removeStereotypeItem(getPackageName() + ".S2");
		checkResult("alternatives", ".S1");
	}
	
	private String getDocumentText() {
		EditorPartWrapper beans = new EditorPartWrapper();
		beans.activateSourcePage();
		String text = new DefaultStyledText().getText();
		return text;
	}
	
	private void checkResult(String type, String elementName) {
		String documentText = getDocumentText();
		List<String> nodeList = getItems(documentText, type);
		assertTrue(containsItem(nodeList, getPackageName() + elementName));
	}
	
	private void addClassItem(String className){
		EditorPartWrapper beans = beansXMLHelper.openBeansXml(PROJECT_NAME);
		beans.activateTreePage();
		try {
			beans.addClasses(className);
			Assert.assertTrue(beans.isDirty());
			beans.activateSourcePage();
			String text = new DefaultStyledText().getText();
			List<String> nl = getItems(text, "classes");
			assertTrue(containsItem(nl, className));
		} finally {
			if (beans.isDirty()) {
				beans.save();
			}
		}
	}
	
	private void addInterceptorItem(String interceptorName){
		EditorPartWrapper beans = beansXMLHelper.openBeansXml(PROJECT_NAME);
		beans.activateTreePage();
		try {
			beans.addInterceptors(interceptorName);
			Assert.assertTrue(beans.isDirty());
			beans.activateSourcePage();
			String text = new DefaultStyledText().getText();
			List<String> nl = getItems(text, "interceptors");
			assertTrue(containsItem(nl, interceptorName));
		} finally {
			if (beans.isDirty()) {
				beans.save();
			}
		}
	}
	
	private void addStereotypeItem(String stereotypeName){
		EditorPartWrapper beans = beansXMLHelper.openBeansXml(PROJECT_NAME);
		beans.activateTreePage();
		try {
			beans.addStereotypes(stereotypeName);
			Assert.assertTrue(beans.isDirty());
			beans.activateSourcePage();
			String text = new DefaultStyledText().getText();
			List<String> nl = getItems(text, "stereotypes");
			assertTrue(containsItem(nl, stereotypeName));
		} finally {
			if (beans.isDirty()) {
				beans.save();
			}
		}
	}
	
	private void addDecoratorItem(String decoratorName){
		EditorPartWrapper beans = beansXMLHelper.openBeansXml(PROJECT_NAME);
		beans.activateTreePage();
		try {
			beans.addDecorators(decoratorName);
			Assert.assertTrue(beans.isDirty());
			beans.activateSourcePage();
			beans.save();
			String text = new DefaultStyledText().getText();
			List<String> nl = getItems(text, "decorators");
			assertTrue(containsItem(nl, decoratorName));
		} finally {
			if (beans.isDirty()) {
				beans.save();
			}
		}
	}
	
	private void removeDecoratorItem(String decoratorName){
		EditorPartWrapper beans = new EditorPartWrapper();
		beans.activateTreePage();
		try {
			beans.removeDecorators(decoratorName);
			Assert.assertTrue(beans.isDirty());
			beans.activateSourcePage();
			String text = new DefaultStyledText().getText();
			List<String> nl = getItems(text, "decorators");
			assertFalse(containsItem(nl, decoratorName));
		} finally {
			if (beans.isDirty()) {
				beans.save();
			}
		}
	}
	
	private void removeInterceptorItem(String interceptorName){
		EditorPartWrapper beans = new EditorPartWrapper();
		beans.activateTreePage();
		try {
			beans.removeInterceptors(interceptorName);
			Assert.assertTrue(beans.isDirty());
			beans.activateSourcePage();
			String text = new DefaultStyledText().getText();
			List<String> nl = getItems(text, "interceptors");
			assertFalse(containsItem(nl, interceptorName));
		} finally {
			if (beans.isDirty()) {
				beans.save();
			}
		}
	}
	
	private void removeClassItem(String className){
		EditorPartWrapper beans = new EditorPartWrapper();
		beans.activateTreePage();
		try {
			beans.removeClasses(className);
			Assert.assertTrue(beans.isDirty());
			beans.activateSourcePage();
			String text = new DefaultStyledText().getText();
			List<String> nl = getItems(text, "classes");
			assertFalse(containsItem(nl, className));
		} finally {
			if (beans.isDirty()) {
				beans.save();
			}
		}
	}
	
	private void removeStereotypeItem(String stereotypeName){
		EditorPartWrapper beans = new EditorPartWrapper();
		beans.activateTreePage();
		try {
			beans.removeStereotypes(stereotypeName);
			Assert.assertTrue(beans.isDirty());
			beans.activateSourcePage();
			String text = new DefaultStyledText().getText();
			List<String> nl = getItems(text, "stereotypes");
			assertFalse(containsItem(nl, stereotypeName));
		} finally {
			if (beans.isDirty()) {
				beans.save();
			}
		}
	}
	
	private Document getDocument(String text) {
		Document d = null;
		try {
			d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(text.getBytes()));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return d;
	}
	
	private List<String> getItems(String doc, String item) {
		Document d = getDocument(doc);
		
		if(item.equals("classes") || item.equals("stereotypes")){
			item = "alternatives";
		}
		
		NodeList nl = d.getElementsByTagName(item);
		
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (item.equals(n.getNodeName())) {
				System.out.println("matched node "+n.getNodeName());
				NodeList cl = n.getChildNodes();
				for(int y=0;y< cl.getLength(); y++){
					if(!cl.item(y).getNodeName().equals("#text")){
						list.add(cl.item(y).getTextContent());
					}
				}
			}
		}
		return list;
	}
	

	private boolean containsItem(List<String> nl, String name) {
		if (nl == null) {
			return false;
		}
		for(String s: nl){
			if(name.equals(s)){
				return true;
			}
		}
		return false;
	}
	
}
