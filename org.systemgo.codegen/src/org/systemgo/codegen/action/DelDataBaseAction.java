package org.systemgo.codegen.action;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.systemgo.codegen.dom.ConfigFile;

/**
 * ɾ�����ݿ��¼�
 * 
 * @author gaoht
 *
 */
public class DelDataBaseAction extends Action {

	
	public DelDataBaseAction(TableViewer viewer, ConfigFile dbConfig) {
		this.viewer = viewer;
		this.dbConfig = dbConfig;
		this.setText("ɾ�����ݿ�");
		this.setToolTipText("ɾ��һ�����ݿ�");
		this.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));
	}

	private TableViewer viewer;
	private ConfigFile dbConfig;

	public void run() {
		TableItem[] tableItem = viewer.getTable().getSelection();
		if (!ArrayUtils.isEmpty(tableItem)) {
			MessageBox md = new MessageBox(viewer.getControl()
					.getShell(), SWT.ICON_WARNING | SWT.YES | SWT.NO);
			md.setMessage("�Ƿ�Ҫɾ�����ݿ����ӣ�");
			if (SWT.YES == md.open()) {
				for (int i = 0; i < tableItem.length; i++) {
					String name = tableItem[i].getText();
					dbConfig.delConfig(name);
				}
			}
		}
		viewer.refresh();
	}
}
