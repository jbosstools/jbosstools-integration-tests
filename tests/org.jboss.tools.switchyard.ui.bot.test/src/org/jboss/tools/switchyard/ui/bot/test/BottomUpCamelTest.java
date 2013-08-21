package org.jboss.tools.switchyard.ui.bot.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.switchyard.reddeer.component.Component;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.editor.TextEditor;
import org.jboss.tools.switchyard.reddeer.server.ServerDeployment;
import org.jboss.tools.switchyard.reddeer.wizard.HTTPBindingWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ImportFileWizard;
import org.jboss.tools.switchyard.reddeer.wizard.NewServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.PromoteServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.SwitchYardProjectWizard;
import org.jboss.tools.switchyard.ui.bot.test.suite.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.switchyard.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.Server;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.State;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.Type;
import org.jboss.tools.switchyard.ui.bot.test.suite.SwitchyardSuite;
import org.junit.Before;
import org.junit.Test;

/**
 * Creation test from existing Camel route
 * 
 * @author apodhrad
 * 
 */
@CleanWorkspace
@Perspective(name = "Java EE")
@Server(type = Type.ALL, state = State.RUNNING)
public class BottomUpCamelTest extends SWTBotTestCase {

	public static final String PROJECT = "camel_project";
	public static final String PACKAGE = "com.example.switchyard.camel_project";
	public static final String JAVA_FILE = "MyRouteBuilder";

	@Before
	public void closeSwitchyardFile() {
		try {
			new SwitchYardEditor().saveAndClose();
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
	}

	@Test
	public void bottomUpCamelTest() throws Exception {
		new SwitchYardProjectWizard(PROJECT).impl("Camel Route").binding("HTTP").create();
		Project project = new ProjectExplorer().getProject(PROJECT);

		// Import java file
		project.getProjectItem("src/main/java", PACKAGE).select();
		new ImportFileWizard().importFile("resources/java", JAVA_FILE + ".java");

		// Edit java file
		project.getProjectItem("src/main/java", PACKAGE, JAVA_FILE + ".java").open();
		new TextEditor(JAVA_FILE + ".java").deleteLineWith("package")
				.type("package " + PACKAGE + ";").saveAndClose();

		new SwitchYardEditor().addComponent("Component");
		new Component("Component").select();
		new SwitchYardEditor().activateTool("Camel (Java)");
		new Component("Component").click();

		new PushButton("Browse...").click();
		Bot.get().shell("Select entries").activate();
		new DefaultText(0).setText(JAVA_FILE);
		Bot.get().sleep(1000);
		Bot.get().shell("Select entries").activate();
		new PushButton("OK").click();
		Bot.get().shell("Camel Implementation").activate();
		new PushButton("Finish").click();

		new Component("Component").contextButton("Service").click();
		new NewServiceWizard().createJavaInterface("Hello").activate().finish();

		// Edit the interface
		new Service("Hello").doubleClick();
		new TextEditor("Hello.java").typeAfter("Hello", "String sayHello(String name);")
				.saveAndClose();

		// Edit the camel route
		new Component("Component").doubleClick();
		new TextEditor(JAVA_FILE + ".java").deleteLineWith("file:in")
				.type("from(\"switchyard://Hello\")").deleteLineWith("file:out").type(";")
				.saveAndClose();

		PromoteServiceWizard wizard = new Service("Hello").promoteService();
		wizard.activate().setServiceName("HelloService").finish();

		new Service("HelloService").addBinding("HTTP");
		new HTTPBindingWizard().setContextpath(PROJECT).setOperation("sayHello").finish();
		new SwitchYardEditor().save();
		
		Bot.get().sleep(1000);
		
		new Service("HelloService").contextButton("Properties").click();
		new DefaultTreeItem("Bindings").select();

		try {
			assertEquals(PROJECT, new LabeledText("Context Path").getText());
			assertEquals("sayHello", new DefaultCombo(0).getText());
		} finally {
			new PushButton("OK").click();
		}
		new SwitchYardEditor().save();
		
		new ServerDeployment(SwitchyardSuite.getServerName()).deployProject(PROJECT);

		String url = "http://localhost:8080/" + PROJECT;
		assertEquals("Hello apodhrad", curl(url, "apodhrad"));
		assertEquals("Hello JBoss", curl(url, "JBoss"));
	}

	public static String curl(String url, String data) throws MalformedURLException, IOException {
		StringBuffer response = new StringBuffer();
		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
		con.setRequestMethod("POST");
		if (data != null) {
			con.setDoOutput(true);
			con.getOutputStream().write(data.getBytes());
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String line = null;
		while ((line = in.readLine()) != null) {
			response.append(line);
		}
		in.close();
		return response.toString();
	}
}
