package mageGuild;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class PredicateBuilderPanel extends JPanel {
	
	static final long serialVersionUID = 1L;
	
	//constructor
	public PredicateBuilderPanel(){
		
		setLayout(new MigLayout());
		setBorder(BorderFactory.createTitledBorder("Build Predicate"));
		//TODO: line below is just a place holder for now
		this.add(new JTextField("You will enter predicate here"), "cell 1 1");
		
		this.setVisible(true);
	}

}
