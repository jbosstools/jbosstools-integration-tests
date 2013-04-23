package org.eclipse.bpmn2.ui.editor.constructs;

import org.eclipse.bpmn2.ui.editor.ConnectionType;
import org.eclipse.bpmn2.ui.editor.ConstructType;
import org.eclipse.bpmn2.ui.editor.Position;

/**
 * ISSUES: 
 *   1) Drill down does not work properly.
 *   	+ After a activity is added in the tab (drill down) the view will
 *        automatically be switched to the main process (back).
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class ContainerConstruct extends Construct {

	/**
	 * 
	 * @param name
	 * @param type
	 */
	public ContainerConstruct(String name, ConstructType type) {
		this(name, type, null, -1);
	}
	
	/**
	 * 
	 * @param name
	 * @param type
	 * @param parent
	 * @param index
	 */
	public ContainerConstruct(String name, ConstructType type, Construct parent, int index) {
		super(name, type, parent, index);
	}

	/**
	 * 
	 * @param name
	 * @param connectionType
	 */
	public void add(String name, ConstructType constructType) {
		add(name, constructType, ConnectionType.SEQUENCE_FLOW, Position.EAST);
	}
	
	/**
	 * 
	 * @param name
	 * @param constructType
	 * @param connectionType
	 */
	public void add(String name, ConstructType constructType, ConnectionType connectionType) {
		add(name, constructType, connectionType, Position.EAST);
	}
	
	/**
	 * 
	 * @param name
	 * @param constructType
	 * @param relativePosition
	 */
	public void add(String name, ConstructType constructType, ConnectionType connectionType, Position relativePosition) {
		// TBD
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 
	 */
	public void pushDown() {
		editor.clickContextMenu("Push down");
	}
	
	/**
	 * 
	 */
	public void undoPushDown() {
		editor.clickContextMenu("Undo Push down");
	}
}
