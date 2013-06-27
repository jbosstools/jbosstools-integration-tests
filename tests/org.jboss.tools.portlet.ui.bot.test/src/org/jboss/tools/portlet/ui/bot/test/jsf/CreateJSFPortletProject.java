package org.jboss.tools.portlet.ui.bot.test.jsf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.portlet.ui.bot.entity.FacetDefinition;
import org.jboss.tools.portlet.ui.bot.task.facet.Facets;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.JBossJSFPortletCapabilitiesWizardPageFillingTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.JBossPortletCapabilitiesWizardPageFillingTask;
import org.jboss.tools.portlet.ui.bot.test.template.CreatePortletProjectTemplate;
import org.jboss.tools.ui.bot.ext.config.TestConfigurator;


/**
 * Creates a new Dynamic Web Project with the specific JBoss JSF Portlet facet. 
 * 
 * @author Lucia Jelinkova
 * @author Petr Suchy
 *
 */
public class CreateJSFPortletProject extends CreatePortletProjectTemplate{

	public static final String PROJECT_NAME = "jsf-portlet";
	
	@Override
	public String getProjectName() {
		return PROJECT_NAME;
	}
	
	@Override
	public List<FacetDefinition> getRequiredFacets() {
		List<FacetDefinition> facets = new ArrayList<FacetDefinition>();
		facets.add(Facets.JAVA_FACET);
		facets.add(Facets.JSF_FACET);
		facets.add(Facets.CORE_PORTLET_FACET);
		facets.add(Facets.JSF_PORTLET_FACET);
		return facets;
	}
	
	@Override
	public void processAdditionalWizardPages(WizardDialog dialog) {
		dialog.next();
		dialog.next();
		dialog.next();
		new DefaultCombo("Type:").setSelection(JBossPortletCapabilitiesWizardPageFillingTask.Type.RUNTIME_PROVIDER.toString());
		dialog.next();
		dialog.next();
		new DefaultCombo("Type:").setSelection(JBossJSFPortletCapabilitiesWizardPageFillingTask.Type.RUNTIME_PROVIDER.toString());
		
		// new DefaultText("Portletbridge Runtime") does not work (WidgetNotFoundException)
		try{
			Bot.get().textInGroup("Portletbridge Runtime").setText(TestConfigurator.currentConfig.getPortletBridge().getLocation());
		} catch (WidgetNotFoundException e) {
			// ok, the portlet bridge is recognized in the server location
		}
	}
	
	@Override
	public List<String> getExpectedFiles() {
		return Arrays.asList(WEB_XML, PORTLET_XML, PORTLET_LIBRARIES, FACES_CONFIG_XML, WEB_APP_LIBRARIES);
	}
	
	@Override
	public List<String> getNonExpectedFiles() {
		return Arrays.asList(PAGES_XML, COMPONENTS_XML, JBOSS_WEB_XML);
	}
}
