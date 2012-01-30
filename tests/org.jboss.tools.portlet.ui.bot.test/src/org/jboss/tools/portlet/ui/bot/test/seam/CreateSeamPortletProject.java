package org.jboss.tools.portlet.ui.bot.test.seam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jboss.tools.portlet.ui.bot.entity.FacetDefinition;
import org.jboss.tools.portlet.ui.bot.task.wizard.WizardPageDefaultsFillingTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.WizardPageFillingTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.JBossJSFPortletCapabilitiesWizardPageFillingTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.JBossPortletCapabilitiesWizardPageFillingTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.seam.Seam2FacetWizardPageFillingTask;
import org.jboss.tools.portlet.ui.bot.test.template.CreatePortletProjectTemplate;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.DB;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Seam;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.config.TestConfigurator;


/**
 * Creates a new Dynamic Web Project with the specific JBoss Seam Portlet facet. 
 * 
 * @author Lucia Jelinkova
 *
 */
@Require(db=@DB, seam=@Seam, server=@Server(state=ServerState.Present, type=ServerType.EPP))
public class CreateSeamPortletProject extends CreatePortletProjectTemplate{

	public static final String PROJECT_NAME = "seam-portlet";

	@Override
	public String getProjectName() {
		return PROJECT_NAME;
	}
	
	@Override
	public List<FacetDefinition> getRequiredFacets() {
		List<FacetDefinition> facets = new ArrayList<FacetDefinition>();
		facets.add(JAVA_FACET);
		facets.add(JSF_FACET);
		facets.add(new FacetDefinition("Seam", null, configuredState.getSeam().version));
		facets.add(CORE_PORTLET_FACET);
		facets.add(JSF_PORTLET_FACET);
		facets.add(SEAM_PORTLET_FACET);
		return facets;
	}
	
	@Override
	public List<WizardPageFillingTask> getAdditionalWizardPages() {
		List<WizardPageFillingTask> tasks = new ArrayList<WizardPageFillingTask>();
		tasks.add(new WizardPageDefaultsFillingTask());
		tasks.add(new WizardPageDefaultsFillingTask());
		tasks.add(new JBossPortletCapabilitiesWizardPageFillingTask(
				JBossPortletCapabilitiesWizardPageFillingTask.Type.RUNTIME_PROVIDER));
		tasks.add(new WizardPageDefaultsFillingTask());
		tasks.add(getSeamFacetPageFillingTask());
		tasks.add(new JBossJSFPortletCapabilitiesWizardPageFillingTask(
				JBossJSFPortletCapabilitiesWizardPageFillingTask.Type.RUNTIME_PROVIDER, 
				TestConfigurator.currentConfig.getPortletBridge().getLocation()));
		return tasks;
	}
	
	private WizardPageFillingTask getSeamFacetPageFillingTask() {
		Seam2FacetWizardPageFillingTask task = new Seam2FacetWizardPageFillingTask();
		task.setDatabaseType("HSQL");
		task.setConnectionProfile(SWTTestExt.configuredState.getDB().name);
		return task;
	}

	@Override
	public List<String> getExpectedFiles() {
		return Arrays.asList(
				WEB_XML, 
				PORTLET_XML, 
				PORTLET_LIBRARIES, 
				FACES_CONFIG_XML, 
				WEB_APP_LIBRARIES,
				PAGES_XML, 
				COMPONENTS_XML, 
				JBOSS_WEB_XML);
	}
	
	@Override
	public List<String> getNonExpectedFiles() {
		return Collections.emptyList();
	}
}
