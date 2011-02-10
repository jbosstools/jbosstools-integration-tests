/*******************************************************************************
 * Copyright (c) 2007-2011 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor.formattingbar;

import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.jboss.tools.ui.bot.test.WidgetVariables;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;

/**
 * Class for testing VPE Formatting Bar
 * @author dmaliarevich
 */
public class FormattingBarTest extends VPEEditorTestCase {
	
	private static final String DIV_TEXT = "<div> div with text </div>"; //$NON-NLS-1$
	private static final String STYLE_TEXT = " style = \"color: red; font-size: 14px;\""; //$NON-NLS-1$
	private static final String STYLE_TEXT2 = " style = \"color: red;font-size: 14px;\""; //$NON-NLS-1$
	private static final String STYLE_TEXT3 = " style = \"color: red;font-size:20px;\""; //$NON-NLS-1$
	
	private SWTBotExt botExt = null;
	private SWTBotEditorExt jspEditor;
	
	public FormattingBarTest() {
		super();
		botExt = new SWTBotExt();
	}

}
