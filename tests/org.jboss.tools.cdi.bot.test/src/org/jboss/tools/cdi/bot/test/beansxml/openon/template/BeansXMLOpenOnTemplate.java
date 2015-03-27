package org.jboss.tools.cdi.bot.test.beansxml.openon.template;

import static org.junit.Assert.assertTrue;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.common.model.ui.editor.EditorPartWrapper;
import org.junit.Test;

//https://issues.jboss.org/browse/JBIDE-7025
public class BeansXMLOpenOnTemplate extends CDITestBase{
	
	@Test
	public void testBeansXMLDecoratorOpenOn() {
		
		beansHelper.createDecorator("D1", getPackageName(),
				"java.util.Set", null, true, false, false, false, true);
		
		checkOpenOnBeanXml(getPackageName(), "D1");
	}
	
	@Test
	public void testBeansXMLInterceptorOpenOn() {
		
		beansHelper.createInterceptor("I1", getPackageName(),
				null, false,true);
		
		checkOpenOnBeanXml(getPackageName(), "I1");
	}
	
	@Test
	public void testBeansXMLAlternativeOpenOn() {
		
		beansHelper.createStereotype("A1", getPackageName(), false, false, 
				true, true, false);
		
		checkOpenOnBeanXml(getPackageName(), "A1");
	}
	
	private void checkOpenOnBeanXml(String packageName, String className) {
		EditorPartWrapper beans = beansXMLHelper.openBeansXml(PROJECT_NAME);
		beans.activateSourcePage();
		new DefaultStyledText().selectText(packageName + "." + className);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.F3);
		assertTrue(new DefaultEditor().getTitle().equals(className+".java"));
	}

}
