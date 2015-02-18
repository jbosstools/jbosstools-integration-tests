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
package org.jboss.tools.cdi.bot.test.editor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.common.model.ui.editor.EditorPartWrapper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Test checks if Bean Editor works properly
 * prerequisite - CDIAtWizardTest!!!
 * 
 * 
 * @author Lukas Jungmann
 * @author jjankovi
 */
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
@OpenPerspective(JavaEEPerspective.class)
public class BeansEditorTest extends CDITestBase {

	private static final String descPath = CDIConstants.WEB_INF_BEANS_XML_PATH;
		
	@BeforeClass
	public static void setup() {
		CDITestBase.prepareWorkspaceStatic(CDITestBase.PROJECT_NAME);
		editResourceUtil.copyResource("resources/beans.xml", descPath);
		editResourceUtil.copyResource("resources/Foo.jav_", "src/cdi/Foo.java");
		editResourceUtil.copyResource("resources/Bar.jav_", "src/cdi/Bar.java");
		/**
		 * project should be located in workspace after previous test
		 */
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		assertTrue(pe.containsProject("CDIProject"));
		pe.getProject("CDIProject").getProjectItem("WebContent","WEB-INF","beans.xml").open();
		EditorPartWrapper beans = new EditorPartWrapper();
		beans.save();
	}
	
	@Override
	public void prepareWorkspace() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(getProjectName()).getProjectItem(descPath.split("/")).open();						
	}
			
	@Test
	public void testClasses() {		
		addClassItem(getPackageName() + ".Foo");
		addClassItem(getPackageName() + ".Bar");
		removeClassItem(getPackageName() + ".Foo");
		checkResult("alternatives", ".Bar");
	}
	
	@Test
	public void testInterceptors() {
		addInterceptorItem(getPackageName() + ".I1");
		removeInterceptorItem(getPackageName() + ".I1");
		addInterceptorItem(getPackageName() + ".I2");
		checkResult("interceptors", ".I2");
	}

	
	@Test
	public void testDecorators() {
		addDecoratorItem(getPackageName() + ".MapDecorator");
		addDecoratorItem(getPackageName() + ".ComparableDecorator");
		removeDecoratorItem(getPackageName() + ".ComparableDecorator");
		checkResult("decorators", ".MapDecorator");
	}
		
	
	@Test
	public void testStereotypes() {
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
		EditorPartWrapper beans = new EditorPartWrapper();
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
		EditorPartWrapper beans = new EditorPartWrapper();
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
		EditorPartWrapper beans = new EditorPartWrapper();
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
		EditorPartWrapper beans = new EditorPartWrapper();
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
