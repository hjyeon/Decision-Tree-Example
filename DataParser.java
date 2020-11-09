import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * This class contains a static method to parse csv files and 
 * generates an matrix containing all the training data in the file. 
 * Here, a matrix is implemented as a list of double arrays. 
 * Test data format: (any numer, x1, x2, .. , xn) where xi's are values of the feature.
 * @author Hyejin Jenny Yeon
 */
public class DataParser {
	
    /**
     * This method parses csv file and returns a matrix as ArrayList<double[]>.
     * @param filePath is the path of the file. 
     * @return ArrayList<double[]> a matrix containing all the training data
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<ArrayList<Integer>> parseTrainingRecords(String filePath) throws FileNotFoundException, IOException {
    	List<ArrayList<Integer>> data = new ArrayList<ArrayList<Integer>>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = "";
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] stringValues = line.split(",");
            int colSize = stringValues.length;
            Integer[] intValues = new Integer[colSize];
           	for (int i = 1 ; i < colSize-1; i ++) {
           		if (i == 3) {
           			double age = Double.parseDouble(stringValues[3]);
           			age = Math.floor(age);  
           			intValues[2] = (int) Math.floorMod((int)age, 10);
           		}
           		else {
           			intValues[i-1] = Integer.parseInt(stringValues[i]);  
           		}   	
            }
           	double fare = Double.parseDouble(stringValues[colSize-1]);
           	if (intValues[colSize-3]+intValues[colSize-4]!=0) {
               	fare = fare/(intValues[colSize-3]+intValues[colSize-4]);
           	}
           	fare = fare/10;
           	fare = Math.floor(fare);
           	intValues[colSize-2] = (int) fare;
           	if (Integer.parseInt(stringValues[0])==1) {
           		intValues[colSize-1] = 2; //Survived, make it distinct from 0,1
           								  //for an easier distinction from features
           	}
           	else {
               	intValues[colSize-1] = 4; //deceased
           	}

           	ArrayList<Integer> asArray = new ArrayList<Integer>();
           	for (Integer i : intValues) {
           		asArray.add(i);
           	}

            data.add(asArray);
        }
        reader.close();
    return data;
    }
    
    /**
     * This method parses csv file and returns a matrix as ArrayList<double[]>.
     * @param filePath is the path of the file. 
     * @return ArrayList<double[]> a matrix containing all the training data
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<ArrayList<Integer>> parseTestRecords(String filePath) throws FileNotFoundException, IOException {
    	List<ArrayList<Integer>> data = new ArrayList<ArrayList<Integer>>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = "";
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] stringValues = line.split(",");
            int colSize = stringValues.length;
            Integer[] intValues = new Integer[colSize];
           	for (int i = 1 ; i < colSize-1; i ++) {
           		if (i == 3) {
           			double age = Double.parseDouble(stringValues[3]);
           			age = Math.floor(age);  
           			intValues[2] = (int) Math.floorMod((int)age, 10);
           		}
           		else {
           			intValues[i-1] = Integer.parseInt(stringValues[i]);  
           		}   	
            }
           	double fare = Double.parseDouble(stringValues[colSize-1]);
           	if (intValues[colSize-3]+intValues[colSize-4]!=0) {
               	fare = fare/(intValues[colSize-3]+intValues[colSize-4]);
           	}
           	fare = fare/10;
           	fare = Math.floor(fare);
           	intValues[colSize-2] = (int) fare;
           	ArrayList<Integer> asArray = new ArrayList<Integer>();
           	for (Integer i : intValues) {
           		asArray.add(i);
           	}

            data.add(asArray);
        }
        reader.close();
    return data;
    }
    

}
