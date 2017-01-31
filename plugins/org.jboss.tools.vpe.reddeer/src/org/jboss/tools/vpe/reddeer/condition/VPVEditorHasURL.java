/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.reddeer.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.tools.vpe.reddeer.preview.editor.VPVEditor;

public class VPVEditorHasURL extends AbstractWaitCondition {

	private VPVEditor editor;
	private String url;

	public VPVEditorHasURL(VPVEditor editor, String url) {
		this.editor = editor;
		this.url = url;
	}

	@Override
	public boolean test() {
		return editor.getBrowserURL().matches(url);
	}

}