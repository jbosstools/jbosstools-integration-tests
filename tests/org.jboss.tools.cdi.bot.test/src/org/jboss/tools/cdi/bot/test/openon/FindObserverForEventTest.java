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

package org.jboss.tools.cdi.bot.test.openon;


import static org.junit.Assert.*;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.workbench.api.Editor;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.cdi.bot.test.annotations.CDIWizardType;
import org.junit.Test;

/**
 * Test operates on finding right observer for event and vice versa
 * 
 * @author Jaroslav Jankovic
 */
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
@OpenPerspective(JavaEEPerspective.class)
@CleanWorkspace
public class FindObserverForEventTest extends OpenOnBase {

	@Test
	public void testSimpleCaseObserverFinding() {

		prepareSimpleObserverFinding();

		for (int i = 0; i < events.length; i++) {			
			String eventsClass = "EventsProducer.java";
			
			String showObserverOption = "Show CDI Observer Methods...";
			
			checkEventsAndObserver(events[i], eventsClass, showObserverOption);
		}
		

		for (int i = 0; i < observers.length; i++) {
			String observerClass = "ObserverBean.java";
			
			/**
			 * there are two observer methods for which there is only one
			 * event, so there will be no "Show CDI Events" option, instead
			 * of that, there will be "Open CDI Event" option
			 */
			String showObserverOption = ((observers[i].equals("observeQ1MyBean2")) || 
							(observers[i].equals("observeQ2MyBean2"))) ? "Open CDI Event" : "Show CDI Events...";
			
			checkEventsAndObserver(observers[i], observerClass, showObserverOption);
		} 

	}

	// not implemented yet
	@Test
	public void testComplexCaseObserverFinding() {

		prepareComplexObserverFinding();

		testComplexObserverFinding();
	}

	private void prepareSimpleObserverFinding() {

		wizard.createCDIComponent(CDIWizardType.QUALIFIER, "Q1", getPackageName(), null);

		wizard.createCDIComponent(CDIWizardType.QUALIFIER, "Q2", getPackageName(), null);

		wizard.createCDIComponentWithContent(CDIWizardType.BEAN, "MyBean1", 
				getPackageName(), null, "/resources/events/MyBean1.java.cdi");		

		wizard.createCDIComponentWithContent(CDIWizardType.BEAN, "MyBean2", 
				getPackageName(), null, "/resources/events/MyBean2.java.cdi");

		wizard.createCDIComponentWithContent(CDIWizardType.BEAN, "EventsProducer", 
				getPackageName(), null, "/resources/events/EventsProducer.java.cdi");
		
		wizard.createCDIComponentWithContent(CDIWizardType.BEAN, "ObserverBean", 
				getPackageName(), null, "/resources/events/ObserverBean.java.cdi");
	}

	private void checkEventsAndObserver(String name, String className,
			String option) {
		
		Editor e = new DefaultEditor(className);
		new DefaultStyledText().selectText(name);
		AbstractWait.sleep(TimePeriod.SHORT);
		ContentAssistant ca = e.openOpenOnAssistant();
		for(String p: ca.getProposals()){
			if(p.startsWith(option)){
				ca.chooseProposal(p);
				break;
			}
		}
		
		if (option.equals("Open CDI Event")) {
			if (name.equals("observeQ1MyBean2")) {
				LOGGER.info("Testing observer: observeQ1MyBean2 started");
				assertTrue(new DefaultStyledText().getSelectionText().equals("myBean2Q1Event"));
				LOGGER.info("Testing observer: observeQ1MyBean2 ended");
			}else {  
				//observeQ1MyBean2
				LOGGER.info("Testing observer: observeQ1MyBean2 started");
				assertTrue(new DefaultStyledText().getSelectionText().equals("myBean2Q2Event"));
				LOGGER.info("Testing observer: observeQ1MyBean2 ended");
			}			
		} else {
			Shell s = new DefaultShell();
			Table observerTable = new DefaultTable();
			if (className.equals("EventsProducer.java")) {
				assertTrue(checkAllObserverMethodsForEvent(name, observerTable)); 
			}
			if (className.equals("ObserverBean.java")) {
				assertTrue(checkAllEventsForObserverMethod(name, observerTable)); 
			}
			s.close();
		}
	}

