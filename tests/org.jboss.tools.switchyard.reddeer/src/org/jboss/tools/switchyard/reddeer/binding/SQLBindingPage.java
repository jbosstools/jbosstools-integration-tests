package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * SQL binding page
 * 
 * @author apodhrad
 * 
 */
public class SQLBindingPage extends OperationOptionsPage<SQLBindingPage> {

	public static final String NAME = "Name";
	public static final String QUERY = "Query*";
	public static final String DATA_SOURCE = "Data Source*";

	public SQLBindingPage setName(String name) {
		new LabeledText(NAME).setFocus();
		new LabeledText(NAME).setText(name);
		return this;
	}

	public String getName() {
		return new LabeledText(NAME).getText();
	}

	public SQLBindingPage setQuery(String query) {
		new LabeledText(QUERY).setFocus();
		new LabeledText(QUERY).setText(query);
		new LabeledText(NAME).setFocus();
		return this;
	}

	public String getQuery() {
		return new LabeledText(QUERY).getText();
	}

	public SQLBindingPage setDataSource(String dataSource) {
		new LabeledText(DATA_SOURCE).setFocus();
		new LabeledText(DATA_SOURCE).setText(dataSource);
		new LabeledText(NAME).setFocus();
		return this;
	}

	public String getDataSource() {
		return new LabeledText(DATA_SOURCE).getText();
	}
}
