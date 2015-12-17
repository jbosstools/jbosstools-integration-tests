package org.jboss.tools.ui.bot.test;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.internal.PartSite;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;

/**
 * SWTWorkbenchBot is a {@link SWTWorkbenchBot} with capabilities for 
 * testing Eclipse workbench item like MultiPageEditor
 * @author yzhishko
 *
 */

public class SWTJBTBot extends SWTWorkbenchBot{
	/**
	 * 
	 * @param matcher
	 * @return
	 */
	
	public JSPMultiPageEditorExt multiPageEditor(IEditorPart editorPart) {
		return new JSPMultiPageEditorExt((IEditorReference)((PartSite) editorPart.getSite()).getPartReference());
	}
	
	/**
	 * Get editor by title
	 * @param fileName - the name of editor
	 * @return - object with {@link JSPMultiPageEditorExt} reference type and current name
	 */
	
	public JSPMultiPageEditorExt multiPageEditorByTitle (String fileName) {
		return multiPageEditor(new TextEditor(fileName).getEditorPart());
	}
	
}
