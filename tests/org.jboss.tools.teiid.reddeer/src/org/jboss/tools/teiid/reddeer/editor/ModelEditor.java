package org.jboss.tools.teiid.reddeer.editor;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefFigureCanvas;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefViewer;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCTabItem;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.ui.IEditorReference;
import org.hamcrest.Matcher;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.teiid.reddeer.matcher.IsTransformation;
import org.jboss.tools.teiid.reddeer.matcher.WaitForFigure;
import org.jboss.tools.teiid.reddeer.matcher.WithBounds;
import org.jboss.tools.teiid.reddeer.matcher.WithLabel;
import org.jboss.tools.teiid.reddeer.widget.SWTBotGefFigure;
import org.jboss.tools.teiid.reddeer.widget.TeiidStyledText;

/**
 * This class represents Model Editor in Teiid Designer perspective.
 * 
 * @author apodhrad
 * 
 */
public class ModelEditor extends SWTBotEditor {

	public static final String TRANSFORMATION_DIAGRAM = "Transformation Diagram";
	public static final String MAPPING_DIAGRAM = "Mapping Diagram";
	public static final String PACKAGE_DIAGRAM = "Package Diagram";
	public static final String TABLE_EDITOR = "Table Editor";

	private SWTBotGefViewer viewer;

	public ModelEditor(SWTBotEditor editor, SWTWorkbenchBot bot) {
		this(editor.getReference(), bot);
	}

	public ModelEditor(IEditorReference editorReference, SWTWorkbenchBot bot) {
		super(editorReference, bot);
		Bot.get().sleep(5 * 1000);
	}

	public ModelEditor(String title) {
		super(Bot.get().editorByTitle(title).getReference(), Bot.get());
		Bot.get().sleep(5 * 1000);
	}

	private GraphicalEditor getGraphicalEditor(String tabLabel) {
		final SWTBotCTabItem tabItem = showTab(tabLabel);
		GraphicalEditor graphicalEditor = syncExec(new Result<GraphicalEditor>() {

			@Override
			public GraphicalEditor run() {
				Object obj = tabItem.widget.getData();
				if (obj instanceof GraphicalEditor) {
					return (GraphicalEditor) obj;
				}
				return null;
			}
		});
		return graphicalEditor;
	}

	public SWTBotGefViewer getGraphicalViewer(String tabLabel) {
		final GraphicalEditor graphicalEditor = getGraphicalEditor(tabLabel);
		GraphicalViewer graphicalViewer = syncExec(new Result<GraphicalViewer>() {

			@Override
			public GraphicalViewer run() {
				Object obj = graphicalEditor.getAdapter(GraphicalViewer.class);
				if (obj instanceof GraphicalViewer) {
					return (GraphicalViewer) obj;
				}
				return null;
			}
		});

		return new SWTBotGefViewer(graphicalViewer);
	}

	public SWTBotCTabItem showTab(String label) {
		SWTBotCTabItem tabItem = bot.cTabItem(label);
		tabItem.activate();
		tabItem.show();
		return tabItem;
	}

	public void showTransformation() {
		viewer = getGraphicalViewer(TRANSFORMATION_DIAGRAM);
		viewer.editParts(IsTransformation.isTransformation()).get(0).select();
		viewer.clickContextMenu("Edit");
		Bot.get().sleep(5 * 1000);
	}

	public void showMappingTransformation(String label) {
		viewer = getGraphicalViewer(MAPPING_DIAGRAM);
		viewer.getEditPart(label).select();
		viewer.clickContextMenu("Edit");
	}

	public CriteriaBuilder criteriaBuilder() {
		bot.toolbarButtonWithTooltip("Criteria Builder").click();
		SWTBotShell shell = bot.shell("Criteria Builder");
		shell.activate();
		return new CriteriaBuilder(shell);
	}

	public void setTransformationProcedureBody(String procedure) {
		String transformationText = getTransformation();
		transformationText = transformationText.replaceAll("<--.*-->;", procedure);

		TeiidStyledText styledText = new TeiidStyledText(0);
		styledText.setText(transformationText);
		styledText.navigateTo(2, procedure.length() / 2);
		styledText.mouseClickOnCaret();
	}

	public String getTransformation() {
		return bot.styledText(0).getText();
	}

	public void setTransformation(String text) {
		Bot.get().styledText(0).setText(text);
	}

	public void saveAndValidateSql() {
		clickButtonOnToolbar("Save/Validate SQL");
	}

	public void clickButtonOnToolbar(String button) {
		bot.toolbarButtonWithTooltip(button).click();
	}

	public void showTransformation(String label) {
		SWTBotGefFigure figureBot = figureWithLabel(label);
		editFigure(figureBot);
	}

	public void editFigure(SWTBotGefFigure figureBot) {
		Rectangle rectangle = figureBot.getAbsoluteBounds();
		SWTBotGefFigureCanvas canvas = getFigureCanvas();
		canvas.mouseMoveLeftClick(rectangle.x + 1, rectangle.y + 1);
		canvas.contextMenu("Edit").click();
		bot().waitUntil(new DefaultCondition() {

			@Override
			public boolean test() throws Exception {
				try {
					bot.styledText();
					return true;
				} catch (WidgetNotFoundException wnfe) {
					return false;
				}
			}

			@Override
			public String getFailureMessage() {
				return "Process wasn't completed";
			}
		});
		bot().styledText();
	}

	private SWTBotGefFigureCanvas getFigureCanvas() {
		Matcher<FigureCanvas> matcher = widgetOfType(FigureCanvas.class);
		return new SWTBotGefFigureCanvas((FigureCanvas) bot.widget(matcher, 0));
	}

	public SWTBotGefFigure figureWithLabel(String label) {
		return figureWithLabel(label, 0);
	}

	public SWTBotGefFigure figureWithLabel(String label, int index) {
		Matcher<IFigure> matcher = new WithLabel(label);
		return new SWTBotGefFigure(figure(matcher, index));
	}

	public SWTBotGefFigure tFigure() {
		Matcher matcher = allOf(instanceOf(ImageFigure.class), new WithBounds(40, 60));
		return new SWTBotGefFigure(figure(matcher, 0));
	}

	public IFigure figure(Matcher<IFigure> matcher, int index) {
		SWTBotGefFigureCanvas canvas = getFigureCanvas();
		WaitForFigure waitForFigure = new WaitForFigure(matcher, (FigureCanvas) canvas.widget);
		bot().waitUntil(waitForFigure);
		return waitForFigure.get(index);
	}

	public ModelDiagram getModeDiagram(String label) {
		return getModelDiagram(label, PACKAGE_DIAGRAM);
	}

	public ModelDiagram getModelDiagram(String label, String tab) {
		SWTBotGefViewer viewer = getGraphicalViewer(tab);
		SWTBotGefEditPart editPart = viewer.getEditPart(label);
		if (editPart != null) {
			return new ModelDiagram(viewer.getEditPart(label));
		} else {
			return null;
		}
	}
}
