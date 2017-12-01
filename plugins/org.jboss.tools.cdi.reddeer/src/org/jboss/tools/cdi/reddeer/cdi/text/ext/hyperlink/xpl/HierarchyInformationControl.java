package org.jboss.tools.cdi.reddeer.cdi.text.ext.hyperlink.xpl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.exception.CoreLayerException;
import org.eclipse.reddeer.core.handler.TableItemHandler;
import org.eclipse.reddeer.core.lookup.ShellLookup;
import org.eclipse.reddeer.core.lookup.WidgetLookup;
import org.eclipse.reddeer.core.matcher.WithTextMatcher;
import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.swt.api.Table;
import org.eclipse.reddeer.swt.api.TableItem;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.shell.AbstractShell;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.swt.widgets.Control;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class HierarchyInformationControl extends AbstractShell{
	
	public static final String OBSERVER_LABEL = "CDI Observer Methods";
	public static final String EVENTS_LABEL = "CDI Events";
	
	public HierarchyInformationControl(String label) {
		super(lookForShellWithLabel(label));
		setFocus();
	}
	
	public void selectProposal(String proposal){
		Table observerTable = new DefaultTable();		
		TableItemHandler.getInstance().setDefaultSelection(observerTable.getItem(proposal).getSWTWidget());
		new WaitWhile(new ShellIsAvailable(this));
	}
	
	public List<String> getProposals(){
		Table observerTable = new DefaultTable();
		List<String> toReturn = new ArrayList<String>();
		for(TableItem ti: observerTable.getItems()){
			toReturn.add(ti.getText());
		}
		return toReturn;
	}
	
	public Table getProposalsTable(){
		return new DefaultTable();
	}

	private static org.eclipse.swt.widgets.Shell lookForShellWithLabel(final String label) {
		Matcher<String> labelMatcher = new BaseMatcher<String>() {
			public boolean matches(Object obj) {
				if (obj instanceof Control) {
					final Control control = (Control) obj;
					ReferencedComposite ref = new ReferencedComposite() {
						public Control getControl() {
							return control;
						}
					};
					try {
						org.eclipse.swt.widgets.Label l = WidgetLookup.getInstance().
								activeWidget(ref, org.eclipse.swt.widgets.Label.class, 0, TimePeriod.NONE, new WithTextMatcher(label));
						if(l != null){
							return true;
						}
						return false;
					} catch (CoreLayerException e) {
						// ok, this control doesn't contain the label
					}
				}
				return false;
			}

			public void describeTo(Description description) {
				description.appendText("containing label '" + label + "'");
			}
		};
		return ShellLookup.getInstance().getShell(TimePeriod.LONG, labelMatcher);
	}

}
