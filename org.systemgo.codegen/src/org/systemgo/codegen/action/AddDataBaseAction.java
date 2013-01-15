package org.systemgo.codegen.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.systemgo.codegen.dialog.adddb.AddDbWizard;

/**
 * 增加数据库事件
 * 
 * @author gaoht
 *
 */
public class AddDataBaseAction extends Action {

	public AddDataBaseAction(TableViewer viewer) {
		this.viewer = viewer;
		this.setText("新增数据库");
		this.setToolTipText("新增一个数据库");
		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
	}

	private TableViewer viewer;

	public void run() {
		AddDbWizard adw = new AddDbWizard();
		WizardDialog wd = new WizardDialog(viewer.getControl().getShell(), adw);
		wd.create();
		if (WizardDialog.OK == wd.open()) {
			viewer.refresh();
		}
	}
}
