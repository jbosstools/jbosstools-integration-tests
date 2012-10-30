package org.jboss.tools.bpel.ui.bot.ext;

import static org.junit.Assert.assertTrue;

import org.eclipse.swtbot.eclipse.gef.finder.SWTGefBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.jboss.tools.bpel.ui.bot.ext.editor.BpelEditor;
import org.jboss.tools.bpel.ui.bot.test.BPELTest;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.view.ProjectExplorer;
import org.jboss.tools.ui.bot.ext.view.ServersView;
import org.jboss.tools.ui.bot.ext.view.TabbedPropertiesView;

/**
 * 
 * @author apodhrad
 * 
 */
public class BpelBot extends SWTBotExt {

	private ProjectExplorer projectExplorer;

	public BpelBot() {
		projectExplorer = new ProjectExplorer();
	}

	public BpelEditor bpelEditor(String bpelFileName) {
		SWTBotGefEditor editor = new SWTGefBot().gefEditor(bpelFileName);
		return new BpelEditor(editor, this);
	}

	public BpelEditor openBpelFile(String projectName, String bpelFileName) {
		projectExplorer.openFile(projectName, "bpelContent", bpelFileName);
		return bpelEditor(bpelFileName);
	}

	public TabbedPropertiesView bpelProperties() {
		return new TabbedPropertiesView();
	}

	public void createDeploymentDescriptor() {

	}

	public void deployProject(String projectName) {
		String serverName = BPELTest.configuredState.getServer().name;
		ServersView serversView = new ServersView();
		serversView.addProjectToServer(projectName, serverName);
		assertTrue("Project wasn't deployed", serversView.containsProject(serverName, projectName));
	}

}
