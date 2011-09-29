package org.jboss.tools.bpel.ui.bot.test.assertion;

import java.util.Properties;

import org.apache.log4j.Logger;

import org.eclipse.bpel.model.impl.AssignImpl;
import org.eclipse.bpel.model.impl.CompensateImpl;
import org.eclipse.bpel.model.impl.CompensateScopeImpl;
import org.eclipse.bpel.model.impl.CompensationHandlerImpl;
import org.eclipse.bpel.model.impl.ElseIfImpl;
import org.eclipse.bpel.model.impl.ElseImpl;
import org.eclipse.bpel.model.impl.EmptyImpl;
import org.eclipse.bpel.model.impl.ExitImpl;
import org.eclipse.wst.wsdl.internal.impl.ExtensibleElementImpl;
import org.eclipse.bpel.model.impl.FaultHandlerImpl;
import org.eclipse.bpel.model.impl.FlowImpl;
import org.eclipse.bpel.model.impl.ForEachImpl;
import org.eclipse.bpel.model.impl.IfImpl;
import org.eclipse.bpel.model.impl.InvokeImpl;
import org.eclipse.bpel.model.impl.OnAlarmImpl;
import org.eclipse.bpel.model.impl.OnMessageImpl;
import org.eclipse.bpel.model.impl.PickImpl;
import org.eclipse.bpel.model.impl.ReceiveImpl;
import org.eclipse.bpel.model.impl.RepeatUntilImpl;
import org.eclipse.bpel.model.impl.ReplyImpl;
import org.eclipse.bpel.model.impl.RethrowImpl;
import org.eclipse.bpel.model.impl.ScopeImpl;
import org.eclipse.bpel.model.impl.SequenceImpl;
import org.eclipse.bpel.model.impl.ThrowImpl;
import org.eclipse.bpel.model.impl.ValidateImpl;
import org.eclipse.bpel.model.impl.WaitImpl;
import org.eclipse.bpel.model.impl.WhileImpl;

import org.junit.Assert;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author mbaluch
 */
public class BPELValidator {

	Logger log = Logger.getLogger(BPELValidator.class);
	

	/** Creates a new instanceof BPELValidator */
	public BPELValidator() {
	}
	
	// TODO: test me
	public void validate(AssignImpl mAssign, Properties expected) {
		log(mAssign);
		validateElementName(mAssign.getElement(), "bpel:assign");
		validateElementAttribute(mAssign.getElement(), "name", expected.getProperty("name"));
		NodeList copyNodes = mAssign.getElement().getElementsByTagName("bpel:copy");
		Assert.assertTrue(copyNodes.getLength() > 0);
		for(int i=0; i<copyNodes.getLength(); i++) {
			Element copy = (Element) mAssign.getElement().getElementsByTagName("bpel:copy").item(i);
			Assert.assertNotNull(copy);
			Assert.assertTrue(copy.getElementsByTagName("bpel:from").getLength() == 1);
			Assert.assertTrue(copy.getElementsByTagName("bpel:to").getLength() == 1);
		}
	}
	
	public void validate(InvokeImpl  mInvoke, Properties expected) {
		log(mInvoke);
		validateElementName(mInvoke.getElement(), "bpel:invoke");
		validateElementAttribute(mInvoke.getElement(), "name", expected.getProperty("name"));
		validateElementAttribute(mInvoke.getElement(), "operation", expected.getProperty("operation"));
		validateElementAttribute(mInvoke.getElement(), "partnerLink", expected.getProperty("partnerLink"));
		validateElementAttribute(mInvoke.getElement(), "inputVariable", expected.getProperty("inputVariable"));
		validateElementAttribute(mInvoke.getElement(), "outputVariable", expected.getProperty("outputVariable"));
	} 
	
	public void validate(ReceiveImpl mReceive, Properties expected) {
		log(mReceive);
		validateElementName(mReceive.getElement(), "bpel:receive");
		validateElementAttribute(mReceive.getElement(), "name", expected.getProperty("name"));
		validateElementAttribute(mReceive.getElement(), "operation", expected.getProperty("operation"));
		validateElementAttribute(mReceive.getElement(), "partnerLink", expected.getProperty("partnerLink"));
		validateElementAttribute(mReceive.getElement(), "variable", expected.getProperty("variable"));
		if(expected.getProperty("createInstance") != null) {
			validateElementAttribute(mReceive.getElement(), "createInstance", "yes");
		}
	}
	
	public void validate(ReplyImpl mReply, Properties expected) {
		log(mReply);
		validateElementName(mReply.getElement(), "bpel:reply");
		validateElementAttribute(mReply.getElement(), "name", expected.getProperty("name"));
		validateElementAttribute(mReply.getElement(), "operation", expected.getProperty("operation"));
		validateElementAttribute(mReply.getElement(), "partnerLink", expected.getProperty("partnerLink"));
		validateElementAttribute(mReply.getElement(), "variable", expected.getProperty("variable"));
	}
	
