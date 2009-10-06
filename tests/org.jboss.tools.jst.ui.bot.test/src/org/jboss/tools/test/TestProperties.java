package org.jboss.tools.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

public class TestProperties extends Properties {

	public TestProperties() {
		super();
		// TODO Auto-generated constructor stub
	}


	public TestProperties(Properties defaults) {
		super(defaults);
		// TODO Auto-generated constructor stub
	}


	@Override
	public synchronized void load(InputStream inStream) throws IOException {
		// TODO Auto-generated method stub
		super.load(inStream);
		substituteSystemProperties(this);
	}

	
	public static void substituteSystemProperties(Properties projectProperties2) {
		for (Object opject : projectProperties2.keySet()) {
			String propertyValue = projectProperties2.get(opject).toString();
			if(propertyValue.matches("\\$\\{.*")) {
				Enumeration<?> names = System.getProperties().propertyNames();
				
				while(names.hasMoreElements()) {
					String substitute = names.nextElement().toString();
					String regexp = "\\$\\{" + substitute + "}";
					if(propertyValue.matches(regexp)) {
						projectProperties2.put(opject, propertyValue.replaceAll(regexp, System.getProperty(substitute)));
					}
				}
			}
		}
	}
}
