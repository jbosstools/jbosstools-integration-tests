package org.jboss.tools.cdi.reddeer.uiutils;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.ui.views.markers.QuickFixPage;
import org.jboss.reddeer.eclipse.ui.views.markers.QuickFixWizard;
import org.jboss.tools.cdi.reddeer.validators.ValidationProblem;

public class ValidationHelper {
	
	private class SpecificProblemExists extends AbstractWaitCondition {
		
		private ValidationProblem validationProblem;
		
		public SpecificProblemExists(ValidationProblem validationProblem){
			this.validationProblem = validationProblem;
		}

		@Override
		public boolean test() {
			ProblemsView pw = new ProblemsView();
			pw.open();
			List<Problem> problems = pw.getProblems(ProblemType.ANY);
			List<Problem> foundProblems = new ArrayList<Problem>();
			for(Problem p: problems){
				try{
					if(validationProblem.equals(p)){
						foundProblems.add(p);
					}
				} catch (CoreLayerException l){
					//do nothing, problem was probably disposed
				}
			}
			return !foundProblems.isEmpty();
		}

		@Override
		public String description() {
			return "validation problem is found";
		}
		
		
	
	}
	
	public List<Problem> findProblems(ValidationProblem validationProblem){
		new WaitUntil(new SpecificProblemExists(validationProblem),TimePeriod.NORMAL,false);
		ProblemsView pw = new ProblemsView();
		pw.open();
		List<Problem> problems = pw.getProblems(ProblemType.ANY);
		List<Problem> foundProblems = new ArrayList<Problem>();
		for(Problem p: problems){
			try{
				if(validationProblem.equals(p)){
					foundProblems.add(p);
				}
			} catch (RedDeerException e) {
				// disposed
			}
		}
		return foundProblems;
	}
	
	public void openQuickfix(ValidationProblem validationProblem){
		openQuickfix(validationProblem,0);
	}
	
	public void openQuickfix(ValidationProblem validationProblem, int quickfixIndex){
		List<Problem> foundProblems = findProblems(validationProblem);
		QuickFixWizard qf = foundProblems.get(0).openQuickFix();
		QuickFixPage qp = new QuickFixPage();
		List<String> fixes = qp.getAvailableFixes();
		String chosenFix = null;
		String proposedFix = validationProblem.getQuickFixes().get(quickfixIndex);
		if(proposedFix == null || proposedFix.isEmpty()){
			fail("No quickfix is defined");
		}
		for(String fix: fixes){
			if(fix.contains(validationProblem.getQuickFixes().get(quickfixIndex))){
				chosenFix = fix;
				break;
			}
		}
		if(chosenFix == null){
			fail("Unable to find proper quickfix");
		}
		qp.selectFix(chosenFix);
		qf.finish();
	}
}
