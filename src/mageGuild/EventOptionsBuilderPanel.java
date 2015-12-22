package mageGuild;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class EventOptionsBuilderPanel extends JPanel{

	static final long serialVersionUID = 1L;
	
	//constructor
	public EventOptionsBuilderPanel(){
		
		setLayout(new MigLayout());
		setBorder(BorderFactory.createTitledBorder("Build Options"));
		this.add(new JTextField("you will and results here"), "cell 0 1");
		this.add(new JButton("add more"), "cell 1 1");
		this.setVisible(true);
	}
	
}
