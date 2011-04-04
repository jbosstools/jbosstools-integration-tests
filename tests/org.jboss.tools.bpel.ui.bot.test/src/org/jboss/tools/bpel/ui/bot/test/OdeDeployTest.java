package org.jboss.tools.bpel.ui.bot.test;

import org.eclipse.core.resources.IFile;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.jboss.tools.bpel.ui.bot.test.suite.BPELTest;
import org.jboss.tools.bpel.ui.bot.test.util.ResourceHelper;
import org.jboss.tools.ui.bot.ext.config.Annotations.SWTBotTestRequires;
import org.jboss.tools.ui.bot.ext.view.ProjectExplorer;
import org.jboss.tools.ui.bot.ext.view.ServersView;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
/**
 * 
 * @author psrna
 *
 */
@SWTBotTestRequires(perspective="BPEL Perspective")
public class OdeDeployTest extends BPELTest {
	
	static String BUNDLE   = "org.jboss.tools.bpel.ui.bot.test";
	ServersView sView = new ServersView();
	ProjectExplorer projExplorer = new ProjectExplorer();
	
	@Before
	public void setupWorkspace() throws Exception {
		ResourceHelper.importProject(BUNDLE, "/projects/bpel_say_hello", "say_hello");
		bot.viewByTitle("Project Explorer").setFocus();
		projExplorer.selectProject("say_hello");
	}
	
	@Test
	public void deployDescriptor() throws Exception {
		
		IFile deployFile = createNewDeployDescriptor("say_hello");

		bot.editorByTitle("deploy.xml").show();
		bot.editorByTitle("deploy.xml").setFocus();
		SWTBot editorBot = bot.editorByTitle("deploy.xml").bot();
		
		SWTBotTable table = editorBot.table(0);
		table.click(0, 1);
		bot.sleep(TIME_1S);
		
		editorBot.ccomboBox("-- none -- ").setSelection("SayHelloPort");
		table.click(0, 2);
		bot.editorByTitle("deploy.xml").save();
		
		String deployContent = loadFile(deployFile);
		Assert.assertTrue(deployContent != null);
		Assert.assertTrue(deployContent.contains("<deploy xmlns=\"http://www.apache.org/ode/schemas/dd/2007/03\" xmlns:examples=\"http://www.jboss.org/bpel/examples\">"));
		Assert.assertTrue(deployContent.contains("<process name=\"examples:HelloWorld\">"));
		Assert.assertTrue(deployContent.contains("<active>true</active>"));
		Assert.assertTrue(deployContent.contains("<retired>false</retired>"));
		Assert.assertTrue(deployContent.contains("<process-events generate=\"all\"/>"));
		Assert.assertTrue(deployContent.contains("<provide partnerLink=\"client\">"));
		Assert.assertTrue(deployContent.contains("<service name=\"examples:SayHelloService\" port=\"SayHelloPort\"/>"));
		Assert.assertTrue(deployContent.contains("</provide>"));
		Assert.assertTrue(deployContent.contains("</process>"));
		Assert.assertTrue(deployContent.contains("</deploy>"));
			
		bot.sleep(TIME_10S);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
