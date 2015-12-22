package mageGuild;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;


public class MainGuiPanel extends JPanel {
	//an object of this class will be the main GUI area of the game.  It is what
	//the player interacts with throughout the game.  It interacts with the 
	//gameState ojbect, and displays different information/interfaces depending
	//upon the phase of the game.  

	//attributes
	static final long serialVersionUID = 1L;
	private GameState gameState;
	GridBagConstraints gbc;
	Gui fullGui;
	
	//phase 1 attributes:
	private Integer startTime;
	private Integer currentTime;
	private JLabel currentRestTime;
	private ArrayList<MiniGui> miniGuis;
	private HashMap<String, Integer[]> nextGroupLocation;
	private LinkedHashMap<TimeSpendOption, Integer> activitiesAndTimes
	        = new LinkedHashMap<TimeSpendOption, Integer>();
	int tempCol;
	int tempRow;
	
	
	//constructor
	public MainGuiPanel(GameState aGameState, Gui theGui){
		gameState = aGameState;
		fullGui = theGui;
		//Dimension size = getPreferredSize();
		//size.setSize(975, 650);
		//setPreferredSize(size);
			
		setLayout(new MigLayout());
		displayPanel();
	}
	
	
	private void displayPanel(){
	//this method checks the game phase and calls the appropriate
	//display method based on the phase.
		
		setBorder(BorderFactory.createTitledBorder(  
				RulesSet.getBorderString(gameState)
		));
		
		fullGui.displayPlayerInfo();
		
		String phaseType = RulesSet.getPhaseType(gameState);
		this.removeAll();
		if(phaseType == "spend"){
			displaySpendPhase();
		} else if(phaseType == "select"){
			displaySelectPhase();
		} else if(phaseType == "event"){
			displayEventPhase();
		}
		
	}
	
	
	private void displaySpendPhase(){
		/*
		 * use this during resource spending phases
		 */
		
		JLabel description = new JLabel("How will you spend your time this month?");
		this.add(description, "span 3, wrap 15");
		//reset phase 1 attributes:
		this.startTime = RulesSet.getFullTimeAllowed();
		this.currentTime = this.startTime;
		
		currentRestTime = new JLabel("Rest and Relaxation: " + this.currentTime);
		this.add(currentRestTime, "span 4, align center, wrap 25");
		nextGroupLocation = new HashMap<String, Integer[]>();
		
		//now add the things the player can spend time on:
		HashMap<String, ArrayList<TimeSpendOption>> activityGroups = RulesSet.getSpendActivities(gameState);
		
		miniGuis = new ArrayList<MiniGui>();
		tempCol = 0;
		tempRow = 3;
		
		activityGroups.forEach((name, options) -> {
			MiniGui temp = new MiniGui(name, options, this, 1);
			nextGroupLocation.put(name, new Integer[]{0,tempRow});
			addMiniGui(temp, name);
			
			//want previous month's choices to remain, so need to
			//loop over last months time spending and use it to 
			//fill in miniguis:
			//NOTE--this is ugly way of doing things.  .containsKey didn't
			//seem to be working, so this is a kludge.  much room for 
			//cleaning this up.
			options.forEach((TimeSpendOption currentOption) -> {
				activitiesAndTimes.forEach((TimeSpendOption key, Integer value)->{
					if(key.toString().equals(currentOption.toString()) & value > 0){
						if(temp.getTimeToSpend() > 0){
							temp.getLastMiniGui().expandIfPossible();
						}
						//do stuff to last one
						temp.getLastMiniGui().setSelected(currentOption);
						temp.getLastMiniGui().setTime(value);
						//System.out.println("do stuff w/" + currentOption);
					}
				});
			});
			
			
			
			
			//this.add(temp,  "cell "+tempCol + " " +tempRow);
			tempRow++;
		});
		
		//now need to set the mini gui's to the values of last month's 
		//time spend, if any:
		activitiesAndTimes.forEach((TimeSpendOption, Integer)->{
			//System.out.println(TimeSpendOption);
		});
		
		
		
		
		//add a done button; when clicked it needs to send the info to the gameState
		JButton doneButton = new JButton("Done");
		doneButton.addActionListener((ActionEvent e) -> {
			//reset the hashmap of the options and the time spent on each
			activitiesAndTimes = new LinkedHashMap<TimeSpendOption, Integer>();
			for(MiniGui tempGui: miniGuis){
				activitiesAndTimes.put(tempGui.getSelected(), tempGui.getTimeToSpend());
			}
			//also add rest and relaxation:
			activitiesAndTimes.put(RulesSet.getRestTimeSpendOption(), currentTime);
			
			//tell the game state to enter the next phase
			//note: we don't submit the values to the ruleset until after the next phase, due
			//to there being a back button they can use.
			gameState.nextPhase();
			displayPanel();
		});
		
		
		this.add(doneButton, "South");
		this.revalidate();
	}
	//a few methods required in phase1:
	public void changeCurrentTimeBy(Integer changeBy){
		this.currentTime += changeBy;
		this.currentRestTime.setText("Rest and Relaxation: " + this.currentTime);
	}
	public int getCurrentTime(){
		return currentTime;
	}
	public Integer[] getNextGroupLocation(String name){
		if(nextGroupLocation.containsKey(name)){
			return(nextGroupLocation.get(name));
		} else {
			return new Integer[]{};
		}
	}
	public void addMiniGui(MiniGui miniGui, String groupName){
		miniGuis.add(miniGui);
		Integer[] tempLocation = getNextGroupLocation(groupName);
		Integer x = tempLocation[0]+1;
		Integer y = tempLocation[1];
		this.add(miniGui, "cell " + x + " " + y);
		this.revalidate();
		this.repaint();
	}
	public ArrayList<MiniGui> getMiniGuis(){
		return miniGuis;
	}
	
