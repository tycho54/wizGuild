package mageGuild;

import java.awt.EventQueue;

public class Game {

	/**
	 * @param args (none used)
	 * This is the class that holds the main function for running 
	 * a game of mage guild.  
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//this creates and displays the GUI for the first time
		EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
        
            	//TODO  need to replace this with a chargen (and world init)
            	Character playerCharacter = RulesSet.sampleCharacter();
            	
            	
            	GameState gameState = new GameState(playerCharacter);
            	Gui theGUI = new Gui(gameState);
                theGUI.setVisible(true);
            }
        });
		
	}

}
