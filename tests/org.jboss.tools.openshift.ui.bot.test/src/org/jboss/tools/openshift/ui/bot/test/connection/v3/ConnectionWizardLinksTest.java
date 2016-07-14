package org.jboss.tools.openshift.ui.bot.test.connection.v3;

import static org.junit.Assert.assertEquals;

import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.swt.api.Browser;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS3;
import org.jboss.tools.openshift.reddeer.utils.EmulatedLinkStyledText;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.junit.Test;

public class ConnectionWizardLinksTest {

	@Test
	public void gettingStartedLinkTest() {
		openConnectionWizardAndSelectOS3();

		EmulatedLinkStyledText linkText = new EmulatedLinkStyledText("Enter a token or retrieve a new one.");
		linkText.click(linkText.getPositionOfText("retrieve") + 3);

		new WaitUntil(new ShellWithTextIsActive(""));
		final InternalBrowser internalBrowser = new InternalBrowser();

		login(internalBrowser);

		new WaitUntil(new LoginPageIsLoaded(() -> internalBrowser.getText().contains("Your API token is")));

		String token = getTokenFromBrowser(internalBrowser);
		// close browser shell
		new PushButton("Close").click();

		String tokenText = new LabeledText(OpenShiftLabel.TextLabels.TOKEN).getText();

		assertEquals(token, tokenText);

		new CancelButton().click();
	}

	private void login(final InternalBrowser internalBrowser) {
		new WaitUntil(new LoginPageIsLoaded(() -> testGitHubLoginButton(internalBrowser)));

		internalBrowser.execute("document.getElementsByClassName(\"login-github\")[0].click()");

		new WaitUntil(new LoginPageIsLoaded(() -> testGithubLoginPage(internalBrowser)));

		fillAndSendGithubCredentials(internalBrowser);
	}

	private String getTokenFromBrowser(final InternalBrowser internalBrowser) {
		return (String) internalBrowser.evaluate("return document.getElementsByTagName(\"code\")[0].innerHTML");
	}

	private void fillAndSendGithubCredentials(final InternalBrowser internalBrowser) {
		internalBrowser.execute(
				String.format("document.getElementById(\"login_field\").value=\"%s\"", DatastoreOS3.GIT_USERNAME));
		internalBrowser.execute(
				String.format("document.getElementById(\"password\").value=\"%s\"", DatastoreOS3.GIT_PASSWORD));
		internalBrowser.execute("document.getElementById(\"password\").parentElement.parentElement.submit()");
	}

	private void openConnectionWizardAndSelectOS3() {
		OpenShiftExplorerView openShiftExplorerView = new OpenShiftExplorerView();
		openShiftExplorerView.open();
		openShiftExplorerView.openConnectionShell();
		new DefaultShell(OpenShiftLabel.Shell.NEW_CONNECTION);
		new LabeledCombo(OpenShiftLabel.TextLabels.SERVER_TYPE)
				.setSelection(OpenShiftExplorerView.ServerType.OPENSHIFT_3.toString());
		new LabeledCombo(OpenShiftLabel.TextLabels.SERVER).setText(DatastoreOS3.PUBLIC_OS3_SERVER);
	}

	private boolean testGithubLoginPage(Browser browser) {
		Object evaluate;
		try {
			evaluate = browser.evaluate("return document.getElementById(\"login_field\").name");
		} catch (CoreLayerException e) {
			return false;
		}
		if (evaluate instanceof String) {
			String returnString = (String) evaluate;
			System.out.println("Output: " + returnString);
			return returnString.equals("login");
		}
		return false;
	}

	private boolean testGitHubLoginButton(Browser browser) {
		Object evaluate;
		try {
			evaluate = browser.evaluate("return document.getElementsByClassName(\"login-github\")[0].innerHTML");
		} catch (CoreLayerException e) {
			return false;
		}
		if (evaluate instanceof String) {
			String returnString = (String) evaluate;
			return returnString.contains("Login with github");
		}
		return false;
	}

	private class LoginPageIsLoaded implements WaitCondition {

		private TestCondition myTest;

		public LoginPageIsLoaded(TestCondition myTest) {
			this.myTest = myTest;
		}

		@Override
		public boolean test() {
			return myTest.test();
		}

		@Override
		public String description() {
			return "browser is loaded";
		}

		@Override
		public String errorMessage() {
			return "browser is not fully loaded";
		}

	}

	private interface TestCondition {
		public boolean test();
	}
}
