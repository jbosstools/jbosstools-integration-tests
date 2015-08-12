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

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.deltaspike.ui.bot.test.condition.SpecificProblemExists;
import org.junit.After;
import org.junit.Test;

/**
 * Test @ConfigProperty annotation, two approaches:
 * 
 * 1. When config property is a valid injection point
 * 2. When config property is not a valid injection point
 * 
 * @author jjankovi
 *
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
public class ConfigPropertyAnnotationTest extends DeltaspikeTestBase {

	private RegexMatcher validationProblemRegexMatcher = new RegexMatcher("No bean is eligible.*");
	
	@InjectRequirement
	private ServerRequirement sr;
	
	@After
	public void closeAllEditors() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		for(Project p: pe.getProjects()){
			p.delete(true);
		}
	}

	
	@Test
	public void testInjectSupportedConfigProperty() {
		
		String projectName = "configProperty-support";
		importDeltaspikeProject(projectName,sr);
		AbstractWait.sleep(TimePeriod.getCustom(TimePeriod.NORMAL.getSeconds()*2));
		
		new WaitUntil(new SpecificProblemExists(
				validationProblemRegexMatcher), TimePeriod.LONG);

		insertIntoFile(projectName, "test", "Test.java", 7, 0, 
				"@ConfigProperty(name = \"boolean\") \n");
		insertIntoFile(projectName, "test", "Test.java", 2, 0, 
				"import org.apache.deltaspike.core.api.config.ConfigProperty; \n");
		
		new WaitWhile(new SpecificProblemExists(
				validationProblemRegexMatcher), TimePeriod.LONG);
		
	}
	
	@Test
	public void testInjectUnsupportedConfigProperty() {
		
		String projectName = "configProperty-unsupport";
		importDeltaspikeProject(projectName,sr);
		AbstractWait.sleep(TimePeriod.getCustom(TimePeriod.NORMAL.getSeconds()*2));
		
		new WaitWhile(new SpecificProblemExists(
				validationProblemRegexMatcher), TimePeriod.LONG);

		insertIntoFile(projectName, "test", "Test.java", 8, 0, 
				"@ConfigProperty(name = \"boolean\") \n");
		insertIntoFile(projectName, "test", "Test.java", 2, 0, 
				"import org.apache.deltaspike.core.api.config.ConfigProperty; \n");
		try{
			new WaitUntil(new SpecificProblemExists(
					validationProblemRegexMatcher), TimePeriod.LONG);
		} catch(WaitTimeoutExpiredException ex){
			fail("this is known issue JBIDE-13554");
		}
	}
	
}
