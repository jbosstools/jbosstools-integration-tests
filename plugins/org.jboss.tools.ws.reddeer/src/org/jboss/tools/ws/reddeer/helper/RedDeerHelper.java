package org.jboss.tools.ws.reddeer.helper;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.core.handler.TreeHandler;
import org.jboss.reddeer.core.util.Display;

/**
 * Red Deer Helper.
 * 
 * @author Radoslav Rabara, mlabuda@redhat.com
 */
public class RedDeerHelper {
	
	/**
	 * Click on specified tree item in the specified column.
	 * 
	 * @param item tree item to click
	 * @param column column to click
	 */
	public static void click(final TreeItem item, final int column) {
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				Rectangle bounds = item.getSWTWidget().getBounds(column);
				Point center = new Point(bounds.x + (bounds.width / 2), bounds.y + (bounds.height / 2));
				click(item.getSWTWidget(), center.x, center.y);
			}
		});
	}
	
	/**
	 * Click on the [X,Y] coordinates of tree item.
	 *
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	private static void click(final org.eclipse.swt.widgets.TreeItem treeItem, int x, int y) {
		TreeHandler treeHandler = TreeHandler.getInstance();
		treeHandler.createEventForTree(treeItem, SWT.MouseEnter);
		treeHandler.createEventForTree(treeItem, SWT.MouseEnter);
		treeHandler.createEventForTree(treeItem, SWT.MouseMove);
		treeHandler.createEventForTree(treeItem, SWT.Activate);
		treeHandler.createEventForTree(treeItem, SWT.FocusIn);
		treeHandler.notifyTree(treeItem, createMouseEvent(treeItem, x, y, 1, SWT.NONE, 1, SWT.MouseDown));
		treeHandler.notifyTree(treeItem, createMouseEvent(treeItem, x, y, 1, SWT.BUTTON1, 1, SWT.MouseUp));
		treeHandler.notifyTree(treeItem, createSelectionEvent(treeItem, SWT.BUTTON1));
		treeHandler.createEventForTree(treeItem, SWT.MouseHover);
		treeHandler.createEventForTree(treeItem, SWT.MouseMove);
		treeHandler.createEventForTree(treeItem, SWT.MouseExit);
		treeHandler.createEventForTree(treeItem, SWT.Deactivate);
		treeHandler.createEventForTree(treeItem, SWT.FocusOut);
	}
	
	private static Event createMouseEvent(org.eclipse.swt.widgets.TreeItem item, int x, int y, int button, int stateMask, int count, int type) {
		Event event = new Event();
		event.time = (int) System.currentTimeMillis();
		event.widget = item;
		event.display = Display.getDisplay();
		event.type = type;
		event.x = x;
		event.y = y;
		event.button = button;
		event.stateMask = stateMask;
		event.count = count;
		return event;
	}
	
	private static Event createSelectionEvent(org.eclipse.swt.widgets.TreeItem item, int stateMask) {
		Event event = new Event();
		event.time = (int) System.currentTimeMillis();
		event.type = SWT.Selection;
		event.widget = item;
		event.display = Display.getDisplay();
		event.stateMask = stateMask;
		return event;
	}
}
	