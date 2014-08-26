package org.jboss.tools.ws.reddeer.ui.wizards.jaxrs;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * First and the only {@link JAXRSApplicationWizard} page.
 *
 * Page offers 2 options have to create a new JAX-RS Application:
 * 1. "Subclass of javax.ws.rs.core.Application()" - {@link #useSubclassOfApplication()}
 * 2. "Defined in the web deployment descriptor" - {@link #useDeploymentDescriptor()}
 *
 * @author Radoslav Rabara
 *
 */
public class JAXRSApplicationWizardPage extends WizardPage {
	/**
	 * Returns info text ( = info/warning/error)
	 * @return
	 */
	public String getWizardPageInfoText() {
		return new DefaultText(5).getText();
	}
	
	public SubclassOfApplicationWizardPart useSubclassOfApplication() {
		new RadioButton(0).click();//"Subclass of javax.ws.rs.core.Application"
		return new SubclassOfApplicationWizardPart();
	}
	
	public DeploymentDescriptorWizardPart useDeploymentDescriptor() {
		new RadioButton(1).click();//"Defined in the web deployment descriptor"
		return new DeploymentDescriptorWizardPart();
	}
	
	public class DeploymentDescriptorWizardPart {
		public void setApplicationPath(String path) {
			new LabeledText("Application path:").setText(path);
		}
	}
	
	public class SubclassOfApplicationWizardPart {
		//source folder
		public String getSourceFolder() {
			return getSourceFolderText().getText();
		}
		
		public void setSourceFolder(String srcFolder) {
			getSourceFolderText().setText(srcFolder);
		}
		
		private Text getSourceFolderText() {
			return new LabeledText("Source folder:");
		}
		
		//browse source folder
		public void openBrowseSourceFolder() {
			getBrowseSourceFolderButton().click();
		}
		
		public boolean isBrowseSourceFolderEnabled() {
			return getBrowseSourceFolderButton().isEnabled();
		}
		
		private PushButton getBrowseSourceFolderButton() {
			PushButton browse =  new PushButton(0);
			assert browse.getText().equals("Br&owse...");
			return browse;
		}
		
		//package
		public void setPackage(String pkg) {
			new LabeledText("Package:").setText(pkg);
		}
		
		//browse package
		public void openBrowsePackage() {
			getBrowsePackageButton().click();
		}
		
		public boolean isBrowsePackageEnabled() {
			return getBrowsePackageButton().isEnabled();
		}
		
		private PushButton getBrowsePackageButton() {
			PushButton browse =  new PushButton(1);
			assert browse.getText().equals("Bro&wse...");
			return browse;
		}
		
		//name
		public void setName(String name) {
			new LabeledText("Name:").setText(name);
		}
		
		//application path
		public void setApplicationPath(String appPath) {
			new LabeledText("Application path:").setText(appPath);
		}
		
		//configure templates and default values
		public void configureTemplatesAndDefaultValues() {
			throw new UnsupportedOperationException();
		}
		
		//generate comments
		public void setGenerateComments(boolean generateComments) {
			CheckBox checkBox = new CheckBox();
			if(checkBox.isChecked() == !generateComments) {
				checkBox.click();
			}
		}
	}
}
