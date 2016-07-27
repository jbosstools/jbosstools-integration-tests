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


import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;

public class ClassHasErrorMarker implements WaitCondition{
	
	private String className;
	
	public ClassHasErrorMarker(String className) {
		this.className = className;
	}

	@Override
	public boolean test() {
		TextEditor ed = new TextEditor(className+".java");
		return ed.getMarkers().size() > 0;
	}

	@Override
	public String description() {
		return "class "+className+" has errors";
	}

	@Override
	public String errorMessage() {
		return "class "+className+" has errors"; 
	}

}
