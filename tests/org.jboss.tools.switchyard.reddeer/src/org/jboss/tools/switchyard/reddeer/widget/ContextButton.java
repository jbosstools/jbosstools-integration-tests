package org.jboss.tools.switchyard.reddeer.widget;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.asyncExec;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import java.util.List;

import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.switchyard.reddeer.matcher.ContextButtonMatcher;
import org.jboss.tools.switchyard.reddeer.utils.FigureUtils;

/**
 * Represents a context button in graphiti framework.
 * 
 * @author apodhrad
 * 
 */
public class ContextButton {

	private Clickable contextButton;

	public ContextButton(String label) {
		FigureCanvas canvas = Bot.get().widget(widgetOfType(FigureCanvas.class), 1);
		List<IFigure> figures = FigureUtils.getFigures(canvas, new ContextButtonMatcher(label));
		if (figures.isEmpty()) {
			throw new RuntimeException("Couldn't found context button with label '" + label + "'");
		}
		contextButton = (Clickable) figures.get(0);
	}

	public void click() {
		asyncExec(new VoidResult() {

			@Override
			public void run() {
				contextButton.doClick();
			}

		});
	}
}
