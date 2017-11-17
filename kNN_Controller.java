 package pacman.controllers.kenan_akisik;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.io.*;

import pacman.controllers.Controller;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class kNN_Controller extends Controller<MOVE> {
	public static MOVE best = MOVE.NEUTRAL;
	public MOVE getMove(Game game,long timeDue)
	{
		try {
			BufferedReader br = new BufferedReader(new FileReader("training.txt"));
			String line = null;
			double min = 100000;
			int [] powerIndices = game.getPowerPillIndices();
			MOVE curMove = null;
			while((line = br.readLine()) != null)
			{
				String temp[] = line.split(";");
				int tLevel = 100 * (Integer.parseInt(temp[0]));
				int tPIndex = Integer.parseInt(temp[1]);
				int tPill = Integer.parseInt(temp[2]);
				int tPower = Integer.parseInt(temp[3]);
				int tDistPower1 = Integer.parseInt(temp[4]);
				int tDistPower2 = Integer.parseInt(temp[5]);
				int tDistPower3 = Integer.parseInt(temp[6]);
				int tDistPower4 = Integer.parseInt(temp[7]);
				int tBlinky = Integer.parseInt(temp[8]);
				int tPinky = Integer.parseInt(temp[9]);
				int tInky = Integer.parseInt(temp[10]);
				int tSue = Integer.parseInt(temp[11]);
				int tBlinkyEdible;
				int tPinkyEdible;
				int tInkyEdible;
				int tSueEdible;
				if (temp[12].equals("false")) tBlinkyEdible = 0;
				else tBlinkyEdible = 100;
				if (temp[13].equals("false")) tPinkyEdible = 0;
				else tPinkyEdible = 100;
				if (temp[14].equals("false")) tInkyEdible = 0;
				else tInkyEdible = 100;
				if (temp[15].equals("false")) tSueEdible = 0;
				else tSueEdible = 100;
				
				int dLevel = 100 * (game.getCurrentLevel());
	    		int dPIndex = game.getPacmanCurrentNodeIndex();
	    		int dPill = game.getNumberOfActivePills();
	    		int dPower = game.getNumberOfActivePowerPills();
	    		int dDistPower1 = game.getShortestPathDistance(powerIndices[0], game.getPacmanCurrentNodeIndex());
	    		int dDistPower2 = game.getShortestPathDistance(powerIndices[1], game.getPacmanCurrentNodeIndex());
	    		int dDistPower3 = game.getShortestPathDistance(powerIndices[2], game.getPacmanCurrentNodeIndex());
	    		int dDistPower4 = game.getShortestPathDistance(powerIndices[3], game.getPacmanCurrentNodeIndex());
				int dBlinky= game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),
	    				game.getGhostCurrentNodeIndex(GHOST.BLINKY));
				int dPinky = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),
	    				game.getGhostCurrentNodeIndex(GHOST.PINKY));
				int dInky = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),
	    				game.getGhostCurrentNodeIndex(GHOST.INKY));
				int dSue= game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),
	    				game.getGhostCurrentNodeIndex(GHOST.SUE));
				int dBlinkyEdible = 0;
				int dPinkyEdible = 0;
				int dInkyEdible = 0;
				int dSueEdible = 0;
				if (game.isGhostEdible(GHOST.BLINKY) == false) dBlinkyEdible = 0;
				else dBlinkyEdible = 100;
				if (game.isGhostEdible(GHOST.PINKY) == false) dPinkyEdible = 0;
				else dPinkyEdible = 100;
				if (game.isGhostEdible(GHOST.INKY) == false) dInkyEdible = 0;
				else dInkyEdible = 100;
				if (game.isGhostEdible(GHOST.SUE) == false) dSueEdible = 0;
				else dSueEdible = 100;
				
				double dist = euclidian(dLevel,dPIndex,dPill,dPower,dDistPower1,dDistPower2,dDistPower3,dDistPower4
						,dBlinky,dPinky,dInky,dSue,dBlinkyEdible,dPinkyEdible,dInkyEdible,dSueEdible
						,tLevel,tPIndex,tPill,tPower,tDistPower1,tDistPower2,tDistPower3,tDistPower4
						,tBlinky,tPinky,tInky,tSue,tBlinkyEdible,tPinkyEdible,tInkyEdible,tSueEdible);
						
				if ( dist < min)
				{
					min = dist;
					curMove = string_to_move(temp[16]);
				}
			}

			br.close();	
			return curMove;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return MOVE.RIGHT;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return MOVE.RIGHT;
		}    
		   
	}
	public double euclidian(int dLevel,int dPIndex,int dPill,int dPower,int dDistPower1,int dDistPower2,int dDistPower3,int dDistPower4
			,int dBlinky,int dPinky,int dInky,int dSue,int dBlinkyEdible,int dPinkyEdible,int dInkyEdible,int dSueEdible
			,int tLevel,int tPIndex,int tPill,int tPower,int tDistPower1,int tDistPower2,int tDistPower3,int tDistPower4
			,int tBlinky,int tPinky,int tInky,int tSue,int tBlinkyEdible,int tPinkyEdible,int tInkyEdible,int tSueEdible)
	{
		double dist = 0;
		dist = Math.sqrt(
			   Math.pow((dLevel - tLevel),2)
			   + Math.pow((dPIndex - tPIndex),2) 
			   + Math.pow((dPill - tPill),2) 
			   + Math.pow((dPower - tPower),2)
			   + Math.pow((dDistPower1 - tDistPower1),2)
			   + Math.pow((dDistPower2 - tDistPower2),2)
			   + Math.pow((dDistPower3 - tDistPower3),2)
			   + Math.pow((dDistPower4 - tDistPower4),2)
			   + Math.pow((dBlinky - tBlinky),2)
			   + Math.pow((dPinky - tPinky),2)
			   + Math.pow((dInky - tInky),2)
			   + Math.pow((dSue - tSue),2)
			   + Math.pow((dBlinkyEdible - tBlinkyEdible),2)
			   + Math.pow((dPinkyEdible - tPinkyEdible),2)
			   + Math.pow((dInkyEdible - tInkyEdible),2)
			   + Math.pow((dSueEdible - tSueEdible),2));
		return dist;
	}
	public MOVE string_to_move(String move)
	{
		if (move.equals("RIGHT"))
		{
			return MOVE.RIGHT;
		}
		else if (move.equals("LEFT"))
		{
			return MOVE.LEFT;
		}
		else if (move.equals("UP"))
		{
			return MOVE.UP;
		}
		else if (move.equals("DOWN"))
		{
			return MOVE.DOWN;
		}
		else
		{
			return MOVE.UP;
		}
	}

}
