package org.jboss.tools.bpel.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.tools.bpel.ui.bot.test.ActivityModelingTest;
import org.jboss.tools.bpel.ui.bot.test.AssignActivityTest;
import org.jboss.tools.bpel.ui.bot.test.AssociateRuntimeTest;
import org.jboss.tools.bpel.ui.bot.test.BPELTest;
import org.jboss.tools.bpel.ui.bot.test.FaultModelingTest;
import org.jboss.tools.bpel.ui.bot.test.OdeDeployTest;
import org.jboss.tools.bpel.ui.bot.test.SimpleModelingTest;
import org.jboss.tools.bpel.ui.bot.test.ToolingCompatibilityTest;
import org.jboss.tools.bpel.ui.bot.test.WizardTest;
import org.jboss.tools.bpel.ui.bot.test.examples.BluePrint1ExampleTest;
import org.jboss.tools.bpel.ui.bot.test.examples.BluePrint2ExampleTest;
import org.jboss.tools.bpel.ui.bot.test.examples.BluePrint3ExampleTest;
import org.jboss.tools.bpel.ui.bot.test.examples.BluePrint4ExampleTest;
import org.jboss.tools.bpel.ui.bot.test.examples.BluePrint5ExampleTest;
import org.jboss.tools.bpel.ui.bot.test.examples.CorrelationExampleTest;
import org.jboss.tools.bpel.ui.bot.test.examples.FaultCompensationExampleTest;
import org.jboss.tools.bpel.ui.bot.test.examples.HelloWorldExampleTest;
import org.jboss.tools.bpel.ui.bot.test.examples.HelloWorldOdeExampleTest;
import org.jboss.tools.bpel.ui.bot.test.examples.HelloWorldWsdlExampleTest;
import org.jboss.tools.bpel.ui.bot.test.examples.LoanApprovalExampleTest;
import org.jboss.tools.bpel.ui.bot.test.examples.MathExampleTest;
import org.jboss.tools.bpel.ui.bot.test.examples.SalutationsExampleTest;
import org.jboss.tools.bpel.ui.bot.test.examples.SayHelloExampleTest;
import org.jboss.tools.bpel.ui.bot.test.examples.ServiceHandlerExampleTest;
import org.jboss.tools.bpel.ui.bot.test.examples.SimpleInvokeExampleTest;
import org.jboss.tools.bpel.ui.bot.test.examples.SimplePickExampleTest;
import org.jboss.tools.bpel.ui.bot.test.examples.WhileWaitExampleTest;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
		OdeDeployTest.class,
		WizardTest.class,
		ActivityModelingTest.class,
		AssignActivityTest.class,
		SimpleModelingTest.class,
		FaultModelingTest.class,
		ToolingCompatibilityTest.class,
		AssociateRuntimeTest.class,
		HelloWorldExampleTest.class, 
		HelloWorldOdeExampleTest.class,
		HelloWorldWsdlExampleTest.class, 
		LoanApprovalExampleTest.class, 
		MathExampleTest.class,
		SalutationsExampleTest.class, 
		SayHelloExampleTest.class, 
		ServiceHandlerExampleTest.class,
		CorrelationExampleTest.class,
		SimpleInvokeExampleTest.class,
		SimplePickExampleTest.class,
		BluePrint1ExampleTest.class, 
		BluePrint2ExampleTest.class, 
		BluePrint3ExampleTest.class,
		BluePrint4ExampleTest.class, 
		BluePrint5ExampleTest.class,
		FaultCompensationExampleTest.class,
		WhileWaitExampleTest.class
		})
@RunWith(RequirementAwareSuite.class)
public class BPELAllTest extends TestSuite {

	@BeforeClass
	public static void setUpSuite() {
		BPELTest.prepare();
	}

	@AfterClass
	public static void tearDownSuite() {
		BPELTest.clean();
	}
}
