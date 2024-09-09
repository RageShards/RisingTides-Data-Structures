package tides;

import java.util.*;
import java.util.Arrays;
import java.util.ArrayList;
/**
 * This class contains methods that provide information about select terrains 
 * using 2D arrays. Uses floodfill to flood given maps and uses that 
 * information to understand the potential impacts. 
 * Instance Variables:
 *  - a double array for all the heights for each cell
 *  - a GridLocation array for the sources of water on empty terrain 
 * 
 * @author Original Creator Keith Scharz (NIFTY STANFORD) 
 * @author Vian Miranda (Rutgers University)
 */
public class RisingTides {

    // Instance variables
    private double[][] terrain;     // an array for all the heights for each cell
    private GridLocation[] sources; // an array for the sources of water on empty terrain 

    /**
     * DO NOT EDIT!
     * Constructor for RisingTides.
     * @param terrain passes in the selected terrain 
     */
    public RisingTides(Terrain terrain) {
        this.terrain = terrain.heights;
        this.sources = terrain.sources;
    }

    /**
     * Find the lowest and highest point of the terrain and output it.
     * 
     * @return double[][], with index 0 and index 1 being the lowest and 
     * highest points of the terrain, respectively
     */
    public double[] elevationExtrema() {
        double max = Integer.MIN_VALUE;
        double min = Integer.MAX_VALUE;
        double[] extrema = new double[2];
        /* WRITE YOUR CODE BELOW */
        for(int row = 0; row < terrain.length; row++){
            for(int col = 0; col < terrain[row].length; col++){
                if(terrain[row][col] <= min){
                    min = terrain[row][col];
                }
                else if(terrain[row][col] >= max){
                    max = terrain[row][col];
                }
            }
        }
        extrema[0] = min;
        extrema[1] = max;
        return extrema; // substitute this line. It is provided so that the code compiles.
    }

    /**
     * Implement the floodfill algorithm using the provided terrain and sources.
     * 
     * All water originates from the source GridLocation. If the height of the 
     * water is greater than that of the neighboring terrain, flood the cells. 
     * Repeat iteratively till the neighboring terrain is higher than the water 
     * height.
     * 
     * 
     * @param height of the water
     * @return boolean[][], where flooded cells are true, otherwise false
     */
    public boolean[][] floodedRegionsIn(double height) {
        /* WRITE YOUR CODE BELOW */
        boolean[][] tf = new boolean[terrain.length][terrain[0].length];
        ArrayList<GridLocation> storeFlood = new ArrayList<>(Arrays.asList(sources));
        for(int i=0; i< storeFlood.size(); i++){
            tf[storeFlood.get(i).row][storeFlood.get(i).col] = true;
        }
        

        while(storeFlood.size()>0){
            int sfR = storeFlood.get(0).row;
            int sfC = storeFlood.get(0).col;
            storeFlood.remove(0);
            if(sfR < terrain.length -1){
                
                if((height >= terrain[sfR + 1][sfC]) && (tf[sfR + 1][sfC] == false)){
                    tf[sfR + 1][sfC] = true;
                    GridLocation x = new GridLocation(sfR+1, sfC);
                    storeFlood.add(x);
                }
            }
            if(sfC < terrain[0].length -1){

                if((height >= terrain[sfR][sfC + 1]) && (tf[sfR][sfC + 1] == false)){
                    tf[sfR][sfC + 1] = true;
                    GridLocation y = new GridLocation(sfR, sfC + 1);
                    storeFlood.add(y);
                }
            }
            if(sfR > 0){

                if((height >= terrain[sfR - 1][sfC]) && (tf[sfR - 1][sfC] == false)){
                    tf[sfR - 1][sfC] = true;
                    GridLocation z = new GridLocation(sfR - 1, sfC);
                    storeFlood.add(z);
                }
            }
            if(sfC > 0){

                if((height >= terrain[sfR][sfC - 1]) && (tf[sfR][sfC - 1] == false)){
                    tf[sfR][sfC -1 ] = true;
                    GridLocation a = new GridLocation(sfR, sfC-1);
                    storeFlood.add(a);
                }
            }
        }
        
        return tf; // substitute this line. It is provided so that the code compiles.
    }

    /**
     * Checks if a given cell is flooded at a certain water height.
     * 
     * @param height of the water
     * @param cell location 
     * @return boolean, true if cell is flooded, otherwise false
     */
    public boolean isFlooded(double height, GridLocation cell) {
        
        /* WRITE YOUR CODE BELOW */

        return floodedRegionsIn(height)[cell.row][cell.col]; // substitute this line. It is provided so that the code compiles.
    }

