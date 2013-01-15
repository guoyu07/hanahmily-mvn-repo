package org.systemgo.codegen.dialog.ctlgen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.TreeItem;
import org.systemgo.codegen.dialog.SelectTablePage;
import org.systemgo.codegen.module.Column;
import org.systemgo.codegen.module.Table;

public class GenCodeWizard extends Wizard {

	public GenCodeWizard() {
		setWindowTitle("生成代码");
	}

	SelectTablePage selectTablePage = new SelectTablePage();

	ConfigGenInfoPage configGenInfoPage = new ConfigGenInfoPage();

	@Override
	public void addPages() {
		this.addPage(selectTablePage);
		this.addPage(configGenInfoPage);
	}

	@Override
	public boolean performFinish() {
		String tableNamesStr = selectTablePage.getTargetTableName();

		if (!StringUtils.isEmpty(tableNamesStr)) {
			StringTokenizer st = new StringTokenizer(tableNamesStr, ", ");
			while (st.hasMoreTokens()) {
				String tableName = st.nextToken();
				this.genFile(tableName);
			}
		}

		return true;
	}

	private String genContent(String tableName) {
		StringBuilder contentBuilder = new StringBuilder();
		contentBuilder.append("Load data").append("\n");
		contentBuilder.append("append into table ").append(tableName)
				.append("\n");
		contentBuilder.append("fields terminated by ','").append("\n");
		contentBuilder.append("TRAILING NULLCOLS").append("\n");
		contentBuilder.append("(").append("\n");
		List<Column> tableList = Table.getColumns(tableName);
		if (null != tableList) {
			for (int i = 0; i < tableList.size(); i++) {
				if (i > 0) {
					contentBuilder.append(",");
				}
				Column column = tableList.get(i);
				contentBuilder.append(column.getName()).append(" ");

				if ("Timestamp".equals(column.getType())) {
					contentBuilder
							.append("\"to_date(substr(:UPDATE_TIME,0,19),'YYYY/MM/DD HH24:MI:SS')\"");
				} else {
					contentBuilder.append("char(")
							.append(column.getPrecision()).append(")");
				}

				contentBuilder.append("\n");
			}
		}
		contentBuilder.append(")").append("\n");
		return contentBuilder.toString();
	}

	/**
	 * 生成文件
	 * 
	 * @param tableName
	 * @param content
	 */
	private void genFile(String tableName) {
		TreeItem[] itemArray = configGenInfoPage.treeViewer.getTree()
				.getSelection();
		if (null != itemArray) {
			TreeItem item = itemArray[0];
			IResource resource = (IResource) item.getData();
			File file = new File(resource.getRawLocation().toOSString() + "\\"
					+ tableName + ".ctl");
			FileWriter writer = null;
			try {
				writer = new FileWriter(file);
				writer.write(this.genContent(tableName));
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
					}
				}
			}

		}
	}

}
