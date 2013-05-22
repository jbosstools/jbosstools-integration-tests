package org.jboss.tools.switchyard.reddeer.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.hamcrest.Matcher;

/**
 * Class for finding figures in a given canvas.
 * 
 * @author apodhrad
 * 
 */
public class FigureUtils {

	public static List<IFigure> getFigures(FigureCanvas canvas, Matcher<IFigure> matcher) {
		List<IFigure> figures = new ArrayList<IFigure>();
		Stack<IFigure> stack = new Stack<IFigure>();
		// Initial push
		stack.push(canvas.getContents());
		// Depth first search
		while (!stack.isEmpty()) {
			// Pop figure
			IFigure figure = stack.pop();
			// Does it matches?
			if (matcher.matches(figure)) {
				figures.add(figure);
			}
			// Push another children
			for (Object child : figure.getChildren()) {
				if (child instanceof IFigure) {
					stack.push((IFigure) child);
				}
			}
		}
		return figures;
	}
}
