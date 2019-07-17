/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.central.test.ui.reddeer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.eclipse.jdt.debug.ui.jres.JREsPreferencePage;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.jre.JRERequirement.JRE;
import org.eclipse.reddeer.swt.api.TableItem;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.eclipse.reddeer.workbench.handler.WorkbenchShellHandler;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.central.reddeer.api.ExamplesOperator;
import org.jboss.tools.central.reddeer.projects.ArchetypeProject;
import org.jboss.tools.central.test.ui.reddeer.projects.AngularJSForge;
import org.jboss.tools.central.test.ui.reddeer.projects.HTML5Project;
import org.jboss.tools.central.test.ui.reddeer.projects.JavaEEWebProject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author rhopp
 * @contributor jkopriva@redhat.com
 * @contributor vprusa@redhat.com
 */
@JRE(cleanup=false)
@RunWith(RedDeerSuite.class)
public class ArchetypesTest {

	@RequirementRestriction
	public static Collection<RequirementMatcher> getRestrictionMatcher() {
		return Arrays.asList(new RequirementMatcher(JRE.class, "version", "1.8"));
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface JRE {

		/**
		 * Cleanup.
		 *
		 * @return true, if successful
		 */
		boolean cleanup() default false;
	}
	
	private static final String CENTRAL_LABEL = "Red Hat Central";
	private static Map<org.jboss.tools.central.reddeer.projects.Project, List<String>> projectWarnings = new HashMap<org.jboss.tools.central.reddeer.projects.Project, List<String>>();
	private static final Logger log = Logger.getLogger(ExamplesOperator.class);

	private static final String BLACKLIST_ERRORS_REGEXES_FILE = "resources/blacklist-test-errors-regexes.json";

	public static JSONObject blacklistErrorsFileContents;

	@BeforeClass
	public static void setup() {
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();

		JREsPreferencePage jrePreferencePage = new JREsPreferencePage(preferenceDialog);
		preferenceDialog.select(jrePreferencePage);
		List<TableItem> tis = new DefaultTable(0).getItems();
		for (TableItem ti : tis) {
			if (ti.getText(0).startsWith("Java SE 8") || ti.getText(0).startsWith("openjdk-1.8")) {
				ti.setChecked(true);
				jrePreferencePage.apply();
			}
		}
		preferenceDialog.ok();
		
		new DefaultToolItem(new WorkbenchShell(), CENTRAL_LABEL).click();
		// activate central editor
		new DefaultEditor(CENTRAL_LABEL);
	}

	@After
	public void teardown() {
		WorkbenchShellHandler.getInstance().closeAllNonWorbenchShells();
		new ProjectExplorer().deleteAllProjects();
		new DefaultToolItem(new WorkbenchShell(), CENTRAL_LABEL).click();
		// activate central editor
		new DefaultEditor(CENTRAL_LABEL);
	}

	@AfterClass
	public static void teardownClass() {
		StringBuilder sb = new StringBuilder();
		boolean fail = false;

		loadBlacklistErrorsFile(BLACKLIST_ERRORS_REGEXES_FILE);

		for (Entry<org.jboss.tools.central.reddeer.projects.Project, List<String>> projectWarning : projectWarnings
				.entrySet()) {

			if (blacklistErrorsFileContents == null
					|| (!blacklistErrorsFileContents.containsKey(projectWarning.getKey().getName())
							&& !blacklistErrorsFileContents.containsKey("*"))) {
				sb.append(projectWarning.getKey().getName() + "\n\r");
				if (!projectWarning.getValue().isEmpty())
					fail = true;
				for (String warning : projectWarning.getValue()) {
					sb.append("\t" + warning + "\n\r");
				}

			} else {

				log.info("Lets ignore known errors.. real errors:");

				JSONArray errorsToIgnore = new JSONArray();
				if (blacklistErrorsFileContents.containsKey(projectWarning.getKey().getName())) {
					errorsToIgnore = (JSONArray) blacklistErrorsFileContents.get(projectWarning.getKey().getName());
				}
				if (blacklistErrorsFileContents.containsKey("*")) {
					
					JSONArray errorsArr = (JSONArray) blacklistErrorsFileContents.get("*");
					List<String> errorsToIgnoreList = (List<String>) (List<?>) Arrays.asList((errorsArr).toArray());
					sb.append(projectWarning.getKey().getName() + "\n\r");
						for (String warning : projectWarning.getValue()) {
							if(!errorsToIgnoreList.stream().filter(s->warning.matches(s)).findAny().isPresent()) {
								if (!projectWarning.getValue().isEmpty()) {
									log.info("projectWarning.getKey().toString()");
									log.info(projectWarning.getKey().toString());
									log.info(projectWarning.getValue().toString());
									fail = true;
								}
								sb.append("\t" + warning + "\n\r");
							}
						}
				}
				log.info(errorsToIgnore.toJSONString());
			}

		}
		projectWarnings.clear();
		assertFalse(sb.toString(), fail);
	}

	// https://www.mkyong.com/java/json-simple-example-read-and-write-json
	public static void loadBlacklistErrorsFile(String blacklisterrorsFile) {
		if (blacklisterrorsFile.isEmpty()) {
			return;
		}
		String pathToFile = "";
		try {
			pathToFile = new File(blacklisterrorsFile).getCanonicalPath();
			JSONParser parser = new JSONParser();
			blacklistErrorsFileContents = (JSONObject) parser.parse(new FileReader(pathToFile));
		} catch (IOException ex) {
			fail("Blacklist file not found! Path is: " + pathToFile);
		} catch (ParseException e) {
			fail("ParseException: unable to parse file at path is: " + pathToFile);
		}
	}

	@Test
	public void HTML5ProjectTest() {
		ArchetypeProject project = new HTML5Project();
		importArchetypeProject(project);
	}

	@Test
	public void AngularJSForgeTest() {
		ArchetypeProject project = new AngularJSForge();
		importArchetypeProject(project);
	}

	@Test
	public void JavaEEWebProjectTest() {
		importArchetypeProject(new JavaEEWebProject(false));
	}
	
	@Test
	public void JavaEEWebProjectBlankTest() {
		importArchetypeProject(new JavaEEWebProject(true));
	}
	
//	@Test
//	public void HybridMobileTest(){
//		try{
//			new DefaultHyperlink("Hybrid Mobile Project").activate();
//			new WaitUntil(new ShellWithTextIsActive("Information"));
//			//HMT is not installed
//			new PushButton("No").click();
//		}catch(WaitTimeoutExpiredException ex){
//			//TODO check whether this is OK.
//			new DefaultShell().close();
//		}
//	}

	private void importArchetypeProject(ArchetypeProject project) {
		ExamplesOperator.getInstance().importArchetypeProject(project);
		projectWarnings.put(project, ExamplesOperator.getInstance().getAllWarnings());
	}
}
