package org.systemgo.codegen.dialog;

import org.eclipse.jface.wizard.Wizard;

public class GenCodeWizard extends Wizard {
	
	public GenCodeWizard() {
		setWindowTitle("Éú³É´úÂë");
	}
	
	SelectTablePage selectTablePage = new SelectTablePage();

	ConfigGenInfoPage configGenInfoPage = new ConfigGenInfoPage();
	
	@Override
	public void addPages() {
		this.addPage(selectTablePage);
		this.addPage(configGenInfoPage);
	}

	@Override
	public boolean performFinish() {
		
		configGenInfoPage.voPathText.getText();
		configGenInfoPage.daoPathText.getText();
		return false;
	}
	
	

}
