package org.jboss.tools.deltaspike.ui.bot.test;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.uiforms.impl.hyperlink.DefaultHyperlink;
import org.jboss.tools.cdi.reddeer.cdi.ui.CDIValidatorPreferencePage;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.junit.Test;

/**
 * Tests if there are all deltaspike validation messages under CDI Validator 
 * 
 * @author jjankovi
 *
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
public class ValidationsInPreferenceTest extends DeltaspikeTestBase {

	@Test
	public void testAllValidationsPresence()  {
		
		new CDIValidatorPreferencePage().open();
		
		new DefaultHyperlink("Extensions").activate();
		new DefaultHyperlink("Deltaspike").activate();
		
		//bot.twistieByLabel("Extensions").toggle();
		//bot.twistieByLabel("Deltaspike").toggle();
		
		checkDeltaspikeValidationPresense();
		
		new PushButton(IDELabel.Button.CANCEL).click();
		new WaitWhile(new ShellWithTextIsActive(IDELabel.Menu.PREFERENCES));
	}
	
	private void checkDeltaspikeValidationPresense() {
		for (DeltaspikeValidations validation: DeltaspikeValidations.values()) {
			new DefaultLabel(validation.validationMessage());
		}
	}
	
}

enum DeltaspikeValidations {
	
	EXCEPTION_HANDLER("Bean is not annotated @ExceptionHandler:"), 
	INVALID_HANDER("Invalid hander method parameter type:"), 
	UMBIGUOUS_AUTHORIZER("Umbiguous authorizer for a secured method:"),
	UNRESOLVED_AUTHORIZER("Unresolved authorizer for a secured method:"),
	INVALID_AUTHORIZER("Invalid authorizer:");
	
	private String message;
	
	private DeltaspikeValidations(String message) {
		this.message = message;
	}
	
	public String validationMessage() {
		return message;
	}
	
}
