import java.io.*;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) throws IOException {
        int matrix[][] = loadTest.parseXML("test/rbg323.atsp");
     //   int matrixChangedSize[][] = loadTest.changeTableSize(matrix, 4);
        int matrixChangedSize[][] = matrix;


        runTabu(matrixChangedSize);
       // runHeldKarp(matrix);

    }

    private static void runTabu(int matrix[][]){
/*      TabuSearch tabuSearch = new TabuSearch(matrix, 500   , 100, 10) {
            @Override
            public void giveResults(ArrayList<Integer> bestPath, int bestPathLength, int time) {
                System.out.print("\nŚcieżka dla tabu: 0-");
                for(int i = 0; i<bestPath.size(); ++i){
                    System.out.print(bestPath.get(i));
                    System.out.print("-");
                }
                System.out.print("0\nKoszt: "+bestPathLength);
                System.out.print("\nCzas: "+time+"ms\n");
            }
        };*/
        int iteractions = 500;
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(matrix, 500, 200, 400 , 0.1f) {
            @Override
            public void giveResults(ArrayList<Integer> bestPath, int bestPathLength, long time) {
                System.out.print("\nŚcieżka dla tabu: ");
                for(int i = 0; i<bestPath.size(); ++i){
                    System.out.print(bestPath.get(i));
                    System.out.print("-");
                }
                System.out.print("0\nKoszt: "+bestPathLength);
                System.out.print("\nCzas: "+time+"ms\n");
            }
        };
    }

    private static void runHeldKarp(int matrix [][]){
        Held_KarpAlgorithm engine = new Held_KarpAlgorithm();
        ArrayList<Integer> outputArray = engine.computeTSP(matrix, matrix.length);

        System.out.print("\nŚcieżka held karpa: ");
        for(int i = 0; i<outputArray.size()-2; ++i){
            System.out.print(outputArray.get(i));
            if(i<outputArray.size()-3)
                System.out.print("-");
        }
        System.out.print("\nKoszt: "+outputArray.get(outputArray.size()-2));
        System.out.print("\nCzas: "+outputArray.get(outputArray.size()-1)+"ms");
    }

}
