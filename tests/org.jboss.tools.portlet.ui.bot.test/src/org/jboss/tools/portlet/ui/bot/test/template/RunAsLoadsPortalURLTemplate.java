package org.jboss.tools.portlet.ui.bot.test.template;

import org.jboss.tools.portlet.ui.bot.matcher.browser.BrowserUrlMatcher;
import org.jboss.tools.portlet.ui.bot.task.SWTTask;
import org.jboss.tools.portlet.ui.bot.task.facet.Facets;
import org.jboss.tools.portlet.ui.bot.task.facet.FacetsSelectionTask;
import org.jboss.tools.portlet.ui.bot.task.server.RunninngProjectOnServerTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.WizardPageDefaultsFillingTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.DynamicWebProjectCreationTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.DynamicWebProjectWizardPageFillingTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.JBossPortletCapabilitiesWizardPageFillingTask;
import org.jboss.tools.portlet.ui.bot.test.testcase.SWTTaskBasedTestCase;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.junit.Test;

/**
 * Creates a java portlet and checks the loaded URL in browser after Run as...
 * 
 * This test is ran only once - no need to run it specially for java, jsf and seam portlet. 
 * 
 * @author Lucia Jelinkova
 *
 */
public abstract class RunAsLoadsPortalURLTemplate extends SWTTaskBasedTestCase {

	private static final String PROJECT_NAME = "runAsURLTestProject";
	
	public abstract String getExpectedURL();
		
	@Test
	public void runAsLoadsPortalURL(){
		doPerform(createJavaPortletTask());
		doPerform(runOnServerTask());
		
		doAssertThatInWorkspace(getExpectedURL(), new BrowserUrlMatcher());
	}

	private SWTTask createJavaPortletTask() {
		DynamicWebProjectCreationTask wizardTask = new DynamicWebProjectCreationTask();
		wizardTask.addWizardPage(dynamicWebProjectTask());
		wizardTask.addWizardPage(new WizardPageDefaultsFillingTask());
		wizardTask.addWizardPage(new WizardPageDefaultsFillingTask());
		wizardTask.addWizardPage(new JBossPortletCapabilitiesWizardPageFillingTask(JBossPortletCapabilitiesWizardPageFillingTask.Type.RUNTIME_PROVIDER));
		return wizardTask;
	}
	
	private DynamicWebProjectWizardPageFillingTask dynamicWebProjectTask(){
		DynamicWebProjectWizardPageFillingTask task = new DynamicWebProjectWizardPageFillingTask();
		task.setProjectName(PROJECT_NAME);
		task.setWebModuleVersion("2.5");
		task.setServerName(SWTTestExt.configuredState.getServer().name);
		task.setSelectFacetsTask(getFacets());
		return task;
	}
	
	private FacetsSelectionTask getFacets() {
		FacetsSelectionTask task = new FacetsSelectionTask();
		task.addFacet(Facets.JAVA_FACET);
		task.addFacet(Facets.CORE_PORTLET_FACET);
		return task;
	}
	
	private SWTTask runOnServerTask() {
		return new RunninngProjectOnServerTask(PROJECT_NAME);
	}
}
