package pacman.controllers.kenan_akisik;
import pacman.game.Game;
import pacman.game.Constants.MOVE;
import java.util.ArrayList;

public class HillNode {
	private Game game;
	private ArrayList<MOVE> moveList;
	public HillNode(Game game, ArrayList<MOVE> moveList)
	{
		this.game = game;
		this.moveList = moveList;
	}
	public Game getGame()
	{
		return game;
	}
	public ArrayList<MOVE> getMoveList()
	{
		return moveList;
	}
	public ArrayList<MOVE> add(MOVE move,ArrayList<MOVE> moves)
	{
		for(int i=0; i<moves.size(); i++){
			moveList.add(moves.get(i));
		}
		moveList.add(move);

		return moveList;
	 
	}
}
