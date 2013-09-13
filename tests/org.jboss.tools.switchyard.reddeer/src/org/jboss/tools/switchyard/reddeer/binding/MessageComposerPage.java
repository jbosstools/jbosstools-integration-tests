package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.condition.TableHasRows;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.wait.WaitUntil;

/**
 * 
 * @author apodhrad
 *
 */
public class MessageComposerPage extends WizardPage {

	public MessageComposerPage setMessageComposer(String composer) {
		new PushButton("Browse...").click();
		new DefaultText().setText(composer);
		new WaitUntil(new TableHasRows(new DefaultTable()));
		new PushButton("OK").click();
		return this;
	}

}
