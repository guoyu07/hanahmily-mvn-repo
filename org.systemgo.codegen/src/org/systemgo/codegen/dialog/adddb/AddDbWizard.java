package org.systemgo.codegen.dialog.adddb;

import java.util.Properties;

import org.eclipse.jface.wizard.Wizard;
import org.systemgo.codegen.dialog.AddDbWizardPage;
import org.systemgo.codegen.dom.ConfigFile;
import org.systemgo.codegen.dom.ConfigFile.ConfigType;

public class AddDbWizard extends Wizard {

	public AddDbWizard() {
		setWindowTitle("�½����ݿ�����");
	}

	AddDbWizardPage addDbWizardPage = new AddDbWizardPage();

	@Override
	public void addPages() {
		this.addPage(addDbWizardPage);
	}

	@Override
	public boolean performFinish() {

		if (!addDbWizardPage.validate()) {
			return false;
		}

		// �������
		Properties saveData = new Properties();
		AddDatabaseDialogData data = addDbWizardPage.getInputData();
		saveData.setProperty("url", data.getUrl());
		saveData.setProperty("username", data.getText(AddDatabaseDialogData.USERNAME));
		saveData.setProperty("password", data.getText(AddDatabaseDialogData.PASSWORD));
		ConfigFile cf = new ConfigFile(ConfigType.DATABASE);
		cf.writeConfig(data.getText(AddDatabaseDialogData.NAME), saveData);
		return true;
	}

}
