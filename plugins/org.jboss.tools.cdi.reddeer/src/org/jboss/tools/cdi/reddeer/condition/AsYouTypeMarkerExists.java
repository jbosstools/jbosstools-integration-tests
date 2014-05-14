package org.jboss.tools.cdi.reddeer.condition;

import org.jboss.reddeer.swt.condition.WaitCondition;

public class AsYouTypeMarkerExists implements WaitCondition{
	
	private String message;
	private AsYouTypeValidationHelper helper = new AsYouTypeValidationHelper();
	
	public AsYouTypeMarkerExists(){
		this(null);
	}
	
	public AsYouTypeMarkerExists(String message) {
		this.message = message;
	}

	@Override
	public boolean test() {
		return helper.markerExists(helper.getAnnotationModel(), null, message);
	}

	@Override
	public String description() {
		return "No as-you-type marker exists in '" + 
				helper.getActiveTextEditor().getTitle() + "'";
	}

}
