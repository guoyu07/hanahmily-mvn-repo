package org.systemgo.codegen.pub;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.systemgo.codegen.module.Table;

public class BaseDAO {

	protected Connection getConn() {
		DbUtils.loadDriver("oracle.jdbc.driver.OracleDriver");
		Connection conn = null;
		try {
			return DriverManager.getConnection(
					"jdbc:oracle:thin:@10.1.35.115:1521:yw6dev",
					"devframework", "devframework");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			DbUtils.printStackTrace(e);
			DbUtils.closeQuietly(conn);
		} finally {

		}
		return null;

	}

	public static final void main(String[] args) {

		DbUtils.loadDriver("oracle.jdbc.driver.OracleDriver");
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@10.1.35.115:1521:yw6dev",
					"devframework", "devframework");
			ResultSetHandler<List<Table>> rsh = new BeanListHandler<Table>(
					Table.class);
			QueryRunner qr = new QueryRunner();
			List<Table> listTable = qr
					.query(conn,
							"SELECT aa.table_name tableName,aa.tablespace_name FROM all_tables aa where aa.owner = 'DEVFRAMEWORK'",
							rsh);

			for (Table table : listTable) {
				System.out.println(table.getTableName());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			DbUtils.printStackTrace(e);
		} finally {
			DbUtils.closeQuietly(conn);
		}

	}
}
