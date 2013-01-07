package org.systemgo.codegen.dialog;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.Wizard;
import org.systemgo.codegen.dom.CodeGenerator;

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
		
		if(StringUtils.isEmpty(configGenInfoPage.voPathText.getText())){
			return false;
		}
		if(StringUtils.isEmpty(configGenInfoPage.daoPathText.getText())){
			return false;
		}
		
		Properties prop = new Properties();
		prop.setProperty("outputpath", Platform.getInstanceLocation().getURL().getPath());
		prop.setProperty("vopackage", configGenInfoPage.voPathText.getText());
		prop.setProperty("daopackage", configGenInfoPage.daoPathText.getText());

		prop.setProperty("tablenames", selectTablePage.getSelectTableName());
		prop.setProperty("batch", "Y");
		prop.setProperty("isNumeric", configGenInfoPage.isNumbericBtn.isEnabled()?"Y":"N");

		CodeGenerator generator = new CodeGenerator();
		generator.generate(prop);
		return true;
	}
	
	

}