	public void validate(IfImpl mIf, Properties expected) {
		log(mIf);
		validateElementName(mIf.getElement(), "bpel:if");
		validateElementAttribute(mIf.getElement(), "name", expected.getProperty("name"));
		Element condition = (Element) mIf.getElement().getElementsByTagName("bpel:condition").item(0);
		validateElementValue(condition, expected.getProperty("condition"));
	}
	
	public void validate(ElseIfImpl mElseIf, Properties expected) {
		log(mElseIf);
		validateElementName(mElseIf.getElement(), "bpel:elseif");
		Element condition = (Element) mElseIf.getElement().getElementsByTagName("bpel:condition").item(0);
		validateElementValue(condition, expected.getProperty("condition"));
	}

	public void validate(ElseImpl mElse, Properties expected) {
		log(mElse);
		validateElementName(mElse.getElement(), "bpel:else");
	}

	public void validate(ValidateImpl mValidate, Properties expected) {
		log(mValidate);
		validateElementName(mValidate.getElement(), "bpel:validate");
		validateElementAttribute(mValidate.getElement(), "name", expected.getProperty("name"));
		
		// validate content
		String actual = mValidate.getElement().getAttribute("variables");
		String[] expectedVars = expected.getProperty("variables").split(" ");
		for(String var : expectedVars) {
			Assert.assertTrue(actual.contains(var));
			// remove ${var} from the string. At the end we should have only an empty string
			actual = actual.replace(var, "");
		}
		Assert.assertEquals(actual.trim(), "");
	}
	
	public void validate(EmptyImpl mEmpty, Properties expected) {
		log(mEmpty);
		validateElementName(mEmpty.getElement(), "bpel:empty");
		validateElementAttribute(mEmpty.getElement(), "name", expected.getProperty("name"));
	}
	
	public void validate(PickImpl mPick, Properties expected) {
		log(mPick);
		validateElementName(mPick.getElement(), "bpel:pick");
		validateElementAttribute(mPick.getElement(), "name", expected.getProperty("name"));
	}
	
	public void validate(OnMessageImpl mOnMessage, Properties expected) {
		log(mOnMessage);
		validateElementName(mOnMessage.getElement(), "bpel:onMessage");
		validateElementAttribute(mOnMessage.getElement(), "operation", expected.getProperty("operation"));
		validateElementAttribute(mOnMessage.getElement(), "partnerLink", expected.getProperty("partnerLink"));
		validateElementAttribute(mOnMessage.getElement(), "variable", expected.getProperty("variable"));
	}
	
	public void validate(OnAlarmImpl mOnAlarm, Properties expected) {
		log(mOnAlarm);
		validateElementName(mOnAlarm.getElement(), "bpel:onAlarm");
		Element scope = (Element) mOnAlarm.getElement().getElementsByTagName("bpel:scope").item(0);
		Assert.assertNotNull(scope);
		Element condition = (Element) mOnAlarm.getElement().getElementsByTagName("bpel:for").item(0);
		Assert.assertNotNull(condition);
		validateElementValue(condition, expected.getProperty("for"));
	}

	public void validate(WhileImpl mWhile, Properties expected) {
		log(mWhile);
		validateElementName(mWhile.getElement(), "bpel:while");
		validateElementAttribute(mWhile.getElement(), "name", expected.getProperty("name"));
		// validate condition
		Element condition = (Element) mWhile.getElement().getElementsByTagName("bpel:condition").item(0);
		Assert.assertNotNull(condition);
		validateElementValue(condition, expected.getProperty("condition"));
	}
	
	public void validate(ForEachImpl mForEach, Properties expected) {
		log(mForEach);
		validateElementName(mForEach.getElement(), "bpel:forEach");
		validateElementAttribute(mForEach.getElement(), "name", expected.getProperty("name"));
		validateElementAttribute(mForEach.getElement(), "counterName", "Counter");
		
		Element start = (Element) mForEach.getElement().getElementsByTagName("bpel:startCounterValue").item(0);
		Assert.assertNotNull(start);
		validateElementValue(start, expected.getProperty("startCounterValue"));
		
		Element stop = (Element) mForEach.getElement().getElementsByTagName("bpel:finalCounterValue").item(0);
		Assert.assertNotNull(stop);
		validateElementValue(stop, expected.getProperty("finalCounterValue"));
	}
	
	public void validate(RepeatUntilImpl mRepeatUntil, Properties expected) {
		log(mRepeatUntil);
		validateElementName(mRepeatUntil.getElement(), "bpel:repeatUntil");
		validateElementAttribute(mRepeatUntil.getElement(), "name", expected.getProperty("name"));
		
		Element condition = (Element) mRepeatUntil.getElement().getElementsByTagName("bpel:condition").item(0);
		Assert.assertNotNull(condition);
		validateElementValue(condition, expected.getProperty("condition"));
	}
	