	private boolean checkAllObserverMethodsForEvent(String eventName, 
			Table observerTable) {
		String observerClass = "ObserverBean";
		String packageProjectPath = getPackageName() + " - /" + getProjectName() + "/src";
		String parametrizedEventItem = observerClass + ".observeXXX() - " + packageProjectPath;
		boolean allObserversFound = false;		
		for (int i = 0; i < events.length; i++) {
			if (eventName.equals(events[i])) {
				LOGGER.info("Testing event: " + events[i] + " started");
				switch (i) {
				//myBean1Q1Event
				//myBean1Q1Event.fire(new MyBean1())
				case 0: 
				case 6:
					if (observerTable.containsItem(parametrizedEventItem.replace("XXX", "Q1MyBean1")) &&
						observerTable.containsItem(parametrizedEventItem.replace("XXX", "AnyMyBean1")) &&
						observerTable.containsItem(parametrizedEventItem.replace("XXX", "NoQualifierMyBean1"))) {
						allObserversFound = true;
					}
					break;
				//myBean1AnyEvent
				//myBean1AnyEvent.fire(new MyBean1())	
				//myBean1AnyEvent.fire(new MyBean2())				
				case 1:
				case 7:
				case 12:
					if (observerTable.containsItem(parametrizedEventItem.replace("XXX", "AnyMyBean1")) &&
						observerTable.containsItem(parametrizedEventItem.replace("XXX", "NoQualifierMyBean1"))) {
						allObserversFound = true;
					}
					break;
				//myBean2Q1Event
				//myBean2Q1Event.fire(new MyBean2())
				case 2:
				case 8:
					if (observerTable.containsItem(parametrizedEventItem.replace("XXX", "Q1MyBean1")) &&
						observerTable.containsItem(parametrizedEventItem.replace("XXX", "Q1MyBean2")) &&
						observerTable.containsItem(parametrizedEventItem.replace("XXX", "AnyMyBean1")) &&
						observerTable.containsItem(parametrizedEventItem.replace("XXX", "AnyMyBean2")) &&
						observerTable.containsItem(parametrizedEventItem.replace("XXX", "NoQualifierMyBean1")) &&						
						observerTable.containsItem(parametrizedEventItem.replace("XXX", "NoQualifierMyBean2"))) {
						allObserversFound = true;
					}
					break;
				//myBean2AnyEvent	
				//myBean2AnyEvent.fire(new MyBean2())				
				case 3:
				case 9:
					if (observerTable.containsItem(parametrizedEventItem.replace("XXX", "AnyMyBean1")) &&
						observerTable.containsItem(parametrizedEventItem.replace("XXX", "AnyMyBean2")) &&
						observerTable.containsItem(parametrizedEventItem.replace("XXX", "NoQualifierMyBean1")) &&
						observerTable.containsItem(parametrizedEventItem.replace("XXX", "NoQualifierMyBean2"))) {
						allObserversFound = true;
					}
					break;	
				//myBean1Q2Event	
				//myBean1Q2Event.fire(new MyBean1())				
				case 4:
				case 10:
					if (observerTable.containsItem(parametrizedEventItem.replace("XXX", "Q2MyBean1")) &&
						observerTable.containsItem(parametrizedEventItem.replace("XXX", "AnyMyBean1")) &&
						observerTable.containsItem(parametrizedEventItem.replace("XXX", "NoQualifierMyBean1"))) {
						allObserversFound = true;
					}
					break;
				//myBean2Q2Event	
				//myBean2Q2Event.fire(new MyBean2())
				case 5:
				case 11:
					if (observerTable.containsItem(parametrizedEventItem.replace("XXX", "Q2MyBean1")) &&
						observerTable.containsItem(parametrizedEventItem.replace("XXX", "Q2MyBean2")) &&
						observerTable.containsItem(parametrizedEventItem.replace("XXX", "AnyMyBean1")) &&
						observerTable.containsItem(parametrizedEventItem.replace("XXX", "AnyMyBean2")) &&
						observerTable.containsItem(parametrizedEventItem.replace("XXX", "NoQualifierMyBean1")) &&						
						observerTable.containsItem(parametrizedEventItem.replace("XXX", "NoQualifierMyBean2"))) {
						allObserversFound = true;
					}
					break;
				}
				LOGGER.info("Testing event: " + events[i] + " ended");
				break;
			}
		}
		return allObserversFound;
	}

	private boolean checkAllEventsForObserverMethod(String observerName,
			Table eventsTable) {
		String eventsClass = "EventsProducer";
		String packageProjectPath = getPackageName() + " - /" + getProjectName() + "/src";
		String parametrizedEventItem = eventsClass + ".myBeanXXX - " + packageProjectPath;
		boolean allEventsFound = false;		
		for (int i = 0; i < observers.length; i++) {
			if (observerName.equals(observers[i])) {
				LOGGER.info("Testing observer: " + observers[i] + " started");
				switch (i) {
				//observeNoQualifierMyBean1		
				//observeAnyMyBean1
				case 0:
				case 1:
					if (eventsTable.containsItem(parametrizedEventItem.replace("XXX", "1Q1Event")) &&
						eventsTable.containsItem(parametrizedEventItem.replace("XXX", "2Q1Event")) &&						
						eventsTable.containsItem(parametrizedEventItem.replace("XXX", "1AnyEvent")) &&
						eventsTable.containsItem(parametrizedEventItem.replace("XXX", "2AnyEvent")) &&						
						eventsTable.containsItem(parametrizedEventItem.replace("XXX", "1Q2Event")) &&
						eventsTable.containsItem(parametrizedEventItem.replace("XXX", "2Q2Event"))) {
						allEventsFound = true;
					}
					break;
				//observeQ1MyBean1
				case 2:
					if (eventsTable.containsItem(parametrizedEventItem.replace("XXX", "1Q1Event")) &&
						eventsTable.containsItem(parametrizedEventItem.replace("XXX", "2Q1Event"))) {
						allEventsFound = true;
					}
					break;
				//observeNoQualifierMyBean2	
				//observeAnyMyBean2	
				case 3:
				case 4:
					if (eventsTable.containsItem(parametrizedEventItem.replace("XXX", "2Q1Event")) &&
						eventsTable.containsItem(parametrizedEventItem.replace("XXX", "2AnyEvent")) &&
						eventsTable.containsItem(parametrizedEventItem.replace("XXX", "2Q2Event"))) {
						allEventsFound = true;
						}
					break;	
				//observeQ1MyBean2	
				case 5:
					throw new IllegalStateException("Observer method \"observeQ1MyBean2\" should " +
							"have been tested earlier!!");							
				//observeQ2MyBean1
				case 6:
					if (eventsTable.containsItem(parametrizedEventItem.replace("XXX", "1Q2Event")) &&
						eventsTable.containsItem(parametrizedEventItem.replace("XXX", "2Q2Event"))) {
						allEventsFound = true;
						}
					break;
				//observeQ2MyBean2	
				case 7:	
					throw new IllegalStateException("Observer method \"observeQ2MyBean2\" should " +
							"have been tested earlier!!");					
				}
				LOGGER.info("Testing observer: " + observers[i] + " ended");
				break;
			}
		}
		return allEventsFound;
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
}
