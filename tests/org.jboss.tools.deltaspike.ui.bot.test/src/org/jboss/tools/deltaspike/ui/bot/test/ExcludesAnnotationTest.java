/*******************************************************************************
 * Copyright (c) 2010-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.deltaspike.ui.bot.test;

import static org.junit.Assert.*;
import org.hamcrest.core.IsEqual;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.regex.Regex;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.deltaspike.ui.bot.test.condition.SpecificProblemExists;
import org.junit.After;
import org.junit.Test;

/**
 * Test @Exclude annotation, the base TCs are (not for events-observer):
 * 
 * 1. check there is "multiple beans eligible" validation problem
 * 2. one of two eligible beans annotate @Exclude (import necessary packages)
 * 		a) in case of testPackage, whole package is annotated @Exclude
 * 3. check there is no validation problem (one of two classes is excluded)
 *		a) in case of testPackage, one of two classes is located in excluded package
 * 4. open on injected point to check annotation works correctly   
 * 
 * @author jjankovi
 *
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
public class ExcludesAnnotationTest extends DeltaspikeTestBase {

	private Regex validationProblemRegex = new Regex(
			"Multiple beans are eligible for injection.*");
	
	@InjectRequirement
	private ServerRequirement sr;

	@After
	public void closeAllEditors() {
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		for(Project p: pe.getProjects()){
			p.delete(true);
		}
	}
	
	@Test
	public void testManagedBeans() {

		String projectName = "exclude-mb";
		importDeltaspikeProject(projectName,sr);
		
		new WaitUntil(new SpecificProblemExists(
				validationProblemRegex), TimePeriod.LONG);

		annotateBean(projectName, "test", "InterfaceImpl1.java", 1, 0, "@Exclude");
		
		new WaitWhile(new SpecificProblemExists(
				validationProblemRegex), TimePeriod.LONG);
		
		checkInjectedPoint(projectName, "Test.java", "CustomInterface",
				"InterfaceImpl2.java");
		
	}

	@Test
	public void testSessionBean() {
		
		String projectName = "exclude-sb";
		importDeltaspikeProject(projectName,sr);
		
		new WaitUntil(new SpecificProblemExists(validationProblemRegex), 
				TimePeriod.LONG);
		
		annotateBean(projectName, "test", "InterfaceImpl1.java", 4, 0, "@Exclude");

		new WaitWhile(new SpecificProblemExists(validationProblemRegex), 
				TimePeriod.LONG);
		
		checkInjectedPoint(projectName, "Test.java", "CustomInterface",
				"InterfaceImpl2.java");
		
	}
	
	@Test
	public void testProducerMethod() {
		
		String projectName = "exclude-pm";
		importDeltaspikeProject(projectName,sr);
		
		new WaitUntil(new SpecificProblemExists(validationProblemRegex), 
				TimePeriod.LONG);

		annotateBean(projectName, "test", "ProducerBean1.java", 3, 0, "@Exclude");
		
		new WaitWhile(new SpecificProblemExists(validationProblemRegex), 
				TimePeriod.LONG);
		
		checkInjectedPoint(projectName, "Test.java", "inter",
				"ProducerBean2.java");
		
	}
	
	@Test
	public void testProducerField() {
	
		String projectName = "exclude-pf";
		importDeltaspikeProject(projectName,sr);
		
		new WaitUntil(new SpecificProblemExists(validationProblemRegex), 
				TimePeriod.LONG);

		annotateBean(projectName, "test", "ProducerField1.java", 3, 0, "@Exclude");
		
		new WaitWhile(new SpecificProblemExists(validationProblemRegex), 
				TimePeriod.LONG);
		
		checkInjectedPoint(projectName, "Test.java", "inter",
				"ProducerField2.java");
		
	}
	
	@Test
	public void testEvents() {
			
		String projectName = "exclude-eo";
		importDeltaspikeProject(projectName,sr);
		
		checkEventsCount(projectName, "Test.java", 2, null);

		annotateBean(projectName, "test", "EventProducer1.java", 4, 0, "@Exclude");
		
		/** will be deprecated once validation messages are added (JBIDE-11899) **/
		AbstractWait.sleep(TimePeriod.SHORT);
		
		checkEventsCount(projectName, "Test.java", 1, "EventProducer2.java");
		
	}
	
	@Test
	public void testPackage() {
	
		String projectName = "exclude-wp";
		importDeltaspikeProject(projectName,sr);
		
		new WaitUntil(new SpecificProblemExists(
				validationProblemRegex), TimePeriod.LONG);
		
		insertIntoFile(projectName, "impl1", "package-info.java", 0, 0, 
				"@org.apache.deltaspike.core.api.exclude.Exclude");
		
		new WaitWhile(new SpecificProblemExists(
				validationProblemRegex), TimePeriod.LONG);
		
		checkInjectedPoint(projectName, "Test.java", "CustomInterface",
				"InterfaceImpl2.java");
		
	}
	
	private void checkInjectedPoint(String projectName, String testBean,
			String injectedPoint, String injectedComponent) {
		
		openClass(projectName, "test", testBean);
		TextEditor e = new TextEditor(testBean);
		e.selectText(injectedPoint);
		ContentAssistant ca = e.openOpenOnAssistant();
		for(String p: ca.getProposals()){
			if(p.contains("Open @Inject")){
				ca.chooseProposal(p);
				break;
			}
		}
		new TextEditor(injectedComponent);
	}
	
	private void checkEventsCount(String projectName, String observer, 
			int expectedCount, String eventProducer) {
		
		openClass(projectName, "test", observer);
		TextEditor e = new TextEditor(observer);
		
		if (eventProducer == null && expectedCount > 1) {
			e.selectText("PaymentEvent event");
			ContentAssistant ca = e.openOpenOnAssistant();
			ca.chooseProposal("Show CDI Events...");
			
			Shell shell = new DefaultShell();
			assertThat(new DefaultTable().rowCount(), IsEqual.equalTo(expectedCount));
			shell.close();
		} else {
			e.selectText("PaymentEvent event");
			ContentAssistant ca = e.openOpenOnAssistant();
			for(String p: ca.getProposals()){
				if(p.contains("Open CDI Event")){
					ca.chooseProposal(p);
					break;
				}
			}
			new TextEditor(eventProducer);
		}
		
	}
	
}
