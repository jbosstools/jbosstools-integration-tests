package org.jboss.tools.bpmn2.itests.swt.matcher;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import org.jboss.tools.bpmn2.itests.editor.BPMN2Editor;

/**
 * Returns an edit part which is on a given location (given by X, Y) in the editor.
 * 
 * @author mbaluch
 */
public class ConstructOnPoint<T extends EditPart> extends BaseMatcher<EditPart> {

	private int x;
	
	private int y;
	
	/**
	 * 
	 * @param x
	 * @param y
	 */
	public ConstructOnPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * 
	 * @param item
	 */
	public boolean matches(Object item) {
		return new BPMN2Editor().getBounds((GraphicalEditPart) item).contains(x, y);
	}

	/**
	 * 
	 * @param description
	 */
	public void describeTo(Description description) {
		description.appendText(" checking point [x,y] = " + "[" + x + ", " + y + "] for editPart presence.");
	}

}
