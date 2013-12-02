package org.jboss.tools.portlet.ui.bot.test.seam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.Group;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.tools.portlet.ui.bot.entity.FacetDefinition;
import org.jboss.tools.portlet.ui.bot.task.facet.Facets;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.JBossJSFPortletCapabilitiesWizardPageFillingTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.JBossPortletCapabilitiesWizardPageFillingTask;
import org.jboss.tools.portlet.ui.bot.test.template.CreatePortletProjectTemplate;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.TestConfigurator;
import org.jboss.tools.ui.bot.ext.config.Annotations.DB;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Seam;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;


/**
 * Creates a new Dynamic Web Project with the specific JBoss Seam Portlet facet. 
 * 
 * @author Lucia Jelinkova
 * @author Petr Suchy
 *
 */
@Require(db=@DB, seam=@Seam, server=@Server(state=ServerState.Present))
public class CreateSeamPortletProject extends CreatePortletProjectTemplate{

	public static final String PROJECT_NAME = "seam-portlet";

	@Override
	public String getProjectName() {
		return PROJECT_NAME;
	}
	
	@Override
	public List<FacetDefinition> getRequiredFacets() {
		List<FacetDefinition> facets = new ArrayList<FacetDefinition>();
		facets.add(Facets.JAVA_FACET);
		facets.add(Facets.JSF_FACET);
		facets.add(new FacetDefinition("Seam", null, configuredState.getSeam().version));
		facets.add(Facets.CORE_PORTLET_FACET);
		facets.add(Facets.JSF_PORTLET_FACET);
		facets.add(Facets.SEAM_PORTLET_FACET);
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
		new DefaultCombo("Database Type:").setSelection("HSQL");
		new DefaultCombo("Connection profile:").setSelection(SWTTestExt.configuredState.getDB().name);
		dialog.next();
		new DefaultCombo("Type:").setSelection(JBossJSFPortletCapabilitiesWizardPageFillingTask.Type.RUNTIME_PROVIDER.toString());
		
		processWizardPageJSFPortletCapabilities();
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
