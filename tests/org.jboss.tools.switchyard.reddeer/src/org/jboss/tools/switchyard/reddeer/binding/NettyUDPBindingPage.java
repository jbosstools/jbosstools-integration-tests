package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Netty UDP binding page
 * 
 * @author apodhrad
 * 
 */
public class NettyUDPBindingPage extends OperationOptionsPage<NettyUDPBindingPage> {

	public static final String NAME = "Name";
	public static final String HOST = "Host*";
	public static final String PORT = "Port*";

	public NettyUDPBindingPage setName(String name) {
		new LabeledText(NAME).setFocus();
		new LabeledText(NAME).setText(name);
		return this;
	}

	public String getName() {
		return new LabeledText(NAME).getText();
	}

	public NettyUDPBindingPage setHost(String host) {
		new LabeledText(HOST).setFocus();
		new LabeledText(HOST).setText(host);
		new LabeledText(NAME).setFocus();
		return this;
	}

	public String getHost() {
		return new LabeledText(HOST).getText();
	}

	public NettyUDPBindingPage setPort(String port) {
		new LabeledText(PORT).setFocus();
		new LabeledText(PORT).setText(port);
		new LabeledText(NAME).setFocus();
		return this;
	}

	public String getPort() {
		return new LabeledText(PORT).getText();
	}
}
