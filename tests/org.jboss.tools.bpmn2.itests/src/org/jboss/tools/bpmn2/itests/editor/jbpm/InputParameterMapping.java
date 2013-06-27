package org.jboss.tools.bpmn2.itests.editor.jbpm;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.bpmn2.itests.editor.IMappingSide;
import org.jboss.tools.bpmn2.itests.editor.IParameterMapping;
import org.jboss.tools.bpmn2.itests.reddeer.BPMN2PropertiesView;

// -- OUTPUT ASSOCIATION --
// FROM: DataOutput
//  - Name
//  - Data State
//  - Data Type
// TO:   Variable|Transformation|...
//	- Source
//
//
// -- INTPUT ASSOCIATION --
// FROM: DataInput
// 	- Name
//	- Data State
//	- Data Type
// TO:   Variable|Transformation
//  - source
public class InputParameterMapping implements IParameterMapping {

	protected final String SECTION = "Input Parameter Mapping"; 
	
	protected BPMN2PropertiesView propertiesView = new BPMN2PropertiesView();
	protected SWTBot bot = Bot.get();
	
	protected IMappingSide from;
	protected IMappingSide to;
	
	public InputParameterMapping(IMappingSide from, IMappingSide to) {
		this.from = from;
		this.to = to;
	}
	
	@Override
	public void add() {
		bot.toolbarButtonWithTooltip("Add", propertiesView.indexOfSection(SECTION)).click();
		from.add();
		to.add();
		bot.toolbarButtonWithTooltip("Close").click();
	}

	@Override
	public void remove() {
		bot.table(0).select(from.getName());
		bot.toolbarButtonWithTooltip("Remove", propertiesView.indexOfSection(SECTION)).click();
	}
	
}
