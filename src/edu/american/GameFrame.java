package edu.american;
/* BUNKER 1916
  Copyright 2013 Meredith Myers

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/** The game play window on which the background, player, and npcs are drawn.
 * @author fleck, myers
 */
public class GameFrame extends JPanel implements Painter {
	
	
	/**
	 * ID used when serializing this object (basically an object ID)
	 */
	private static final long serialVersionUID = 2423423423L;

	/** Width of the screen we are using. */
	public static final int SCREEN_WIDTH = 900;
	
	/** Height of the screen we are using. */
	public static final int SCREEN_HEIGHT = 700;
	
	/** How far to move each step. */
	public final static int STEP_SIZE = 25; 
	
	/** How many grid squares we have (wide) (50) . */
	public final static int GRID_WIDTH = SCREEN_WIDTH/STEP_SIZE; 
	
	/** How many grid squares we have (tall)(35).  */
	public final static int GRID_HEIGHT = SCREEN_HEIGHT/STEP_SIZE; 
	
	/** Stores a matrix showing where the tunnels can be.
	 *  These are essentially valid player locations.
	 *   */
	private byte [][] matrix;
	
	/** The background image. */
	private BufferedImage image = null;
	
	/** The frame. */
	private JFrame theFrame ;
	
	/** Everything that shows up on the screen, must be a Painter. 
	 *  This ArrayList holds all the Painters.
	 *  */
	private ArrayList<Painter> painters = new ArrayList<Painter>();
	
	/** Holds the tunnels. Basically, a parsed map. */
	private ArrayList<Rectangle> tunnels = new ArrayList<Rectangle>();

	
	/** Constructor. Take a KeyListener that wants to know about keys pressed in the frame. */
	public GameFrame(KeyListener kl) {
		super();	
		// Set up the frame
		theFrame = new JFrame("BUNKER 1916");
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Add the keylistener to the frame
		theFrame.addKeyListener(kl);
		this.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);

		// Add ourselves to the screen.
		theFrame.getContentPane().add(this);
		this.setDoubleBuffered(true);
		
		// Set frame attributes
		theFrame.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        theFrame.setResizable(false);
		theFrame.setVisible(true);
	}
	
	/** Set the background image for the game. */
	public void setBackground(String imageFile) {
		try {
			image = ImageIO.read(new File(imageFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Set the map showing where the tunnels are. 
	 * @throws IOException */
	public void setMap(String mapFile) throws IOException {
		//System.out.println(mapFile);
		BufferedReader in = new BufferedReader(new FileReader(mapFile));
		String line = in.readLine();
		int lineNum = 0;
		matrix = new byte[GRID_WIDTH+1][];
		while (line != null) {
			byte [] bytes = line.getBytes();
			matrix[lineNum] = bytes;
			lineNum++;
			line = in.readLine();
			//System.out.println(l);
		}
		
		// Build map rectangles
		getTunnels().clear();
		for (int row=0; row < GRID_HEIGHT; row++) {
			for (int col=0; col< GRID_WIDTH; col++) {
				if (matrix[row][col] == '.') {
					// Convert from Grid coordinates to screen pixels
					int x = col*GameFrame.STEP_SIZE;
					int y = row*GameFrame.STEP_SIZE;
					
					Rectangle r = new Rectangle(x,y,33,33);					
					getTunnels().add(r);
				}
			}
		}
	}
	
	/** Can the player move onto this location?
	 *  
	 * @param x pixel location of rectangle
	 * @param y pixel location of rectangle
	 * @return true if the square is a tunnel.
	 */
	public synchronized boolean canMove(int x, int y) {
		int gridLocX = x / GameFrame.STEP_SIZE;
		int gridLocY = y / GameFrame.STEP_SIZE;
		//System.out.println(x + " ---> " + gridLocX +"  "+ y + " ---> " + gridLocY + "   "+ matrix[gridLocY][gridLocX]);

		// Set the rectangle to covered
		if ((x >= 0) && (x < SCREEN_WIDTH) && (y < SCREEN_HEIGHT)){
			return matrix[gridLocY][gridLocX] != 'x';
		}
		else{
			return false;
		}
	}
	
	
	/** Add to the list of things to paint. */
	public synchronized void addPainter(Painter p) {
		painters.add(p);
	}
	
	/** Clears all the painters so the game can restart.*/
	public synchronized void removePainters(){
		painters.clear();
	}

	/** Close this frame. */
	public void dispose() {
		theFrame.dispose();
	}
	
	/** Override the paint method to do custom painting. */
	public synchronized void paint(Graphics g) {
		
		// Draw the background
		if (image != null) {
			int x = (getWidth() - image.getWidth())/2;
	        int y = (getHeight() - image.getHeight())/2;
	        g.drawImage(image, x, y, this);
		}
		
		// Paint any other painters
		for (Painter p : painters) {
			//System.out.println(p);
			p.paint(g);
		}
		
	}

	/** Setter for the tunnels.*/
	public synchronized void setTunnels(ArrayList<Rectangle> tunnels) {
		this.tunnels = tunnels;
	}
	/** Getter for the tunnels.*/
	public synchronized ArrayList<Rectangle> getTunnels() {
		return tunnels;
	}

}
