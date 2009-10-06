/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test;

import junit.framework.Assert;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.VpeEditorPart;

/**
 * Class for importing project from jar file.
 * 
 * @author sdzmitrovich
 */
public class TestUtil {

	/**
     * Gets visual page editor controller.
     * 
     * @param part the part
     * 
     * @return {@link VpeController}
     */
    public static VpeController getVpeController(JSPMultiPageEditor part) {

        VpeEditorPart visualEditor = (VpeEditorPart) part.getVisualEditor();
        return visualEditor.getController();
    }

    /**
     * Fail.
     * 
     * @param t the t
     */
    public static void fail(Throwable t){
        Assert.fail("Test case was fail "+t.getMessage()+":"+t);
    }
}
