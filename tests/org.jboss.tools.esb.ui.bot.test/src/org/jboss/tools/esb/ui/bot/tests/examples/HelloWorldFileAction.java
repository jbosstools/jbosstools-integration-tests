package org.jboss.tools.esb.ui.bot.tests.examples;

import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.SWTOpenExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.ESBESBFile;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTableItem;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.GeneralFolder;

@Require(server=@Server(type=ServerType.SOA,state=ServerState.Running))
public class HelloWorldFileAction extends ESBExampleTest {
	
	public static String inputDir = "inputDir";
	public static String outputDir = "outputDir";
	public static String errorDir = "errorDir";
	public static String projectName = "helloworld_file_action";
	public static String clientProjectName = "helloworld_file_action_client";
	public static String baseDir = null;	
	
	@Override
	public String getExampleName() {
		return "JBoss ESB HelloWorld File Action Example - ESB";
	}
	@Override
	public String[] getProjectNames() {
		return new String[] {"helloworld_file_action","helloworld_file_action_client"};
	}
	@Override
	protected void executeExample() {
		
		/* Create the data directories needed by the quickstart */	
		bot.menu("File").menu("New").menu("Folder").click();	
		bot.tree(0).getTreeItem(projectName).select();
		bot.text(1).setText(inputDir);
		bot.button("&Finish").click();
		bot.sleep(Timing.time3S());
		
		bot.menu("File").menu("New").menu("Folder").click();	
		bot.tree(0).getTreeItem(projectName).select();
		bot.text(1).setText(outputDir);
		bot.button("&Finish").click();
		bot.sleep(Timing.time3S());
		
		bot.menu("File").menu("New").menu("Folder").click();	
		bot.tree(0).getTreeItem(projectName).select();
		bot.text(1).setText(errorDir);
		bot.button("&Finish").click();
		bot.sleep(Timing.time3S());
		
		/* We need to get the project base dir for the directory definitions in jboss-esb.xml */ 
		SWTBotView theSWTBotView = open.viewOpen(ActionItem.View.GeneralNavigator.LABEL);
		SWTBotTreeItem theProject = bot.tree(0).getTreeItem(projectName).select();
		bot.menu("File").menu("Properties").click();
		
		if (System.getProperty("file.separator").equals("/")) { 
			baseDir = bot.textWithLabel("Location:").getText() + System.getProperty("file.separator");
		}	
		else 	{
			/* Needed to avoid a syntax error with Windows \ dir path characters */
			//baseDir = bot.textWithLabel("Location:").getText().replaceAll(System.getProperty("file.separator"), System.getProperty("file.separator") + System.getProperty("file.separator")) + System.getProperty("file.separator");
			baseDir = bot.textWithLabel("Location:").getText().replaceAll(System.getProperty("file.separator"), "zzz");
		}						
//		if (System.getProperty("file.separator").equals("/")) { 
//			baseDir = bot.textWithLabel("Location:").getText() + System.getProperty("file.separator");
//		}
//		else {
//			baseDir = bot.textWithLabel("Location:").getText().replaceAll("\\", "\\\\") + System.getProperty("file.separator");
//		}
		bot.button("OK").click();
				
		theSWTBotView = open.viewOpen(ActionItem.View.GeneralNavigator.LABEL);		
		SWTBotEditor editor = projectExplorer.openFile(projectName, "esbcontent","META-INF","jboss-esb.xml");
		SWTBot theEditor = editor.bot();
		
		theEditor.tree(0).expandNode("jboss-esb.xml", true);	
		SWTBotTreeItem jbossesbxml = theEditor.tree(0).getTreeItem("jboss-esb.xml");
		SWTBotTreeItem providers = jbossesbxml.getNode("Providers");
		SWTBotTreeItem FSProvider1 = providers.getNode("FSprovider1");
		SWTBotTreeItem helloFileChannel = FSProvider1.getNode("helloFileChannel");
		SWTBotTreeItem filter = helloFileChannel.getNode("Filter");
		filter.select();
		
		theEditor.text("@INPUTDIR@").setText(baseDir + inputDir);
		theEditor.text("@OUTPUTDIR@").setText(baseDir + outputDir);
		theEditor.text("@ERRORDIR@").setText(baseDir + errorDir);
	
		editor.save();	
		bot.sleep(Timing.time30S());		
		//bot.sleep(30000l);
		
		/* Deploy the quickstart */
		super.executeExample();	
		
		/* Now, edit the client code */
		theSWTBotView = open.viewOpen(ActionItem.View.GeneralNavigator.LABEL);
		SWTBotTreeItem theClientProject = bot.tree(0).getTreeItem(clientProjectName).select();
		theClientProject.expand();		
		//bot.sleep(30000l);
		
		editor = projectExplorer.openFile(clientProjectName, "src","org.jboss.soa.esb.samples.quickstart.helloworldfileaction.test", "CreateTestFile.java");
		theEditor = editor.bot();
		//System.out.println ("DEBUG " + theEditor.styledText().getText() );
				
		theEditor.styledText().insertText(10, 0, "//");
		theEditor.styledText().insertText(11, 0, "\t\tString inputDirectory = \"" + baseDir + "\" + System.getProperty(\"file.separator\") + \"inputDir\";\n");
		theEditor.styledText().insertText(12, 0, "//");
		theEditor.styledText().insertText(13, 0, "\t\tString fileName = \"MyInput.dat" + "\";\n");
		theEditor.styledText().insertText(14, 0, "//");
		theEditor.styledText().insertText(15, 0, "\t\tString fileContents = \"Hello World In A File\";\n");
		theEditor.styledText().insertText(16, 0, "//");
		theEditor.styledText().insertText(17, 0, "\t\tFile x = new File(inputDirectory + System.getProperty(\"file.separator\") + fileName);\n");
		theEditor.styledText().insertText(23, 0, "//");
		theEditor.styledText().insertText(24, 0, "\t\tSystem.out.println(\"Error while writing the file: \" + inputDirectory + System.getProperty(\"file.separator\") + fileName);\n");

		//bot.sleep(30000l);
		//System.out.println ("DEBUG " + theEditor.styledText().getText() );
		editor.save();		
		//bot.sleep(30000l);
		
		String text = executeClientGetServerOutput(getExampleClientProjectName(),"src",
				"org.jboss.soa.esb.samples.quickstart.helloworldfileaction.test",
				"CreateTestFile.java"); 
		assertFalse ("Test fails due to ESB deployment error: NNNN", text.contains("ERROR [org.apache.juddi.v3.client.transport.wrapper.RequestHandler]"));
		assertNotNull("Calling JMS Send message failed, nothing appened to server log",text);	
		assertTrue("Calling JMS Send message failed, unexpected server output :"+text,text.contains("Body: Hello World"));
		
		SWTTestExt.servers.removeAllProjectsFromServer();
	}
}
