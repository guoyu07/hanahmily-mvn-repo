package org.systemgo.codegen.dialog;

import java.security.InvalidParameterException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class SelectTablePage extends WizardPage {
	private Text text;
	private Table table;
	private Button button;
	private Label errMsg;

	/**
	 * Create the wizard.
	 */
	public SelectTablePage() {
		super("wizardPage");
		setTitle("\u9009\u62E9\u8868");
		setDescription("\u67E5\u8BE2\u5E76\u9009\u62E9\u9700\u8981\u751F\u6210\u7684\u8868\uFF0C\u67E5\u8BE2\u4E0D\u533A\u5206\u5927\u5C0F\u5199");
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(4, false));

		text = new Text(container, SWT.BORDER);
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text.widthHint = 100;
		text.setLayoutData(gd_text);

		button = new Button(container, SWT.NONE);
		button.setText("\u67E5\u8BE2");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		table = new Table(container, SWT.BORDER | SWT.MULTI);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(232);
		tableColumn.setText("\u8868\u540D");

		TableColumn tableColumn_1 = new TableColumn(table, SWT.NONE);
		tableColumn_1.setWidth(300);
		tableColumn_1.setText("\u8868\u7A7A\u95F4");

		errMsg = new Label(container, SWT.NONE);
		errMsg.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		errMsg.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		this.makeListener();
	}

	private void makeListener() {
		button.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				SelectTablePage.this.searchEvent();
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

		text.addTraverseListener(new TraverseListener() {

			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_RETURN) {
					SelectTablePage.this.searchEvent();
				}
			}
		});

		table.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				if (table.getSelectionCount() > 0) {
					SelectTablePage.this.setPageComplete(true);
				} else {
					SelectTablePage.this.setPageComplete(false);
				}

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

	// 查询表事件
	private void searchEvent() {
		if (!StringUtils.isEmpty(text.getText())) {
			errMsg.setText("");
			List<org.systemgo.codegen.module.Table> list = org.systemgo.codegen.module.Table
					.queryTable(text.getText().toUpperCase());
			if (list != null) {
				table.removeAll();
				for (org.systemgo.codegen.module.Table tableData : list) {
					TableItem item = new TableItem(table, SWT.LEFT);
					item.setText(0, tableData.getTableName());
					item.setText(1, tableData.getTablespaceName());
				}
			}
		} else {
			errMsg.setText("请输入查询条件");
		}
	}

	@Override
	public boolean isPageComplete() {
		if (table.getItemCount() < 1) {
			return false;
		} else {
			return true;
		}

	}

	public String getSelectTableName() {
		if (table.getSelectionCount() < 1) {
			throw new InvalidParameterException();
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < table.getSelection().length; i++) {
			TableItem item = table.getSelection()[i];
			if (i > 0) {
				sb.append(",");
			}
			sb.append(item.getText(0));
		}
		return sb.toString();
	}

}
