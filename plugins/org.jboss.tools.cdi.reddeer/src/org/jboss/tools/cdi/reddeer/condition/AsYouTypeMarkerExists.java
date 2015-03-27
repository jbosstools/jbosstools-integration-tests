package org.jboss.tools.cdi.reddeer.condition;

import java.util.List;

import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.workbench.api.Editor;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.Marker;

public class AsYouTypeMarkerExists implements WaitCondition{
	
	private String message;
	private String editor;
	
	public AsYouTypeMarkerExists(String editor, String message) {
		this.message = message;
		this.editor = editor;
	}

	@Override
	public boolean test() {
		Editor e = new DefaultEditor(editor);
		List<Marker> markers = e.getMarkers();
		boolean markerFound = false;
		for(Marker m: markers){
			if(m.getText().contains(message)){
				markerFound = true;
				break;
			}
		}
		return markerFound;
	}

	@Override
	public String description() {
		return "No as-you-type marker exists in '" + 
				editor + "'";
	}

}
