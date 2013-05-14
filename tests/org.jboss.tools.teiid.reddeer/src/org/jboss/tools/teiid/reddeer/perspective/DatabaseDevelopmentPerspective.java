package org.jboss.tools.teiid.reddeer.perspective;

import org.jboss.reddeer.eclipse.ui.perspectives.AbstractPerspective;
import org.jboss.tools.teiid.reddeer.view.DataSourceExplorer;
import org.jboss.tools.teiid.reddeer.view.SQLResultView;

/**
 * Represents a Database development perspective. It is a singleton.
 * 
 * @author Lucia Jelinkova
 *
 */
public class DatabaseDevelopmentPerspective extends AbstractPerspective {

	private static final String NAME = "Database Development";
	
	private static final DatabaseDevelopmentPerspective INSTANCE = new DatabaseDevelopmentPerspective();
	
	private DataSourceExplorer explorerView;
	
	private SQLResultView sqlResultsView;
	
	private DatabaseDevelopmentPerspective() {
		super(NAME);
	}

	public static final DatabaseDevelopmentPerspective getInstance(){
		INSTANCE.open();
		return INSTANCE;
	}
	
	public DataSourceExplorer getExplorerView() {
		if (explorerView == null){
			explorerView = new DataSourceExplorer();
		}
		return explorerView;
	}
	
	public SQLResultView getSqlResultsView() {
		if (sqlResultsView == null){
			sqlResultsView = new SQLResultView();
		}
		return sqlResultsView;
	}
}
