package mageGuild;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;

public class Game {

	/**
	 * @param args (none used)
	 * This is the class that holds the main function for running 
	 * a game of mage guild.  
	 */
	public static void main(String[] args) {
		
		//this creates and displays the GUI for the first time
		EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
      	
            	//temporary branch here to get into event builder.  
            	//TODO remove this in final version
            	final JFrame questionPane = new JFrame();
            	questionPane.setLayout(new MigLayout());
            	JButton gameButton = new JButton();
            	JButton eventBuilderButton = new JButton();
            	
            	gameButton.setText("Start Game");
            	gameButton.addActionListener((event)->{
            		//TODO  need to replace this with a chargen (and world init)
                	Character playerCharacter = RulesSet.sampleCharacter();
                	
                	GameState gameState = new GameState(playerCharacter);
                	Gui theGUI = new Gui(gameState);
                    theGUI.setVisible(true);
                    questionPane.dispose();
                    
                    
                    /* this stuff is for testing only
                    gameState.getPlayerCharacter().addFlag("test", "blah");
                    gameState.addFlag("otherTest", "foo");
                    //String tempString = "flagCalled_FromCharacter npcTest npcNamed bob";
                    String tempString = "menWomenOf NPCnamed bob";
                    Character tempChar = new Character("Robert", Character.Gender.MALE);
                    gameState.addNPC("bob", tempChar);
                    gameState.getNPC("bob").addFlag("npcTest", "bar");
                    //System.out.println(gameState.getNPC("bob").getFlag("npcTest"));
                    
             
                    System.out.println(
                    		//TextParser.parseText(new ArrayList<String>(Arrays.asList(tempString.split(" ")))).apply(gameState)
                    		TextParser.parseFullText("££ flagCalled_FromCharacter     test  playerCharacter  ££ words ££menWomenOf NPCnamed bob££ other words").apply(gameState)
                    		);
                    */
            	});
            	
            	eventBuilderButton.setText("Event Builder");
            	eventBuilderButton.addActionListener((event)->{
            		EventBuilderGui eventBuilderGui = new EventBuilderGui();
            		eventBuilderGui.setVisible(true);
            		questionPane.dispose();
            	});
            	
            	questionPane.add(gameButton, "cell 1 1");
            	questionPane.add(eventBuilderButton, "cell 2 1");
            	questionPane.pack();
            	questionPane.setVisible(true);
            	
            	
            	
            	
            	/* commented out while event builder is in use
            	//TODO  need to replace this with a chargen (and world init)
            	Character playerCharacter = RulesSet.sampleCharacter();
            	
            	GameState gameState = new GameState(playerCharacter);
            	Gui theGUI = new Gui(gameState);
                theGUI.setVisible(true);
                */
            }
        });
		
	}

}
