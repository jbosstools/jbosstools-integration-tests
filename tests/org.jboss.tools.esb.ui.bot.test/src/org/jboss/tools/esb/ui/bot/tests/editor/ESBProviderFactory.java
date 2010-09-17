package org.jboss.tools.esb.ui.bot.tests.editor;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

public class ESBProviderFactory {
	public static ESBProvider providerBus() {
		return new ESBProvider("Bus Provider", "bus-provider");
	}
	public static ESBProvider providerFS() {
		return new ESBProvider("FS Provider", "fs-provider");
	}
	public static ESBProvider providerFTP() {
		return new ESBProvider("FTP Provider", "ftp-provider") {
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(IDELabel.Button.NEXT), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().text(1).setText("localhost");
				Assertions.assertButtonEnabled(shell.bot().button(IDELabel.Button.NEXT), true);
				shell.bot().button(IDELabel.Button.NEXT).click();
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}
	public static ESBProvider providerHibernate() {
		return new ESBProvider("Hibernate Provider", "hibernate-provider"){
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(IDELabel.Button.NEXT), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().text(1).setText("hibernate.cfg.xml");
				Assertions.assertButtonEnabled(shell.bot().button(IDELabel.Button.NEXT), true);
				shell.bot().button(IDELabel.Button.NEXT).click();
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}
	public static ESBProvider providerHTTP() {
		return new ESBProvider("HTTP Provider", "http-provider");
	}
	public static ESBProvider providerJBR() {
		return new ESBProvider("JBR Provider", "jbr-provider") {
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(IDELabel.Button.NEXT), false);
				shell.bot().text(0).setText(this.uiName);
				Assertions.assertButtonEnabled(shell.bot().button(IDELabel.Button.NEXT), true);
				shell.bot().button(IDELabel.Button.NEXT).click();
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().text(1).setText("8888");
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}
	public static ESBProvider providerJCA() {
		return new ESBProvider("JCA Provider", "jms-jca-provider") {
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(IDELabel.Button.NEXT), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().text(1).setText(this.uiName);
				Assertions.assertButtonEnabled(shell.bot().button(IDELabel.Button.NEXT), true);
				shell.bot().button(IDELabel.Button.NEXT).click();
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}
	public static ESBProvider providerJMS() {
		return new ESBProvider("JMS Provider", "jms-provider") {
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(IDELabel.Button.NEXT), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().text(1).setText(this.uiName);
				Assertions.assertButtonEnabled(shell.bot().button(IDELabel.Button.NEXT), true);
				shell.bot().button(IDELabel.Button.NEXT).click();
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}
	public static ESBProvider providerSchedule() {
		return new ESBProvider("Schedule Provider", "schedule-provider") {
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}
	public static ESBProvider providerSQL() {
		return new ESBProvider("SQL Provider", "sql-provider");
	}
	public static ESBProvider providerCamel() {
		return new ESBProvider("Camel Provider", "camel-provider");
	}
}
