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
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
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
public class RequiresAnnotationTest extends SolderAnnotationTestBase {

	@InjectRequirement
    private ServerRequirement sr;
	
	@After
	public void waitForJobs() {
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.deleteAllProjects();
	}

	@Test
	public void testManagedBeans() {

		String managerProducer = "ManagerProducer";
		String manager = "Manager";
		String projectName = "requires1";

		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1, sr.getRuntimeNameLabelText(sr.getConfig()));

		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), APPLICATION_CLASS).open();
		new DefaultEditor(APPLICATION_CLASS);

		testNoBeanValidationProblemExists(projectName);
		
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), managerProducer + ".java").open();
		new DefaultEditor(managerProducer + ".java");
		
		editResourceUtil.replaceInEditor("@Requires(\"cdi.test." + manager
				+ "\")", "@Requires(\"" + getPackageName() + "." + manager
				+ "\")");

		testProperInjectBean(projectName, "managerProducer", managerProducer);

	}

	@Test
	public void testSessionBean() {

		String managerProducer = "ManagerProducer";
		String manager = "Manager";
		String projectName = "requires2";

		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1, sr.getRuntimeNameLabelText(sr.getConfig()));

		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), APPLICATION_CLASS).open();
		new DefaultEditor(APPLICATION_CLASS);

		testNoBeanValidationProblemExists(projectName);

		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), managerProducer + ".java").open();
		new DefaultEditor(managerProducer + ".java");
		
		editResourceUtil.replaceInEditor("@Requires(\"cdi.test." + manager
				+ "\")", "@Requires(\"" + getPackageName() + "." + manager
				+ "\")");

		testProperInjectBean(projectName, "managerProducer", managerProducer);

	}

	@Test
	public void testProducerMethod() {

		String managerProducer = "ManagerProducer";
		String manager = "Manager";
		String projectName = "requires3";

		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1, sr.getRuntimeNameLabelText(sr.getConfig()));

		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), APPLICATION_CLASS).open();
		new DefaultEditor(APPLICATION_CLASS);

		testNoBeanValidationProblemExists(projectName);

		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), managerProducer + ".java").open();
		new DefaultEditor(managerProducer + ".java");
		
		editResourceUtil.replaceInEditor("@Requires(\"cdi.test." + manager
				+ "\")", "@Requires(\"" + getPackageName() + "." + manager
				+ "\")");

		testProperInjectProducer(projectName, "managerProducer",
				managerProducer, "getManagerProducer");

	}

	@Test
	public void testProducerField() {

		String managerProducer = "ManagerProducer";
		String manager = "Manager";
		String projectName = "requires4";

		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1, sr.getRuntimeNameLabelText(sr.getConfig()));

		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), APPLICATION_CLASS).open();
		new DefaultEditor(APPLICATION_CLASS);

		testNoBeanValidationProblemExists(projectName);

		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), managerProducer + ".java").open();
		new DefaultEditor(managerProducer + ".java");
		
		editResourceUtil.replaceInEditor("@Requires(\"cdi.test." + manager
				+ "\")", "@Requires(\"" + getPackageName() + "." + manager
				+ "\")");

		testProperInjectProducer(projectName, "managerProducer",
				managerProducer, "managerProducer");

	}

	@Test
	public void testObserverMethods() {

		String managerProducer = "ManagerProducer";
		String manager = "Manager";
		String projectName = "requires5";
		String eventAttribute = "eventAttribute";

		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1, sr.getRuntimeNameLabelText(sr.getConfig()));

		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), APPLICATION_CLASS).open();
		new DefaultEditor(APPLICATION_CLASS);
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, getPackageName(), managerProducer + ".java").open();
		new DefaultEditor(managerProducer + ".java");
		
		editResourceUtil.replaceInEditor("@Requires(\"cdi.test." + manager
				+ "\")", "@Requires(\"" + getPackageName() + "." + manager
				+ "\")");

		AbstractWait.sleep(TimePeriod.SHORT);// wait a while for CDI validator to validate observer
		
		TextEditor te = new TextEditor(APPLICATION_CLASS);
		te.selectText(eventAttribute);
		ContentAssistant ca = te.openOpenOnAssistant();
		for(String p: ca.getProposals()){
			if(p.contains(CDIConstants.OPEN_CDI_OBSERVER_METHOD)){
				ca.chooseProposal(p);
				break;
			}
		}
		TextEditor t = new TextEditor(managerProducer + ".java");
		String selectedString = t.getSelectedText();
		assertTrue("'method' should be selected. " + "Actual selected text: "
				+ selectedString, selectedString.equals("method"));

	}

}
