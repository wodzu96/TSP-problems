import java.util.ArrayList;

/**
 * @author Aditya Nugroho
 */
public class Held_KarpAlgorithm {

        private ArrayList<Integer> outputArray = new ArrayList<Integer>();
        /*stała wielkość tablicy ze względu na lepszą wydajność (w stosunku do arraylist) przy założeniu żę nie będą dodawane i usuwane elementy*/
        private int npow, N, distances[][], connections[][], path[][];
        public static long time;

        public Held_KarpAlgorithm() {
        }

        public ArrayList<Integer> computeTSP(int[][] graphMatrix, int n) {
            long start = System.nanoTime();

            N = n;
            npow = (int) Math.pow(2, n);
            connections = new int[n][npow];
            path = new int[n][npow];
            distances = graphMatrix;

            int i, j;

            for (i = 0; i < n; i++) {
                for (j = 0; j < npow; j++) {
                    connections[i][j] = -1; //zapełnienie tablicy wartościami aby nie były losowe
                    path[i][j] = -1;
                }
            }


            for (i = 0; i < n; i++) {
                connections[i][0] = graphMatrix[i][0]; //zainicjonowanie połączeń z pustym zestawem. Krok pierwszy w algorytmi Held-Karpa
            }
            int result = tsp(0, npow - 2);
            outputArray.add(0);
            getPath(0, npow - 2);
            outputArray.add(result);

            long end = System.nanoTime();
            time = (end - start) / 1000;
            outputArray.add((int)time/1000);
            outputArray.add(0,0);
            return outputArray;
        }

        private int tsp(int start, int set) {
            int masked, mask, result = -1, temp;
          //  System.out.print("Start: "+start+", set: "+set+"\n");
            if (connections[start][set] != -1) {
                return connections[start][set]; //sprawdznie czy dane połączenie już istnieje w pamięci - cecha harakterystyczna dla programowania dynamicznego
            } else {
                for (int x = 0; x < N; x++) {
                    mask = npow - 1 - (int) Math.pow(2, x);
                    /*W poniższej linijce mamy iloczyn bitowy aby sprawdzić wszystkie opcje ze zbioru połączenia*/
                    masked = set & mask;
                    //System.out.print("Masked: "+masked+", set: "+set+", mask: "+mask+"\n");
                    if (masked != set) {    //jeśli dana opcja istnieje w zbiorze połączeń wykonać poniższy kod
                        temp = distances[start][x] + tsp(x, masked); //przypisanie do zmiennej tymczasowej nowo wygenerowanego połączenia
                      //  System.out.print("Temp: "+temp+"\n");
                        if (result == -1 || result > temp) {
                            result = temp; //przypianie do wyniku najmniejszej zmiennej tymczasowej - odpowiada to połączeniu o najmniejszej wadze
                            path[start][set] = x;   //zapisanie poprzednika
                        }
                    }
                }
                connections[start][set] = result;   //zapisanie najbardziej wydajnego połączenia dla danego miasta z danym zbiorem
                return result;
            }
        }

        private void getPath(int start, int set) {
            if (path[start][set] == -1) {
                return;
            }
            //odtworzenie ścieżki połączeń przez sprawdzenie kolejnych połączeń ze zbioru path - także za pomocą iloczynów bitowych

            int x = path[start][set];
            int mask = npow - 1 - (int) Math.pow(2, x);
            int masked = set & mask;

            outputArray.add(0,x);
            getPath(x, masked);
        }
}
