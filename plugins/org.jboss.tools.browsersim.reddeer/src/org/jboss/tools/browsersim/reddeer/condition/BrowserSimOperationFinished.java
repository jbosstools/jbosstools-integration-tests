/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.browsersim.reddeer.condition;

import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IStreamMonitor;
import org.eclipse.debug.core.model.IStreamsProxy;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;

public class BrowserSimOperationFinished extends AbstractWaitCondition{
	
	private String operation;
	private IStreamsProxy proxy;
	private StreamListener listener;
	
	public BrowserSimOperationFinished(String operation, IStreamsProxy proxy) {
		this.proxy = proxy;
		this.operation = operation;
		this.listener = new StreamListener();
		this.proxy.getOutputStreamMonitor().addListener(listener);
	}

	@Override
	public boolean test() {
		return listener.hasFinished();
	}
	
	private class StreamListener implements IStreamListener{
		
		private boolean finished;
			
		@Override
		public void streamAppended(String message, IStreamMonitor monitor) {
			if(message.equals("done: "+operation)){
				finished = true;
			}	
		}
		
		public boolean hasFinished(){
			return finished;
		}
		
	}

}
