package org.jboss.tools.usercase.ticketmonster.ui.bot.test.part2;

import static org.junit.Assert.*;

import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardPage;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.junit.Before;
import org.junit.Test;

public class Chapter12AddNewEntity extends AbstractPart2Test{
	
	@Before
	public void importProject(){
		createTicketMonsterEAP6();
		ProjectExplorer pe = new ProjectExplorer();
		ticketMonsterProject = pe.getProject(TICKET_MONSTER_NAME);
	}
	
	@Test
	public void addEntityUsingJBDS(){
		ticketMonsterProject.getProjectItem("Java Resources","src/main/java","org.jboss.jdf.example.ticketmonster.model").select();
		new ContextMenu("New","Class").select();
		new DefaultShell("New Java Class");
		NewJavaClassWizardDialog newJava  = new NewJavaClassWizardDialog();
		NewJavaClassWizardPage newJavaPage = newJava.getFirstPage();
		newJavaPage.setName("Venue");
		newJava.finish();
		replaceEditorContentWithFile("Venue.java", "resources/classes/VenueEntity.txt", 3, 0, false, false);
		new ShellMenu("Source","Generate Getters and Setters...").select();;
		new DefaultShell("Generate Getters and Setters");
		new PushButton("Select All").click();
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning());
		//bug - shell menu is not enabled ,context menu is https://issues.jboss.org/browse/JBIDE-13210
		new ContextMenu("Source","Generate Hibernate/JPA annotations...").select();
		new DefaultShell("Save Modified Resources");
		new PushButton("OK").click();
		new DefaultShell("Hibernate: add JPA annotations");
		new PushButton("Next >").click();
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsActive("Hibernate: add JPA annotations"));
		String editorText = new TextEditor("Venue.java").getText().replace("\n", " ");
		assertTrue(editorText.contains("import javax.persistence.Entity;"));
		assertTrue(editorText.contains("import javax.persistence.GeneratedValue;"));
		assertTrue(editorText.contains("import javax.persistence.Id;"));
		assertTrue(editorText.contains("@Entity public class Venue"));
		assertTrue(editorText.contains("@Id 	@GeneratedValue 	private Long id;"));
	}
	

}
