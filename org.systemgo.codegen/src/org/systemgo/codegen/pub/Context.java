package org.systemgo.codegen.pub;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 全局变量存放
 * @author gaoht
 *
 */
public class Context {
	public enum ContextType{
		CURRENT_DB,
		PROJECT;
	};
	public static Properties getContext(ContextType type){
		return data.get(type);
	}
	
	public static void register(ContextType type, Properties prop){
		data.put(type, prop);
	}
	
	private static final Map<ContextType, Properties> data = new HashMap<ContextType, Properties>();
}
