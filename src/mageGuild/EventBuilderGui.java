package mageGuild;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
//import java.io.FileFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

public class EventBuilderGui extends JFrame {
	
	static final long serialVersionUID = 1L;
	
	private enum ValidationStatus {
		valid, invalid, unchecked
	}
	
	private JPanel eventInitPanel;
	private JPanel eventPredicatePanel;
	private JPanel eventTextPanel;
	private JTextArea eventInitTextArea;
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
	
	private final JFileChooser fileChooser; // = new JFileChooser();
	
	private ValidationStatus eventInitValidated;
	private ValidationStatus eventTextValidated;
	private ValidationStatus eventPredicateValidated;
	private ValidationStatus optionsValidated;
	private ArrayList<ValidationStatus> optionsPredicateValidated;
	private ArrayList<ValidationStatus> optionsTextValidated;
	private ArrayList<ValidationStatus> optionsConsequencesValidated;
	private int currentOption;
	
	private TextParser textParser;
	
	//constructor
	public EventBuilderGui(){
		setLayout(new MigLayout());
		setTitle("Event Builder");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        textParser = new TextParser();
        
        //create the file chooser for saving and loading
        fileChooser = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter(".wev, wiz guild event","wev");
    	fileChooser.setFileFilter(filter);
    	
    	//set initial values
        eventPredicateText = "Enter event Predicate here";
        eventText = "Enter event text here";
        optionsList = new ArrayList<LinkedHashMap<String, String>>();
        optionsList.add(createBlankOption());
        eventInitValidated = ValidationStatus.invalid;
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
    	
        

        //Event Init section: -----------------------------------------------
        eventInitPanel = new JPanel();
        eventInitPanel.setLayout(new MigLayout());
        eventInitPanel.setBorder(BorderFactory.createTitledBorder("Initialize Event"));
		eventInitTextArea = new JTextArea("Enter event Initial flags here", 10, 40);
		eventInitTextArea.setLineWrap(true);
		eventInitTextArea.setWrapStyleWord(true);
		JScrollPane eventInitScrollPane = new JScrollPane(eventInitTextArea);
		eventInitPanel.add(eventInitScrollPane, "cell 0 1, span 1 2");
		JButton eventInitValidationButton = new JButton("Validate");
		JLabel eventInitValidationLabel = new JLabel();
		colorCodedValidation(eventInitValidationLabel, eventInitValidated);			
		eventInitValidationButton.addActionListener((event)->{
			//TODO currently just a stub that does no checking.  Need to
			//make this actually validate the values
			eventInitValidated = ValidationStatus.valid;
			colorCodedValidation(eventInitValidationLabel, eventInitValidated);
		});
		eventInitPanel.add(eventInitValidationButton, "cell 1 2");
		eventInitPanel.add(eventInitValidationLabel, "cell 2 2");
		eventInitPanel.setVisible(true);	
        this.add(eventInitPanel, "cell 1 0");
    	
        
        
        
        
        //Event Predicate section: ---------------------------------------------
        eventPredicatePanel = new JPanel();
        eventPredicatePanel.setLayout(new MigLayout());
        eventPredicatePanel.setBorder(BorderFactory.createTitledBorder("Event Predicate"));
		eventPredicateTextArea = new JTextArea("Enter event Predicate here", 10, 40);
		eventPredicateTextArea.setLineWrap(true);
		eventPredicateTextArea.setWrapStyleWord(true);
		JScrollPane eventPredicateScrollPane = new JScrollPane(eventPredicateTextArea);
		eventPredicatePanel.add(eventPredicateScrollPane, "cell 0 1, span 1 2");
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
    	
        
        
        //event Text Section -----------------------------------------------
        eventTextPanel = new JPanel();
        eventTextPanel.setLayout(new MigLayout());
        eventTextPanel.setBorder(BorderFactory.createTitledBorder("Event Text"));
		eventTextArea = new JTextArea("Add Event Text Here.", 10, 40);
		eventTextArea.setLineWrap(true);
		eventTextArea.setWrapStyleWord(true);
		JScrollPane eventTextScrollPane = new JScrollPane(eventTextArea);
		eventTextPanel.add(eventTextScrollPane, "cell 0 0, span 1 4");
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
				Function<GameState, String> textValidationTest = textParser.parseFullText(eventTextArea.getText());
				String testString = textValidationTest.apply(new GameState( RulesSet.sampleCharacter()));
				eventTextValidated = ValidationStatus.valid;
				
				/* some testing
				GameState testState = new GameState( RulesSet.sampleCharacter());
				testState.addFlag("flag1", "testing flag1");
				testState.getPlayerCharacter().addFlag("PCflag", "PCflag is set");
				Character tempChar = RulesSet.sampleCharacter();
				tempChar.setName("Robert");
				testState.addNPC("bob", tempChar);
				testString = textValidationTest.apply(testState);
				System.out.println(testString);
				*/
			} catch (UnsupportedOperationException e) {
				eventTextValidated = ValidationStatus.invalid;
				JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
			}
			
