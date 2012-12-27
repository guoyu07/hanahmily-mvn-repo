package org.systemgo.codegen.views;

import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.systemgo.codegen.Activator;
import org.systemgo.codegen.dialog.AddDbWizard;
import org.systemgo.codegen.dialog.GenCodeWizard;
import org.systemgo.codegen.dom.ConfigFile;
import org.systemgo.codegen.dom.ConfigFile.ConfigType;
import org.systemgo.codegen.pub.Context;
import org.systemgo.codegen.pub.Context.ContextType;

/**
 * 
 * ��ѯ���ݿ��еı���ͼ
 * 
 * @author gaoht
 * 
 */
public class DatabaseView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.systemgo.codegen.views.DatabaseView";

	/**
	 * �������ݿ⶯��
	 */
	private Action addDatabaseAction;

	/**
	 * ɾ�����ݿ⶯��
	 */
	private Action delDatabaseAction;

	/**
	 * ���ɴ����¼�
	 */
	private Action genCodeAction;

	private Composite parent;

	private TableViewer viewer;

	/**
	 * �������ݴ洢
	 */
	private ConfigFile dbConfig;

	/**
	 * The constructor.
	 */
	public DatabaseView() {
	}

	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			dbConfig = new ConfigFile(ConfigType.DATABASE);
			Set<String> configNames = dbConfig.listConfigName();
			return configNames.toArray();
		}
	}

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		public Image getImage(Object obj) {
			return Activator.getImageDescriptor("icons/db.png").createImage();
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		this.parent = parent;
		initConfig();
		// �����Ϊ
		makeActions();
		// �����Ϊ�˵�
		contributeToActionBars();

		// ����˫���¼�
		hookDoubleClickAction();
	}

	/**
	 * 
	 */
	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				genCodeAction.run();
			}
		});
	}

	private void initConfig() {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(getViewSite());
	}

	/**
	 * �����Ϊ
	 */
	private void makeActions() {
		// �������ݿ���Ϊ
		addDatabaseAction = new Action() {
			public void run() {
				AddDbWizard adw = new AddDbWizard();
				WizardDialog wd = new WizardDialog(
						DatabaseView.this.parent.getShell(), adw);
				wd.create();
				if (WizardDialog.OK == wd.open()) {
					viewer.refresh();
				}
			}
		};
		addDatabaseAction.setText("�������ݿ�");
		addDatabaseAction.setToolTipText("����һ�����ݿ�");
		addDatabaseAction.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJ_ADD));

		// ɾ�����ݿ���Ϊ
		delDatabaseAction = new Action() {
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
		};
		delDatabaseAction.setText("ɾ�����ݿ�");
		delDatabaseAction.setToolTipText("ɾ��һ�����ݿ�");
		delDatabaseAction.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));

		// ���ɴ����¼�
		genCodeAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object dbConnName = ((IStructuredSelection) selection)
						.getFirstElement();
				
				//ע��ȫ�ֱ���
				Context.register(ContextType.CURRENT_DB,
						dbConfig.getProperties(dbConnName.toString()));
				GenCodeWizard adw = new GenCodeWizard();

				WizardDialog wd = new WizardDialog(
						DatabaseView.this.parent.getShell(), adw);
				wd.create();
				if (WizardDialog.OK == wd.open()) {
					viewer.refresh();
				}
			}
		};
		genCodeAction.setText("���ɴ���");
		genCodeAction.setToolTipText("���ɴ���");
	}

	/**
	 * ��ӹ�����
	 */
	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	/**
	 * ��������˵�
	 * 
	 * @param menuManager
	 *            �˵�������
	 */
	private void fillLocalPullDown(IMenuManager menuManager) {

	}

	/**
	 * ��ӹ�����
	 * 
	 * @param toolBarManager
	 *            ������������
	 */
	private void fillLocalToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(addDatabaseAction);
		toolBarManager.add(delDatabaseAction);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
	}
}