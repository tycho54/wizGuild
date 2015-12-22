package mageGuild;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;

public class EventTextBuilderPanel extends JPanel{
	
	static final long serialVersionUID = 1L;
		
	//constructor
	public EventTextBuilderPanel(){
			
		setLayout(new MigLayout());
		setBorder(BorderFactory.createTitledBorder("Build Event text"));	
		
		
		JTextArea textArea = new JTextArea("here are some words", 10, 100);
		this.add(textArea, "cell 1 1");
		
		//this.add(new JButton("done"), "cell 1 3");
		//TODO need to make this button (or main gui button) take the 
		//text, and parse it for code its, and make an event out of it
		
		this.setVisible(true);
	}

}
	
	
	

