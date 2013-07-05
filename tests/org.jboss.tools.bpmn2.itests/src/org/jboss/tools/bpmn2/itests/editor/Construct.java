package org.jboss.tools.bpmn2.itests.editor;

import static org.hamcrest.Matchers.allOf;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.hamcrest.Matcher;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.bpmn2.itests.reddeer.BPMN2Editor;
import org.jboss.tools.bpmn2.itests.reddeer.BPMN2PropertiesView;
import org.jboss.tools.bpmn2.itests.swt.matcher.ConstructOfType;
import org.jboss.tools.bpmn2.itests.swt.matcher.ConstructOnPoint;
import org.jboss.tools.bpmn2.itests.swt.matcher.ConstructWithName;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class Construct {
	
	protected String name;
	protected ConstructType type;
	protected Construct parent;
	
	protected BPMN2Editor editor;
	protected BPMN2PropertiesView properties;
	protected SWTBot bot;
	
	protected SWTBotGefEditPart editPart;
	
	protected Logger log = Logger.getLogger(getClass());
	
	/*
	 * TBD: 
	 * 	1) make parent mandatory.
	 * 	2) if not passed in as parameter then make Process construct the default parent.
	 * 		- detect using the editor and file name.
	 */
	/**
	 * Creates a new instance of Construct.
	 * 
	 * @param name
	 * @param type
	 */
	public Construct(String name, ConstructType type) {
		this(name, type, null, -1, true);
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
	 * @param select
	 */
	public Construct(String name, ConstructType type, Construct parent, int index, boolean select) {
		this.editor = new BPMN2Editor();
		this.properties = new BPMN2PropertiesView();
		this.bot = Bot.get();
		
		this.name = name;
		this.type = type;
		this.parent = parent;
		
		Matcher<EditPart>/*<? extends EditPart>*/ matcher1 = new ConstructOfType<EditPart>(type.name());
		Matcher<EditPart>/*<? extends EditPart>*/ allMatcher = allOf(matcher1, new ConstructWithName<EditPart>(name));
		
		if (parent != null) {
			List<SWTBotGefEditPart> editParts = editor.getEditParts(parent.editPart, allMatcher);
			if (!editParts.isEmpty()) {
				editPart = editParts.get(index);
			}
		} else if (name != null) {
			editPart = editor.getEditPart(name);
		} else {
			editPart = editor.getEditPart(allMatcher, index);
		}
		
		if (editPart == null) {
			throw new RuntimeException("Could not find construct with name '" + name + "' of type '" + type.name() + "'");
		}
		
		if (select) {
			select();
		}
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
	 * Add a boundary event to this construct.
	 *  
	 * @param name
	 * @param eventType
	 */
	protected void addEvent(String name, ConstructType eventType) {
		if (!eventType.toString().endsWith("BOUNDARY_EVENT")) {
			throw new IllegalArgumentException("Can add only BOUNDARY_EVENT types.");
		}

		/*
		 * Add the event
		 */
		Point p = editor.getBounds(editPart).getTopLeft();
		editor.activateTool(eventType.toToolPath()[0], eventType.toToolPath()[1]);
		editor.click(p.x(), p.y());
		
		/*
		 * Set the name of the event
		 */
		Construct event = editor.getLastConstruct(eventType);
		event.setName(name);
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
	 * @param position
	 */
	public void append(String name, ConstructType constructType, Position position) {
		append(name, constructType, ConnectionType.SEQUENCE_FLOW, position);
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
		
		Point point = findPoint(parent, this, relativePosition);
		if (!isAvailable(point)) {
			throw new RuntimeException(point + " is not available");
		}
		
		editor.activateTool(constructType.toToolPath()[0], constructType.toToolPath()[1]);
		editor.click(point.x(), point.y());
			
		Construct construct = editor.getLastConstruct(constructType);
		if (construct == null) throw new RuntimeException("Unexpected error. Could not find added construct.");
		if (name != null) construct.setName(name);
		
		connectTo(construct, connectionType);
	}
	
	/**
	 * 
	 * @param construct
	 */
	public void connectTo(Construct construct) {
		connectTo(construct, ConnectionType.SEQUENCE_FLOW);
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
		log.info("Connecting construct '" + this.name + "' and construct '" + construct.getName() + "' using '" + connectionType + "'.");
		/*
		 * Get the dimensions of the source (this) construct. 
		 */
		Rectangle rs = editor.getBounds(editPart);
		/*
		 * Get the dimensions of the target construct. 
		 */
		Rectangle rt = editor.getBounds(construct.editPart);
		/*
		 * Create the connection.
		 * 
		 * Bring forward elements in case they are covered by another bigger
		 * element. Then perform the clicks.
		 */
		editor.activateTool(connectionType.toName());
		
		select();
		log.info("\tClicking on points:");
		log.info("\t\tPoint 1)'" + rs.getCenter() + "'");
		editor.click(rs.getCenter().x(), rs.getCenter().y());
		
		construct.select();
		log.info("\t\tPoint 2)'" + rt.getCenter() + "'");
		editor.click(rt.getCenter().x(), rt.getCenter().y());
		editor.activateTool("Select");
	}
	
	/**
	 * Delete this construct. Cannot be undone.
	 */
	public void delete() {
		select();
		editor.clickContextMenu("Delete");
	}
	
	/**
	 * 
	 * @param point
	 * @return
	 */
	protected boolean isAvailable(Point point) {
		/*
		 * Check weather the point is not already taken by another child editPart.
		 */
		return editor.getEditParts(editPart.parent(), new ConstructOnPoint<EditPart>(point)).isEmpty();
	}
	
	/**
	 * Get a point to which a child can be added inside a parent.
	 * 
	 * @param parent
	 * @param child
	 * @param position
	 * @return
	 */
	protected Point findPoint(Construct parent, Construct child, Position position) {
		/*
		 * Initialize variables.
		 */
		Rectangle childRectangle = editor.getBounds(child.getEditPart());
		
		int childStartX = childRectangle.x();
		int childEndX = childRectangle.right();
		
		int childStartY = childRectangle.y();
		int childEndY = childRectangle.bottom();
		
		int childCenterX = childRectangle.getCenter().x();
		int childCenterY = childRectangle.getCenter().y();
		
		Point point = new Point(-1, -1);
		/*
		 * Assign 'x' and 'y'.
		 */
		switch (position) {
			case NORTH:
				point.x = childCenterX;
				point.y = childStartY -100;
				break;
			case NORTH_EAST:
				point.x = childEndX + 75;
				point.y = childStartY - 100;
				break;
			case EAST:
				point.x = childEndX + 100;
				point.y = childCenterY;
				break;
			case SOUTH_EAST:
				point.x = childEndX  + 75;
				point.y = childEndY + 100;
				break;
			case SOUTH:
				point.x = childCenterX;
				point.y = childEndY + 100;
				break;
			case SOUTH_WEST:
				point.x = childStartX - 75;
				point.y = childEndY + 100;
				break;
			case WEST:
				point.x = childStartX - 100;
				point.y = childCenterY;
				break;
			case NORTH_WEST:
				point.x = childStartX - 75;
				point.y = childStartY - 100;
				break;
			default:
				throw new UnsupportedOperationException();
		}
		/*
		 * Check parent bounds. In case the point (marked as '+') is out of bounds.
		 * 	- '*'  signals a properly added construct.
		 * 	- '->' marks a connection between to '*'
		 * 
		 *     +
		 *    ________
		 * + |        |
		 *   |        | +
		 *   | *->*   |
		 *   |________|
		 *       +
         *
		 */
		
		if (parent != null) {
		
			Rectangle parentRectangle = editor.getBounds(parent.getEditPart());
			
			int parentStartX = parentRectangle.x();
			int parentEndX = parentRectangle.right();
			
			int parentStartY = parentRectangle.y();
			int parentEndY = parentRectangle.bottom();
			
			if (point.x < parentStartX) point.x = parentStartX;
			if (point.x > parentEndX) point.x = parentEndX;
			if (point.y < parentStartY) point.y = parentStartY;
			if (point.y > parentEndY) point.y = parentEndY;
		}
		/*
		 * Return
		 */
		return point;
	}
	
	/**
	 * Validate this construct. At this moment only attributes are validated. Element structure
	 * is validated using a XSD schema via the BPMN2Validator and or the JBPM6Validator.
	 * 
	 * @param attributes expected attribute names
	 * @param values     expected attribute values
	 * 
	 * @return
	 */
	public List<String> validate(String[] attributes, String[] values) {
		log.info("Validating construct '" + name + "' of type '" + type + "'");
		/*
		 * check bounds.
		 */
		if (attributes.length != values.length) {
			throw new RuntimeException("Attribute names size '" + attributes.length + "' is not equal to attribute values size '" + values.length + "'");
		}
		/*
		 * validate attributes
		 */
		List<String> errors = new ArrayList<String>();
		Element e = getEditPartElement();
		for (int i=0; i<attributes.length; i++) {
			String attributeName = attributes[i];
			String actualValue = e.getAttribute(attributeName);
			String expectedValue = values[i];
			
			if (actualValue == null || actualValue.isEmpty()) {
				errors.add("Missing attribute '" + attributeName + "'");
			}
			
			if (!expectedValue.equals(actualValue)) {
				errors.add("Expected atribute '" + attributeName + "' to have value '" + expectedValue + "'." +
						"Found value '" + actualValue + "' instead.");
			}
		}
		
		return errors;
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
	public Element getEditPartElement() {
		/*
		 * required to store the code to xml
		 */
		editor.save();
		/*
		 * find the element
		 */
		try {
			InputStream inputStream = new ByteArrayInputStream(editor.getSourceText().getBytes());		
			Document xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
					
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			XPathExpression xPathExpression = xPath.compile("//*[@name='" + name + "']");
			
			NodeList nodeList = (NodeList) xPathExpression.evaluate(xmlDocument, XPathConstants.NODESET);
			
			if (nodeList.getLength() == 0 || nodeList.getLength() > 1) {
				throw new RuntimeException("Found '" + nodeList.getLength() + "' nodes with name '" + name + "'. Expected exactly '1'."); 
			}

			return (Element) nodeList.item(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @return
	 */
	public BPMN2Editor getEditor() {
		return editor;
	}
	
	/**
	 * 
	 * @return
	 */
	public BPMN2PropertiesView getProperties() {
		return properties;
	}
	
}
