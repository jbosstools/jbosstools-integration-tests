package org.jboss.tools.esb.ui.bot.tests.editor;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;

public class ESBListenerFactory {

	public static ESBListener listenerGeneric() {
		return new ESBListener("Listener", "listener");
	}
	public static ESBListener listenerFTP() {
		return new ESBListener("FTP Listener", "ftp-listener");
	}
	public static ESBListener listenerFS() {
		return new ESBListener("FS Listener", "fs-listener");
	}
	public static ESBListener listenerGroovy() {
		return new ESBListener("Groovy Listener", "groovy-listener") {
			@Override
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().text(1).setText(this.uiName + ".groovy");
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}
	public static ESBListener listenerHibernate() {
		return new ESBListener("Hibernate Listener", "hibernate-listener");
	}
	public static ESBListener listenerHTTPGateway() {
		return new ESBListener("HTTP Gateway", "http-gateway");
	}
	public static ESBListener listenerJBR() {
		return new ESBListener("JBR Listener", "jbr-listener");
	}
	public static ESBListener listenerJMS() {
		return new ESBListener("JMS Listener", "jms-listener");
	}
	public static ESBListener listenerJCA() {
		return new ESBListener("JCA Gateway", "jca-gateway") {
			@Override
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().text(1).setText(this.uiName + ".adapter");
				shell.bot().text(2).setText("java.lang.Object");
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}
	public static ESBListener listenerScheduled() {
		return new ESBListener("Scheduled Listener", "scheduled-listener");
	}
	public static ESBListener listenerSQL() {
		return new ESBListener("SQL Listener", "sql-listener");
	}
	public static ESBListener listenerUDP() {
		return new ESBListener("UDP Listener", "udp-listener") {
			@Override
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().text(1).setText("localhost");
				shell.bot().text(2).setText("123");
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}
}
