package org.jboss.tools.portlet.ui.bot.task.debug;

import java.io.PrintStream;

import javax.swing.table.TableColumn;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.hamcrest.Matcher;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.portlet.ui.bot.task.finder.BasicWidgetsVisitor;
import org.jboss.tools.portlet.ui.bot.task.finder.WidgetFindingTask;

/**
 * Finds all widgets recursively and writes debuggin information about each of them. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class PrintWidgetsTask extends AbstractSWTTask {

	private Matcher<? extends Widget> matcher;

	private Widget parentWidget;
	
	private PrintStream stream;
	
	public PrintWidgetsTask() {
		this(null);
	}

	public PrintWidgetsTask(Matcher<? extends Widget> matcher) {
		this(matcher, null);
	}
	
	public PrintWidgetsTask(Matcher<? extends Widget> matcher, Widget parentWidget) {
		this(matcher, parentWidget, System.out);
	}
	
	public PrintWidgetsTask(Matcher<? extends Widget> matcher, Widget parentWidget, PrintStream stream) {
		super();
		this.matcher = matcher;
		this.parentWidget = parentWidget;
		this.stream = stream;
	}

	@Override
	public void perform() {
		stream.println("--------------- Start of the list of widgets ---------------");
		performInnerTask(new WidgetFindingTask(parentWidget, matcher, new WriteAllWidgetsVisitor()));
		stream.println("--------------- End of the list of widgets ---------------");
	}

	private class WriteAllWidgetsVisitor extends BasicWidgetsVisitor {

		@Override
		protected void visitButton(Button widget) {
			stream.println(widget);
		}

		@Override
		protected void visitBrowser(Browser widget) {
			stream.println(widget);
		}

		@Override
		protected void visitCCombo(CCombo widget) {
			stream.println(widget);
		}

		@Override
		protected void visitCLabel(CLabel widget) {
			stream.println(widget);
		}

		@Override
		protected void visitCombo(Combo widget) {
			stream.println(widget);
		}

		@Override
		protected void visitCTabItem(CTabItem widget) {
			stream.println(widget);
		}

		@Override
		protected void visitDateTime(DateTime widget) {
			stream.println(widget);
		}

		@Override
		protected void visitExpandBar(ExpandBar widget) {
			stream.println(widget);
		}

		@Override
		protected void visitExpandItem(ExpandItem widget) {
			stream.println(widget);
		}

		@Override
		protected void visitLabel(Label widget) {
			stream.println(widget);
		}

		@Override
		protected void visitLink(Link widget) {
			stream.println(widget);
		}

		@Override
		protected void visitList(List widget) {
			stream.println(widget);
		}

		@Override
		protected void visitMenu(Menu widget) {
			stream.println(widget);
		}

		@Override
		protected void visitScale(Scale widget) {
			stream.println(widget);
		}

		@Override
		protected void visitShell(Shell widget) {
			stream.println(widget);
		}

		@Override
		protected void visitSlider(Slider widget) {
			stream.println(widget);
		}

		@Override
		protected void visitSpinner(Spinner widget) {
			stream.println(widget);
		}

		@Override
		protected void visitStyledText(StyledText widget) {
			stream.println(widget);
		}

		@Override
		protected void visitTabItem(TabItem widget) {
			stream.println(widget);
		}

		@Override
		protected void visitTable(Table widget) {
			stream.println(widget);
		}

		@Override
		protected void visitTableColumn(TableColumn widget) {
			stream.println(widget);
		}

		@Override
		protected void visitTableItem(TableItem widget) {
			stream.println(widget);
		}

		@Override
		protected void visitText(Text widget) {
			stream.print("Text {");
			stream.print(widget.getText());
			stream.println("}");
		}

		@Override
		protected void visitToolBar(ToolBar widget) {
			stream.print("Toolbar {");
			stream.print(widget.getRowCount() + " row(s), ");
			stream.print(widget.getItemCount() + " item(s)");
			stream.println("}");
		}

		@Override
		protected void visitToolItem(ToolItem widget) {
			stream.print("Toolitem {");
			stream.print("text = '" + widget.getText() + "', ");
			stream.print("tooltip = '" + widget.getToolTipText() + "'");
			stream.println("}");
		}
		
		@Override
		protected void visitTray(Tray widget) {
			stream.println(widget);
		}

		@Override
		protected void visitTree(Tree widget) {
			stream.print("Tree {");
			stream.print(widget.getItemCount() + " item(s), ");
			stream.print(widget.getColumnCount() + " columns(s), ");
			stream.print("items = {");
			for (SWTBotTreeItem item : new SWTBotTree(widget).getAllItems()){
				stream.print(item.getText() + ", ");
			}
			stream.print("}");
			stream.println("}");
		}

		@Override
		protected void visitTreeItem(TreeItem widget) {
			stream.println(widget);
		}
		
		@Override
		protected void visitHyperLink(Hyperlink widget) {
			stream.print("Hyperlink {");
			stream.print("text = '" + widget.getText() + "'");
			stream.println("}");
		}

		@Override
		protected void visitUnkownItem(Widget widget) {
			stream.println("Unknown: " + widget);
		}
	}
}
