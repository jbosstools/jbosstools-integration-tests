package org.jboss.tools.usercase.ticketmonster.ui.bot.test.part3;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardPage;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.workbench.editor.DefaultEditor;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.wizard.GenerateHashCodeAndEqualsDialog;
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
		NewJavaClassWizardPage javaPage = newjava.getFirstPage();
		javaPage.setName("TicketCategory");
		javaPage.setPackage(TICKET_MONSTER_PACKAGE+"."+PACKAGE_MODEL);
		newjava.finish();
		
		replaceEditorContentWithText("TicketCategory.java", "package "+TICKET_MONSTER_PACKAGE+"."+PACKAGE_MODEL+";\n\n", 0, 0, true, false);
		replaceEditorContentWithFile("TicketCategory.java", "resources/classes/part3/TicketCategoryPartial.txt", 2, 0, false, false);
		new DefaultEditor("TicketCategory.java");
		GenerateHashCodeAndEqualsDialog hashAndEqualsDialog = new GenerateHashCodeAndEqualsDialog();
		hashAndEqualsDialog.open(false);
		for(TreeItem i: hashAndEqualsDialog.getFields()){
			assertTrue(i.isChecked());
		}
		hashAndEqualsDialog.ok();
		assertTrue(new DefaultStyledText().getText().contains("equals(Object obj)"));
		assertTrue(new DefaultStyledText().getText().contains("hashCode()"));
	}

}
