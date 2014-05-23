/*******************************************************************************
 * Copyright (c) 2010-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.seam3.bot.test.tests;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.seam3.bot.test.base.SolderAnnotationTestBase;
import org.jboss.tools.cdi.seam3.bot.test.util.SeamLibrary;
import org.junit.After;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
public class ExactAnnotationTest extends SolderAnnotationTestBase {

	private static final String EXACT_INTERFACE = "exact-interface";
	private static final String EXACT_BEANS = "exact-beans";
	
	@After
	public void waitForJobs() {
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		for(Project p: pe.getProjects()){
			p.delete(true);
		}
	} 
	
	@Test
	public void testExactAnnotationForInterface() {
		
		testExactAnnotationsForProject(EXACT_INTERFACE);
		
	}
	
	@Test
	public void testExactAnnotationForBeans() {
		
		testExactAnnotationsForProject(EXACT_BEANS);
		
	}
	
	private void testExactAnnotationsForProject(String projectName) {

		String managerClass = "Manager.class";
		String peopleManager = "PeopleManager";
		String otherManager = "OtherManager";
		
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1);
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), APPLICATION_CLASS).open();
		new DefaultEditor(APPLICATION_CLASS);
		
		testMultipleBeansValidationProblemExists(projectName);
				
		editResourceUtil.replaceInEditor(managerClass, peopleManager + ".class");
		testProperInjectBean(projectName, peopleManager + ".class", 
				peopleManager);
		
		new DefaultEditor(APPLICATION_CLASS);
		
		editResourceUtil.replaceInEditor(peopleManager + ".class", otherManager + ".class");
		testProperInjectBean(projectName, otherManager + ".class", 
				otherManager);
		
	}
	
}