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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
/** Creates the NPC players and checks their status.*/
public class NPC implements Painter {
	

	// Constants for directions of motion of the sprite
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int UP = 2;
	public static final int DOWN = 3;
	public static final int STOPPED = 4;
	public static final int BACKUP = 5;
	
	// Constant for the level at which the NPC starts
	public static final int LEVEL1 = 0;
	public static final int LEVEL2 = 1;
	public static final int LEVEL3 = 2;
	public static final int LEVEL4 = 3;
	
	// Constant for the side the NPC starts
	public static final int LEFT_SIDE = 0;
	public static final int RIGHT_SIDE = 1;
	
	
	/**
	 * A serial UID
	 */
	private static final long serialVersionUID = -3150167263241846626L;
	/** Top left X component. */
	protected int x;
	/** Top left Y component. */
	protected int y;
	/** Holds the sprite sheet. */
	private BufferedImage NPCspriteSheet;
	/** Width of the grid for sprites. */
	private int gridWidth;
	/** Height of the grid for sprites. */
	private int gridHeight;
	/** Sequence of sprites for moving left. */
	private int[] leftSequence;
	/** Sequence of sprites for moving right. */
	private int[] rightSequence;
	/** Which sprite on the sheet are we currently
	 *  showing. So this is like 18, 28, 38...
	 */
	private int curSpriteIndex;
	/** How many columns are there on the sprite sheet? */
	private int numSpriteCols;
	/** What direction is the sprite currently 
	 *  moving. Needed to decide which sequence 
	 *  of sprites to show. 
	 */
	private int direction;
	/** Current sprite index in the sequence, 
	 *  so this is like 0,1,2...
	 */
	private int currentSpriteNum;
	/** Just a counter to slow down animations. */
	private int updateCounter;
	/** Which tunnel level the NPC chooses to start on.*/
	private int level;
	/** Which side the NPC chooses to start on.*/
	private int side;
	/** Rifle shots sound.*/
	Clip gunShot;
	/** Bomb falling sound.*/
	Clip bombFall;
	/** Explosion sound.*/
	Clip explosion;
	/** Audio stream 1.*/
	AudioInputStream audioIn1;
	/** Audio stream 2.*/
	AudioInputStream audioIn2;
	/** Audio stream 3.*/
	AudioInputStream audioIn3;
	/** Random number generator*/
	Random rand = new Random();
	/** Main object.*/
	private Main main;
	
	/** NPC Constructor. */
	public NPC(Main main){
		super(); // Call superclass constructor	
		this.main = main;
		// Size of each sprite on the grid
		gridWidth = 35;
		gridHeight = 35;
		
		// Sequences of sprites
		leftSequence = new int[] {6, 7, 6, 4};
		rightSequence = new int[] {1, 3, 1, 0};
		
		// Number of columns in sprite sheet
		numSpriteCols = 8; 
		
		// Load sprite sheet from 
		String fileName = main.menuFrame.getPlayer2();
		File spriteFile = new File(fileName);
		try {
			NPCspriteSheet = ImageIO.read(spriteFile);
		} catch (IOException e) {
			System.err.println("Could not load the image file!");
			e.printStackTrace();
		}
		
		// Load the gun shot clip
		File soundFile1 = new File("media/gun shots.wav");
		try {audioIn1 = AudioSystem.getAudioInputStream(soundFile1);} 
			catch (UnsupportedAudioFileException e1) {e1.printStackTrace();} 
			catch (IOException e1) {e1.printStackTrace();}
		try {gunShot = AudioSystem.getClip();} 
			catch (LineUnavailableException e1) {e1.printStackTrace();}
		try {gunShot.open(audioIn1);} 
			catch (LineUnavailableException e) {e.printStackTrace();} 
			catch (IOException e) {e.printStackTrace();}
			
		// Load the bomb falling clip
		File soundFile2 = new File("media/bombFalling.wav");
		try {audioIn2 = AudioSystem.getAudioInputStream(soundFile2);} 
			catch (UnsupportedAudioFileException e1) {e1.printStackTrace();} 
			catch (IOException e1) {e1.printStackTrace();}
		try {bombFall = AudioSystem.getClip();} 
			catch (LineUnavailableException e1) {e1.printStackTrace();}
		try {bombFall.open(audioIn2);} 
			catch (LineUnavailableException e) {e.printStackTrace();} 
			catch (IOException e) {e.printStackTrace();}
		
		// Load the explosion clip
		File soundFile3 = new File("media/explosion.wav");
		try {audioIn3 = AudioSystem.getAudioInputStream(soundFile3);} 
			catch (UnsupportedAudioFileException e1) {e1.printStackTrace();} 
			catch (IOException e1) {e1.printStackTrace();}
		try {explosion = AudioSystem.getClip();} 
			catch (LineUnavailableException e1) {e1.printStackTrace();}
		try {explosion.open(audioIn3);} 
			catch (LineUnavailableException e) {e.printStackTrace();} 
			catch (IOException e) {e.printStackTrace();}
		
			
		// Let the sprite choose where to start moving and on which side
		startNPC();

		
	}
	/** Starts the NPCs movement. */
	public void startNPC(){
		level = rand.nextInt(4);
		
		side = rand.nextInt(2);
		
		//System.out.println("Level: "+level+" Side: "+side);
		
		// For LEVEL1, NPCs can start anywhere
		if (level == LEVEL1){
			y = 272;
			if (side == LEFT_SIDE){
				x = 40;
				setDirection(RIGHT);
				curSpriteIndex = rightSequence[0];
			}
			else{
				x = 899;
				setDirection(LEFT);
				curSpriteIndex = leftSequence[0];
			}
		}
		// For LEVEL2, NPCs can only start on the right
		else if (level == LEVEL2){
			y = 425;
			x = 899;
			setDirection(LEFT);
			curSpriteIndex = leftSequence[0];
		}
		// For LEVEL3, NPCs can only start on the right
		else if (level == LEVEL3){
			y = 500;
			x = 899;
			setDirection(LEFT);
			curSpriteIndex = leftSequence[0];
		}
		// For LEVEL4, NPCs can start anywhere
		else{
			y = 574;
			if (side == LEFT_SIDE){
				x = 1;
				setDirection(RIGHT);
				curSpriteIndex = rightSequence[0];
			}
			else{
				x = 899;
				setDirection(LEFT);
				curSpriteIndex = leftSequence[0];
			}
		}
	}
	
