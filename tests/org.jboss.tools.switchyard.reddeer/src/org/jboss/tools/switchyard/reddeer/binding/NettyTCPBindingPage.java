package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Netty TCP binding page
 * 
 * @author apodhrad
 * 
 */
public class NettyTCPBindingPage extends OperationOptionsPage<NettyTCPBindingPage> {

	public static final String NAME = "Name";
	public static final String HOST = "Host*";
	public static final String PORT = "Port*";

	public NettyTCPBindingPage setName(String name) {
		new LabeledText(NAME).setFocus();
		new LabeledText(NAME).setText(name);
		return this;
	}

	public String getName() {
		return new LabeledText(NAME).getText();
	}

	public NettyTCPBindingPage setHost(String host) {
		new LabeledText(HOST).setFocus();
		new LabeledText(HOST).setText(host);
		new LabeledText(NAME).setFocus();
		return this;
	}

	public String getHost() {
		return new LabeledText(HOST).getText();
	}

	public NettyTCPBindingPage setPort(String port) {
		new LabeledText(PORT).setFocus();
		new LabeledText(PORT).setText(port);
		new LabeledText(NAME).setFocus();
		return this;
	}

	public String getPort() {
		return new LabeledText(PORT).getText();
	}
}
