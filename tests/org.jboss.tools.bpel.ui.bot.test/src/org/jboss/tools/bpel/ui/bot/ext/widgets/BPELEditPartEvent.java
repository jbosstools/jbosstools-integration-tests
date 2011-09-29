package org.jboss.tools.bpel.ui.bot.ext.widgets;

import java.util.Properties;

import org.eclipse.gef.EditPart;

public class BPELEditPartEvent {
	
	EditPart editpart;
	
	Properties properties;
	
	
	public BPELEditPartEvent(EditPart editpart) {
		this(editpart, new Properties());
	}
	
	public BPELEditPartEvent(EditPart editpart, String[] attribs, String[] vals) {
		this.editpart = editpart;
		this.properties = new Properties();
		for(int i=0; i<attribs.length; i++) {
			properties.put(attribs[i], vals[i]);
		}
	}
	
	public BPELEditPartEvent(EditPart editpart, Properties changedProperties) {
		this.editpart = editpart;
		this.properties = changedProperties;
	}
	
	public Properties getChangedProperties() {
		return properties;
	}
	
	public EditPart getEditPart() {
		return editpart;
	}
	
	
}
