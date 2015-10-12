package mageGuild;

//import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
		//size.width = 100;
		//setPreferredSize(size);
		
		setBorder(BorderFactory.createTitledBorder(playerCharacter.getName()));
		
		
		try {
			characterPicture = ImageIO.read(playerCharacter.getCharacterImage());
			JLabel picLabel = new JLabel(new ImageIcon(characterPicture));
			this.add(picLabel, "wrap");
		} catch (Exception e) {
			System.out.print("Error: with picture");
		} finally {	
		}
		
		
		JLabel characterName = new JLabel(playerCharacter.getName());
		this.add(characterName, "align center, wrap 30");
		
		StatLine playerStats = playerCharacter.getStats();
		
		//loop over stats and display them (with no decimals)
		playerStats.forEach((stat, value) -> {	
		    this.add(new JLabel(stat));
		    this.add(new JLabel( new DecimalFormat("#").format(value)), "wrap");
		});
		
		
	}
	
}
