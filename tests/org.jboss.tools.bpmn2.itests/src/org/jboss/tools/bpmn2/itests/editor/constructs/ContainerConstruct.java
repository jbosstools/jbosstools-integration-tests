package org.jboss.tools.bpmn2.itests.editor.constructs;

import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;

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
		Rectangle bounds = editor.getBounds(editor.rootEditPart());
		/*
		 * 1/10 from the X axis 
		 */
		int x = (bounds.x + bounds.width) / 10;
		/* 
		 * 1/2 from the Y axis
		 */
		int y = (bounds.y + bounds.height) / 2;
		/*
		 * Add
		 */
		if (isAvailable(x, y)) {
			add(name, type, x, y);
		} else {
			throw new RuntimeException("[x, y] = " + "[" + x + ", " + y + "] is not available");
		}
	}
	
	/**
	 * 
	 * @param name
	 * @param type
	 * @param x
	 * @param y
	 */
	public void add(String name, ConstructType type, int x, int y) {
		/* 
		 * Add the construct
		 */
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
		List<SWTBotGefEditPart> editPartList = editor.getEditParts(new BaseMatcher<EditPart>() {

			public boolean matches(Object item) {
				/*
				 * instance of org.eclipse.gef.EditPart but nor of org.eclipse.graphiti.ui.internal.parts.DiagramEditPart.
				 * 
				 * - don't want to include other dependencies that's why the class is compared using it's name
				 *   instead of using the 'instanceof' operator.
				 */
				if ((item instanceof EditPart) && !(item.getClass().getName().endsWith("DiagramEditPart"))) {
					Rectangle bounds = editor.getBounds((GraphicalEditPart) item);
					
					int xItemStart = bounds.x;
					int xItemEnd = bounds.x + bounds.width;
					
					int yItemStart = bounds.y;
					int yItemEnd = bounds.y + bounds.height;
					
					return xItemStart < x && xItemEnd > x && yItemStart < y && yItemEnd > y;
				}
				return false;
			}

			public void describeTo(Description description) {
				description.appendText(" checking bounds [x,y] = " + "[" + x + ", " + y + "] for editPart presence.");
			}
			
		});
		
		return editPartList.size() == 0;
		
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
