package org.jboss.tools.usercase.ticketmonster.ui.bot.test.part3;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardPage;
import org.jboss.reddeer.eclipse.jdt.ui.dialogs.GenerateHashCodeEqualsDialog;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.junit.Before;
import org.junit.Test;

public class Chapter18FirstEntity extends AbstractPart3Test{
	
	@Before
	public void setup(){
		createTicketMonsterProject();
		cleanTicketMonster();
	}
	
	@Test
	public void generateHashCodeAndEqualsForTicketCategory(){
		projectExplorer.open();
		Project ticketMonster = projectExplorer.getProject(TICKET_MONSTER_NAME);
		ticketMonster.select();
		
		NewJavaClassWizardDialog newjava = new NewJavaClassWizardDialog();
		newjava.open();
		NewJavaClassWizardPage javaPage = new NewJavaClassWizardPage();
		javaPage.setName("TicketCategory");
		javaPage.setPackage(TICKET_MONSTER_PACKAGE+"."+PACKAGE_MODEL);
		newjava.finish();
		
		replaceEditorContentWithText("TicketCategory.java", "package "+TICKET_MONSTER_PACKAGE+"."+PACKAGE_MODEL+";\n\n", 0, 0, true, false);
		replaceEditorContentWithFile("TicketCategory.java", "resources/classes/part3/TicketCategoryPartial.txt", 2, 0, false, false);
		new DefaultEditor("TicketCategory.java");
		GenerateHashCodeEqualsDialog hashAndEqualsDialog = new GenerateHashCodeEqualsDialog();
		hashAndEqualsDialog.open(false);
		assertTrue(hashAndEqualsDialog.getFields().size() == 1);
		assertTrue(hashAndEqualsDialog.getFields().get(0).isFieldChecked());
		hashAndEqualsDialog.ok();
		assertTrue(new DefaultStyledText().getText().contains("equals(Object obj)"));
		assertTrue(new DefaultStyledText().getText().contains("hashCode()"));
	}

}
