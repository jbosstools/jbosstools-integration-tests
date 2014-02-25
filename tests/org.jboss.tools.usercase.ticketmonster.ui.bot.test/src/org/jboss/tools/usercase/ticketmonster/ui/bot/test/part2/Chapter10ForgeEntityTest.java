package org.jboss.tools.usercase.ticketmonster.ui.bot.test.part2;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.views.contentoutline.OutlineView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.editor.DefaultEditor;
import org.jboss.reddeer.workbench.editor.TextEditor;
import org.jboss.reddeer.workbench.view.impl.WorkbenchView;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.condition.ForgeContainsText;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.forge.ForgeConsole;
import org.junit.Before;
import org.junit.Test;

public class Chapter10ForgeEntityTest extends AbstractPart2Test{
	
	private Project ticketMonsterProject;
	private ForgeConsole forge;
	
	public static final String FORGE_ENTITY="entity --named Event --package org.jboss.jdf.example.ticketmonster.model";
	public static final String FORGE_ENTITY_NAME="field string --named name";
	public static final String FORGE_VALIDATION="validation setup --provider JAVA_EE";
	public static final String FORGE_NOT_NULL_NAME="constraint NotNull --onProperty name";
	public static final String FORGE_CONSTRAINT_NAME="constraint Size --onProperty name --min 5 --max 50 --message \"Must be > 5 and < 50\"";
	public static final String FORGE_ENTITY_DESCRIPTION="field string --named description";
	public static final String FORGE_CONSTRAINT_DESCRIPTION="constraint Size --onProperty description --min 20 --max 1000 --message \"Must be > 20 and <1000\"";
	public static final String FORGE_ENTITY_MAJOR="field boolean --named major";
	public static final String FORGE_ENTITY_PICTURE="field string --named picture";
	
	
	@Before
	public void importProject(){
		createTicketMonsterEAP6();
		ProjectExplorer pe = new ProjectExplorer();
		ticketMonsterProject = pe.getProject(TICKET_MONSTER_NAME);
		ticketMonsterProject.select();
		ticketMonsterProject.getTreeItem().doubleClick(); //expand does not work
	}
	
	@Test
	public void createEntityViaForge(){
		ticketMonsterProject.getProjectItem("Java Resources","src/main/java",TICKET_MONSTER_PACKAGE+"."+PACKAGE_MODEL).select();
		new ContextMenu("Show In","Forge Console").select();
		new DefaultShell("Forge Not Running");
		new PushButton("Yes").click();
		new WorkbenchView("Forge Console");
		new WaitUntil(new JobIsRunning());
		new WaitWhile(new JobIsRunning(),TimePeriod.LONG);
		forge = new ForgeConsole();
		forge.initStyledText();
		new WaitUntil(new ForgeContainsText(forge, "[ticket-monster] model $"));
		
		createEntity();
		addNameToEntity();
		addNameValidation();
		addDescriptionToEntity();
		addDescriptionValidation();
		addMajorToEntity();
		addPictureToEntity();
		
		OutlineView outline = new OutlineView();
		for(TreeItem i: outline.outlineElements()){
			if(i.getText().equals("Event")){
				try{
					i.getItem("name : String");
				}catch(SWTLayerException ex){
					fail("Outline view does not contain name");
				}
				try{
					i.getItem("description : String");
				}catch(SWTLayerException ex){
					fail("Outline view does not contain description");
				}
				try{
					i.getItem("picture : String");
				}catch(SWTLayerException ex){
					fail("Outline view does not contain picture");
				}
				try{
					i.getItem("major : boolean");
				}catch(SWTLayerException ex){
					fail("Outline view does not contain major");
				}
				
			}
		}
		
		assertProperEditorContent();
	}
	
	private void createEntity(){
		forge.typeCommand(FORGE_ENTITY);
	}
	
	private void addNameToEntity(){
		forge.typeCommand(FORGE_ENTITY_NAME);
	}
	
	private void addNameValidation(){
		forge.typeCommand(FORGE_VALIDATION);
		forge.typeCommand(FORGE_NOT_NULL_NAME);
		forge.typeCommand(FORGE_CONSTRAINT_NAME);
	}
	
	private void addDescriptionToEntity(){
		forge.typeCommand(FORGE_ENTITY_DESCRIPTION);
	}
	
	private void addDescriptionValidation(){
		forge.typeCommand(FORGE_CONSTRAINT_DESCRIPTION);
	}
	
	private void addMajorToEntity(){
		forge.typeCommand(FORGE_ENTITY_MAJOR);
	}
	
	private void addPictureToEntity(){
		forge.typeCommand(FORGE_ENTITY_PICTURE);
	}
	
	private void assertProperEditorContent(){
		new DefaultEditor("Event.java");
		
		BufferedReader br =null;
		String fileContent = null;
		try {
			br = new BufferedReader(new FileReader("resources/classes/EventEntity.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		    	sb.append(line);
		        line = br.readLine();
		        if(line != null){
		        	sb.append('\n');
		        }
		    }
		    fileContent = sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		assertEquals(fileContent, new TextEditor().getText());
	}
	

}
