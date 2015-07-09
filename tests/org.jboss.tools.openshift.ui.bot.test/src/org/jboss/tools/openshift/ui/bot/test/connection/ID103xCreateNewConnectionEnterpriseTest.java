package org.jboss.tools.openshift.ui.bot.test.connection;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsEnabled;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.Datastore;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test creating a new OpenShift Enterprise connection.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID103xCreateNewConnectionEnterpriseTest {

	@BeforeClass
	public static void storeCredentials() {
		Datastore.SERVER = System.getProperty("libra.server"); 
		Datastore.USERNAME = System.getProperty("user.name");
	}
	
	@Test
	public void testCreateConnectionToOpenShiftEnterprise() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.openConnectionShell();
		
		new DefaultShell("");

		new CheckBox(0).click();
		
		new LabeledCombo(OpenShiftLabel.TextLabels.SERVER).setText(Datastore.SERVER);
		new LabeledText(OpenShiftLabel.TextLabels.USERNAME).setText(Datastore.USERNAME);
		new LabeledText(OpenShiftLabel.TextLabels.PASSWORD).setText(
				System.getProperty("user.pwd"));
		
		new WaitUntil(new ButtonWithTextIsEnabled(new FinishButton()), TimePeriod.NORMAL);
		
		new FinishButton().click();
		
		acceptCertificate();
		
		new WaitWhile(new ShellWithTextIsAvailable(""), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		// also there is sometimes required some time
		AbstractWait.sleep(TimePeriod.getCustom(3));
		
		try {
			explorer.getConnection(Datastore.USERNAME);
			// PASS
		} catch (JFaceLayerException ex) {
			fail("Connection has not been created. It is not listed in OpenShift explorer");
		}
	}	
	
	private void acceptCertificate() {	
		try {
			new DefaultShell("Untrusted SSL Certificate");
			new PushButton("Yes").click();
		} catch (RedDeerException ex) {
			fail("Aceptance of SSL certificate failed.");
		}
	}
}
