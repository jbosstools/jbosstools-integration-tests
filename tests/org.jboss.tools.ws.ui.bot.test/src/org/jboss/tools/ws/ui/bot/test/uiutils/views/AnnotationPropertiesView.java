/*******************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.uiutils.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.view.ViewBase;

/**
 * 
 * @author jjankovi
 * 
 */
public class AnnotationPropertiesView extends ViewBase {

	public AnnotationPropertiesView() {
		viewObject = ActionItem.View.JAXWSAnnotationProperties.LABEL;
	}

	public List<SWTBotTreeItem> getAllAnnotations() {

		List<SWTBotTreeItem> annotations = new ArrayList<SWTBotTreeItem>();
		SWTBotTree annotationViewTree = null;

		annotationViewTree = show().bot().tree();
		for (SWTBotTreeItem ti : annotationViewTree.getAllItems()) {
			annotations.add(ti);
		}

		return annotations;
	}
	
	public SWTBotTreeItem getAnnotation(String annotationName) {
		
		for (SWTBotTreeItem annotation : getAllAnnotations()) {
			if (annotation.getText().equals(annotationName)) {
				return annotation;
			}
		}
		return null;
	}

	public List<SWTBotTreeItem> getAnnotationValues(
			SWTBotTreeItem annotation) {

		List<SWTBotTreeItem> annotationValues = new ArrayList<SWTBotTreeItem>();
		
		annotation.expand();
		
		for (SWTBotTreeItem ti : annotation.getItems()) {
			annotationValues.add(ti);
		}
		return annotationValues;

	}

	//!!!not working
	public boolean isAnnotationPresence(SWTBotTreeItem annotation) {
		return annotation.isChecked();
	}

	//!!!not working
	public List<SWTBotTreeItem> getAllActiveAnnotation() {

		List<SWTBotTreeItem> activeAnnotations = new ArrayList<SWTBotTreeItem>();
		for (SWTBotTreeItem ti : getAllAnnotations()) {
			if (isAnnotationPresence(ti)) {
				activeAnnotations.add(ti);
			}
		}
		return activeAnnotations;

	}
	
	
	//!!!not working
	public SWTBotTreeItem activateAnnotation(SWTBotTreeItem annotation) {
		return changeStateOfAnnotation(annotation);
	}

	//!!!not working
	public SWTBotTreeItem deactivateAnnotation(SWTBotTreeItem annotation) {
		return changeStateOfAnnotation(annotation);
	}

	public SWTBotTreeItem changeAnnotationParamValue(SWTBotTreeItem annotation,
			String param, String newValue) {
		for (SWTBotTreeItem parameter : getAnnotationValues(annotation)) {
			if (parameter.getText().equals(param)) {
				annotation.getNode(param).select();
				parameter.click(1);
				show().bot().text().setText(newValue);
				parameter.click(0);
				break;
			}
		}
		return annotation;
	}
	
	private SWTBotTreeItem changeStateOfAnnotation(SWTBotTreeItem annotation) {
		
		show().bot().tree().select(annotation.getText());
		annotation.click(1);
		return annotation;
	}

}
