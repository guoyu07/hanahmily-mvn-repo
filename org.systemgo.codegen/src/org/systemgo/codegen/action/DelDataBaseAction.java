package org.systemgo.codegen.action;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.systemgo.codegen.views.DatabaseView;

/**
 * 删除数据库事件
 * 
 * @author gaoht
 *
 */
public class DelDataBaseAction extends Action {

	
	public DelDataBaseAction(TableViewer viewer, DatabaseView databaseView) {
		this.viewer = viewer;
		this.databaseView = databaseView;
		this.setText("删除数据库");
		this.setToolTipText("删除一个数据库");
		this.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));
	}

	private TableViewer viewer;
	private DatabaseView databaseView;

	public void run() {
		TableItem[] tableItem = viewer.getTable().getSelection();
		if (!ArrayUtils.isEmpty(tableItem)) {
			MessageBox md = new MessageBox(viewer.getControl()
					.getShell(), SWT.ICON_WARNING | SWT.YES | SWT.NO);
			md.setMessage("是否要删除数据库连接？");
			if (SWT.YES == md.open()) {
				for (int i = 0; i < tableItem.length; i++) {
					String name = tableItem[i].getText();
					databaseView.getDbConfig().delConfig(name);
				}
			}
		}
		viewer.refresh();
	}
}
