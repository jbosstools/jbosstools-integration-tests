package org.jboss.tools.bpmn2.itests.editor;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.jboss.tools.bpmn2.itests.swt.matcher.ConstructOnPoint;

/**
 * ISSUES: 
 *   1) Drill down does not work properly.
 *   	+ After a activity is added in the tab (drill down) the view will
 *        automatically be switched to the main process (back).
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class ContainerConstruct extends Construct {

	/**
	 * 
	 * @param name
	 * @param type
	 */
	public ContainerConstruct(String name, ConstructType type) {
		this(name, type, null, -1, true);
	}
	
	/**
	 * 
	 * @param name
	 * @param type
	 * @param parent
	 * @param index
	 * @param select
	 */
	public ContainerConstruct(String name, ConstructType type, Construct parent, int index, boolean select) {
		super(name, type, parent, index, select);
	}

	/**
	 * 
	 * @param name
	 * @param type
	 */
	public void add(String name, ConstructType type) {
		String sectionName = type.toToolPath()[0];
		
		Rectangle bounds = editor.getBounds(editPart);
		int x = 0;
		int y = 0;
		
		
		if ("Boundary Events".equals(sectionName)) {
			/*
			 * Upper left corner
			 */
			x = y = 5;
		} else {
			x = bounds.x + (bounds.width / 8);
			/*
			 * ISSUE: Need to have -5 otherwise it will hit the connection arrow if an activity is under it!!!
			 * 		  Rerun AdHocSubProcessTest without the -5 to reproduce.
			 */
			y = bounds.y + (bounds.height / 10);
		}
		add(name, type, new Point(x, y));
	}
	
	/**
	 * 
	 * @param nextTo
	 * @param name
	 * @param type
	 * @param position
	 */
	public void add(String name, ConstructType type, Construct nextTo, Position position) {
		add(name, type, findPoint(this, nextTo, position));
	}
	
	
	/**
	 * 
	 * @param name
	 * @param type
	 * @param point
	 */
	private void add(String name, ConstructType type, Point point) {
		if (!isInternalAvailable(point)) {
			throw new RuntimeException("'" + point + "' is not available");
		}
		/*
		 * Add the construct using the tool in the palette.
		 */
		select();
		log.info("Adding consturct '" + name + "' of type '" + type + "' to '" + point + "'");
		editor.activateTool(type.toToolPath()[0], type.toToolPath()[1]);
		editor.click(point.x(), point.y());
		/*
		 * Set name
		 */
		Construct c = editor.getLastConstruct(type);
		c.select();
		c.setName(name);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	protected boolean isInternalAvailable(Point point) {
		/*
		 * The point must be inside this edit part.
		 */
		if (editor.getBounds(editPart).contains(point)) {
			/*
			 * Check weather the point is not already taken by another child editPart.
			 */
			return editor.getEditParts(editPart, new ConstructOnPoint<EditPart>(point)).isEmpty();
		}
		/*
		 * Out of bounds.
		 */
		return false;
	}
	
	/**
	 * 
	 */
	public void pushDown() {
		editor.clickContextMenu("Push down");
	}
	
	/**
	 * 
	 */
	public void undoPushDown() {
		editor.clickContextMenu("Undo Push down");
	}
}
