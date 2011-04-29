package org.jboss.tools.bpel.ui.bot.ext.widgets;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.wst.wsdl.internal.impl.ExtensibleElementImpl;
import org.jboss.tools.bpel.ui.bot.test.assertion.BPELValidator;
import org.junit.Assert;

public class BPELEditPartListener {

	Logger log = Logger.getLogger(BPELEditPartListener.class);
	
	BPELValidator validator = new BPELValidator();
	
	public void editFinished(BPELEditPartEvent event) {
		log.info("Edited part: " + event.getEditPart());
		ExtensibleElementImpl impl = null;
		try {
			impl = (ExtensibleElementImpl) event.getEditPart().getModel();
			Class<?> clazz = impl.getClass();
			Method m = validator.getClass().getMethod("validate", clazz, Properties.class);
			m.invoke(validator, clazz.cast(impl), event.getChangedProperties());
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			// rethrow exception if it's an AssertionError coming from jUnit
			if(e.getCause() instanceof AssertionError) {
				log.error(e.getMessage(), e.getCause());
				throw ((AssertionError) e.getCause());
			}
			throw new RuntimeException(e);
		} 
	}
	
	public void childRemoved(BPELEditPartEvent event) {
		log.info("Removed part: " + event.getEditPart());
	}
	
}
