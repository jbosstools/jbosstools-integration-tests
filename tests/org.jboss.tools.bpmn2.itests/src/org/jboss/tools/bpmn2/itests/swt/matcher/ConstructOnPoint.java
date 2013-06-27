package org.jboss.tools.bpmn2.itests.swt.matcher;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import org.jboss.tools.bpmn2.itests.reddeer.BPMN2Editor;

/**
 * Returns an edit part which is on a given location (given by X, Y) in the editor.
 * 
 * @author mbaluch
 */
public class ConstructOnPoint<T extends EditPart> extends BaseMatcher<EditPart> {

	private Point p;
	
	/**
	 * 
	 * @param p
	 */
	public ConstructOnPoint(Point p) {
		this.p = p;
	}

	/**
	 * 
	 * @param item
	 */
	public boolean matches(Object item) {
		return new BPMN2Editor().getBounds((GraphicalEditPart) item).contains(p);
	}

	/**
	 * 
	 * @param description
	 */
	public void describeTo(Description description) {
		description.appendText(" checking " + p + " for editPart presence.");
	}

}
