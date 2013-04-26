package org.jboss.tools.bpmn2.itests.editor.constructs;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
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
		Rectangle bounds = editor.getBounds(editPart);
		/*
		 * 1/10 from the X axis 
		 */
		int x = bounds.x + (bounds.width / 8);
		/* 
		 * 1/2 from the Y axis
		 */
		int y = bounds.y + (bounds.height / 2);
		/*
		 * Add the construct
		 */
		add(name, type, x, y);
	}
	
	/**
	 * 
	 * @param name
	 * @param type
	 * @param x
	 * @param y
	 */
	public void add(String name, ConstructType type, int x, int y) {
		if (!isAvailable(x, y)) {
			throw new RuntimeException("[x, y] = " + "[" + x + ", " + y + "] is not available");
		}
		/*
		 * Add the construct using the tool in the palette.
		 */
		log.info("Adding consturct '" + name + "' of type '" + type + "' to [x, y] = [" + x + ", " + y + "]");
		editor.activateTool(type.toToolName());
		editor.click(x, y);
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
	protected boolean isAvailable(final int x, final int y) {
		/*
		 * The point must be inside this edit part.
		 */
		if (editor.getBounds(editPart).contains(x, y)) {
			/*
			 * Check weather the point is not already taken by another child editPart.
			 */
			return editor.getEditParts(editPart, new ConstructOnPoint<EditPart>(x, y)).isEmpty();
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
