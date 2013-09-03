package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.condition.TableHasRows;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.swt.wait.WaitUntil;

/**
 * HTTP binding.
 * 
 * @author apodhrad
 * 
 */
public abstract class BindingWizard<T> extends WizardDialog {

	private String title;

	public BindingWizard(String title) {
		super();
	}

	@SuppressWarnings("unchecked")
	public T activate() {
		Bot.get().shell(title).activate();
		return ((T) this);
	}

	@SuppressWarnings("unchecked")
	public T setMessageComposer(String composer) {
		new PushButton("Browse...").click();
		new DefaultText().setText(composer);
		new WaitUntil(new TableHasRows(new DefaultTable()));
		new PushButton("OK").click();
		return (T) this;
	}

}
