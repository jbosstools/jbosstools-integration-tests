package org.jboss.tools.batch.reddeer.editor.jobxml;

import org.eclipse.ui.texteditor.ITextEditor;
import org.jboss.reddeer.eclipse.wst.xml.ui.tabletree.XMLSourcePage;

/**
 * Represents the source tab of job xml editor. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class JobXMLEditorSourcePage extends XMLSourcePage {

	public JobXMLEditorSourcePage(ITextEditor editor) {
		super(editor);
	}
}
