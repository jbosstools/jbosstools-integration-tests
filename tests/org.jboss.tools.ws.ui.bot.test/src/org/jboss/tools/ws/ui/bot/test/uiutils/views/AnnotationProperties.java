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

import javax.jws.WebService;


import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.view.ViewBase;

/**
 * 
 * @author jjankovi
 *
 */
@WebService(targetNamespace = "http://views.uiutils.test.bot.ui.ws.tools.jboss.org/", portName = "AnnotationPropertiesPort", serviceName = "AnnotationPropertiesService")
public class AnnotationProperties extends ViewBase {

	public AnnotationProperties() {
		viewObject = ActionItem.View.JAXWSAnnotationProperties.LABEL;
	}
	
	public List<SWTBotTreeItem> getAllAnnotations() {
		
		List<SWTBotTreeItem> annotations = new ArrayList<SWTBotTreeItem>();
		SWTBotTree annotationViewTree = null;
		try {
			annotationViewTree = show().bot().tree();
			for (SWTBotTreeItem ti : annotationViewTree.getAllItems()) {
				annotations.add(ti);
			}
		} catch (WidgetNotFoundException exc) {
			log.warn("There are no web service annotations in Annotation Properties view");
		}
		
		return annotations;
	}
	
	public List<SWTBotTreeItem> getAnnotationValues(SWTBotTreeItem annotation) {
		
		List<SWTBotTreeItem> annotValues = new ArrayList<SWTBotTreeItem>();
		for (SWTBotTreeItem ti : annotValues) {
			annotValues.add(ti);
		}
		return annotValues;
	
	}
	
	public boolean isAnnotationPresence(SWTBotTreeItem annotation) {
		return annotation.isChecked();
	}
	
	public SWTBotTreeItem activateAnnotation(SWTBotTreeItem annotation) {
		annotation.check();
		return annotation;
	}
	
	public SWTBotTreeItem deactivateAnnotation(SWTBotTreeItem annotation) {
		annotation.uncheck();
		return annotation;
	}
	
	public SWTBotTreeItem changeAnnotationParamValue(SWTBotTreeItem annotation, String param,
											         String newValue) {
		for (SWTBotTreeItem parameter : getAnnotationValues(annotation)) {
			if (parameter.equals(param)) {
				//change value of parameter no newValue
				break;
			}
		}
		return annotation;
	}
	
}
