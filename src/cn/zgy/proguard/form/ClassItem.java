package cn.zgy.proguard.form;

import cn.zgy.proguard.ClassBean;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class ClassItem extends JPanel {

    CheckListener checkListener;
    CheckListener_Field checkListener_field;

    ClassBean classBean;
    int position;

    //ui
    protected JCheckBox classCheck;
    protected JLabel className;
    protected JCheckBox fieldCheck;

    public ClassItem(ClassBean classBean, int position, CheckListener checkListener, CheckListener_Field checkListener_field) {
        this.classBean = classBean;
        this.position = position;
        this.checkListener = checkListener;
        this.checkListener_field = checkListener_field;

        classCheck = new JCheckBox();
        classCheck.setPreferredSize(new Dimension(40, 26));
        classCheck.setSelected(classBean.isSelect());
        classCheck.addChangeListener(new ClassCheckListener());


        fieldCheck = new JCheckBox();
        fieldCheck.setPreferredSize(new Dimension(40, 26));
        fieldCheck.setSelected(classBean.isSelect());
        fieldCheck.addChangeListener(new FieldCheckListener());

        className = new JLabel(classBean.getName(), 10);

//        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setMaximumSize(new Dimension(Short.MAX_VALUE, 54));
        add(Box.createRigidArea(new Dimension(24, 0)));
        add(classCheck);
        add(Box.createRigidArea(new Dimension(50, 0)));

        add(fieldCheck);
        add(Box.createRigidArea(new Dimension(50, 0)));

        add(className);
        add(Box.createRigidArea(new Dimension(50, 0)));


    }

    public class ClassCheckListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            checkListener.check(position, classCheck.isSelected());
        }
    }

    public class FieldCheckListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            checkListener_field.check_field(position, fieldCheck.isSelected());
        }
    }

    public interface CheckListener {
        void check(int position, boolean isSelect);
    }

    public interface CheckListener_Field {
        void check_field(int position, boolean isSelect);
    }
}
