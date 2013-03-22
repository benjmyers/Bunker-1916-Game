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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
/** Creates the dots.
 * @author fleck, myers
 */
public class Dots implements Painter {
	
	/** ArrayList of all the dots. */
	public ArrayList<Rectangle> dotList = new ArrayList<Rectangle>();
	/** Boots running audio clip. */
	Clip running;
	/** Audio input. */
	AudioInputStream audioIn;
	/** Main object. */
	private Main main;
	
	/** Constructor. */
	public Dots(Main main){
		this.main = main;
		// Loop through the array list of tunnels and add a dot for each
		for(Rectangle r : main.frame.getTunnels()){
			dotList.add(new Rectangle(r.x+30,r.y+30,4,4));
			
		}
		
		// Create the running clip.
		File soundFile = new File("media/running.wav");
		
		try {audioIn = AudioSystem.getAudioInputStream(soundFile);} 
			catch (UnsupportedAudioFileException e1) {e1.printStackTrace();} 
			catch (IOException e1) {e1.printStackTrace();}
		try {running = AudioSystem.getClip();} 
			catch (LineUnavailableException e1) {e1.printStackTrace();}
		try {running.open(audioIn);} 
			catch (LineUnavailableException e) {e.printStackTrace();} 
			catch (IOException e) {e.printStackTrace();}
	}
	
	/** Checks if the sprite has hit any dots.
	 * @param p - the sprite. */
	public boolean hitDot(Player p){
		// Initialize
		boolean hit = false;
		
		// Loop through the list of dots
		for (int i=0;i< dotList.size();i++){
			Rectangle r = dotList.get(i);
			
			// If the player has hit a dot
			if (r.intersects(p.x-10,p.y-10,28,28)){
				// Remove the dot from the list
				dotList.remove(r);
				// Play the running clip
				running.loop(100);
				// Return true
				hit = true;
			}
			// Else if the sprite hasn't hit any dots but is moving
			else if (main.sprite.getDirection()!= Player.STOPPED){
				// Play the running clip
				running.loop(100);
			}
			// Else a dot hasn't been hit and the sprite isn't moving
			else{
				if(running.isRunning()&& main.sprite.getDirection()== Player.STOPPED){
					// Stop the running clip
					running.stop();
				}
			}
		}
		// Return whether or not a dot has been hit
		return hit;
	}
	/** Close this frame. */
	public void end() {
		dotList.clear();
		if(running.isRunning()){
			running.stop();
		}
	}
	
	/** Paints the dots. */
	@Override
	public void paint(Graphics g) {
		for(Rectangle dr : dotList){
			g.setColor(Color.WHITE);
			g.draw3DRect(dr.x, dr.y, dr.width,dr.height, true);

		}
	}

}
