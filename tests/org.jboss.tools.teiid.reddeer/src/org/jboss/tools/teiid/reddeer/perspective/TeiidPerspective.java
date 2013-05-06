package org.jboss.tools.teiid.reddeer.perspective;

import org.jboss.reddeer.eclipse.ui.perspectives.AbstractPerspective;
import org.jboss.tools.teiid.reddeer.view.ModelExplorerView;
import org.jboss.tools.teiid.reddeer.view.TeiidInstanceView;

/**
 * Represents a Teiid perspective. It is a singleton. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class TeiidPerspective extends AbstractPerspective {

	private static final String NAME = "Teiid Designer";
	
	private static final TeiidPerspective INSTANCE = new TeiidPerspective();
	
	private TeiidInstanceView teiidInstanceView;
	
	private ModelExplorerView modelExplorerView;
	
	private TeiidPerspective() {
		super(NAME);
	}
	
	public static final TeiidPerspective getInstance(){
		INSTANCE.open();
		return INSTANCE;
	}
	
	public TeiidInstanceView getTeiidInstanceView(){
		if (teiidInstanceView == null){
			teiidInstanceView = new TeiidInstanceView();
		}
		return teiidInstanceView;
	}
	
	public ModelExplorerView getModelExplorerView() {
		if (modelExplorerView == null){
			modelExplorerView = new ModelExplorerView();
		}
		return modelExplorerView;
	}
}
