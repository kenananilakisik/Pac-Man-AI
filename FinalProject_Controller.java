package pacman.controllers.kenan_akisik;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class FinalProject_Controller extends Controller<MOVE> {
	public static StarterGhosts ghosts = new StarterGhosts();
	static int targetPowerPill = 0;
	static int ghostDetectionRange = 25;
	public MOVE getMove(Game game,long timeDue){
		int [] powerIndices = game.getActivePowerPillsIndices();
		int [] pillIndices = game.getActivePillsIndices();
		int [] ghostIndices = new int [4];
		int blinky,pinky,inky,sue = 0;
		MOVE moveChoice = MOVE.RIGHT;
		int current = game.getPacmanCurrentNodeIndex();
		int closestPillIndex = findClosestPill(game,current,pillIndices);
		if(game.getGhostCurrentNodeIndex(GHOST.BLINKY) == -1) blinky = 1000;
		else blinky = game.getGhostCurrentNodeIndex(GHOST.BLINKY);
		if(game.getGhostCurrentNodeIndex(GHOST.PINKY) == -1) pinky = 1000;
		else pinky = game.getGhostCurrentNodeIndex(GHOST.PINKY);
		if(game.getGhostCurrentNodeIndex(GHOST.INKY) == -1) inky = 1000;
		else inky = game.getGhostCurrentNodeIndex(GHOST.INKY);
		if(game.getGhostCurrentNodeIndex(GHOST.SUE) == -1) sue = 1000;
		else inky = game.getGhostCurrentNodeIndex(GHOST.SUE);
		ghostIndices[0] = blinky;
		ghostIndices[1] = pinky;
		ghostIndices[2] = inky;
		ghostIndices[3] = sue;
		int closestGhostIndex = findClosestGhost(game, current,ghostIndices);
		if(isGhostInRange(game,current,ghostIndices)){
			if(isClosestGhostEdible(game,closestGhostIndex)){
				moveChoice = game.getNextMoveTowardsTarget(current, closestGhostIndex, DM.PATH);
			}else{
				if(isMoreThanTwoGhostsNearby(game,current,ghostIndices)){
					moveChoice = game.getNextMoveAwayFromTarget(current, closestGhostIndex, DM.PATH);
					if(isDangerous(game,current,ghostIndices,moveChoice)){
						moveChoice = moveChoice.opposite();
					}
				}else{
					moveChoice = game.getNextMoveAwayFromTarget(current, closestGhostIndex, DM.PATH);
				}
			}
		}
		else if(isPowerPillInRange(game, current,powerIndices)){
			moveChoice = game.getNextMoveTowardsTarget(current, targetPowerPill, DM.PATH);
		}
		else{
			moveChoice = game.getNextMoveTowardsTarget(current, closestPillIndex, DM.PATH);
		}		
		return moveChoice;
	}
	public boolean isPowerPillInRange (Game game, int current, int[] powerIndices){
		boolean flag = false;
		for(int i = 0; i<powerIndices.length; i++){
			if(distance(game.getNodeXCood(current)
					,game.getNodeYCood(current)
					,game.getNodeXCood(powerIndices[i])
					,game.getNodeYCood(powerIndices[i]))<=40){
				flag = true;
				targetPowerPill = powerIndices[i];
				break;
			}else{
				flag = false;
			}	
		}
		return flag;
	}
	public boolean isGhostInRange (Game game, int current, int [] ghostIndices){
		boolean flag = false;
		for(int i = 0; i<ghostIndices.length; i++){
			if(distance(game.getNodeXCood(current)
					,game.getNodeYCood(current)
					,game.getNodeXCood(ghostIndices[i])
					,game.getNodeYCood(ghostIndices[i]))<=ghostDetectionRange){
				flag = true;
				break;
			}else{
				flag = false;
			}	
		}
		return flag;
	}
	public int findClosestGhost (Game game, int current, int [] ghostIndices){
		int minIndex = ghostIndices[0];
		double min = distance(game.getNodeXCood(current)
				,game.getNodeYCood(current)
				,game.getNodeXCood(ghostIndices[0])
				,game.getNodeYCood(ghostIndices[0]));
		for(int i = 0; i<ghostIndices.length; i++){
			double dist = distance(game.getNodeXCood(current)
					,game.getNodeYCood(current)
					,game.getNodeXCood(ghostIndices[i])
					,game.getNodeYCood(ghostIndices[i]));
			if(dist< min){
				minIndex = ghostIndices[i];
				min = dist;
			}
		}
		return minIndex;
	}
	public boolean isClosestGhostEdible(Game game, int closestGhost){
		boolean flag = false;
		int blinky = game.getGhostCurrentNodeIndex(GHOST.BLINKY);
		boolean isBlinkyEdible = game.isGhostEdible(GHOST.BLINKY);
		int pinky = game.getGhostCurrentNodeIndex(GHOST.PINKY);
		boolean isPinkyEdible = game.isGhostEdible(GHOST.PINKY);
		int inky = game.getGhostCurrentNodeIndex(GHOST.INKY);
		boolean isInkyEdible = game.isGhostEdible(GHOST.INKY);
		int sue = game.getGhostCurrentNodeIndex(GHOST.SUE);
		boolean isSueEdible = game.isGhostEdible(GHOST.SUE);
		if(blinky == closestGhost && isBlinkyEdible) flag = true;
		else if(pinky == closestGhost && isPinkyEdible) flag = true;
		else if(inky == closestGhost && isInkyEdible) flag = true;
		else if(sue == closestGhost && isSueEdible) flag = true;
		else flag = false;
		return flag;
	}
	public boolean isPowerPillReachable (Game game, int current){
		return true;
	}
	public double distance(int x, int y, int px, int py){
		return Math.sqrt(Math.pow(x-px, 2)+Math.pow(y-py, 2));
	}
	public int findClosestPill(Game game, int current, int [] pillIndices){
		int minIndex = pillIndices[0];
		double min = distance(game.getNodeXCood(current)
				,game.getNodeYCood(current)
				,game.getNodeXCood(pillIndices[0])
				,game.getNodeYCood(pillIndices[0]));
		for(int i = 0; i<pillIndices.length; i++){
			double dist = distance(game.getNodeXCood(current)
					,game.getNodeYCood(current)
					,game.getNodeXCood(pillIndices[i])
					,game.getNodeYCood(pillIndices[i]));
			if(dist< min){
				minIndex = pillIndices[i];
				min = dist;
			}
		}
		return minIndex;
	}
	public boolean isMoreThanTwoGhostsNearby(Game game, int current, int[] ghostIndices){
		int count = 0;
		for(int i = 0; i<ghostIndices.length; i++){
			if(distance(game.getNodeXCood(current)
					,game.getNodeYCood(current)
					,game.getNodeXCood(ghostIndices[i])
					,game.getNodeYCood(ghostIndices[i]))<=25){
				count++;
			}
		}
		if(count >= 2) return true;
		else return false;
	}
	public boolean isDangerous(Game game, int current, int[] ghostIndices, MOVE move){
		Game gameCopy = game.copy();
		gameCopy.advanceGame(move, ghosts.getMove(gameCopy, 0));
    	Queue<moveNode> queue = new LinkedList<moveNode>();
    	moveNode rootNode = new moveNode(gameCopy,0, new ArrayList<MOVE>());
        queue.add(rootNode);
		return Search(queue);
	}
	public boolean Search(Queue<moveNode> queue){
        Game tempCopy = null;
        Game anotherCopy = null;
        boolean flag = true;
		while(!queue.isEmpty()){
			int blinky,pinky,inky,sue = 0;
			int [] pointerGhosts = new int [4];
        	moveNode nodePointer = queue.remove();
        	Game pointerGame = nodePointer.getGame();
        	int pointerPacman = nodePointer.getGame().getPacmanCurrentNodeIndex();
        	if(pointerGame.getGhostCurrentNodeIndex(GHOST.BLINKY) == -1) blinky = 1000;
     		else blinky = pointerGame.getGhostCurrentNodeIndex(GHOST.BLINKY);
     		if(pointerGame.getGhostCurrentNodeIndex(GHOST.PINKY) == -1) pinky = 1000;
     		else pinky = pointerGame.getGhostCurrentNodeIndex(GHOST.PINKY);
     		if(pointerGame.getGhostCurrentNodeIndex(GHOST.INKY) == -1) inky = 1000;
     		else inky = pointerGame.getGhostCurrentNodeIndex(GHOST.INKY);
     		if(pointerGame.getGhostCurrentNodeIndex(GHOST.SUE) == -1) sue = 1000;
     		else inky = pointerGame.getGhostCurrentNodeIndex(GHOST.SUE);
     		pointerGhosts[0] = blinky;
     		pointerGhosts[1] = pinky;
     		pointerGhosts[2] = inky;
     		pointerGhosts[3] = sue;
        	MOVE[] possibleMoves = 
        		nodePointer.getGame().getPossibleMoves(pointerPacman);
        	if(nodePointer.depth>7){
        		if(pointerGame.wasPacManEaten()) flag = false;
        	}else{
        		ArrayList<MOVE> temp = new ArrayList<MOVE>(nodePointer.getMoveList());
        		tempCopy = nodePointer.getGame().copy();
        		for(MOVE n: possibleMoves){
            		anotherCopy = tempCopy.copy();
            		anotherCopy.advanceGame(n, ghosts.getMove(anotherCopy, 0));
            		moveNode childNode = new moveNode(anotherCopy,nodePointer.depth+1,new ArrayList<MOVE>());
            		childNode.add(n,temp);
            		queue.add(childNode);
            	}
        		 
        	}
		 }
		return flag;
	}
	public boolean isSafe(Game game, int current, int[] ghostIndices){
		boolean flag = true;
		for(int i = 0; i<ghostIndices.length; i++){
			if(distance(game.getNodeXCood(current),game.getNodeYCood(current)
					,game.getNodeXCood(ghostIndices[i]),game.getNodeYCood(ghostIndices[i]))<5){
				flag = false;
			}
		}
		return flag;
	}
}