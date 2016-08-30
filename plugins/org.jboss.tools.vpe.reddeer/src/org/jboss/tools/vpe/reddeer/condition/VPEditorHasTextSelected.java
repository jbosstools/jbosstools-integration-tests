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

import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.tools.vpe.reddeer.preview.editor.VPVEditor;

public class VPEditorHasTextSelected implements WaitCondition{
	
	private VPVEditor editor;
	
	public VPEditorHasTextSelected(VPVEditor editor) {
		this.editor = editor;
	}

	@Override
	public boolean test() {
		return !editor.getSelectedTextInBrowser().isEmpty();
	}

	@Override
	public String description() {
		return "VPE editor has text selected";
	}

	@Override
	public String errorMessage() {
		return "No text was selected in VPE editor";
	}

}
