package org.jboss.tools.cdi.bot.test.beansxml.validation.cdi11;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.tools.cdi.bot.test.beansxml.validation.template.BeansXMLValidationQuickFixTemplate;
import org.jboss.tools.cdi.reddeer.common.model.ui.editor.EditorPartWrapper;
import org.jboss.tools.cdi.reddeer.uiutils.EditorResourceHelper;
import org.jboss.tools.cdi.reddeer.validators.BeansXmlValidationProviderCDI11;
import org.junit.Before;

@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY10x, cleanup=false)
@OpenPerspective(JavaEEPerspective.class)
public class BeansXMLValidationQuickFixTestCDI11 extends BeansXMLValidationQuickFixTemplate{
	
	@Before
	public void setValidationProvider(){
		validationProvider = new BeansXmlValidationProviderCDI11();
		requireBeansXML = false;
		EditorPartWrapper beansEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		beansEditor.activateTreePage();
		beansEditor.selectBeanDiscoveryMode("all");
		beansEditor.save();
		beansEditor.activateSourcePage();
		new EditorResourceHelper().replaceInEditor("/>", "></beans>");
		beansEditor.save();
		beansEditor.close();
		new WaitWhile(new JobIsRunning());
	}

}