    /**
     * Given the water height and a GridLocation find the difference between 
     * the chosen cells height and the water height.
     * 
     * If the return value is negative, the Driver will display "meters below"
     * If the return value is positive, the Driver will display "meters above"
     * The value displayed will be positive.
     * 
     * @param height of the water
     * @param cell location
     * @return double, representing how high/deep a cell is above/below water
     */
    public double heightAboveWater(double height, GridLocation cell) {
        
        /* WRITE YOUR CODE BELOW */

        return terrain[cell.row][cell.col] - height; // substitute this line. It is provided so that the code compiles.
    }

    /**
     * Total land available (not underwater) given a certain water height.
     * 
     * @param height of the water
     * @return int, representing every cell above water
     */
    public int totalVisibleLand(double height) {
        int landCounter = 0;
        boolean[][] store = floodedRegionsIn(height);
        for(int row = 0; row < terrain.length; row++){
            for(int col = 0; col < terrain[row].length; col++){
                if(store[row][col] == false)
                    landCounter = landCounter + 1;
            }
        }
        return landCounter; // substitute this line. It is provided so that the code compiles.
    } 


    /**
     * Given 2 heights, find the difference in land available at each height. 
     * 
     * If the return value is negative, the Driver will display "Will gain"
     * If the return value is positive, the Driver will display "Will lose"
     * The value displayed will be positive.
     * 
     * @param height of the water
     * @param newHeight the future height of the water
     * @return int, representing the amount of land lost or gained
     */
    public int landLost(double height, double newHeight) {
        
        /* WRITE YOUR CODE BELOW */
        return (int)(totalVisibleLand(height)- totalVisibleLand(newHeight)); // substitute this line. It is provided so that the code compiles.
    }

    /**
     * Count the total number of islands on the flooded terrain.
     * 
     * Parts of the terrain are considered "islands" if they are completely 
     * surround by water in all 8-directions. Should there be a direction (ie. 
     * left corner) where a certain piece of land is connected to another 
     * landmass, this should be considered as one island. A better example 
     * would be if there were two landmasses connected by one cell. Although 
     * seemingly two islands, after further inspection it should be realized 
     * this is one single island. Only if this connection were to be removed 
     * (height of water increased) should these two landmasses be considered 
     * two separate islands.
     * 
     * @param height of the water
     * @return int, representing the total number of islands
     */
    public int numOfIslands(double height) {
        WeightedQuickUnionUF islands = new WeightedQuickUnionUF(terrain.length, terrain[0].length);

        boolean[][] store = floodedRegionsIn(height);
        ArrayList<GridLocation> storeIslands = new ArrayList<>();

        for(int row = 0; row < store.length; row++){
            for(int col = 0; col < store[0].length; col++){

                 Boolean value = store[row][col];
                if(store[row][col] == false){
                if(row < store.length -2){
                    if(value == store[row + 1][col] && value == false){
                        islands.union(new GridLocation(row + 1, col), new GridLocation(row, col));
                    }
                }

                if(col < store[row].length -2){
                    if(value == store[row][col + 1] && value == false){
                        islands.union(new GridLocation(row, col+1), new GridLocation(row, col));
                    }
                }

                if(row > 0){
                    if(value == store[row - 1][col] && value == false){
                        islands.union(new GridLocation(row - 1, col), new GridLocation(row, col));
                    }
                }

                if(col > 0){
                    if(value == store[row][col-1] && value == false){
                        islands.union(new GridLocation(row, col-1), new GridLocation(row, col));
                    }
                }

                if((row < store.length -2) && (col < store[row].length -2)){
                    if(value == store[row + 1][col + 1] && value == false){
                        islands.union(new GridLocation(row + 1, col + 1), new GridLocation(row, col));
                    }
                }

                if((row > 0) && (col > 0)){
                    if(value == store[row - 1][col - 1] && value == false){
                        islands.union(new GridLocation(row - 1, col - 1), new GridLocation(row, col));
                    }
                }
                
                if((col < store[row].length -2) && (row > 0)){
                    if(value == store[row - 1][col + 1] && value == false){
                        islands.union(new GridLocation(row - 1, col + 1), new GridLocation(row, col));
                    }
                }

                if((row < store.length -2) && (col > 0)){
                    if(value == store[row + 1][col - 1] && value == false){
                        islands.union(new GridLocation(row + 1, col - 1), new GridLocation(row, col));
                    }
                }
            }
        }
        }
        


        for(int r = 0; r < store.length; r++){
            for(int c = 0; c < store[0].length; c++){
                if(store[r][c] == false){
                if(!(storeIslands.contains(islands.find(new GridLocation(r, c))))){
                    storeIslands.add(islands.find(new GridLocation(r, c)));
                }
            }
            }
        }

        int numOfIslands = storeIslands.size();

        /* WRITE YOUR CODE BELOW */
        return numOfIslands; // substitute this line. It is provided so that the code compiles.
    }
}
