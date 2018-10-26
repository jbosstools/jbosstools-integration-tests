/*******************************************************************************
 * Copyright (c) 2010-2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.bot.test.beansxml.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.eclipse.reddeer.common.exception.WaitTimeoutExpiredException;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.NewClassCreationWizard;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.workbench.condition.EditorHasValidationMarkers;
import org.eclipse.reddeer.workbench.impl.editor.Marker;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
public class BeansXMLDiscoveryModesTemplate extends CDITestBase{
	
	@Before
	public void addBeans(){
		createClass(PROJECT_NAME, "Bean1");
		createClass(PROJECT_NAME, "Bean2");
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).getProjectItem(CDIConstants.JAVA_RESOURCES,CDIConstants.SRC,"test","Bean1.java").open();
		TextEditor ed = new TextEditor("Bean1.java");
		ed.insertLine(1, "import javax.inject.Inject;");
		ed.insertLine(4, "@Inject");
		ed.insertLine(5, "Bean2 bean2;");
		ed.save();
	}
	
	@After
	public void clean(){
		deleteAllProjects();
	}
	
	@Test
	public void testModeAll(){
		prepareBeanXml("all", false);
		TextEditor ed = new TextEditor("Bean1.java");
		try{
			new WaitWhile(new EditorHasValidationMarkers(ed));
		} catch (WaitTimeoutExpiredException ex){
			fail("this is known issue JBIDE-18964");
		}
		List<Marker> markers = ed.getMarkers();
		assertEquals(0,markers.size());
	}
	
	@Test
	public void testModeAnnotated(){
		prepareBeanXml("annotated", false);
		TextEditor ed = new TextEditor("Bean1.java");
		List<Marker> markers = ed.getMarkers();
		assertEquals(0,markers.size());
		ed.insertLine(1, "import javax.enterprise.context.ApplicationScoped;");
		ed.insertLine(3, "@ApplicationScoped");
		ed.save();
		new WaitUntil(new EditorHasValidationMarkers(ed, 7));
		markers = ed.getMarkers();
		assertEquals(1,markers.size());
		assertEquals("No bean is eligible for injection to the injection point [JSR-346 §5.2.2]",markers.get(0).getText());
		assertEquals(7,markers.get(0).getLineNumber());
		
		ed = new TextEditor("Bean2.java");
		ed.insertLine(1, "import javax.enterprise.context.ApplicationScoped;");
		ed.insertLine(2, "@ApplicationScoped");
		ed.save();
		
		ed = new TextEditor("Bean1.java");
		new WaitWhile(new EditorHasValidationMarkers(ed),TimePeriod.DEFAULT, false);
		markers = ed.getMarkers();
		try{
			assertEquals(0,markers.size());
		} catch (AssertionError ex){
			fail("this is known issue JBIDE-18964");
		}
	}
	
	@Test
	public void testModeNone(){
		prepareBeanXml("none", false);
		TextEditor ed = new TextEditor("Bean1.java");
		new WaitWhile(new EditorHasValidationMarkers(ed),TimePeriod.DEFAULT, false);
		List<Marker> markers = ed.getMarkers();
		assertEquals(0,markers.size());
	}
	
	@Test
	public void testWithoutBeansXml(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).getProjectItem("WebContent","WEB-INF","beans.xml").delete();
		TextEditor ed = new TextEditor("Bean1.java");
		new WaitUntil(new EditorHasValidationMarkers(ed),TimePeriod.DEFAULT, false);
		List<Marker> markers = ed.getMarkers();
		assertEquals(0,markers.size());
		ed.insertLine(1, "import javax.enterprise.context.ApplicationScoped;");
		ed.insertLine(3, "@ApplicationScoped");
		ed.save();
		new WaitUntil(new EditorHasValidationMarkers(ed,7));
		markers = ed.getMarkers();
		assertEquals(1,markers.size());
		assertEquals("No bean is eligible for injection to the injection point [JSR-346 §5.2.2]",markers.get(0).getText());
		assertEquals(7,markers.get(0).getLineNumber());
		
		ed = new TextEditor("Bean2.java");
		ed.insertLine(1, "import javax.enterprise.context.ApplicationScoped;");
		ed.insertLine(3, "@ApplicationScoped");
		ed.save();
		
		ed = new TextEditor("Bean1.java");
		new WaitWhile(new EditorHasValidationMarkers(ed),TimePeriod.DEFAULT, false);
		markers = ed.getMarkers();
		assertEquals(0,markers.size());
		
		
	}
	
	private void createClass(String project, String className){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(project);
		NewClassCreationWizard c = new NewClassCreationWizard();
		c.open();
		NewClassWizardPage page = new NewClassWizardPage(c);
		page.setPackage("test");
		page.setName(className);
		c.finish();
	}
	
	
}
