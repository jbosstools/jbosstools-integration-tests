package org.jboss.tools.portlet.ui.bot.test.jsf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.tools.portlet.ui.bot.entity.FacetDefinition;
import org.jboss.tools.portlet.ui.bot.task.wizard.WizardPageDefaultsFillingTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.WizardPageFillingTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.JBossJSFPortletCapabilitiesWizardPageFillingTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.JBossPortletCapabilitiesWizardPageFillingTask;
import org.jboss.tools.portlet.ui.bot.test.template.CreatePortletProjectTemplate;


/**
 * Creates a new Dynamic Web Project with the specific JBoss JSF Portlet facet. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class CreateJSFPortletProject extends CreatePortletProjectTemplate{

	public static final String PROJECT_NAME = "jsf-portlet";
	
	protected static final FacetDefinition JSF_PORTLET_FACET = new FacetDefinition("JBoss JSF Portlet", JBOSS_FACET_CATEGORY);
	
	protected static final FacetDefinition JSF_FACET = new FacetDefinition("JavaServer Faces");
	
	protected static final String FACES_CONFIG_XML = "WebContent/WEB-INF/faces-config.xml";
	
	protected static final String WEB_APP_LIBRARIES = "Web App Libraries";
	
	@Override
	public String getProjectName() {
		return PROJECT_NAME;
	}
	
	@Override
	public List<FacetDefinition> getRequiredFacets() {
		List<FacetDefinition> facets = new ArrayList<FacetDefinition>();
		facets.add(JAVA_FACET);
		facets.add(JSF_FACET);
		facets.add(CORE_PORTLET_FACET);
		facets.add(JSF_PORTLET_FACET);
		return facets;
	}
	
	@Override
	public List<WizardPageFillingTask> getAdditionalWizardPages() {
		List<WizardPageFillingTask> tasks = new ArrayList<WizardPageFillingTask>();
		tasks.add(new WizardPageDefaultsFillingTask());
		tasks.add(new WizardPageDefaultsFillingTask());
		tasks.add(new JBossPortletCapabilitiesWizardPageFillingTask(JBossPortletCapabilitiesWizardPageFillingTask.Type.RUNTIME_PROVIDER));
		tasks.add(new WizardPageDefaultsFillingTask());
		tasks.add(new JBossJSFPortletCapabilitiesWizardPageFillingTask(JBossJSFPortletCapabilitiesWizardPageFillingTask.Type.RUNTIME_PROVIDER));
		return tasks;
	}
	
	@Override
	public List<String> getExpectedFiles() {
		return Arrays.asList(WEB_XML, PORTLET_XML, PORTLET_LIBRARIES, FACES_CONFIG_XML, WEB_APP_LIBRARIES);
	}
}
