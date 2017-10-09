package org.jboss.tools.ws.ui.bot.test.rest;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.List;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.core.exception.CoreLayerException;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.workbench.condition.EditorHasValidationMarkers;
import org.eclipse.reddeer.workbench.impl.editor.Marker;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Testing as-you-type validation.
 *
 * @author Radoslav Rabara
 *
 * @see http://tools.jboss.org/documentation/whatsnew/jbosstools/4.2.0.Beta3.html#as-you-type-validation
 * @since 4.2.0.Beta3
 */
@RunWith(RedDeerSuite.class)
public class AsYouTypeValidationTest extends RESTfulTestBase {

	@After
	public void cleanup() {
		//save the modified file
		try {
			new TextEditor().save();
		} catch(CoreLayerException e) {
			
		}
		//delete the project
		super.cleanup();
	}

	@Override
	protected String getWsProjectName() {
		return "restAsYouTypeValidation";
	}

	@Test
	public void invalidPathParamTest() {
		/* assert there is no error */
		assertCountOfProblemsExists(ProblemType.ERROR, getWsProjectName(), null, null, 0);

		/* change pathparam's value to be invalid */
		openJavaFile(getWsProjectName(), "org.rest.test", "RestService.java");
		TextEditor textEditor = new TextEditor();
		int lineNumber = textEditor.getLineOfText("}") - 1;
		textEditor.insertLine(lineNumber, "\t@Path(\"{id}\")\n\tpublic void test(@PathParam(\"test\") Integer test){}\n");
		textEditor.save();
		
		new WaitUntil(new EditorHasValidationMarkers(textEditor, 9));

		/* error shows */
		List<Marker> markers = textEditor.getMarkers();
		assertThat("Number of markers in editor", markers.size(), IsNot.not(Is.is(0)));
		assertExpectedErrorIsPresent(markers);
	}
	
	private void assertExpectedErrorIsPresent(List<Marker> markers) {
		final String EXPECTED_ERROR = "@PathParam value 'test' does not match any @Path annotation template parameters of the java method 'test' and its enclosing java type 'org.rest.test.RestService'.";
		for(Marker m : markers) {
			if(m.getText().equals(EXPECTED_ERROR)) {
				return;
			}
		}
		fail("Error marked with text '" + EXPECTED_ERROR + "' was not found.\n" + getMarkersText(markers));
	}

	private String getMarkersText(List<Marker> markers) {
		StringBuilder text = new StringBuilder();
		for(int i=0;i<markers.size();i++) {
			if (i>0) {
				text.append(", ");
			}
			text.append(markers.get(i).getText());
		}
		return text.toString();
	}
}
