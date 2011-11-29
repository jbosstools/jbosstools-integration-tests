package org.jboss.tools.portlet.ui.bot.test.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.tools.portlet.ui.bot.entity.FacetDefinition;
import org.jboss.tools.portlet.ui.bot.task.wizard.WizardPageDefaultsFillingTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.WizardPageFillingTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.JBossPortletCapabilitiesWizardPageFillingTask;
import org.jboss.tools.portlet.ui.bot.test.template.CreatePortletProjectTemplate;

/**
 * Creates a new Dynamic Web Project with the specific JBoss Core Portlet facet. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class CreateJavaPortletProject extends CreatePortletProjectTemplate {

	public static final String PROJECT_NAME = "java-portlet";
	
	@Override
	public String getProjectName() {
		return PROJECT_NAME;
	}
	
	@Override
	public List<FacetDefinition> getRequiredFacets() {
		List<FacetDefinition> facets = new ArrayList<FacetDefinition>();
		facets.add(JAVA_FACET);
		facets.add(CORE_PORTLET_FACET);
		return facets;
	}
	
	@Override
	public List<WizardPageFillingTask> getAdditionalWizardPages() {
		List<WizardPageFillingTask> tasks = new ArrayList<WizardPageFillingTask>();
		tasks.add(new WizardPageDefaultsFillingTask());
		tasks.add(new WizardPageDefaultsFillingTask());
		tasks.add(new JBossPortletCapabilitiesWizardPageFillingTask(JBossPortletCapabilitiesWizardPageFillingTask.Type.RUNTIME_PROVIDER));
		return tasks;
	}
	
	@Override
	public List<String> getExpectedFiles() {
		return Arrays.asList(WEB_XML, PORTLET_XML, PORTLET_LIBRARIES);
	}
	
	@Override
	public List<String> getNonExpectedFiles() {
		return Arrays.asList(FACES_CONFIG_XML, WEB_APP_LIBRARIES, PAGES_XML, COMPONENTS_XML, JBOSS_WEB_XML);
	}
}
