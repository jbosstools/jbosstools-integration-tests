/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.bot.test.beans.openon.template;


import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.cdi.text.ext.hyperlink.xpl.HierarchyInformationControl;
import org.junit.After;
import org.junit.Test;

/**
 * Test operates on finding right observer for event and vice versa
 * 
 * @author Jaroslav Jankovic
 */
public abstract class FindObserverEventTemplate extends CDITestBase {
	
	private static final Map<String,List<String>> eventWithObservers = new HashMap<String,List<String>>();
	
	private static final Map<String,List<String>> observerWithEvents = new HashMap<String,List<String>>();
	
	static{
		eventWithObservers.put("myBean1Q1Event", Arrays.asList("observeQ1MyBean1",
				"observeAnyMyBean1","observeNoQualifierMyBean1"));
		eventWithObservers.put("myBean1AnyEvent", Arrays.asList("observeAnyMyBean1",
				"observeNoQualifierMyBean1"));
		eventWithObservers.put("myBean2Q1Event",Arrays.asList("observeQ1MyBean1",
				"observeQ1MyBean2","observeAnyMyBean1","observeAnyMyBean2",
				"observeNoQualifierMyBean1","observeNoQualifierMyBean1"));
		eventWithObservers.put("myBean2AnyEvent", Arrays.asList("observeAnyMyBean1",
				"observeAnyMyBean2","observeNoQualifierMyBean1","observeNoQualifierMyBean2"));
		eventWithObservers.put("myBean1Q2Event", Arrays.asList("observeQ2MyBean1",
				"observeAnyMyBean1", "observeNoQualifierMyBean1"));
		eventWithObservers.put("myBean2Q2Event", Arrays.asList("observeQ2MyBean1",
				"observeQ2MyBean2","observeAnyMyBean1","observeAnyMyBean2","observeNoQualifierMyBean1",
				"observeNoQualifierMyBean2"));
		
		
		observerWithEvents.put("observeNoQualifierMyBean1", Arrays.asList("myBean1Q1Event",
				"myBean2Q1Event","myBean1AnyEvent","myBean2AnyEvent","myBean1Q2Event","myBean2Q2Event"));
		observerWithEvents.put("observeAnyMyBean1", Arrays.asList("myBean1Q1Event",
				"myBean2Q1Event","myBean1AnyEvent","myBean2AnyEvent","myBean1Q2Event","myBean2Q2Event"));
		observerWithEvents.put("observeQ1MyBean1",Arrays.asList("myBean1Q1Event","myBean2Q1Event"));
		observerWithEvents.put("observeNoQualifierMyBean2", Arrays.asList("myBean2Q1Event",
				"myBean2AnyEvent","myBean2Q2Event"));
		observerWithEvents.put("observeAnyMyBean2", Arrays.asList("myBean2Q1Event",
				"myBean2AnyEvent","myBean2Q2Event"));
		observerWithEvents.put("observeQ1MyBean2",Arrays.asList("myBean2Q1Event"));
		observerWithEvents.put("observeQ2MyBean1", Arrays.asList("myBean1Q2Event",
				"myBean2Q2Event"));
		observerWithEvents.put("observeQ2MyBean2", Arrays.asList("myBean2Q2Event"));
		
	}
	
	@After
	public void clean(){
		deleteAllProjects();
	}
	
	@Test
	public void testSimpleCaseObserverFinding() {
		
		prepare();
		
		for(String event: eventWithObservers.keySet()){
			checkEvent(event, "EventsProducer", "ObserverBean");
			openEvent(event, "EventsProducer", "ObserverBean");
		}
		
		for(String observer: observerWithEvents.keySet()){
			checkObserver(observer, "ObserverBean", "EventsProducer");
			openObserver(observer, "ObserverBean", "EventsProducer");
		}
		
		

	}

	// not implemented yet
	//@Test
	public void testComplexCaseObserverFinding() {
//TODO
		prepareComplexObserverFinding();

		testComplexObserverFinding();
	}
	
	private void checkEvent(String eventName, String className, String observerClass){
		openOnHelper.selectProposal(className, eventName, "Show CDI Observer Methods...");
		HierarchyInformationControl hic = new HierarchyInformationControl(HierarchyInformationControl.OBSERVER_LABEL);
		
		String packageProjectPath = getPackageName() + " - /" + getProjectName() + "/src";
		List<String> expectedObservers = eventWithObservers.get(eventName);
		assertEquals(expectedObservers.size(), hic.getProposals().size());
		for(String expectedObserver : expectedObservers){
			assertTrue(hic.getProposalsTable().containsItem(
					observerClass + "."+expectedObserver+"() - " + packageProjectPath));
		}
		hic.close();
	}
	
