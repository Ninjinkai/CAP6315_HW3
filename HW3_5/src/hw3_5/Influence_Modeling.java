package hw3_5;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Influence_Modeling
{	
//	Run the influence simulation 10 times for each node and report the average
//	number of nodes activated for each node.
	public static String cascade(Double[][] matrix, double p)
	{
		String output = "";
		double avg;
		
//		Change the adjacency matrix to an activation probability weighted matrix
//		based on the passed influence parameter p.
		scalarMultiply(matrix, p);
		
		output += "Influence parameter: " + p + "\n";
		
            for(int i = 0; i < matrix.length; i++) {
            	
            	output += "Starting node: " + (i + 1) + "\n";
            	avg = 0.0;
            	
            	for (int j = 1; j <= 10; j++) {
            		avg += RunSimulation(matrix, i);
            	}
            	
            	output += "Average number of nodes activated: " + avg/10.00 +"\n\n";
            }
        return output;
    }
	
//	Run an independent cascade model simulation on the network, passed as the matrix
//	parameter, from the starting node, passed as startnode.
	public static double RunSimulation(Double[][] matrix, int startnode)
	{
//		These arrays list nodes that are already active, nodes that activated this iteration,
//		and neighboring nodes that may activate next iteration.
        Double[] activeNodes = zeroArray(matrix.length);
        Double[] currentNodes = zeroArray(matrix.length);
        @SuppressWarnings("unused")
		Double[] currentNeighbors;
        
//      Activate the first node in the array of nodes activated this iteration.
        currentNodes[startnode] = 1.00;
        
//      Each iteration will get a set of adjacent inactive nodes to the set of active nodes,
//      apply the activation probability calculation to that set, then repeat with the newly
//      activated set of nodes.
        do {
            currentNeighbors = addAllNeighbors(matrix, currentNodes, activeNodes);
            Double[][] result = calculate(matrix, activeNodes, currentNodes);
            currentNodes = result[0];
            activeNodes = result[1];
        } while(sumArray(currentNodes) != 0);
        
        currentNeighbors = addAllNeighbors(matrix, currentNodes, activeNodes);
        return sumArray(activeNodes);
    }

//	Create and return an array containing all nodes adjacent to nodes activated in the current iteration.
    public static Double[] addAllNeighbors(Double[][] matrix, Double[] currentNodes, Double[] activeNodes)
    {
        Double[] neighbors = zeroArray(matrix.length);
        
        for(int i = 0; i < matrix.length; i++) {
            if(currentNodes[i] > 0) {
                for(int j = 0; j < matrix.length; j++) {
                	if(matrix[i][j] > 0) {
                		if (currentNodes[j] > 0) {
                			neighbors[j] = 0.00;
                		}
                        else if (activeNodes[j] > 0) {
                        	neighbors[j] = 0.00;
                        }
                        else {
                        	neighbors[j] = 1.00;
                        }
                	}
                }
            }
        }
        return neighbors;
    }

//  Determine which nodes are activated each time a new set of nodes is activated.
    public static Double[][] calculate(Double[][] matrix, Double[] activeNodes, Double[] currentNodes)
    {
        Random rand = new Random();
        Double[][] result = zeroMatrix(matrix.length);
        Double[] temp = zeroArray(matrix.length);
        Double[] neighbors;

//      Fill active nodes array with nodes that were activated this iteration.
        for (int i = 0; i < matrix.length; i++) {
        	if (currentNodes[i] > 0) {
        		activeNodes[i] = currentNodes[i];
        	}
        }

//      For each node activated in this iteration, apply the activation probability
//      to its neighbors.
        for(int i = 0; i < matrix.length; i++)
        {
            if(currentNodes[i] > 0)
            {
                neighbors = matrix[i];
                for (int j = 0; j < matrix.length; j++)
                {
                    double random = rand.nextDouble();
                    if(neighbors[j] > 0 && activeNodes[j] == 0)
                    {
                        if (random < matrix[i][j]) {
                            temp[j] = 1.00;
                        }
                    }
                }
            }
        }
        
//      Return the newly activated nodes along with the already activated nodes for this iteration.
        result[0] = temp;
        result[1] = activeNodes;
        return result;
    }

//  Sum the values inside an array.
    public static int sumArray(Double[] array)
    {
        int result = 0;
        for (int i = 0; i < array.length; i++) {
        	result += array[i];
        }
        return result;
    }
    
//  Create and return a zeroed n-sized array.
    public static Double[] zeroArray(int n)
    {
        Double[] array = new Double[n];
        for(int i = 0; i < n; i++) {
        	array[i] = 0.00;
        }
        return array;
    }
    
//  Create and return a zeroed n-by-n matrix.
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
    
//  Read a .csv file generated by Gephi.  This is set for the karate network with
//  34 nodes, source and target only.  The file karate_edges.csv must match the path in the FileReader.
    public static Double[][] readCSV()
    {
    	Double[][] C = zeroMatrix(34);
    	BufferedReader br = null;
    	String lineStr = "";
    	int s;
    	int t;

    	try {
    		br = new BufferedReader(new FileReader("src/rsc/karate_edges.csv"));
    		
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
    
//	Print the adjacency matrix to a string.
    public static String printMatrix(Double[][] matrix)
    {
        String output = "";
    	for (int i = 0; i < matrix.length; i++) {
        	for (int j = 0; j < matrix[i].length; j++) {
        		output += matrix[i][j].toString() + " ";
        	}
        	output += "\n";
        }
    	return output;
    }
    
//  Multiply a matrix by a scalar.
    public static void scalarMultiply(Double[][] matrix, double p)
    {
    	for (int i = 0; i < matrix.length; i++) {
        	for (int j = 0; j < matrix[i].length; j++) {
        		matrix[i][j] = (matrix[i][j]) * p;
        	}
        }
    }

	public static void main(String[] args)
	{
		try {
			FileWriter writer = new FileWriter("output.txt");
			
//			Run the independent cascade model simulation for different influence parameters.
//			Print output to the text file.
			writer.append(printMatrix(readCSV()));
			writer.append(cascade(readCSV(), 0.05));
			writer.append(cascade(readCSV(), 0.20));
			writer.append(cascade(readCSV(), 0.40));
			
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
