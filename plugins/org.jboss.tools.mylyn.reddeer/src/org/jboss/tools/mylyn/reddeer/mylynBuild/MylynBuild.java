package org.jboss.tools.mylyn.reddeer.mylynBuild;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;

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
			new ContextMenu("Delete").select();	
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
