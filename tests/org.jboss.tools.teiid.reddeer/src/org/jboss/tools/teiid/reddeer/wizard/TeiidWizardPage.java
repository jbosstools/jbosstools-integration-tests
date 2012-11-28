package org.jboss.tools.teiid.reddeer.wizard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 
 * @author apodhrad
 * 
 */
public class TeiidWizardPage {

	/**
	 * Fills the wizard page with given properties
	 * 
	 * @param data
	 */
	public void fillWizardPage(Map<String, Object> data) {
		for (String key : data.keySet()) {
			fillWizardPage(key, data.get(key));
		}
	}

	/**
	 * Fills the wizard page with given properties
	 * 
	 * @param attribute
	 * @param value
	 */
	public void fillWizardPage(String attribute, Object value) {
		Class clazz = getClass();
		try {
			Method method = clazz.getMethod(getMethodName(attribute), value.getClass());
			method.invoke(this, value);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private String getMethodName(String attribute) {
		Character firstChar = Character.toUpperCase(attribute.charAt(0));
		return "set" + firstChar + attribute.substring(1);
	}
}
