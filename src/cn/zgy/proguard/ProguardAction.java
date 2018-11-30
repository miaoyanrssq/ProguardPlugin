package cn.zgy.proguard;

import cn.zgy.proguard.form.ClassItem;
import cn.zgy.proguard.form.ClassList;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProguardAction extends AnAction implements ClassItem.CheckListener, ClassItem.CheckListener_Field {

    List<ClassBean> classPaths = new ArrayList<>();
    List<ClassBean> pkgPaths = new ArrayList<>();
    Set beanSet = new HashSet<>();

    List<String> modulePaths = new ArrayList<>();
    String path;
    private Project project;

    private JDialog jFrame;


    JCheckBox pkgCheck, sun_include,keepFile, includeModule;

    ClassList classList, pkgList;

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        project = e.getData(PlatformDataKeys.PROJECT);
        initView();
    }

    @Test
    public void initView() {
        getPackage();
//        testgetClass();
        jFrame = new JDialog();// 定义一个窗体Container container = getContentPane();
        jFrame.setModal(true);
        Container container = jFrame.getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));



        classList = new ClassList(project, (ArrayList<ClassBean>) classPaths, this, this);
        container.add(classList);
        pkgList = new ClassList(project, (ArrayList<ClassBean>) pkgPaths, this, this);
        pkgList.setVisible(false);
        container.add(pkgList);

        container.add(Box.createRigidArea(new Dimension(0, 25)));

        JPanel pkgChoice = new JPanel();// /定义一个面板
        pkgChoice.setLayout(new FlowLayout(FlowLayout.LEFT));
        pkgChoice.setBorder(BorderFactory.createTitledBorder("包配置"));
        pkgChoice.add(Box.createRigidArea(new Dimension(30, 0)));

        pkgCheck = new JCheckBox();
        pkgCheck.setText("按包选择");
        pkgCheck.setSelected(false);
        pkgCheck.addChangeListener(e -> {
            if(pkgCheck.isSelected()){
                classList.setVisible(false);
                pkgList.setVisible(true);
                if(sun_include != null){
                    sun_include.setVisible(true);
                }

            }else {
                classList.setVisible(true);
                pkgList.setVisible(false);
                if(sun_include != null){
                    sun_include.setVisible(false);
                }
            }
        });
        pkgChoice.add(pkgCheck);
        pkgChoice.add(Box.createRigidArea(new Dimension(60, 0)));

        sun_include = new JCheckBox();
        sun_include.setText("包含子包");
        sun_include.setSelected(false);
        sun_include.setVisible(false);
        pkgChoice.add(sun_include);

        container.add(pkgChoice);

        JPanel choice = new JPanel();// /定义一个面板
        choice.setLayout(new FlowLayout(FlowLayout.LEFT));
        choice.setBorder(BorderFactory.createTitledBorder("配置"));
        choice.add(Box.createRigidArea(new Dimension(30, 0)));

        keepFile = new JCheckBox();
        keepFile.setText("保留原文件内容");
        keepFile.setSelected(true);
        choice.add(keepFile);
        choice.add(Box.createRigidArea(new Dimension(20, 0)));
        includeModule = new JCheckBox();
        includeModule.setText("添加模版");
        includeModule.setSelected(false);
        choice.add(includeModule);
        container.add(choice);


        JPanel menu = new JPanel();
        menu.setLayout(new FlowLayout(FlowLayout.RIGHT));

        Button cancle = new Button();
        cancle.setPreferredSize(new Dimension(100, 30));
        cancle.setLabel("cancel");
        cancle.addActionListener(actionListener);

        Button ok = new Button();
        ok.setPreferredSize(new Dimension(100, 30));
        ok.setLabel("ensure");
        ok.addActionListener(actionListener);
        menu.add(cancle);
        menu.add(Box.createRigidArea(new Dimension(20, 0)));
        menu.add(ok);
        container.add(menu);


        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);

    }

    private void createFile(boolean isKeepPath) {
        String filePath = project.getBasePath() + "/App";
        String filename = "Template.txt";
        String content;
        if(includeModule.isSelected()) {
            content = readFile(filename);
        }else
        {
            content = "";
        }
        content = addSelectContent(content);
        writeToFlie(content, filePath, "proguard-rules.pro", isKeepPath);
    }

    private String addSelectContent(String content) {
        StringBuilder builder = new StringBuilder(content);
        if(pkgCheck.isSelected()){
            for (ClassBean bean : pkgPaths) {
                if(sun_include.isSelected()) {
                    if (bean.isField_include()) {
                        builder.append("\n-keep class " + bean.getName()+ ".**" + "{*;}");
                    } else if (bean.isSelect()) {
                        builder.append("\n-keep class " + bean.getName()+ ".**");
                    }
                }else{
                    if (bean.isField_include()) {
                        builder.append("\n-keep class " + bean.getName()+ ".*" + "{*;}");
                    } else if (bean.isSelect()) {
                        builder.append("\n-keep class " + bean.getName()+ ".*");
                    }
                }
            }

        }else {
            for (ClassBean bean : classPaths) {
                if (bean.isField_include()) {
                    builder.append("\n-keep class " + bean.getName() + "{*;}");
                } else if (bean.isSelect()) {
                    builder.append("\n-keep class " + bean.getName());
                }
            }
        }

        return builder.toString();
    }

    private void writeToFlie(String content, String filepath, String filename, boolean isKeepPath) {
        try {
            File floder = new File(filepath);
            if (!floder.exists()) {
                return;
            }
            File file = new File(filepath + "/" + filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            //true表示在文档末尾追加
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), isKeepPath);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFile(String filename) {
        InputStream in = null;
        in = this.getClass().getResourceAsStream(filename);
        String content = "";
        try {
            content = new String(readStream(in));
        } catch (Exception e) {

        }
        return content;
    }

    private byte[] readStream(InputStream in) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = in.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);

            }

        } catch (IOException e) {

        } finally {
            outputStream.close();
            in.close();
        }
        return outputStream.toByteArray();
    }

    /**
     * 获取所有有效module的路径
     */
    public void getPackage(){
        String pkgPath = project.getBasePath();
        doPkgPath(new File(pkgPath));
        getClasses();
    }

    private void doPkgPath(File file) {
        modulePaths.clear();
        pkgPaths.clear();
        beanSet.clear();
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(int i = 0 ; i< files.length ; i++){
                File item = files[i];
                if(item.isDirectory()){
                    File[] file_items = item.listFiles();
                    for (File file1 : file_items){
                        if(file1.getName().equals("src")){
                            modulePaths.add(file1.getPath());
                        }
                    }
                }

            }
        }
    }

    /**
     * 获取class 的包名和name
     */
    public void getClasses() {

        classPaths.clear();
//        path = project.getBasePath() + "/app/src/main/java/";
//        path = getClass().getResource("/").getPath();
        for(String modulePath : modulePaths) {
            path = modulePath +"/main/java/";
            doPath(new File(path), ".java");
        }
    }

    public void testgetClass(){
        path = getClass().getResource("/").getPath();
        doPath(new File(path), ".class");
    }

    /**
     * 获得com.example.ClassName形式的文件名，包名+classname
     *
     * @param file
     */
    private void doPath(File file, String end) {
        if (file.isDirectory()) {//文件夹
            //文件夹就递归
            File[] files = file.listFiles();
            for (File f1 : files) {
                doPath(f1, end);
            }
        } else {//文件
            if (file.getName().endsWith(end)) {
                String classpath = file.getPath().replace(path , "").replace("/", ".").replace(end, "");
                System.out.println("文件:" + classpath);
                ClassBean bean = new ClassBean();
                bean.setName(classpath);
                classPaths.add(bean);

                /**
                 * pkg
                 */
                String pkgpath = file.getPath().replace(path , "").replace("/", ".").replace("." + file.getName(), "");
                //利用Set去重
                if(beanSet.add(pkgpath)){
                    ClassBean pkgbean = new ClassBean();
                    pkgbean.setName(pkgpath);
                    pkgPaths.add(pkgbean);
                }

            }

        }
    }

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("取消")) {
                jFrame.dispose();
            } else {
                jFrame.dispose();
                createFile(keepFile.isSelected());
                Messages.showInfoMessage(project, "生成完毕,目录：app/", "提示");

            }

        }
    };


    @Override
    public void check(int position, boolean isSelect) {
        if(pkgCheck.isSelected()){
            pkgPaths.get(position).setSelect(isSelect);
        }else {
            classPaths.get(position).setSelect(isSelect);
        }
    }

    @Override
    public void check_field(int position, boolean isSelect) {
        if(pkgCheck.isSelected()){
            pkgPaths.get(position).setField_include(isSelect);
        }else {
            classPaths.get(position).setField_include(isSelect);
        }
    }
}
