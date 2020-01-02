/*******************************************************************************
 * Copyright (c) 2010-2019 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.bot.test.beansxml.discovery.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.eclipse.reddeer.common.exception.WaitTimeoutExpiredException;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.workbench.condition.EditorHasValidationMarkers;
import org.eclipse.reddeer.workbench.impl.editor.Marker;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.junit.After;
import org.junit.Before;

/**
 * Tempalte class for tests covering JBIDE-23725
 * @author odockal
 *
 */
public class BeanDiscoveryInArchivesTemplate extends CDITestBase {

	@Before
	public void setupProjectAccordingToGivenValues() {
		valuesToInsertDuringProjectSetup.put(1, "import javax.inject.Inject;");
		valuesToInsertDuringProjectSetup.put(2, "import javax.enterprise.context.RequestScoped;");
		valuesToInsertDuringProjectSetup.put(4, "@RequestScoped");
		valuesToInsertDuringProjectSetup.put(6, "@Inject CdiBean1 bean1;");
		super.setupProject(valuesToInsertDuringProjectSetup);
	}
	
	@After
	public void clean() {
		cleanUp();
	}
	
	public void validationOfBeanDiscoveryInExplicitArchives() {
		prepareBeanXml("all", false);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).getProjectItem(CDIConstants.JAVA_RESOURCES, CDIConstants.SRC, "test", CDI_BEAN_1_JAVA_FILE_NAME + JAVA_FILE_EXTENSION).open();
		TextEditor ed = new TextEditor(CDI_BEAN_2_JAVA_FILE_NAME + JAVA_FILE_EXTENSION);
		ed.insertLine(7, "@Inject String warningHere;");
		ed.save();
		try{
			new WaitUntil(new EditorHasValidationMarkers(ed));
		} catch (WaitTimeoutExpiredException ex){
			fail("There is supposed to be warning present");
		}
		List<Marker> markers = ed.getMarkers();
		assertEquals(1, markers.size());
		Marker validation = markers.get(0);
		assertEquals(validation.getLineNumber(), 8);
		String jsrVersion = "2.0".equals(CDIVersion) ? JSR_365 : "JSR-346";
		
		// JBIDE-26664
		if (CDIVersion.equals("1.2")) {
			jsrVersion = JSR_365;
		}
		
		assertTrue(validation.getText().contains("No bean is eligible for injection to the injection point [" + jsrVersion + " §5.2.2]"));
	}
	
	public void validationOfBeanDiscoveryInImplicitArchives() {
		prepareBeanXml("annotated", false);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).getProjectItem(CDIConstants.JAVA_RESOURCES, CDIConstants.SRC, "test", CDI_BEAN_1_JAVA_FILE_NAME + JAVA_FILE_EXTENSION).open();
		TextEditor ed = new TextEditor(CDI_BEAN_2_JAVA_FILE_NAME + JAVA_FILE_EXTENSION);
		try{
			new WaitUntil(new EditorHasValidationMarkers(ed));
		} catch (WaitTimeoutExpiredException ex){
			fail("There is supposed to be warning present");
		}
		List<Marker> markers = ed.getMarkers();
		assertEquals(1, markers.size());
		Marker validation = markers.get(0);
		assertEquals(validation.getLineNumber(), 7);
		String jsrVersion = "2.0".equals(CDIVersion) ? JSR_365 : "JSR-346";
		
		// JBIDE-26664
		if (CDIVersion.equals("1.2")) {
			jsrVersion = JSR_365;
		}
		
		assertTrue(validation.getText().contains("No bean is eligible for injection to the injection point [" + jsrVersion + " §5.2.2]"));
	}
}
