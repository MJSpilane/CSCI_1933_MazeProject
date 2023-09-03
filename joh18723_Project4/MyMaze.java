// Names:Matthew Johnson
// x500s:joh18723

import java.util.Random;
import java.util.Scanner;

public class MyMaze{
    static Cell[][] maze;
    int startRow;
    int endRow;

    public MyMaze(int rows, int cols, int startRow, int endRow) {
        maze = new Cell[rows][cols];
        for(int i =0;i<rows;i++){
            for(int j = 0;j<cols;j++){
                maze[i][j] = new Cell();
                this.startRow = startRow;
                this.endRow = endRow;
            }
        }
    }

    /* TODO: Create a new maze using the algorithm found in the writeup. */
    public static MyMaze makeMaze() {

        //instantiating the maze using user input and random values
        Scanner s = new Scanner(System.in);
        int cols;
        int rows;
        while (true) { //loop until valid inputs are given
            System.out.println("Enter a number between 5 and 20 (inclusive) for the rows");
            rows = s.nextInt();
            System.out.println("Enter a number between 5 and 20 (inclusive) for the cols");
            cols = s.nextInt();
            if (rows <= 20 && rows >= 5 && cols <= 20 && cols >= 5) { //valid inputs
                break;
            }
            System.out.println("Try again. Make sure that the number of rows and cols are between 5 and 20 (inclusive)");
        }
        Random random = new Random();
        int startRow = random.nextInt(rows);
        int endRow = random.nextInt(rows);
        MyMaze newMaze = new MyMaze(rows, cols, startRow, endRow);
        //generating the maze using the algorithm on the write-up
        Stack1Gen<int[]> stack = new Stack1Gen<>(); //create a stack that will hold indices
        int[] currentIndex = new int[]{startRow, 0}; //create index array for the starting cell
        Cell currentCell;
        Cell rightNeighbor = new Cell();
        Cell leftNeighbor = new Cell();
        Cell upperNeighbor = new Cell();
        Cell lowerNeighbor = new Cell();
        boolean rightValid;
        boolean leftValid;
        boolean upperValid;
        boolean lowerValid;
        stack.push(currentIndex);
        maze[startRow][0].setVisited(true);
        Cell[] validNeighbors;
        int numOfNeighbors;
        while (!stack.isEmpty()) {
            //reset all variables
            rightValid = false;
            leftValid = false;
            upperValid = false;
            lowerValid = false;
            numOfNeighbors=0;
            currentIndex = stack.top();
            currentCell = maze[currentIndex[0]][currentIndex[1]];
            //test for right validity
            if(currentIndex[1]+1 <maze[0].length){
                rightNeighbor = maze[currentIndex[0]][currentIndex[1]+1];
                if(!rightNeighbor.getVisited()){
                    numOfNeighbors+=1;
                    rightValid = true;
                }
            }
            //test for left validity
            if(currentIndex[1]-1 >=0){
                leftNeighbor = maze[currentIndex[0]][currentIndex[1]-1];
                if(!leftNeighbor.getVisited()){
                    numOfNeighbors +=1;
                    leftValid = true;
                }
            }
            //test for upper validity
            if(currentIndex[0]-1 >=0){
                upperNeighbor = maze[currentIndex[0]-1][currentIndex[1]];
                if(!upperNeighbor.getVisited()){
                    numOfNeighbors+=1;
                    upperValid = true;
                }
            }
            //test for lower validity
            if(currentIndex[0]+1 <maze.length){
                lowerNeighbor = maze[currentIndex[0]+1][currentIndex[1]];
                if(!lowerNeighbor.getVisited()){
                    numOfNeighbors+=1;
                    lowerValid = true;
                }
            }
            if(numOfNeighbors == 0){ //no valid neighbors, dead end
                stack.pop();
                continue;
            }
            validNeighbors = new Cell[numOfNeighbors];
            int counter=0;
            if(rightValid){
                validNeighbors[counter] = rightNeighbor;
                counter++;
            }
            if(leftValid){
                validNeighbors[counter] = leftNeighbor;
                counter++;
            }
            if(upperValid){
                validNeighbors[counter] = upperNeighbor;
                counter++;
            }
            if(lowerValid){
                validNeighbors[counter] = lowerNeighbor;
                counter++;
            }
            int randChoice = random.nextInt(numOfNeighbors);
            Cell randomNeighbor = validNeighbors[randChoice];
            //now we have a random neighbor, we have to delete the wall between it and the currentCell and mark it as visited
            randomNeighbor.setVisited(true);
            if (rightValid){ //we might have selected the right neighbor
                if (randomNeighbor.equals(rightNeighbor)){
                    //we have selected the right neighbor
                    int[] rightIndex = new int[]{currentIndex[0], currentIndex[1]+1};
                    stack.push(rightIndex);
                    currentCell.setRight(false);
                    continue;
                }
            }
            if(leftValid){
                if(randomNeighbor.equals(leftNeighbor)){
                    //we have selected the left neighbor
                    int[] leftIndex = new int[]{currentIndex[0], currentIndex[1]-1};
                    stack.push(leftIndex);
                    randomNeighbor.setRight(false);
                    continue;
                }
            }
            if(upperValid){
                if(randomNeighbor.equals(upperNeighbor)){
                    //we have selected the upper neighbor
                    int[] upperIndex = new int[]{currentIndex[0]-1, currentIndex[1]};
                    stack.push(upperIndex);
                    randomNeighbor.setBottom(false);
                    continue;
                }
            }
            if(lowerValid){
                if(randomNeighbor.equals(lowerNeighbor)){
                    //we have selected the lower neighbor
                    int[] lowerIndex = new int[]{currentIndex[0]+1, currentIndex[1]};
                    stack.push(lowerIndex);
                    currentCell.setBottom(false);
                    continue;
                }
            }
        }
        //set the visited attribute of all the cells back to false
        for(int i=0;i<maze.length;i++){
            for(int j=0;j<maze[0].length;j++){
                maze[i][j].setVisited(false);
            }
        }
        return newMaze;
    }

