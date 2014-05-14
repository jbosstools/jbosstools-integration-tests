package org.jboss.tools.portlet.ui.bot.test.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.tools.portlet.ui.bot.entity.FacetDefinition;
import org.jboss.tools.portlet.ui.bot.task.facet.Facets;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.JBossPortletCapabilitiesWizardPageFillingTask;
import org.jboss.tools.portlet.ui.bot.test.template.CreatePortletProjectTemplate;

/**
 * Creates a new Dynamic Web Project with the specific JBoss Core Portlet facet. 
 * 
 * @author Lucia Jelinkova
 * @author Petr Suchy
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
		facets.add(Facets.JAVA_FACET);
		facets.add(Facets.CORE_PORTLET_FACET);
		return facets;
	}
	
	@Override
	public void processAdditionalWizardPages(WizardDialog dialog) {
		dialog.next();
		dialog.next();
		dialog.next();
		new LabeledCombo("Type:").setSelection(JBossPortletCapabilitiesWizardPageFillingTask.Type.RUNTIME_PROVIDER.toString());
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
