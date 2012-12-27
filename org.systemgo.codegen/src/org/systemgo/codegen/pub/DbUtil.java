package org.systemgo.codegen.pub;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbutils.DbUtils;
import org.systemgo.codegen.pub.Context.ContextType;

public class DbUtil {
	static {
		DbUtils.loadDriver("oracle.jdbc.driver.OracleDriver");
	}
	
	public static Connection getConn() {
		Connection conn = null;
		Properties prop = Context.getContext(ContextType.CURRENT_DB);
		if (null == prop) {
			return null;
		}
		try {
			conn = DriverManager.getConnection(prop.getProperty("url"),
					prop.getProperty("username"), prop.getProperty("password"));
		} catch (SQLException e) {
			DbUtils.printStackTrace(e);
		}

		return conn;
	}

}
