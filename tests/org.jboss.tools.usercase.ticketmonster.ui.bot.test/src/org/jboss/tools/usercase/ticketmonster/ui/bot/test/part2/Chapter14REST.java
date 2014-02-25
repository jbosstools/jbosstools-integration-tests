package org.jboss.tools.usercase.ticketmonster.ui.bot.test.part2;

import static org.junit.Assert.fail;

import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardPage;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.workbench.editor.DefaultEditor;
import org.jboss.reddeer.workbench.editor.Editor;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.condition.ProjectContainsItem;
import org.junit.Before;
import org.junit.Test;

public class Chapter14REST extends AbstractPart2Test{
	
	private Project ticketMonsterProject;
	
	@Before
	public void importProject(){
		createTicketMonsterEAP6();
		ProjectExplorer pe = new ProjectExplorer();
		ticketMonsterProject = pe.getProject(TICKET_MONSTER_NAME);
		ticketMonsterProject.select();
		ticketMonsterProject.getTreeItem().doubleClick(); //expand does not work
	}
	
	@Test
	public void createEventService(){
		ticketMonsterProject.getProjectItem("Java Resources","src/main/java","org.jboss.jdf.example.ticketmonster.rest").select();
		new ContextMenu("New","Class").select();
		new DefaultShell("New Java Class");
		NewJavaClassWizardDialog newJava  = new NewJavaClassWizardDialog();
		NewJavaClassWizardPage newJavaPage = newJava.getFirstPage();
		newJavaPage.setName("EventService");
		newJava.finish();
		Editor editor = new DefaultEditor("EventService.java");
		replaceEventServiceContent();
		boolean shellOpened = false;
		while(!shellOpened){
			try{
				new ContextMenu("Source","Organize Imports").select();;
				new DefaultShell("Organize Imports");
				shellOpened = true;
			} catch(SWTLayerException ex){
				shellOpened = false;
			}
		}
		new DefaultTableItem("javax.ws.rs.core.MediaType").select();
		new PushButton("Next >").click();
		new DefaultTableItem("org.jboss.jdf.example.ticketmonster.model.Event").select();
		new PushButton("Next >").click();
		new DefaultTableItem("javax.ws.rs.Produces").select();
		new PushButton("Next >").click();
		new DefaultTableItem("java.util.List").select();
		new PushButton("Next >").click();
		new DefaultTableItem("javax.inject.Inject").select();
		new PushButton("Next >").click();
		new DefaultTableItem("javax.enterprise.context.RequestScoped").select();
		new PushButton("Finish").click();
		editor.save();
		checkProjectExplorerRESTItems();
	}
	
	public void checkProjectExplorerRESTItems(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		try{
			new WaitUntil(new ProjectContainsItem(ticketMonsterProject,"JAX-RS Web Services","GET /rest/events"));
			new WaitUntil(new ProjectContainsItem(ticketMonsterProject,"JAX-RS Web Services","GET /rest/members"));
			new WaitUntil(new ProjectContainsItem(ticketMonsterProject,"JAX-RS Web Services","POST /rest/members"));
			new WaitUntil(new ProjectContainsItem(ticketMonsterProject,"JAX-RS Web Services","GET /rest/members/{id:[0-9][0-9]*}"));
		} catch (WaitTimeoutExpiredException ex){
			fail(ex.getMessage());
		}
	}
	
	private void replaceEventServiceContent(){
		replaceEditorContentWithFile("EventService.java", "resources/classes/EventService.txt",0,0,true, false);
	}

}
