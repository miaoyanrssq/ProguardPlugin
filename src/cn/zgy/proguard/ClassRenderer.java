package cn.zgy.proguard;

import com.intellij.ui.components.JBCheckBox;

import javax.swing.*;
import java.awt.*;

public class ClassRenderer extends JBCheckBox implements ListCellRenderer {

    private ClassBean bean;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if(value instanceof ClassBean){
            bean = (ClassBean) value;

        }
        setText(bean.getName());
        setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
        setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
        setSelected(isSelected);
        System.out.println("name:"+ bean.getName());
        return this;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        try{
//            JTextField textField = new JTextField();
//            textField.setText(bean.getName());
//            add(textField);
//            JCheckBox checkBox = new JCheckBox();
//            checkBox.setSelected(bean.isField_include());
//            add(checkBox);
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }
}
