package org.jboss.tools.ws.ui.bot.test.rest;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ws.reddeer.ui.wizards.jaxrs.JAXRSApplicationWizard;
import org.jboss.tools.ws.reddeer.ui.wizards.jaxrs.JAXRSApplicationWizardPage;
import org.jboss.tools.ws.reddeer.ui.wizards.jaxrs.JAXRSApplicationWizardPage.DeploymentDescriptorWizardPart;
import org.jboss.tools.ws.reddeer.ui.wizards.jaxrs.JAXRSApplicationWizardPage.SubclassOfApplicationWizardPart;
import org.jboss.tools.ws.ui.bot.test.WSTestBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests operates on JAX-RS Application wizard
 * 
 * @author Radoslav Rabara
 */
@RunWith(RedDeerSuite.class)
@JBossServer(state=ServerReqState.PRESENT)
public class CreateJAXRSApplicationTest extends WSTestBase {

	protected final JAXRSApplicationWizard wizard = new JAXRSApplicationWizard();
	protected final JAXRSApplicationWizardPage page = new JAXRSApplicationWizardPage();
	
	private final String[] webXmlPath = {"WebContent", "WEB-INF", "web.xml"};
	
	private final String PROJECT_SRC_FOLDER_PATH = getWsProjectName() + "/src";
	private final String PACKAGE_NAME = "org.rest.test";
	private final String FILE_NAME = "RestApp";
	private final String APPLICATION_PATH = "/app/path";
	
	private final String ERROR_SOURCE_FOLDER_NAME_IS_EMPTY = " Source folder name is empty.";
	
	private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
	
	@Override
    protected String getWsProjectName() {
        return "JAXRSApplicationTestProject";
    }
	
	@Before
	public void setup() {
		super.setup();
		wizard.open();
	}
	
	@After
	public void cleanup() {
		try {//close the wizard if it's present
			wizard.cancel();
		} catch(RedDeerException e) {
			
		}
		super.cleanup();
		deleteAllProjects();
	}
	
	@Test
	public void createSubclassOfApplicationTest() {
		/* select "Subclass of javax.ws.rs.core.Application" option */
		SubclassOfApplicationWizardPart wp = page.useSubclassOfApplication();
		
		/* set fields */
		wp.setSourceFolder(PROJECT_SRC_FOLDER_PATH);
		wp.setPackage(PACKAGE_NAME);
		wp.setName(FILE_NAME);
		wp.setApplicationPath(APPLICATION_PATH);
		
		/* create JAX-RS Activator */
		wizard.finish();
		
		/* get source folder */
		ProjectItem srcProjectItem = getProject().getProjectItem("Java Resources", "src");
		
		/* source folder contains the specified package */
		List<ProjectItem> srcChildren = srcProjectItem.getChildren();
		if(PACKAGE_NAME != null) {
			assertContains(srcChildren, PACKAGE_NAME,
						"Src folder doesn't contain package \"" + PACKAGE_NAME + "\" but it contains "
						+ Arrays.toString(srcChildren.toArray()));
		}
		assertTrue("Src folder should contain just 1 package but it contains "
					+ Arrays.toString(srcChildren.toArray()), srcChildren.size() == 1);
		
		/* package have to contain the specified class = activator */
		ProjectItem pkgProjectItem = srcChildren.get(0);
		List<ProjectItem> pkgChildren = pkgProjectItem.getChildren();
		assertContains(pkgChildren, FILE_NAME + ".java",
					"Package doesn't contain file \"" + PACKAGE_NAME + ".java\" but it contains "
					+ Arrays.toString(srcChildren.toArray()));
		assertTrue("Package should contain just 1 file but it contains "
					+ Arrays.toString(srcChildren.toArray()), pkgChildren.size() == 1);
		
		/* the class contains @ApplicationPath and that it extends Application */
		ProjectItem sourceFile = pkgChildren.get(0);
		sourceFile.open();
		TextEditor editor = new TextEditor();
		String generatedText = editor.getText();
		editor.close();
		generatedText.contains("@ApplicationPath(\"" + APPLICATION_PATH + "\")");
		generatedText.contains("extends Application");
		generatedText.contains("import javax.ws.rs.ApplicationPath;");
		generatedText.contains("import javax.ws.rs.core.Application;");
		
		/* there is a web.xml*/
		assertTrue("Project does not contains " + Arrays.toString(webXmlPath), getProject().containsItem(webXmlPath));
	}
	
