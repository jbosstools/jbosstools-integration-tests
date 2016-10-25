package org.jboss.tools.hibernate.reddeer.editor;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;

/**
 * HQL Editor RedDeer implementation
 * @author Jiri Peterka
 *
 */
public class HQLEditor extends TextEditor {

	private Logger log = Logger.getLogger(HQLEditor.class);
	
	/**
	 * Sets focus to HQL Editor with given title 
	 * @param title
	 */
	public HQLEditor(String title) {
		super(title);
	}

	/**
	 * Executes HQL query
	 */
	public void runHQLQuery() {
		new DefaultToolItem("Run HQL").click();
		
		try {
			Shell s= new DefaultShell("Open Session factory");
			new YesButton().click();
			new WaitWhile(new ShellIsAvailable(s));
		}
		catch (WaitTimeoutExpiredException e) {
			log.warn("Open Session factory question dialog was expected");
		}		
	}
	
}
