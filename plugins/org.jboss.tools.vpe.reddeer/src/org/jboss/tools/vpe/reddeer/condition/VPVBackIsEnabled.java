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

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.tools.vpe.reddeer.preview.editor.VPVEditor;

public class VPVBackIsEnabled extends AbstractWaitCondition{
	
	private VPVEditor editor;
	
	public VPVBackIsEnabled(VPVEditor editor) {
		this.editor=editor;
	}

	@Override
	public boolean test() {
		return editor.isBackEnabled();
	}
}
