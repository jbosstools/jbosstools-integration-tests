package org.jboss.tools.bpel.ui.bot.test.suite;

import junit.framework.TestSuite;

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
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
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
public class BPELExampleTest extends TestSuite {

}
