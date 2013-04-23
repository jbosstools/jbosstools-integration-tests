package org.jboss.tools.bpmn2.itests.editor.constructs;

import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.swt.finder.SWTBot;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.bpmn2.itests.editor.BPMN2Editor;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.properties.BPMN2PropertiesView;


/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class Process implements IProcess, IConstruct {

	protected BPMN2Editor editor;
	protected BPMN2PropertiesView properties;
	
	protected SWTBot bot;
	
	protected String name;
	
	/**
	 * Creates a new instance of Process.
	 */
	public Process() {
		this.name = "No Name";
		
		this.editor     = new BPMN2Editor();
		this.properties = new BPMN2PropertiesView();
		
		this.bot = Bot.get();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * ISSUES:
	 * 	1) Set the name of the process changes description! Changing the description
	 *     does not change the name of the process!
	 * 
	 * @param name
	 */
	public void setName(String name) {
		properties.selectTab("Description");
		new LabeledText("Name").setText(name);
		
		this.name = name;
	}
	
	/**
	 * Select the process. This is the blank are around all the BPMN2 constructs.
	 */
	public void select() {
		editor.click(editor.rootEditPart());
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
	 * @param b
	 */
	public void setExecutable(boolean b) {
		properties.selectTab("Process");
		properties.selectCheckBox(new CheckBox("Is Executable"), b);
	}
	
 }
