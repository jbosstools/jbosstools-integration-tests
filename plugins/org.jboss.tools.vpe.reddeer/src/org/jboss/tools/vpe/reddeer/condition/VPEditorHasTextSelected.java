/******************************************************************************* 
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.reddeer.condition;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.tools.vpe.reddeer.preview.editor.VPVEditor;

public class VPEditorHasTextSelected extends AbstractWaitCondition{
	
	private VPVEditor editor;
	private String text;
	
	public VPEditorHasTextSelected(VPVEditor editor) {
		this.editor = editor;
	}
	
	public VPEditorHasTextSelected(VPVEditor editor, String text) {
		this.editor = editor;
		this.text = text;
	}

	@Override
	public boolean test() {
		if(text != null){
			return text.equals(editor.getSelectedTextInBrowser());
		}
		return (editor.getSelectedTextInBrowser() != null && !editor.getSelectedTextInBrowser().isEmpty());
	}

	@Override
	public String description() {
		if(text != null){
			return "VPE editor has '"+text+"' selected. Currently selected text is '"+
					editor.getSelectedTextInBrowser()+"'";
		}
		return "VPE editor has text selected";
	}

	@Override
	public String errorMessageUntil() {
		if(text != null){
			return "'"+editor.getSelectedTextInBrowser()+"' was selected but '"+text+"' was expected";
		}
		return "No text was selected in VPE editor";
	}

}
