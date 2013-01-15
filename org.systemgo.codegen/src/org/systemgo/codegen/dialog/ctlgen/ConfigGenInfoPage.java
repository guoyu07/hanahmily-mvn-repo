package org.systemgo.codegen.dialog.ctlgen;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class ConfigGenInfoPage extends WizardPage {

	protected TreeViewer treeViewer;

	/**
	 * Create the wizard.
	 */
	public ConfigGenInfoPage() {
		super("wizardPage");
		setTitle("\u751F\u6210\u4EE3\u7801\u76EE\u5F55\u9009\u62E9");
		setDescription("\u9009\u62E9vo\u548Cdao\u7684\u751F\u6210\u76EE\u5F55\uFF0C\u4EE5\u53CA\u76F8\u5173\u914D\u7F6E\u4FE1\u606F\n");
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		treeViewer = new TreeViewer(container, SWT.BORDER);
		treeViewer.setContentProvider(new BaseWorkbenchContentProvider());
		treeViewer.setLabelProvider(new WorkbenchLabelProvider());
		treeViewer.setAutoExpandLevel(2);
		treeViewer.setInput(ResourcesPlugin.getWorkspace().getRoot());

		ViewerFilter[] filterArray = { new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement,
					Object element) {
				//¹ýÂËÎÄ¼þ
				if(element instanceof IFile){
					return false;
				}
				return true;
			}
		} };

		treeViewer.setFilters(filterArray);
	}

}
