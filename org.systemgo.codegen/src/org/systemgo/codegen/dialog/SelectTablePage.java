package org.systemgo.codegen.dialog;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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
	private Table sourceTable;
	private Button button;
	private Label errMsg;
	private Table targetTable;
	private TableColumn tableColumn_2;
	private DragSource dragSource;
	private DropTarget dropTarget;

	/**
	 * Create the wizard.
	 */
	public SelectTablePage() {
		super("wizardPage");
		setTitle("\u9009\u62E9\u8868");
		setDescription("\u67E5\u8BE2\u5E76\u9009\u62E9\u9700\u8981\u751F\u6210\u7684\u8868\uFF0C\u67E5\u8BE2\u4E0D\u533A\u5206\u5927\u5C0F\u5199\u3002\u5C06\u201C\u5F85\u9009\u8868\u540D\u201D\u4E2D\u7684\u8868\u62D6\u62FD\u5230\u201C\u751F\u6210\u76EE\u6807\u8868\u540D\u201D\u8868\u683C\u4E2D\uFF0C\u4F5C\u4E3A\u751F\u6210\u7684\u76EE\u6807\u8868");
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

		sourceTable = new Table(container, SWT.BORDER | SWT.MULTI);
		GridData gd_sourceTable = new GridData(SWT.FILL, SWT.FILL, false,
				false, 1, 1);
		gd_sourceTable.heightHint = 200;
		sourceTable.setLayoutData(gd_sourceTable);
		sourceTable.setHeaderVisible(true);
		sourceTable.setLinesVisible(true);

		TableColumn tableColumn = new TableColumn(sourceTable, SWT.NONE);
		tableColumn.setWidth(200);
		tableColumn.setText("\u5F85\u9009\u8868\u540D");

		dragSource = new DragSource(sourceTable, DND.DROP_MOVE);
		new Label(container, SWT.NONE);

		targetTable = new Table(container, SWT.BORDER | SWT.MULTI);
		GridData gd_targetTable = new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1);
		gd_targetTable.heightHint = 200;
		targetTable.setLayoutData(gd_targetTable);
		targetTable.setHeaderVisible(true);
		targetTable.setLinesVisible(true);

		tableColumn_2 = new TableColumn(targetTable, SWT.NONE);
		tableColumn_2.setWidth(200);
		tableColumn_2.setText("\u751F\u6210\u76EE\u6807\u8868\u540D");

		dropTarget = new DropTarget(targetTable, DND.DROP_MOVE);
		new Label(container, SWT.NONE);

		errMsg = new Label(container, SWT.NONE);
		errMsg.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		errMsg.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		this.makeData();
		this.makeListener();
	}

	private void makeData() {
		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		dragSource.setTransfer(types);
		dropTarget.setTransfer(types);
	}

	private void makeListener() {

		// 查询
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				SelectTablePage.this.searchEvent();
			}
		});

		// 查询框回车
		text.addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_RETURN) {
					SelectTablePage.this.searchEvent();
				}
			}
		});

		// 托出源表
		dragSource.addDragListener(new DragSourceAdapter() {

			@Override
			public void dragStart(DragSourceEvent event) {
				// 如果没有选择表就拖拽，是不允许的
				if (sourceTable.getSelectionCount() < 1) {
					event.doit = false;
				}
			};

			@Override
			public void dragSetData(DragSourceEvent event) {

				if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
					event.data = SelectTablePage.this.getSourceSelectTableName();
				}
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				if (event.detail == DND.DROP_MOVE) {

				}
			}
		});

		// 拖入目标表
		dropTarget.addDropListener(new DropTargetAdapter() {

			@Override
			public void drop(DropTargetEvent event) {
				if (event.data == null) {
					event.detail = DND.DROP_NONE;
					return;
				}

				String names = (String) event.data;
				StringTokenizer st = new StringTokenizer(names, ", ");
				while (st.hasMoreTokens()) {
					TableItem item = new TableItem(targetTable, SWT.LEFT);
					item.setText(0, st.nextToken());
				}
				SelectTablePage.this.setPageComplete(true);
			}
		});

		// 删除目标表数据项
		targetTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if (targetTable.getSelection().length < 1) {
					return;
				}

				List<String> removeTableNames = new ArrayList<String>();
				for (int i = 0; i < targetTable.getSelection().length; i++) {
					removeTableNames.add(targetTable.getSelection()[i]
							.getText(0));
				}

				for (String tableName : removeTableNames) {
					for (int j = 0; j < targetTable.getItems().length; j++) {
						if (StringUtils.containsOnly(tableName,
								targetTable.getItems()[j].getText(0))) {
							targetTable.remove(j);
							j--;
						}
					}
				}

				if (targetTable.getSelection().length < 1) {
					SelectTablePage.this.setPageComplete(true);
				}

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
				sourceTable.removeAll();
				for (org.systemgo.codegen.module.Table tableData : list) {
					TableItem item = new TableItem(sourceTable, SWT.LEFT);
					item.setText(0, tableData.getTableName());
				}
			}
		} else {
			errMsg.setText("请输入查询条件");
		}
	}

	@Override
	public boolean isPageComplete() {
		if (targetTable.getItemCount() < 1) {
			return false;
		} else {
			return true;
		}

	}

	private String getSourceSelectTableName() {
		if (sourceTable.getSelectionCount() < 1) {
			throw new InvalidParameterException();
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < sourceTable.getSelection().length; i++) {
			TableItem item = sourceTable.getSelection()[i];
			if (i > 0) {
				sb.append(",");
			}
			sb.append(item.getText(0));
		}
		return sb.toString();
	}
	
	public String getTargetTableName() {
		if (targetTable.getItemCount() < 1) {
			throw new InvalidParameterException();
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < targetTable.getItems().length; i++) {
			TableItem item = targetTable.getItems()[i];
			if (i > 0) {
				sb.append(",");
			}
			sb.append(item.getText(0));
		}
		return sb.toString();
	}

}
