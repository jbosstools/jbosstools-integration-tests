package org.jboss.tools.hibernate.reddeer.editor;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
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
			new WaitUntil(new ShellWithTextIsActive("Open Session factory"), TimePeriod.SHORT);
			new YesButton().click();
		}
		catch (WaitTimeoutExpiredException e) {
			log.warn("Open Session factory question dialog was expected");
		}		
	}
	
}
