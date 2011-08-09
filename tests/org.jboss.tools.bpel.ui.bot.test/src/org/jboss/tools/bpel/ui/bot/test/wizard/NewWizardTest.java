package org.jboss.tools.bpel.ui.bot.test.wizard;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;

public class NewWizardTest extends TestCase{
private static SWTWorkbenchBot bot; 
private IProject project;
protected void setUp() throws Exception {
	bot = new SWTWorkbenchBot(); 
    bot.viewByTitle("Welcome").close();  
}
public void testNewWizard() throws IOException, CoreException{
   createBPELProject();
   createBPELFile();
}

private void createBPELFile() throws IOException, CoreException {
	bot.menu("File").menu("New").menu("Other...").click();
	bot.shell("New").activate();
	SWTBotTree tree  = bot.tree();
	tree.expandNode("BPEL 2.0").expandNode("BPEL Process File").select();
	//tree.expandNode("BPEL 2.0").expandNode("New BPEL Process File").select();
    assertTrue(bot.button("Next >").isEnabled());
    bot.button("Next >").click();
   
    assertFalse(bot.button("Next >").isEnabled());
    
    bot.textWithLabel("BPEL Process Name:").setText("a"); 
    bot.comboBoxWithLabel("Namespace:").setText("http://eclipse.org/bpel/sample"); 
    bot.comboBoxWithLabel("Template:").setSelection(2); 
    assertTrue(bot.button("Next >").isEnabled());
    bot.button("Next >").click();
    assertEquals("a", bot.textWithLabel("Service Name").getText());
    bot.button("Finish").click();
    bot.sleep(10000);
    
    IFile bpelFile = project.getFile( new Path("bpelContent/a.bpel"));
    assertNotNull(bpelFile);
    IFile wsdlFile = project.getFile( new Path("bpelContent/aArtifacts.wsdl"));
    assertNotNull(wsdlFile);    
    
    String con = getContents(wsdlFile.getContents(),wsdlFile);
    assertTrue(con.contains("tns:aBinding"));
}
private void createBPELProject() {
	bot.menu("File").menu("New").menu("Project...").click();
	bot.shell("New Project").activate();
	SWTBotTree tree  = bot.tree();
	tree.expandNode("BPEL 2.0").expandNode("BPEL Project").select();
    assertTrue(bot.button("Next >").isEnabled());
    bot.button("Next >").click();
    bot.shell("BPEL Project").activate();
    assertFalse(bot.button("Finish").isEnabled());
    
    bot.textWithLabel("Project name:").setText("A"); 
    assertTrue(bot.button("Finish").isEnabled());
    bot.button("Finish").click();
    bot.sleep(10000);
    project = ResourcesPlugin.getWorkspace().getRoot().getProject("A");
    assertNotNull(project);
}
protected void tearDown() throws Exception {
	bot = null;
}

protected String getContents(InputStream is, IFile file) throws IOException {
	BufferedInputStream bis = new BufferedInputStream(is);
    int l = (int)file.getLocation().toFile().length();
    byte[] bs = new byte[l];
    l = bis.read(bs, 0, l);
    bis.close();
    return new String(bs);
}
}
