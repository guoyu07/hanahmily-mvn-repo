package org.systemgo.codegen.module;

/**
 * 2007-5-21
 * 
 * @author wangcl
 * 
 */
public class Column {

    // ���ݿ��ֶ�������д������USER_NAME,REMARKS
    private String name;

    // java�������ͣ���Date,String
    private String type;
    
    // ����
    private Integer precision;

    // �Ƿ�������
    private boolean pk = false;

    // �Ƿ��Ϊ��
    private boolean nullable = true;
    
    // �Ƿ��������ͣ���Ҫ���������ֶθ�ֵʱ���������������ͨ��setLong������ֵ���Ա�Ӧ������
    private boolean numeric = false;

    // �Ƿ���Ҫ���ݴ��ֶ�����findBy��������������findByUserName(String userName)
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
