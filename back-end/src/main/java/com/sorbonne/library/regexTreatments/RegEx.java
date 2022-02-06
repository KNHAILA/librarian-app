package com.sorbonne.library.regexTreatments;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Exception;

public class RegEx {
	// MACROS
	static final int CONCAT = 0xC04CA7;
	static final int ETOILE = 0xE7011E;
	static final int ALTERN = 0xA17E54;
	static final int PROTECTION = 0xBADDAD;

	static final int PARENTHESEOUVRANT = 0x16641664;
	static final int PARENTHESEFERMANT = 0x51515151;
	static final int DOT = 0xD07;
	static int MATRIX_SIZE = 0;
	static int MATRIX_BLOCK_SIZE = 0;



	// CONSTRUCTOR
	public RegEx() {
	}

	public static void calculateFinalStateNumbers(RegExTree tree)
	{
		MATRIX_SIZE+=2;
		calculateStateNumbers(tree);
		MATRIX_BLOCK_SIZE=MATRIX_SIZE;	
	}
	
	public static void calculateStateNumbers(RegExTree tree)
	{
		if(tree.getRoot()<256)
			MATRIX_SIZE++;
		if(tree.getRoot()==RegEx.ALTERN)
		{
			MATRIX_SIZE+=4;
			calculateStateNumbers(tree.getSubTrees().get(0));
			calculateStateNumbers(tree.getSubTrees().get(1));
		}
		if(tree.getRoot()==RegEx.DOT)
		{
			MATRIX_SIZE++;
		}
		if(tree.getRoot()==RegEx.ETOILE)
		{
			MATRIX_SIZE+=3;
			calculateStateNumbers(tree.getSubTrees().get(0));
		}
		if(tree.getRoot()==RegEx.CONCAT)
		{
			MATRIX_SIZE+=2;
			calculateStateNumbers(tree.getSubTrees().get(0));
			calculateStateNumbers(tree.getSubTrees().get(1));
		}
	}
	
	//FROM REGEX TO SYNTAX TREE
	public static RegExTree parse(String regE) throws Exception {
		//BEGIN DEBUG: set conditionnal to true for debug example
	    if (false) throw new Exception();
	    RegExTree example = exampleAhoUllman();
	    if (false) return example;
	    //END DEBUG
	    
	    ArrayList<RegExTree> result = new ArrayList<RegExTree>();
	   // if(regEx.charAt(i)=='\\' && regEx.charAt(i+1)=='.')
	    int i=0;
	    for (i=0;i<regE.length()-1;i++)
	    	{
	    	if(regE.charAt(i)=='\\' && regE.charAt(i+1)=='.')
	    	{
	    		result.add(new RegExTree(46,new ArrayList<RegExTree>()));
	    		i++;
	    	}else
	    		result.add(new RegExTree(charToRoot(regE.charAt(i)),new ArrayList<RegExTree>()));
	    	}
	    if(i<regE.length())
	    	result.add(new RegExTree(charToRoot(regE.charAt(i)),new ArrayList<RegExTree>()));
	    return parse(result);
	  }
	 

	private static int charToRoot(char c) {
		if (c == '.')
			return DOT;
		if (c == '*')
			return ETOILE;
		if (c == '|')
			return ALTERN;
		if (c == '(')
			return PARENTHESEOUVRANT;
		if (c == ')')
			return PARENTHESEFERMANT;
		return (int) c;
	}

	public static RegExTree parse(ArrayList<RegExTree> result) throws Exception {
		while (containParenthese(result))
			result = processParenthese(result);
		while (containEtoile(result))
			result = processEtoile(result);
		while (containConcat(result))
			result = processConcat(result);
		while (containAltern(result))
			result = processAltern(result);

		if (result.size() > 1)
			throw new Exception();

		return removeProtection(result.get(0));
	}

	private static boolean containParenthese(ArrayList<RegExTree> trees) {
		for (RegExTree t : trees)
			if (t.root == PARENTHESEFERMANT || t.root == PARENTHESEOUVRANT)
				return true;
		return false;
	}

