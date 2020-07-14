import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.Scanner;

public class Dijkstra{
    
    public static void main(String[] args) throws Exception {
        Dijkstra dijkstra = new Dijkstra();
        Scanner fileInput = openReadFile(); //sets the scanner to read the file as a stream
        if(fileInput == null){
            System.exit(1);
        }

        int numVertex = readNumVertex(fileInput); //just reads the first int
        int startingVertex = readStartVertex(fileInput); //just reads the second int
        
        Vertex[] distances = initDistance(numVertex, startingVertex, dijkstra);//puts each vertex into the array and gives it max distance
        int[][] graph = readInput(fileInput, numVertex); //puts all the weights into a matrix
        fileInput.close(); //closes scanner and by extension the file

        dijkstra.dijkstrasAlgo(graph, distances); //the meat and potatoes
        
        distances[startingVertex - 1].distance = -1;//to match output but maintain correct math on weights
        printVertecies(distances, numVertex);
    }

    public static int readNumVertex(Scanner fileInput){
        return fileInput.nextInt();
    }

    public static int readStartVertex(Scanner fileInput){
        return fileInput.nextInt();
    }

    public static Vertex[] initDistance(int numVertex, int startingVertex, Dijkstra dijkstra){
        Vertex[] distances = new Vertex[numVertex];//array of vertex objects
        for(int i = 0; i < numVertex; i++){
            distances[i] = dijkstra.new Vertex();
        }//constuctor sets distance to int MAX

        distances[startingVertex-1].distance = 0; //-1 cause indexing in code is zero but 1 in output
        return distances;
    }
    
    public static int[][] readInput(Scanner fileInput, int numVertex){
        fileInput.nextInt(); //throw away number of edges cause we use while
        
        int[][] graph = new int[numVertex][numVertex]; //V x V space...YUCK
        while(fileInput.hasNextInt()){
            int source = fileInput.nextInt();
            int dest = fileInput.nextInt();
            int weight = fileInput.nextInt();
            
            graph[source-1][dest-1] = weight;  //for this assignment connections go both ways
            graph[dest-1][source-1] = weight;  //again -1 for indexing
        }
        
        
        return graph;

    }

    public static void printVertecies(Vertex[] distances, int numVertex){
        File outputFile = new File("cop3503-asn2-output-Bicknell-Kenneth.txt");
        try(Formatter output = new Formatter(outputFile)){
            
            output.format("%d\n", numVertex);
            for(int i = 0; i < numVertex; i++){
                output.format("%d %d %d\n", i+1, distances[i].distance, distances[i].parent);
            }
            //formatter is basically like just using System.out for files
            //try with resources is a babe and closes it for us which forces its output to the stream
        } catch(FileNotFoundException e){
            System.err.println("Unable to create output file");
        }
    }

    public void dijkstrasAlgo(int[][] graph, Vertex[] distances){
        int currVertex = findSmallestDistance(distances);
        if (currVertex == -1){//out of vertexes
            return;
        }
        
        distances[currVertex].visited = true;
        updateDistances(currVertex, graph, distances);
        this.dijkstrasAlgo(graph, distances); //recursion!
    }

    private int findSmallestDistance(Vertex[] distances){
        int minIndex = -1;
        int minDistance = Integer.MAX_VALUE;
        
        for(int i = 0; i < distances.length; i++){
            if(distances[i].distance < minDistance && !distances[i].visited){
                minIndex = i;
                minDistance = distances[i].distance;
            }
        }// O(n) isn't great but it sure was simple to implement

        //I wish i learned of TreeSet sooner
        
        return minIndex;

    }

    private void updateDistances(int currVertex, int[][] graph, Vertex[] distances){
        for(int i = 0; i < graph[currVertex].length; i++){
            if(graph[currVertex][i] > 0){//if a link exists
                
                if(distances[currVertex].distance + graph[currVertex][i] < distances[i].distance){  //if the weight through this point is less than the last best
                    distances[i].distance = distances[currVertex].distance + graph[currVertex][i];  //make the weight through this point the new best
                    distances[i].parent = currVertex + 1;                                           //and update the parent

                }
            }
        }
    }

    public static Scanner openReadFile(){
        //scanner with file stream
        try {
            File input = new File("cop3503-asn2-input.txt");
            Scanner fileInput = new Scanner(input);
            return fileInput;
        } catch (FileNotFoundException e) {
            System.out.println("Could not find input file \"cop3503-asn2-input.txt\".");
            return null;
        }
    }

    private class Vertex{
        public int distance;
        public int parent;
        public boolean visited;

        public Vertex(){
            distance = Integer.MAX_VALUE;
            parent = -1;
            visited = false;
        }


    }
}


