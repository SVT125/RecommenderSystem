// Assume that the theta matrix has dimensions numUsers x numMovies.
package recommendersystem;
import java.io.*;
import java.util.*;
import org.apache.commons.math3.linear.*;

/**
 * Implementation of a recommender system based on collaborative filtering.
 * @author James
 */
public class RecommenderSystem {
    boolean[][] rated;
    RealMatrix ratings, features, theta;
    public static void main(String[] args) {
        RecommenderSystem rs = new RecommenderSystem();
        rs.ratings = rs.readData("ratings.txt");
        rs.features = rs.readData("features.txt");
        rs.rated = rs.countRatings(rs.ratings);
        rs.theta = rs.randInitialize(new BlockRealMatrix(rs.ratings.getColumnDimension(),
                rs.ratings.getRowDimension()),3);
        // randomly initialize the unknown examples? implement...
        // execute optimization algorithms
    }
    
    // Returns a boolean 2d array that returns a representation of the rated mat elements.
    private boolean[][] countRatings(RealMatrix mat) {
        int matRows = mat.getRowDimension(), matCols = mat.getColumnDimension();
        boolean[][] ratings = new boolean[matRows][matCols];
        for( int i = 0; i < matRows; i++ ) {
            for( int j = 0; j < matCols; j++ ) {
                if(mat.getEntry(i,j) == -1)
                    ratings[i][j] = false;
                else 
                    ratings[i][j] = true;
            }
        }
        return ratings;
    }
    
    // Reads in the data. 
    private RealMatrix readData(String fileName) {
	List<List<Double>> list = new ArrayList<List<Double>>();
	File f = new File(fileName);
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            while((line = br.readLine()) != null) {
            	List<Double> convertedStrings = new ArrayList<Double>();
		List<String> strings = Arrays.asList(line.split("\\s+"));
		for( int i = 0; i < strings.size(); i++ ) {
                    String result = strings.get(i);
                    if(result.equals('n'))
                        strings.set(i, "-1");
                    convertedStrings.add(Double.parseDouble(result));
                }
				
                list.add(convertedStrings);
            }
        } catch(IOException ioe) {
            System.err.println("IO exception occured.");
        }
	int numExamples = list.size(), numFeatures = list.get(0).size();
	BlockRealMatrix examples = new BlockRealMatrix(numExamples,numFeatures);
	for( int i = 0; i < numExamples; i++ ) {
		Double[] example = new Double[numFeatures];
		list.get(i).toArray(example);
		examples.setRowVector(i,new ArrayRealVector(example));
	}
	return examples;
    }    
    
    // Inserts a random element from [-epsilon,epsilon] in mat.
    private RealMatrix randInitialize(RealMatrix mat, double epsilon) {
        Random r = new Random();
        for( int i = 0; i < mat.getRowDimension(); i++ ) {
            for( int j = 0; j < mat.getColumnDimension(); j++ ) {
                double rand = r.nextDouble() * (2 * epsilon) - epsilon;
                mat.setEntry(i, j, rand);
            }
        }
        return mat;
    }
}
