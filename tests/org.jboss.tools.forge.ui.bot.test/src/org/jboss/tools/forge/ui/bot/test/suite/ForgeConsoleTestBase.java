/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.forge.ui.bot.test.suite;


import java.io.File;

import org.eclipse.core.resources.ResourcesPlugin;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.forge.reddeer.condition.ForgeConsoleHasText;
import org.jboss.tools.forge.reddeer.view.ForgeConsoleView;
import org.junit.After;
import org.junit.Before;


/**
 * 
 * @author psrna
 *
 */
public class ForgeConsoleTestBase {

	protected static final String PROJECT_NAME = "testproject";
	protected static final String PACKAGE_NAME = "org.jboss.testpackage";
	protected static final String ENTITY_NAME = "testentity";
	protected static final String FIELD_NAME = "testfield";
	
	protected static final String WORKSPACE = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
	
	protected static ProjectExplorer pExplorer = null;
	protected static ForgeConsoleView fView = null;
	 
	
	@Before
	public void setup(){
				
		pExplorer = new ProjectExplorer();
		fView = new ForgeConsoleView();
		fView.open();
		fView.selectRuntime(new RegexMatcher("Forge 1.*"));;
		fView.start();
		cdWS();
		fView.clear();
	}
	
	@After
	public void cleanup(){

		cdWS();
		fView.clear();
		pExplorer = new ProjectExplorer();
		pExplorer.deleteAllProjects();
		
	}
	
	public static void cdWS() {
		fView.setConsoleText("cd # \n");
		String ws_dir = WORKSPACE.substring(WORKSPACE.lastIndexOf(File.separatorChar) + 1);
		new WaitUntil(new ForgeConsoleHasText(ws_dir));
	}
	
	public enum ProjectTypes{
		jar, war, pom
	}
	
	
	protected void createProject(){
		cdWS();
		fView.setConsoleText("new-project --named " + PROJECT_NAME + 
								" --topLevelPackage " + PACKAGE_NAME + "\n");
		fView.setConsoleText("Y\n");
		new WaitUntil(new ForgeConsoleHasText("project [" + PROJECT_NAME + "]"), TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	protected void createProject(ProjectTypes type){
		cdWS();
		fView.setConsoleText("new-project --named " + PROJECT_NAME + 
								" --topLevelPackage " + PACKAGE_NAME + 
								" --type " + type + "\n");
		fView.setConsoleText("Y\n");
		new WaitUntil(new ForgeConsoleHasText("project [" + PROJECT_NAME + "]"), TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	protected void createPersistence(){	
		createPersistence("HIBERNATE", "JBOSS_AS7");
	}
	
	protected void createPersistence(String provider, String container){
		
		fView.setConsoleText("persistence setup\n");
		fView.setConsoleText(provider + "\n");
		fView.setConsoleText(container + "\n");
		fView.setConsoleText("\n"); //accept default java-ee-spec
		fView.setConsoleText("N\n"); //JPA 2 metamodel generator?
		fView.setConsoleText("N\n"); //extended APIs. Install these as well?
		new WaitUntil(new ForgeConsoleHasText("persistence.xml"), TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	protected void createEntity(){
		createEntity(ENTITY_NAME, PACKAGE_NAME);
	}
	
	protected void createEntity(String entityName, String packageName){
		
		final String ENTITY_CREATED = "Created @Entity [" + packageName + "." + entityName + "]";
		
		fView.setConsoleText("entity\n");
		fView.setConsoleText(entityName + "\n");
		fView.setConsoleText(packageName + "\n");
		new WaitUntil(new ForgeConsoleHasText(ENTITY_CREATED), TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	protected void createStringField(String fieldName){
		
		final String FIELD_ADDED = "Added field to " + PACKAGE_NAME + "." +
									ENTITY_NAME + ": @Column private String " + fieldName + ";";
		
		fView.setConsoleText("field string --named " + fieldName + "\n" );
		
		new WaitUntil(new ForgeConsoleHasText(FIELD_ADDED), TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	protected void installPlugin(String pluginName){
		
		fView.setConsoleText("forge install-plugin " + pluginName + "\n");
		new WaitUntil(new ForgeConsoleHasText("***SUCCESS*** Installed"), TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	public static String pwd(){
		String separator = System.getProperty("line.separator");
		fView.setConsoleText("pwd\n");
		String[] lines = fView.getConsoleText().split(separator);
		return lines[lines.length - 2];
	}
	
}
