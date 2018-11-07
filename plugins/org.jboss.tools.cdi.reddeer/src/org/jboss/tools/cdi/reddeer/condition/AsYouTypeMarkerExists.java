/*******************************************************************************
 * Copyright (c) 2010-2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.reddeer.condition;

import java.util.List;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.workbench.api.Editor;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.eclipse.reddeer.workbench.impl.editor.Marker;

public class AsYouTypeMarkerExists extends AbstractWaitCondition{
	
	private String message;
	private String editor;
	
	public AsYouTypeMarkerExists(String editor, String message) {
		this.message = message;
		this.editor = editor;
	}

	@Override
	public boolean test() {
		Editor e = new DefaultEditor(editor);
		List<Marker> markers = e.getMarkers();
		boolean markerFound = false;
		for(Marker m: markers){
			if(m.getText().contains(message)){
				markerFound = true;
				break;
			}
		}
		return markerFound;
	}

	@Override
	public String description() {
		return "No as-you-type marker exists in '" + 
				editor + "'";
	}

}
