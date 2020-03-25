/*******************************************************************************
 * Copyright (c) 2011-2020 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.maven.ui.bot.test.project;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.jsf.reddeer.ui.JSFNewProjectFirstPage;
import org.jboss.tools.jsf.reddeer.ui.JSFNewProjectSecondPage;
import org.jboss.tools.jsf.reddeer.ui.JSFNewProjectWizard;
import org.jboss.tools.maven.reddeer.requirement.NewRepositoryRequirement.DefineMavenRepository;
import org.jboss.tools.maven.reddeer.requirement.NewRepositoryRequirement.PredefinedMavenRepository;
import org.jboss.tools.maven.ui.bot.test.AbstractMavenSWTBotTest;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@OpenPerspective(JavaPerspective.class)
@JBossServer(state=ServerRequirementState.PRESENT)
@DefineMavenRepository(predefinedRepositories = {
		@PredefinedMavenRepository(ID = "jboss-public-repository", snapshots = true) })
public class JSFProjectTest extends AbstractMavenSWTBotTest{
	public static final String POM_FILE = "pom.xml";
	public static final String PROJECT_NAME7_V22="JSFProject7_2.2";
	public static final String PROJECT_NAME7_V23="JSFProject7_2.3";
	public static final String GROUPID ="javax.faces";
	public static final String ARTIFACTID ="javax.faces-api";
	public static final String JSF_VERSION_2_2 ="2.2";
	public static final String JSF_VERSION_2_3 ="2.3";
	
    @InjectRequirement
    private ServerRequirement sr;
    
	@Test
	public void createJSFProjectTestAS7JSFv22() {
		createJSFProject(PROJECT_NAME7_V22, "JSF 2.2", "JSFKickStartWithoutLibs", sr.getRuntimeName());
		convertToMavenProject(PROJECT_NAME7_V22, "war", true);
		addDependency(PROJECT_NAME7_V22, GROUPID,ARTIFACTID,JSF_VERSION_2_2);
		buildProject(PROJECT_NAME7_V22,"..Maven build...","clean package",true);
		checkWebTarget(PROJECT_NAME7_V22, PROJECT_NAME7_V22+"-0.0.1-SNAPSHOT");
	}
	
	@Test
	public void createJSFProjectTestAS7JSFv23() {
		createJSFProject(PROJECT_NAME7_V23, "JSF 2.3", "JSFKickStartWithoutLibs", sr.getRuntimeName());
		convertToMavenProject(PROJECT_NAME7_V23, "war", true);
		addDependency(PROJECT_NAME7_V23, GROUPID,ARTIFACTID,JSF_VERSION_2_3);
		buildProject(PROJECT_NAME7_V23,"..Maven build...","clean package",true);
		checkWebTarget(PROJECT_NAME7_V23,PROJECT_NAME7_V23+"-0.0.1-SNAPSHOT");
	}

	private void createJSFProject(String name, String version, String jsfType, String runtime){
		JSFNewProjectWizard jsfd = new JSFNewProjectWizard();
		jsfd.open();
		JSFNewProjectFirstPage fp = new JSFNewProjectFirstPage(jsfd);
		fp.setProjectName(name);
		fp.setJSFEnvironment(version);
		fp.setProjectTemplate(jsfType);
		jsfd.next();
		JSFNewProjectSecondPage sp = new JSFNewProjectSecondPage(jsfd);
		sp.setRuntime(runtime);
		jsfd.finish(TimePeriod.VERY_LONG);
	}
}