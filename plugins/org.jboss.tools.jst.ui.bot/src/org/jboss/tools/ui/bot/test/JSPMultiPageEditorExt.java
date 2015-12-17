package org.jboss.tools.ui.bot.test;

import org.eclipse.ui.IEditorReference;
import org.hamcrest.SelfDescribing;
import org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPMultiPageEditor;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
/**
 * This represents Eclipse JSPMultiPageEditor part
 * @author yzhishko, vpakan
 *
 */

public class JSPMultiPageEditorExt {

	/**
	 * Constructs an instance for the given editorReference.
	 * @param editorReference - the part reference.
	 * @param description - the description of the editor part.
	 */
	
	private JSPMultiPageEditor jspMultiPageEditor;
	
	public JSPMultiPageEditorExt(IEditorReference editorReference, SelfDescribing description) {
		if (editorReference.getPart(true) instanceof JSPMultiPageEditor) {
			jspMultiPageEditor = (JSPMultiPageEditor)editorReference.getPart(true);
		}
	}
	
	/**
	 * Constructs an instance for the given editorReference.
	 * @param editorReference - the editor reference
	 */
	
	public JSPMultiPageEditorExt(IEditorReference editorReference) {
		this(editorReference, null);
	}
	/**
	 * 
	 * @return <b>null</b> if current MultiPageEditor isn't instance of {@link JSPMultiPageEditor}, <i>else</i> <p>
	 * An object that has {@link JSPMultiPageEditor} reference type
	 * @see JSPMultiPageEditor
	 */

	public JSPMultiPageEditor getJspMultiPageEditor() {
		return jspMultiPageEditor;
	}
	
	public void selectTab(final String tabLabel) {
		new DefaultCTabItem(tabLabel).activate();
	}	
}
