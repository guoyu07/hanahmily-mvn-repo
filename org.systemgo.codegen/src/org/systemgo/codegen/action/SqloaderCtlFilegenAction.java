package org.systemgo.codegen.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.systemgo.codegen.dialog.ctlgen.GenCodeWizard;
import org.systemgo.codegen.dom.ConfigFile;
import org.systemgo.codegen.pub.Context;
import org.systemgo.codegen.pub.Context.ContextType;

/**
 * 生成代码行为,生成ctl控制文件
 * 
 * @author gaoht
 *
 */
public class SqloaderCtlFilegenAction extends Action {

	
	public SqloaderCtlFilegenAction(TableViewer viewer, ConfigFile dbConfig) {
		this.viewer = viewer;
		this.dbConfig = dbConfig;
		this.setText("Sqldr控制文件生成");
		this.setToolTipText("Sqldr控制文件生成");
	}

	private TableViewer viewer;
	private ConfigFile dbConfig;

	public void run() {
		ISelection selection = viewer.getSelection();
		Object dbConnName = ((IStructuredSelection) selection)
				.getFirstElement();
		
		//注册全局变量
		Context.register(ContextType.CURRENT_DB,
				dbConfig.getProperties(dbConnName.toString()));
		GenCodeWizard adw = new GenCodeWizard();

		WizardDialog wd = new WizardDialog(
				this.viewer.getControl().getShell(), adw);
		wd.create();
		if (WizardDialog.OK == wd.open()) {
			viewer.refresh();
		}
	}
}
