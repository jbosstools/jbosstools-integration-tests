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


import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.reddeer.common.condition.WaitCondition;
import org.eclipse.reddeer.workbench.impl.editor.Marker;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;

public class ClassHasErrorOrWarningMarker implements WaitCondition{
	
	protected static final Set<String> ALLOWED_MARKERS_TYPES = new HashSet<String>(Arrays.asList("org.eclipse.ui.workbench.texteditor.warning", "org.eclipse.ui.workbench.texteditor.error"));
	
	private String className;
	private List<Marker> markers;
	
	public ClassHasErrorOrWarningMarker(String className) {
		this.className = className;
	}

	@Override
	public boolean test() {
		TextEditor ed = new TextEditor(className+".java");
		
		markers = filterErrorsAndWarnings(ed.getMarkers());
		
		if (markers.size() > 0) {
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
	
	/**
	 * Remove uninteresting markers like "org.eclipse.jdt.ui.overrideIndicator" for example.
	 * @param markers List of the markers which will be cleaned.
	 */
	private List<Marker> filterErrorsAndWarnings(List<Marker> markers) {
		Iterator<Marker> markersIterator = markers.iterator();
		while(markersIterator.hasNext()) {
			if (!ALLOWED_MARKERS_TYPES.contains(markersIterator.next().getType())) {
				markersIterator.remove();
			}
		}
		return markers;
	}

}
