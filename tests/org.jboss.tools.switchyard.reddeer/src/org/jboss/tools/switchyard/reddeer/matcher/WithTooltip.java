package org.jboss.tools.switchyard.reddeer.matcher;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Matcher which returns true if an edit part has a given tooltip.
 * 
 * @author apodhrad
 * 
 */
public class WithTooltip extends BaseMatcher<EditPart> {

	private String tooltip;

	public WithTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	@Override
	public boolean matches(Object obj) {
		if (obj instanceof GraphicalEditPart) {
			IFigure figure = ((GraphicalEditPart) obj).getFigure();
			IFigure tooltip = figure.getToolTip();
			if (tooltip instanceof Label) {
				Label label = (Label) tooltip;
				return label.getText().equals(this.tooltip);
			}
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("with tooltip '" + tooltip + "'");
	}

}
