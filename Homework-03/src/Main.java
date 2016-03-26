public class Main
{
    public static void main(String args[])
    {
        Double[][] matrix = {{0.0, 0.2, 0.2},
                             {0.2, 0.0, 0.2},
                             {0.2, 0.2, 0.0}};

        for(int i = 0; i < 10; i++) new Influence(matrix);
    }
}
