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
package org.jboss.tools.vpe.reddeer.view;

import org.eclipse.reddeer.gef.view.PaletteView;
import org.eclipse.reddeer.swt.impl.toolbar.DefaultToolItem;

public class JBTPaletteView extends PaletteView {
	
	public void clickImportToolItem () {
		activate();
		new DefaultToolItem("Import").click();
	}
	
	public void clickPaletteEditorToolItem () {
		activate();
		new DefaultToolItem("Palette Editor").click();
	}
	
	public void clickShowHideToolItem () {
		activate();
		new DefaultToolItem("Show/Hide").click();
	}

}