/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.reddeer.annotation;

public enum ValidationType {
	
	SERIALIZABLE, MULTIPLE_BEAN_ELIGIBLE, NO_BEAN_ELIGIBLE, DISPOSES, OBSERVES,
	PRODUCES, TARGET, RETENTION, TYPED, NAMED, NONBINDING, SPECIALIZES, STATELESS,
	NO_CLASS, NO_ANNOTATION, NO_ALTERNATIVE_STEREOTYPE,
	NO_ALTERNATIVE, NO_DECORATOR, NO_INTERCEPTOR, NO_BEANS_XML 

}
