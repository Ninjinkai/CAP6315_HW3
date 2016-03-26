import java.util.Random;

public class Influence
{
    public Influence(Double[][] matrix)
    {
            for(int i = 0; i < matrix.length; i++) RunSimulation(matrix, i);
    }

    public void RunSimulation(Double[][] matrix, int startnode)
    {
        System.out.println("Start Node : " + startnode);
        Double[] activenodes = CreateArray(matrix.length);
        Double[] currentnodes = CreateArray(matrix.length);
        Double[] currentneighbors;
        currentnodes[startnode] = 1.0;
        do {
            currentneighbors = AddAllNeighbours(matrix, currentnodes, activenodes);
            System.out.println(">> Active Nodes : " + DisplayList(activenodes));
            System.out.println(">> Current Nodes : " + DisplayList(currentnodes));
            System.out.println(">> Current Neighbors : " + DisplayList(currentneighbors));
            Double[][] result = Calculate(matrix,activenodes,currentnodes);
            currentnodes = result[0];
            activenodes = result[1];

        }while(CountArray(currentnodes) != 0);
        currentneighbors = AddAllNeighbours(matrix, currentnodes, activenodes);
        System.out.println(">> Active Nodes : " + DisplayList(activenodes));
        System.out.println(">> Current Nodes : " + DisplayList(currentnodes));
        System.out.println(">> Current Neighbors : " + DisplayList(currentneighbors));
        System.out.println("Active Node Count : " + CountArray(activenodes) + "\n");
    }

    public Double[] AddAllNeighbours(Double[][] matrix, Double[] currentnodes, Double[] activenodes)
    {
        Double[] neighbors = CreateArray(matrix.length);
        for(int i = 0; i < matrix.length; i++)
        {
            if(currentnodes[i] > 0)
            {
                for(int j = 0; j < matrix.length; j++) if(matrix[i][j] > 0)
                {
                    if (currentnodes[j] > 0) neighbors[j] = 0.0;
                    else if (activenodes[j] > 0) neighbors[j] = 0.0;
                    else neighbors[j] = 1.0;
                }
            }
        }
        return neighbors;
    }

    public Double[] GetNeighbours(Double[][] matrix, int index)
    {
        return matrix[index];
    }

    public Double[][] Calculate(Double[][] matrix, Double[] activenodes, Double[] currentnodes)
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
                            System.out.println(">>>> " + i + " -> " + j + " Successful");
                            temp[j] = 1.0;
                        } else {
                            System.out.println(">>>> " + i + " -> " + j + " Unsuccessful");
                        }
                    }
                }
            }
        }
        result[0] = temp;
        result[1] = activenodes;
        return result;
    }

    public Double[] CreateArray(int size)
    {
        Double[] array = new Double[size];
        for(int i = 0; i < size; i++) array[i] = 0.0;
        return array;
    }

    public String DisplayList(Double[] list)
    {
        String result = "";
        for (int i = 0; i < list.length; i++) if(list[i] > 0) result += i + ", ";
        if (result.length() < 3) return "-";
        else return result.substring(0, result.length() - 2);
    }

    public int CountArray(Double[] array)
    {
        int result = 0;
        for (int i = 0; i < array.length; i++) result += array[i];
        return result;
    }
}
