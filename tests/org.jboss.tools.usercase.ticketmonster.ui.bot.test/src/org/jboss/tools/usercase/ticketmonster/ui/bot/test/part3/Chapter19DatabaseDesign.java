package org.jboss.tools.usercase.ticketmonster.ui.bot.test.part3;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.editor.DefaultEditor;
import org.junit.Before;
import org.junit.Test;

public class Chapter19DatabaseDesign extends AbstractPart3Test{
	//TicketCategory, EventCategory
	@Before
	public void setup(){
		createTicketMonsterEAP6();
		cleanTicketMonster();
	}
	
	@Test
	public void createMediaTypeAndItem(){
		createEnumClass("MediaType", TICKET_MONSTER_PACKAGE+"."+PACKAGE_MODEL);
		replaceEditorContentWithURL("MediaType.java", TicketMonsterClassURL.MEDIA_TYPE, 0, 0, true, true);
		createJavaClass("MediaItem", TICKET_MONSTER_PACKAGE+"."+PACKAGE_MODEL);
		replaceEditorContentWithFile("MediaItem.java", "resources/classes/part3/MediaItem", 3, 0, false, false);
		generateGettersAndSetters();
		generateJPAAnnotations();
		assertEditorContainsAnnotation("MediaItem.java", "@Id");
		assertEditorContainsAnnotation("MediaItem.java", "@GeneratedValue");
		assertEditorContainsAnnotation("MediaItem.java", "@Enumerated");
		
		//wait for https://github.com/jboss-reddeer/reddeer/issues/381
		
	}
	
	private void assertEditorContainsAnnotation(String editor, String annotation){
		new DefaultEditor(editor);
		String editorText = new DefaultStyledText().getText();
		assertTrue("Editor "+editor+" is missing "+annotation+" annotation",editorText.contains(annotation));
	}
	
	private void generateGettersAndSetters(){
		new ShellMenu("Source","Generate Getters and Setters...").select();;
		new DefaultShell("Generate Getters and Setters");
		new PushButton("Select All").click();
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning());
	}
	
	private void generateJPAAnnotations(){
		new ContextMenu("Source","Generate Hibernate/JPA annotations...").select();
		new DefaultShell("Save Modified Resources");
		new PushButton("OK").click();
		new DefaultShell("Hibernate: add JPA annotations");
		new PushButton("Next >").click();
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsActive("Hibernate: add JPA annotations"));
	}
	
	

}
