package org.jboss.tools.batch.ui.bot.test.editor.features;

import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.core.resources.ProjectItem;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.eclipse.reddeer.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.swt.api.Text;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.FinishButton;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.tools.batch.reddeer.editor.BatchExceptions;
import org.jboss.tools.batch.reddeer.wizard.BatchArtifacts;
import org.junit.After;
import org.junit.Test;

/**
 * Test whether job.xml file is updated when renaming artifact class.
 * 
 * @author lvalach
 *
 */
@OpenPerspective(JavaPerspective.class)
public class RenameTest extends AbstractFeatureBaseTest {

	private static final String EXCEPTION_STEP_ID = "My-exception-step";

	private static final String PROPERTY_STEP_ID = "My-prop-step";

	private static final String READER_CLASS = "MyReader";

	private static final String WRITER_CLASS = "MyWriter";

	private static final String READER_ID = getBatchArtifactID(READER_CLASS);

	private static final String WRITER_ID = getBatchArtifactID(WRITER_CLASS);

	private static final String RENAMED_PREFIX = "Renamed";

	private static final String RENAMED_PROPERTY_NAME = "renamedTestProperty";

	/**
	 * Delete all files used in this test and create clean job xml file
	 */
	@After
	public void clean() {
		deleteItemIfExists(new String[] { JAVA_FOLDER, getPackage() });
	}

	@Test
	public void renameBatchlet() {
		this.createBatchletWithProperty();

		String newId = RENAMED_PREFIX + BATCHLET_PROPERTY_ID;
		String newFileName = RENAMED_PREFIX + BATCHLET_PROPERTY_JAVA_CLASS;
		String[] pathToClass = new String[] { JAVA_FOLDER, getPackage(), BATCHLET_PROPERTY_JAVA_CLASS };
		String[] pathToClassRenamed = new String[] { JAVA_FOLDER, getPackage(), newFileName };

		// Rename to "Renamed..." and search for reference
		assertTrue("Can't rename class " + BATCHLET_PROPERTY_JAVA_CLASS, renameClass(newId, pathToClass));
		assertTrue(searchForClassReference(JOB_XML_FILE, pathToClassRenamed));
		assertNoProblems();
	}

	@Test
	public void renameProperty() {
		this.createBatchletWithProperty();

		String[] pathToClass = new String[] { JAVA_FOLDER, getPackage(), BATCHLET_PROPERTY_JAVA_CLASS };

		// Rename property in bachlet class.
		renamePropertyInFile(PROPERTY_NAME, RENAMED_PROPERTY_NAME, pathToClass);

		// Search for renamed property
		assertTrue("Property with name " + RENAMED_PROPERTY_NAME + " was not found in search results.",
				searchForPropertyInFile(JOB_XML_FILE, RENAMED_PROPERTY_NAME,
						new String[] { JAVA_FOLDER, getPackage(), BATCHLET_PROPERTY_JAVA_CLASS }));
	}

	@Test
	public void renameException() {
		this.createExceptionClass();

		String newId = RENAMED_PREFIX + EXCEPTION_ID;
		String newFileName = RENAMED_PREFIX + EXCEPTION_JAVA_CLASS;
		String[] pathToClass = new String[] { JAVA_FOLDER, getPackage(), EXCEPTION_JAVA_CLASS };
		String[] pathToClassRenamed = new String[] { JAVA_FOLDER, getPackage(), newFileName };

		// Rename to "Renamed..." and search for reference
		assertTrue("Can't rename class " + EXCEPTION_JAVA_CLASS, renameClass(newId, pathToClass));
		assertTrue(searchForClassReference(JOB_XML_FILE, pathToClassRenamed));
		assertNoProblems();
	}

	protected boolean renameClass(String newName, String... path) {
		ProjectItem projectItem = getProject().getProjectItem(path);
		if (projectItem != null) {
			projectItem.select();
			new ContextMenuItem("Refactor", "Rename...").select();
			new WaitUntil(new ShellIsAvailable("Rename Compilation Unit"), TimePeriod.DEFAULT);
			Text name = new DefaultText(0);
			name.setText(newName);
			new FinishButton().click();
			new WaitWhile(new ShellIsAvailable("Rename Compilation Unit"), TimePeriod.DEFAULT);
			editor.save();
			return true;
		}
		return false;
	}

	protected void renamePropertyInFile(String oldPropertyName, String newPropertyName, String... path) {
		ProjectItem projectItem = getProject().getProjectItem(path);
		projectItem.open();

		ContentOutline outlineView = new ContentOutline();
		outlineView.open();

		DefaultTree tree = new DefaultTree();
		Collection<TreeItem> items = tree.getAllItems();

		for (TreeItem item : items) {
			if (item.getText().matches(oldPropertyName + ".*")) {
				item.select();
				break;
			}
		}

		new ContextMenuItem("Refactor", "Rename...").select();
		new WaitUntil(new ShellIsAvailable("Rename Field"), TimePeriod.DEFAULT);
		Text name = new DefaultText(0);
		name.setText(newPropertyName);
		new OkButton().click();
		new WaitWhile(new ShellIsAvailable("Rename Field"), TimePeriod.DEFAULT);

		editor.save();
	}

	/**
	 * Create batchlet class with one property and add these class to job xml
	 * file.
	 */
	private void createBatchletWithProperty() {
		createBatchArtifactWithProperty(BatchArtifacts.BATCHLET, BATCHLET_PROPERTY_ID, PROPERTY_NAME);
		closeEditor(BATCHLET_PROPERTY_JAVA_CLASS);
		addStep(PROPERTY_STEP_ID);
		addBatchlet(PROPERTY_STEP_ID, getBatchArtifactID(BATCHLET_PROPERTY_ID));
		getDesignPage().addProperty(PROPERTY_STEP_ID, "Batchlet", PROPERTY_NAME, "xxx");
		editor.save();
	}

	/**
	 * Create exception class, reader and writer class, then Add step, chunk and
	 * exception class artifacts into the job xml file.
	 */
	private void createExceptionClass() {
		// Create exception class, reader and writer class.
		createExceptionClass(EXCEPTION_ID);
		addDefaultSerialVersionID(EXCEPTION_JAVA_CLASS, 3);
		closeEditor(EXCEPTION_JAVA_CLASS);
		createBatchArtifact(BatchArtifacts.ITEM_READER, READER_CLASS);
		closeEditor(getFullFileName(READER_CLASS, "java"));
		createBatchArtifact(BatchArtifacts.ITEM_WRITER, WRITER_CLASS);
		closeEditor(getFullFileName(WRITER_CLASS, "java"));

		// Add step, chunk and exception class artifacts into the job xml file.
		addStep(EXCEPTION_STEP_ID);
		addChunk(EXCEPTION_STEP_ID);
		setReaderRef(EXCEPTION_STEP_ID, READER_ID);
		setWriterRef(EXCEPTION_STEP_ID, WRITER_ID);
		getDesignPage().addExceptionClass(EXCEPTION_STEP_ID, "Chunk", BatchExceptions.SKIPPABLE.getSectionName(),
				BatchExceptions.SKIPPABLE.getType(), getPackage() + "." + EXCEPTION_ID);

		editor.save();
	}
}
