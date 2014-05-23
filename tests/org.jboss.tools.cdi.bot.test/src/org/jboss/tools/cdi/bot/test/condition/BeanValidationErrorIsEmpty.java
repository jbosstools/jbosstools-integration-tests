package org.jboss.tools.cdi.bot.test.condition;

import java.util.List;

import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.WaitCondition;

public class BeanValidationErrorIsEmpty implements WaitCondition{
	
	private String projectName;
	
	public BeanValidationErrorIsEmpty(String projectName){
		this.projectName = projectName;
	}

	public boolean test() {
		ProblemsView pv = new ProblemsView();
		pv.open();
		List<TreeItem> errors = pv.getAllErrors();
		boolean toReturn = true;
		for(TreeItem error: errors){
			if(error.getCell(2).contains("/"+projectName) &&
				error.getCell(1).contains("beans.xml") &&
				error.getCell(4).contains("CDI Problem")) {
				toReturn = false;
			}
 		}
		return toReturn;
	}

	public String description() {
		ProblemsView pv = new ProblemsView();
		pv.open();
		List<TreeItem> errors = pv.getAllErrors();
		StringBuilder b = new StringBuilder();
		for(TreeItem i: errors){
			b.append("\n "+i.getText());
		}
		return  "Problems View doesnt contain bean xml validation error. Current Errors: "+b.toString();
	}

}
