package cn.zgy.proguard;

public class ClassBean {

    private String name;
    private boolean select;
    private boolean field_include;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isField_include() {
        return field_include;
    }

    public void setField_include(boolean field_include) {
        this.field_include = field_include;
    }
}
