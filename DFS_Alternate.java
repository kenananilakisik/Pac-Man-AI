package pacman.controllers.kenan_akisik;
import java.util.ArrayList;
import java.util.Stack;
import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
/**
 *
 * @author KENAN ANIL AKISIK
 * Goal is this controller is to eat all the power pills that is present in the maze
 * when all the power pills are eaten game is over and score is based on the last move 
 * thats is taken towards the last power pill.
 */
public class DFS_Alternate extends Controller<MOVE>{
    public static StarterGhosts ghosts = new StarterGhosts();
    public static MOVE lastMove = MOVE.NEUTRAL;
    public static int ind = 0;
    public static boolean over = false;
    static ArrayList<MOVE> moves = new ArrayList<MOVE>();
	public MOVE getMove(Game game,long timeDue){
		System.out.println("here1");
		if(moves.isEmpty()){
			System.out.println("here2");
			int[] powerIndices = game.getActivePowerPillsIndices();
	        MOVE[] possibleMove = null;
	        moveNode rootNode = null;
	        Game tempCopy = null;
	        Game anotherCopy = null;
	    	ArrayList<moveNode> list = new ArrayList<moveNode>();
	    	Stack<moveNode> stack = new Stack<moveNode>();
	    	Stack<moveNode> emptyStack = new Stack<moveNode>();
	        Game gameCopy1 = game.copy();
	        rootNode = new moveNode(gameCopy1,0, new ArrayList<MOVE>());
	        stack.push(rootNode);
	        
	        while(!stack.empty()){
	        	System.out.println("here3");
	        	moveNode nodePointer = stack.pop();
	        	possibleMove = nodePointer.getGame().getPossibleMoves(nodePointer.getGame().getPacmanCurrentNodeIndex());
	        	System.out.println(nodePointer.getGame().getScore());
	        	if(Math.abs(nodePointer.getGame().getScore())>game.getScore()){  
	        		System.out.println("here4");
	        		moves = nodePointer.getMoveList();
	        		stack = emptyStack;
	        	}
	        	else{
	        		ArrayList<MOVE> temp = new ArrayList<MOVE>(nodePointer.getMoveList());
	        		tempCopy = nodePointer.getGame().copy();
	        		for(MOVE n: possibleMove){
	            		anotherCopy = tempCopy.copy();
	            		anotherCopy.advanceGame(n, ghosts.getMove(anotherCopy, 0));
	            		moveNode childNode = new moveNode(anotherCopy,nodePointer.depth+1,new ArrayList<MOVE>());
	            		childNode.add(n,temp);
	            		stack.push(childNode);
	            	}
	        	}                	
	        }
		}
        MOVE curMove;
        curMove = moves.get(0);
        System.out.println("Moves in the List: ");
        for(int i = 0; i< moves.size(); i++){
        	System.out.print(moves.get(i)+ ",");
        }
        System.out.println("\nMove that is being extracted from the list: "+ moves.get(0));
        moves.remove(0);
        return curMove;        
	}
	public boolean isMember(int index, int[]indices){
		boolean flag = false;
		for(int i=0; i<indices.length; i++){
			if(index == indices[i]){
				flag = true;
			}
		}
		return flag;
	}
}

