package pacman.controllers.kenan_akisik;

import java.util.ArrayList;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

public class EV_Strategy extends Controller<MOVE>{
	public static StarterGhosts ghosts = new StarterGhosts();
	public MOVE getMove(Game game,long timeDue)
	{
		ArrayList <MOVE> list = new ArrayList<MOVE>();
		list.add(MOVE.UP);
		list.add(MOVE.DOWN);
		list.add(MOVE.RIGHT);
		list.add(MOVE.LEFT);
		ArrayList <ArrayList<MOVE>> population = new ArrayList <ArrayList<MOVE>>();
		Game gameCopy = game.copy();
		Game gameCopy1 = game.copy();
		for(int i = 0; i <20; i++)
		{
			ArrayList <MOVE> individual = new ArrayList <MOVE>();
			for(int n = 0; n<10; n++)
			{
				MOVE random = list.get(new Random().nextInt(list.size()));
				individual.add(random);
				
			}
			population.add(individual);
		}
		
		for(int repeat = 0; repeat<30; repeat++)
		{
			int parents = new Random().nextInt(10)+1;
			int [] scores = new int[parents];
			ArrayList<ArrayList<MOVE>> offsprings = new ArrayList<ArrayList<MOVE>>();
			for(int i = 0; i<parents;i++)
			{
				offsprings.add(mutate(population.get(i),list));
				scores[i] = fitness(mutate(population.get(i),list), gameCopy);
			}
			int max = scores[0];
			int ind = 0;
			for(int i =0; i<scores.length; i++)
			{
				if(scores[i]>max)
				{
					max = scores[i];
					ind= i;
				}
			}
			population.set(ind, offsprings.get(ind));
		}

		return highestFitness(population, gameCopy1);
	}
	public int fitness(ArrayList<MOVE> individual, Game game)
	{
		Game gameCopy = game.copy();
		for(int i = 0; i<10; i++)
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
	public MOVE highestFitness(ArrayList <ArrayList<MOVE>> population, Game gameCopy)
	{
		int temp = 0;
		MOVE move = MOVE.RIGHT;
		for(int i = 0 ; i<20; i++)
		{
			if(fitness(population.get(i), gameCopy) > temp)
			{
				temp = fitness(population.get(i), gameCopy);
				move = population.get(i).get(0);
			}
		}
		return move;
	}

}
