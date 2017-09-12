package org.jboss.tools.jst.reddeer.web.ui.editor.jspeditor;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.reddeer.workbench.core.lookup.EditorPartLookup;
import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.common.util.ResultRunnable;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabFolder;
import org.eclipse.reddeer.workbench.api.Editor;
import org.eclipse.reddeer.workbench.exception.WorkbenchLayerException;
import org.eclipse.reddeer.workbench.impl.editor.AbstractEditor;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.eclipse.reddeer.core.handler.CTabItemHandler;

/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc. Distributed under license by Red Hat, Inc.
 * All rights reserved. This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
public class JSPMultiPageEditor extends AbstractEditor implements Editor {

	protected org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPMultiPageEditor editor;

	public JSPMultiPageEditor() {
		super(EditorPartLookup.getInstance().getEditor());
		if (!(editorPart instanceof org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPMultiPageEditor)) {
			throw new WorkbenchLayerException(
					"The active editor is not a JSPMultiPageEditor editor, but " + editorPart.getClass());
		}
		editor = (org.jboss.tools.jst.web.ui.internal.editor.jspeditor.JSPMultiPageEditor) editorPart;
	}

	public String getSelectedTextInEditor() {
		return new TextEditor().getSelectedText();
	}

	public boolean hasSourceTab() {
		for (String label : getItemLabels()) {
			if (label.equals("Source")) {
				return true;
			}
		}
		return false;
	}

	public boolean hasPreviewTab() {
		for (String label : getItemLabels()) {
			if (label.equals("Preview")) {
				return true;
			}
		}
		return false;
	}

	public boolean hasVisualSourceTab() {
		for (String label : getItemLabels()) {
			if (label.equals("Visual/Source")) {
				return true;
			}
		}
		return false;
	}

	private String[] getItemLabels() {
		org.eclipse.swt.custom.CTabItem[] tabItem = getTabItems(new DefaultCTabFolder().getSWTWidget());
		String[] tabItemLabel = new String[tabItem.length];
		for (int i = 0; i < tabItem.length; i++) {
			tabItemLabel[i] = CTabItemHandler.getInstance().getText(tabItem[i]);
		}
		return tabItemLabel;
	}

	private CTabItem[] getTabItems(final CTabFolder tabFolder) {
		return Display.syncExec(new ResultRunnable<CTabItem[]>() {
			@Override
			public CTabItem[] run() {
				return tabFolder.getItems();
			}
		});
	}

}
