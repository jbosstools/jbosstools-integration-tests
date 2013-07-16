package org.jboss.tools.teiid.reddeer.matcher;

import org.eclipse.swt.widgets.Button;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Gets all buttons with the same tooltip (e.g. radio buttons column/constant/function in criteria builder)
 * @author lfabriko
 *
 */
public class ButtonWithToolTip extends BaseMatcher {

	private String toolTip;
	
	public ButtonWithToolTip(String toolTip){
		this.toolTip = toolTip;
	}
	
	@Override
	public boolean matches(Object o) {
		if (o instanceof Button){
			Button but = (Button)o;
			if (but.getToolTipText().equals(this.toolTip)){
				return true;
			}
		}
		return false;
	}

	@Override
	public void describeTo(Description arg0) {
		// TODO Auto-generated method stub
		
	}

}
