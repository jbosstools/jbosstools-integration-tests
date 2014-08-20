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
package org.jboss.tools.ws.ui.bot.test.uiutils.wizards;


public class WebServiceWizard extends WsWizardBase {

	public enum Service_Type {
		BOTTOM_UP, TOP_DOWN;
		
		String getDescription() {
			String prefix = "";
			switch (this) {
			case BOTTOM_UP:
				prefix += "Bottom up";
				break;
			case TOP_DOWN:
				prefix += "Top down";
				break;
			default:
				throw new IllegalArgumentException("Unknown type: " + this);
			}
			return prefix + " Java bean Web Service";
		}
	}

	public void setServiceType(Service_Type type) {
    	setFocus();
		bot().comboBoxWithLabel("Web service type:").setSelection(type.getDescription());
	}
	
	public Service_Type getServiceType() {
    	setFocus();
		String s = bot().comboBoxWithLabel("Web service type:").getText();
		return s.startsWith("Bottom up") ? Service_Type.BOTTOM_UP : Service_Type.TOP_DOWN;
	}
	
	@Override
	protected String getSourceComboLabel() {
		String s = null;
		switch (getServiceType()) {
		case BOTTOM_UP:
			s = "Service implementation:";
			break;
		case TOP_DOWN:
			s = "Service definition:";
			break;
		default:
			throw new IllegalArgumentException("Unknown type: " + this);
		}
		return s;
	}
	
	public void setServiceProject(String name) {
		setTargetProject("Service project:", name);
	}
	
	public void setServiceEARProject(String name) {
		setTargetProject("Service EAR project:", name);
	}
	
	public void setServiceSlider(Slider_Level level) {
		if (Slider_Level.NO_CLIENT == level) {
			throw new UnsupportedOperationException("Unsupported level: " + level);
		}
		setSlider(level, 0);
	}
	
	public void setClientSlider(Slider_Level level) {
		setSlider(level, 1);
	}
	
	public boolean isClientEnabled() {
		return isScaleEnabled(1);
	}
}
