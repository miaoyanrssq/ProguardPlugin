package cn.zgy.proguard.form;

import org.junit.Test;

import javax.swing.*;
import java.awt.*;

public class PluginHeader extends JPanel {

	protected JLabel mClassSelect;
	protected JLabel className;
	protected JLabel mFieldInclude;

	public PluginHeader() {
		mClassSelect = new JLabel("classSelect");
		mClassSelect.setPreferredSize(new Dimension(100, 26));
		mClassSelect.setFont(new Font(mClassSelect.getFont().getFontName(), Font.BOLD, mClassSelect.getFont().getSize()));

		mFieldInclude  = new JLabel("fieldInclude");
		mFieldInclude.setPreferredSize(new Dimension(100,26));
		mFieldInclude.setFont(new Font(mFieldInclude.getFont().getFontName(),Font.BOLD,mFieldInclude.getFont().getSize()));

		className = new JLabel("name");
		className.setPreferredSize(new Dimension(200, 26));
		className.setFont(new Font(className.getFont().getFontName(), Font.BOLD, className.getFont().getSize()));



//		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setLayout(new FlowLayout(FlowLayout.LEFT));
//		add(Box.createRigidArea(new Dimension(50, 0)));
		add(mClassSelect);
//		add(Box.createRigidArea(new Dimension(10, 0)));
		add(mFieldInclude);
		add(Box.createRigidArea(new Dimension(80, 0)));
		add(className);

	}

}
