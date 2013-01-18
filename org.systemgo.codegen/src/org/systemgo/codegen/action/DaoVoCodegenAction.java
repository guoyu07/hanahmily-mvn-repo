package org.systemgo.codegen.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.systemgo.codegen.dialog.vodaogen.GenCodeWizard;
import org.systemgo.codegen.pub.Context;
import org.systemgo.codegen.pub.Context.ContextType;
import org.systemgo.codegen.views.DatabaseView;

/**
 * 生成代码行为
 * 
 * @author gaoht
 *
 */
public class DaoVoCodegenAction extends Action {

	
	private DatabaseView databaseView;

	public DaoVoCodegenAction(TableViewer viewer, DatabaseView databaseView) {
		this.viewer = viewer;
		this.databaseView = databaseView;
		this.setText("DaoVo代码生成");
		this.setToolTipText("DaoVo代码生成");
	}

	private TableViewer viewer;

	public void run() {
		ISelection selection = viewer.getSelection();
		Object dbConnName = ((IStructuredSelection) selection)
				.getFirstElement();
		
		//注册全局变量
		Context.register(ContextType.CURRENT_DB,
				databaseView.getDbConfig().getProperties(dbConnName.toString()));
		GenCodeWizard adw = new GenCodeWizard();

		WizardDialog wd = new WizardDialog(
				this.viewer.getControl().getShell(), adw);
		wd.create();
		if (WizardDialog.OK == wd.open()) {
			viewer.refresh();
		}
	}
}
