package org.jboss.ide.eclipse.as.ui.bot.test.ejb;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class EJBWizardsTest {

	protected static SWTWorkbenchBot bot;

	@BeforeClass
	public static void beforeClass() throws Exception {
		bot = initSWTBot();
	}

	private static SWTWorkbenchBot initSWTBot() throws CoreException,
			InterruptedException {		
		SWTWorkbenchBot swtbot = new SWTWorkbenchBot();
		SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
		SWTBotPreferences.TIMEOUT = 1000;
		SWTBotPreferences.PLAYBACK_DELAY = 5;
		SWTUtilExt util = new SWTUtilExt(bot);
		util.waitForAll();
		try {
			SWTBotView view = swtbot.viewByTitle("Welcome");
			if (view != null) {
				view.close();
			}
		} catch (WidgetNotFoundException ignore) {
		}

		SWTBotShell[] shells = swtbot.shells();
		for (SWTBotShell shell : shells) {
			final Shell widget = shell.widget;
			Object parent = UIThreadRunnable.syncExec(shell.display,
					new Result<Object>() {
						public Object run() {
							return widget.isDisposed() ? null : widget
									.getParent();
						}
					});
			if (parent == null) {
				continue;
			}
			shell.close();
		}

		List<? extends SWTBotEditor> editors = swtbot.editors();
		for (SWTBotEditor e : editors) {
			e.close();
		}

		return swtbot;
	}

	@Test
	public void createEJBProjectWithoutDescriptorTest()
			throws InterruptedException, CoreException {
		String projectName = "EJBWithoutDescriptor";
		createEJBProjectWithoutDescriptor(projectName);
		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectName);

		//WorkspaceHelpers.assertNoErrors(project);
	}

	@Test
	public void createEJBProjectWithDescriptorTest()
			throws InterruptedException, CoreException {
		String projectName = "EJBWithDescriptor";
		createEJBProjectWithDescriptor(projectName);
		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectName);
		//WorkspaceHelpers.assertNoErrors(project);
	}

	@Test
	public void createSessionBeanStateless() throws InterruptedException,
			CoreException {
		String projectName = "sessionBeans";
		String fileName = "StatelessBean";
		String packageName = "beans";

		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectName);
		if (!project.exists()) {
			createEJBProjectWithoutDescriptor(projectName);
			createPackage(projectName, packageName);
		}
		
		SWTUtilExt util = new SWTUtilExt(bot);
		SWTBotExt botExt = new SWTBotExt();
		botExt.menu("File").menu("New").menu("Other...").click();
		SWTBot shell = botExt.shell("New").activate().bot();
		shell.tree().expandNode("EJB").select("Session Bean (EJB 3.x)").click();
		shell.button("Next >").click();
		botExt.comboBoxWithLabel("Project:").setSelection(projectName);
		botExt.textWithLabel("Java package:").setText(packageName);
		botExt.comboBoxWithLabel("State type:").setSelection("Stateless");
		botExt.textWithLabel("Class name:").setText(fileName);
		botExt.button("Finish").click();
		util.waitForAll();
	}

	@Test
	public void createSessionBeanStateful() throws InterruptedException,
			CoreException {
		String projectName = "sessionBeans";
		String fileName = "StatefulBean";
		String packageName = "beans";
		
		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectName);
		if (!project.exists()) {
			createEJBProjectWithoutDescriptor(projectName);
			createPackage(projectName, packageName);
		}
		
		SWTUtilExt util = new SWTUtilExt(bot);
		SWTBotExt botExt = new SWTBotExt();
		botExt.menu("File").menu("New").menu("Other...").click();
		SWTBot shell = botExt.shell("New").activate().bot();
		shell.tree().expandNode("EJB").select("Session Bean (EJB 3.x)").click();
		shell.button("Next >").click();
		botExt.comboBoxWithLabel("Project:").setSelection(projectName);
		botExt.textWithLabel("Java package:").setText(packageName);
		botExt.textWithLabel("Class name:").setText(fileName);
		botExt.comboBoxWithLabel("State type:").setSelection("Stateful");
		botExt.button("Finish").click();
		util.waitForAll();
	}
	
	@Test
	public void createSessionBeanSingleton() throws InterruptedException,
			CoreException {
		String projectName = "sessionBeans";
		String fileName = "SingletonBean";
		String packageName = "beans";
		

		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectName);
		if (!project.exists()) {
			createEJBProjectWithoutDescriptor(projectName);
			createPackage(projectName, packageName);
		}
		
		SWTUtilExt util = new SWTUtilExt(bot);
		SWTBotExt botExt = new SWTBotExt();
		botExt.menu("File").menu("New").menu("Other...").click();
		SWTBot shell = botExt.shell("New").activate().bot();
		shell.tree().expandNode("EJB").select("Session Bean (EJB 3.x)").click();
		shell.button("Next >").click();
		botExt.comboBoxWithLabel("Project:").setSelection(projectName);
		botExt.textWithLabel("Java package:").setText(packageName);
		botExt.textWithLabel("Class name:").setText(fileName);
		botExt.comboBoxWithLabel("State type:").setSelection("Singleton");
		botExt.button("Finish").click();
		util.waitForAll();
	}
	
	@Test
	public void createMessageDrivenBean() throws InterruptedException,
			CoreException {
		String projectName = "sessionBeans";
		String fileName = "MessageDrivenBean";
		String packageName = "beans";
		

		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectName);
		if (!project.exists()) {
			createEJBProjectWithoutDescriptor(projectName);
			createPackage(projectName, packageName);
		}
		
		SWTUtilExt util = new SWTUtilExt(bot);
		SWTBotExt botExt = new SWTBotExt();
		botExt.menu("File").menu("New").menu("Other...").click();
		SWTBot shell = botExt.shell("New").activate().bot();
		shell.tree().expandNode("EJB").select("Message-Driven Bean (EJB 3.x)").click();
		shell.button("Next >").click();
		botExt.comboBoxWithLabel("Project:").setSelection(projectName);
		botExt.textWithLabel("Java package:").setText(packageName);
		botExt.textWithLabel("Class name:").setText(fileName);
		botExt.button("Finish").click();
		util.waitForAll();
	}
	
	

	private void createEJBProjectWithoutDescriptor(String projectName)
			throws InterruptedException, CoreException {
		SWTUtilExt util = new SWTUtilExt(bot);
		SWTBotExt botExt = new SWTBotExt();
		botExt.menu("File").menu("New").menu("Other...").click();
		SWTBot shell = botExt.shell("New").activate().bot();
		shell.tree().expandNode("EJB").select("EJB Project").click();
		shell.button("Next >").click();
		botExt.textWithLabel("Project name:").setText(projectName);
		botExt.button("Next >").click();
		botExt.button("Next >").click();
		botExt.button("Finish").click();
		util.waitForAll();
		botExt.button("No").click();
		util.waitForAll();
	}

	private void createEJBProjectWithDescriptor(String projectName)
			throws InterruptedException, CoreException {
		SWTUtilExt util = new SWTUtilExt(bot);
		SWTBotExt botExt = new SWTBotExt();
		botExt.menu("File").menu("New").menu("Other...").click();
		SWTBot shell = botExt.shell("New").activate().bot();
		shell.tree().expandNode("EJB").select("EJB Project").click();
		shell.button("Next >").click();
		botExt.textWithLabel("Project name:").setText(projectName);
		botExt.button("Next >").click();
		botExt.button("Next >").click();
		botExt.checkBox("Generate ejb-jar.xml deployment descriptor").select();
		botExt.button("Finish").click();
		util.waitForAll();
		botExt.button("No").click();
		util.waitForAll();
	}

	private static void createPackage(String projectName, String packageName)
			throws InterruptedException {
		SWTUtilExt util = new SWTUtilExt(bot);
		SWTBotExt botExt = new SWTBotExt();
		botExt.menu("File").menu("New").menu("Other...").click();
		SWTBot shell = botExt.shell("New").activate().bot();
		shell.tree().expandNode("Java").select("Package").click();
		shell.button("Next >").click();
		botExt.textWithLabel("Source folder:").setText(
				projectName + "/ejbModule");
		botExt.textWithLabel("Name:").setText(packageName);
		botExt.button("Finish").click();
		util.waitForAll();
	}
}
