 /*******************************************************************************
  * Copyright (c) 2016 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.deltaspike.ui.bot.test.condition;


import java.util.List;

import org.eclipse.reddeer.common.condition.WaitCondition;
import org.eclipse.reddeer.workbench.impl.editor.Marker;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;

public class ClassHasErrorMarker implements WaitCondition{
	
	private String className;
	private List<Marker> markers;
	
	public ClassHasErrorMarker(String className) {
		this.className = className;
	}

	@Override
	public boolean test() {
		TextEditor ed = new TextEditor(className+".java");
		
		if (ed.getMarkers().size() > 0) {
			markers = ed.getMarkers();
			return true;
		}
		return false;
	}

	@Override
	public String description() {
		return "class "+className+" has errors";
	}

	@Override
	public String errorMessageWhile() {
		return "class "+className+" has errors"; 
	}

	@Override
	public String errorMessageUntil() {
		return "class "+className+" has no errors"; 
	}

	@Override
	public List<Marker> getResult() {
		return markers;
	}

}
