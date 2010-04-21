package org.jboss.tools.ui.bot.ext.config;


public class SeamBean {

	public String version;
	public String seamHome;
	
	public static SeamBean fromString(String propValue) throws Exception{
		try {
			String[] seamParams = propValue.split(",");
			SeamBean bean = new SeamBean();
			bean.seamHome=seamParams[1];
			bean.version=seamParams[0];
			return bean;
			}
			catch (Exception ex) {
				throw new Exception("Cannot parse SEAM property line",ex);
			}
	}
}