	public void validate(WaitImpl mWait, Properties expected) {
		log(mWait);
		validateElementName(mWait.getElement(), "bpel:wait");
		validateElementAttribute(mWait.getElement(), "name", expected.getProperty("name"));
		
		Element time = (Element) mWait.getElement().getElementsByTagName("bpel:for").item(0);
		Assert.assertNotNull(time);
		validateElementValue(time, expected.getProperty("for"));
	}
	
	public void validate(SequenceImpl mSequence, Properties expected) {
		log(mSequence);
		validateElementName(mSequence.getElement(), "bpel:sequence");
		validateElementAttribute(mSequence.getElement(), "name", expected.getProperty("name"));
	}
	
	public void validate(FlowImpl mFlow, Properties expected) {
		log(mFlow);
		validateElementName(mFlow.getElement(), "bpel:flow");
		validateElementAttribute(mFlow.getElement(), "name", expected.getProperty("name"));
	}
	
	public void validate(ScopeImpl mScope, Properties expected) {
		log(mScope);
		validateElementName(mScope.getElement(), "bpel:scope");
		validateElementAttribute(mScope.getElement(), "name", expected.getProperty("name"));
	}
	
	public void validate(CompensationHandlerImpl mHandler, Properties expected) {
		log(mHandler);
		validateElementName(mHandler.getElement(), "bpel:compensationHandler");
	}
	
	public void validate(FaultHandlerImpl mHandler, Properties expected) {
		log(mHandler);
		validateElementName(mHandler.getElement(), "bpel:faultHandlers");
		Element catchElem = (Element) mHandler.getElement().getElementsByTagName("bpel:catch").item(0);
		Assert.assertNotNull(catchElem);
	}
	
	public void validate(ExitImpl mExit, Properties expected) {
		log(mExit);
		validateElementName(mExit.getElement(), "bpel:exit");
		validateElementAttribute(mExit.getElement(), "name", expected.getProperty("name"));
	}
	
	public void validate(ThrowImpl mThrow, Properties expected) {
		log(mThrow);
		validateElementName(mThrow.getElement(), "bpel:throw");
		validateElementAttribute(mThrow.getElement(), "name", expected.getProperty("name"));
		// TODO: add this when this gets fixed !!!
//		validateElementAttribute(mThrow.getElement(), "faultVariable", expected.getProperty("faultVariable"));
		
		// validate faultName which is in form ns:fault
		String actual = mThrow.getElement().getAttribute("faultName");
		Assert.assertTrue(actual.matches("(.)+:" + expected.getProperty("faultName")));
	}
	
	public void validate(RethrowImpl mRethrow, Properties expected) {
		log(mRethrow);
		validateElementName(mRethrow.getElement(), "bpel:rethrow");
		validateElementAttribute(mRethrow.getElement(), "name", expected.getProperty("name"));
	}
	
	public void validate(CompensateImpl mCompensate, Properties expected) {
		log(mCompensate);
		validateElementName(mCompensate.getElement(), "bpel:compensate");
		validateElementAttribute(mCompensate.getElement(), "name", expected.getProperty("name"));
	}
	
	public void validate(CompensateScopeImpl mCompensateScope, Properties expected) {
		log(mCompensateScope);
		validateElementName(mCompensateScope.getElement(), "bpel:compensateScope");
		validateElementAttribute(mCompensateScope.getElement(), "name", expected.getProperty("name"));
		validateElementAttribute(mCompensateScope.getElement(), "target", expected.getProperty("target"));
	}

	
	void validateElementName(Element elem, String expectedName) {
		Assert.assertEquals(expectedName, elem.getNodeName());
	}
	
	void validateElementAttributes(Element elem, String[] expectedNames, String[] expectedValues) {
		Assert.assertTrue(expectedNames.length == expectedValues.length);
		Assert.assertTrue(elem.getAttributes().getLength() == expectedNames.length);
		for(int i=0; i<expectedNames.length; i++) {
			validateElementAttribute(elem, expectedNames[i], expectedValues[i]);
		}
	}
	
	void validateElementAttribute(Element elem, String expectedAttribute, String expectedValue) {
		String val = elem.getAttribute(expectedAttribute);
		Assert.assertNotSame("", val);
		Assert.assertEquals(expectedValue, val);
	}
	
	void validateElementValue(Element elem, String expectedVal) {
		Node node = elem.getChildNodes().item(0);
		String actual = node.getNodeValue();
		Assert.assertEquals(expectedVal, actual);
	}
	
	private void log(ExtensibleElementImpl elem) {
		log.info("Validating element: " + elem.getElement());
	}
}
