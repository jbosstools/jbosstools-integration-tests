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

import static org.junit.Assert.assertThat;

import org.hamcrest.core.IsEqual;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.regex.Regex;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.deltaspike.ui.bot.test.condition.SpecificProblemExists;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.OpenOnHelper;
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
public class ExcludesAnnotationTest extends DeltaspikeTestBase {

	private Regex validationProblemRegex = new Regex(
			"Multiple beans are eligible for injection.*");

	@After
	public void closeAllEditors() {
	//	Bot.get().closeAllEditors();
		projectExplorer.deleteAllProjects();
	}
	
	@Test
	public void testManagedBeans() {

		String projectName = "exclude-mb";
		importDeltaspikeProject(projectName);
		
		new WaitUntil(new SpecificProblemExists(
				validationProblemRegex), TimePeriod.NORMAL);

		annotateBean(projectName, "test", "InterfaceImpl1.java", 1, 0, "@Exclude");
		
		new WaitWhile(new SpecificProblemExists(
				validationProblemRegex), TimePeriod.NORMAL);
		
		checkInjectedPoint(projectName, "Test.java", "CustomInterface",
				"InterfaceImpl2.java");
		
	}

	@Test
	public void testSessionBean() {
		
		String projectName = "exclude-sb";
		importDeltaspikeProject(projectName);
		
		new WaitUntil(new SpecificProblemExists(validationProblemRegex), 
				TimePeriod.NORMAL);
		
		annotateBean(projectName, "test", "InterfaceImpl1.java", 4, 0, "@Exclude");

		new WaitWhile(new SpecificProblemExists(validationProblemRegex), 
				TimePeriod.NORMAL);
		
		checkInjectedPoint(projectName, "Test.java", "CustomInterface",
				"InterfaceImpl2.java");
		
	}
	
	@Test
	public void testProducerMethod() {
		
		String projectName = "exclude-pm";
		importDeltaspikeProject(projectName);
		
		new WaitUntil(new SpecificProblemExists(validationProblemRegex), 
				TimePeriod.NORMAL);

		annotateBean(projectName, "test", "ProducerBean1.java", 3, 0, "@Exclude");
		
		new WaitWhile(new SpecificProblemExists(validationProblemRegex), 
				TimePeriod.NORMAL);
		
		checkInjectedPoint(projectName, "Test.java", "inter",
				"ProducerBean2.java");
		
	}
	
	@Test
	public void testProducerField() {
	
		String projectName = "exclude-pf";
		importDeltaspikeProject(projectName);
		
		new WaitUntil(new SpecificProblemExists(validationProblemRegex), 
				TimePeriod.NORMAL);

		annotateBean(projectName, "test", "ProducerField1.java", 3, 0, "@Exclude");
		
		new WaitWhile(new SpecificProblemExists(validationProblemRegex), 
				TimePeriod.NORMAL);
		
		checkInjectedPoint(projectName, "Test.java", "inter",
				"ProducerField2.java");
		
	}
	
	@Test
	public void testEvents() {
			
		String projectName = "exclude-eo";
		importDeltaspikeProject(projectName);
		
		checkEventsCount(projectName, "Test.java", 2, null);

		annotateBean(projectName, "test", "EventProducer1.java", 4, 0, "@Exclude");
		
		/** will be deprecated once validation messages are added (JBIDE-11899) **/
		AbstractWait.sleep(Timing.time3S());
		
		checkEventsCount(projectName, "Test.java", 1, "EventProducer2.java");
		
	}
	
	@Test
	public void testPackage() {
	
		String projectName = "exclude-wp";
		importDeltaspikeProject(projectName);
		
		new WaitUntil(new SpecificProblemExists(
				validationProblemRegex), TimePeriod.NORMAL);
		
		insertIntoFile(projectName, "impl1", "package-info.java", 0, 0, 
				"@org.apache.deltaspike.core.api.exclude.Exclude");
		
		new WaitWhile(new SpecificProblemExists(
				validationProblemRegex), TimePeriod.NORMAL);
		
		checkInjectedPoint(projectName, "Test.java", "CustomInterface",
				"InterfaceImpl2.java");
		
	}
	
	private void checkInjectedPoint(String projectName, String testBean,
			String injectedPoint, String injectedComponent) {
		
		openClass(projectName, "test", testBean);
		OpenOnHelper.checkOpenOnFileIsOpened(bot, testBean, 
				injectedPoint, "Open @Inject", injectedComponent);
	}
	
	private void checkEventsCount(String projectName, String observer, 
			int expectedCount, String eventProducer) {
		
		openClass(projectName, "test", observer);
		
		if (eventProducer == null && expectedCount > 1) {
			OpenOnHelper.selectOpenOnOption(bot, observer, "PaymentEvent event", 
					"Show CDI Events");
			Shell shell = new DefaultShell();
			assertThat(new DefaultTable().rowCount(), IsEqual.equalTo(expectedCount));
			shell.close();
		} else {
			OpenOnHelper.checkOpenOnFileIsOpened(bot, observer, "PaymentEvent event", 
					"Open CDI Event", eventProducer);
		}
		
	}
	
}
