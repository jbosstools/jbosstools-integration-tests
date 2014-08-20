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

import static org.junit.Assert.*;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
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
public class VetoAnnotationTest extends SolderAnnotationTestBase {

	@After
	public void waitForJobs() {
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		for(Project p: pe.getProjects()){
			p.delete(true);
		}
	} 
	
	@Test
	public void testManagedBeans() {
		
		String vetoBean = "Bean";
		String otherBean = "OtherBean";
		String projectName = "veto1";
		
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1);
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), APPLICATION_CLASS).open();
		new DefaultEditor(APPLICATION_CLASS);
		
		testNoBeanValidationProblemExists(projectName);
		
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), otherBean + ".java").open();
		new DefaultEditor(otherBean + ".java");
		
		editResourceUtil.replaceInEditor("public class " + otherBean,
				"public class " + otherBean + " extends " + vetoBean);
		
		testProperInjectBean(projectName, "bean", otherBean);
		
	}
	
	@Test
	public void testSessionBean() {
		
		String vetoBean = "Bean";
		String otherBean = "OtherBean";
		String projectName = "veto2";
		
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1);
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), APPLICATION_CLASS).open();
		new DefaultEditor(APPLICATION_CLASS);
		
		testNoBeanValidationProblemExists(projectName);
		
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), otherBean + ".java").open();
		new DefaultEditor(otherBean + ".java");
		editResourceUtil.replaceInEditor("public class " + otherBean,
				"public class " + otherBean + " extends " + vetoBean);
		
		testProperInjectBean(projectName, "bean", otherBean);
		
	}
	
	@Test
	public void testProducerMethod() {
		
		String vetoBean = "Bean";
		String projectName = "veto3";
		
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1);
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), APPLICATION_CLASS).open();
		new DefaultEditor(APPLICATION_CLASS);
		
		testNoBeanValidationProblemExists(projectName);
		
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), vetoBean + ".java").open();
		new DefaultEditor(vetoBean + ".java");
		editResourceUtil.replaceInEditor("@Veto", "");
		editResourceUtil.replaceInEditor("import org.jboss.solder.core.Veto;", "");
		
		testProperInjectProducer(projectName, "manager", vetoBean, "getManager");
		
	}
	
	@Test
	public void testProducerField() {
		
		String vetoBean = "Bean";
		String projectName = "veto4";
		
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1);
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), APPLICATION_CLASS).open();
		new DefaultEditor(APPLICATION_CLASS);
		
		testNoBeanValidationProblemExists(projectName);
		
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), vetoBean + ".java").open();
		new DefaultEditor(vetoBean + ".java");
		editResourceUtil.replaceInEditor("@Veto", "");
		editResourceUtil.replaceInEditor("import org.jboss.solder.core.Veto;", "");
		
		testProperInjectProducer(projectName, "manager", vetoBean,  "manager");
		
	}
	
	@Test
	public void testObserverMethods() {
		
		String vetoBean = "Bean";
		String projectName = "veto5";
		String eventAttribute = "eventAttribute";
		
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1);
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), APPLICATION_CLASS).open();
		new DefaultEditor(APPLICATION_CLASS);
		
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), vetoBean + ".java").open();
		new DefaultEditor(vetoBean + ".java");
		editResourceUtil.replaceInEditor("@Veto", "");
		editResourceUtil.replaceInEditor("import org.jboss.solder.core.Veto;", "");
		
		AbstractWait.sleep(TimePeriod.SHORT);; // wait a while for CDI validator to validate observer  
		
		TextEditor te = new TextEditor(APPLICATION_CLASS);
		te.selectText(eventAttribute);
		ContentAssistant ca = te.openOpenOnAssistant();
		for(String p: ca.getProposals()){
			if(p.contains(CDIConstants.OPEN_CDI_OBSERVER_METHOD)){
				ca.chooseProposal(p);
				break;
			}
		}
		TextEditor t = new TextEditor(vetoBean + ".java");
		
		String selectedString = t.getSelectedText();
		
		assertTrue("'method' should be selected. " +
				"Actual selected text: " + selectedString,selectedString.
				equals("method"));
		
	}
	
}
