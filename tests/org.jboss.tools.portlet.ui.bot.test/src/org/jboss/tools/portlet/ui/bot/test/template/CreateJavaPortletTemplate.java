package org.jboss.tools.portlet.ui.bot.test.template;

import static org.jboss.tools.portlet.ui.bot.test.core.CreateJavaPortletProject.PROJECT_NAME;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.baseadaptor.BaseData;
import org.eclipse.osgi.framework.internal.core.BundleHost;
import org.jboss.tools.portlet.ui.bot.entity.XMLNode;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.AbstractPortletCreationTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.JavaPortletCreationTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.JavaPortletWizardPageFillingTask;
import org.junit.Before;
import org.osgi.framework.Bundle;

/**
 * Creates a new java portlet and checks if the right files are generated.  
 * 
 * @author Lucia Jelinkova
 *
 */
public abstract class CreateJavaPortletTemplate extends CreatePortletTemplate {

	public static final String CLASS_NAME = "UITestingJavaPortlet";
	
	protected static final String PACKAGE_NAME = "org.jboss.tools.tests.ui.portlet";
	
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
		removeSlash("org.eclipse.jst.j2ee");
		removeSlash("org.eclipse.jst.j2ee.web");
		removeSlash("org.jboss.tools.portlet.ui");
	}
	
	private void removeSlash(String pluginName){
		Bundle bundle = Platform.getBundle(pluginName);
		if (bundle instanceof BundleHost){
			BundleHost bundleHost = (BundleHost) bundle;
			if (bundleHost.getBundleData() instanceof BaseData){
				BaseData baseData = (BaseData) bundleHost.getBundleData();
				if (baseData.getLocation().endsWith("/")){
					baseData.setLocation(baseData.getLocation().substring(0, baseData.getLocation().length() - 1));
				}
				System.out.println("Location of bundle is: " + bundle.getLocation());
			} else {
				System.out.println("The BundleHost data is not of type BaseData");
			}
		} else {
			System.out.println("The bundle is not of type BundleHost");
		}
	}
	
	@Override
	protected String getProjectName() {
		return PROJECT_NAME;
	}
	
	protected AbstractPortletCreationTask getCreatePortletTask() {
		JavaPortletWizardPageFillingTask task = new JavaPortletWizardPageFillingTask();
		task.setProject(PROJECT_NAME);
		task.setPackageName(PACKAGE_NAME);
		task.setClassName(CLASS_NAME);
		
		JavaPortletCreationTask wizardTask = new JavaPortletCreationTask();
		wizardTask.addWizardPage(task);
		return wizardTask;
	}
	
	@Override
	protected List<XMLNode> getExpectedXMLNodes() {
		return Arrays.asList(new XMLNode("portlet-app/portlet/portlet-class", FULL_CLASS_NAME));
	}
}
