package org.systemgo.codegen.dialog;

import java.util.Properties;

/**
 * �������ݿ�Ի���
 * 
 * @author gaoht
 * 
 */
public class AddDatabaseDialogData {

	/**
	 * 
	 * @return jdbc�ַ���
	 */
	public String getUrl() {
		return "jdbc:oracle:thin:@" + config.getProperty(IP) + ":"
				+ config.getProperty(PORT) + ":" + config.getProperty(SID);
	}

	/**
	 * ��ȡ���������
	 * 
	 * @param key
	 * @return
	 */
	public String getText(String key) {
		return config.getProperty(key);
	}

	public void setText(String key, String value) {
		config.setProperty(key, value);
	}

	private Properties config = new Properties();

	public static final String NAME = "NAME";
	public static final String IP = "IP";
	public static final String PORT = "PORT";
	public static final String SID = "SID";
	public static final String USERNAME = "USERNAME";
	public static final String PASSWORD = "PASSWORD";
}
