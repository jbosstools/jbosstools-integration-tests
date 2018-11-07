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
package org.jboss.tools.cdi.bot.test.weld.template;

import static org.junit.Assert.fail;

import org.eclipse.reddeer.common.exception.WaitTimeoutExpiredException;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.NewClassCreationWizard;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.swt.impl.menu.ContextMenu;
import org.eclipse.reddeer.workbench.condition.EditorHasValidationMarkers;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.junit.Before;
import org.junit.Test;

//based on JBIDE-12644
public class WeldParametersAnnotationTemplate extends CDITestBase{
	
	public static final String WELD_SE_JAR=System.getProperty("jbosstools.test.weld-se.home");
	
	@Before
	public void addLibs(){
		projectHelper.addLibrariesIntoProject(PROJECT_NAME, WELD_SE_JAR);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).select();
		new ContextMenu().getItem("Refresh").select();
		new WaitWhile(new JobIsRunning(),TimePeriod.LONG);
		NewClassCreationWizard jd = new NewClassCreationWizard();
		jd.open();
		NewClassWizardPage jp = new NewClassWizardPage(jd);
		jp.setName("ParametersExtension");
		jp.setPackage("cdi.parameters");
		jd.finish();
		
	}
	
	@Test
	public void testParametersAnnotation(){
		TextEditor te = new TextEditor("ParametersExtension.java");
		
		editResourceUtil.replaceClassContentByResource("ParametersExtension.java", 
				readFile("resources/weldParametersAnnotation/ParametersExtension.jav_"), false, false);
		new WaitWhile(new JobIsRunning());
		
		try {
			new WaitWhile(new EditorHasValidationMarkers(te),TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException e) {
			fail("Editor has validation markers: " + te.getMarkers());
		}
		
		te.save();
		new WaitUntil(new JobIsRunning(),TimePeriod.SHORT,false);
		
		try {
			new WaitWhile(new EditorHasValidationMarkers(te),TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException e) {
			fail("Editor has validation markers: " + te.getMarkers());
		}		
	}
}
