package org.jboss.tools.switchyard.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardPage;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.ProjectItem;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.view.impl.WorkbenchView;
import org.jboss.tools.switchyard.reddeer.component.Drools;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.editor.TextEditor;
import org.jboss.tools.switchyard.reddeer.view.JUnitView;
import org.jboss.tools.switchyard.reddeer.widget.ProjectItemExt;
import org.jboss.tools.switchyard.reddeer.wizard.SwitchYardProjectWizard;
import org.junit.Test;

/**
 * Create simple Drools application, run JUnit test
 * @author lfabriko
 *
 */
public class DroolsTest extends SWTBotTestCase {

	private static final String PROJECT = "switchyard-drools-interview";
	private static final String PACKAGE = "org.switchyard.quickstarts.drools.service";
	private static final String GROUP_ID = "org.switchyard.quickstarts.drools.service";
	private static final String PACKAGE_MAIN_JAVA = "src/main/java";
	private static final String PACKAGE_MAIN_RESOURCES = "src/main/resources";
	private static final String INTERVIEW = "Interview";
	private static final String APPLICANT = "Applicant";
	private static final String TEST = INTERVIEW + "Test.java";
	
	
	@Test
	public void test01(){
		//new project, support rules
		new SwitchYardProjectWizard(PROJECT).impl("Rules (Drools)").groupId(GROUP_ID).packageName(PACKAGE).create();
		//open sy.xml, add rules, service, promote
		new Drools().setName(INTERVIEW).setfileName(INTERVIEW+".drl").setService(INTERVIEW).create();
		new Service(INTERVIEW,1).promoteService().finish();
		
		//insert rules
		openFile(PROJECT, PACKAGE_MAIN_RESOURCES, INTERVIEW + ".drl");
		new TextEditor(INTERVIEW+".drl").deleteLineWith("Interview").deleteLineWith("System.out.println").deleteLineWith("global")
		.type("global java.lang.String userName").newLine()
		.type("rule \"Is of valid age\"")
		.typeAfter("when", "$a : Applicant( age > 17 )")
		.typeAfter("then", "$a.setValid( true );").newLine()
		.type("System.out.println(\"********** \" + $a.getName() + \" is a valid applicant **********\");")
		.typeAfter("end", "rule \"Is not of valid age\"").newLine()
		.type("when").newLine()
		.type("$a : Applicant( age < 18 )").newLine()
		.type("then").newLine()
		.type("$a.setValid( false );").newLine()
		.type("System.out.println(\"***\" + $a.getName() + \" is not a valid applicant ***\");")
		.newLine()
		.type("end");
				
		//insert code
		openFile(PROJECT, PACKAGE_MAIN_JAVA, PACKAGE, INTERVIEW+".java");
		new TextEditor(INTERVIEW+".java").typeAfter("interface", "public void verify(Applicant applicant);");
		
		createPojo(APPLICANT);
		new TextEditor(APPLICANT + ".java")
		.typeAfter("class", "private String name; private int age; private boolean valid;")
		.newLine()
		.type("public Applicant(String name, int age){this.name=name;this.age=age;}")
		.generateGettersSetters("name");
		
		//test
		new org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor().show();
		bot.sleep(1000);
		new Service(INTERVIEW,2).newServiceTestClass();
		new TextEditor(TEST).deleteLineWith("null").type("Applicant message=new Applicant(\"Twenty\", 20);").deleteLineWith("Implement")
		.type("message=new Applicant(\"Ten\", 10);").type("service.operation(\"verify\").sendInOnly(message);");
		
		new ShellMenu("File", "Save All").select();
		
		ProjectItem item = new ProjectExplorer().getProject(PROJECT)
				.getProjectItem("src/test/java", PACKAGE,TEST);
		new ProjectItemExt(item).runAsJUnitTest();
		assertEquals("1/1", new JUnitView().getRunStatus());
		assertEquals("0", new JUnitView().getNumberOfErrors());
		assertEquals("0", new JUnitView().getNumberOfFailures());
	}
	
	private void openFile(String... file){
		//focus on project explorer
		new WorkbenchView("General", "Project Explorer").open();
		new DefaultTreeItem(0, file).doubleClick();
	}
	
	private void createPojo(String name){
		NewJavaClassWizardDialog wizard = new NewJavaClassWizardDialog();
		wizard.open();
		NewJavaClassWizardPage page = new NewJavaClassWizardPage(wizard);
		page.setName(name);
		new LabeledText("Source folder:").setText(PROJECT+"/"+PACKAGE_MAIN_JAVA);
		page.setPackage(PACKAGE);
		wizard.finish();
		bot.sleep(1000);
	}
}
