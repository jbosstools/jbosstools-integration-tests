package org.jboss.tools.ui.bot.ext.config;

public class ESBBean extends RuntimeBean{
	public ESBBean() {
		this.key = TestConfigurator.Keys.ESB;
	}
	
	public static ESBBean fromString(String propValue) throws Exception {
		return (ESBBean)fromString(propValue, new ESBBean());
	}
}
