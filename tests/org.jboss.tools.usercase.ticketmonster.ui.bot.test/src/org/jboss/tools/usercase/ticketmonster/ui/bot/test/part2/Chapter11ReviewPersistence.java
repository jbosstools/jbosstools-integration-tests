package org.jboss.tools.usercase.ticketmonster.ui.bot.test.part2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.api.Group;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.junit.Before;
import org.junit.Test;

public class Chapter11ReviewPersistence extends AbstractPart2Test{
	
	@Before
	public void importProject(){
		createTicketMonsterEAP6();
		ProjectExplorer pe = new ProjectExplorer();
		ticketMonsterProject = pe.getProject(TICKET_MONSTER_NAME);
	}
	
	//tickemonster pdf says that location is src/main/resources/ without META-INF
	@Test
	public void checkPersistence(){
		assertTrue(ticketMonsterProject.containsItem("src","main","resources","META-INF","persistence.xml"));
	}
	
	@Test
	public void checkPersistenceContent(){
		ticketMonsterProject.getProjectItem("src","main","resources","META-INF","persistence.xml").open();
		new DefaultEditor("persistence.xml");
		new DefaultCTabItem("Connection").activate();
		Group databaseGrp = new DefaultGroup(new DefaultSection("Persistence Unit Connection"),"Database");
		assertEquals("java:jboss/datasources/ticket-monsterDS",new LabeledText(databaseGrp, "JTA data source:").getText());
		new DefaultCTabItem("Properties").activate();
		assertEquals("create-drop",new DefaultTable().getItem("hibernate.hbm2ddl.auto", 1).getText(2));
	}
	
	//tickemonster pdf says that location is src/main/webapp/ without WEB-INF
	@Test
	public void checkDS(){
		assertTrue(ticketMonsterProject.containsItem("src","main","webapp","WEB-INF","ticket-monster-ds.xml"));
	}
	
	@Test
	public void addSQLStatements(){
		ticketMonsterProject.getProjectItem("src","main","resources","import.sql").select();
		new ContextMenu("Open With","Text Editor").select();
		replaceEditorContentWithFile("import.sql", "resources/commands/importSQL.txt",19,0, false, true);
	}

}
