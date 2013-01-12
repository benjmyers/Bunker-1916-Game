package edu.american;

/* Bonus work: 
 * Menu window that allows for selection of player and npc - edited 6 sprite sheets for use.
 * End game window which allows player to restart the game or exit.
 * Added sound clips: rifle shots sound (occurs when player chases after NPC),
 *  	bomb falling sound (warning that the NPC is getting close to the "pipe",
 * 		explosion sound if player loses, song that plays during game. 
 * Made all of the graphics.
 * Note: tunnels are not resizable because the are specifically fitted to the background image.*/
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


/** Constructs everything and updates all players and NPCs.
 * @author myers*/

public class Main implements KeyListener {
	/** ArrayList of players. */
	public ArrayList<Object> players = new ArrayList<Object>();
	/** ArrayList of NPCs */
	public ArrayList<Object> NPCs = new ArrayList<Object>();
	/** Player object.*/
	public Player sprite;
	/** Window displayed at the end of the game.*/
	public EndGame endGameWindow;
	/** GameFrame object. */
	public GameFrame frame;         
	/** Dots object.*/
	public Dots dots;
	/** Score painter object.*/
	public ScorePainter score;
	/** Keeps track of the score.*/
	public int currentScore = 0;
	/** Score needed to win */
	private static final int MAX_SCORE = 180;
	/** Likelyhood the computer will generate a new sprite (lower # means more likely)*/
	private static final int NPC_PROBABILITY = 10000;
	/** audio clip. */
	static Clip song;   
	/** Audio input. */
	AudioInputStream audioIn;
	/** The menu frame*/
	public Menu menuFrame;
	/** Whether or not the game is continuing. */
	private boolean inPlay = true;
	/** Random number generator*/
	Random rand = new Random();
	
