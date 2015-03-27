package org.jboss.tools.cdi.bot.test.beansxml.validation.template;

import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.common.model.ui.editor.EditorPartWrapper;
import org.jboss.tools.cdi.reddeer.condition.AsYouTypeMarkerExists;
import org.junit.Test;

public class BeansXMLAsYouTypeValidationTemplate extends CDITestBase{
	
	private static final String BEAN_IS_NOT_ALTERNATIVE = "is not an alternative bean class";
	
	@Test
	public void testBeansXmlAYTValidation() {
		
		beansHelper.createBean("A1", getPackageName(), false,false,false,false,false,false,false,null,null);
		
		beansHelper.createBean("A2", getPackageName(), false,true,false,false,false,false,false,null,null);
		
		//=======================================================================
		// 	Invoke as-you-type validation marker appearance without saving file
		//=======================================================================
		
		EditorPartWrapper ep = beansXMLHelper.openBeansXml(getProjectName());
		ep.activateSourcePage();
		editResourceUtil.replaceInEditor("</beans>", "<alternatives><class>"+getPackageName()+".A1</class></alternatives></beans>");
		
		
		new WaitUntil(new AsYouTypeMarkerExists("beans.xml",BEAN_IS_NOT_ALTERNATIVE));
		
		//==========================================================================
		// 	Invoke as-you-type validation marker disappearance without saving file
		//==========================================================================
		
		editResourceUtil.replaceInEditor("beans.xml","A1", "A2", false);
		
		new WaitWhile(new AsYouTypeMarkerExists("beans.xml",BEAN_IS_NOT_ALTERNATIVE));
	}

}
