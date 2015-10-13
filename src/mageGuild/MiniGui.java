package mageGuild;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
//import javax.swing.MutableComboBoxModel;

import net.miginfocom.swing.MigLayout;

public class MiniGui extends JPanel implements ActionListener {
	static final long serialVersionUID = 1L;
	Integer timeToSpend;
	JLabel timeToSpendLabel;
	JButton lessTime;
	JButton moreTime;
	JButton splitButton;
	String myGroupName;
	JComboBox<TimeSpendOption> optionsBox;
	DefaultComboBoxModel<TimeSpendOption> options;
	MainGuiPanel linkedGuiPanel;
	TimeSpendOption previousOption;
	TimeSpendOption currentOption;
	Integer membersInGroup; //this is the number of other miniGuis in the panel that share the items
	
	
	// most stuff gets created in the constructor:
	public MiniGui(String activityName, 
			ArrayList<TimeSpendOption> options, 
			MainGuiPanel linkedGuiPanel,
			Integer groupSize){	
		
		setLayout(new MigLayout());
		timeToSpend = new Integer(0);
		myGroupName = activityName;
		setBorder(BorderFactory.createTitledBorder(myGroupName));
		this.linkedGuiPanel = linkedGuiPanel;
		this.currentOption = options.get(0);
		this.previousOption = null;
		membersInGroup = groupSize;
		
		
		
		//drop down box for selecting the sub topic
		optionsBox = new JComboBox<TimeSpendOption>(options.toArray(new TimeSpendOption[options.size()]));
		//create the internal list of options from the comboboxmodel:
		this.options = (DefaultComboBoxModel<TimeSpendOption>)optionsBox.getModel();
		
		optionsBox.setSelectedIndex(0);
		this.add(optionsBox, "cell 0 0, spanx 3, align center, growx");
		optionsBox.addActionListener(this);
		//note, the action listener effects are handled in a method below
		
		
		//label to displace the amount of time
		timeToSpendLabel = new JLabel(""+timeToSpend);
		this.add(timeToSpendLabel, "cell 1 1, align center, gapx 15px 15px");
		
		//button to decrease time
		lessTime = new JButton("-");
		lessTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(timeToSpend > 0){
					timeToSpend--;
					timeToSpendLabel.setText(""+timeToSpend);
					linkedGuiPanel.changeCurrentTimeBy(1);
				}
			}          
	    });
		this.add(lessTime, "cell 0 1");
		
		//button to add more time
		moreTime = new JButton("+");
		moreTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(timeToSpend < options.get(optionsBox.getSelectedIndex()).getMaxTime()
						& linkedGuiPanel.getCurrentTime() > RulesSet.getMinTimeAllowed()){
					timeToSpend++;
					timeToSpendLabel.setText(""+timeToSpend);
					linkedGuiPanel.changeCurrentTimeBy(-1);
				}
			}          
	    });
		this.add(moreTime, "cell 2 1");	
		
		//create a button to make a linked mini gui with the same options:
		//but only do so if there are enough items on list to justify such a button
		if(this.options.getSize() > 1 & membersInGroup < 5){
			//number 5 is arbitrary here, but thought that more than that wouldn't fit on screen
			splitButton = new JButton(">");
			
			splitButton.addActionListener(l -> {
				this.splitButton.setVisible(false);
				
				//make a new list of options for the new miniGui.  Is same as this object's list
				//of options, but without the currently selected item
				ArrayList<TimeSpendOption> nextOptions = new ArrayList<TimeSpendOption>();
				
				for(int i=0; i< this.options.getSize(); i++){
					TimeSpendOption option = (TimeSpendOption)(this.options.getElementAt(i));
					if(option != currentOption){
						nextOptions.add(option);
					}		
				}
				
				MiniGui temp = new MiniGui(activityName, nextOptions, linkedGuiPanel, this.membersInGroup + 1);;
				//send it to the linked panel
				linkedGuiPanel.addMiniGui(temp, activityName);
				//remove the splitButton, as only the newest miniGui in the group should have it:
				this.remove(splitButton);
				
				//now need to remove the selected item of the newly created gui from all others
				//in the same group:
				linkedGuiPanel.getMiniGuis().forEach((miniGui)-> {
					if(miniGui.getGroupName().equals(this.getGroupName()) & miniGui != temp){
						miniGui.removeOption(nextOptions.get(0));
					}
				});
				
				linkedGuiPanel.setPreferredSize(linkedGuiPanel.getPreferredSize());
				//linkedGuiPanel.revalidate();
				//linkedGuiPanel.repaint();
			});
			this.add(splitButton, "cell 3 0, spany 2, growy");
		}
		
	} //closes the constructor
	
	
	
	//some getters:
	public String getGroupName(){
		return myGroupName;
	}
	public Integer getTimeToSpend(){
		return(timeToSpend);
	}
	public TimeSpendOption getSelected(){
		return (TimeSpendOption)optionsBox.getSelectedItem();
	}
	

	//two methods for adding/removing elements
	private void addOption(TimeSpendOption option){
		((DefaultComboBoxModel<TimeSpendOption>)(optionsBox.getModel())).addElement(option);
		this.repaint();
		this.revalidate();
	}
	private void removeOption(TimeSpendOption option){
		((DefaultComboBoxModel<TimeSpendOption>)(optionsBox.getModel())).removeElement(option);
		this.repaint();
		this.revalidate();
	}
	
	//if they change the selected option, need to remove that option from 
	//other mini guis with the same name, and add back the name of the
	//option that was previously selected.
	public void actionPerformed(ActionEvent e){
		previousOption = currentOption;
		currentOption = this.getSelected();
		linkedGuiPanel.getMiniGuis().forEach((miniGui)-> {
			if(miniGui.getGroupName().equals(this.getGroupName()) & miniGui != this ){
				miniGui.addOption(previousOption);
				miniGui.removeOption(currentOption);
			}
		});
	}
	
	
	
}
