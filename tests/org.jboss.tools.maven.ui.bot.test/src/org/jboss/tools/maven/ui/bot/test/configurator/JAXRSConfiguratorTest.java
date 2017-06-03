/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.maven.ui.bot.test.configurator;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.maven.ui.bot.test.utils.ProjectHasNature;
import org.junit.Test;
/**
 * @author Rastislav Wagner
 * 
 */
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY10x)
public class JAXRSConfiguratorTest extends AbstractConfiguratorsTest{
    
    @InjectRequirement
    private ServerRequirement sr;

	@Test
	public void testJAXRSConfiguratorJersey() {
		String projectName = PROJECT_NAME_JAXRS+"_noRuntime";
		
		createWebProject(projectName,null,false);
		convertToMavenProject(projectName, "war", false);
		checkProjectWithoutRuntime(projectName);

		addDependency(projectName, "com.cedarsoft.rest", "jersey", "1.0.0");
		updateConf(projectName);
		new WaitUntil(new ProjectHasNature(projectName, JAXRS_FACET, null));
	}
	
	@Test
	public void testJAXRSConfiguratorResteasy() {
		String projectName = PROJECT_NAME_JAXRS+"_noRuntime";
		createWebProject(projectName,null,false);
		convertToMavenProject(projectName, "war", false);
		
		checkProjectWithoutRuntime(projectName);
		
		addDependency(projectName, "org.jboss.jbossas", "jboss-as-resteasy", "6.1.0.Final");
		updateConf(projectName);
		new WaitUntil(new ProjectHasNature(projectName, JAXRS_FACET, null));
	}
	
	@Test
	public void testJAXRSConfigurator() {
		createWebProject(PROJECT_NAME_JAXRS, sr.getRuntimeNameLabelText(sr.getConfig()),false);
		convertToMavenProject(PROJECT_NAME_JAXRS, "war", true);
		updateConf(PROJECT_NAME_JAXRS);
		checkProjectWithRuntime(PROJECT_NAME_JAXRS);
		new WaitUntil(new ProjectHasNature(PROJECT_NAME_JAXRS, JAXRS_FACET, null));
	}
}