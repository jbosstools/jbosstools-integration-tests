package org.jboss.ide.eclipse.as.reddeer.server.editor;

import org.hamcrest.Matcher;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.workbench.impl.editor.AbstractEditor;

/**
 * Represents a special editor that contains only browser. 
 * 
 * @author Lucia Jelinkova
 *
 */
public abstract class AbstractEditorWithBrowser extends AbstractEditor {

	private InternalBrowser browser;
	
	public AbstractEditorWithBrowser(Matcher<String> title) {
		super(title);
	}
	
	public String getText(){
		activate();
		return getBrowser().getText();
	}
	
	public void refresh(){
		activate();
		getBrowser().refresh();
	}
	
	private InternalBrowser getBrowser() {
		if (browser == null){
			activate();
			browser = new InternalBrowser();
		}
		return browser;
	}
}
