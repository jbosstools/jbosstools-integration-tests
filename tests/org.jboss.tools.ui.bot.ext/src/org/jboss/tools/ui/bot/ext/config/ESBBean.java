package org.jboss.tools.ui.bot.ext.config;

public class ESBBean {
	public String version;
	public String esbHome;
	
	public static ESBBean fromString(String propValue) throws Exception{
		try {
			String[] esbParams = propValue.split(",");
			ESBBean bean = new ESBBean();
			bean.esbHome=esbParams[1];
			bean.version=esbParams[0];
			return bean;
			}
			catch (Exception ex) {
				throw new Exception("Cannot parse ESB property line",ex);
			}
	}
}
