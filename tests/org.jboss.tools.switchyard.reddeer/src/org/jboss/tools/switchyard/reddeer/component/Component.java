package org.jboss.tools.switchyard.reddeer.component;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;

import java.util.List;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.hamcrest.Matcher;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.switchyard.reddeer.condition.ContextButtonAppeared;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.matcher.WithTooltip;
import org.jboss.tools.switchyard.reddeer.utils.MouseUtils;
import org.jboss.tools.switchyard.reddeer.widget.ContextButton;

/**
 * A general switchyard component.
 * 
 * @author apodhrad
 * 
 */
public class Component {

	protected SWTBotGefEditPart editPart;

	public Component() {
	}

	public Component(String tooltip) {
		this(tooltip, 0);
	}

	public Component(String tooltip, int index) {
		this(new WithTooltip(tooltip), index);
	}

	public Component(Matcher<EditPart> matcher, int index) {
		List<SWTBotGefEditPart> list = new SwitchYardEditor().editParts(matcher);
		if (list.size() > index) {
			editPart = list.get(index);
		} else {
			throw new ComponentNotFoundException(matcher, index);
		}
	}
	
	public void delete() {
		contextButton("Delete").click();
		String deleteShellText = "Confirm Delete";
		new WaitUntil(new ShellWithTextIsActive(deleteShellText));
		new PushButton("Yes").click();
		new WaitWhile(new ShellWithTextIsActive(deleteShellText));
		new WaitWhile(new JobIsRunning());
	}

	public ContextButton contextButton(final String label) {
		new WaitUntil(new ContextButtonAppeared(this, label));
		return new ContextButton(label);
	}

	public void select() {
		editPart.select();
	}

	public void hover() {
		Rectangle rectangle = getDisplayBounds();
		Point centralPoint = getCentralPoint(rectangle);
		MouseUtils.mouseMove(centralPoint.x, centralPoint.y);
	}

	public void click() {
		Point centralPoint = getCentralPoint(getAbsoluteBounds());
		click(centralPoint.x, centralPoint.y);
	}

	public void click(int x, int y) {
		editPart.click(new org.eclipse.draw2d.geometry.Point(x, y));
	}

	public void doubleClick() {
		Rectangle rectangle = getDisplayBounds();
		Point centralPoint = getCentralPoint(rectangle);
		MouseUtils.doubleClick(centralPoint.x, centralPoint.y);
	}

	public Rectangle getAbsoluteBounds() {
		IFigure figure = getFigure(editPart);
		Rectangle bounds = figure.getBounds().getCopy();
		figure.translateToAbsolute(bounds);
		return bounds;
	}

	public Rectangle getDisplayBounds() {
		Rectangle bounds = getAbsoluteBounds();
		Point point = syncExec(new Result<Point>() {

			@Override
			public Point run() {
				return getFigureCanvas().toDisplay(0, 0);
			}
		});
		return new Rectangle(bounds.x + point.x, bounds.y + point.y, bounds.width, bounds.height);
	}

	private Point getCentralPoint(Rectangle rectangle) {
		int x = rectangle.x + rectangle.width / 2;
		int y = rectangle.y + rectangle.height / 2;
		return new Point(x, y);
	}

	private IFigure getFigure(SWTBotGefEditPart editPart) {
		return ((GraphicalEditPart) editPart.part()).getFigure();
	}

	private FigureCanvas getFigureCanvas() {
		return (FigureCanvas) editPart.part().getRoot().getViewer().getControl();
	}

	public SWTBotGefEditPart getEditPart() {
		return editPart;
	}

}
