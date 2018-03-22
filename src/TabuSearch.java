import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by wodzu on 11.12.2017.
 */
abstract public class TabuSearch {
    private int[][] matrix;
    private int numberOfIterations, lifetime, criticalEvents, maxCriticalEvents;
    private ArrayList<Integer> currentPath = new ArrayList<>(), bestPath = new ArrayList<>();
    private int currentPathLength, bestPathLength;
    private int tabuList[][];

    public abstract void giveResults(ArrayList<Integer> bestPath, int bestPathLength, int time);
    TabuSearch(int[][] graphMatrix, int iterations, int lifetime, int maxCriticalEvents){
        long start = System.nanoTime();
        matrix = graphMatrix;
        numberOfIterations = iterations;
        this.lifetime = lifetime;
        this.maxCriticalEvents = maxCriticalEvents;

        initTabu();
        System.out.print("Początek - wartość funkcji celu: "+currentPathLength+"\n");
        solve();
        long end = System.nanoTime();
        long time = (end - start) / 1000;
        System.out.print("Koniec Nr iteracji: "+numberOfIterations+" wartość funkcji celu: "+bestPathLength+"\n");
        giveResults(bestPath, bestPathLength, (int)time/1000);
    }

    private void initTabu(){
        //stworzenie losowej ścieżki
        for (int i = 1; i<matrix.length; ++i)
            currentPath.add(i);

        Collections.shuffle(currentPath);

        currentPathLength = getPathLength(currentPath);

        clearTabuList();

        bestPath = new ArrayList<>(currentPath);
        bestPathLength = currentPathLength;

    }

    private void clearTabuList(){
        tabuList = new int[matrix.length][matrix.length];
        for(int i = 0; i<matrix.length; ++i)
            for(int j = 0; j<matrix.length; ++j)
                tabuList[i][j]=0;
    }

    private void solve(){
        for(int i = 0; i<numberOfIterations; ++i){
            int temporaryCurrentPathLength = currentPathLength;

            currentPath = swapPathElements(currentPath);
            currentPathLength = getPathLength(currentPath);



            for(int j = 0; j< tabuList.length; ++j)
                for(int k = (j+1); k<tabuList.length; ++k)
                    if(tabuList[j][k]>0)
                        tabuList[j][k]--;

            criticalEvents = temporaryCurrentPathLength <currentPathLength ? criticalEvents+1 : 0;

            if(criticalEvents >= maxCriticalEvents){
                Collections.shuffle(currentPath);
                clearTabuList();
            }

            if(currentPathLength<bestPathLength){
                bestPath = new ArrayList<>(currentPath);
                System.out.print("Nr iteracji: "+i+" wartość funkcji celu: "+currentPathLength+"\n");
                bestPathLength = currentPathLength;
            }

        }
    }

    private ArrayList<Integer> swapPathElements(ArrayList<Integer> path){

        ArrayList<Integer> temporaryPath, bestPathForSwap = new ArrayList<>();
        int temporaryPathLength;
        int bestPathForSwapLength = Integer.MAX_VALUE;
        int bestNodeX = 0, bestNodeY = 0;

        for(int i = 0; i<path.size(); ++i)
            for(int j = (i+1); j<path.size(); ++j){
                if(tabuList[i][j]==0){
                    temporaryPath = new ArrayList<>(path);
                    Collections.swap(temporaryPath, i, j);

                    temporaryPathLength = getPathLength(temporaryPath);

                    if(temporaryPathLength < bestPathForSwapLength){
                        bestPathForSwap = new ArrayList<>(temporaryPath);
                        bestPathForSwapLength = temporaryPathLength;
                        bestNodeX = i;
                        bestNodeY = j;
                    }
                }
            }
        tabuList[bestNodeX][bestNodeY] = lifetime;
        return  bestPathForSwap;
    }

    int getPathLength(ArrayList<Integer> path){
        int length = 0;
        int previousNode = 0;

        for(int i =0; i<path.size(); ++i){
            length+=matrix[previousNode][path.get(i)];
            previousNode = path.get(i);
        }
        length += matrix[previousNode][0];
        return length;
    }
}
