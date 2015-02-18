package org.jboss.tools.cdi.bot.test.cd11;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.condition.ProblemsExists;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectFirstPage;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.cdi.reddeer.cdi.ui.CDIProjectWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.wizard.facet.CDIInstallWizardPage;
import org.jboss.tools.cdi.reddeer.common.model.ui.editor.EditorPartWrapper;
import org.junit.After;
import org.junit.Test;


//based on JBIDE-18701
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY8x)
public class CDI11Wizard {
	
	private static final String PROJECT_NAME = "CDI11Project";
	
	@InjectRequirement
	protected static ServerRequirement sr;
	
	@After
	public void cleanup(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.deleteAllProjects();
	}
	
	@Test
	public void createCDI11Project(){
		CDIProjectWizard cw = new CDIProjectWizard();
		cw.open();
		WebProjectFirstPage fp = (WebProjectFirstPage)cw.getWizardPage(0);
		fp.setProjectName(PROJECT_NAME);
		assertEquals(sr.getRuntimeNameLabelText(sr.getConfig()),fp.getTargetRuntime());
		assertEquals("Dynamic Web Project with CDI 1.2 (Context and Dependency Injection)",fp.getConfiguration());
		cw.finish();
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		assertTrue(pe.containsProject(PROJECT_NAME));
		assertTrue(pe.getProject(PROJECT_NAME).containsItem("WebContent","WEB-INF","beans.xml"));
		pe.getProject(PROJECT_NAME).getProjectItem("WebContent","WEB-INF","beans.xml").open();
		EditorPartWrapper beans = new EditorPartWrapper();
		beans.activateSourcePage();
		assertEquals(0,beans.getMarkers().size());
		new WaitUntil(new ProblemsExists(), TimePeriod.LONG, false);
		new WaitWhile(new ProblemsExists());
	}
	
	@Test
	public void createCDI11ProjectWithoutBeansXml(){
		CDIProjectWizard cw = new CDIProjectWizard();
		cw.open();
		WebProjectFirstPage fp = (WebProjectFirstPage)cw.getWizardPage(0);
		fp.setProjectName(PROJECT_NAME);
		assertEquals(sr.getRuntimeNameLabelText(sr.getConfig()),fp.getTargetRuntime());
		assertEquals("Dynamic Web Project with CDI 1.2 (Context and Dependency Injection)",fp.getConfiguration());
		CDIInstallWizardPage ip = (CDIInstallWizardPage) cw.getWizardPage(3);
		ip.toggleCreateBeansXml(false);
		cw.finish();
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		assertTrue(pe.containsProject(PROJECT_NAME));
		assertFalse("this a known issue JBIDE-19289",pe.getProject(PROJECT_NAME).containsItem("WebContent","WEB-INF","beans.xml"));
		new WaitUntil(new ProblemsExists(), TimePeriod.LONG, false);
		new WaitWhile(new ProblemsExists());
	}

}
