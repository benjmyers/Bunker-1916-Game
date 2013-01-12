package edu.american;

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
