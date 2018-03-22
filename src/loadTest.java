import com.oracle.tools.packager.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created by wodzu on 20.10.2017.
 */
public class loadTest {
    static class Coordinates
    {
        Coordinates(){
            this.x = 0;
            this.y = 0;
        }
        public int x;
        public int y;
    };

    static int[][] parseXML(String pathname) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(new File(pathname)));
        String sCurrentLine;
        ArrayList<Coordinates> coordinates = new ArrayList<>();
        boolean next= false, next1 = false, next2 = false, next3 = false, tsp = false;
        int k = 0, l = 0;
        int[][] matrix = new int[0][];
        int citiesCount = 0, counter  = 0;
        while ((sCurrentLine = br.readLine()) != null) {
            String[] splitedLine = sCurrentLine.split(" ");
            for(String character : splitedLine){
                if(character.length()!=0){
                    if(next2){
                        tsp = character.equals("TSP");
                        next2 = false;
                        break;
                    }
                    else if(character.equals("TYPE:")){
                        next2 = true;
                    }
                    if(next) {
                        citiesCount = Integer.valueOf(character);
                        break;
                    }
                    else if(character.equals("DIMENSION:"))
                        next = true;
                    if(next1){
                            if(!tsp && counter<citiesCount*citiesCount){
                                matrix[counter/citiesCount][counter%citiesCount]=Integer.valueOf(character);
                                ++counter;
                            }
                            else if(tsp && k<citiesCount && l<citiesCount){
                                matrix[l][k] = Integer.valueOf(character);
                                matrix[k][l] = Integer.valueOf(character);
                                if(k==l){
                                    ++l;
                                    k=0;
                                }
                                else
                                    ++k;
                            }

                    }
                    if(next3){
                        if(character.equals("EOF"))
                            break;
                        else if(Integer.valueOf(character)-counter==1) {
                            ++counter;
                            coordinates.add(new Coordinates());
                        }
                        else {
                            Coordinates coordinate = coordinates.get(coordinates.size() - 1);
                            if (coordinate.x ==0)
                                coordinate.x = Integer.valueOf(character);
                            else if(coordinate.y == 0)
                                coordinate.y = Integer.valueOf(character);
                        }

                    }
                    else if(character.equals("EDGE_WEIGHT_SECTION"))
                        next1 = true;
                    else if(character.equals("NODE_COORD_SECTION"))
                        next3 = true;
                }
            }
            if (next){
                matrix = new int[citiesCount][citiesCount];
                next = false;
            }
        }
        if(next3)
            matrix = calculateCoordinates(coordinates, matrix);
        return matrix;
    }

    static int[][] calculateCoordinates(ArrayList<Coordinates>coordinates, int[][] matrix){
        for(int i = 0; i<matrix.length; ++i){
            for(int j = 0; j<matrix.length; ++j){
                double delta_x = abs(coordinates.get(i).x - coordinates.get(j).x);
                double delta_y = abs(coordinates.get(i).y - coordinates.get(j).y);
                matrix[i][j] =  (int)sqrt(pow(delta_x, 2) + pow(delta_y, 2));
            }
        }
        return matrix;
    }
    static int[][] changeTableSize(int[][] input, int size){
        if(size>input.length)
            return null;
        int output[][] = new int [size][size];
        for(int i = 0; i<size; ++i) {
            for (int j = 0; j < size; ++j) {
                output[i][j] = input[i][j];
            }
        }
        return output;
    }
}
