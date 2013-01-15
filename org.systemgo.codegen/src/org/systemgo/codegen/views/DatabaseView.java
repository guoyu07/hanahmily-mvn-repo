package org.systemgo.codegen.views;

import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;
import org.systemgo.codegen.Activator;
import org.systemgo.codegen.action.AddDataBaseAction;
import org.systemgo.codegen.action.DaoVoCodegenAction;
import org.systemgo.codegen.action.DelDataBaseAction;
import org.systemgo.codegen.action.SqloaderCtlFilegenAction;
import org.systemgo.codegen.dom.ConfigFile;
import org.systemgo.codegen.dom.ConfigFile.ConfigType;

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
	
	/**
	 * ���ɿ����ļ��¼�
	 */
	private Action genCtlAction;


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

		// �����Ҽ��˵�
		hookContextMenu();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				DatabaseView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}
	
	private void fillContextMenu(IMenuManager manager) {
		manager.add(genCodeAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(genCtlAction);
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
		addDatabaseAction = new AddDataBaseAction(viewer);

		// ɾ�����ݿ���Ϊ
		delDatabaseAction = new DelDataBaseAction(viewer, dbConfig);

		// ���ɴ����¼�
		genCodeAction = new DaoVoCodegenAction(viewer, dbConfig);
		
		// ���ɴ�������ļ�
		genCtlAction = new SqloaderCtlFilegenAction(viewer, dbConfig);
	}

	/**
	 * ˫����Ϊ
	 */
	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				genCodeAction.run();
			}
		});
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