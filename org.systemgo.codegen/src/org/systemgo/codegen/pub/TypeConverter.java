package org.systemgo.codegen.pub;

/**
 * Convert types to specified style <br>
 * 2007-5-15
 * 
 * @author wangcl
 * 
 */
public class TypeConverter {
    /**
     * convert database type to simple java type without full qulified package
     * 
     * @param dbtype
     * @return
     */
    public static String convert(String dbtype) {
        String type = "String";
        if (isDate(dbtype)) {
            type = "Timestamp";
        } else if (isBlob(dbtype)) {
            type = "Blob";
        } else if (isClob(dbtype)) {
            type = "Clob";
        }
        return type;
    }

    public static boolean isDate(String type) {
        return ("java.sql.Date".equals(type) || "java.sql.Timestamp".equals(type));
    }

    public static boolean isBlob(String type) {
        return "oracle.sql.BLOB".equals(type);
    }

    public static boolean isClob(String type) {
        return "oracle.sql.CLOB".equals(type);
    }

    public static boolean isNumeric(int columnType) {
        return (java.sql.Types.BIGINT == columnType || java.sql.Types.BIT == columnType
                || java.sql.Types.INTEGER == columnType || java.sql.Types.NUMERIC == columnType
                || java.sql.Types.SMALLINT == columnType || java.sql.Types.TINYINT == columnType);
    }
}
