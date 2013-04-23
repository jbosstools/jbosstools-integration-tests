package org.jboss.tools.bpmn2.itests.editor.constructs;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.util.Bot;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public abstract class AbstractEventDefinition {
	
	private int index;
	
	private String name;

	protected SWTBot bot;
	
	/**
	 * Creates a new instance of EventDefinition.
	 * 
	 * @param name
	 */
	public AbstractEventDefinition(String name) {
		this(name, 0);
	}

	/**
	 * Creates a new instance of EventDefinition.
	 * 
	 * @param name
	 * @param index
	 */
	public AbstractEventDefinition(String name, int index) {
		this.name = name;
		this.index = index;
		
		this.bot = Bot.get();
	}
	
	/**
	 * Add this event definition to a event.
	 * 
	 * @param event
	 */
	public void add(AbstractEvent event) {
		/*
		 * Select correct tab
		 */
		event.select();
		event.getProperties().selectTab("Event");
		/*
		 * Add event definition
		 */
		new PushButton(index).click();
		new DefaultTable().select(name);
		new PushButton("OK").click();
		/*
		 * Set up the event
		 */
		setUpDefinition();
	}
	
	/**
	 * Remove this event definition from a event.
	 * 
	 * @param event
	 */
	public void remove(AbstractEvent event) {
		event.getProperties().selectTab("Event");
		new DefaultTable(0).select(name, 0);
		Bot.get().toolbarButtonWithTooltip("Remove").click();
	}
	
	/**
	 * 
	 */
	protected abstract void setUpDefinition();
	
}
