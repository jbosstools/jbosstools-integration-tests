package org.jboss.tools.cdi.reddeer.cdi.text.ext.hyperlink;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.reddeer.swt.api.TableItem;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;

public class AssignableBeansDialog extends DefaultShell{
	
	private static final String UNAVAILABLE_BEANS = "Unavailable Beans";
	
	private static final String DECORATOR = "@Decorator";
	
	private static final String INTERCEPTOR = "@Interceptor";
	
	private static final String ELIMINATED_AMBIGUOUS = "Eliminated ambiguous";
	
	private static final String UNSELECTED_ALTERNATIVE = "Unselected @Alternative";
	
	private static final String UNAVAILABLE_PRODUCER = "@Produces in unavailable bean"; 
	
	private static final String SPECIALIZED_BEANS = "Specialized beans";
	
	public AssignableBeansDialog(){
		super("Assignable Beans");
	}
	
	public List<String> getAllBeans() {
		List<String> allBeans = new ArrayList<String>();
		for (TableItem i: new DefaultTable().getItems()) {
			allBeans.add(i.getText());
		}
		return allBeans;
	}
	
	public void typeInFilter(String text) {
		DefaultText filterText = new DefaultText(0);
		filterText.setText(""); // clear filter textbox
		filterText.setText(text);
	}
	
	public void hideUnavailableBeans() {
		getTreeItem(UNAVAILABLE_BEANS).setChecked(false);
	}
	
	public void showUnavailableBeans() {
		getTreeItem(UNAVAILABLE_BEANS).setChecked(true);
	}
	
	public void hideDecorators() {
		getTreeItem(UNAVAILABLE_BEANS, DECORATOR).setChecked(false);
	}
	
	public void showDecorators() {
		getTreeItem(UNAVAILABLE_BEANS, DECORATOR).setChecked(true);
	}
	
	public void hideInterceptors() {
		getTreeItem(UNAVAILABLE_BEANS, INTERCEPTOR).setChecked(false);
	}
	
	public void showInterceptors() {
		getTreeItem(UNAVAILABLE_BEANS, INTERCEPTOR).setChecked(true);
	}
	
	public void hideUnselectedAlternatives() {
		getTreeItem(UNAVAILABLE_BEANS, UNSELECTED_ALTERNATIVE).setChecked(false);
	}
	
	public void showUnselectedAlternatives() {
		getTreeItem(UNAVAILABLE_BEANS, UNSELECTED_ALTERNATIVE).setChecked(true);
	}
	
	public void hideUnavailableProducers() {
		getTreeItem(UNAVAILABLE_BEANS, UNAVAILABLE_PRODUCER).setChecked(false);
	}
	
	public void showUnavailableProducers() {
		getTreeItem(UNAVAILABLE_BEANS, UNAVAILABLE_PRODUCER).setChecked(true);
	}
	
	public void hideSpecializedBeans() {
		getTreeItem(UNAVAILABLE_BEANS, SPECIALIZED_BEANS).setChecked(false);
	}
	
	public void showSpecializedBeans() {
		getTreeItem(UNAVAILABLE_BEANS, SPECIALIZED_BEANS).setChecked(true);
	}
	
	public void hideAmbiguousBeans() {
		getTreeItem(ELIMINATED_AMBIGUOUS).setChecked(false);
	}
	
	public void showAmbiguousBeans() {
		getTreeItem(ELIMINATED_AMBIGUOUS).setChecked(true);
	}
	
	protected TreeItem getTreeItem(String... path) {
		return new DefaultTreeItem(path);
	}

}
