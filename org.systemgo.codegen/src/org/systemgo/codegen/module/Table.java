package org.systemgo.codegen.module;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.systemgo.codegen.pub.DbUtil;

public class Table {
	private String tableName;

	private String tablespaceName;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTablespaceName() {
		return tablespaceName;
	}

	public void setTablespaceName(String tablespaceName) {
		this.tablespaceName = tablespaceName;
	}

	public static List<Table> queryTable(String tableName) {
		Connection conn = DbUtil.getConn();
		ResultSetHandler<List<Table>> rsh = new BeanListHandler<Table>(
				Table.class);
		QueryRunner qr = new QueryRunner();
		List<Table> listTable = null;
		try {
			listTable = qr
					.query(conn,
							"SELECT aa.table_name tableName,aa.tablespace_name  tablespaceName FROM all_tables aa where aa.owner = 'DEVFRAMEWORK' and aa.table_name like ?",
							rsh, tableName+"%");
		} catch (SQLException e) {
			DbUtils.printStackTrace(e);
		}

		return listTable;
	}

}