	@Test
	public void createSubclassOfApplicationWithCommentsTest() {
		/* select "Subclass of javax.ws.rs.core.Application" option */
		SubclassOfApplicationWizardPart wp = page.useSubclassOfApplication();
		
		/* set fields */
		wp.setSourceFolder(PROJECT_SRC_FOLDER_PATH);
		wp.setPackage(PACKAGE_NAME);
		wp.setName(FILE_NAME);
		wp.setApplicationPath(APPLICATION_PATH);
		
		/* generate comments */
		wp.setGenerateComments(true);
		
		/* create JAX-RS Activator */
		wizard.finish();
		
		/* get generated class */
		ProjectItem generatedClass = getProject().getProjectItem("Java Resources", "src")
				.getChildren().get(0).getChildren().get(0);
		
		/* the class contains @ApplicationPath and that it extends Application */
		generatedClass.open();
		TextEditor editor = new TextEditor();
		String generatedText = editor.getText();
		editor.close();
		generatedText.contains("/**");
		generatedText.contains("*/");
		generatedText.contains("@ApplicationPath(\"" + APPLICATION_PATH + "\")");
		generatedText.contains("extends Application");
		generatedText.contains("import javax.ws.rs.ApplicationPath;");
		generatedText.contains("import javax.ws.rs.core.Application;");
	}
	
	@Test
	public void useDefinitionInTheWebDeploymentDescriptorTest() {
		/* select "Defined in the web deployment descriptor" option */
		DeploymentDescriptorWizardPart wp = page.useDeploymentDescriptor();
		
		/* create JAX-RS Activator */
		wp.setApplicationPath("/app/path");
		wizard.finish();
		
		assertThatWebXmlContainsRequiredText();
	}
	
	@Test
	public void setSourceFolderTest() {
		String NON_EXISTING_PROJECT_NAME = "NON_EXISTING_PROJECT";

		/* select "Subclass of javax.ws.rs.core.Application" option */
		SubclassOfApplicationWizardPart wp = page.useSubclassOfApplication();
		
		/* button, which is used to browse source folder, is always enabled */
		assertThat("Browse source folder should be always enabled",
				wp.isBrowseSourceFolderEnabled(), Is.is(true));
		
		/* error is showed when the source folder is empty */
		wp.setSourceFolder("");
		assertThat(page.getWizardPageInfoText(),
				Is.is(ERROR_SOURCE_FOLDER_NAME_IS_EMPTY));
		assertThat("Browse package button should be disabled when source folder is not set",
				wp.isBrowsePackageEnabled(), Is.is(false));
		
		/* error is showed when the source folder doesn't exist */
		wp.setSourceFolder(NON_EXISTING_PROJECT_NAME);
		assertThat(page.getWizardPageInfoText(), Is.is(" Folder '" + NON_EXISTING_PROJECT_NAME
				+ "' does not exist."));
		
		/* error disappear when the source folder is set */
		wp.setSourceFolder(PROJECT_SRC_FOLDER_PATH);
		assertThat(page.getWizardPageInfoText(), IsNot.not(Is.is(ERROR_SOURCE_FOLDER_NAME_IS_EMPTY)));
		assertThat( "Browse package button should be enabled when source folder is set",
				wp.isBrowsePackageEnabled(), Is.is(true));
	}
	
	@Test
	public void setNameTest() {
		final String ERROR_TYPE_NAME_IS_EMPTY = " Type name is empty.";
		
		/* select "Subclass of javax.ws.rs.core.Application" option */
		SubclassOfApplicationWizardPart wp = page.useSubclassOfApplication();
		wp.setSourceFolder(PROJECT_SRC_FOLDER_PATH);
		
		/* error is showed when name is empty */
		wp.setName("");
		assertThat(page.getWizardPageInfoText(), Is.is(ERROR_TYPE_NAME_IS_EMPTY));
	}
	
