package org.jboss.tools.portlet.ui.bot.test.template;

import static org.jboss.tools.portlet.ui.bot.test.core.CreateJavaPortletProject.PROJECT_NAME;

import java.util.Arrays;
import java.util.List;

import org.jboss.tools.portlet.ui.bot.entity.XMLNode;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.NewJavaPortletDialog;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.NewJavaPortletWizardPage;
import org.junit.Before;

/**
 * Creates a new java portlet and checks if the right files are generated.  
 * 
 * @author Lucia Jelinkova
 *
 */
@SuppressWarnings("restriction")
public abstract class CreateJavaPortletTemplate extends CreatePortletTemplate {

	public static final String CLASS_NAME = "UITestingJavaPortlet";
	
	public static final String PACKAGE_NAME = "org.jboss.tools.tests.ui.portlet";
	
	protected static final String SOURCE_FILE_NAME = "src";
	
	protected static final String CLASS_FILE = SOURCE_FILE_NAME + "/" + PACKAGE_NAME + "/" + CLASS_NAME + ".java";
	
	protected static final String FULL_CLASS_NAME = PACKAGE_NAME + "." + CLASS_NAME;
	
	/**
	 * An ugly fix of Eclipse issue:
	 * ID: Bug 368436 
	 * URL: https://bugs.eclipse.org/bugs/show_bug.cgi?id=368436
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected String getProjectName() {
		return PROJECT_NAME;
	}
	
	@Override
	protected void createPortlet() {
		NewJavaPortletDialog dialog = new NewJavaPortletDialog();
		dialog.open();
		NewJavaPortletWizardPage page = (NewJavaPortletWizardPage) dialog.getFirstPage();
		page.setClassName(CLASS_NAME);
		page.setPackage(PACKAGE_NAME);
		page.setProject(PROJECT_NAME);
		dialog.finish();
	}
	
	@Override
	protected List<XMLNode> getExpectedXMLNodes() {
		return Arrays.asList(new XMLNode("portlet-app/portlet/portlet-class", FULL_CLASS_NAME));
	}
}
