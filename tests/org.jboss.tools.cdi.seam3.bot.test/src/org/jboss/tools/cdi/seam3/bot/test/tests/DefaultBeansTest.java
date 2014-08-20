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

import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewBeanCreationWizard;
import org.jboss.tools.cdi.seam3.bot.test.base.SolderAnnotationTestBase;
import org.jboss.tools.cdi.seam3.bot.test.uiutils.AssignableBeansDialogExt;
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
public class DefaultBeansTest extends SolderAnnotationTestBase {

	private static String projectName = "defaultBeans";

	@After
	public void waitForJobs() {
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		for(Project p: pe.getProjects()){
			p.delete(true);
		}
	}

	@Test
	public void testProperAssign() {

		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1);
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC,getPackageName(), APPLICATION_CLASS).open();

		TextEditor te = new TextEditor(APPLICATION_CLASS);
		te.selectText("managerImpl");
		ContentAssistant ca = te.openOpenOnAssistant();
		for(String p: ca.getProposals()){
			if(p.contains(CDIConstants.OPEN_INJECT_BEAN)){
				ca.chooseProposal(p);
				break;
			}
		}
		new TextEditor("DefaultOne.java");

	}

	@Test
	public void testProperAssignAlternativesDeactive() {

		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1);
		
		NewBeanCreationWizard bw = new NewBeanCreationWizard();
		bw.open();
		bw.setPackage(getPackageName());
		bw.setName("ManagerImpl");
		bw.setPublic(true);
		bw.setAbstract(false);
		bw.setFinal(false);
		bw.setGenerateComments(false);
		bw.setAlternative(true);
		bw.setRegisterInBeans(false);
		bw.addInterfaces("Manager");
		bw.finish();
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC,getPackageName(), APPLICATION_CLASS).open();

		TextEditor te = new TextEditor(APPLICATION_CLASS);
		te.selectText("managerImpl");
		ContentAssistant ca = te.openOpenOnAssistant();
		ca.chooseProposal(CDIConstants.SHOW_ALL_ASSIGNABLE);

		AssignableBeansDialogExt assignDialog = new AssignableBeansDialogExt();

		List<String> allBeans = assignDialog.getAllBeans();
		assertTrue(allBeans.size() == 2);
		assignDialog.hideUnavailableBeans();
		allBeans = assignDialog.getAllBeans();
		assertTrue(allBeans.size() == 1);

		allBeans = assignDialog.getAllBeans();
		assertTrue(allBeans.size() == 1);
		assertTrue(allBeans.get(0).contains("DefaultOne"));
		
		assignDialog.close();
		
		te = new TextEditor(APPLICATION_CLASS);
		te.selectText("managerImpl");
		ca = te.openOpenOnAssistant();
		for(String p: ca.getProposals()){
			if(p.contains(CDIConstants.OPEN_INJECT_BEAN)){
				ca.chooseProposal(p);
				break;
			}
		}
		new TextEditor("DefaultOne.java");
	}

	@Test
	public void testProperUnassign() {

		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1);
		
		NewBeanCreationWizard bw = new NewBeanCreationWizard();
		bw.open();
		bw.setPackage(getPackageName());
		bw.setName("ManagerImpl");
		bw.setPublic(true);
		bw.setAbstract(false);
		bw.setFinal(false);
		bw.setGenerateComments(false);
		bw.setAlternative(false);
		bw.setRegisterInBeans(false);
		bw.addInterfaces("Manager");
		bw.finish();
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC,getPackageName(), APPLICATION_CLASS).open();

		TextEditor te = new TextEditor(APPLICATION_CLASS);
		te.selectText("managerImpl");
		ContentAssistant ca = te.openOpenOnAssistant();
		ca.chooseProposal(CDIConstants.SHOW_ALL_ASSIGNABLE);

		AssignableBeansDialogExt assignDialog = new AssignableBeansDialogExt();

		List<String> allBeans = assignDialog.getAllBeans();
		assertTrue(allBeans.size() == 2);
		assignDialog.hideDefaultBeans();
		allBeans = assignDialog.getAllBeans();
		assertTrue(allBeans.size() == 1);

		allBeans = assignDialog.getAllBeans();
		assertTrue(allBeans.size() == 1);
		assertTrue(allBeans.get(0).contains("ManagerImpl"));
		
		assignDialog.close();
		
		te = new TextEditor(APPLICATION_CLASS);
		te.selectText("managerImpl");
		ca = te.openOpenOnAssistant();
		for(String p: ca.getProposals()){
			if(p.contains(CDIConstants.OPEN_INJECT_BEAN)){
				ca.chooseProposal(p);
				break;
			}
		}
		new TextEditor("ManagerImpl.java");

	}

	@Test
	public void testProperUnassignAlternativesActive() {

		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1);
		
		NewBeanCreationWizard bw = new NewBeanCreationWizard();
		bw.open();
		bw.setPackage(getPackageName());
		bw.setName("ManagerImpl");
		bw.setPublic(true);
		bw.setAbstract(false);
		bw.setFinal(false);
		bw.setGenerateComments(false);
		bw.setAlternative(true);
		bw.setRegisterInBeans(true);
		bw.addInterfaces("Manager");
		bw.finish();
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC,getPackageName(), APPLICATION_CLASS).open();

		TextEditor te = new TextEditor(APPLICATION_CLASS);
		te.selectText("managerImpl");
		ContentAssistant ca = te.openOpenOnAssistant();
		ca.chooseProposal(CDIConstants.SHOW_ALL_ASSIGNABLE);

		AssignableBeansDialogExt assignDialog = new AssignableBeansDialogExt();

		List<String> allBeans = assignDialog.getAllBeans();
		assertTrue(allBeans.size() == 2);
		assignDialog.hideDefaultBeans();
		allBeans = assignDialog.getAllBeans();
		assertTrue(allBeans.size() == 1);
		assignDialog.showDefaultBeans();
		assignDialog.hideAmbiguousBeans();
		assertTrue(allBeans.size() == 1);

		allBeans = assignDialog.getAllBeans();
		assertTrue(allBeans.size() == 1);
		assertTrue(allBeans.get(0).contains("ManagerImpl"));
		
		assignDialog.close();
		
		te = new TextEditor(APPLICATION_CLASS);
		te.selectText("managerImpl");
		ca = te.openOpenOnAssistant();
		for(String p: ca.getProposals()){
			if(p.contains(CDIConstants.OPEN_INJECT_BEAN)){
				ca.chooseProposal(p);
				break;
			}
		}
		new TextEditor("ManagerImpl.java");;

	}

}
