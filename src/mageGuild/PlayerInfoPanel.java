package mageGuild;

import java.awt.Color;
import java.awt.Dimension;
//import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import net.miginfocom.swing.MigLayout;


public class PlayerInfoPanel extends JPanel {
	
	static final long serialVersionUID = 1L;
	
	Character playerCharacter;
	BufferedImage characterPicture;
	Gui fullGui;
	
	public PlayerInfoPanel(Character thePlayer, Gui theGui){
		playerCharacter = thePlayer;
		fullGui = theGui;
		
		setLayout(new MigLayout());
		
		//Dimension size = getPreferredSize();
		//size.setSize(200, 650);
		//size.width = 200;
		//setPreferredSize(size);
		
		setBorder(BorderFactory.createTitledBorder(playerCharacter.getName()));
		
		
		try {
			characterPicture = ImageIO.read(playerCharacter.getCharacterImage());
			JLabel picLabel = new JLabel(new ImageIcon(characterPicture));
			this.add(picLabel, "align center, wrap");
		} catch (Exception e) {
			System.out.print("Error: with picture");
		} finally {	
		}
		
		
		JLabel characterName = new JLabel(playerCharacter.getName());
		this.add(characterName, "align center, wrap 30");
		
		//create a bold font, and use it for the character name
		//Font font = characterName.getFont();
		// same font but bold
		//Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
		//characterName.setFont(boldFont);
		
		
		StatLine playerStats = playerCharacter.getStats();
		
		JPanel skillPanel = new JPanel();
		JScrollPane scrollPane = new JScrollPane(skillPanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(scrollPane);
		skillPanel.setLayout(new MigLayout());
		
		
		//loop over stats and display them (with no decimals)
		skillPanel.add(new JLabel("ATTRIBUTES:"), "wrap");
		RulesSet.getListOfAttributes().forEach((stat) -> {
			skillPanel.add(new JLabel("   " + stat));
			Float value = thePlayer.getStat(stat);
			skillPanel.add(new JLabel( "     " + new DecimalFormat("#").format(value)), "wrap");
		});
		
		skillPanel.add(new JLabel(" "), "wrap");
		skillPanel.add(new JLabel("MAGICS:"), "wrap");
		RulesSet.getListOfMagics().forEach((stat) -> {
			Float value = thePlayer.getStat(stat);
			if(value > 0){
				JLabel tempLabel = new JLabel("   " + stat );
				skillPanel.add(tempLabel);
				skillPanel.add(new JLabel( "     " + new DecimalFormat("#").format(value)), "wrap");
				if(thePlayer.getFlags().get("casts"+stat)=="true"){
					tempLabel.setForeground(Color.BLUE);
				}
			}
		});
		
		skillPanel.add(new JLabel(" "), "wrap");
		skillPanel.add(new JLabel("SKILLS:"), "wrap");
		RulesSet.getListOfSkills().forEach((stat) -> {
			Float value = thePlayer.getStat(stat);
			if(value > 0){
				skillPanel.add(new JLabel("   " + stat ));
				skillPanel.add(new JLabel( "     " + new DecimalFormat("#").format(value)), "wrap");
			}
		});
		
		
		/*
		playerStats.forEach((stat, value) -> {	
			if(value > -1){
				skillPanel.add(new JLabel(stat));
				skillPanel.add(new JLabel("     ")); //spacer for nicer formatting
				skillPanel.add(new JLabel( new DecimalFormat("#").format(value)), "wrap");
			}
		});
		*/
		//set the width of the skill panel:
		Dimension skillPanelSize = skillPanel.getPreferredSize();
		skillPanelSize.width = 150;
		skillPanel.setPreferredSize(skillPanelSize);
		
	}
	
}
