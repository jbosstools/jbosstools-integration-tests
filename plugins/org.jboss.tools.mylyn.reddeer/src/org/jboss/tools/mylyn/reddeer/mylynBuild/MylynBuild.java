package org.jboss.tools.mylyn.reddeer.mylynBuild;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.mylyn.tasks.ui.views.TaskListView;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;

	/**
	 * Represents a TaskList on {@link TaskListView}. 
	 * 
	 * @author ldimaggi
	 * 
	 */
	public class MylynBuild {

		private static final TimePeriod TIMEOUT = TimePeriod.VERY_LONG;
		
		protected final Logger log = Logger.getLogger(this.getClass());
		
		private TreeItem treeItem;

		public MylynBuild(TreeItem treeItem) {
			this.treeItem = treeItem;
		}

		public void delete() {
			delete(false);
		}

		public void delete(boolean stopFirst) {
			log.info("Deleting Build");
			select();
			new ContextMenuItem("Delete").select();	
			new PushButton("OK").click();
			new WaitUntil(new TreeItemIsDisposed(treeItem), TIMEOUT);
			new WaitWhile(new JobIsRunning(), TIMEOUT);
		}

		protected void select() {
			treeItem.select();
		}

		public String getName(){
			return treeItem.getText();
		}

		private class TreeItemIsDisposed extends AbstractWaitCondition {

			private TreeItem treeItem;

			public TreeItemIsDisposed(TreeItem treeItem) {
				this.treeItem = treeItem;
			}

			@Override
			public boolean test() {
				return treeItem.isDisposed();
			}

			@Override
			public String description() {
				return "Build tree item is disposed";
			}
		}
	}
