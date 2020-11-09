import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * This class builds forest based on trees made from DecTree.java 
 * One can build tree with any % data they want to use.
 * One can also perform n-cross validation either with all available features
 * or by using only selected features and exclude one at a time.  
 * @author Hyejin Jenny Yeon
 */
public class DecForest {
	private int numTrees;
	private int numAttr;
	//TODO: make trees private and only use getForest() in HW4.java. 
	public ArrayList<DecTree> trees; // Forest's trees are here
	private int numberofTrainingData;
	private double percentage;
	private List<ArrayList<Integer>> fullTrainData;
	private List<ArrayList<Integer>> fullTestData;
	private ArrayList<Integer> featureList;
	
	/**
	 * Constructor for using all availble featrues
	 * @param numTrees
	 * @param percentageData
	 * @param filePath
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public DecForest(int numTrees, double percentageData, String filePath) throws FileNotFoundException, IOException {
		this.featureList = null;
		this.percentage = percentageData;
		this.numAttr = 6;
		trees = new ArrayList<DecTree>();
		this.numTrees = numTrees;
		fullTrainData = DataParser.parseTrainingRecords(filePath);
		fullTestData = DataParser.parseTestRecords(filePath);
		this.numberofTrainingData = (int) Math.ceil(this.fullTestData.size()*percentageData);
		int sizeOfData = fullTrainData.size();
		for (int t = 0 ; t < numTrees ; t++) {
			Set<Integer> set = new HashSet<Integer>();
			Random r = new Random();
			while (set.size() < (sizeOfData - numberofTrainingData)) {
			    set.add(r.nextInt(numberofTrainingData));
			}
			List<ArrayList<Integer>> subTrainData = new ArrayList<ArrayList<Integer>>();
			for (Integer index : set) {
				subTrainData.add(this.fullTrainData.get(index));
			}

			trees.add(new DecTree(subTrainData));			
		}
	}
	
	/**
	 * Constructor for using only selected features. If forest is built with 
	 * this constructor, then each tree is built
	 * with one feature excluded at a time. 
	 * @param percentageData
	 * @param filePath
	 * @param features
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public DecForest(double percentageData, String filePath, ArrayList<Integer> features) throws FileNotFoundException, IOException {
		this.featureList = features;
		this.numAttr = features.size()-1;
		this.percentage = percentageData;
		trees = new ArrayList<DecTree>();
		this.numTrees = features.size();
		fullTrainData = DataParser.parseTrainingRecords(filePath);
		fullTestData = DataParser.parseTestRecords(filePath);
		this.numberofTrainingData = (int) Math.ceil(this.fullTestData.size()*percentageData);
		int sizeOfData = fullTrainData.size();
		for (int t = 0 ; t < numTrees ; t++) {
			Set<Integer> set = new HashSet<Integer>();
			Random r = new Random();
			while (set.size() < (sizeOfData - numberofTrainingData)) {
			    set.add(r.nextInt(numberofTrainingData));
			}
			List<ArrayList<Integer>> subTrainData = new ArrayList<ArrayList<Integer>>();
			for (Integer index : set) {
				subTrainData.add(this.fullTrainData.get(index));
			}
			int removedFeature = this.featureList.get(t);
			this.featureList.remove(t);
			ArrayList<Integer> treefreature = new ArrayList<Integer>();
			for (Integer f : this.featureList) {
				treefreature.add(f);
			}			
			trees.add(new DecTree(subTrainData,treefreature));	
			this.featureList.add(t, removedFeature);
	
		}
	}
	
	/**
	 * This supports forest built with either all features or sub-features. 
	 * @param fold
	 * @param originalFilePath
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public ArrayList<Integer> crossValidation(int fold, String originalFilePath) throws FileNotFoundException, IOException{
		int sizeOfSet = this.fullTrainData.size()/fold;
		ArrayList<Integer> results = new ArrayList<Integer>();
		this.fullTestData = DataParser.parseTestRecords(originalFilePath);
		for (int i = 0 ; i < fold ; i ++) {
			this.trees = new ArrayList<DecTree>();
			List<ArrayList<Integer>> trainingSubset = new ArrayList<ArrayList<Integer>>();
			List<ArrayList<Integer>> testSubset = new ArrayList<ArrayList<Integer>>();
			if (i == (fold - 1)) {
				trainingSubset = this.fullTrainData.subList(0, sizeOfSet*i);
				testSubset = this.fullTestData.subList(sizeOfSet*i, this.fullTrainData.size());
			}
			else {
				trainingSubset = this.fullTrainData.subList(sizeOfSet*(i+1),this.fullTrainData.size());
				testSubset = this.fullTestData.subList(sizeOfSet*i, sizeOfSet*(i+1));
			}
			this.numberofTrainingData = (int) Math.ceil(trainingSubset.size()*this.percentage);
				// Build Forest
				for (int t = 0 ; t < this.numTrees ; t++) {
	
					int sizeOfData = trainingSubset.size();
					Set<Integer> set = new HashSet<Integer>();
					Random r = new Random();
					while (set.size() < numberofTrainingData) {
					    set.add(r.nextInt(sizeOfData));
					}
					List<ArrayList<Integer>> subTrainData = new ArrayList<ArrayList<Integer>>();
					for (Integer index : set) {
						subTrainData.add(this.fullTrainData.get(index));
					}
					
					if (this.featureList == null) this.trees.add(new DecTree(subTrainData));
					else {
						int removedFeature = this.featureList.get(t);
						this.featureList.remove(t);
						ArrayList<Integer> treefreature = new ArrayList<Integer>();
						for (Integer f : this.featureList) {
							treefreature.add(f);
						}
						trees.add(new DecTree(subTrainData,treefreature));
						this.featureList.add(t, removedFeature);
					}
				}
				ArrayList<ArrayList<Integer>> partialResults = new ArrayList<ArrayList<Integer>>();
				for (DecTree t : this.trees) {
					t.processTestData(testSubset);
					//t.testData = testSubset;
					partialResults.add(t.testResult());
				}
				for (int test = 0 ; test < partialResults.get(0).size() ; test ++) {
					int countfortwo=0;
					int countforfour=0;
					for (ArrayList<Integer> re : partialResults) {
						if (re.get(test) == 2) countfortwo++;
						else countforfour++;
					}
					if(countfortwo > countforfour) results.add(2);
					else results.add(4);
				}	
			}
		return results;
	}

	/**
	 * Prints accuracy in the format of "705 correct out of 887"
	 * This supports forest built with either all features or sub-features. 
	 * @param fold
	 * @param originalFilePath
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void printCVAccuracy(int fold, String originalFilePath) throws FileNotFoundException, IOException {
		ArrayList<Integer> result = this.crossValidation(fold, originalFilePath);
		this.fullTrainData = DataParser.parseTrainingRecords(originalFilePath);
		int count = 0;
		int dataSize = this.fullTrainData.size();
		for (int i = 0 ; i < dataSize ; i++) {
			if (this.fullTrainData.get(i).get(6) == result.get(i)) count++;
		}
		System.out.println(count + " correct out of " + dataSize);
	}
	
	/**
	 * This method takes a test data as a file and build a list containing
	 * predicted label. 
	 * Test data format: (any numer, x1, x2, .. , xn) where xi's are values of the feature.
	 * @param filePath
	 * @return lables predicted by the tree
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public ArrayList<Integer> testResult(String filePath) throws FileNotFoundException, IOException{
		this.fullTestData = DataParser.parseTestRecords(filePath);
		ArrayList<Integer> results = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> partialResults = new ArrayList<ArrayList<Integer>>();
		for (DecTree t : this.trees) {
			t.testData = this.fullTestData;					
					partialResults.add(t.testResult());
		}
		for (int test = 0 ; test < partialResults.get(0).size() ; test ++) {
			int countfortwo=0;
			int countforfour=0;
			for (ArrayList<Integer> re : partialResults) {
				if (re.get(test) == 2) countfortwo++;
				else countforfour++;
			}
			if(countfortwo > countforfour)results.add(2);
			else results.add(4);
		}

		return results;
	}
	
	/**
	 * Getter method for forest
	 * @return forest = list of trees
	 */
	public ArrayList<DecTree> getForest(){
		return this.trees;
	}

}
