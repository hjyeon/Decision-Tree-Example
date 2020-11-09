import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
/**
 * This is a class to make various trees or forests. One can also use
 * public methods in classes DecTree.java and DecForest.java such as
 * cross-validation, building tree only using subset of featrues, etc.
 * One can un-comment things to reproduce results shown in the homework 
 * submission.   
 * @author Hyejin Jenny Yeon
 */
public class HW4 {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		ArrayList<Integer> features = new ArrayList<Integer>();
		features.add(1);
		features.add(2);
		features.add(3);
		features.add(4);
		features.add(5);
		features.add(6);
		//Only de-comment one of the two lines below
	//	DecTree test1 = new DecTree("titanic_data.csv", features);
	//	DecTree test1 = new DecTree("titanic_data.csv");
		
	//	System.out.println(DecTree.entropy(239.0/683.0));
	//	System.out.println(DecTree.informationGain(test1.trainData, 0, 2));
	//	System.out.println("--------------------------------");
	//	test1.printTree();
	//	test1.processTestData("myData.csv");
	//	System.out.println("Depth of the non-pruned tree: " + test1.getDepth());
	//	System.out.println("--------------------------------");
	//	test1.pruneTree(13);
	//	System.out.println(test1.testResult().toString());
	//	test1.printAccuracy();
	//	test1.printTree();
	//	System.out.println("Depth of the pruned tree: " + test1.getDepth());
	//	System.out.println(test1.crossValidation(10, "titanic_data.csv").toString());
	//	test1.printCVAccuracy(10, "titanic_data.csv");
	
	////////Random Forest, Problem 4.7 and onward////////
		DecForest forest = new DecForest(5, 0.8,"titanic_data.csv");
		System.out.println(forest.testResult("myData.csv"));
		int treeNumber = 1;
		/*
		for (DecTree t : forest.trees) {
			System.out.println("Tree Number: "+ treeNumber);
			t.printTree();
			System.out.println("=====================================");
			treeNumber++;
		}
		*/
		//ArrayList<Integer> forestCV = forest.crossValidation(10, "titanic_data.csv");
		forest.printCVAccuracy(10, "titanic_data.csv");
		//System.out.println(forest.testResult("myData.csv"));
		DecForest forest2 = new DecForest(0.8,"titanic_data.csv",features);
		treeNumber = 1;
		/*
		for (DecTree t : forest2.trees) {
			System.out.println("Tree Number: The feature x"+ treeNumber+" is excluded.");
			t.printTree();
			System.out.println("=====================================");
			treeNumber++;
		}
		*/
		//System.out.println(forest2.crossValidation(10, "titanic_data.csv"));
		forest2.printCVAccuracy(10, "titanic_data.csv");
		for (DecTree t : forest2.trees) {
			System.out.println("Tree Number: The feature x"+ treeNumber+" is excluded.");
			t.printTree();
			System.out.println("=====================================");
			treeNumber++;
		}
	}
}
	
