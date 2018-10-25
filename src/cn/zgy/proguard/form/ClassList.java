package cn.zgy.proguard.form;

import cn.zgy.proguard.ClassBean;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ClassList extends JPanel {

    ClassItem.CheckListener checkListener;
    ClassItem.CheckListener_Field checkListener_field;

    protected Project project;
    protected ArrayList<ClassBean> classBeans;
    protected ArrayList<ClassItem> classItems = new ArrayList<>();

    public ClassList(Project project, ArrayList<ClassBean> classBeans, ClassItem.CheckListener checkListener, ClassItem.CheckListener_Field checkListener_field) {
        this.project = project;
        this.classBeans = classBeans;
        this.checkListener = checkListener;
        this.checkListener_field = checkListener_field;

        setPreferredSize(new Dimension(770, 360));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        addInjections();
    }

    private void addInjections() {
        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        contentPanel.add(new PluginHeader());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JPanel injectionsPanel = new JPanel();
        injectionsPanel.setLayout(new BoxLayout(injectionsPanel, BoxLayout.PAGE_AXIS));
        injectionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        int cnt = 0;
        for(int i = 0 ; i< classBeans.size() ; i++){
            ClassItem item = new ClassItem(classBeans.get(i), i, checkListener, checkListener_field);
            if(cnt > 0){
                injectionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
            injectionsPanel.add(item);
            cnt++;
            classItems.add(item);
        }

        injectionsPanel.add(Box.createVerticalGlue());
        injectionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JBScrollPane scrollPane = new JBScrollPane(injectionsPanel,JBScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JBScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(740, 300));
        contentPanel.add(scrollPane);

        add(contentPanel, BorderLayout.CENTER);
        refresh();
    }

    protected void refresh(){
        revalidate();
    }
}
