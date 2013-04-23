package org.jboss.tools.bpmn2.itests.editor.constructs.eventdefinitions;

import org.jboss.tools.bpmn2.itests.editor.constructs.AbstractEventDefinition;

/**
 * 
 * @author
 */
public class LinkEventDefinition extends AbstractEventDefinition {

	private String linkName;
	
	private String source;
	
	private String target;
	
	public LinkEventDefinition(String linkName, String source, String target) {
		this(linkName, source, target, 0);
	}
	
	public LinkEventDefinition(String linkName, String source, String target, int index) {
		super("Link Event Definition", index);
		
		this.linkName = linkName;
		this.source = source;
		this.target = target;
	}
	
	/**
	 * TBD: set attributes
	 */
	@Override
	protected void setUpDefinition() {
		System.out.println(linkName + "[" + source + " -> " + target + "]");
		
		bot.toolbarButtonWithTooltip("Add").click();
		bot.table().select("Link Event Definition");
		bot.button("OK").click();
		bot.toolbarButtonWithTooltip("Close").click();		
	}
}
