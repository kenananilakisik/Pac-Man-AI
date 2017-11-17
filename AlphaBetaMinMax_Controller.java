package pacman.controllers.kenan_akisik;
import java.util.*;
import java.lang.Math;
import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
public class AlphaBetaMinMax_Controller extends Controller<MOVE>
{
	public static StarterGhosts ghosts = new StarterGhosts();
	public static MOVE lastMove = MOVE.NEUTRAL;
	
	Stack<moveNodeAlphaBeta> stack = new Stack<moveNodeAlphaBeta>();
	public MOVE getMove(Game game,long timeDue)
	{
		MOVE[] startingMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
		moveNodeAlphaBeta rootNode = null;
		int temp = 0;
		MOVE candidate = MOVE.RIGHT;

		for(MOVE m: startingMoves)
        {
			Stack<moveNodeAlphaBeta> stack = new Stack<moveNodeAlphaBeta>();
            Game gameCopy1 = game.copy();
            gameCopy1.advanceGame(m, ghosts.getMove(gameCopy1, 0));
            rootNode = new moveNodeAlphaBeta(gameCopy1,gameCopy1.getScore());
            stack.push(rootNode);
            int score = ab(rootNode,7,-100000,100000,true,stack);
            if (score >= temp)
            {
            	System.out.println("here");
            	temp = score;
            	if(m != lastMove.opposite())
            	{
            		candidate = m;
            	}
            	else
            	{
            		candidate = lastMove;
            	}
            }               
        }
		AlphaBetaMinMax_Controller.lastMove = candidate;
		System.out.println(candidate);
        return candidate;
	}
	public int ab(moveNodeAlphaBeta node,
			int depth,int alpha,int beta,boolean maximizingPlayer,
			Stack<moveNodeAlphaBeta> stack)
	{
		MOVE[] possibleMove = null;
		Game tempCopy = null;
		moveNodeAlphaBeta nodePointer = null;
		moveNodeAlphaBeta childNode = null;
    	nodePointer = stack.pop();
    	possibleMove = nodePointer.game.getPossibleMoves(nodePointer.game.getPacmanCurrentNodeIndex());
    	int v = 0;
    	while (!stack.empty())
    	{
			if (depth == 0)
			{
				return nodePointer.score;
			}
			if (maximizingPlayer)
			{
				v = -100000;
				for(MOVE n: possibleMove)
	        	{
	        		tempCopy = nodePointer.game.copy();
	        		tempCopy.advanceGame(n, ghosts.getMove(tempCopy, 0));
	        		childNode = new moveNodeAlphaBeta(tempCopy,tempCopy.getScore());
	        		stack.push(childNode);
	        		v = Math.max(v,ab(childNode,depth-1,alpha,beta,false,stack));
	        		alpha = Math.max(alpha, v);
	        		if(beta <= alpha)
	        			break;
	        	}
				return v;
			}
			else
			{
				v = 100000;
				for(MOVE n: possibleMove)
	        	{
	        		tempCopy = nodePointer.game.copy();
	        		tempCopy.advanceGame(n, ghosts.getMove(tempCopy, 0));
	        		childNode = new moveNodeAlphaBeta(tempCopy,tempCopy.getScore());
	        		stack.push(childNode);
	        		v = Math.min(v,ab(childNode,depth-1,alpha,beta,true,stack));
	        		alpha = Math.min(alpha, v);
	        		if(beta <= alpha)
	        			break;
	        	}
				return v;
			}	
    	}
    	return v;
	}
}
