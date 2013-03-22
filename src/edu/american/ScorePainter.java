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
import java.awt.Font;
import java.awt.Graphics;
/** Paints the score on the game frame and adds points.
 * @author myers*/
public class ScorePainter implements Painter{
	/** The font.*/
	private Font font;
	/** The current score.*/
	private int currentScore = 0;
	
	/** The constructor. */
	public ScorePainter(){
		// Make the font
		font = new Font("Segoe Script", Font.BOLD, 28);
	}
	
	/** Adds a point to the score.*/
	public int addPoint(boolean hit){
		// If the player hit a dot, add a point.
		if (hit){
			currentScore++;
		}
		return currentScore;
	}
	
	/** Paint the score.*/
	@Override
	public void paint(Graphics g) {
		
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("Score: "+currentScore, 725,30);
		
	}

}
