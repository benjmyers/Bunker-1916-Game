package edu.american;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/** The menu which allows the user to pick their players. 
 * @author myers*/
public class Menu extends JPanel implements Painter, MouseListener {
	
	/** Serial ID. */
	private static final long serialVersionUID = 1L;
	/** The menu frame. */
	private JFrame menuFrame;
	/** The background image. */
	private BufferedImage image;
	/** Player 1 (user controlled sprite).*/
	public String player1 = "";
	/** Player 2 (NPC).*/
	public String player2 = "";
	/** Keeps track of how many buttons were clicked. */
	private int clickCount = 0;
	/** Array that holds the players. */
	private String[] players = {"",""};
	/** Rifle shots audio clip. */
	Clip rifle;
	/** Audio input. */
	AudioInputStream audioIn;
	
	/** Menu constructor. */
	public Menu(){
		// Make the menu frame
		menuFrame = new JFrame("BUNKER 1916");
		menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
        // Create the mainPanel
        Container mainPanel = menuFrame.getContentPane();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        

		
		// Load the background image
		try {
			//System.out.println("load image");
			image = ImageIO.read(new File("images/menuWindow.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Create the rifle clip.
		File soundFile = new File("media/rifleCocking.wav");
		try {audioIn = AudioSystem.getAudioInputStream(soundFile);} 
			catch (UnsupportedAudioFileException e1) {e1.printStackTrace();} 
			catch (IOException e1) {e1.printStackTrace();}
		try {rifle = AudioSystem.getClip();} 
			catch (LineUnavailableException e1) {e1.printStackTrace();}
		try {rifle.open(audioIn);} 
			catch (LineUnavailableException e) {e.printStackTrace();} 
			catch (IOException e) {e.printStackTrace();}
		
		// Add ourselves to the screen.
		mainPanel.add(this);

        // Add mouse listener
		menuFrame.addMouseListener(this);

		// Add menu frame attributes.
		menuFrame.setSize(image.getWidth(),image.getHeight()+20);
        menuFrame.setResizable(false);
		menuFrame.setVisible(true);
		
		
	}
	
	/** Returns the number of clicks. */
	public int getClickCount(){
		return clickCount;
	}
	
	/** Returns player1 (sprite). */
	public String getPlayer1(){
		return player1;
	}
	
	/** Returns player2 (NPC). */
	public String getPlayer2(){
		return player2;
	}

	/** Close the frame. */
	public void dispose() {
		clickCount = 0;
		
		menuFrame.dispose();
	}
	
	/** Paints the graphics. */
	@Override
	public void paint(Graphics g) {
		// Draw the background
		if (image != null) {;
	        g.drawImage(image, 0, 0, null);
		}

		
	}
	


	/** Handles mouse clicks. */
	@Override
	public void mouseClicked(MouseEvent e) {
		// Click location
		int x = e.getX();
		int y = e.getY();
		
		// Allies column
		if(x > 126 && x < 389){
			// Great Britian
			if(y > 422 && y < 456){
				players[clickCount]=("images/UK.png");
				rifle.start();
				clickCount++;

			}
			// France
			else if(y > 476 && y < 509){
				players[clickCount]=("images/FR.png");
				rifle.start();
				clickCount++;
			}
			// Russia
			else if(y > 535 && y < 573){
				players[clickCount]=("images/RUS.png");
				rifle.start();
				clickCount++;
			}
			
		}
		// Central column
		else if(x > 544 && x < 818){
			// Germany
			if(y > 422 && y < 456){
				rifle.start();
				players[clickCount]=("images/GE.png");
				clickCount++;
			}
			// AH 
			else if(y > 476 && y < 517){
				rifle.start();
				players[clickCount]=("images/AH.png");
				clickCount++;
			}
			// Turks
			else if(y > 535 && y < 577){
				rifle.start();
				players[clickCount]=("images/TURK.png");
				clickCount++;
			}

		}
		else{
			// DO nothing
		}
		
		// If the user has picked two players, assign them.
		if(clickCount == 2){
			player1 = players[0];
			player2 = players[1];
			//System.out.println(player1);
			//System.out.println(player2);
		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}
