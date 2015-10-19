package org.jboss.tools.openshift.ui.bot.test.application.v3.basic;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.condition.TableContainsItem;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.BackButton;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.wizard.v3.NewOpenShift3ApplicationWizard;
import org.jboss.tools.openshift.ui.bot.test.util.DatastoreOS3;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TemplateParametersTest {

	public static final String APPLICATION_HOSTNAME = "APPLICATION_HOSTNAME";
	public static final String APPLICATION_NAME = "APPLICATION_NAME";
	public static final String GENERIC_SECRET = "GENERIC_TRIGGER_SECRET";
	public static final String GITHUB_SECRET = "GITHUB_TRIGGER_SECRET";
	public static final String GIT_URI = "GIT_URI";
	
	public static final String APPLICATION_HOSTNAME_VALUE = "Custom hostname for service routes.  "
			+ "Leave blank for default hostname, e.g.: <application-name>.<project>."
			+ "<default-domain-suffix>";
	public static final String APPLICATION_NAME_VALUE = "The name for the application.";
	public static final String GIT_URI_VALUE = "https://github.com/jboss-developer/jboss-eap-quickstarts";
	public static final String PERSONAL_GIT_REPO_URI = "https://github.com/mlabuda/jboss-eap-quickstarts";
	public static final String SECRET_VALUE = "(generated)";
	
	@Before
	public void openTemplateParametersWizardPage() {
		new NewOpenShift3ApplicationWizard(DatastoreOS3.SERVER, DatastoreOS3.USERNAME,
				DatastoreOS3.PROJECT1_DISPLAYED_NAME).openWizardFromExplorer();
		new DefaultTree().selectItems(new DefaultTreeItem(OpenShiftLabel.Others.EAP_TEMPLATE));
		
		new WaitUntil(new WidgetIsEnabled(new NextButton()), TimePeriod.NORMAL);
		
		new NextButton().click();
		
		new WaitUntil(new WidgetIsEnabled(new BackButton()), TimePeriod.LONG);
	}
	
	@Test
	public void testTemplateParameterDetails() {
		verifyParameter(APPLICATION_HOSTNAME, APPLICATION_HOSTNAME_VALUE);
		verifyParameter(APPLICATION_NAME, APPLICATION_NAME_VALUE);
		verifyParameter(APPLICATION_HOSTNAME, APPLICATION_HOSTNAME_VALUE);
	}
	
	@Test
	public void testTemplateParametersDefaultValues() {
		assertTrue("Value for " + APPLICATION_HOSTNAME + " parameter should be empty.",
				new DefaultTable().getItem(APPLICATION_HOSTNAME).getText(1).equals(""));
		assertTrue("Value for " + GENERIC_SECRET + " parameter should be " + SECRET_VALUE,
				new DefaultTable().getItem(GENERIC_SECRET).getText(1).equals(SECRET_VALUE));
		assertTrue("Value for " + GITHUB_SECRET + " parameter should be " + SECRET_VALUE,
				new DefaultTable().getItem(GITHUB_SECRET).getText(1).equals(SECRET_VALUE));
		assertTrue("Value for " + GIT_URI + " parameters should be " + GIT_URI_VALUE,
				new DefaultTable().getItem(GIT_URI).getText(1).equals(GIT_URI_VALUE));
	}
	
	@Test
	public void testModifyTemplateParameter() {
		new DefaultTable().getItem(GIT_URI).select();
		new PushButton(OpenShiftLabel.Button.EDIT).click();
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_TEMPLATE_PARAMETER);
		new DefaultText().setText(PERSONAL_GIT_REPO_URI);
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EDIT_TEMPLATE_PARAMETER));
		
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);
		assertTrue("New value of git repo URI has not been modified successfully.",
				new DefaultTable().getItem(GIT_URI).getText(1).equals(PERSONAL_GIT_REPO_URI));
		
		new PushButton(OpenShiftLabel.Button.RESET).click();

		try {
			new WaitUntil(new TableContainsItem(new DefaultTable(), GIT_URI_VALUE, 1), TimePeriod.NORMAL);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Value for git repo URI has not been reset.");
		}
	}
	
	@After
	public void closeNewApplicationWizard() {
		new CancelButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning());
	}
	
	private void verifyParameter(String parameterName, String parameterValue) {
		new DefaultTable().select(parameterName);
		try {
			new DefaultStyledText(parameterValue);
		} catch (RedDeerException ex) {
			fail("Details for " + parameterName + " have not been shown properly.");
		}
	}
}
