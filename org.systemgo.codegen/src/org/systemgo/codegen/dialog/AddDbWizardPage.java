package org.systemgo.codegen.dialog;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;
import org.systemgo.codegen.dialog.adddb.AddDatabaseDialogData;

public class AddDbWizardPage extends WizardPage {

	protected Text nameText;

	protected Text ipText;
	protected Text portText;
	protected Text usernameText;
	protected Text passwordText;
	protected Text sidText;

	private Composite container;

	/**
	 * Create the wizard.
	 */
	public AddDbWizardPage() {
		super("wizardPage");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(
				"org.systemgo.codegen", "icons/db-dialog.png"));
		setTitle("\u65B0\u5EFA\u6570\u636E\u5E93\u8FDE\u63A5");
		setDescription("\u65B0\u589E\u4E00\u4E2A\u6570\u636E\u5E93\u8FDE\u63A5\uFF0C\u63D0\u4F9B\u6570\u636E\u8868\u6765\u6E90");
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);

		GridLayout gl_container = new GridLayout(1, false);
		gl_container.verticalSpacing = 20;
		gl_container.horizontalSpacing = 0;
		gl_container.marginWidth = 0;
		gl_container.marginHeight = 0;
		container.setLayout(gl_container);

		// 输入内容构建
		Composite inputComposite = new Composite(container, SWT.NONE);
		GridData gd_inputComposite = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_inputComposite.heightHint = 187;
		gd_inputComposite.widthHint = 485;
		inputComposite.setLayoutData(gd_inputComposite);
		GridLayout gl_inputComposite = new GridLayout(2, false);
		gl_inputComposite.marginWidth = 10;
		gl_inputComposite.horizontalSpacing = 10;
		inputComposite.setLayout(gl_inputComposite);

		Label label_1 = new Label(inputComposite, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_1.setText("\u6570\u636E\u5E93\u540D\u79F0");

		nameText = new Text(inputComposite, SWT.BORDER);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		Label label_2 = new Label(inputComposite, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_2.setText("\u6570\u636E\u5E93\u5730\u5740");

		ipText = new Text(inputComposite, SWT.BORDER);
		ipText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));

		Label label_3 = new Label(inputComposite, SWT.NONE);
		label_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_3.setText("\u7AEF\u53E3");

		portText = new Text(inputComposite, SWT.BORDER);
		GridData gd_portText = new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1);
		gd_portText.widthHint = 50;
		portText.setLayoutData(gd_portText);

		Label lblSid = new Label(inputComposite, SWT.NONE);
		lblSid.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		lblSid.setText("SID");

		sidText = new Text(inputComposite, SWT.BORDER);
		sidText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		Label label_4 = new Label(inputComposite, SWT.NONE);
		label_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_4.setText("\u7528\u6237\u540D");

		usernameText = new Text(inputComposite, SWT.BORDER);
		usernameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label label_5 = new Label(inputComposite, SWT.NONE);
		label_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_5.setText("\u5BC6\u7801");

		passwordText = new Text(inputComposite, SWT.BORDER);
		passwordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		setControl(container);
	}

	public boolean validate() {
		StringBuilder errMsg = new StringBuilder();
		if (StringUtils.isBlank(nameText.getText())) {
			errMsg.append("数据库名称不能为空\n");
		}

		if (StringUtils.isBlank(ipText.getText())) {
			errMsg.append("IP或域名地址不能为空\n");
		}
		
		if (StringUtils.isBlank(portText.getText())) {
			errMsg.append("端口不能为空\n");
		}else if(!StringUtils.isNumeric(portText.getText())){
			errMsg.append("端口必须为数字\n");
		}

		if (StringUtils.isBlank(sidText.getText())) {
			errMsg.append("SID不能为空\n");
		}

		if (StringUtils.isBlank(usernameText.getText())) {
			errMsg.append("用户名不能为空\n");
		}

		if (StringUtils.isBlank(ipText.getText())) {
			errMsg.append("密码不能为空\n");
		}

		if (errMsg.length() > 0) {
			MessageBox md = new MessageBox(container.getShell(),
					SWT.ICON_WARNING | SWT.YES);
			md.setMessage(errMsg.toString());
			md.open();
			return false;
		}
		return true;

	}
	
	public AddDatabaseDialogData getInputData(){
		AddDatabaseDialogData data = new AddDatabaseDialogData();
		data.setText(AddDatabaseDialogData.NAME,nameText.getText());
		data.setText(AddDatabaseDialogData.IP,ipText.getText());
		data.setText(AddDatabaseDialogData.PORT,portText.getText());
		data.setText(AddDatabaseDialogData.SID,sidText.getText());
		data.setText(AddDatabaseDialogData.USERNAME,usernameText.getText());
		data.setText(AddDatabaseDialogData.PASSWORD,passwordText.getText());
		return data;
	}

}
