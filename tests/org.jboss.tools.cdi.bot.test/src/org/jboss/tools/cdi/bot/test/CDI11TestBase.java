package org.jboss.tools.cdi.bot.test;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.cdi.reddeer.uiutils.CDIProjectHelper;
import org.jboss.tools.common.reddeer.preferences.SourceLookupPreferencePage;
import org.junit.Before;
import org.junit.BeforeClass;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;

@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY8x)
public abstract class CDI11TestBase {
	
	protected static String PROJECT_NAME = "CDI11Project";
	
	@InjectRequirement
	protected static ServerRequirement sr;

	@BeforeClass
	public static void disableSourceLookup() {
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		SourceLookupPreferencePage sourceLookupPreferencePage = new SourceLookupPreferencePage();
		preferenceDialog.select(sourceLookupPreferencePage);
		sourceLookupPreferencePage.setSourceAttachment(
				SourceLookupPreferencePage.SourceAttachmentEnum.NEVER);
		sourceLookupPreferencePage.ok();
	}
	
	@Before
	public void prepareWorkspace(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		if(pe.containsProject(getProjectName())){
			return;
		}
		CDIProjectHelper helper = new CDIProjectHelper();
		helper.createCDI11ProjectWithCDIWizard(getProjectName(), sr.getRuntimeNameLabelText(sr.getConfig()));
	}
	
	public String getProjectName(){
		return PROJECT_NAME;
	}
	
	
}
