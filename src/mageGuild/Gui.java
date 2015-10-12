package mageGuild;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import net.miginfocom.swing.MigLayout;


public class Gui extends JFrame{
/*
 * this is the main, overall gui for the mage guild game.  its various
 * components will have their own classes/objects.  the main function
 * in the Game class will communicate with the GUI, call its methods to
 * update what the user sees.  likewise, the GUI will send messages to
 * send messages to the GameState ojects based on what the user does.  
 */
	
	//declare attributes
	private GameState gameState;
	public GameState getGameState(){
		return(gameState);
	}
	
	static final long serialVersionUID = 1L;
	private PlayerInfoPanel playerInfoPanel;
	private MainGuiPanel mainGuiPanel;
	private Container pane;
	
	public JMenuBar menuBar;
	public JMenu menu;
	public JMenuItem menuItem;
	
	
	//constructor
	public Gui(GameState aGameState){
		gameState = aGameState;
		
		setTitle("Mage Guild");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        pane = getContentPane();
        
        setLayout(new MigLayout());
   
        addMenuBar(); 
        
        playerInfoPanel = new PlayerInfoPanel(gameState.getPlayerCharacter(), this);
        pane.add(playerInfoPanel, "cell 0 0");
        
        mainGuiPanel = new MainGuiPanel(gameState, this);
        pane.add(mainGuiPanel, "cell 1 0");
        
	}
	
	//these two functions can be used to repaint the two panels:
	public void displayPlayerInfo(){
		playerInfoPanel.removeAll();
		this.remove(playerInfoPanel);
	    playerInfoPanel = new PlayerInfoPanel(gameState.getPlayerCharacter(), this);
	    pane.add(playerInfoPanel,"cell 0 0");
	}
	
	public void displayMainInfo(){
	    mainGuiPanel.removeAll();
	    this.remove(mainGuiPanel);
	    mainGuiPanel = new MainGuiPanel(gameState, this);
	    pane.add(mainGuiPanel,"cell 1 1");
	}
	
	
	private void addMenuBar(){
		//this function just builds the menu bar.  moved it here to keep things
		//a bit less cluttered in the constructor.
        //build the menu bar
		
		//TODO need to decide the actual menu items and make them do something
        menuBar = new JMenuBar();
        //first menu 
        menu = new JMenu("File");
        menuBar.add(menu);
        menuItem = new JMenuItem("test");
        menu.add(menuItem);
        menuItem = new JMenuItem("another test");
        menu.add(menuItem);
        //next menu
        menu = new JMenu("Player");
        menuBar.add(menu);
        menuItem = new JMenuItem("test");
        menu.add(menuItem);
        menuItem = new JMenuItem("another test");
        //next menu
        menu = new JMenu("Spells");
        menuBar.add(menu);
        menuItem = new JMenuItem("test");
        menu.add(menuItem);
        menuItem = new JMenuItem("another test");
        //next menu
        menu = new JMenu("Characters");
        menuBar.add(menu);
        menuItem = new JMenuItem("test");
        menu.add(menuItem);
        menuItem = new JMenuItem("another test");
        
        
        
        
        this.setJMenuBar(menuBar);
	}
	
}
