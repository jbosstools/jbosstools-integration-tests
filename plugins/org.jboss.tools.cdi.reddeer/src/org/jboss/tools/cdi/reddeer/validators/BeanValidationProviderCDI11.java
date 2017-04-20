package org.jboss.tools.cdi.reddeer.validators;

import java.util.Arrays;

import org.jboss.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.jboss.tools.cdi.reddeer.annotation.ValidationType;

public class BeanValidationProviderCDI11 extends AbstractValidationProvider{
	
	private final String jsr = "JSR-346"; 
	
	public BeanValidationProviderCDI11(){
		
	}

	@Override
	void init() {
		problems.add(new ValidationProblem(ProblemType.WARNING, ValidationType.NO_BEAN_ELIGIBLE,
				"No bean is eligible for injection to the injection point", jsr, null));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.OBSERVER_INJECT, 
				"Observer method cannot be annotated @Inject", jsr, Arrays.asList(
				"Delete annotation @Inject from method","Delete annotation @Observes from parameter")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.OBSERVER_DISPOSES, 
				"Observer method has a parameter annotated @Disposes", jsr, Arrays.asList(
				"Delete annotation @Disposes from parameter","Delete annotation @Observes from parameter")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.PRODUCER_DISPOSES, 
				"Producer method has a parameter annotated @Disposes", jsr, Arrays.asList(
				"Delete annotation @Disposes from parameter","Delete annotation @Produces from method")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.PRODUCER_OBSERVES, 
				"Producer method has a parameter annotated @Observes", jsr, Arrays.asList(
				"Delete annotation @Observes from parameter","Delete annotation @Produces from method")));
		
		problems.add(new ValidationProblem(ProblemType.WARNING, ValidationType.SERIALIZABLE, 
				"which declares a passivating scope SessionScoped must be passivation capable",jsr,
				Arrays.asList("Add java.io.Serializable interface to class")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.PRODUCER_INJECT, 
				"Producer method or field cannot be annotated @Inject",jsr,Arrays.asList(
				"Delete annotation @Inject from method","Delete annotation @Produces from method")));	
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.CONSTRUCTOR_DISPOSES, 
				"Bean constructor cannot have a parameter annotated @Disposes",jsr,Arrays.asList(
				"Delete annotation @Disposes from parameter")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.CONSTRUCTOR_OBSERVES, 
				"Bean constructor cannot have a parameter annotated @Observes",jsr,Arrays.asList(
				"Delete annotation @Observes from parameter")));
		
		problems.add(new ValidationProblem(ProblemType.ERROR, ValidationType.DISPOSER_INJECT, 
				"Disposer method cannot be annotated @Inject",jsr,Arrays.asList(
				"Delete annotation @Disposes from parameter","Delete annotation @Inject from method")));
		
	}

}
