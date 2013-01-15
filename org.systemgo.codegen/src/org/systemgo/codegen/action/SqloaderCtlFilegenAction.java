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
 * ���ɴ�����Ϊ,����ctl�����ļ�
 * 
 * @author gaoht
 *
 */
public class SqloaderCtlFilegenAction extends Action {

	
	public SqloaderCtlFilegenAction(TableViewer viewer, ConfigFile dbConfig) {
		this.viewer = viewer;
		this.dbConfig = dbConfig;
		this.setText("Sqldr�����ļ�����");
		this.setToolTipText("Sqldr�����ļ�����");
	}

	private TableViewer viewer;
	private ConfigFile dbConfig;

	public void run() {
		ISelection selection = viewer.getSelection();
		Object dbConnName = ((IStructuredSelection) selection)
				.getFirstElement();
		
		//ע��ȫ�ֱ���
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
