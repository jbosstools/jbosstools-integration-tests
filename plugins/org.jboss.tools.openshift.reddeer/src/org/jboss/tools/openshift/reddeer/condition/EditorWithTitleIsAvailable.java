package org.jboss.tools.openshift.reddeer.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;

/**
 * Waits for an editor with specific title to be available.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class EditorWithTitleIsAvailable extends AbstractWaitCondition {

	private String title;
	
	/**
	 * Creates a new Editor with title is available wait condition 
	 * to wait for an editor with specified title.
	 * 
	 * @param title title of editor
	 */
	public EditorWithTitleIsAvailable(String title) {
		this.title = title;
	}
	
	@Override
	public boolean test() {
		try {
			new DefaultEditor(title);
			return true;
		} catch (RedDeerException ex) {
			return false;
		}
	}	
}