	private void openEvent(String eventName, String className, String observerClass){
		List<String> proposals = openOnHelper.getProposals(className, eventName, "Show CDI Observer Methods...");
		for(String proposal: proposals){
			openOnHelper.selectProposal(className, eventName, "Show CDI Observer Methods...");			
			HierarchyInformationControl hic = new HierarchyInformationControl(HierarchyInformationControl.OBSERVER_LABEL);
			hic.selectProposal(proposal);
			
			String[] splitted = proposal.split("\\.");
			TextEditor te = new TextEditor();
			assertEquals(splitted[0]+".java",te.getTitle());
			String[] splitted1 = splitted[1].split("\\(");
			assertEquals(splitted1[0],te.getSelectedText());
		}
	}
	
	private void openObserver(String observer, String className, String eventClass){
		List<String> events = observerWithEvents.get(observer);
		//no need to test with one, was tested previously
		if(events.size()!=1){
			List<String> proposals = openOnHelper.getProposals(className, observer, "Show CDI Events...");
			for(String proposal: proposals){
				openOnHelper.selectProposal(className, observer, "Show CDI Events...");
				HierarchyInformationControl hic = new HierarchyInformationControl(HierarchyInformationControl.EVENTS_LABEL);
				hic.selectProposal(proposal);
				
				String[] splitted = proposal.split("\\.");
				TextEditor te = new TextEditor();
				assertEquals(splitted[0]+".java",te.getTitle());
				String[] splitted1 = splitted[1].split(" ");
				assertEquals(splitted1[0],te.getSelectedText());
			}
		}
	}
	
	private void checkObserver(String observer, String className, String eventClass){
		List<String> events = observerWithEvents.get(observer);
		if(events.size() == 1){
			openOnHelper.selectProposal(className, observer, "Open CDI Event "+eventClass+"."+events.get(0));
			TextEditor te = new TextEditor();
			assertEquals(eventClass+".java",te.getTitle());
			assertEquals(events.get(0),te.getSelectedText());
		} else {
			openOnHelper.selectProposal(className, observer, "Show CDI Events...");
			HierarchyInformationControl hic = new HierarchyInformationControl(HierarchyInformationControl.EVENTS_LABEL);

			String packageProjectPath = getPackageName() + " - /" + getProjectName() + "/src";
			List<String> expectedEvents = observerWithEvents.get(observer);
			assertEquals(expectedEvents.size(), hic.getProposals().size());
			for(String expectedEvent : expectedEvents){
				assertTrue(hic.getProposalsTable().containsItem(
						eventClass + "."+expectedEvent+" - " + packageProjectPath));
			}
			hic.close();
		}
	}

	// not implemented yet
	private void prepareComplexObserverFinding() {

	}

	// not implemented yet
	private void testComplexObserverFinding() {
		/**
		 * main idea - check events which have multiple qualifiers defined
		 * (http://docs.jboss.org/weld/reference/1.0.0/en-US/html/events.html -
		 * 11.6) - check events with qualifiers which has members
		 * (http://docs.jboss.org/weld/reference/1.0.0/en-US/html/events.html -
		 * 11.5)
		 */
	}
	
	private void prepare(){
		beansHelper.createQualifier("Q1", getPackageName(), false, false);
		beansHelper.createQualifier("Q2", getPackageName(), false, false);
		
		beansHelper.createBean("MyBean1", getPackageName(), false, false, false, 
				false, false, true,false,null, null);
		editResourceUtil.replaceClassContentByResource("MyBean1.java", readFile("resources/events/MyBean1.java.cdi"),false);
		
		beansHelper.createBean("MyBean2", getPackageName(), false, false, false, 
				false, false, true,false,null, null);
		editResourceUtil.replaceClassContentByResource("MyBean2.java", readFile("resources/events/MyBean2.java.cdi"),false);
		
		
		beansHelper.createBean("EventsProducer", getPackageName(), false, false, false, 
				false, false, true,false,null, null);
		editResourceUtil.replaceClassContentByResource("EventsProducer.java", readFile("resources/events/EventsProducer.java.cdi"),false);
		
		
		beansHelper.createBean("ObserverBean", getPackageName(), false, false, false, 
				false, false, true,false,null, null);
		editResourceUtil.replaceClassContentByResource("ObserverBean.java", readFile("resources/events/ObserverBean.java.cdi"),false);
	}
}
