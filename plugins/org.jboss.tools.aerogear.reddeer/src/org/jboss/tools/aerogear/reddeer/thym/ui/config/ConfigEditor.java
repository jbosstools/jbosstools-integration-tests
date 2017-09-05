/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.aerogear.reddeer.thym.ui.config;

import org.eclipse.reddeer.workbench.core.lookup.EditorPartLookup;
import org.eclipse.reddeer.workbench.matcher.EditorPartClassMatcher;
import org.eclipse.reddeer.workbench.matcher.EditorPartTitleMatcher;
import org.eclipse.reddeer.core.matcher.WithTextMatcher;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.workbench.exception.WorkbenchLayerException;
import org.eclipse.reddeer.workbench.impl.editor.AbstractEditor;
/**
 * RedDeer implementation of Hybrid Mobile Config Editor
 * 
 * @author Vlado Pakan
 * 
 */
@SuppressWarnings("restriction")
public class ConfigEditor extends AbstractEditor{
	
	private static final String PLATFORM_PROPERTIES_TAB_ITEM = "Platform Properties";
	private org.eclipse.thym.ui.config.internal.ConfigEditor editor;
	
	public ConfigEditor(){
		super(EditorPartLookup.getInstance().getEditor());
		if (!(editorPart instanceof org.eclipse.thym.ui.config.internal.ConfigEditor)){
			throw new WorkbenchLayerException("The active editor is not a ConfigEditor editor, but " + editorPart.getClass());
		}
		editor = (org.eclipse.thym.ui.config.internal.ConfigEditor) editorPart;
	}
	
	@SuppressWarnings("unchecked")
	public ConfigEditor(String title) {
		super(EditorPartLookup.getInstance().getEditor(
				new EditorPartClassMatcher(org.eclipse.thym.ui.config.internal.ConfigEditor.class), 
				new EditorPartTitleMatcher(new WithTextMatcher(title))));
		editor = (org.eclipse.thym.ui.config.internal.ConfigEditor) editorPart;
	}
	
	public PropertiesPage getPlatformPropertiesPage() {
		activate();
		new DefaultCTabItem(ConfigEditor.PLATFORM_PROPERTIES_TAB_ITEM).activate();
		return new PropertiesPage();
	}
	
}


