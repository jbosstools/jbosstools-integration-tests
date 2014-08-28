package org.jboss.tools.ws.ui.bot.test.rest;

import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.StringContains;
import org.jboss.reddeer.swt.keyboard.Keyboard;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.workbench.exception.WorkbenchPartNotFound;
import org.jboss.reddeer.workbench.impl.editor.Marker;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ws.reddeer.editor.ExtendedTextEditor;
import org.junit.After;
import org.junit.Test;

/**
 * Testing as-you-type validation.
 *
 * @author Radoslav Rabara
 *
 * @see http://tools.jboss.org/documentation/whatsnew/jbosstools/4.2.0.Beta3.html#as-you-type-validation
 * @since 4.2.0.Beta3
 */
public class AsYouTypeValidationTest extends RESTfulTestBase {

	@After
	public void cleanup() {
		//save the modified file
		try {
			new TextEditor().save();
		} catch(WorkbenchPartNotFound e) {
			
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
		assertCountOfErrors(getWsProjectName(), 0);

		/* change pathparam's value to be invalid */
		openJavaFile(getWsProjectName(), "org.rest.test", "RestService.java");
		ExtendedTextEditor textEditor = new ExtendedTextEditor();
		int lineNumber = textEditor.getLineNum(StringContains.containsString("}")) -1;
		textEditor.selectLine(lineNumber);
		Keyboard keyboard = KeyboardFactory.getKeyboard();
		keyboard.type("\t@Path(\"{id}\")\n\tpublic void test(@PathParam(\"test\") Integer test){\n");//enclosing } will be added automatically by editor
		AbstractWait.sleep(TimePeriod.getCustom(2));

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
		fail("Error marked with text '" + EXPECTED_ERROR + "' was not found.\n" + Arrays.toString(markers.toArray()));
	}
}
