package org.jboss.tools.openshift.ui.bot.test.connection;

import static org.junit.Assert.assertFalse;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView.ServerType;
import org.junit.Test;

/**
 * Test invalid credentials usage in new connection dialog.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID104InvalidCredentialsValidationTest {
	
	@Test
	public void testInvalidCredentials() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.openConnectionShell();
		
		new DefaultShell("");
		
		new CheckBox(0).click();
		
		new LabeledCombo(OpenShiftLabel.TextLabels.SERVER_TYPE).setSelection(ServerType.OPENSHIFT_2.toString());
		
		incorrectServer();
		verify("server");
		
		incorrectUsername();
		verify("username");
		
		incorrectPassword();
		verify("password");
		
		new CancelButton().click();
	}
	
	private void incorrectServer() {
		new LabeledCombo(OpenShiftLabel.TextLabels.SERVER).setText("incorrect.server.url");
		new LabeledText(OpenShiftLabel.TextLabels.USERNAME).setText(Datastore.USERNAME);
		new LabeledText(OpenShiftLabel.TextLabels.PASSWORD).setText(
				System.getProperty("user.pwd"));		
	}

	private void incorrectUsername() {
		new LabeledCombo(OpenShiftLabel.TextLabels.SERVER).setText(Datastore.SERVER);
		new LabeledText(OpenShiftLabel.TextLabels.USERNAME).setText("nonexisting");
		new LabeledText(OpenShiftLabel.TextLabels.PASSWORD).setText(
				System.getProperty("user.pwd"));	
	}
	
	private void incorrectPassword() {
		new LabeledCombo(OpenShiftLabel.TextLabels.SERVER).setText(Datastore.SERVER);
		new LabeledText(OpenShiftLabel.TextLabels.USERNAME).setText(Datastore.USERNAME);
		new LabeledText(OpenShiftLabel.TextLabels.PASSWORD).setText("incorrectpwd");
	}
	
	private void verify(String credential) {
		new WaitUntil(new ButtonWithTextIsActive(new FinishButton()), TimePeriod.NORMAL);
		
		new FinishButton().click();
		
		new WaitUntil(new ButtonWithTextIsActive(new CancelButton()), TimePeriod.LONG);

		assertFalse("Finish button should not be enabled after validation incorrect "
				+ credential + ".", new FinishButton().isEnabled());
	}
}
