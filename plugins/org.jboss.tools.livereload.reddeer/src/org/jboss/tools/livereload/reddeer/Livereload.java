/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.livereload.reddeer;

import java.util.List;
import java.util.Map;

import org.eclipse.wst.server.core.IServer;
import org.eclipse.reddeer.common.logging.Logger;
import org.jboss.tools.livereload.core.internal.server.jetty.LiveReloadProxyServer;
import org.jboss.tools.livereload.core.internal.server.wst.LiveReloadServerBehaviour;
import org.jboss.tools.livereload.core.internal.service.EventFilter;
import org.jboss.tools.livereload.core.internal.service.EventService;
import org.jboss.tools.livereload.core.internal.service.Subscriber;

public class Livereload {
	
	protected static final Logger log = Logger.getLogger(Livereload.class);
	
	public static int getLivereloadPort(){
		Map<Subscriber, List<EventFilter>> subs = EventService.getInstance().getSubscribers();
		for(Subscriber s: subs.keySet()){
			if(s instanceof LiveReloadServerBehaviour){
				if(((LiveReloadServerBehaviour)s).getServer().getServerState() != IServer.STATE_STARTED){
					continue;
				}
				Map<IServer, LiveReloadProxyServer> servers = ((LiveReloadServerBehaviour)s).getProxyServers();
				for(IServer srv: servers.keySet()){
					return servers.get(srv).getProxyPort();
				}
			}
		}
		return -1;
	}
	
	public static String getLivereloadURL(String projectName, String webPage){
		String lURL = "http://localhost:"+getLivereloadPort()+"/"+projectName+"/"+webPage;
		log.info("livereload URL is "+lURL);
		return lURL;
		
	}

}
