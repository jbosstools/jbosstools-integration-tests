/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.bot.test;

import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.lookup.ShellLookup;
import org.eclipse.reddeer.eclipse.core.resources.DefaultProject;
import org.eclipse.reddeer.eclipse.core.resources.Project;
import org.eclipse.reddeer.eclipse.jdt.ui.preferences.BuildPathsPropertyPage;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.AbstractJavaWizardPage;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.NewClassCreationWizard;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.NewInterfaceCreationWizard;
import org.eclipse.reddeer.eclipse.jst.servlet.ui.project.facet.WebProjectFirstPage;
import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;
import org.eclipse.reddeer.eclipse.ui.dialogs.PropertyDialog;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.wst.web.ui.wizards.DataModelFacetCreationWizardPage;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.requirements.jre.JRERequirement.JRE;
import org.eclipse.reddeer.swt.api.TableItem;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.workbench.condition.EditorIsDirty;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.handler.EditorHandler;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.ide.eclipse.as.reddeer.server.family.ServerMatcher;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.cdi.reddeer.cdi.ui.CDIProjectWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewInterceptorCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewStereotypeCreationWizard;
import org.jboss.tools.cdi.reddeer.common.model.ui.editor.EditorPartWrapper;
import org.jboss.tools.cdi.reddeer.uiutils.BeansHelper;
import org.jboss.tools.cdi.reddeer.uiutils.BeansXMLHelper;
import org.jboss.tools.cdi.reddeer.uiutils.EditorResourceHelper;
import org.jboss.tools.cdi.reddeer.uiutils.OpenOnHelper;
import org.jboss.tools.cdi.reddeer.uiutils.ProjectHelper;
import org.jboss.tools.cdi.reddeer.uiutils.ValidationHelper;
import org.jboss.tools.common.reddeer.preferences.SourceLookupPreferencePage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class CDITestBase {

	protected String CDIVersion;
	protected boolean isJava11FacetActivated;

	protected String PROJECT_NAME = "CDIProject";
	protected static final String PACKAGE_NAME = "cdi";

	protected static final Logger LOGGER = Logger.getLogger(CDITestBase.class.getName());
	protected static final BeansXMLHelper beansXMLHelper = new BeansXMLHelper();
	protected static final BeansHelper beansHelper = new BeansHelper();
	protected static final EditorResourceHelper editResourceUtil = new EditorResourceHelper();
	protected static final ValidationHelper validationHelper = new ValidationHelper();
	protected static final OpenOnHelper openOnHelper = new OpenOnHelper();
	protected static final ProjectHelper projectHelper = new ProjectHelper();

	private static final String JAVA_VERSION_STR;
	private static final Double JAVA_VERSION;
	
	protected static final String CDI_BEAN_1_JAVA_FILE_NAME = "CdiBean1";
	protected static final String CDI_BEAN_2_JAVA_FILE_NAME = "CdiBean2";
	protected static final String JAVA_FILE_EXTENSION = ".java";
	
	protected Map<Integer, String> valuesToInsertDuringProjectSetup = new HashMap<Integer, String>();
 
	protected static final String VERSION = "version";
	protected static final String FAMILY = "family";
	protected static final String JSR_365 = "JSR-365";
	
	static {
		JAVA_VERSION_STR = System.getProperty("java.version");
		JAVA_VERSION = Double.parseDouble(JAVA_VERSION_STR.substring(0, JAVA_VERSION_STR.indexOf(".") + 1));
	}
	
	public static boolean isJavaLE8() {
		return JAVA_VERSION <= 1.8;
	}
	
	protected static Collection<RequirementMatcher> getRestrictionMatcherCDI10() {
		if (isJavaLE8()) {
			return Arrays.asList(new RequirementMatcher(JBossServer.class, FAMILY, ServerMatcher.AS()));
		} else {
			return Arrays.asList(new RequirementMatcher(JBossServer.class, FAMILY, ServerMatcher.AS()),
					new RequirementMatcher(JRE.class, VERSION, "1.8"));
		}
	}

	protected static Collection<RequirementMatcher> getRestrictionMatcherCDI12() {
		return Arrays.asList(new RequirementMatcher(JBossServer.class, FAMILY, ServerMatcher.WildFly()),
				new RequirementMatcher(JBossServer.class, VERSION, "13"),
				new RequirementMatcher(JRE.class, VERSION, "1.8"));
	}

	protected static Collection<RequirementMatcher> getRestrictionMatcherCDI20() {
		return Arrays.asList(new RequirementMatcher(JBossServer.class, FAMILY, ServerMatcher.WildFly()),
				new RequirementMatcher(JBossServer.class, VERSION, "24+"),
				new RequirementMatcher(JRE.class, VERSION, "11"));

	}

	@InjectRequirement
	private ServerRequirement sr;

	@BeforeClass
	public static void maximizeWorkbench() {
		new WorkbenchShell().maximize();
	}

	@Before
	public void prepareWorkspace() {
		ProjectExplorer projExp = new ProjectExplorer();
		projExp.open();
		if (projExp.getProjects().isEmpty()) {
			if (!PROJECT_NAME.equals("CDIValidatorTest") &&
				!PROJECT_NAME.equals("BeanValidationQuickFixTest") &&
				!PROJECT_NAME.equals("NamedComponentsTest")) { // CDIValidatorTestCDI20, BeanValidationQuickFixTest and NamedComponentsTest requires same project for few tests
				PROJECT_NAME = PROJECT_NAME + Integer.toString(new Random().nextInt(100)); // same project name might cause a problem
			}

			CDIProjectWizard pw = new CDIProjectWizard();
			pw.open();
			WebProjectFirstPage fp = new WebProjectFirstPage(pw);
			fp.setProjectName(PROJECT_NAME);
			if (CDIVersion.equals("2.0")) {
				new DefaultCombo(2).setSelection("Dynamic Web Project with CDI 2.0 (Contexts and Dependency Injection)");
			}
			fp.setTargetRuntime(sr.getRuntimeName());
			activateFacets(fp);
			fp.activateFacet(CDIVersion, "CDI (Contexts and Dependency Injection)");
			pw.finish();
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
			workaroundJBIDE27781(PROJECT_NAME);
		}
	}

	@After
	public void waitForJobs() {
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	@AfterClass
	public static void cleanUp() {
		new WaitWhile(new JobIsRunning());
		EditorHandler.getInstance().closeAll(false);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		for (Project p : pe.getProjects()) {
			try {
				org.eclipse.reddeer.direct.project.Project.delete(p.getName(), true, true);
			} catch (RuntimeException ex) {
				AbstractWait.sleep(TimePeriod.DEFAULT);
				org.eclipse.reddeer.direct.project.Project.delete(p.getName(), true, true);
			}
		}
	}
	
	protected void createNewClass(String project, String packageName, String className) {
		NewClassCreationWizard c = new NewClassCreationWizard();
		AbstractJavaWizardPage page = new AbstractJavaWizardPage(c) {};
		createNewProjectFile(project, packageName, className, c, page);
	}

	protected void createNewInterface(String project, String packageName, String interfaceName) {
		NewInterfaceCreationWizard c = new NewInterfaceCreationWizard();
		AbstractJavaWizardPage page = new AbstractJavaWizardPage(c) {};
		createNewProjectFile(project, packageName, interfaceName, c, page);
	}

	protected void createNewInterceptor(String project, String packageName, String interceptorName) {
		NewInterceptorCreationWizard c = new NewInterceptorCreationWizard();
		AbstractJavaWizardPage page = new AbstractJavaWizardPage(c) {};
		createNewProjectFile(project, packageName, interceptorName, c, page);
	}

	protected void createNewStereotype(String project, String packageName, String stereotypeName) {
		NewStereotypeCreationWizard c = new NewStereotypeCreationWizard();
		AbstractJavaWizardPage page = new AbstractJavaWizardPage(c) {};
		createNewProjectFile(project, packageName, stereotypeName, c, page);
	}

	protected void createNewProjectFile(String project, String packageName, String fileName, NewMenuWizard c,
			AbstractJavaWizardPage page) {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(project);
		c.open();
		page.setPackage(packageName);
		page.setName(fileName);
		c.finish();
	}

	protected static String readFile(String path) {
		Scanner s = null;
		try {
			s = new Scanner(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			fail("unable to find file " + path);
		}
		Scanner s1 = s.useDelimiter("\\A");
		String file = s.next();
		s.close();
		s1.close();
		return file;

	}

	protected String getProjectName() {
		return PROJECT_NAME;
	}

	protected String getPackageName() {
		return PACKAGE_NAME;
	}

	protected static void disableSourceLookup() {
		// wait for some shell to get activated
		ShellLookup.getInstance().getActiveShell();
		String originalShellText = new DefaultShell().getText();
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		SourceLookupPreferencePage sourceLookupPreferencePage = new SourceLookupPreferencePage(preferenceDialog);
		preferenceDialog.select(sourceLookupPreferencePage);
		sourceLookupPreferencePage.setSourceAttachment(SourceLookupPreferencePage.SourceAttachmentEnum.NEVER);
		preferenceDialog.ok();
		new WaitUntil(new ShellIsAvailable(originalShellText));
	}

	public void refreshProject() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(getProjectName()).refresh();
	}

	/**
	 * Set bean discovery mode. This feature is available since CDI 1.1.<br>
	 * Bean discovery mode may be:<br>
	 * <ul>
	 * <li>none</li>
	 * <li>annotated</li>
	 * <li>all</li>
	 * </ul>
	 * 
	 * @param beanDiscoveryMode
	 *            bean discovery mode
	 */
	public void prepareBeanXml(String beanDiscoveryMode, boolean replaceBeansTag) {		
		EditorPartWrapper beansEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		beansEditor.activateTreePage();
		
		if (!beansEditor.getBeanDiscoveryMode().equals(beanDiscoveryMode)) {
			beansEditor.selectBeanDiscoveryMode(beanDiscoveryMode);
			new WaitUntil(new EditorIsDirty(beansEditor), false);
			beansEditor.save();
			new WaitUntil(new JobIsRunning(), TimePeriod.SHORT, false);
			new WaitWhile(new JobIsRunning(), false);
		}
		
		if (replaceBeansTag) {
			beansEditor.activateSourcePage();
			editResourceUtil.replaceInEditor("/>", "></beans>", false);
			new WaitUntil(new EditorIsDirty(beansEditor), false);
			beansEditor.save();
			new WaitUntil(new JobIsRunning(), TimePeriod.SHORT, false);
			new WaitWhile(new JobIsRunning(), false);
		}
		
		beansEditor.close();
	}
	
	public void setupProject(Map<Integer, String> valuesToInsertIntoTextEditor) {
		createNewClass(PROJECT_NAME, "test", CDI_BEAN_1_JAVA_FILE_NAME);
		createNewClass(PROJECT_NAME, "test", CDI_BEAN_2_JAVA_FILE_NAME);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).getProjectItem("src", "main", "java", "test",
				CDI_BEAN_2_JAVA_FILE_NAME + JAVA_FILE_EXTENSION).open();
		TextEditor ed = new TextEditor(CDI_BEAN_2_JAVA_FILE_NAME + JAVA_FILE_EXTENSION);
		for (Map.Entry<Integer, String> entry : valuesToInsertIntoTextEditor.entrySet()) {
			ed.insertLine(entry.getKey(), entry.getValue());
		}
		ed.save();
	}
	
	protected void createProject(NewMenuWizard pw, DataModelFacetCreationWizardPage fp) {
		pw.open();
		fp.setProjectName(PROJECT_NAME);
		activateFacets(fp);
		pw.finish();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	protected void activateFacets(DataModelFacetCreationWizardPage fp) {
		if (CDIVersion.equals("1.0") || CDIVersion.equals("1.2")) {
			fp.activateFacet("1.8", "Java");
			this.isJava11FacetActivated = false;
		} else {
			fp.activateFacet("11", "Java");
			this.isJava11FacetActivated = true;
		}
	}
	
	public static void workaroundJBIDE27781(String projectName) {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		DefaultProject project = pe.getProject(projectName);
		project.select();
		// Workaround for https://issues.redhat.com/browse/JBIDE-27781
		PropertyDialog properties = project.openProperties();
		BuildPathsPropertyPage buildPathsPropertyPage = new BuildPathsPropertyPage(properties);
		properties.select(buildPathsPropertyPage);
		buildPathsPropertyPage.activateOrderAndExportTab();
		DefaultTable orders = new DefaultTable();
		TableItem source = orders.getItems().stream().filter(item -> item.getText(0).contains("src/main")).findFirst().get();
		source.select();
		while(new PushButton("Up").isEnabled()) {
			new PushButton("Up").click();
		}
		buildPathsPropertyPage.apply();
		properties.ok();
	}
	
}