			colorCodedValidation(eventTextValidationLabel, eventTextValidated);
		});
		eventTextPanel.add(eventTextValidationButton, "cell 1 3");
		eventTextPanel.add(eventTextValidationLabel, "cell 2 3");
		eventTextPanel.setVisible(true);	
    	this.add(eventTextPanel, "cell 1 2, span 1 2");
        
    	//event text code word options
    	JComboBox<String> eventTextCodeWordsComboBox = new JComboBox<String>();
    	textParser.getCodeWords().getMap().forEach((name, word) -> {
    		eventTextCodeWordsComboBox.addItem(name);
    		//eventTextCodeWordsComboBox.addItem(word.getHoverMsg());
    	});
    	eventTextCodeWordsComboBox.setSelectedIndex(0);
    	eventTextPanel.add(eventTextCodeWordsComboBox, "cell 1 0, span 2 1");
    	//insert word into text when selected
    	eventTextCodeWordsComboBox.addActionListener((e) ->{
    		eventTextArea.insert((String)(eventTextCodeWordsComboBox.getSelectedItem()) + " ", 
					eventTextArea.getCaretPosition());
			eventTextArea.requestFocus();
    	});
    	//alternative option is using a button
    	//decided this was unnecessarily clunky
    	/*
    	JButton insertWordButton = new JButton("Insert");
    	eventTextPanel.add(insertWordButton, "cell 1 1");
    	insertWordButton.addActionListener((event)->{
			eventTextArea.insert((String)(eventTextCodeWordsComboBox.getSelectedItem()) + " ", 
					eventTextArea.getCaretPosition());
			eventTextArea.requestFocus();
		});
		*/
    	
    	//and a toggle code button:
    	JButton insertCodeButton = new JButton("toggle code");
    	eventTextPanel.add(insertCodeButton, "cell 1 1"); //was 2 1 when button was in
    	insertCodeButton.addActionListener((event)->{
			eventTextArea.insert("££", eventTextArea.getCaretPosition());
			eventTextArea.requestFocus();
		});
    	
    	
    	//Event options section ------------------------------------------------
        eventOptionsPanel = new JPanel();
        eventOptionsPanel.setLayout(new MigLayout());
        JScrollPane optionsScrollPane = new JScrollPane(eventOptionsPanel,
        		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        		 JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //eventOptionsPanel.setBorder(BorderFactory.createTitledBorder("Event Options"));
        optionsScrollPane.setBorder(BorderFactory.createTitledBorder("Event Options"));
        displayOptionsList();
		eventOptionsPanel.setVisible(true);	
    	//this.add(eventOptionsPanel, "cell 1 3");
    	
		this.add(optionsScrollPane, "cell 2 0");
    	
    	//current option predicate section --------------------------------------
        optionPredicatePanel = new JPanel();
        optionPredicatePanel.setLayout(new MigLayout());
        optionPredicatePanel.setBorder(BorderFactory.createTitledBorder("Option Predicate"));
		optionPredicatePanel.setVisible(true);	
    	this.add(optionPredicatePanel, "cell 2 1");
    	
    	//options text section ---------------------------------------------------
        optionTextPanel = new JPanel();
        optionTextPanel = new JPanel();
        optionTextPanel.setLayout(new MigLayout());
        optionTextPanel.setBorder(BorderFactory.createTitledBorder("Option Text"));
		optionTextPanel.setVisible(true);	
    	this.add(optionTextPanel, "cell 2 2");
        
    	
    	//consequences panel -----------------------------------------------------
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
		
	}
	//----------------------------------------------------------------------
	//end of constructor
	
	
	private void displayOptionData(int j){
		//first set the current option to the one about to be displayed
		this.currentOption = j;
		//predicate panel first:
		optionPredicatePanel.removeAll();
		optionPredicatePanel.add(new JLabel("Option " +(currentOption+1) + " Predicate"), "cell 0 0");
		JTextArea optionPredicateTextArea = new JTextArea(optionsList.get(j).get("Predicate"), 5, 40);
		optionPredicateTextArea.setLineWrap(true);
		optionPredicateTextArea.setWrapStyleWord(true);
		JScrollPane optionPredicateScrollPane = new JScrollPane(optionPredicateTextArea);
		//optionPredicatePanel.add(optionPredicateTextArea, "cell 0 1");		
		optionPredicatePanel.add(optionPredicateScrollPane, "cell 0 1, span 1 2");	
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
		JTextArea optionTextArea = new JTextArea(optionsList.get(j).get("Text"), 10, 40);
		optionTextArea.setLineWrap(true);
		optionTextArea.setWrapStyleWord(true);
		JScrollPane optionTextScrollPane = new JScrollPane(optionTextArea);
		optionTextPanel.add(optionTextScrollPane, "cell 0 1, span 1 2");	
		//optionTextPanel.add(optionTextArea, "cell 0 1");	
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
    	JTextArea consequencesTextArea = new JTextArea(optionsList.get(j).get("Consequences"), 10, 40);
    	consequencesTextArea.setLineWrap(true);
    	consequencesTextArea.setWrapStyleWord(true);
    	JScrollPane consequencesScrollPane = new JScrollPane(consequencesTextArea);
    	//optionConsequencePanel.add(consequencesTextArea, "cell 0 1");
    	optionConsequencePanel.add(consequencesScrollPane, "cell 0 1, span 1 2");
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
		
		//TODO 
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
        		tempArray.add(eventInitTextArea.getText());
        		tempArray.add(eventPredicateTextArea.getText());
        		tempArray.add(eventTextArea.getText());
        		//add the number of options
        		int numOptions = this.optionsList.size();			
        		tempArray.add(Integer.toString(numOptions));
        		
        		
        		try{
        			boolean reallySave = true;
        			
        			if(!file.getName().endsWith(".wev")){
        				//need to make sure it has the correct extension 
    					file = new File(file.getPath() + ".wev");
    				}
        			if(file.exists()){ //if file exists, need to make really sure they mean to overwrite
        				int response = JOptionPane.showConfirmDialog(null, 
        						"Are you sure you want to overwrite existing file?", "Confirm",
        					    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        				if (response == JOptionPane.NO_OPTION |
        					response == JOptionPane.CLOSED_OPTION	) {
        					reallySave = false;
        				}
        			} 
        				
        			if(reallySave){
        				Path p = Paths.get(file.getPath());
        				try(OutputStream out = new BufferedOutputStream(
          				    Files.newOutputStream(p, 
          				    		java.nio.file.StandardOpenOption.CREATE, 
          				    		java.nio.file.StandardOpenOption.TRUNCATE_EXISTING))) {
        					byte data[];
        					
        					//event init
        					data = ("<eventInit>"+System.lineSeparator()).getBytes();
        					out.write(data, 0, data.length);
        					data = (tempArray.get(0)+System.lineSeparator()).getBytes();
        					out.write(data, 0, data.length);
        					data = ("</eventInit>"+System.lineSeparator()).getBytes();
        					out.write(data, 0, data.length);
        					
        					
        					//event predicate 
        					data = ("<eventPredicate>"+System.lineSeparator()).getBytes();
        					out.write(data, 0, data.length);
        					data = (tempArray.get(1)+System.lineSeparator()).getBytes();
        					out.write(data, 0, data.length);
        					data = ("</eventPredicate>"+System.lineSeparator()).getBytes();
        					out.write(data, 0, data.length);
        					
        					//event text
        					data = ("<eventText>"+System.lineSeparator()).getBytes();
        					out.write(data, 0, data.length);
        					data = (tempArray.get(2)+System.lineSeparator()).getBytes();
        					out.write(data, 0, data.length);
        					data = ("</eventText>"+System.lineSeparator()).getBytes();
        					out.write(data, 0, data.length);	
        					
        					//the number of options:
        					data = ("<numberOfOptions>"+System.lineSeparator()).getBytes();
        					out.write(data, 0, data.length);
        					data = (tempArray.get(3)+System.lineSeparator()).getBytes();
        					out.write(data, 0, data.length);
        					data = ("</numberOfOptions>"+System.lineSeparator()).getBytes();
        					out.write(data, 0, data.length);
        					
        					//info for each option:
        					storeCurrentOption();
        					for(int i=0; i < this.optionsList.size(); i++){
            					//predicate
            					data = ("<Predicate " + i +">"+System.lineSeparator()).getBytes();
            					out.write(data, 0, data.length);
            					data = (this.optionsList.get(i).get("Predicate")+System.lineSeparator()).getBytes();
            					out.write(data, 0, data.length);
            					data = ("</Predicate " + i +">"+System.lineSeparator()).getBytes();
            					out.write(data, 0, data.length);
            					//text
            					data = ("<Text " + i +">"+System.lineSeparator()).getBytes();
            					out.write(data, 0, data.length);
            					data = (this.optionsList.get(i).get("Text")+System.lineSeparator()).getBytes();
            					out.write(data, 0, data.length);
            					data = ("</Text " + i +">"+System.lineSeparator()).getBytes();
            					out.write(data, 0, data.length);
            					//consequences
            					data = ("<Consequences " + i +">"+System.lineSeparator()).getBytes();
            					out.write(data, 0, data.length);
            					data = (this.optionsList.get(i).get("Consequences")+System.lineSeparator()).getBytes();
            					out.write(data, 0, data.length);
            					data = ("</Consequences " + i +">"+System.lineSeparator()).getBytes();
            					out.write(data, 0, data.length);
            					
        					}
     
        	        		
        					
    				    } catch (IOException x) {
    				      System.err.println(x);
    				    }			    
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
        			int start, end;
            		
        			//read in event Init Text Area
            		start = lines.indexOf("<eventInit>")+1;
            		end = lines.indexOf("</eventInit>");
            		if(start != end){
            			eventInitTextArea.setText(lines.get(start)+System.lineSeparator());
            			for(int i=(start+1); i < end; i++){
            				eventInitTextArea.setText(
            						eventInitTextArea.getText() 
            						+ lines.get(i)
            						+ System.lineSeparator()
            						);
            			}
            		}
        			
            		//read in event Predicate Text Area
            		start = lines.indexOf("<eventPredicate>")+1;
            		end = lines.indexOf("</eventPredicate>");
            		if(start != end){
            			eventPredicateTextArea.setText(lines.get(start)+System.lineSeparator());
            			for(int i=(start+1); i < end; i++){
            				eventPredicateTextArea.setText(
            						eventPredicateTextArea.getText() 
            						+ lines.get(i)
            						+ System.lineSeparator()
            						);
            			}
            		}
            		
            		
            		//read in event text Area
            		start = lines.indexOf("<eventText>")+1;
            		end = lines.indexOf("</eventText>");
            		if(start != end){
            			eventTextArea.setText(lines.get(start)+System.lineSeparator());
            			for(int i=(start+1); i < end; i++){
            				eventTextArea.setText(
            						eventTextArea.getText() 
            						+ lines.get(i)
            						+ System.lineSeparator()
            						);
            			}
            		}
            	
            		//read in the options
            		start = lines.indexOf("<numberOfOptions>")+1;
            		end = lines.indexOf("</numberOfOptions>");
            		this.optionsList.clear(); //clear any existing options before loading
            		optionsPredicateValidated.clear();
            		optionsTextValidated.clear();
            		optionsConsequencesValidated.clear();
            		
            		if(start != end){
            			int numOptions = Integer.parseInt(lines.get(start));
            			for(int i=0; i < numOptions; i++){
            				this.optionsList.add(i, createBlankOption());
            				start = lines.indexOf("<Predicate " + i +">")+1;
                    		end = lines.indexOf("</Predicate " + i +">");
                    		String tempString = "";
                    		if(start != end){
                    			for(int j=(start); j < end; j++){
                    				tempString = tempString  
                    						+ lines.get(j)
                    						+ System.lineSeparator();
                    			}
                    			this.optionsList.get(i).put("Predicate", tempString);
                    			optionsPredicateValidated.add(i, ValidationStatus.unchecked);
                    		}
                    		start = lines.indexOf("<Text " + i +">")+1;
                    		end = lines.indexOf("</Text " + i +">");
                    		tempString = "";
                    		if(start != end){
                    			for(int j=(start); j < end; j++){
                    				tempString = tempString  
                    						+ lines.get(j)
                    						+ System.lineSeparator();
                    			} 
                    			this.optionsList.get(i).put("Text", tempString);
                    			optionsTextValidated.add(i, ValidationStatus.unchecked);
                    		}
                    		start = lines.indexOf("<Consequences " + i +">")+1;
                    		end = lines.indexOf("</Consequences " + i +">");
                    		tempString = "";
                    		if(start != end){
                    			for(int j=(start); j < end; j++){
                    				tempString = tempString  
                    						+ lines.get(j)
                    						+ System.lineSeparator();
                    			}
                    			this.optionsList.get(i).put("Consequences", tempString);
                    			optionsConsequencesValidated.add(i, ValidationStatus.unchecked);
                    		}
            			}
            		}
            		
        		} catch(Exception e){
        			System.out.println("Error in loading file: " + file);
        			System.err.println(e.getMessage());
        		}
        		
        		displayOptionData(0);
        		displayOptionsList();
            } 
        	
        });
        menu.add(menuItem);
        
        
        //menu item for clearing out existing data and starting fresh
        menuItem = new JMenuItem("new");
        menuItem.addActionListener((event)->{
        	
        	int response = JOptionPane.showConfirmDialog(null, 
					"Create new empty event? You will lose any unsaved data.", "Confirm",
				    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (response == JOptionPane.NO_OPTION |
				response == JOptionPane.CLOSED_OPTION	) {
				//do nothing
			} else {
        	
				eventInitTextArea.setText("");
				eventPredicateTextArea.setText("");
				eventPredicateValidated = ValidationStatus.unchecked;
				eventTextArea.setText("");
				eventTextValidated = ValidationStatus.unchecked;
        	
				optionsList = new ArrayList<LinkedHashMap<String, String>>();
				optionsList.add(createBlankOption());
				optionsValidated = ValidationStatus.invalid;
				optionsPredicateValidated = new ArrayList<ValidationStatus>();
				optionsPredicateValidated.add(ValidationStatus.invalid);
				optionsTextValidated = new ArrayList<ValidationStatus>();
				optionsTextValidated.add(ValidationStatus.invalid);
				optionsConsequencesValidated = new ArrayList<ValidationStatus>();
				optionsConsequencesValidated.add(ValidationStatus.invalid);
				currentOption = 0;
				displayOptionData(0);
				displayOptionsList();
			}
        });
        menu.add(menuItem);
        
        
        
        //next menu (these are just non-functional place-holders for now)
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
	
	
	//method to produce a blank option 
	private LinkedHashMap<String, String> createBlankOption(){
		LinkedHashMap<String, String> option = new LinkedHashMap<String, String>();
		option.put("Predicate", "Enter your Predicate Here");
		option.put("Text", "Enter your option text here");
		option.put("Consequences", "Enter your consequences here");
		return(option);
	}
	
	//method of color-coding validation status
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
				int response = JOptionPane.showConfirmDialog(null, 
						"Really delete option " + (m+1) + "?", "Confirm",
					    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.NO_OPTION |
					response == JOptionPane.CLOSED_OPTION	) {
					//do nothing
				} else {
					optionsList.remove(m);
					optionsPredicateValidated.remove(m);
					//need to remove/reset the text fields here as well
					//(should be removed from teh options list entirely...?)
					
					
					optionsTextValidated.remove(m);
					optionsConsequencesValidated.remove(m);
					if(m == currentOption){
						currentOption=0;
						displayOptionData(currentOption);
					} else if(currentOption > optionsList.size()){
						currentOption = optionsList.size()-1; //element is 0 for size 1
						storeCurrentOption();
						displayOptionData(currentOption);
					}
					displayOptionsList();
				}
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
		//method to put the text in the options panels into the
		//actual data objects
		int j = currentOption; 
		
		LinkedHashMap<String, String> tempMap = createBlankOption();
		JScrollPane temp = (JScrollPane)(optionPredicatePanel.getComponent(1));
		tempMap.put("Predicate", ((JTextArea)(temp.getViewport().getView())).getText());
		temp = (JScrollPane)(optionTextPanel.getComponent(1));
		tempMap.put("Text", ((JTextArea)(temp.getViewport().getView())).getText());
		temp = (JScrollPane)(optionConsequencePanel.getComponent(1));
		tempMap.put("Consequences", ((JTextArea)(temp.getViewport().getView())).getText());
		//NOTE--using temp above b/c was having trouble with the casting when 
		//i tried to do it on one line.
		
		
		optionsList.set(j, tempMap);
	}
	
}