	private void displaySelectPhase(){
		/*
		 * use this during selection phases (ie, phases when you make choices to trigger events)
		 * formerly displayPhase2()
		 */
		
		
		JLabel description = new JLabel("Here you will select your special action for the month.  GUI TBD.");
		this.add(description, "wrap 15");
		
		
		JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RulesSet.goBackOnePhase(gameState);
				displayPanel();
			}          
	    });
		this.add(backButton, "cell 0 1");
		
		JButton doneButton = new JButton("Done");
		doneButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameState.nextPhase();
				//results of last phase now submitted, since we're past the point that they 
				//can use the back button.
				RulesSet.submitSpendPhase(activitiesAndTimes, gameState);
				displayPanel();
			}          
	    });
		this.add(doneButton, "cell 3 1");
		
		this.revalidate();
		
		
	}
	
	private void displayEventPhase(){
		/*
		 * use this for event phases
		 * formerly displayPhase3()
		 */
		
		JLabel description = new JLabel("This phase presents the random monthly event.");
		this.add(description, "wrap 15");
		
		//TODO need to ask the rulesSet to get us the current event here
		Event tempEvent = RulesSet.sampleEvent(gameState);
		
		JTextArea eventText = new JTextArea(tempEvent.getText());
		eventText.setEditable(false);
		eventText.setLineWrap(true);
		eventText.setWrapStyleWord(true);
		eventText.setPreferredSize(new Dimension(250, 150));
		this.add(eventText, "align center, wrap 15");
		
		
		for(Event.EventOption option : tempEvent.getOptions()){
			JLabel tempLabel = new JLabel(option.getText());
			if(option.optionAvailable()){
				tempLabel.addMouseListener(new MouseListener(){
					public void mouseClicked(MouseEvent e){
						option.selectOption();
						gameState.nextPhase();
						displayPanel();
					}
					public void mouseExited(MouseEvent e){}
					public void mouseEntered(MouseEvent e){}
					public void mousePressed(MouseEvent e){}
					public void mouseReleased(MouseEvent e){}
				});
				tempLabel.setForeground(Color.blue);
			} else {
				
				tempLabel.setForeground(Color.red);
				
			}
			this.add(tempLabel, "wrap");
			
		}
		
		this.revalidate();
	}
	
	
	
	
}
