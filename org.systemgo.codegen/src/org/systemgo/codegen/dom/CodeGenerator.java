package org.systemgo.codegen.dom;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.systemgo.codegen.module.Column;
import org.systemgo.codegen.module.Table;
import org.systemgo.codegen.pub.NameConverter;

/**
 * 2007-5-15
 * 
 * @author wangcl
 * 
 */
public class CodeGenerator {

	/* initialize velocity */
	static {
		Properties config = new Properties();
		try {
			config.put("file.resource.loader.class",
					"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

			Velocity.init(config);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * generate all classes: vo, idao, dao
	 */
	public void generate(Properties config) {
		List<String> tableNames = this.getTableNames(config);
		for (int i = 0; i < tableNames.size(); i++) {
			VelocityContext context = getContext(config, tableNames.get(i));
			generateVo(config, context);
			generateDao(config, context);
		}
	}

	/* generate vo class */
	private void generateVo(Properties config, VelocityContext context) {
		Template template = null;
		NameConverter converter = NameConverter.getInstance();
		try {
			template = Velocity.getTemplate("VoTemplate.vm");
			BufferedWriter writer = new BufferedWriter(
					new FileWriter(
							new File(
									config.getProperty("vopackage")
											+ converter
													.setUpperCaseForFirstLetter(converter
															.convert((String) context
																	.get("tablename")))
											+ "SVO.java")));
			if (template != null) {
				template.merge(context, writer);
			}
			writer.flush();
			writer.close();
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			System.out.println("cannot find template " + "VoTemplate.vm");
		} catch (ParseErrorException e) {
			System.out.println("Syntax error in template : " + e);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* generate dao class */
	private void generateDao(Properties config, VelocityContext context) {
		Template template = null;
		NameConverter converter = NameConverter.getInstance();
		try {
			template = Velocity.getTemplate("DaoTemplate.vm");
			BufferedWriter writer = new BufferedWriter(
					new FileWriter(
							new File(
									config.getProperty("daopackage")
											+ converter
													.setUpperCaseForFirstLetter(converter
															.convert((String) context
																	.get("tablename")))
											+ "SDAO.java")));
			if (template != null) {
				template.merge(context, writer);
			}
			writer.flush();
			writer.close();
		} catch (ResourceNotFoundException e) {
			System.out.println("cannot find template " + "DaoTemplate.vm");
		} catch (ParseErrorException e) {
			System.out.println("Syntax error in template : " + e);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* initialize velocity context */
	private VelocityContext getContext(Properties config, String tableName) {
		VelocityContext context = new VelocityContext();
		NameConverter converter = NameConverter.getInstance();
		context.put("nameconverter", converter);
		context.put("componentname", config.getProperty("componentname"));
		// 如果可以生成日志表
		context.put("isLog", "N");
		context.put("tablename", tableName.toUpperCase());
		List<Column> columns = Table.getColumns(tableName);

		context.put("columns", columns);
		context.put("insertsql", getDefaultInsertString(columns));
		context.put("selectsql", getDefaultSelectString(columns));
		context.put("batch", config.getProperty("batch") == null ? "n" : config
				.getProperty("batch").toLowerCase());
		context.put("isNumeric", config.getProperty("isNumeric") == null ? "n"
				: config.getProperty("isNumeric").toLowerCase());
		context.put("vopackagename", config.getProperty("vopackagename"));
		context.put("daopackagename", config.getProperty("daopackagename"));
		return context;
	}

	/*
	 * part of insert sql string, like "(USER_ID,USER_NAME) values(?,?)". it can
	 * be processed in velocity template, but this process makes the template
	 * code hard to read. instead, it is easier to process it in java :~)
	 */
	private String getDefaultInsertString(List<Column> columns) {
		String sql = "(";
		for (int i = 0; i < columns.size(); i++) {
			Column column = columns.get(i);
			sql += column.getName() + ",";
		}
		sql = sql.substring(0, sql.length() - 1);
		sql += ") values(";
		for (int i = 1; i < columns.size(); i++) {
			sql += "?,";
		}
		sql += "?)";
		return sql;
	}

	/*
	 * part of select sql string, like "a.COLUMN_NAME1,a.COLUMN_NAME2".
	 */
	private String getDefaultSelectString(List<Column> columns) {
		String sql = "";
		for (int i = 0; i < columns.size(); i++) {
			Column column = columns.get(i);
			sql += "a." + column.getName() + ",";
		}
		sql = sql.substring(0, sql.length() - 1);
		return sql;
	}

	/* get all tables from configuration */
	private List<String> getTableNames(Properties config) {
		List<String> tableNames = new ArrayList<String>();
		if (config.getProperty("tablenames") != null) {
			String names = config.getProperty("tablenames");
			StringTokenizer st = new StringTokenizer(names, ", ");
			while (st.hasMoreTokens()) {
				tableNames.add(st.nextToken());
			}
		}
		return tableNames;
	}
}