	private static ArrayList<RegExTree> processParenthese(ArrayList<RegExTree> trees) throws Exception {
		ArrayList<RegExTree> result = new ArrayList<RegExTree>();
		boolean found = false;
		for (RegExTree t : trees) {
			if (!found && t.root == PARENTHESEFERMANT) {
				boolean done = false;
				ArrayList<RegExTree> content = new ArrayList<RegExTree>();
				while (!done && !result.isEmpty())
					if (result.get(result.size() - 1).root == PARENTHESEOUVRANT) {
						done = true;
						result.remove(result.size() - 1);
					} else
						content.add(0, result.remove(result.size() - 1));
				if (!done)
					throw new Exception();
				found = true;
				ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
				subTrees.add(parse(content));
				result.add(new RegExTree(PROTECTION, subTrees));
			} else {
				result.add(t);
			}
		}
		if (!found)
			throw new Exception();
		return result;
	}

	private static boolean containEtoile(ArrayList<RegExTree> trees) {
		for (RegExTree t : trees)
			if (t.root == ETOILE && t.subTrees.isEmpty())
				return true;
		return false;
	}

	private static ArrayList<RegExTree> processEtoile(ArrayList<RegExTree> trees) throws Exception {
		ArrayList<RegExTree> result = new ArrayList<RegExTree>();
		boolean found = false;
		for (RegExTree t : trees) {
			if (!found && t.root == ETOILE && t.subTrees.isEmpty()) {
				if (result.isEmpty())
					throw new Exception();
				found = true;
				RegExTree last = result.remove(result.size() - 1);
				ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
				subTrees.add(last);
				result.add(new RegExTree(ETOILE, subTrees));
			} else {
				result.add(t);
			}
		}
		return result;
	}

	private static boolean containConcat(ArrayList<RegExTree> trees) {
		boolean firstFound = false;
		for (RegExTree t : trees) {
			if (!firstFound && t.root != ALTERN) {
				firstFound = true;
				continue;
			}
			if (firstFound)
				if (t.root != ALTERN)
					return true;
				else
					firstFound = false;
		}
		return false;
	}

	private static ArrayList<RegExTree> processConcat(ArrayList<RegExTree> trees) throws Exception {
		ArrayList<RegExTree> result = new ArrayList<RegExTree>();
		boolean found = false;
		boolean firstFound = false;
		for (RegExTree t : trees) {
			if (!found && !firstFound && t.root != ALTERN) {
				firstFound = true;
				result.add(t);
				continue;
			}
			if (!found && firstFound && t.root == ALTERN) {
				firstFound = false;
				result.add(t);
				continue;
			}
			if (!found && firstFound && t.root != ALTERN) {
				found = true;
				RegExTree last = result.remove(result.size() - 1);
				ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
				subTrees.add(last);
				subTrees.add(t);
				result.add(new RegExTree(CONCAT, subTrees));
			} else {
				result.add(t);
			}
		}
		return result;
	}

	private static boolean containAltern(ArrayList<RegExTree> trees) {
		for (RegExTree t : trees)
			if (t.root == ALTERN && t.subTrees.isEmpty())
				return true;
		return false;
	}

	private static ArrayList<RegExTree> processAltern(ArrayList<RegExTree> trees) throws Exception {
		ArrayList<RegExTree> result = new ArrayList<RegExTree>();
		boolean found = false;
		RegExTree gauche = null;
		boolean done = false;
		for (RegExTree t : trees) {
			if (!found && t.root == ALTERN && t.subTrees.isEmpty()) {
				if (result.isEmpty())
					throw new Exception();
				found = true;
				gauche = result.remove(result.size() - 1);
				continue;
			}
			if (found && !done) {
				if (gauche == null)
					throw new Exception();
				done = true;
				ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
				subTrees.add(gauche);
				subTrees.add(t);
				result.add(new RegExTree(ALTERN, subTrees));
			} else {
				result.add(t);
			}
		}
		return result;
	}

	private static RegExTree removeProtection(RegExTree tree) throws Exception {
		if (tree.root == PROTECTION && tree.subTrees.size() != 1)
			throw new Exception();
		if (tree.subTrees.isEmpty())
			return tree;
		if (tree.root == PROTECTION)
			return removeProtection(tree.subTrees.get(0));

		ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
		for (RegExTree t : tree.subTrees)
			subTrees.add(removeProtection(t));
		return new RegExTree(tree.root, subTrees);
	}

