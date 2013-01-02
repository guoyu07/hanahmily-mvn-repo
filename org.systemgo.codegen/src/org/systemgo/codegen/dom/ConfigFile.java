package org.systemgo.codegen.dom;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.core.runtime.Platform;
import org.systemgo.codegen.Activator;

/**
 * 配置管理
 * 
 * @author gaoht
 * 
 */
public class ConfigFile {

	private final static String CONF_PATH = Platform.getInstanceLocation()
			.getURL().getPath()
			+ ".metadata/.plugins/" + Activator.PLUGIN_ID;

	private final static String DB_CONF_DIR = "/db";

	public enum ConfigType {
		DATABASE(DB_CONF_DIR);

		private String dir;

		ConfigType(String dir) {
			this.dir = dir;
		}

		public String getDir() {
			return CONF_PATH + dir;
		}
	}

	private TreeMap<String, File> config;

	private ConfigType type;

	public ConfigFile(ConfigType type) {
		this.type = type;
		// 检查目录是否存在
		File directroy = new File(type.getDir());
		if (!directroy.exists()) {
			directroy.mkdirs();
		}
		// 加载配置数据
		File[] files = directroy.listFiles();
		config = new TreeMap<String,File>();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			config.put(file.getName(), file);
		}
	}
	
	public Set<String> listConfigName(){
		return config.keySet();
	}

	public void writeConfig(String name, Properties properties) {
		File file = config.get(name);
		if (file == null) {
			file = new File(type.getDir() + "/" + name);
			try {
				properties.store(new FileWriter(file), name);
			} catch (IOException e) {
				throw new RuntimeException("保存配置文件失败");
			}
		}
	}
	
	public void delConfig(String name){
		File file = config.get(name);
		file.delete();
		config.remove(name);
	}

	public Properties getProperties(String name) {
		File file = config.get(name);
		Properties p = new Properties();
		try {
			p.load(new FileReader(file));
		} catch (IOException e) {
			throw new RuntimeException("加载错误");
		}
		return p;
	}
}
