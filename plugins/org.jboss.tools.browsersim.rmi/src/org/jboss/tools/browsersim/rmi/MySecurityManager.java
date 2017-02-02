package org.jboss.tools.browsersim.rmi;

import java.io.FileDescriptor;
import java.security.Permission;

public class MySecurityManager extends SecurityManager{
	
	@Override
	public void checkPermission(Permission perm) {

	}
	
	@Override
	public void checkAccess(Thread t) {
	}
	
	@Override
	public void checkWrite(String file) {
	}
	
	@Override
	public void checkWrite(FileDescriptor fd) {
	}
	
	

}
