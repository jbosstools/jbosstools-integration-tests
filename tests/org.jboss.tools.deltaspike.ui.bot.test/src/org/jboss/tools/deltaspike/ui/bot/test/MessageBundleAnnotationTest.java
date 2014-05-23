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


import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.regex.Regex;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.deltaspike.ui.bot.test.condition.SpecificProblemExists;
import org.junit.After;
import org.junit.Test;

/**
 * Test @MessageBundle annotation, with following approach:
 * 
 * 1. try to inject interface directly (no bean eligible validation problem)
 * 2. annotate interface with MessageBundle annotation -> deltaspike will 
 * 	  automatically create producer for interface annotated with such an annotation
 *    -> no validation marker 
 * 
 * @author jjankovi
 *
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
public class MessageBundleAnnotationTest extends DeltaspikeTestBase {

	private Regex validationProblemRegex = new Regex(
			"No bean is eligible.*");
	
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
	public void testInjectMessageBundleInterface() {
		
		String projectName = "messageBundle";
		importDeltaspikeProject(projectName,sr);
		
		new WaitUntil(new SpecificProblemExists(
				validationProblemRegex), TimePeriod.NORMAL);

		insertIntoFile(projectName, "test", "CustomInterface.java", 2, 0, "@MessageBundle");
		insertIntoFile(projectName, "test", "CustomInterface.java", 1, 0, 
				"import org.apache.deltaspike.core.api.message.MessageBundle; \n");
		
		new WaitWhile(new SpecificProblemExists(
				validationProblemRegex), TimePeriod.NORMAL);
		
	}
}
