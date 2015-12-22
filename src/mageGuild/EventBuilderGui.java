package mageGuild;

import java.awt.Color;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;

public class EventBuilderGui extends JFrame {
	
	static final long serialVersionUID = 1L;
	//private PredicateBuilderPanel predicateBuilderPanel;
	//private EventTextBuilderPanel eventTextBuilderPanel;
	//private EventOptionsBuilderPanel eventOptionsBuilderPanel;
	private JPanel eventPredicatePanel;
	private JPanel eventTextPanel;
	private JTextArea eventTextArea;
	private JTextArea eventPredicateTextArea;
	private JPanel eventOptionsPanel;
	private JPanel optionPredicatePanel;
	private JPanel optionTextPanel;
	private JPanel optionConsequencePanel;
	private JButton doneButton;
	public JMenuBar menuBar;
	public JMenu menu;
	public JMenuItem menuItem;
	
	
	private String eventPredicateText;
	private String eventText;
	private ArrayList<LinkedHashMap<String, String>> optionsList;
	
	private final JFileChooser fileChooser = new JFileChooser();
			
	private enum ValidationStatus {
		valid, invalid, unchecked
	}
	private ValidationStatus eventTextValidated;
	private ValidationStatus eventPredicateValidated;
	private ValidationStatus optionsValidated;
	private ArrayList<ValidationStatus> optionsPredicateValidated;
	private ArrayList<ValidationStatus> optionsTextValidated;
	private ArrayList<ValidationStatus> optionsConsequencesValidated;
	private int currentOption;
	
	
	
	
	//constructor
	public EventBuilderGui(){
		setLayout(new MigLayout());
		setTitle("Event Builder");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        eventPredicateText = "Enter event Predicate here";
        eventText = "Enter event text here";
        optionsList = new ArrayList<LinkedHashMap<String, String>>();
        optionsList.add(createBlankOption());
        eventTextValidated = ValidationStatus.invalid;
    	eventPredicateValidated = ValidationStatus.invalid;
    	optionsValidated = ValidationStatus.invalid;
    	optionsPredicateValidated = new ArrayList<ValidationStatus>();
    	optionsPredicateValidated.add(ValidationStatus.invalid);
    	optionsTextValidated = new ArrayList<ValidationStatus>();
    	optionsTextValidated.add(ValidationStatus.invalid);
    	optionsConsequencesValidated = new ArrayList<ValidationStatus>();
    	optionsConsequencesValidated.add(ValidationStatus.invalid);
        currentOption = 0;
    	
        //Event Predicate section:
        eventPredicatePanel = new JPanel();
        eventPredicatePanel.setLayout(new MigLayout());
        eventPredicatePanel.setBorder(BorderFactory.createTitledBorder("Event Predicate"));
        //eventPredicatePanel.add(new JLabel("Event Predicate builder"), "cell 0 0");
		eventPredicateTextArea = new JTextArea("Enter event Predicate here", 5, 50);
		eventPredicatePanel.add(eventPredicateTextArea, "cell 0 1");		
		JButton eventPredicateValidationButton = new JButton("Validate");
		JLabel eventPredicateValidationLabel = new JLabel();
		colorCodedValidation(eventPredicateValidationLabel, eventTextValidated);			
		eventPredicateValidationButton.addActionListener((event)->{
			//TODO currently just a stub that does no checking.  Need to
			//make this actually validate the values
			eventPredicateValidated = ValidationStatus.valid;
			colorCodedValidation(eventPredicateValidationLabel, eventPredicateValidated);
		});
		eventPredicatePanel.add(eventPredicateValidationButton, "cell 1 2");
		eventPredicatePanel.add(eventPredicateValidationLabel, "cell 2 2");
		eventPredicatePanel.setVisible(true);	
        this.add(eventPredicatePanel, "cell 1 1");
    	
        
        
        //event Text Section
        eventTextPanel = new JPanel();
        eventTextPanel.setLayout(new MigLayout());
        eventTextPanel.setBorder(BorderFactory.createTitledBorder("Event Text"));
        //eventTextPanel.add(new JLabel("Event Text builder"), "cell 0 0");
		eventTextArea = new JTextArea("Add Event Text Here.", 10, 50);
		eventTextPanel.add(eventTextArea, "cell 0 1");	
		JButton eventTextValidationButton = new JButton("Validate");
		JLabel eventTextValidationLabel = new JLabel();
		colorCodedValidation(eventTextValidationLabel, eventTextValidated);	
		//add a listener to change validation status on changes to text:
		eventTextArea.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
				    public void removeUpdate(DocumentEvent e) {
						eventTextValidated = ValidationStatus.unchecked;
						colorCodedValidation(eventTextValidationLabel, eventTextValidated);
				    }
					@Override
				    public void insertUpdate(DocumentEvent e) {
						eventTextValidated = ValidationStatus.unchecked;
						colorCodedValidation(eventTextValidationLabel, eventTextValidated);
				    }
					@Override
			        public void changedUpdate(DocumentEvent arg0) {
						eventTextValidated = ValidationStatus.unchecked;
						colorCodedValidation(eventTextValidationLabel, eventTextValidated);
			        }
				}	
		);
		
		eventTextValidationButton.addActionListener((event)->{
			try{
				Function<GameState, String> textValidationTest = TextParser.parseFullText(eventTextArea.getText());
				String testString = textValidationTest.apply(new GameState( RulesSet.sampleCharacter()));
				eventTextValidated = ValidationStatus.valid;
			} catch (UnsupportedOperationException e) {
				eventTextValidated = ValidationStatus.invalid;
				//System.out.println(e.getMessage());
			}
			
			
			colorCodedValidation(eventTextValidationLabel, eventTextValidated);
		});
		eventTextPanel.add(eventTextValidationButton, "cell 1 2");
		eventTextPanel.add(eventTextValidationLabel, "cell 2 2");
		eventTextPanel.setVisible(true);	
    	this.add(eventTextPanel, "cell 1 2");
        
    	
    	//Event options section
        eventOptionsPanel = new JPanel();
        eventOptionsPanel.setLayout(new MigLayout());
        eventOptionsPanel.setBorder(BorderFactory.createTitledBorder("Event Options"));
        displayOptionsList();
		eventOptionsPanel.setVisible(true);	
    	this.add(eventOptionsPanel, "cell 1 3");
        
    	
    	//current option predicate section
        optionPredicatePanel = new JPanel();
        optionPredicatePanel.setLayout(new MigLayout());
        optionPredicatePanel.setBorder(BorderFactory.createTitledBorder("Option Predicate"));
		optionPredicatePanel.setVisible(true);	
    	this.add(optionPredicatePanel, "cell 2 1");
    	
    	//options text section
        optionTextPanel = new JPanel();
        optionTextPanel = new JPanel();
        optionTextPanel.setLayout(new MigLayout());
        optionTextPanel.setBorder(BorderFactory.createTitledBorder("Option Text"));
		optionTextPanel.setVisible(true);	
    	this.add(optionTextPanel, "cell 2 2");
        
    	
    	//consequences panel
        optionConsequencePanel = new JPanel();
        optionConsequencePanel.setLayout(new MigLayout());
        optionConsequencePanel.setBorder(BorderFactory.createTitledBorder("Build Consequences"));
    	optionConsequencePanel.setVisible(true);		
        this.add(optionConsequencePanel, "cell 2 3");
        
        displayOptionData(currentOption);
        
        
        doneButton = new JButton("Validate and Export");
        this.add(doneButton, "cell 1 4, span 2");
        addMenuBar();
		this.revalidate();
		
		//TODO will need a "save" button at the bottom, which opens a file
		//selector?--no, should go into the file option on menu
		//might also want undo buttons in each box?
	}
	
	
	private void displayOptionData(int j){
		//first set the current option to the one about to be displayed
		this.currentOption = j;
		//predicate panel first:
		optionPredicatePanel.removeAll();
		optionPredicatePanel.add(new JLabel("Option " +(currentOption+1) + " Predicate"), "cell 0 0");
		JTextArea optionPredicateTextArea = new JTextArea(optionsList.get(j).get("Predicate"), 5, 50);
		optionPredicatePanel.add(optionPredicateTextArea, "cell 0 1");		
		JButton optionPredicateValidationButton = new JButton("Validate");
		JLabel optionPredicateValidationLabel = new JLabel();
		colorCodedValidation(optionPredicateValidationLabel, optionsPredicateValidated.get(j));			
		optionPredicateValidationButton.addActionListener((event)->{
			//TODO currently just a stub that does no checking.  Need to
			//make this actually validate the values
			optionsPredicateValidated.set(currentOption, ValidationStatus.valid);
			colorCodedValidation(optionPredicateValidationLabel, optionsPredicateValidated.get(j));
		});
		optionPredicatePanel.add(optionPredicateValidationButton, "cell 1 2");
		optionPredicatePanel.add(optionPredicateValidationLabel, "cell 2 2");
		optionPredicatePanel.revalidate();
		
		//option text section:
		optionTextPanel.removeAll();
		optionTextPanel.add(new JLabel("Option " +(currentOption+1) + " Text"), "cell 0 0");
		JTextArea optionTextArea = new JTextArea(optionsList.get(j).get("Text"), 10, 50);
		optionTextPanel.add(optionTextArea, "cell 0 1");	
		JButton optionTextValidationButton = new JButton("Validate");
		JLabel optionTextValidationLabel = new JLabel();
		colorCodedValidation(optionTextValidationLabel, optionsTextValidated.get(j));			
		optionTextValidationButton.addActionListener((event)->{
			//TODO currently just a stub that does no checking.  Need to
			//make this actually validate the values
			optionsTextValidated.set(currentOption, ValidationStatus.valid);
			colorCodedValidation(optionTextValidationLabel, optionsTextValidated.get(j));
		});
		optionTextPanel.add(optionTextValidationButton, "cell 1 2");
		optionTextPanel.add(optionTextValidationLabel, "cell 2 2");
		optionTextPanel.revalidate();
		
		//option consequence section:
		optionConsequencePanel.removeAll();
		optionConsequencePanel.add(new JLabel("Consequences Panel"), "cell 0 0");
    	JTextArea consequencesTextArea = new JTextArea(optionsList.get(j).get("Consequences"), 5, 50);
    	optionConsequencePanel.add(consequencesTextArea, "cell 0 1");
    	JButton consequenceValidationButton = new JButton("Validate");
		JLabel consequenceValidationLabel = new JLabel();
		colorCodedValidation(consequenceValidationLabel, optionsConsequencesValidated.get(j));			
		consequenceValidationButton.addActionListener((event)->{
			//TODO currently just a stub that does no checking.  Need to
			//make this actually validate the values
			optionsConsequencesValidated.set(currentOption, ValidationStatus.valid);
			colorCodedValidation(consequenceValidationLabel, optionsConsequencesValidated.get(j));
		});
		optionConsequencePanel.add(consequenceValidationButton, "cell 1 2");
		optionConsequencePanel.add(consequenceValidationLabel, "cell 2 2");
		optionConsequencePanel.revalidate();
		
	}
	
	private void addMenuBar(){
		//this function just builds the menu bar.  moved it here to keep things
		//a bit less cluttered in the constructor.
		
		//TODO need to decide the actual menu items and make them do something
		//will need to load existing, save current, and clear to blank...
		//anything other than those?
		//should be able to save invalid data for later use, so need a separate
		//option to export a working finished event.
        menuBar = new JMenuBar();
        //first menu 
        menu = new JMenu("File");
        menuBar.add(menu);
        
        menuItem = new JMenuItem("save");
        menuItem.addActionListener((event)->{
        	//Create a file chooser
        	int returnVal = fileChooser.showSaveDialog(this);
        	if (returnVal == JFileChooser.APPROVE_OPTION) {
        		File file = fileChooser.getSelectedFile();
        		
        		//TODO need to capture all the options data here too,
        		//once we have something in place for that.
        		//may want to add extra words to delimit starts/stops of
        		//sections too, to make reading in easier
        		ArrayList<String> tempArray = new ArrayList<String>();
        		tempArray.add(eventPredicateTextArea.getText());
        		tempArray.add(eventTextArea.getText());
        		
        		try{
        			if(!file.exists()){
        				Files.write(Paths.get(file.getPath()), tempArray);
        			} else {
        				//TODO should be able to overwrite in final version
        				System.out.println("can't overwrite this file");
        			}
        		} catch(Exception e){
        			System.out.println("Error in saving to file: " + file);
        		}
   
            } 
        });
        menu.add(menuItem);
        
        menuItem = new JMenuItem("load");
        menuItem.addActionListener((event)->{
        	//Create a file chooser
        	int returnVal = fileChooser.showOpenDialog(this);
        	if (returnVal == JFileChooser.APPROVE_OPTION) {
        		File file = fileChooser.getSelectedFile();
        		
        		try{
        			List<String> lines = Files.readAllLines(Paths.get(file.getPath()));
        			eventPredicateTextArea.setText(lines.get(0));
            		eventTextArea.setText(lines.get(1));
            		//TODO once we have system for options, will need to 
            		//load them in here.
        		} catch(Exception e){
        			System.out.println("Error in loading file: " + file);
        		}
   
            } 
        	
        	});
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
	
	
	private LinkedHashMap<String, String> createBlankOption(){
		LinkedHashMap<String, String> option = new LinkedHashMap<String, String>();
		option.put("Predicate", "Enter your Predicate Here");
		option.put("Text", "Enter your option text here");
		option.put("Consequences", "Enter your consequences here");
		return(option);
	}
	
	private void colorCodedValidation(JLabel theLabel, ValidationStatus status){
		//red, green, yellow for the three status values:
		switch(status){
			case valid:
				theLabel.setText("Valid");
				theLabel.setForeground(Color.GREEN);
				break;
			case invalid:
				theLabel.setText("Invalid");
				theLabel.setForeground(Color.RED);
				break;
			case unchecked:
				theLabel.setText("Unchecked");
				theLabel.setForeground(Color.YELLOW);
				break;
			default:
				theLabel.setText("ERROR");
				theLabel.setForeground(Color.RED);
				break;
		}
	}
	
	
	
	private void displayOptionsList(){
		//call this whenever the list of options gets changed, and needs
		//to be redisplayed.
		eventOptionsPanel.removeAll();
		
		int n = optionsList.size();
		for(int j=0; j < n; j++){
			String tempText = optionsList.get(j).get("Text");
			tempText = tempText.substring(0, 
					Math.min(20, tempText.length())) + "...";
			eventOptionsPanel.add(
					new JLabel((j+1) + ". " + tempText), "cell 0 " + (j+1));
			JLabel currentOptionValidatedLabel = new JLabel();
			colorCodedValidation(currentOptionValidatedLabel, checkOption(j));
			eventOptionsPanel.add(currentOptionValidatedLabel, "cell 1 " + (j+1));
			
			JButton delete = new JButton("delete");
			delete.setMnemonic(j);
			delete.addActionListener((click) -> {
				int m = delete.getMnemonic();
				optionsList.remove(m);
				optionsPredicateValidated.remove(m);
				optionsTextValidated.remove(m);
				optionsConsequencesValidated.remove(m);
				if(currentOption >= optionsList.size()){
					currentOption = optionsList.size()-1;
					storeCurrentOption();
					displayOptionData(currentOption);
				}
				displayOptionsList();
			});
			if(n <= 1){ //don't let them delete last option
				delete.setEnabled(false);
			}
			eventOptionsPanel.add(delete, "cell 2 " + (j+1));	
			
			JButton select = new JButton("select");
			select.setMnemonic(j);
			select.addActionListener((click) ->{
				storeCurrentOption();
				displayOptionData(select.getMnemonic());
				displayOptionsList();				
			});
			if(j == currentOption){ //this is already selected
				select.setEnabled(false);
			}
			eventOptionsPanel.add(select, "cell 3 " + (j+1));	
			
		}
		
		
		//button for creating a new option
		JButton addOption = new JButton("Add Option");
		addOption.addActionListener((click) -> {
			optionsList.add(createBlankOption());
			optionsValidated = ValidationStatus.invalid;
			optionsPredicateValidated.add(ValidationStatus.invalid);
			optionsTextValidated.add(ValidationStatus.invalid);
			optionsConsequencesValidated.add(ValidationStatus.invalid);
			eventOptionsPanel.removeAll();
			displayOptionsList();
		});
		eventOptionsPanel.add(addOption, "cell 2 " + (n+2));
			
		JButton eventOptionsValidationButton = new JButton("Validate");
		JLabel optionsValidationLabel = new JLabel();
	    colorCodedValidation(optionsValidationLabel, optionsValidated);			
		eventOptionsValidationButton.addActionListener((event)->{
				//TODO currently just a stub that does no checking.  Need to
				//make this actually validate the values
			optionsValidated = ValidationStatus.valid;
			colorCodedValidation(optionsValidationLabel, optionsValidated);
		});
		eventOptionsPanel.add(eventOptionsValidationButton, "cell 3 " + (n+2));
		eventOptionsPanel.add(optionsValidationLabel, "cell 4 " + (n+2));
		eventOptionsPanel.revalidate();
	}
	
	
	
	private ValidationStatus checkOption(int index){
		//this check the three fields of an option, and returns the 
		//proper validation status
		ArrayList<ValidationStatus> tempList = new ArrayList<ValidationStatus>();
		tempList.add((optionsPredicateValidated).get(index));
		tempList.add((optionsTextValidated).get(index));
		tempList.add((optionsConsequencesValidated).get(index));
		int numValid = tempList.stream()
								.filter(o -> o == ValidationStatus.valid)
								.mapToInt(o -> 1)
								.sum();
		int numInvalid = tempList.stream()
				.filter(o -> o == ValidationStatus.invalid)
				.mapToInt(o -> 1)
				.sum();
		if(numInvalid > 0){
			return (ValidationStatus.invalid);
		} 
		if(numValid == 3){
			return (ValidationStatus.valid);
		} else {
			return (ValidationStatus.unchecked);
		}
	}
	
	private void storeCurrentOption(){
		//method to ptu the text in the options panels into the
		//actual data objects
		int j = currentOption;
		
		LinkedHashMap<String, String> tempMap = createBlankOption();
		tempMap.put("Predicate", ((JTextArea)(optionPredicatePanel.getComponent(1))).getText());
		tempMap.put("Text", ((JTextArea)(optionTextPanel.getComponent(1))).getText());
		tempMap.put("Consequences", ((JTextArea)(optionConsequencePanel.getComponent(1))).getText());
		
		optionsList.set(j, tempMap);
	}
	
}
