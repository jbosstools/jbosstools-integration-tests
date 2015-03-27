package org.jboss.tools.cdi.bot.test.beans.openon.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.junit.Test;

public class BeanInjectOpenOnTemplate extends CDITestBase{
	
	private static final Map<String,List<String>> beanInjections = new HashMap<String,List<String>>();
	
	static{
		beanInjections.put("myBean1", Arrays.asList("@Produces MyBean3.getMyBean1()",
				"@Produces MyBean3.getMyBean1WithIMB2()","@Produces MyBean3.getMyBean2()",
				"MyBean1","MyBean2"));
		beanInjections.put("myBean2", Arrays.asList("@Produces MyBean3.getMyBean2()",
				"MyBean2"));
		beanInjections.put("myBean3", Arrays.asList("@Produces MyBean3.getMyBean1WithQ1()",
				"@Produces MyBean3.getMyBean1WithIMB2Q1()","@Produces MyBean3.getMyBean2WithQ1()",
				"MyBean4"));
		beanInjections.put("myBean4", Arrays.asList("@Produces MyBean3.getMyBean2WithQ1()",
				"MyBean4"));
		beanInjections.put("myBean5", Arrays.asList("@Produces MyBean3.getMyBean1WithQ2()",
				"@Produces MyBean3.getMyBean1WithIMB2Q2()","@Produces MyBean3.getMyBean2WithQ2()",
				"MyBean4","MyBean5"));
		beanInjections.put("myBean6", Arrays.asList("@Produces MyBean3.getMyBean2WithQ2()",
				"MyBean4","MyBean5"));
		beanInjections.put("myBean7", Arrays.asList("@Produces MyBean3.getMyBean1()",
				"@Produces MyBean3.getMyBean1WithIMB2()",
				"@Produces MyBean3.getMyBean1WithIMB2Q1()","@Produces MyBean3.getMyBean1WithIMB2Q2()",
				"@Produces MyBean3.getMyBean1WithQ1()", "@Produces MyBean3.getMyBean1WithQ2()",
				"@Produces MyBean3.getMyBean2()", "@Produces MyBean3.getMyBean2WithQ1()",
				"@Produces MyBean3.getMyBean2WithQ2()",
				"MyBean1","MyBean2","MyBean4","MyBean5"));
		beanInjections.put("myBean8", Arrays.asList("@Produces MyBean3.getMyBean2()",
				"@Produces MyBean3.getMyBean2WithQ1()","@Produces MyBean3.getMyBean2WithQ2()",
				"MyBean2","MyBean4","MyBean5"));
		beanInjections.put("myBean9", Arrays.asList("MyBean4"));
		beanInjections.put("myBean10", Arrays.asList("MyBean4"));
		beanInjections.put("myBean11", Arrays.asList("MyBean4"));
		
	}
	
	@Test
	public void testBeanInjectOpenOn() {

		prepareInjectedPointsComponents();
		
		for(String injectionPoint: beanInjections.keySet()){
			checkInjectedPoint(injectionPoint);
			openInjectionPoint(injectionPoint);
			
		}

	}
	
	private void prepareInjectedPointsComponents() {
		beansHelper.createQualifier("Q1", getPackageName(), false, false);
		beansHelper.createQualifier("Q2", getPackageName(), false, false);
		beansHelper.createBean("MyBean1", getPackageName(), false, false ,false, false,
				false, true,false,null, null);
		
		List<String> classes = Arrays.asList("MyBean2","MyBean3","MyBean4","MyBean5","MainBean");
		
		for(String s: classes){
			beansHelper.createBean(s, getPackageName(), false, false ,false, false,
					false, true,false,null,null);
			editResourceUtil.replaceClassContentByResource(s+".java", 
					readFile("resources/openon/InjectedPoints/"+s+".java.cdi"), false);
		}
	}
	
	private void checkInjectedPoint(String injectionPoint) {
		TextEditor e = new TextEditor("MainBean.java");
		e.selectText(injectionPoint);
		List<String> beans =  beanInjections.get(injectionPoint);
		if(beans.size() == 1){
			openOnHelper.selectProposal("MainBean", injectionPoint, "Open @Inject Bean "+beans.get(0));
			TextEditor te = new TextEditor();
			assertEquals(beans.get(0)+".java",te.getTitle());
			assertEquals(beans.get(0),te.getSelectedText());
		} else {
			openOnHelper.selectProposal("MainBean", injectionPoint, "Show All Assignable Beans...");
			Shell s = new DefaultShell("Assignable Beans");
			Table eventsTable = new DefaultTable();
			String packageProjectPath = getPackageName() + " - /" + getProjectName() + "/src";
			assertEquals(beans.size(), eventsTable.getItems().size());
			for(String bean : beans){
				assertTrue(eventsTable.containsItem(
						bean+" - " + packageProjectPath));
			}
			s.close();
		}
	}
	
	private void openInjectionPoint(String injectionPoint){
		TextEditor e = new TextEditor("MainBean.java");
		e.selectText(injectionPoint);
		List<String> beans =  beanInjections.get(injectionPoint);
		//no need to test with only one assignable bean, was tested previously
		if(beans.size() > 1){
			List<String> proposals = openOnHelper.getProposals("MainBean", injectionPoint, 
					"Show All Assignable Beans...");
			for(String proposal: proposals){
				openOnHelper.selectProposal("MainBean", injectionPoint, 
						"Show All Assignable Beans...");
				Table observerTable = new DefaultTable();
				observerTable.getItem(proposal).select();
			
				KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.CR);
			
				if(proposal.contains("@")){
					String[] splitted = proposal.split(" ");
					String[] splitted1 = splitted[1].split("\\.");
					TextEditor te = new TextEditor();
					assertEquals(splitted1[0]+".java",te.getTitle());
					assertEquals(splitted1[1].replace("()", ""),te.getSelectedText());
				} else {
					String[] splitted = proposal.split(" ");
					TextEditor te = new TextEditor();
					assertEquals(splitted[0]+".java",te.getTitle());
					assertEquals(splitted[0],te.getSelectedText());
				}
			}
		}
	}

}
