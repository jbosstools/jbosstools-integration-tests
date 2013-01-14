package org.jboss.tools.deltaspike.ui.bot.test;

import org.jboss.reddeer.eclipse.jface.preference.FoldingPreferencePage;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RequirementAwareSuite.class)
@SuiteClasses({
	ValidationsInPreferenceTest.class,
	ExcludesAnnotationTest.class,
	ConfigPropertyAnnotationTest.class,
	MessageBundleAnnotationTest.class,
	MessageContextAnnotationTest.class,
	ExceptionHandlerAnnotationTest.class,
	ExceptionHandlerMethodsTest.class,
	
	/** not implemented yet
	 *
	 * SequresAnnotationTest.class,
	 * SequredAnnotationTest.class,
	 * SecurityBindingTypeAnnotationTest.class,
	 * SecurityParameterBindingAnnotationTest.class
	**/
})
public class DeltaspikeAllBotTests {
	
	@BeforeClass
	public static void disableFolding() {
		FoldingPreferencePage foldingPreferecePage = 
				new FoldingPreferencePage();
		foldingPreferecePage.open();
		foldingPreferecePage.disableFolding();
		foldingPreferecePage.ok();
	}
	
}
