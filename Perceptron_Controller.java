package pacman.controllers.kenan_akisik;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class Perceptron_Controller extends Controller<MOVE> {
	public MOVE getMove(Game game,long timeDue)
	{
		double values [] = new double [4];
		int output [] = new int [4];
		int [] powerIndices = game.getPowerPillIndices();
		MOVE curMove = null;
		double updatedWeights [][] = {
				{0.4657781700249076,0.578862085468224,0.8266839928313067,0.15065818028215483,0.9183841454545578,0.17880092164291472,0.6497450774538843,0.6997839591959479,0.17701540063803023,0.18459644206351478,0.8060827799902202,0.34323201695573724,0.8447431399570119,0.5764759305874158,0.6120133104307012,0.9070010347673731},
				{0.41221428237912594,0.03214535412106134,0.44336867575119043,0.3907939438792555,0.6979372716224624,0.1905823790903558,0.47644925228549173,0.46798598817311,0.07153181881443971,0.9084827416306349,0.28272343658535204,0.8634723254073474,0.7138191433971257,0.36261768138507433,0.5910285811898488,0.4848901845171444},
				{0.35574284648260834,0.8542308915179233,0.26475983177535367,0.08656405721589777,0.2718144232509877,0.22006325983344333,0.032879406988211346,0.19142342368079923,0.6136586833376766,0.998089189180114,0.4673600181423011,0.7877524660043855,0.8595501190397741,0.7484482422671637,0.2596238773639755,0.9437795384680637},
				{0.11508432518471068,0.6916108527072745,0.9219378952636594,0.4218718114954719,0.39661708137635576,0.7741907868881321,0.7452142966582121,0.4384820534158138,0.32596664643025286,0.22142570306249187,0.4135353656441263,0.5574816441605813,0.4381997584476717,0.6139587272027892,0.25074678608678125,0.16592033936427575}
		};
		double features [] = new double [16];
		
		features[0] = game.getCurrentLevel();
		features[1] = game.getPacmanCurrentNodeIndex()/10;
		features[2] = game.getNumberOfActivePills()/10;
		features[3] = game.getNumberOfActivePowerPills();
		features[4] = game.getShortestPathDistance(powerIndices[0], game.getPacmanCurrentNodeIndex());
		features[5] = game.getShortestPathDistance(powerIndices[1], game.getPacmanCurrentNodeIndex());
		features[6] = game.getShortestPathDistance(powerIndices[2], game.getPacmanCurrentNodeIndex());
		features[7] = game.getShortestPathDistance(powerIndices[3], game.getPacmanCurrentNodeIndex());
		features[8] = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),
				game.getGhostCurrentNodeIndex(GHOST.BLINKY));
		features[9] = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),
				game.getGhostCurrentNodeIndex(GHOST.PINKY));
		features[10] = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),
				game.getGhostCurrentNodeIndex(GHOST.INKY));
		features[11]= game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),
				game.getGhostCurrentNodeIndex(GHOST.SUE));
		features[12] = 0;
		features[13] = 0;
		features[14] = 0;
		features[15] = 0;
		if (game.isGhostEdible(GHOST.BLINKY) == false) features[12] = 2;
		else features[12] = 1;
		if (game.isGhostEdible(GHOST.PINKY) == false) features[13] = 2;
		else features[13] = 1;
		if (game.isGhostEdible(GHOST.INKY) == false) features[14] = 2;
		else features[14] = 1;
		if (game.isGhostEdible(GHOST.SUE) == false) features[15] = 2;
		else features[15] = 1;
		
		double featuresToNumbers [] = new double [16];
		for (int z = 0; z<16; z++)
		{
			if((z>3 && z<12) || (z>0 && z<3))
			{
				featuresToNumbers [z] = features[z]/10;
			}
			else featuresToNumbers [z] = features[z];
			
		}
		
		values = calcValues(featuresToNumbers, updatedWeights);
		output = calcOutput(values);
		for(int i = 0; i<4; i++)
		{
			System.out.print(values[i]+",");
		}
		System.out.println();
		int [] out0 = {1,0,0,0};
		int [] out1 = {0,1,0,0};
		int [] out2 = {0,0,1,0};
		
		if(Arrays.equals(output,out0)) curMove = MOVE.RIGHT;
		else if (Arrays.equals(output,out1)) curMove = MOVE.LEFT;
		else if (Arrays.equals(output,out2)) curMove = MOVE.UP;
		else curMove = MOVE.DOWN;
		MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
		boolean flag = false;
		for(int i = 0; i<possibleMoves.length;i++)
		{
			if(curMove == possibleMoves[i]) flag = true;
				
		}
		ArrayList <MOVE> list = new ArrayList<MOVE>();
		list.add(MOVE.UP);
		list.add(MOVE.DOWN);
		list.add(MOVE.RIGHT);
		list.add(MOVE.LEFT);
		MOVE random = list.get(new Random().nextInt(list.size()));
		if (flag==true) return curMove;
		else return random;	   
	}
	
	public double [] calcValues(double [] f,double[][]w)
	{
		double [] values = new double [4];

		for (int n = 0; n<4; n++)
		{
			for(int m = 0; m<16; m++)

				values[n] += w[n][m] * f[m];
			
		}
		return values;
	}
	
	public int [] calcOutput(double [] values)
	{
		int [] out = new int [4];
		int count = 0;
		double max = values[0];
		
		for (int i = 0; i<4;i++)
		{
			if(values[i]> max)
			{
				max = values[i];
				count = i;
			}
		}
		
		for(int i = 0; i<4; i++)
		{
			if(i == count)
			{
				out[i] = 1;
			}
			else
			{
				out[i] = 0;
			}
		}
		return out;
	}
	
}