	// EXAMPLE
	// --> RegEx from Aho-Ullman book Chap.10 Example 10.25
	private static RegExTree exampleAhoUllman() {
		RegExTree a = new RegExTree((int) 'a', new ArrayList<RegExTree>());
		RegExTree b = new RegExTree((int) 'b', new ArrayList<RegExTree>());
		RegExTree c = new RegExTree((int) 'c', new ArrayList<RegExTree>());
		ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
		subTrees.add(c);
		RegExTree cEtoile = new RegExTree(ETOILE, subTrees);
		subTrees = new ArrayList<RegExTree>();
		subTrees.add(b);
		subTrees.add(cEtoile);
		RegExTree dotBCEtoile = new RegExTree(CONCAT, subTrees);
		subTrees = new ArrayList<RegExTree>();
		subTrees.add(a);
		subTrees.add(dotBCEtoile);
		return new RegExTree(ALTERN, subTrees);
	}

	// Make DFA
	public static String matrixToStringDFA(int[][][] mat) {
		String res = "";
		for (int i = 0; i < MATRIX_SIZE; i++) {
			res += printListInteger(mat[i][97]) + " ";
			res += printListInteger(mat[i][98]) + " ";
			res += printListInteger(mat[i][99]) + " ";
			res += "\n";
		}
		return res;
	}
	
	static public boolean equal(int[][] firstElement, int[][] secondElement, int columnSize, int rowSize) {
		for (int i = 0; i < columnSize; i++) {
			for (int j = 0; j < rowSize; j++) {
				if(secondElement[i][j] == firstElement[i][j]) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	public static void printSimplify(int[][] s) {
		for(int i=0;i<s.length;i++)
		{
			//System.out.print("[ " +s[i][0]+" ]");
			for(int j =97;j<99;j++)
			{
				System.out.print("[ " +s[i][j]+" ]");
			}
			System.out.print("\n");
		}
	} 
	
	public static ArrayList<Integer[]> getValueMatRow(int[][][] mat) {
		ArrayList<Integer[]> rowcolval = new ArrayList<Integer[]>();
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				if (mat[i][j][0] != 0) {

					Integer[] val = new Integer[2];
					val[0] = i;
					val[1] = j;
					rowcolval.add(val);
				}
			}
		}
		return rowcolval;
	}
	

	  public static void readFile(int[][] a, int accep[], String fileLink)
	  {
		  File file = new File(fileLink);
		  BufferedReader br;
		  try {
			  br = new BufferedReader(new FileReader(file));
			  String st;
			  try {
				  while ((st = br.readLine()) != null)
				  {
					  if(find(st, a, accep))
						  System.out.println(st);
				  }
			  } catch (IOException e) {
				  e.printStackTrace();
			  }
		  } catch (FileNotFoundException e) {
			e.printStackTrace();
		  }
	  }
	  
	  
	  
	  public static boolean find(String line, int[][] a, int[] acce)
	  {
		  int i=0;
		  int currentState=0;
		  int stateNumber = a.length;
		  int j;
		  while(i<line.length())
		  {
			  j=0;
			  currentState = i;
			  while(j<stateNumber && i<line.length())
			  {
				  if(acce[j]==1)
					  return true;
				  if((int)line.charAt(i)<0 ||(int)line.charAt(i)>255 || a[j][(int)line.charAt(i)]==0)
					  break ;
				  j = a[j][(int)line.charAt(i)];
				  i++;
			  }
			  i=++currentState;
		  }
		  return false;
	  }
	  public static void printArrayListListInteger(ArrayList<Integer[]> arr, int[][][] mat) {
			for (int i = 0; i < arr.size(); i++) {
				Integer[] row = arr.get(i);
				System.out.println("(" + row[0] + ", " + row[1] + ") : " + printListInteger(mat[row[0]][row[1]]));
			}
		}

		public static String printListInteger(int[] values) {
			String res = "[";
			for (int i = 0; i < values.length; i++) {
				if (values[i] != 0) {
					res += (values[i] + " ");
				}
			}
			res += "]";
			return res;
		}

		public static String matrixToString(int[][][] mat) {
			String res = "";
			for (int i = 0; i < mat.length; i++) {
				for (int j = 0; j < mat[0].length; j++) {
					res += printListInteger(mat[i][j]) + " ";
				}
				res += "\n";
			}
			return res;
		}

	public static void initializeRegEx(){
		MATRIX_SIZE = 0;
		MATRIX_BLOCK_SIZE = 0;
	}

}
