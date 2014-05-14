/*******************************************************************************
 * Copyright (c) 2010-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.reddeer.condition;

import java.util.Iterator;

import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.jboss.tools.common.util.IEditorWrapper;

public class AsYouTypeValidationHelper {
	
	public ITextEditor getActiveTextEditor() {
		ITextEditor textEditor = null;
		if(new ReferencedEditor().getReference() instanceof IEditorPart){
			IEditorPart editorPart = (IEditorPart)new ReferencedEditor().getReference();
			if (editorPart instanceof IEditorWrapper) {
				editorPart = ((IEditorWrapper) editorPart).getEditor();
			}
			if (editorPart instanceof ITextEditor) {
				textEditor = (ITextEditor) editorPart;
			} else {
				textEditor = editorPart == null ? null : (ITextEditor)editorPart.getAdapter(ITextEditor.class);
			}	
			return textEditor;
		}
		return null;
	}
	
	public IAnnotationModel getAnnotationModel() {
		return getAnnotationModel(getActiveTextEditor());
	}
	
	public IAnnotationModel getAnnotationModel(ITextEditor textEditor) {		
		if (textEditor == null) {
			return null;
		}
		final IDocumentProvider documentProvider = textEditor.getDocumentProvider();
		if (documentProvider == null) {
			return null;
		}
		return documentProvider.getAnnotationModel(textEditor.getEditorInput());
	}
	
	public boolean markerExists() {
		return markerExists(null, null);
	}
	
	public boolean markerExists(IAnnotationModel annotationModel) {
		return markerExists(annotationModel, null, null);
	}
	
	public boolean markerExists(String type, String message) {
		return markerExists(getAnnotationModel(), type, message);
	}
	
	public boolean markerExists(IAnnotationModel annotationModel, String type, String message) {
		Iterator<?> it = annotationModel.getAnnotationIterator();
		boolean found = false;
		while (it.hasNext() && !found) {
			Object o = it.next();

			if (!(o instanceof Annotation))
				continue;
			
			Annotation annotation = (Annotation) o;
			if (annotation.getText().matches(message)) {
				found = true;
			}
		}
		return found;
	}
	
}
