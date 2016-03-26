package hw3_5;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Influence_Modeling {
	
	public static String Influence(Double[][] matrix, double p)
    {
		String output = "";
		
		scalarMultiply(matrix, p);
		
		output += "Influence parameter: " + p + "\n";
		output += printMatrix(matrix) + "\n";
		
            for(int i = 0; i < matrix.length; i++) {
            	output += "Starting node: " + (i + 1) + "\n";
            	double avg = 0;
            	
            	for (int j = 1; j <= 10; j++) {
            		avg += RunSimulation(matrix, i);
            	}
            	
            	output += "Average number of nodes influenced: " + avg/10.00 +"\n\n";
            }
        return output;
    }
	
	public static double RunSimulation(Double[][] matrix, int startnode)
    {
        Double[] activenodes = CreateArray(matrix.length);
        Double[] currentnodes = CreateArray(matrix.length);
        Double[] currentneighbors;
        currentnodes[startnode] = 1.00;
        do {
            currentneighbors = AddAllNeighbors(matrix, currentnodes, activenodes);
            Double[][] result = Calculate(matrix,activenodes,currentnodes);
            currentnodes = result[0];
            activenodes = result[1];

        }while(CountArray(currentnodes) != 0);
        
        currentneighbors = AddAllNeighbors(matrix, currentnodes, activenodes);
        return CountArray(activenodes);
    }

    public static Double[] AddAllNeighbors(Double[][] matrix, Double[] currentnodes, Double[] activenodes)
    {
        Double[] neighbors = CreateArray(matrix.length);
        for(int i = 0; i < matrix.length; i++)
        {
            if(currentnodes[i] > 0)
            {
                for(int j = 0; j < matrix.length; j++) if(matrix[i][j] > 0)
                {
                    if (currentnodes[j] > 0) neighbors[j] = 0.00;
                    else if (activenodes[j] > 0) neighbors[j] = 0.00;
                    else neighbors[j] = 1.00;
                }
            }
        }
        return neighbors;
    }

    public static Double[] GetNeighbours(Double[][] matrix, int index)
    {
        return matrix[index];
    }

    public static Double[][] Calculate(Double[][] matrix, Double[] activenodes, Double[] currentnodes)
    {
        Double[][] result = {CreateArray(matrix.length), CreateArray(matrix.length)};
        Random rand = new Random();
        Double[] temp = CreateArray(matrix.length);

        for(int i = 0; i < matrix.length; i++) if (currentnodes[i] > 0) activenodes[i] = currentnodes[i];

        for(int i = 0; i < matrix.length; i++)
        {
            if(currentnodes[i] > 0)
            {
                Double[] neighbours = GetNeighbours(matrix, i);
                for (int j = 0; j < matrix.length; j++)
                {
                    double random = rand.nextDouble();
                    if(neighbours[j] > 0 && activenodes[j] == 0)
                    {
                        if (random < matrix[i][j]) {
                            temp[j] = 1.00;
                        }
                    }
                }
            }
        }
        result[0] = temp;
        result[1] = activenodes;
        return result;
    }

    public static Double[] CreateArray(int size)
    {
        Double[] array = new Double[size];
        for(int i = 0; i < size; i++) array[i] = 0.00;
        return array;
    }

    public static String DisplayList(Double[] list)
    {
        String result = "";
        for (int i = 0; i < list.length; i++) if(list[i] > 0) result += i + ", ";
        if (result.length() < 3) return "-";
        else return result.substring(0, result.length() - 2);
    }

    public static int CountArray(Double[] array)
    {
        int result = 0;
        for (int i = 0; i < array.length; i++) result += array[i];
        return result;
    }
    
 // return a zeroed m-by-n matrix
    public static Double[][] zeroMatrix(int n)
    {
        Double[][] C = new Double[n][n];
        for (int i = 0; i < n; i++) {
        	for (int j = 0; j < n; j++) {
        		C[i][j] = 0.00;
        	}
        }
        return C;
    }
    
    public static Double[][] readCSV()
    {
    	Double[][] C = zeroMatrix(34);
    	BufferedReader br = null;
    	String lineStr = "";
    	int s;
    	int t;

    	try {

    		br = new BufferedReader(new FileReader("src/hw3_5/karate_edges.csv"));
    		while ((lineStr = br.readLine()) != null) {

    			String[] edge = lineStr.split(",");
    			if (!edge[0].equalsIgnoreCase("Source")) {
    				
    				s = (int) Double.parseDouble(edge[0]) - 1;
    				t = (int) Double.parseDouble(edge[1]) - 1;
    				C[s][t] = 1.00;
    				C[t][s] = 1.00;
    			}

    		}

    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	} finally {
    		if (br != null) {
    			try {
    				br.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    	
    	return C;
    }
    
//	Print the adjacency matrix to a string
    public static String printMatrix(Double[][] matrix) {
        String output = "";
    	for (int i = 0; i < matrix.length; i++) {
        	for (int j = 0; j < matrix[i].length; j++) {
        		output += matrix[i][j].toString() + " ";
        	}
        	output += "\n";
        }
    	return output;
    }
    
//  Multiply a matrix by a scalar
    public static void scalarMultiply(Double[][] matrix, double p)
    {
    	for (int i = 0; i < matrix.length; i++) {
        	for (int j = 0; j < matrix[i].length; j++) {
        		matrix[i][j] = (matrix[i][j]) * p;
        	}
        }
    }

	public static void main(String[] args) {

		Double[][] matrix = readCSV();
		double p;
		
//		p = 0.05;
//		p = 0.20;
		p = 0.40;
		System.out.println(Influence(matrix, p));
	}

}
