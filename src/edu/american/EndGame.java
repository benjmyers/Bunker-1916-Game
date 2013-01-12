package edu.american;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/** Constructs the end of game window which allows the user to play again or quit.
 * @author myers */
public class EndGame extends JPanel implements Painter, MouseListener {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;
	/** The end game frame. */
	public JFrame frame;
	/** The background image. */
	private BufferedImage image;
	/** The win/lose font. */
	private Font font;
	/** Whether not the user has won. */
	private boolean win = false;
	
	/** End Game window constructor. */
	public EndGame(){
		
		// Create the new frame
		frame = new JFrame("BUNKER 1916");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
        // Create the mainPanel
        Container mainPanel = frame.getContentPane();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        
        // Add mouse listener
		frame.addMouseListener(this);
		
		// Create the custom font
		font = new Font("Segoe Script", Font.BOLD, 40);
		
		// Load the background image
		try {
			image = ImageIO.read(new File("images/endGameWindow.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Add frame to the screen.
		frame.getContentPane().add(this);

		// Set frame attributes
		frame.setSize(image.getWidth(),image.getHeight()+20);
        frame.setResizable(false);
		frame.setVisible(true);
		
	}
	


	/** Paints the graphics to the frame. */
	@Override
	public void paint(Graphics g) {
		// Draw the background
		if (image != null) {;
	        g.drawImage(image, 0, 0, null);
		}	
		// If the user won, display a message
		if(win){
			g.setFont(font);
			g.setColor(Color.WHITE);
			g.drawString("Victory!",380,290);
		}
		// If the user lost, display a message
		else if(!win){
			g.setFont(font);
			g.setColor(Color.WHITE);
			g.drawString("Defeat!",380,290);
		}
	}
	
	/** Setter for win. */
	public void setWin(boolean w){
		win = w;
	}

	/** Handles mouse events.*/
	@Override
	public void mouseClicked(MouseEvent e) {
		// Mouse click location
		int x = e.getX();
		int y = e.getY();
		
		//System.out.println("x "+x+" y "+y);
		/*  x 444 y 508
			x 666 y 528
			x 758 y 509
			x 843 y 526*/
		
		// If the mouse clicked in the button area
		if(y > 508 && y < 525){
			// Play again
			if(x > 444 && x < 666){
				if(Main.song.isRunning()){
					Main.song.stop();
				}
				frame.dispose();
				Main.startGame();
			}
			// Exit
			else if(x > 758 && x < 843){
				frame.dispose();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
