package org.jboss.tools.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

public class TestProperties extends Properties {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2611391472191407247L;


	public TestProperties() {
		super();
	}


	public TestProperties(Properties defaults) {
		super(defaults);
	}


	@Override
	public synchronized void load(InputStream inStream) throws IOException {
		super.load(inStream);
		substituteSystemProperties(this);
	}

	
	public static void substituteSystemProperties(Properties projectProperties2) {
		for (Object opject : projectProperties2.keySet()) {
			String propertyValue = projectProperties2.get(opject).toString();
			if(propertyValue.matches("\\$\\{.*")) { //$NON-NLS-1$
				Enumeration<?> names = System.getProperties().propertyNames();
				
				while(names.hasMoreElements()) {
					String substitute = names.nextElement().toString();
					String regexp = "\\$\\{" + substitute + "}"; //$NON-NLS-1$ //$NON-NLS-2$
					if(propertyValue.matches(regexp)) {
						projectProperties2.put(opject, propertyValue.replaceAll(regexp, System.getProperty(substitute)));
					}
				}
			}
		}
	}
}
