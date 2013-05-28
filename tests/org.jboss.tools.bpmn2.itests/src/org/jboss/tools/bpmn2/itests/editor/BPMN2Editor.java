package org.jboss.tools.bpmn2.itests.editor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotMultiPageEditor;
import org.eclipse.swtbot.eclipse.gef.finder.finders.PaletteFinder;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.ui.IEditorPart;

import org.hamcrest.Matcher;

import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.bpmn2.itests.editor.constructs.Construct;
import org.jboss.tools.bpmn2.itests.swt.matcher.ConstructAttributeMatchingRegex;
import org.jboss.tools.bpmn2.itests.swt.matcher.ConstructWithName;
import org.jboss.tools.bpmn2.itests.swt.matcher.PaletteEntryMatcher;


/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class BPMN2Editor extends SWTBotGefEditor {

	private static final int SAVE_SLEEP_TIME = 2000;
	
	private Logger log = Logger.getLogger(BPMN2Editor.class);
	
	/**
	 * Creates a new instance of Bpmn2Editor.
	 */
	public BPMN2Editor() {
		super(Bot.get().activeEditor().getReference(), Bot.get());
	}
	
	/**
	 * Creates a new instance of Bpmn2Editor.
	 * 
	 * @param title
	 */
	public BPMN2Editor(String title) {
		super(Bot.get().editorByTitle(title).getReference(), Bot.get());
	}
	
	/**
	 * 
	 * @param editPart
	 * @param byLabel
	 */
	public void selectEditPart(SWTBotGefEditPart editPart) {
		if (!getSelectedEditParts().contains(editPart)) {
			editPart.select();
		}
		setFocus();
	}

	/**
	 * 
	 * @return
	 */
	public List<SWTBotGefEditPart> getSelectedEditParts() {
		return selectedEditParts();
	}
	
	/**
	 * 
	 * @return
	 */
	public SWTBotGefEditPart getSelectedEditPart() {
		List<SWTBotGefEditPart> selectedParts = selectedEditParts();
		if (!selectedParts.isEmpty()) {
			return selectedParts.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * @param matcher
	 * @param index
	 * @return
	 */
	public SWTBotGefEditPart getEditPart(Matcher<? extends EditPart> matcher, int index) {
		return editParts(matcher).get(index);
	}

	/**
	 * 
	 * @param matcher
	 * @return
	 */
	public List<SWTBotGefEditPart> getEditParts(Matcher<? extends EditPart> matcher) {
		return editParts(matcher);
	}
	
	/**
	 * 
	 * @param label
	 */
	public List<SWTBotGefEditPart> getEditParts(String label) {
		return editParts(new ConstructWithName<EditPart>(label));
	}
	
	/**
	 * 
	 * @param label
	 */
	public SWTBotGefEditPart getEditPart(String label) {
		List<SWTBotGefEditPart> partList = getEditParts(label);
		if (!partList.isEmpty()) {
			return partList.get(0);
		}
		return null;
	}

	/**
	 * 
	 * @param editPart
	 * @param matcher
	 * @return
	 */
	public List<SWTBotGefEditPart> getEditParts(SWTBotGefEditPart editPart, Matcher<? extends EditPart> matcher) {
		return editPart.descendants(matcher);
	}
	
	/**
	 * 
	 * @param editPart
	 * @return
	 */
	public Rectangle getBounds(SWTBotGefEditPart editPart) {
		return getBounds((GraphicalEditPart) editPart.part());
	}
	
	/**
	 * 
	 * @param part
	 * @return
	 */
	public Rectangle getBounds(GraphicalEditPart part) {
		IFigure figure = part.getFigure();
		Rectangle bounds = figure.getBounds().getCopy();
		figure.translateToAbsolute(bounds);
		return bounds;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName(SWTBotGefEditPart editPart) {
		Object model = editPart.part().getModel();
		if (model instanceof Shape) {
			Shape shape = (Shape) model;
			PictogramLink link = shape.getLink();
			if (link != null) {
				EList<EObject> objectList = link.getBusinessObjects();
				for (EObject eo : objectList) {
					try {
						// Just one of the business objects should have a name!
						Method method = eo.getClass().getMethod("getName");
						return method.invoke(eo).toString();
					} catch (Exception e) {
						// Ignore
					}
				}
			}
		}
		// This should not happen!
		return null;
	}
	
	/**
	 * 
	 * @param label
	 */
	public void selectConstruct(String label) {
		SWTBotGefEditPart editPart = getEditPart(label);
		if (editPart == null) {
			throw new RuntimeException("Cannot find '" + label + "'");
		}
		selectEditPart(editPart);
		log.info("Selected construct '" + label + "'");
	}

	/**
	 * 
	 * @param matcher
	 * @param index
	 */
	public void selectConstruct(Matcher<? extends EditPart> matcher, int index) {
		selectEditPart(getEditPart(matcher, index));
	}
	
	/**
	 * 
	 * @param constructType
	 * @return
	 */
	public List<Construct> getConstructs(ConstructType constructType) {
		log.info("Searching for construct '" + constructType.toName() + "'");
		// Find the construct added in last step. The lookup is based
		// on the generation algorithm of the default construct name.
		// The name has the format of 'construct_type sequence_number'
		// e.g. 'Task 10'
		List<Construct> constructList = new ArrayList<Construct>();
		
		Pattern pattern = Pattern.compile(constructType.toId() + "_[0-9]+");
		List<SWTBotGefEditPart> editPartList = getEditParts(new ConstructAttributeMatchingRegex<EditPart>("id", pattern));
		
		for (SWTBotGefEditPart editPart : editPartList) {
			constructList.add(new Construct(getName(editPart), constructType, null, 1, false));
		}
		
		return constructList;
	}
	
	/**
	 * 
	 * @param connectionType
	 * @return
	 */
	public Construct getLastConstruct(ConstructType constructType) {
		List<Construct> constructList = getConstructs(constructType);
		if (!constructList.isEmpty()) {
			return constructList.get(constructList.size() - 1);
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSourceText() {
		clickContextMenu("Show Source View");
		
		SWTBotMultiPageEditor editor = new SWTBotMultiPageEditor(getReference(), bot);
		editor.activatePage("Source");
		
		String text = editor.toTextEditor().getText();
		
		editor.activatePage("Process Diagram");
		
		return text;
	}

	/**
	 * @see org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor#click(int, int)
	 */
	@Override
	public void click(int xPosition, int yPosition) {
		try {
			/*
			 * Must wait till the mouse event get's processed by eclipse.
			 */
			super.click(xPosition, yPosition);
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TBD: log warning
		}
		
	}

	/**
	 * @see org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor#save()
	 */
	@Override
	public void save() {
		try {
			bot.menu("File").menu("Save").click();
			try {
				Thread.sleep(SAVE_SLEEP_TIME);
			} catch (InterruptedException e) {
				// ignore
			}
			SWTBotShell shell = null;
			for (SWTBotShell s : Bot.get().shells()) {
				if (s.getText().equals("Configure BPMN2 Project Nature")) {
					shell = s;
				}
			}
			if (shell != null) {
				shell.activate();
				bot.button("No").click();
			}
		} catch (WidgetNotFoundException e) {
			// ignore
		}
	}

	/**
	 * Activate a tool in a given section.
	 * 
	 * @param section
	 * @param label
	 * 
	 * @throws WidgetNotFoundException
	 */
	public void activateTool(final String section, final String label) throws WidgetNotFoundException {
		activateTool(label, Pattern.compile(Pattern.quote(section)));
	}

	/**
	 * TODO
	 * 	1) make recursive for cases when a section contains subsections.
	 * 		- when a PaletteContainer contain another PaletteContainer.
	 * 
	 * @param label
	 * @param sectionMatcher
	 */
	private void activateTool(final String label, final Pattern sectionMatcher) throws WidgetNotFoundException {
		final WidgetNotFoundException[] exception = new WidgetNotFoundException[1];
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				
				GraphicalViewer graphicalViewer = UIThreadRunnable.syncExec(new Result<GraphicalViewer>() {
					public GraphicalViewer run() {
						final IEditorPart editor = partReference.getEditor(true);
						return (GraphicalViewer) editor.getAdapter(GraphicalViewer.class);
					}
				});
				
				boolean found = false;
				final EditDomain editDomain = graphicalViewer.getEditDomain();
				final List<PaletteEntry> paletteEntries = new PaletteFinder(editDomain).findEntries(new PaletteEntryMatcher(sectionMatcher));
				if (paletteEntries.size() > 0) {
					for (int i=0; i<paletteEntries.size(); i++) {
						PaletteEntry paletteEntry = paletteEntries.get(i);
						
						// TODO - expand the section?
						
						if (paletteEntry instanceof PaletteContainer) {
							final PaletteContainer entry = (PaletteContainer) paletteEntry;
							final List<?> childEntries = entry.getChildren();
							for (int j=0; j<childEntries.size(); j++) {
								Object object = childEntries.get(j);
								if (object instanceof ToolEntry) {
									ToolEntry toolEntry = (ToolEntry) object;
									if (toolEntry.getLabel().equals(label)) {
										editDomain.getPaletteViewer().setActiveTool(toolEntry);
										
										found = true;
										break;
									}
								}
							}
						} else {
							exception[0] = new WidgetNotFoundException(String.format("%s is not a container entry, it's a %s", sectionMatcher
								.toString(), paletteEntry.getClass().getName()));
						}
					}
				} 
				
				if (!found) {
					exception[0] = new WidgetNotFoundException(label);
				}
			}
		});
		if (exception[0] != null) {
			throw exception[0];
		}
	}

//	public void clickContextMenu(String item) {
//	    // E.g. 'Local History...' which is hidden under 
//		// the 'Replace With...' menu item. the clicking 
//	    // is recursive so it will find it. 
//		//
//		// Replace With -> Local History...
//		clickContextMenu(item);
//	}
	
}
