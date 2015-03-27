package org.jboss.tools.cdi.bot.test.condition;

import java.util.List;

import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.swt.exception.SWTLayerException;

public class BeanXMLValidationProblemIsEmpty implements WaitCondition{
	
	private String projectName;
	
	public BeanXMLValidationProblemIsEmpty(String projectName){
		this.projectName = projectName;
	}

	public boolean test() {
		ProblemsView pv = new ProblemsView();
		pv.open();
		List<Problem> problems = pv.getProblems(ProblemType.ANY);
		boolean toReturn = true;
		for(Problem problem: problems){
			if(problem.getPath().contains(projectName) &&
				problem.getResource().contains("beans.xml") &&
				problem.getType().contains("CDI Problem")) {
				toReturn = false;
			}
 		}
		return toReturn;
	}

	public String description() {
		ProblemsView pv = new ProblemsView();
		pv.open();
		List<Problem> problems = pv.getProblems(ProblemType.ANY);
		StringBuilder b = new StringBuilder();
		for(Problem p: problems){
			try{
				b.append("\n "+p.getDescription());
			} catch (SWTLayerException ex){
				//do nothing. Problem is not there anymore
			}
		}
		return  "Problems View doesnt contain bean xml validation error. Current Errors: "+b.toString();
	}

}
