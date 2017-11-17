package pacman.controllers.kenan_akisik;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class Perceptron {
	
	public static void main(String[] args) throws NumberFormatException, IOException
	{
		double [][] updatedWeights = neuralNetwork();
		for(int i = 0; i< 4; i++)
		{
			for(int m = 0; m<16; m++)
			{
				if (m==0) System.out.print("{"+updatedWeights[i][m]+",");
				else if (m>0 && m<15) System.out.print(updatedWeights[i][m]+",");
				else System.out.print(updatedWeights[i][m]+"},");
				
			}
			System.out.println();
			
		}	
	}
	public static double[][] neuralNetwork () throws NumberFormatException, IOException
	{
		double [] wR = new double [16];
		double [] wL = new double [16];
		double [] wU = new double [16];
		double [] wD = new double [16];
		int [] actual = new int [4];
		double globalError;
		int iteration;
		int [] output = new int [4];
		double [] values = new double [4];
		double [][] weights = new double [4][16];
		for(int n = 0; n<16; n++)
		{
			wR[n] = Math.random();
			wL[n] = Math.random();
			wU[n] = Math.random();
			wD[n] = Math.random();
		}
		weights[0] = wR;
		weights[1] = wL;
		weights[2] = wU;
		weights[3] = wD;
		
		iteration = 0;
		do
		{
			iteration++;
			globalError = 0;
			BufferedReader br = new BufferedReader(new FileReader("training.txt"));
			String line = null;
			
			while((line = br.readLine()) != null)
			{
				double localError = 0;
				String features[] = line.split(";");
				features[0] = Integer.toString(Integer.parseInt(features[0]));
				if (features[12].equals("false")) features[12] = "2";
				else features[12] = "1";
				if (features[13].equals("false")) features[13] = "2";
				else features[13] = "1";
				if (features[14].equals("false")) features[14] = "2";
				else features[14] = "1";
				if (features[15].equals("false")) features[15] = "2";
				else features[15] = "1";
				
				double featuresToNumbers [] = new double [16];
				for (int z = 0; z<16; z++)
				{
					if((z>3 && z<12) || (z>0 && z<3))
					{
						featuresToNumbers [z] = (Integer.parseInt(features[z]))/10;
					}
					else featuresToNumbers [z] = (Integer.parseInt(features[z]));
					
				}
				featuresToNumbers [1] = featuresToNumbers [1]/10;
				featuresToNumbers [2] = featuresToNumbers [1]/10;
				
				actual = getClass(features[16]);
				
				values = calcValues(featuresToNumbers,weights);

				output = calcOutput(values);
				
				for(int m = 0; m < 4; m++)
				{
					localError += actual[m] - output[m];
				}
				
				if(localError != 0)
				{
					weights = update(weights,values,featuresToNumbers,localError);
					globalError += localError * localError;
				}
			
			}
			br.close();
		}while(globalError != 0 && iteration<=100);
		
		return weights;
	}
	public static int [] getClass(String move)
	{
		int [] output = new int [4];
		
		if(move.equals("RIGHT"))
		{
			output[0] = 1;
			output[1] = 0;
			output[2] = 0;
			output[3] = 0;
		}
		else if (move.equals("LEFT"))
		{
			output[0] = 0;
			output[1] = 1;
			output[2] = 0;
			output[3] = 0;
		}
		else if (move.equals("UP"))
		{
			output[0] = 0;
			output[1] = 0;
			output[2] = 1;
			output[3] = 0;
		}
		else 
		{
			output[0] = 0;
			output[1] = 0;
			output[2] = 0;
			output[3] = 1;
		}
		return output;
	}
	public static double [] calcValues(double [] f,double[][]w)
	{
		double [] values = new double [4];
		double value;

		for (int n = 0; n<4; n++)
		{
			value = 0;
			for(int m = 0; m<16; m++)
			{
				value += w[n][m] * f[m];
			}
			values[n] = value;
	
		}
		return values;
	}
	
	public static int [] calcOutput(double [] values)
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
	public static double [][] update (double [][] weights, double [] values, double [] features, double localError)
	{
		for (int i = 0; i<4; i++)
		{
			for(int n=0; n<16; n++)
			{
				weights[i][n] = weights[i][n] + 0.1*localError*features[n];				
			}
		}
	
		return weights;
	}
	
	public static int getClassIdent (int [] actual)
	{
		int ident = 0;
		for(int i = 0; i<4; i++)
		{
			if(actual[i] == 1)
			{
				ident = i;
			}
				
		}
		return ident;
	}
	public static boolean needsUpdate (int [] u)
	{
		boolean flag = false;
		for(int i =0 ; i<4; i++)
		{
			if(u[i] == -1)
			{
				flag = true;
			}
		}
		return flag;

	}
	
	
	
}
