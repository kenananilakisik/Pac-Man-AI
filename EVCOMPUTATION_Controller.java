package pacman.controllers.kenan_akisik;
import java.util.*;
import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
public class EVCOMPUTATION_Controller extends Controller<MOVE> {
	 public static StarterGhosts ghosts = new StarterGhosts();
	 static ArrayList<MOVE> moves = new ArrayList<MOVE>();
	 
		public MOVE getMove(Game game,long timeDue)
		{
			if(moves.isEmpty()){
				ArrayList <MOVE> list = new ArrayList<MOVE>();
				list.add(MOVE.UP);
				list.add(MOVE.DOWN);
				list.add(MOVE.RIGHT);
				list.add(MOVE.LEFT);
				ArrayList <ArrayList<MOVE>> population = new ArrayList <ArrayList<MOVE>>();
				Game gameCopy = game.copy();
				Game gameCopy1 = game.copy();
				for(int i = 0; i <100; i++)
				{
					ArrayList <MOVE> individual = new ArrayList <MOVE>();
					for(int n = 0; n<20; n++)
					{
						MOVE random = list.get(new Random().nextInt(list.size()));
						individual.add(random);
						
					}
					population.add(individual);
				}
				
				for(int i = 0; i <100; i++) // number of repetitions till decide
				{
					for(int n = 0; n<100; n++)
					{
						ArrayList<MOVE> mutated = new ArrayList<MOVE>();
						mutated = mutate(population.get(n),list);
						if(fitness(population.get(n), gameCopy) < fitness(mutated,gameCopy))
						{
							population.set(n, mutated);
						}		
					}
				}
				moves = highestFitness(population, gameCopy1);
			}
			MOVE curMove;
	        curMove = moves.get(0);
	        moves.remove(0);
	        return curMove;        
			
		}
		public int fitness(ArrayList<MOVE> individual, Game game)
		{
			Game gameCopy = game.copy();
			for(int i = 0; i<20; i++)
			{
				gameCopy.advanceGame(individual.get(i), ghosts.getMove(gameCopy, 0));
			}
			return gameCopy.getScore();
		}
		public ArrayList<MOVE> mutate (ArrayList<MOVE> individual, ArrayList<MOVE> moves)
		{
			int randomIndex= new Random().nextInt(10);
			MOVE randomMove = moves.get(new Random().nextInt(moves.size()));
			individual.set(randomIndex, randomMove);
			return individual;
		}
		public ArrayList<MOVE> highestFitness(ArrayList <ArrayList<MOVE>> population, Game gameCopy)
		{
			int temp = -1;
			ArrayList<MOVE> movesList = new ArrayList<MOVE>();
			for(int i = 0 ; i<100; i++)
			{
				if(fitness(population.get(i), gameCopy) > temp)
				{
					temp = fitness(population.get(i), gameCopy);
					movesList = population.get(i);
				}
			}
			return movesList;
		}

}