    /* TODO: Print a representation of the maze to the terminal */
    public void printMaze() {
        //create top border
        for(int i=0;i<maze[0].length;i++){
            System.out.print("|---");
        }
        System.out.print("|\n");
        String currentStr;
        boolean [] bottomPattern = new boolean[maze[0].length]; //keeps track of which elements have a bottom wall
        for(int i =0;i<maze.length;i++){//walk through each row of the maze
            currentStr="";
            if(i == startRow){
                currentStr += " ";
            }
            else{
                currentStr += "|";
            }
            for(int j=0;j<maze[0].length;j++){//walk through each element in the row
                //fill in the stars at visited locations
                if(maze[i][j].getVisited()){
                    currentStr += " * ";
                }
                else{
                    currentStr += "   ";
                }
                //fill in the vertical walls
                if(i==endRow){
                    currentStr+=" ";
                }
                else if(maze[i][j].getRight()){
                    currentStr+="|";
                }
                else{
                    currentStr += " ";
                }
                //mark the horizontal walls
                if(maze[i][j].getBottom()){
                    bottomPattern[j] = true;
                }
                else{
                    bottomPattern[j] = false;
                }
            }
            /*
            after we have gone through the entire row and filled in the stars
            and the vertical walls, we create a new line and build the bottom wall for the row
             */
            currentStr+="\n";
            for(int k = 0; k<maze[0].length;k++){
                if(bottomPattern[k]){
                    currentStr+="|---";
                }
                else{
                    currentStr+="|   ";
                }
            }
            currentStr+="|\n";
            System.out.print(currentStr);
        }
    }

    /* TODO: Solve the maze using the algorithm found in the writeup. */
    public void solveMaze() {
        Q1Gen<int[]> queue = new Q1Gen<>();
        int[] currentIndex = new int[]{startRow, 0};
        Cell currentCell = maze[currentIndex[0]][currentIndex[1]];
        queue.add(currentIndex);
        while(queue.length()!=0){
            currentIndex = queue.remove();
            int[] rightIndex = new int[]{currentIndex[0], currentIndex[1]+1};
            int[] leftIndex = new int[]{currentIndex[0], currentIndex[1]-1};
            int[] upperIndex = new int[]{currentIndex[0]-1, currentIndex[1]};
            int[] lowerIndex = new int[]{currentIndex[0]+1, currentIndex[1]};
            currentCell = maze[currentIndex[0]][currentIndex[1]];
            currentCell.setVisited(true);
            if(currentIndex[0] == endRow && currentIndex[1] == maze[0].length-1){
                break; //maze has been solved
            }
            if(currentIndex[1]+1 < maze[0].length){
                Cell rightCell = maze[currentIndex[0]][currentIndex[1]+1];
                if(!currentCell.getRight()){
                    if(!rightCell.getVisited()){
                        queue.add(rightIndex);
                    }
                }
            }
            if(currentIndex[1]-1 >=0){
                Cell leftCell = maze[currentIndex[0]][currentIndex[1]-1];
                if(!leftCell.getRight()){
                    if(!leftCell.getVisited()){
                        queue.add(leftIndex);
                    }
                }
            }
            if(currentIndex[0]-1 >=0){
                Cell upperCell = maze[currentIndex[0]-1][currentIndex[1]];
                if(!upperCell.getBottom()){
                    if(!upperCell.getVisited()){
                        queue.add(upperIndex);
                    }
                }
            }
            if(currentIndex[0]+1 < maze.length){
                Cell lowerCell = maze[currentIndex[0]+1][currentIndex[1]];
                if(!currentCell.getBottom()){
                    if(!lowerCell.getVisited()){
                        queue.add(lowerIndex);
                    }
                }
            }
        }
        printMaze();
    }

    public static void main(String[] args){
        /*Make and solve maze */
        MyMaze newMaze = makeMaze();
        newMaze.solveMaze();
    }
}
