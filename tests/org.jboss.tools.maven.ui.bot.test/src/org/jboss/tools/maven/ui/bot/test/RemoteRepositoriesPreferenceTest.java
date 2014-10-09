package org.jboss.tools.maven.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.tools.maven.reddeer.maven.ui.preferences.RemoteRepositoriesPreferencePage;
import org.jboss.tools.maven.reddeer.wizards.Repository;
import org.junit.After;
import org.junit.Test;

public class RemoteRepositoriesPreferenceTest {
	
	private Repository r;
	
	@After
	public void cleanAddedRepo(){
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		RemoteRepositoriesPreferencePage rem = new RemoteRepositoriesPreferencePage();
		preferenceDialog.select(rem);
		if(rem.getRepository(r.getName())!=null){
			rem.deleteRepository(r);
		}
		preferenceDialog.ok();
	}
	
	
	public RemoteRepositoriesPreferenceTest(){
		r = new Repository("test", "testURL", true);
	}
	
	
	
	@Test
	public void addRepo(){
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		RemoteRepositoriesPreferencePage rem = new RemoteRepositoriesPreferencePage();
		preferenceDialog.select(rem);
		rem.addRepository(r);
		assertNotNull("Repository was not added",rem.getRepository(r.getName()));
		preferenceDialog.ok();
	}
	
	@Test
	public void removeRepo(){
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		RemoteRepositoriesPreferencePage rem = new RemoteRepositoriesPreferencePage();
		preferenceDialog.select(rem);
		rem.addRepository(r);
		rem.deleteRepository(r);
		assertNull("Repository was not deleted",rem.getRepository(r.getName()));
		preferenceDialog.ok();
	}
	
	@Test
	public void modifyRepo(){
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		RemoteRepositoriesPreferencePage rem = new RemoteRepositoriesPreferencePage();
		preferenceDialog.select(rem);
		rem.addRepository(r);
		Repository r1= new Repository("test1", "testURL1", false);
		rem.modifyRepository(r, r1);
		assertNull("Repository was not modified",rem.getRepository(r.getName()));
		assertNotNull("Repository was not modified",rem.getRepository(r1.getName()));
		assertEquals("Repository was not modified",rem.getRepository(r1.getName()).getUrl(), r1.getUrl());
		assertEquals("Repository was not modified",rem.getRepository(r1.getName()).isEnabled(), r1.isEnabled());
		preferenceDialog.ok();
	}

}
