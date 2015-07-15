package org.jboss.tools.openshift.ui.bot.test.connection.v3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.widgets.Control;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.core.lookup.ShellLookup;
import org.jboss.reddeer.core.matcher.AndMatcher;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.core.reference.ReferencedComposite;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.link.AnchorLink;
import org.jboss.reddeer.swt.impl.shell.AbstractShell;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class OpenShiftV3ConnectionDialogHandlingTest {

	@Before
	public void openConnectionShell() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.openConnectionShell();
		
		new DefaultShell("");
	}
	
	@Test
	public void testSwitchServerTypes() {
		new LabeledCombo(OpenShiftLabel.TextLabels.SERVER_TYPE).setSelection(
				OpenShiftExplorerView.ServerType.OPENSHIFT_3.toString());
		
		try {
			new LabeledCombo(OpenShiftLabel.TextLabels.PROTOCOL);
			// pass
		} catch (RedDeerException ex) {
			fail("There is no combo for selection authentication protocol for OpenShift v3 server type.");
		}
		
		new LabeledCombo(OpenShiftLabel.TextLabels.SERVER_TYPE).setSelection(
				OpenShiftExplorerView.ServerType.OPENSHIFT_2.toString());
		
		try {
			new LabeledCombo(OpenShiftLabel.TextLabels.PROTOCOL);
			fail("There should not be combo for selection authentication protocol for OpenShift v2 server type.");
		} catch (RedDeerException ex) {
			// pass
		}
	}
	
	@Test
	public void testSwitchingBasicAndOAuthProtocol() {
		new LabeledCombo(OpenShiftLabel.TextLabels.SERVER_TYPE).setSelection(
				OpenShiftExplorerView.ServerType.OPENSHIFT_3.toString());
		
		// Verify that required fields are present after switching protocol type
		switchToOAuth();
		switchToBasic();
		switchToOAuth();
		switchToBasic();
	}
	
	@Test
	public void testTokenCachingAfterSwitchingAuthProtocol() {
		new LabeledCombo(OpenShiftLabel.TextLabels.SERVER_TYPE).setSelection(
				OpenShiftExplorerView.ServerType.OPENSHIFT_3.toString());
		
		String token = "r4nd0mT0k3n";
		
		switchToOAuth();
		new LabeledText(OpenShiftLabel.TextLabels.TOKEN).setText(token);
		
		switchToBasic();
		switchToOAuth();
		
		assertTrue("Token text field does not cache token after switching authentication protocol.", 
				token.equals(new LabeledText(OpenShiftLabel.TextLabels.TOKEN).getText()));
	}
	
	@Test
	public void testUserAndPassCachingAfterSwitchingAuthProtocol() {
		new LabeledCombo(OpenShiftLabel.TextLabels.SERVER_TYPE).setSelection(
				OpenShiftExplorerView.ServerType.OPENSHIFT_3.toString());
		
		String username = "randomuser";
		
		switchToBasic();
		new LabeledText(OpenShiftLabel.TextLabels.USERNAME).setText(username);
		new LabeledText(OpenShiftLabel.TextLabels.PASSWORD).setText("openshiftv3");
		
		switchToOAuth();
		switchToBasic();
		
		assertTrue("Username text field does not cache user name after switching authentication protocol.",
				username.equals(new LabeledText(OpenShiftLabel.TextLabels.USERNAME).getText()));
		assertFalse("Password text field does not cache password after switching authentication protocol.",
				new LabeledText(OpenShiftLabel.TextLabels.PASSWORD).getText().isEmpty());
	}
	
	private void switchToOAuth() {
		new LabeledCombo(OpenShiftLabel.TextLabels.PROTOCOL).setSelection(
				OpenShiftExplorerView.AuthenticationMethod.OAUTH.toString());
		
		try {
			new LabeledText(OpenShiftLabel.TextLabels.TOKEN);
			// pass
		} catch (RedDeerException ex) {
			fail("Text field for token is not present for OAuth authentication protocol.");
		}
	}
	
	private void switchToBasic() {
		new LabeledCombo(OpenShiftLabel.TextLabels.PROTOCOL).setSelection(
				OpenShiftExplorerView.AuthenticationMethod.BASIC.toString());
		
		try {
			new LabeledText(OpenShiftLabel.TextLabels.USERNAME);
			new LabeledText(OpenShiftLabel.TextLabels.PASSWORD);
			// pass
		} catch (RedDeerException ex) {
			fail("Text field for username and/or password is not present for Basic authentication protocol.");
		}
	}
	
	@Test
	public void testLinkToRetrieveToken() {
		new LabeledCombo(OpenShiftLabel.TextLabels.SERVER_TYPE).setSelection(
				OpenShiftExplorerView.ServerType.OPENSHIFT_3.toString());

		new LabeledCombo(OpenShiftLabel.TextLabels.PROTOCOL).setSelection(
				OpenShiftExplorerView.AuthenticationMethod.OAUTH.toString());
		
		try {
			new AnchorLink(OpenShiftLabel.TextLabels.RETRIEVAL_LINK);
			// pass
		} catch (RedDeerException ex) {
			fail("Link to retrieve token for a connection is not available.");
		}
		
		new LabeledCombo(OpenShiftLabel.TextLabels.SERVER).setText(
				"https://nonexisting.server.com");
		new AnchorLink(OpenShiftLabel.TextLabels.RETRIEVAL_LINK).click();
		
		// There can be problem occured dialog, or it is only in log
		try {
			new DefaultShell("Problem Occurred");
			new OkButton().click();
			new WaitWhile(new ShellWithTextIsAvailable("Problem Occurred"));
			new DefaultShell("");
			//pass
		} catch (RedDeerException ex) {
			// pass
		}
		
		try {
			new InternalBrowser();
			fail("Browser with token should not opened for nonexisting server.");
		} catch (RedDeerException ex) {
			// pass
		}
		
		new LabeledCombo(OpenShiftLabel.TextLabels.SERVER).setText(
				"https://console.stg.openshift.com");
		new AnchorLink(OpenShiftLabel.TextLabels.RETRIEVAL_LINK).click();
		
		try {
			new ShellWithButton("", "Close");
			new PushButton("Close").click();
			AbstractWait.sleep(TimePeriod.SHORT);
		} catch (RedDeerException ex) {
			fail("Browser with token was not opened.");
		}
	}
	
	private class ShellWithButton extends AbstractShell {

		public ShellWithButton(String title, String buttonLabel) {
			super(lookForShellWithButton(title, buttonLabel));
			setFocus();
		}

	}
	
	private org.eclipse.swt.widgets.Shell lookForShellWithButton(final String title, final String buttonLabel) {
		Matcher<String> titleMatcher = new WithTextMatcher(title);
		Matcher<String> buttonMatcher = new BaseMatcher<String>() {
			@Override
			public boolean matches(Object obj) {
				if (obj instanceof Control) {
					final Control control = (Control) obj;
					ReferencedComposite ref = new ReferencedComposite() {
						@Override
						public Control getControl() {
							return control;
						}
					};
					try {
						new PushButton(ref, buttonLabel);
						return true;
					} catch (CoreLayerException e) {
						// ok, this control doesn't contain the button
					}
				}
				return false;
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("containing button '" + buttonLabel + "'");
			}
		};
		@SuppressWarnings("unchecked")
		Matcher<String> matcher = new AndMatcher(titleMatcher, buttonMatcher);
		return ShellLookup.getInstance().getShell(matcher);
	}
	
	@Test
	@Ignore
	public void testDefaultServer() {
		// No Default Server yet
	}
	
	@After
	public void closeNewConnectionShell() {
		new CancelButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(""));
	}
}