	/** Set direction we're moving.
	 * @param dir - the direction. */
	public void setDirection(int dir) {		
				
			this.direction = dir;	
		
	}
	
	
	/** Update the location of the rectangle.
	 * @param newX - the new x location
	 * @param newY - the new y location. */
	public void setLocation(int newX, int newY) {
		//System.out.println("X: "+x+" Y: "+y);
		x=newX;
		y=newY;
		
		//System.out.println("G:"+getX()+" "+getY());
	}
	
	/** Returns the direction.*/
	public int getDirection() {
		return direction;
	}

	/** Paints the graphics.*/
	@Override
	public void paint(Graphics g) {
		
		// Figure out where on the sprite sheet we get
		// the current sprite to draw. This is the 
		// x,y location within the sprite sheet.
		int frameX = (curSpriteIndex % numSpriteCols) * gridWidth;
        int frameY = (curSpriteIndex / numSpriteCols) * gridHeight;
        //System.out.println("X: "+frameX+" Y: "+frameY);

		
        g.drawImage(NPCspriteSheet, 
				x, y-10, x+gridWidth, (y-10)+gridHeight, // Where in the Component
                frameX, frameY, frameX+gridWidth, frameY+gridHeight, null);
			}
	
	/** Close this frame. */
	public void end() {
		if(gunShot.isRunning()){
			gunShot.stop();
		}
		else if(bombFall.isRunning()){
			bombFall.stop();
		}
	}
	
	/** Update the sprite by moving it in whatever direction
	 *  it's going. 
	 */
	
	public void update() {
		
		updateCounter++; // Count
		
		//-----------------------------------------
		// Check bounding conditions for NPC movement
		//-----------------------------------------
		
		// For LEVEL1, NPCs can start anywhere but can't go off the left side
		if (level == LEVEL1){
			// If the sprite is going to hit the left side, turn it around
			if (x == 20){
				setDirection(RIGHT);
				curSpriteIndex = rightSequence[0];
			}
			// Else if the sprite goes off the right side, create a new sprite elsewhere
			else if(x == 900){
				startNPC();
				
			}
			
		}
		// For LEVEL2, NPCs can't go off the left side
		else if (level == LEVEL2){
			// If the sprite is going to hit the left side, turn it around
			if (x == 40){
				setDirection(RIGHT);
				curSpriteIndex = rightSequence[0];
			}
			// Else if the sprite goes off the right side, create a new sprite elsewhere
			else if(x == 900){
				startNPC();
			}
		}
		// For LEVEL3, NPCs can't go off the left side
		else if (level == LEVEL3){
			// If the sprite is going to hit the left side, turn it around
			if (x == 85){
				setDirection(RIGHT);
				curSpriteIndex = rightSequence[0];
			}
			// Else if the sprite goes off the right side, create a new sprite elsewhere
			else if(x == 900){
				startNPC();
			}
		}
		// For LEVEL4, NPCs can move anywhere
		else{
			// If the sprite goes off either side, create it elsewhere
			if(x == 900 || x == 0){
				startNPC();
				
			}
		}
		
		switch(getDirection()) {
		case LEFT:
			
			x--;
			
			if (updateCounter % 5 == 0) {
			  curSpriteIndex = leftSequence[(currentSpriteNum++ % 4)];
			}
			break;
			
		case RIGHT:
			x++;
			if (updateCounter % 5 == 0) {
			  curSpriteIndex = rightSequence[(currentSpriteNum++ % 4)];
			}
			break;
		
		
		}
		
		
	}
	/** Checks if the NPC has hit a sprite*/
	public synchronized boolean hitSprite(NPC p){
		Rectangle testRect = new Rectangle(x,y,35,35);
		//System.out.println(testRect.x+" "+testRect.y+" ");
		if (testRect.intersects(main.sprite.x+5,main.sprite.y+5,20,20)){
			return true;
		}
		else if (testRect.intersects(main.sprite.x-200,main.sprite.y,main.sprite.x+200,35)){
			//System.out.println("gun shot");
			gunShot.loop(50);
		}
		else{
			gunShot.stop();
		}
		return false;
	}
	
	/** Checks if the NPC has hit the pipe*/
	public synchronized boolean hitPipe(NPC p){
		Rectangle testRect = new Rectangle(x,y,35,35);
		ArrayList<Rectangle> pipe = main.sprite.getPipe();
		for(Rectangle r : pipe){
			if (testRect.intersects(r.x+10,r.y+10,r.width,r.height)){
				explosion();
				return true;
			}
			else if ((testRect.intersects(r.x-100,r.y,r.x-10,r.height))||
					testRect.intersects(r.width+10,r.y-5,r.width+100,r.height+5)){
				bombFall();
			}
		}
		return false;
	}

	/** Starts the explosion sound.*/
	private synchronized void explosion(){
		//System.out.println("explosion");
		if (bombFall.isActive()){
			bombFall.stop();
		}
		explosion.start();
	}
	/** Starts the bomb falling sound.*/
	private synchronized void bombFall(){
		//System.out.println("bomb fall");
		bombFall.loop(0);
	}
}



