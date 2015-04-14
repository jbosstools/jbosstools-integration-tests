package org.jboss.tools.usercase.ticketmonster.ui.bot.test.part2;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardPage;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.workbench.api.Editor;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.condition.ProjectContainsItem;
import org.junit.Before;
import org.junit.Test;

public class Chapter14REST extends AbstractPart2Test{
	
	@Before
	public void importProject(){
		createTicketMonsterEAP6();
		ProjectExplorer pe = new ProjectExplorer();
		ticketMonsterProject = pe.getProject(TICKET_MONSTER_NAME);
		if(ticketMonsterProject.containsItem("Java Resources","src/main/java","org.jboss.jdf.example.ticketmonster.model","Event.java")){
			return;
		}
		ticketMonsterProject.getProjectItem("Java Resources","src/main/java","org.jboss.jdf.example.ticketmonster.model").select();
		new ContextMenu("New","Class").select();
		new DefaultShell("New Java Class");
		NewJavaClassWizardDialog newJava  = new NewJavaClassWizardDialog();
		NewJavaClassWizardPage newJavaPage = newJava.getFirstPage();
		newJavaPage.setName("Event");
		newJava.finish();
		replaceEditorContentWithFile("Event.java", "resources/classes/EventEntity.txt", 0, 0, true, false);
		new DefaultEditor("Event.java").save();
	}
	
	@Test
	public void createEventService(){
		new ProjectExplorer().open();
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
				new ContextMenu("Source","Organize Imports").select();
				new DefaultShell("Organize Imports");
				shellOpened = true;
			} catch(SWTLayerException ex){
				shellOpened = false;
			}
		}
		handleImport();
		editor.save();
		checkProjectExplorerRESTItems();
	}
	
	
	private void handleImport(){
		while(new PushButton("Next >").isEnabled()){
			boolean found = false;
			for(TableItem item: new DefaultTable().getItems()){
				String itemText = item.getText();
				if(itemText.equals("javax.inject.Inject") || itemText.equals("javax.ws.rs.Produces") ||
						itemText.equals("javax.ws.rs.core.MediaType") || 
						itemText.equals("org.jboss.jdf.example.ticketmonster.model.Event") || 
						itemText.equals("javax.enterprise.context.RequestScoped") ||
						itemText.equals("java.util.List")) {
					item.select();
					new PushButton("Next >").click();
					found = true;
					break;
				}
			}
			if(!found){
				fail("Unable to find proper import");
			}
		}
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsActive("Organize Imports"));
			
	}
	
	public void checkProjectExplorerRESTItems(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		new WaitUntil(new ProjectContainsItem(ticketMonsterProject,"JAX-RS Web Services","GET /rest/events"));
		new WaitUntil(new ProjectContainsItem(ticketMonsterProject,"JAX-RS Web Services","GET /rest/members"));
		new WaitUntil(new ProjectContainsItem(ticketMonsterProject,"JAX-RS Web Services","POST /rest/members"));
		new WaitUntil(new ProjectContainsItem(ticketMonsterProject,"JAX-RS Web Services","GET /rest/members/{id:[0-9][0-9]*}"));
	}
	
	private void replaceEventServiceContent(){
		replaceEditorContentWithFile("EventService.java", "resources/classes/EventService.txt",0,0,true, false);
	}

}
