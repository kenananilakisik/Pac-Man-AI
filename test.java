package pacman.controllers.kenan_akisik;



import pacman.game.Constants.MOVE;
import pacman.controllers.Controller;
import pacman.game.Game;


public class test extends Controller<MOVE> {
	public MOVE getMove(Game game,long timeDue){
		int [] powerIndices = game.getPowerPillIndices();
		for(int i = 0; i<powerIndices.length; i++){
			System.out.println("x-coordinate: "+ game.getNodeXCood(powerIndices[i])+
					" y-coordinate: " + game.getNodeYCood(powerIndices[i]));
		}
		return MOVE.LEFT;
	}


}
