package pacman.controllers.kenan_akisik;

import java.util.ArrayList;
import java.util.Random;
import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class HillClimbing_Controller extends Controller<MOVE> {
	 public static StarterGhosts ghosts = new StarterGhosts();
	 public static ArrayList<MOVE> curList = new ArrayList<MOVE>();
	public MOVE getMove(Game game,long timeDue){
		System.out.println("here1");
		if(curList.isEmpty()){
			System.out.println("here2");
			HillNode current = new HillNode(game, new ArrayList<MOVE>());
			Game gameCopy = game.copy();
			HillNode neighbor = new HillNode(gameCopy, new ArrayList<MOVE>());
			int i = 0;
			while(i != -1){
				System.out.println("here3");
				MOVE [] possibleMoves = current.getGame().getPossibleMoves(current.getGame().getPacmanCurrentNodeIndex());
				Game copy = current.getGame().copy();
				MOVE highestMove = MOVE.RIGHT;
				int max = -1;
				for(MOVE n : possibleMoves){
					copy.advanceGame(n, ghosts.getMove(copy, 0));
					if(copy.getScore()> max){
						System.out.println("here4");
						max = copy.getScore();
						highestMove = n;	
					}
				}
				copy.advanceGame(highestMove, ghosts.getMove(copy, 0));
				neighbor = new HillNode(copy,new ArrayList<MOVE>());
				neighbor.add(highestMove, current.getMoveList());
				if(current.getGame().getScore()> neighbor.getGame().getScore()){
					curList = current.getMoveList();
					i = -1;
					System.out.println(current.getGame().getScore());
					System.out.println(neighbor.getGame().getScore());
				}else{
					System.out.println("here6");
					current = neighbor;
				}
			}
		}
		System.out.println("here7");
		MOVE curMove;
		if(curList.isEmpty()){
			curMove = MOVE.DOWN;
		}else{
			curMove = curList.get(0);
	        curList.remove(0);
		}
        
        return curMove;   

	}
}
