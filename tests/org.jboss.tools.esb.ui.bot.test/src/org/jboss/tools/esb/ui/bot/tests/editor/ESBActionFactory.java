package org.jboss.tools.esb.ui.bot.tests.editor;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.esb.ui.bot.tests.editor.action.Notifier;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.widgets.SWTBotSection;

public class ESBActionFactory {
	public static ESBAction customAction() {
		return new ESBAction("Custom Action",null,"java.lang.Object") {
			@Override
			public String getShellTitle() {
				return "Add Action";
			}
			@Override
			public String getFinishButton() {
				return IDELabel.Button.OK;
			}
			@Override
			public String getSectionTitle() {
				return "Action";
			}
			@Override
			protected void doEditing(SWTBotEditor editor, String... path) {
				SWTBotSection section = bot.section(editor.bot(),getSectionTitle());				
				editProcess(editor,false);
			}
			@Override
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().text(1).setText("java.lang.Object");
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}
	public static ESBAction bpmProcessor() {
		return new ESBAction("BPM Processor","BPM","org.jboss.soa.esb.services.jbpm.actions.BpmProcessor") {

			@Override
			public String getMenuLabel() {
				return this.uiName;
			}
			@Override
			public String getSectionTitle() {
				return "Bpm Processor Action";
			}
			@Override
			protected void doEditing(SWTBotEditor editor, String... path) {
				SWTBotSection section = bot.section(editor.bot(),getSectionTitle());
				editTextProperty(editor, section.bot(), "Process Definition Name:", "process-definition-name", "process");
				editProcess(editor,true);
			}
			@Override
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().comboBox().setSelection(1);
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);				
			}
			
		};
	}
	public static ESBAction bpmRulesProcessor() {
		return new ESBAction("Business Rules Processor","BPM","org.jboss.soa.esb.actions.BusinessRulesProcessor") {
			@Override
			public String getMenuLabel() {
				return this.uiName;
			}
			@Override
			public String getShellTitle() {
				return this.uiName+"...";
			}
			@Override
			public String getSectionTitle() {
				return "Business Rules Processor";
			}
			@Override
			protected void doEditing(SWTBotEditor editor, String... path) {
				SWTBotSection section = bot.section(editor.bot(),getSectionTitle());				
				editProcess(editor,true);
			}
		};
	}
	public static ESBAction byteArrayToString() {
		return new ESBAction("Byte Array To String","Converters/Transformers","org.jboss.soa.esb.actions.converters.ByteArrayToString") {
		};
	}
	public static ESBAction commandInterpreter() {
		return new ESBAction("Command Interpreter","Converters/Transformers","org.jboss.soa.esb.actions.jbpm.CommandInterpreter") {
		};
	}
	public static ESBAction longToDate() {
		return new ESBAction("Long To Date","Converters/Transformers","org.jboss.soa.esb.actions.converters.LongToDateConverter") {
		};
	}
	public static ESBAction messagePersister() {
		return new ESBAction("Message Persister","Converters/Transformers","org.jboss.soa.esb.actions.MessagePersister") {
			@Override
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().text(1).setText("java.lang.Object");
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}
	public static ESBAction objectInvoke() {
		return new ESBAction("Object Invoke","Converters/Transformers","org.jboss.soa.esb.actions.converters.ObjectInvoke") {
			@Override
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().text(1).setText("java.lang.Object");
				shell.bot().text(2).setText("toString");
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}
	public static ESBAction objectToCSVString() {
		return new ESBAction("Object To CSV String","Converters/Transformers","org.jboss.soa.esb.actions.converters.ObjectToCSVString") {
			@Override
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().text(1).setText("a=a");
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}	
	public static ESBAction objectToXStream() {
		return new ESBAction("Object To XStream","Converters/Transformers","org.jboss.soa.esb.actions.converters.ObjectToXStream") {
		};
	}
	public static ESBAction smooksAction() {
		return new ESBAction("Smooks Action","Converters/Transformers","org.jboss.soa.esb.smooks.SmooksAction") {
			@Override
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().text(1).setText("smooks-config.xml");
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}
	public static ESBAction xStreamToObject() {
		return new ESBAction("XStream To Object","Converters/Transformers","org.jboss.soa.esb.actions.converters.XStreamToObject") {
			@Override
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().text(2).setText("java.lang.Object");
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}
	public static ESBAction xsltAction() {
		return new ESBAction("XSLT Action","Converters/Transformers","org.jboss.soa.esb.actions.transformation.xslt.XsltAction") {
			@Override
			public String getMenuLabel() {
				return this.uiName;
			}
			@Override
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().text(1).setText("template.xsl");
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}
	public static ESBAction printline() {
		return new ESBAction("System Println","Miscellaneous","org.jboss.soa.esb.actions.SystemPrintln") {
			@Override
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().text(1).setText("Hello!");
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}
	public static ESBAction schemaValidation() {
		return new ESBAction("Schema Validation","Miscellaneous","org.jboss.soa.esb.actions.validation.SchemaValidationAction") {
			@Override
			public String getShellTitle() {
				return this.uiName+"...";
			}
			@Override
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().text(1).setText("schema.xsd");
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}
	public static ESBAction aggregator() {
		return new ESBAction("Aggregator","Routers","org.jboss.soa.esb.actions.Aggregator") {
		};
	}
	public static ESBAction routerDrools() {
		return new ESBAction("Content Based Router (Drools)","Routers","org.jboss.soa.esb.actions.ContentBasedRouter") {
			@Override
			public String getShellTitle() {
				return "Add Drools Router";
			}
			@Override
			public String getSectionTitle() {
				return "Content Based Router Action";
			}
			@Override
			protected void doEditing(SWTBotEditor editor, String... path) {
				SWTBotSection section = bot.section(editor.bot(),getSectionTitle());				
				editProcess(editor,true);
			}
			@Override
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().text(1).setText("rules");
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}
	public static ESBAction cbrGeneric() {
		return new ESBAction("Content Based Router (Generic)","Routers","org.jboss.soa.esb.actions.ContentBasedRouter") {
			@Override
			public String getShellTitle() {
				return "Add Content Based Router";
			}
			@Override
			public String getSectionTitle() {
				return "Content Based Router Action";
			}
			@Override
			protected void doEditing(SWTBotEditor editor, String... path) {
				SWTBotSection section = bot.section(editor.bot(),getSectionTitle());				
				editProcess(editor,true);
			}
			@Override
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().comboBox(0).setSelection(2);
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}
	public static ESBAction cbrRegex() {
		return new ESBAction("Content Based Router (Regex)","Routers","org.jboss.soa.esb.actions.ContentBasedRouter") {
			@Override
			public String getShellTitle() {
				return "Add Regex Router";
			}
			@Override
			public String getSectionTitle() {
				return "Content Based Router Action";
			}
			@Override
			protected void doEditing(SWTBotEditor editor, String... path) {
				SWTBotSection section = bot.section(editor.bot(),getSectionTitle());				
				editProcess(editor,true);
			}
		};
	}
	public static ESBAction cbrXpath() {
		return new ESBAction("Content Based Router (XPath)","Routers","org.jboss.soa.esb.actions.ContentBasedRouter") {
			@Override
			public String getShellTitle() {
				return "Add XPath Router";
			}
			@Override
			public String getSectionTitle() {
				return "Content Based Router Action";
			}
			@Override
			protected void doEditing(SWTBotEditor editor, String... path) {
				SWTBotSection section = bot.section(editor.bot(),getSectionTitle());				
				editProcess(editor,true);
			}
		};
	}
	public static ESBAction routerEcho() {
		return new ESBAction("Echo Router","Routers","org.jboss.soa.esb.actions.routing.EchoRouter") {
			@Override
			public String getShellTitle() {
				return "Add "+this.uiName;
			}
		};
	}
	public static ESBAction routerEmail() {
		return new ESBAction("EMail Router","Routers","org.jboss.soa.esb.actions.routing.email.EmailRouter") {
			@Override
			public String getShellTitle() {
				return "Add "+this.uiName;
			}
			@Override
			public String getMenuLabel() {
				return this.uiName;
			}
			@Override
			public String getSectionTitle() {
				return "Email Router Action";
			}
			@Override
			protected void doEditing(SWTBotEditor editor, String... path) {
				SWTBotSection section = bot.section(editor.bot(),getSectionTitle());				
				editProcess(editor,true);
			}
		};
	}
	public static ESBAction routerEmailWiretap() {
		return new ESBAction("EMail Wiretap","Routers","org.jboss.soa.esb.actions.routing.email.EmailWiretap") {
			@Override
			public String getMenuLabel() {
				return this.uiName;
			}
			@Override
			public String getSectionTitle() {
				return "Email Wiretap Action";
			}
			@Override
			protected void doEditing(SWTBotEditor editor, String... path) {
				SWTBotSection section = bot.section(editor.bot(),getSectionTitle());				
				editProcess(editor,true);
			}
		};
	}
	public static ESBAction routerHTTPRouter() {
		return new ESBAction("HTTP Router","Routers","org.jboss.soa.esb.actions.routing.http.HttpRouter") {
			@Override
			public String getShellTitle() {
				return "Add HTTP Wiretap";
			}
			@Override
			public String getMenuLabel() {
				return this.uiName;
			}
		};
	}
	public static ESBAction routerJMS() {
		return new ESBAction("JMS Router","Routers","org.jboss.soa.esb.actions.routing.JMSRouter") {
			@Override
			public String getMenuLabel() {
				return this.uiName;
			}
			@Override
			protected void doEditing(SWTBotEditor editor, String... path) {
				SWTBotSection section = bot.section(editor.bot(),getSectionTitle());				
				editProcess(editor,true);
			}
		};
	}
	public static ESBAction notifier() {
		return new Notifier();
	}
	public static ESBAction staticRouter() {
		return new ESBAction("Static Router","Routers","org.jboss.soa.esb.actions.StaticRouter") {
			@Override
			protected void doEditing(SWTBotEditor editor, String... path) {
				SWTBotSection section = bot.section(editor.bot(),getSectionTitle());				
				editProcess(editor,true);
			}
		};
	}
	public static ESBAction staticWiretap() {
		return new ESBAction("Static Wiretap","Routers","org.jboss.soa.esb.actions.StaticWiretap") {
			@Override
			protected void doEditing(SWTBotEditor editor, String... path) {
				SWTBotSection section = bot.section(editor.bot(),getSectionTitle());				
				editProcess(editor,true);
			}
		};
	}
	public static ESBAction syncServiceInvoker() {
		return new ESBAction("Sync Service Invoker","Routers","org.jboss.soa.esb.actions.SyncServiceInvoker") {
			@Override
			public String getMenuLabel() {
				return this.uiName;
			}
			@Override
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().text(1).setText("Category");
				shell.bot().text(2).setText("HelloService");
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}
	public static ESBAction scripting() {
		return new ESBAction("Scripting","Scripting","org.jboss.soa.esb.actions.scripting.ScriptingAction") {
			@Override
			public String getMenuLabel() {
				return this.uiName;
			}
			@Override
			public String getShellTitle() {
				return "Add Scripting Action";
			}
			@Override
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().text(1).setText("script");
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}
	public static ESBAction groovyActionProcessor() {
		return new ESBAction("Groovy Action Processor","Scripting","org.jboss.soa.esb.actions.scripting.GroovyActionProcessor") {
			@Override
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().text(1).setText("script.groovy");
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}
	public static ESBAction ejbProcessor() {
		return new ESBAction("EJB Processor","Services","org.jboss.soa.esb.actions.EJBProcessor") {
			@Override
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().text(1).setText("script.groovy");
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
			@Override
			public String getMenuLabel() {
				return this.uiName;
			}
		};
	}
	public static ESBAction soapProcessor() {
		return new ESBAction("SOAP Processor","Webservices","org.jboss.soa.esb.actions.soap.SOAPProcessor") {
			@Override
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().text(1).setText("endpoint");
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}
	public static ESBAction soapClient() {
		return new ESBAction("SOAP Client","Webservices","org.jboss.soa.esb.actions.soap.SOAPClient") {
			@Override
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().text(1).setText("wsdl");
				shell.bot().text(2).setText("action");
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}
	public static ESBAction soapProxy() {
		return new ESBAction("SOAP Proxy","Webservices","org.jboss.soa.esb.actions.soap.proxy.SOAPProxy") {
			@Override
			public String getMenuLabel() {
				return this.uiName;
			}
			@Override
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().text(1).setText("endpoint");
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}
	public static ESBAction soapWiseClient() {
		return new ESBAction("SOAP Wise Client","Webservices","org.jboss.soa.esb.actions.soap.wise.SOAPClient") {
			@Override
			public String getMenuLabel() {
				return this.uiName;
			}
			@Override
			protected void doFillForm(SWTBotShell shell) {
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
				shell.bot().text(0).setText(this.uiName);
				shell.bot().text(1).setText("wsdl");
				shell.bot().text(2).setText("action");
				Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
			}
		};
	}
}
