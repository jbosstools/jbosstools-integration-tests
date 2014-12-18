package org.jboss.ide.eclipse.as.reddeer.server.editor;

import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.matcher.RegexMatcher;
import org.jboss.reddeer.workbench.impl.editor.AbstractEditor;

/**
 * Represents a special editor that contains only browser. 
 * 
 * @author Lucia Jelinkova
 *
 */
public abstract class AbstractEditorWithBrowser extends AbstractEditor {

	protected InternalBrowser browser;
	
	public AbstractEditorWithBrowser(RegexMatcher regexMatcher) {
		super(regexMatcher);
	}

	public String getText(){
		activate();
		return getInternalBrowser().getText();
	}
	
	public void refresh(){
		getInternalBrowser().refresh();
	}

	protected InternalBrowser getInternalBrowser(){
		if (browser == null){
			browser = new InternalBrowser();
		}
		return browser;
	}
}
