package org.jboss.tools.ws.ui.bot.test;

import java.io.IOException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.WebServicesWSDL;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

public class JBossWSProjectFacetBotTest extends WSWizardTest {

	public void testNewWizard() throws IOException, CoreException {
		bot.menu("File").menu("New").menu("Project...").click();
		bot.shell("New Project").activate();
		SWTBotTree tree = bot.tree();
		tree.expandNode("Web").expandNode("Dynamic Web Project").select();
		assertTrue(bot.button("Next >").isEnabled());
		bot.button("Next >").click();
		bot.shell("New Dynamic Web Project").activate();
		assertFalse(bot.button("Finish").isEnabled());

		bot.textWithLabel("Project name:").setText("B");
		assertTrue(bot.button("Finish").isEnabled());
		bot.comboBoxInGroup("Dynamic web module version").setSelection("2.5");
		bot.comboBoxInGroup("Configuration").setSelection(
				"JBossWS Web Service Project v3.0");
		assertFalse(bot.button("Finish").isEnabled());
		bot.button("Next >").click();
		bot.button("Next >").click();
		bot.button("Next >").click();
		bot.radio(0).click();
		bot.sleep(2000);
		assertTrue(bot.button("Finish").isEnabled());
		open.finish(bot);
		project = ResourcesPlugin.getWorkspace().getRoot().getProject("B");
		assertNotNull(project);
		IFacetedProject fproj = ProjectFacetsManager.create(project);
		assertNotNull(fproj
				.getProjectFacetVersion((IProjectFacet) ProjectFacetsManager
						.getProjectFacet("jbossws.core")));
		tryJBIDE6250();
	}

	private void tryJBIDE6250() {
		setDefaultWSRuntime();
		
		SWTBot wiz = open.newObject(WebServicesWSDL.LABEL);
		wiz.textWithLabel(WebServicesWSDL.TEXT_FILE_NAME).setText(
				"ClassB" + ".wsdl");
		wiz.textWithLabel(
				WebServicesWSDL.TEXT_ENTER_OR_SELECT_THE_PARENT_FOLDER)
				.setText("B");
		wiz.button(IDELabel.Button.NEXT).click();
		open.finish(wiz);
		eclipse.setClassContentFromResource(bot.editorByTitle("ClassB"
				+ ".wsdl"), true,
				org.jboss.tools.ws.ui.bot.test.Activator.PLUGIN_ID,
				"jbossws", "ClassB" + ".wsdl");
		
		bot.sleep(1000);
		bot.menu("File").menu("New").menu("Other...").click();
		bot.shell("New").activate();

		SWTBotTree tree = bot.tree();
		SWTBotTreeItem item = tree.getTreeItem("Web Services");
		tree.select(item);
		item.expand();
		try {
			item.getNode("Web Service").select();
		} catch (Exception e) {
			item.collapse();
			item.expand();
			item.getNode("Web Service").select();
		}
		bot.sleep(1000);
		assertTrue(bot.button("Next >").isEnabled());
		bot.button("Next >").click();
		bot.shell("Web Service").activate();
		bot.comboBoxWithLabel("Web service type:").setSelection(1);
		bot.comboBoxWithLabel("Service definition:").setText(
				"/B/ClassB.wsdl");
		while (!bot.button("Next >").isEnabled()) {
			bot.sleep(1000);
		}
		selectProject();
		bot.button("Next >").click();
		bot.sleep(1000);	
		assertTrue(bot.checkBox(0).isChecked());
	}
}
