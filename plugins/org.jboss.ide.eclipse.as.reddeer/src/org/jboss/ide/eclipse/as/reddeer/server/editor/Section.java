package org.jboss.ide.eclipse.as.reddeer.server.editor;

import org.jboss.reddeer.swt.lookup.WidgetLookup;
import org.jboss.reddeer.swt.matcher.TextMatcher;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.widgets.Widget;
import org.jboss.reddeer.uiforms.hyperlink.UIFormHyperlink;

public class Section implements Widget {

	private org.eclipse.ui.forms.widgets.Section swtSection;
	
	private String title;
	
	public Section() {
		swtSection = WidgetLookup.getInstance().activeWidget(null, org.eclipse.ui.forms.widgets.Section.class, 0);
	}
	
	public Section(String title) {
		swtSection = WidgetLookup.getInstance().activeWidget(null, org.eclipse.ui.forms.widgets.Section.class, 0, new TextMatcher(title));
		this.title = title;
	}
	
	@Override
	public org.eclipse.ui.forms.widgets.Section getSWTWidget() {
		return swtSection;
	}

	@Override
	public boolean isEnabled() {
		return WidgetLookup.getInstance().isEnabled(swtSection);
	}

	public void open(){
		if (title == null){
			throw new UnsupportedOperationException("Cannot open section without a title");
		}
		
		Display.syncExec(new Runnable() {
			
			@Override
			public void run() {
				if (!swtSection.isExpanded()){
					new UIFormHyperlink(title).activate();
				}					
			}
		});
	}
}
