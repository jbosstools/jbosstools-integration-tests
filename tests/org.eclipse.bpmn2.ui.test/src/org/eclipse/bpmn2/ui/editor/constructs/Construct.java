package org.eclipse.bpmn2.ui.editor.constructs;

import static org.hamcrest.Matchers.allOf;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;

import org.apache.log4j.Logger;

import org.eclipse.bpmn2.ui.awt.AWTBot;
import org.eclipse.bpmn2.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.ui.editor.ConnectionType;
import org.eclipse.bpmn2.ui.editor.ConstructType;
import org.eclipse.bpmn2.ui.editor.Position;
import org.eclipse.bpmn2.ui.editor.properties.BPMN2PropertiesView;
import org.eclipse.bpmn2.ui.swt.matcher.ConstructOfType;
import org.eclipse.bpmn2.ui.swt.matcher.ConstructWithName;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;

import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Bot;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class Construct implements IConstruct {
	
	protected String name;
	protected ConstructType type;
	
	protected BPMN2Editor editor;
	protected BPMN2PropertiesView properties;
	protected AWTBot robot;
	protected SWTBotGefEditPart editPart;
	
	protected Logger log = Logger.getLogger(getClass());
	
	/**
	 * Creates a new instance of Construct.
	 * 
	 * @param name
	 * @param type
	 */
	public Construct(String name, ConstructType type) {
		this(name, type, null, -1);
	}
	
	/**
	 * Creates a new instance of Construct. This construct must already be present
	 * in the editor. If not found base either by ${name} or by ${type} then a
	 * RuntimeException will be thrown. 
	 * 
	 * @param name
	 * @param type
	 * @param parent
	 * @param index
	 */
	public Construct(String name, ConstructType type, Construct parent, int index) {
		this.editor = new BPMN2Editor();
		this.properties = new BPMN2PropertiesView();
		this.robot  = new AWTBot();
		
		this.name = name;
		this.type = type;
		
		List<Matcher<? extends EditPart>> matchers = new ArrayList<Matcher<? extends EditPart>>();
		matchers.add(new ConstructOfType<EditPart>(type.name()));
		if (name != null) {
			matchers.add(new ConstructWithName<EditPart>(name));
		}
		
		Matcher<? extends EditPart> matcher = allOf(matchers);
		if (parent != null) {
			List<SWTBotGefEditPart> editParts = editor.getEditPart(parent.editPart, matcher);
			if (!editParts.isEmpty()) {
				editPart = editParts.get(index);
			}
		} else if (name != null) {
			editPart = editor.getEditPart(name);
		} else {
			editPart = editor.getEditPart(matcher, index);
		}
		
		if (editPart == null) {
			throw new RuntimeException("Could not find construct with name '" + name + "' of type '" + type.name() + "'");
		}
		
		select();
	}
	
	/**
	 * Set the name of this construct.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		new BPMN2PropertiesView().selectTab("Description");
		new LabeledText("Name").setText(name);
		this.name = name;
	}
	
	/**
	 * Select this construct. Select the pictogram not the label.
	 */
	public void select() {
		editor.selectEditPart(editPart);
	}
	
	
	/**
	 * 
	 * @param name
	 * @param connectionType
	 */
	public void append(String name, ConstructType constructType) {
		append(name, constructType, ConnectionType.SEQUENCE_FLOW, Position.EAST);
	}
	
	/**
	 * 
	 * @param name
	 * @param constructType
	 * @param connectionType
	 */
	public void append(String name, ConstructType constructType, ConnectionType connectionType) {
		append(name, constructType, connectionType, Position.EAST);
	}
	
	/**
	 * 
	 * @param name
	 * @param constructType
	 * @param connectionType
	 * @param relativePosition
	 */
	public void append(String name, ConstructType constructType, ConnectionType connectionType, Position relativePosition) {
		log.info("Appending construct name '" + name + "' of type '" + constructType + "' after construct with name '" + this.name + "'.");
		
		Rectangle r = editor.getBounds(editPart);
		
		int centerX = r.x + (r.width / 2);
		int centerY = r.y + (r.height / 2);
		
		int x = 0;
		int y = 0;
		
		switch (relativePosition) {
			case NORTH:
				x = centerX;
				y = r.y -100;
				break;
			case NORTH_EAST:
				x = r.x + r.width + 75;
				y = r.y - 100;
				break;
			case EAST:
				x = r.x + r.width + 100;
				y = centerY;
				break;
			case SOUTH_EAST:
				x = r.x + r.width + 75;
				y = r.y + 100;
				break;
			case SOUTH:
				x = centerX;
				y = r.y + r.height + 100;
				break;
			case SOUTH_WEST:
				x = r.x - 75;
				y = r.y + 100;
				break;
			case WEST:
				x = r.x - 100;
				y = centerY;
				break;
			case NORTH_WEST:
				x = r.x - 75;
				y = r.y - 100;
				break;
			default:
				throw new UnsupportedOperationException();
		}
		
//		switch (relativePosition) {
//			case NORTH:
//				x = centerX;
//				y = r.y + relativePosition.getYShift();
//				break;
//			case NORTH_EAST:
//				x = r.x + r.width + relativePosition.getXShift();
//				y = r.y + relativePosition.getYShift();
//				break;
//			case SOUTH:
//				x = centerX;
//				y = r.y + r.height + relativePosition.getYShift();
//				break;
//			case WEST:
//				x = r.x + relativePosition.getXShift();
//				y = centerY;
//				break;
//			case EAST:
//				x = r.x + r.width + relativePosition.getXShift();
//				y = centerY;
//				break;
//			case WEST_NORTH:
//				x = r.x + relativePosition.getXShift();
//				y = r.y + relativePosition.getYShift();
//			default:
//				throw new UnsupportedOperationException();
//		}
		
		editor.activateTool(constructType.toToolName());
		editor.click(x, y);
		
		Construct construct = editor.getLastConstruct(constructType);
		if (construct == null) throw new RuntimeException("Unexpected error. Could not find added construct.");
		if (name != null) construct.setName(name);
		
		connectTo(construct, connectionType);
	}
	
	/**
	 * 
	 * Connect this construct to another one using a connector
	 * of type ${type}.
	 * 
	 * @param construct
	 * @param connection
	 */
	public void connectTo(Construct construct, ConnectionType connectionType) {
		log.info("Connecting construct name '" + this.name + "' and construct '" + construct.getName() + "'.");

		/*
		 * Get the dimensions of the source (this) construct. 
		 */
		Rectangle rs = editor.getBounds(editPart);
		/*
		 * Get the dimensions of the target construct. 
		 */
		Rectangle rt = editor.getBounds(construct.editPart);
		
		editor.activateTool(connectionType.toName());
		editor.click(rs.x + (rs.width / 2), rs.y + (rs.height / 2));
		editor.click(rt.x + (rt.width / 2), rt.y + (rt.height / 2));
		editor.activateTool("Select");
	}
	
	/**
	 * 
	 * @param construct
	 */
	public void connectTo(Construct construct) {
		connectTo(construct, ConnectionType.SEQUENCE_FLOW);
	}
	
	/**
	 * Delete this construct. Cannot be undone.
	 */
	public void delete() {
		select();
		editor.clickContextMenu("Delete");
	}
	
//	/**
//	 * Add a new construct with ${name} and ${constructType} to
//	 * the editor.
//	 * 
//	 * @param name
//	 * @param constructType
//	 */
//	public void contextMenuAppend(String name, ConstructType constructType) {
//		contextMenuAppend(name, constructType, ConnectionType.SEQUENCE_FLOW);
//	}
//	
//	/**
//	 * Add a new construct to the to this one connected using a
//	 * connector of type ${connectionType}.
//	 * 
//	 * @param name
//	 * @param constructType
//	 * @param connectionType
//	 */
//	public void contextMenuAppend(String name, ConstructType constructType, ConnectionType connectionType) {
//		editor.selectEditPart(editPart, true);
//		editor.clickContextMenu(constructType.getCategory().toName());
//		for (int i=0; i<constructType.indexOf() + 1; i++) robot.type(KeyEvent.VK_DOWN);
//		robot.type(KeyEvent.VK_ENTER);
//
//		Construct construct = editor.getLastConstructByLabelName(constructType);
//		if (construct == null) throw new RuntimeException("Unexpected error. Could not find added construct.");
//		if (name != null) construct.setName(name);
//		
//		connectTo(new Construct(name, constructType), connectionType);
//	}
	
	/**
	 * Validate this construct.
	 * 
	 * @return
	 */
	public boolean validate() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @return
	 */
	public SWTBotGefEditPart getEditPart() {
		return editPart;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Helper.
	 */
	public void printShells() {
		for (SWTBotShell s : Bot.get().shells()) {
			System.out.println(" --- SHELL: " + s);
		}
	}
	
}
