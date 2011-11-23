package org.jboss.tools.portlet.ui.bot.test.create;

import static org.jboss.tools.portlet.ui.bot.test.entity.EntityFactory.file;
import static org.jboss.tools.portlet.ui.bot.test.matcher.factory.DefaultMatchersFactory.isNumberOfErrors;
import static org.jboss.tools.portlet.ui.bot.test.matcher.factory.WorkspaceMatchersFactory.exists;
import static org.jboss.tools.portlet.ui.bot.test.matcher.factory.WorkspaceMatchersFactory.hasFacets;
import static org.jboss.tools.portlet.ui.bot.test.matcher.factory.WorkspaceMatchersFactory.isExistingProject;

import org.jboss.tools.portlet.ui.bot.test.entity.FacetDefinition;
import org.jboss.tools.portlet.ui.bot.test.task.AbstractSWTTask;
import org.jboss.tools.portlet.ui.bot.test.task.facet.FacetsSelectionTask;
import org.jboss.tools.portlet.ui.bot.test.task.wizard.WizardPageDefaultsFillingTask;
import org.jboss.tools.portlet.ui.bot.test.task.wizard.web.DynamicWebProjectCreationTask;
import org.jboss.tools.portlet.ui.bot.test.task.wizard.web.jboss.JBossPortletCapabilitiesWizardPageFillingTask;
import org.jboss.tools.portlet.ui.bot.test.testcase.SWTTaskBasedTestCase;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.junit.Test;

/**
 * Creates a new Dynamic Web Project with the specific JBoss Core Portlet facet. 
 * 
 * @author Lucia Jelinkova
 *
 */
@Require(server=@Server(required=true, state=ServerState.Present, type=ServerType.EPP))
public class CreateJavaPortletProject extends SWTTaskBasedTestCase{

	public static final String PROJECT_NAME = "java-portlet";
	
	private static final String FACET_CATEGORY = "JBoss Portlets";
	
	private static final String FACET_NAME = "JBoss Core Portlet";
	
	@Test
	public void testcreate(){
		doPerform(getCreateDynamicWebProjectTask());

		doAssertThat(0, isNumberOfErrors());
		doAssertThat(PROJECT_NAME, isExistingProject());
		doAssertThat(file(PROJECT_NAME, "WebContent/WEB-INF/portlet.xml"), exists());
		doAssertThat(file(PROJECT_NAME, "JBoss Portlet Libraries"), exists());
		doAssertThat(PROJECT_NAME, hasFacets(new FacetDefinition(FACET_NAME, FACET_CATEGORY)));		
	}
	
	private AbstractSWTTask getCreateDynamicWebProjectTask() {
		DynamicWebProjectCreationTask task = new DynamicWebProjectCreationTask();
		task.setProjectName(PROJECT_NAME);
		task.setWebModuleVersion("2.5");
		task.setServerName(SWTTestExt.configuredState.getServer().name);
		task.setSelectFacetsTask(getSelectFacetsTask());
		task.addWizardPage(new WizardPageDefaultsFillingTask());
		task.addWizardPage(new WizardPageDefaultsFillingTask());
		task.addWizardPage(new JBossPortletCapabilitiesWizardPageFillingTask(JBossPortletCapabilitiesWizardPageFillingTask.Type.RUNTIME_PROVIDER));
		return task;
	}
	
	private FacetsSelectionTask getSelectFacetsTask() {
		FacetsSelectionTask task = new FacetsSelectionTask();
		task.addFacet(new FacetDefinition("Java", null, "1.6"));
		task.addFacet(new FacetDefinition(FACET_NAME, FACET_CATEGORY));
		return task;
	}
}