	@Test
	public void setPackageTest() {
		final String WARNING_USE_OF_DEFAULT_PACKAGE_IS_DISCOURAGED = " The use of the default package is discouraged.";
		final String ERROR_PACKAGE_NAME_CANNOT_START_OR_END_WITH_A_DOT = " Package name is not valid. A package name cannot start or end with a dot";
		
		/* select "Subclass of javax.ws.rs.core.Application" option */
		SubclassOfApplicationWizardPart wp = page.useSubclassOfApplication();
		
		/* set source folder and file name */
		wp.setSourceFolder(PROJECT_SRC_FOLDER_PATH);
		wp.setName(FILE_NAME);
		
		/* warning is showed when the package name is empty */
		wp.setPackage("");
		assertThat(page.getWizardPageInfoText(),
				Is.is(WARNING_USE_OF_DEFAULT_PACKAGE_IS_DISCOURAGED));
		
		/* warning disappear when the package name is set */
		wp.setPackage(PACKAGE_NAME);
		assertThat(page.getWizardPageInfoText(),
				IsNot.not(Is.is(WARNING_USE_OF_DEFAULT_PACKAGE_IS_DISCOURAGED)));
		
		/* package name cannot end with a dot */
		wp.setPackage(PACKAGE_NAME + ".");
		assertThat(page.getWizardPageInfoText(),
				Is.is(ERROR_PACKAGE_NAME_CANNOT_START_OR_END_WITH_A_DOT));
		wp.setPackage(PACKAGE_NAME);
		assertThat(page.getWizardPageInfoText(),
				IsNot.not(Is.is(ERROR_PACKAGE_NAME_CANNOT_START_OR_END_WITH_A_DOT)));
		
		/* package name cannot start with a dot */
		wp.setPackage("." + PACKAGE_NAME);
		assertThat(page.getWizardPageInfoText(),
				Is.is(ERROR_PACKAGE_NAME_CANNOT_START_OR_END_WITH_A_DOT));
	}
	
	private void assertContains(List<ProjectItem> list, String name, String errorMessage) {
		for(ProjectItem pi : list) {
			if(pi.getName().equals(name)) {
				return;
			}
		}
		fail(errorMessage);
	}
	
	private void assertThatWebXmlContainsRequiredText() {
		final String WEB_XML_TEXT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + LINE_SEPARATOR
		+ "<web-app xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
				+ "xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\" xsi:schemaLocation=\"http://xmlns.jcp.org/xml/ns/javaee "
				+ "http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd\" id=\"WebApp_ID\" version=\"3.1\">" + LINE_SEPARATOR
		+ "  <display-name>" + getWsProjectName() + "</display-name>" + LINE_SEPARATOR
		+ "  <welcome-file-list>" + LINE_SEPARATOR
		+ "    <welcome-file>index.html</welcome-file>" + LINE_SEPARATOR
		+ "    <welcome-file>index.htm</welcome-file>" + LINE_SEPARATOR
		+ "    <welcome-file>index.jsp</welcome-file>" + LINE_SEPARATOR
		+ "    <welcome-file>default.html</welcome-file>" + LINE_SEPARATOR
		+ "    <welcome-file>default.htm</welcome-file>" + LINE_SEPARATOR
		+ "    <welcome-file>default.jsp</welcome-file>" + LINE_SEPARATOR
		+ "  </welcome-file-list>" + LINE_SEPARATOR
		+ "  <servlet-mapping>" + LINE_SEPARATOR
		+ "    <servlet-name>javax.ws.rs.core.Application</servlet-name>" + LINE_SEPARATOR
		+ "    <url-pattern>/app/path</url-pattern>" + LINE_SEPARATOR
		+ "  </servlet-mapping>" + LINE_SEPARATOR
		+ "</web-app>";
		
		assertThat(getWebXmlContent(), Is.is(WEB_XML_TEXT));
	}
	
	private String getWebXmlContent() {
		String text;
		
		Project project = new ProjectExplorer().getProject(getWsProjectName());
		assertTrue("web.xml was not generated.\n" + Arrays.toString(webXmlPath) + " is missing",
				project.containsItem(webXmlPath));
		ProjectItem webXml = project.getProjectItem(webXmlPath);
		
		webXml.select();
		new ContextMenu("Open With", "Text Editor").select();;
		
		TextEditor editor = new TextEditor();
		editor.activate();
		text = editor.getText();
		editor.close();
		
		return text;
	}
	
	private Project getProject() {
		return new ProjectExplorer().getProject(getWsProjectName());
	}
}
