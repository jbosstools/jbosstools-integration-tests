package org.jboss.tools.switchyard.reddeer.utils;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swtbot.swt.finder.results.Result;

/**
 * 
 * @author apodhrad
 * 
 */
public class ControlUtils {

	public static Rectangle getBounds(final Control control) {
		return syncExec(new Result<Rectangle>() {

			@Override
			public Rectangle run() {
				return control.getBounds();
			}
		});
	}

	public static Rectangle getAbsoluteBounds(final Control control) {
		Point point = getAbsolutePoint(control);
		Rectangle rec = getBounds(control);
		return new Rectangle(rec.x + point.x, rec.y + point.y, rec.width, rec.height);
	}

	public static Point getAbsolutePoint(final Control control) {
		return syncExec(new Result<Point>() {

			@Override
			public Point run() {
				return control.toDisplay(0, 0);
			}
		});
	}

	public static Point getCentralPoint(final Control control) {
		return getCentralPoint(getBounds(control));
	}

	public static Point getAbsoluteCentralPoint(final Control control) {
		return getCentralPoint(getAbsoluteBounds(control));
	}

	private static Point getCentralPoint(Rectangle rec) {
		return new Point(rec.x + rec.width / 2, rec.y + rec.height / 2);
	}
}
