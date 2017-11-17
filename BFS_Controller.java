package pacman.controllers.kenan_akisik;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
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
public class BFS_Controller extends Controller<MOVE>{
    public static StarterGhosts ghosts = new StarterGhosts();
    public static MOVE lastMove = MOVE.NEUTRAL;
    public static int ind = 0;
    public static boolean over = false;
    static ArrayList<MOVE> moves = new ArrayList<MOVE>();
	public MOVE getMove(Game game,long timeDue){
		if(moves.isEmpty()){
	        MOVE[] possibleMove = null;
	        moveNode rootNode = null;
	        Game tempCopy = null;
	        Game anotherCopy = null;

	    	ArrayList<moveNode> list = new ArrayList<moveNode>();
	    	Queue<moveNode> queue = new LinkedList<moveNode>();
	    	//Stack<moveNode> stack = new Stack<moveNode>();
	        Game gameCopy1 = game.copy();
	        rootNode = new moveNode(gameCopy1,0, new ArrayList<MOVE>());
	        queue.add(rootNode);
	        
	        while(!queue.isEmpty()){
	        	moveNode nodePointer = queue.remove();
	        	possibleMove = nodePointer.getGame().getPossibleMoves(nodePointer.getGame().getPacmanCurrentNodeIndex());
	        	System.out.println(nodePointer.getGame().getScore());
	        	if(nodePointer.depth > 9){   		
	        		list.add(nodePointer);
	        	}
	        	else{
	        		ArrayList<MOVE> temp = new ArrayList<MOVE>(nodePointer.getMoveList());
	        		tempCopy = nodePointer.getGame().copy();
	        		for(MOVE n: possibleMove){
	            		anotherCopy = tempCopy.copy();
	            		anotherCopy.advanceGame(n, ghosts.getMove(anotherCopy, 0));
	            		moveNode childNode = new moveNode(anotherCopy,nodePointer.depth+1,new ArrayList<MOVE>());
	            		childNode.add(n,temp);
	            		queue.add(childNode);
	            	}
	        	}                	
	        }
	        ArrayList<Integer> scores = new ArrayList<Integer>();
	        for(int i=0; i<list.size(); i++){
	        	scores.add(list.get(i).getGame().getScore());
	        }
	        int max= scores.get(0);
	        int index = 0;
	        for(int i=0; i<scores.size(); i++){
	        	if(scores.get(i)> max){
	        		max = scores.get(i);
	        		index = i;
	        	}	
	        }
	        moves = list.get(index).getMoveList();	
		}
        MOVE curMove;
        curMove = moves.get(0);
//        System.out.println("Moves in the List: ");
//        for(int i = 0; i< moves.size(); i++){
//        	System.out.print(moves.get(i)+ ",");
//        }
//        System.out.println("\nMove that is being extracted from the list: "+ moves.get(0));
        moves.remove(0);
        return curMove;        
	}
}

