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
 * 查询数据库中的表视图
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
	 * 增加数据库动作
	 */
	private Action addDatabaseAction;

	/**
	 * 删除数据库动作
	 */
	private Action delDatabaseAction;

	/**
	 * 生成代码事件
	 */
	private Action genCodeAction;

	private Composite parent;

	private TableViewer viewer;

	/**
	 * 配置数据存储
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
		// 添加行为
		makeActions();
		// 添加行为菜单
		contributeToActionBars();

		// 增加双击事件
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
	 * 添加行为
	 */
	private void makeActions() {
		// 增加数据库行为
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
		addDatabaseAction.setText("新增数据库");
		addDatabaseAction.setToolTipText("新增一个数据库");
		addDatabaseAction.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJ_ADD));

		// 删除数据库行为
		delDatabaseAction = new Action() {
			public void run() {
				TableItem[] tableItem = viewer.getTable().getSelection();
				if (!ArrayUtils.isEmpty(tableItem)) {
					MessageBox md = new MessageBox(viewer.getControl()
							.getShell(), SWT.ICON_WARNING | SWT.YES | SWT.NO);
					md.setMessage("是否要删除数据库连接？");
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
		delDatabaseAction.setText("删除数据库");
		delDatabaseAction.setToolTipText("删除一个数据库");
		delDatabaseAction.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));

		// 生成代码事件
		genCodeAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object dbConnName = ((IStructuredSelection) selection)
						.getFirstElement();
				
				//注册全局变量
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
		genCodeAction.setText("生成代码");
		genCodeAction.setToolTipText("生成代码");
	}

	/**
	 * 添加工具条
	 */
	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	/**
	 * 添加下拉菜单
	 * 
	 * @param menuManager
	 *            菜单管理器
	 */
	private void fillLocalPullDown(IMenuManager menuManager) {

	}

	/**
	 * 添加工具条
	 * 
	 * @param toolBarManager
	 *            工具条管理器
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