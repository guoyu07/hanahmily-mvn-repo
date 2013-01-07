package org.systemgo.codegen.dialog;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.wb.swt.SWTResourceManager;

public class ConfigGenInfoPage extends WizardPage {
	protected Text voPathText;
	protected Text daoPathText;
	private Button selectVoPathBtn;
	private Button selectDaoPathBtn;
	private Label voErrMsg;
	private Label daoErrMsg;
	protected Button isNumbericBtn;

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
		container.setLayout(new GridLayout(1, false));

		Group grpVo = new Group(container, SWT.NONE);
		GridData gd_grpVo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
				1);
		gd_grpVo.widthHint = 560;
		grpVo.setLayoutData(gd_grpVo);
		grpVo.setText("VO\u751F\u6210");
		grpVo.setLayout(new GridLayout(3, false));

		Label label = new Label(grpVo, SWT.NONE);
		label.setAlignment(SWT.RIGHT);
		GridData gd_label = new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1);
		gd_label.widthHint = 117;
		label.setLayoutData(gd_label);
		label.setText("\u751F\u6210\u76EE\u5F55\uFF1A");

		voPathText = new Text(grpVo, SWT.BORDER);
		voPathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		selectVoPathBtn = new Button(grpVo, SWT.NONE);
		selectVoPathBtn.setText("\u9009\u62E9");
		new Label(grpVo, SWT.NONE);

		voErrMsg = new Label(grpVo, SWT.NONE);
		voErrMsg.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		new Label(grpVo, SWT.NONE);

		isNumbericBtn = new Button(grpVo, SWT.CHECK);
		isNumbericBtn.setText("\u751F\u6210\u6570\u5B57\u7C7B\u578B");
		new Label(grpVo, SWT.NONE);
		new Label(grpVo, SWT.NONE);

		Group grpDao = new Group(container, SWT.NONE);
		grpDao.setLayout(new GridLayout(3, false));
		GridData gd_grpDao = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 1);
		gd_grpDao.widthHint = 565;
		grpDao.setLayoutData(gd_grpDao);
		grpDao.setText("DAO\u751F\u6210");

		Label label_1 = new Label(grpDao, SWT.NONE);
		label_1.setAlignment(SWT.RIGHT);
		GridData gd_label_1 = new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1);
		gd_label_1.widthHint = 117;
		label_1.setLayoutData(gd_label_1);
		label_1.setText("\u751F\u6210\u76EE\u5F55\uFF1A");

		daoPathText = new Text(grpDao, SWT.BORDER);
		daoPathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));

		selectDaoPathBtn = new Button(grpDao, SWT.NONE);
		selectDaoPathBtn.setText("\u9009\u62E9");
		new Label(grpDao, SWT.NONE);

		daoErrMsg = new Label(grpDao, SWT.NONE);
		daoErrMsg.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		new Label(grpDao, SWT.NONE);

		new Label(grpDao, SWT.NONE);
		new Label(grpDao, SWT.NONE);
		new Label(grpDao, SWT.NONE);

		this.makeListener();
	}

	private void makeListener() {
		selectVoPathBtn.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				ConfigGenInfoPage.this.selectPathEvent(voPathText, voErrMsg);
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		selectDaoPathBtn.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				ConfigGenInfoPage.this.selectPathEvent(daoPathText, daoErrMsg);
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		
	}

	private void selectPathEvent(Text text, Label errMsg) {
		errMsg.setText("");
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(
				this.getShell(), new WorkbenchLabelProvider(),
				new BaseWorkbenchContentProvider());
		dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
		if (ElementTreeSelectionDialog.OK == dialog.open()) {
			Object firstResult = dialog.getFirstResult();
			if (firstResult instanceof IFolder) {
				IFolder folder = (IFolder) firstResult;
				text.setText(folder.getFullPath().toString());
			} else {
				errMsg.setText("aaa请选择正确的生成目录");
			}

		}
	}
}
