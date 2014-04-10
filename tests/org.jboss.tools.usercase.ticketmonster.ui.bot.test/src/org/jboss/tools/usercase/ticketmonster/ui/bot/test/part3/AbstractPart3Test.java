package org.jboss.tools.usercase.ticketmonster.ui.bot.test.part3;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.TicketMonsterBaseTest;

public class AbstractPart3Test extends TicketMonsterBaseTest{
	
	private Project ticketMonsterProject;
	
	protected void createTicketMonsterProject(){
		createTicketMonsterEAP6();
	}
	
	//this is chapter16
	protected void cleanTicketMonster(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		ticketMonsterProject = pe.getProject(TICKET_MONSTER_NAME);
		ticketMonsterProject.select();
		ticketMonsterProject.getTreeItem().doubleClick();
		
		deleteIfExists("Java Resources","src/main/java",TICKET_MONSTER_PACKAGE+"."+PACKAGE_CONTROLLER);
		deleteIfExists("Java Resources","src/main/java",TICKET_MONSTER_PACKAGE+"."+PACKAGE_DATA);
		deleteIfExists("Java Resources","src/main/java",TICKET_MONSTER_PACKAGE+"."+PACKAGE_MODEL,"Member.java");
		deleteIfExists("Java Resources","src/main/java",TICKET_MONSTER_PACKAGE+"."+PACKAGE_REST,"MemberResourceRESTService.java");
		deleteIfExists("Java Resources","src/main/java",TICKET_MONSTER_PACKAGE+"."+PACKAGE_SERVICE,"MemberRegistration.java");
		deleteIfExists("src","main","webapp","index.html");
		deleteIfExists("src","main","webapp","index.xhtml");
		deleteIfExists("src","main","webapp","WEB-INF","templates","default.xhtml");
		deleteIfExists("src","main","webapp","WEB-INF","faces-config.xml");
		deleteIfExists("src","main","webapp","mobile.html");
			
		ticketMonsterProject.getProjectItem("src","main","resources","import.sql").select();
		new ContextMenu("Open With","Text Editor").select();
		ticketMonsterProject.getProjectItem("src","main","resources","import.sql").open();
		replaceEditorContentWithFile("import.sql","resources/commands/importSQL.txt", 0, 0, true, true);
	}
	
	private void deleteIfExists(String... path ){
		if(ticketMonsterProject.containsItem(path)){
			ticketMonsterProject.getProjectItem(path).delete();
		}
	}
	

}
