package com.sorbonne.library.regexTreatments;

/**
* The class <code>NFA</code> a NFA generator component.
*
* <p>Created on : 2021-09-29</p>
* 
* @author	<a href="kaoutar.nhaila@etu.sorbonne-universite.fr">NHAILA Kaoutar</a>
*/

public class NFA {

	public NFA() {
	}
	/**
	 * Matrix which contain transition using a character
	 */;
	public int[][][] matASCI = new int[RegEx.MATRIX_SIZE][256][1];
	/**
	 * Matrix which contain initial state, accepted state and epsilons transitions
	 */;
	public int[][][] matInitTermEps = new int[RegEx.MATRIX_SIZE][3][2];
	/**
	 * Number of statesin nfa
	 */;
	public int stateNum = 0;
	/**
	 * Accepted State
	 */;
	public int finalState = -1;
	/**
	 * Alternance Accepted State
	 */;
	public int finalAlternState = -1;
	/**
	 * This attribute is used to determine if a state is the last one or not
	 */;
	public boolean lastState = false;
	/**
	 * This attribute is used to determine what is the last operation
	 */;
	public int lastOperation = -1;

	/**
	 * <p> This function allow to construct a NFA from tree regex </p>
	 * @param regex Tree
	 */
	public int buildNfaMatrix(RegExTree tree) {
		if(tree.getRoot()<256)
		{
			matInitTermEps[stateNum][0][0] = 1;
			matInitTermEps[0][2][0] = 1;
			matInitTermEps[2][1][0] = 1;
			matASCI[1][tree.getRoot()][0]=2;
			
		}else {
			matInitTermEps[stateNum][0][0] = 1;
			processOperation(tree);
			if (!lastState)
				finalState = stateNum;
			matInitTermEps[finalState][1][0] = 1;
		}
		return 1;
	}
	

	/**
	 * <p> This function allow to construct a NFA of the Dot operator </p>
	 * @param regex Tree
	 */
	
	public int processDot(RegExTree tree) {
		for (int i = 0; i < 255; i++)
			matASCI[stateNum][i][0] = stateNum + 1;
		stateNum += 1;
		return 1;
	}
	
	/**
	 * <p> This function allow to construct a NFA of the Concatenation </p>
	 * @param regex Tree
	 */
	
	public int processConcat(RegExTree tree) {
		if (stateNum == 0) {
			matInitTermEps[stateNum][2][0] = stateNum + 1;
			stateNum += 1;
		}
		processOperation((tree.getSubTrees().get(0)));
		if (lastOperation == RegEx.ALTERN) {
			matInitTermEps[finalAlternState][2][0]=stateNum+1;
		}else {
			matInitTermEps[stateNum][2][0] = stateNum + 1;
		}
		if(tree.getSubTrees().get(1).getRoot()==RegEx.ALTERN){
			lastOperation= RegEx.ALTERN;
		}
		else {
			lastOperation=-1;
		}
		stateNum += 1;
	
		if (tree.getSubTrees().get(1).getRoot() == RegEx.ALTERN) {
			lastState = true;
		}else {
			lastState = false;
		}
		processOperation((tree.getSubTrees().get(1)));
		return 1;
	}
	
	/**
	 * <p> This function allow to construct a NFA of The Closure </p>
	 * @param regex Tree
	 */
	public int processEtoile(RegExTree tree) {
		int l = stateNum;
		if (stateNum > 0) {
			matInitTermEps[stateNum][2][0] = ++stateNum;
		} else {
			matInitTermEps[stateNum][2][0] = 1;
			stateNum++;
		}
		processOperation((tree.getSubTrees().get(0)));
		stateNum += 1;
		if (tree.getSubTrees().get(0).getRoot() == RegEx.ALTERN) {
			matInitTermEps[finalAlternState][2][0] = stateNum;
			matInitTermEps[finalAlternState][2][1] = l + 1;
		} else {
			matInitTermEps[stateNum - 1][2][0] = stateNum;
			matInitTermEps[stateNum - 1][2][1] = l + 1;
		}
		matInitTermEps[l][2][1] = stateNum;
		return 1;
	}
	
	/**
	 * <p> This function allow to construct a NFA of the Union </p>
	 * @param regex Tree
	 */
	
	public int processAltern(RegExTree tree) {
		int l = stateNum;
		if (stateNum == 0)
			lastState = true;
		matInitTermEps[stateNum][2][0] = stateNum + 1;
		stateNum++;
		processOperation((tree.getSubTrees().get(0)));
		if (finalState != -1) {
			matInitTermEps[l][2][1] = stateNum + 2;
			matInitTermEps[finalState][2][0] = ++stateNum;
		} else {
			matInitTermEps[l][2][1] = stateNum + 2;
			matInitTermEps[stateNum][2][0] = ++stateNum;
		}
		finalState = stateNum;
		finalAlternState = finalState;
		stateNum++;
		processOperation((tree.getSubTrees().get(1)));
		matInitTermEps[stateNum][2][0] = finalState;
		return 1;
	}
	/**
	 * <p> This function allow to construct a ASCII character </p>
	 * @param regex Tree
	 */
	public int processChar(RegExTree tree)
	{
		matASCI[stateNum][tree.getRoot()][0] = ++stateNum;
		return 1;
	}
	
	/**
	 * <p> This function to map the regexTree and construct the nfa according to regexTree Root  </p>
	 * @param regex Tree
	 */
	public int processOperation(RegExTree tree) {
		if (tree.getRoot() < 256) { return processChar(tree); }
		if (tree.getRoot() == RegEx.DOT) { return processDot(tree); }
		if (tree.getRoot() == RegEx.CONCAT) { return processConcat(tree); }
		if (tree.getRoot() == RegEx.ETOILE) { return processEtoile(tree); }
		if (tree.getRoot() == RegEx.ALTERN) { return processAltern(tree); }
		return 1;
	}
	public  void initializeNFA(){
		matASCI =new int[RegEx.MATRIX_SIZE][256][1];
		matInitTermEps=null;
		matInitTermEps = new int[RegEx.MATRIX_SIZE][3][2];
		int[][][] j = new int[5][5][5];
		j[0][0][0] = 0;
		System.out.println(j[0][0][0]);
		stateNum = 0;
		finalState = -1;
		finalAlternState = -1;
		lastState = false;
		lastOperation = -1;
	}
}