	/** Constructs the GUI*/
	public Main(){		
		int clickCount = 0;
		
		// --------------------
		// Make the menu window
		// --------------------
		menuFrame = new Menu();
		
		// Create the audio clip.
		File soundFile = new File("media/arthurFields.wav");
		
		try {audioIn = AudioSystem.getAudioInputStream(soundFile);} 
			catch (UnsupportedAudioFileException e1) {e1.printStackTrace();} 
			catch (IOException e1) {e1.printStackTrace();}
		try {song = AudioSystem.getClip();} 
			catch (LineUnavailableException e1) {e1.printStackTrace();}
		try {song.open(audioIn);} 
			catch (LineUnavailableException e) {e.printStackTrace();} 
			catch (IOException e) {e.printStackTrace();}

		song.loop(10);
		
		// Setup the game frame
		while(clickCount < 2){
			menuFrame.repaint();
			clickCount = menuFrame.getClickCount();
			//System.out.println("Click count"+clickCount);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		menuFrame.dispose();
		gameFrame();
		
	}
	
	/** Makes the game frame.*/
	public synchronized void gameFrame(){
		// --------------------
		// Make the GUI frame
		// --------------------
		frame = new GameFrame(this);
		
		// Set the background image
		frame.setBackground("images/bunker1 copy.png");
		
		// Set the mapfile
		try {
			frame.setMap("media/basicmap");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Make the dots
		dots = new Dots(this);
		
		// Add dots to painter list
		frame.addPainter(dots);
		
		// Make the score painter
		score = new ScorePainter();
		
		// Add score to painter list
		frame.addPainter(score);
		
		// --------------------
		// Construct the player
		// --------------------
		sprite = new Player(this);
		
		// Set the initial position
		sprite.setDirection(Player.STOPPED);
		
		// Store the player
		players.add(sprite);
		
		// Add sprite to painter list
		frame.addPainter(sprite);
		
		// Initialize the pipe array
		sprite.pipe.add(new Rectangle(0,0,1,1));
			
		// --------------------
		// NPC construction
		// --------------------
		// (Initially, only three NPCs are put into play)
		
		// Create NPCs
		NPC npc1 = new NPC(this);
		NPC npc2 = new NPC(this);
		NPC npc3 = new NPC(this);
		
		// Add them to the NPC arraylist
		NPCs.add(npc1);
		NPCs.add(npc2);
		NPCs.add(npc3);
		
		// Add them to the frame
		frame.addPainter(npc1);
		frame.addPainter(npc2);
		frame.addPainter(npc3);
		
		// Start an infinite animation loop
		animationLoop();
		
	}
	/** Updates the GameFrame, sprite, and NPCs while checking for winning/losing conditions.*/
	private synchronized void animationLoop() {
		// Initialization
		boolean npcHitPipe = false;
		boolean hitSprite = false;
		boolean hit = false;
		
		
		// Start the animation loop
		while (inPlay) { // loop forever
			
			// Update the location of the sprite
			sprite.update();
			
			// Check if the sprite has hit any dots
			hit = dots.hitDot(sprite);
			
			// If so, add a point to the score
			currentScore = score.addPoint(hit);
			
			// Check if the score is a winning score
			if (currentScore >= MAX_SCORE && inPlay == true){
				endGame(true);
				inPlay = false;
				break;
				
			}
			
			// Randomly generate more NPCs
			if (rand.nextInt(NPC_PROBABILITY) == 35){
				NPC npc = new NPC(this);
				NPCs.add(npc);
				frame.addPainter(npc);
			}
	
			// Update the NPCs
			for(Object npc : NPCs){
				
				((NPC) npc).update();
				
				//Check if they've hit anything
				npcHitPipe = ((NPC) npc).hitPipe((NPC) npc);
				hitSprite = ((NPC) npc).hitSprite((NPC) npc);
				
				// If the pipe was hit, end the game
				if(npcHitPipe && inPlay == true){
					endGame(false);
					inPlay = false;
					break;
				}
				// Else if the sprite hit the NPC, restart the NPC and add a point
				else if (hitSprite){
					//System.out.println("HIT NPC");
					currentScore = score.addPoint(hitSprite);
					((NPC) npc).startNPC();
				}
			}
 
			// Repaint the frame
			frame.repaint();
			
			// Pause a little to allow screen to redraw.
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
		}
		//System.out.println("LOOP END");
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		new Main();
     
    

	}
	
	/** Starts the game. */
	public synchronized static void startGame(){
		Runnable r = new Runnable(){
			public void run(){
				new Main();
			}
		};
		(new Thread(r)).start();
	}
	
	/** Ends the game.*/
	private synchronized void endGame(boolean win){
		
		// Remove all the stored painters
		frame.removePainters();
		// End any running audio files
		for(Object n : NPCs){
			((NPC) n).end();
		}
		// Clear arrays
		NPCs.clear();
		sprite.end();
		players.clear();
		// Stop any audio and clear the dots array
		dots.end();
		// Close the frame
		frame.dispose();
		// Reset the current score
		currentScore = 0;
		// Reinitialize play state
		inPlay = true;
		// Open the end game window
		endGameWindow = new EndGame();
		// Determine which label to display in the end game window
		endGameWindow.setWin(win);
		
		

	}

	/** Handles key events.*/
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch(e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			sprite.setDirection(Player.LEFT);
			break;
		case KeyEvent.VK_RIGHT:
			sprite.setDirection(Player.RIGHT);
			break;
		case KeyEvent.VK_DOWN:
			sprite.setDirection(Player.DOWN);
			break;
		case KeyEvent.VK_UP:
			sprite.setDirection(Player.UP);
			break;
		case KeyEvent.VK_SPACE:
			sprite.setDirection(Player.BACKUP);
			break;
		} 
		
	}
	/** Handles key events.*/
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int current = sprite.getDirection();
		
		// Think it works almost as well to just say STOP MOVING
		// when any key is released
		
		// Set player to not moving if the released 
		// key was the current moving direction.
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			if (current == Player.LEFT) {
				sprite.setDirection(Player.STOPPED);
			}
			break;
		
		case KeyEvent.VK_RIGHT:
			if (current == Player.RIGHT) {
				sprite.setDirection(Player.STOPPED);
			}
			break;
		case KeyEvent.VK_UP:
			if (current == Player.UP) {
				sprite.setDirection(Player.STOPPED);
			}
			break;
		case KeyEvent.VK_DOWN:
			if (current == Player.DOWN) {
				sprite.setDirection(Player.STOPPED);
			}
			break;
		case KeyEvent.VK_SPACE:
			if (current == Player.BACKUP) {
				sprite.setDirection(Player.STOPPED);
			}
			break;
		} 
				
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
