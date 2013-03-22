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
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


/** Creates player (sprite) and updates their location.
 * @author fleck, myers
 */
public class Player implements Painter {

	// Constants for directions of motion of the sprite
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int UP = 2;
	public static final int DOWN = 3;
	public static final int STOPPED = 4;
	public static final int BACKUP = 5;
	/** Step size (how fast the sprite moves)*/
	private static final int STEP_SIZE = 2;
	/** A serial UID. */
	private static final long serialVersionUID = -3150167263241846626L;
	/** Top left X component. */
	protected int x;
	/** Top left Y component. */
	protected int y;
	/** Holds the sprite sheet. */
	private BufferedImage spriteSheet;
	/** Width of the grid for sprites. */
	private int gridWidth;
	/** Height of the grid for sprites. */
	private int gridHeight;
	/** Sequence of sprites for moving left. */
	private int[] leftSequence;
	/** Sequence of sprites for moving right. */
	private int[] rightSequence;
	/** Sequence of sprites for moving down. */
	private int[] downSequence;
	/** Sequence of sprites for moving up. */
	private int[] upSequence;
	/** Sequence of sprites for not moving. */
	private int[] notMovingSequence;
	/** Which sprite on the sheet are we currently showing.*/
	private int curSpriteIndex;
	/** How many columns are there on the sprite sheet? */
	private int numSpriteCols;
	/** What direction is the sprite currently moving. */
	private int direction;
	/** Current sprite index in the sequence */
	private int currentSpriteNum;
	/** Just a counter to slow down animations. */
	private int updateCounter;
	/** Holds the pipe "pieces". */
	public ArrayList<Rectangle> pipe = new ArrayList<Rectangle>();
	/** Main object. */
	private Main main;
	
	/** Constructor. */
	public Player(Main main) {
		super(); // Call superclass constructor	
		this.main = main;
		// Size of each sprite on the grid
		gridWidth = 35;
		gridHeight = 35;
		
		// Sequences of sprites
		leftSequence = new int[] {6, 7, 6, 4};
		rightSequence = new int[] {1, 3, 1, 0};
		downSequence = new int[] {17,16,17,18};
		upSequence = new int[] {8,9,8,11};
		notMovingSequence = new int[] {17,17,17,17};
		
		// Initial location of the sprite
		x=515;
		y=175;
		
		// Number of columns in sprite sheet
		numSpriteCols = 8; 
		
		// Load sprite sheet
		String fileName = main.menuFrame.getPlayer1();
		File spriteFile = new File(fileName);
		try {
			spriteSheet = ImageIO.read(spriteFile);
		} catch (IOException e) {
			System.err.println("Could not load the image file!");
			e.printStackTrace();
		}
		
		// Which direction do we start moving?
		setDirection(STOPPED);
		
		// What sprite index do we start with.
		curSpriteIndex = notMovingSequence[0];
	}
	
	/** Override paint to allow painting.*/
	public synchronized void paint(Graphics g) {
		
		// Figure out where on the sprite sheet we get
		// the current sprite to draw. This is the 
		// x,y location within the sprite sheet.
		int frameX = (curSpriteIndex % numSpriteCols) * gridWidth;
        int frameY = (curSpriteIndex / numSpriteCols) * gridHeight;
        //System.out.println("X: "+frameX+" Y: "+frameY);

        // Print each pipe
		for (Rectangle r : pipe) {
			g.setColor(Color.YELLOW);
			g.drawRect((r.x+15), (r.y+5), r.width, r.height);
		}
		
		// Paint the sprite
        g.drawImage(spriteSheet, 
				x, y-10, x+gridWidth, (y-10)+gridHeight, // Where in the Component
                frameX, frameY, frameX+gridWidth, frameY+gridHeight, null);
			}

	/** Set direction we're moving. */
	public void setPipe(ArrayList<Rectangle> pipe) {		
				
			this.pipe = pipe;	
		
	}
	
	/** Get the sprite's direction.*/
	public ArrayList<Rectangle> getPipe() {
		return pipe;
	}
	
	/** Set direction we're moving. */
	public void setDirection(int dir) {		
				
			this.direction = dir;	
		
	}
	
	/** Get the sprite's direction.*/
	public int getDirection() {
		return direction;
	}
	
	
	/** Update the location of the rectangle. */
	public void setLocation(int newX, int newY) {
		//System.out.println("X: "+x+" Y: "+y);
		x=newX;
		y=newY;		
	}
	
	/** Close this frame. */
	public void end() {
		pipe.clear();
	}
	
	/** Update the sprite by moving it in whatever direction it's going. */
	public void update() {
		// Initialize 
		int i = 0;
		int j = 0;
		updateCounter++; // Count
		
		// Determine which direction the sprite is currently moving
		switch(getDirection()) {
		case LEFT:
			i-=STEP_SIZE;
			if (updateCounter % 5 == 0) {
			  curSpriteIndex = leftSequence[(currentSpriteNum++ % 4)];
			}
			break;
		case RIGHT:
			i+=STEP_SIZE;
			if (updateCounter % 5 == 0) {
			  curSpriteIndex = rightSequence[(currentSpriteNum++ % 4)];
			}
			break;
		case UP:
			j-=STEP_SIZE;
			if (updateCounter % 5 == 0) {
			   curSpriteIndex = upSequence[(currentSpriteNum++ % 4)];
			}
			break;
		case DOWN:
			j+=STEP_SIZE;
			if (updateCounter % 5 == 0) {
				curSpriteIndex = downSequence[(currentSpriteNum++ % 4)]; 
			}
			break;
		case STOPPED:
			if (updateCounter % 5 == 0) {
				curSpriteIndex = notMovingSequence[(currentSpriteNum++ % 4)]; 
			}
			break;
		case BACKUP: 
			if (updateCounter % 5 == 0){
				curSpriteIndex = notMovingSequence[currentSpriteNum++ % 4];
			}
			break;
		
		}
		
	// Update the sprite's location
		
	// If the sprite is stopped, do nothing
	if (getDirection() == STOPPED){
	}
	// Else if the sprite is backing up, reverse the pipe
	else if (getDirection() == BACKUP){
		reversePipe();
	}
	// Else the sprite is moving so update location
	else{
		// If it can move to the new location, update x and y
		if ((main.frame).canMove((x+i),(y+j))){
			x = (x+i);
			y = (y+j);
			// Add a new rectangle to the pipe.
			pipe.add(new Rectangle(x-STEP_SIZE,y-STEP_SIZE,STEP_SIZE,STEP_SIZE));
		}
		// Else it can't move to the new location, so do nothing
		else{
		}			
	}	
	}
	
	/** Reverses the "pipe"*/
	public synchronized void reversePipe(){
		// Size of the arrayList
		int numRects = pipe.size();
		//System.out.println("x "+x+"y "+y);
		
		// If the arrayList has pipe in it and the sprite is not at it's starting location (515,176)
		if (numRects > 1 && x > 515 || y > 176) {	
			// Remove STEP_SIZE pipe sections at a time so it goes faster
			for(int i=0;i<STEP_SIZE;i++){
				numRects--;
				pipe.remove(numRects);
				x = pipe.get(numRects-1).x;
				y = pipe.get(numRects-1).y;
			}
		}	
		else if (x < 515 || y < 176){
			x = 515;
			y = 176;
		}
	}


}

