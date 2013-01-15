package org.systemgo.codegen.module;

/**
 * 2007-5-21
 * 
 * @author wangcl
 * 
 */
public class Column {

    // 数据库字段名（大写），如USER_NAME,REMARKS
    private String name;

    // java数据类型，如Date,String
    private String type;
    
    // 精度
    private Integer precision;

    // 是否是主键
    private boolean pk = false;

    // 是否可为空
    private boolean nullable = true;
    
    // 是否是数字型，主要用于条件字段赋值时，如果是数字型则通过setLong方法赋值，以便应用索引
    private boolean numeric = false;

    // 是否需要根据此字段生成findBy×××方法，如findByUserName(String userName)
    private boolean select = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPk() {
        return pk;
    }

    public void setPk(boolean pk) {
        this.pk = pk;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isNumeric() {
        return numeric;
    }

    public void setNumeric(boolean numeric) {
        this.numeric = numeric;
    }

	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(Integer precision) {
		this.precision = precision;
	}
}
