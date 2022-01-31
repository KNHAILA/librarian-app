package com.sorbonne.library.regexTreatments;


/**
* The class <code>DFA</code> a DFA generator component.
*
* <p>Created on : 2021-09-29</p>
* 
* @authors	<a href="maedeh.daemi@etu.sorbonne-universite.fr">DAEMI Maedeh</a>
*           <a href="kaoutar.nhaila@etu.sorbonne-universite.fr">NHAILA Kaoutar</a>
*/

import java.util.ArrayList;
import java.util.Arrays;

public class DFA {

	public DFA() {
	}

	/**
	 * Array which contain each block of DFA
	 */
	;
	private static int[] returnValuesNDFA = new int[RegEx.MATRIX_BLOCK_SIZE];
	/**
	 * Array which contain accept state in final result
	 */
	;
	public static int[] acceptArray = new int[RegEx.MATRIX_SIZE];
	

	/**
	 * <p>
	 * This function allow to convert NFA to DFA
	 * </p>
	 */
	public static ArrayList<int[][][]> makeDFA(int[][][] mainMatrix, int[][][] matInitTermEps) {

		int[][][] DFAASCIImatrix = new int[RegEx.MATRIX_SIZE][256][RegEx.MATRIX_BLOCK_SIZE];
		int[][][] nodesMatrix = new int[RegEx.MATRIX_SIZE + 1][1][RegEx.MATRIX_BLOCK_SIZE];
		int[] blockValue = new int[RegEx.MATRIX_BLOCK_SIZE];
		int[] template = new int[RegEx.MATRIX_BLOCK_SIZE];

		int terminaisonState = findTerminaisonState(matInitTermEps);

		nodesMatrix[0][0] = DFAinitialState(mainMatrix, matInitTermEps);

		for (int j = 0; j < nodesMatrix.length; j++) {
			if (reachedFinalState(nodesMatrix[j][0])) {
				break;
			}

			for (int ascci = 0; ascci < 256; ascci++) {

				Operation.emptyArray(blockValue);
				int index =Operation.findFirstFreeIndex(nodesMatrix, true, 0);

				for (int i = 0; i < nodesMatrix[j][0].length; i++) {
					int DFAnode = nodesMatrix[j][0][i];
					Operation.emptyArray(returnValuesNDFA);
					Operation.emptyArray(template);
					indexNDFA = 0;

					template = traverseNDFA(mainMatrix, matInitTermEps, DFAnode, ascci, false);

					if (template[0] != 0) {
						Operation.add(blockValue, template);
					}
				}

				if (blockValue[0] != 0) {

					if (!Arrays.equals(blockValue, nodesMatrix[index - 1][0])) {

						if (j == 0) {
							Operation.add(nodesMatrix[index][0], blockValue);

							if (Operation.contains(nodesMatrix[j][0], terminaisonState)) {
								acceptArray[j] = 1;
							}

							if (Operation.contains(nodesMatrix[index][0], terminaisonState)) {
								acceptArray[index] = 1;
							}
						} else {
							if (!isRepetitiveState(DFAASCIImatrix, blockValue, ascci)) { // for prevent loop in *
																							// case
								Operation.add(nodesMatrix[index][0], blockValue);

								if (Operation.contains(nodesMatrix[index][0], terminaisonState)) {
									acceptArray[index] = 1;
								}
							}
						}
					}

					Operation.add(DFAASCIImatrix[j][ascci], blockValue);
				}
			}
		}

		NDFALenght = acceptArray.length;
		ArrayList<int[][][]> finalOutput = new ArrayList<int[][][]>();
		finalOutput.add(nodesMatrix);
		finalOutput.add(DFAASCIImatrix);

		return finalOutput;
	}

	/**
	 * <p>
	 * This function allow to compare one array, with one specific column of the matrix
	 * </p>
	 */
	static private boolean isRepetitiveState(int[][][] matrix, int[] number, int ascii) {
		for (int i = 0; i < matrix.length; i++) {
			if (Arrays.equals(matrix[i][ascii], number)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>
	 * This function allow to get initial state to start making DFA
	 * </p>
	 */
	static private int[] DFAinitialState(int[][][] mainMatrix, int[][][] matInitTermEps) {

		int[] output = new int[RegEx.MATRIX_BLOCK_SIZE];
		Operation.add(output, traverseNDFA(mainMatrix, matInitTermEps, 0, 0, true));
		return output;
	}

	/**
	 * <p>
	 * This function allow to find termination state from NFA matrix
	 * </p>
	 */
	static private int findTerminaisonState(int[][][] matInitTermEps) {
		for (int i = 0; i < matInitTermEps.length; i++) {
			if (matInitTermEps[i][1][0] == 1) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * <p>
	 * This function allow to check if DFA reach the final state
	 * </p>
	 */
	static private boolean reachedFinalState(int[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[0] == 0 && array[1] == 0 && array[2] == 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Array which contain states already traversed in each traverse 
	 */
	;
	private static boolean[] chekedStates = new boolean[RegEx.MATRIX_SIZE];
	/**
	 * Boolean which check termination state in each traverse
	 */
	;
	private static boolean achiveTerminaisonState = false;
	/**
	 * Integer which contain DFA length
	 */
	;
	private static int indexNDFA = 0;
	/**
	 * Integer which contain NDF length
	 */
	;
	private static int NDFALenght = 0;

	/**
	 * <p>
	 * This function allow to traverse DFA
	 * </p>
	 */
	private static int[] traverseNDFA(int[][][] mainMatrix, int[][][] matInitTermEps, int cursor, int ascci,
			boolean justCheckEpsilon) {

		if (!justCheckEpsilon) {
			cursor = mainMatrix[cursor][ascci][0]; // main
			chekedStates[cursor] = true;

			if (cursor == 0) {
				return returnValuesNDFA;
			}

			returnValuesNDFA[indexNDFA] = cursor;
			indexNDFA = indexNDFA + 1;
		}

		justCheckEpsilon = true;

		for (int x = 0; x < matInitTermEps[cursor][2].length; x++) {
			int newCursor = matInitTermEps[cursor][2][x];

			if (newCursor == 0 || chekedStates[newCursor] == true) {
				return returnValuesNDFA;
			}

			if (newCursor != 0 && !achiveTerminaisonState) {
				returnValuesNDFA[indexNDFA] = newCursor;
				indexNDFA = indexNDFA + 1;
			}

			traverseNDFA(mainMatrix, matInitTermEps, newCursor, ascci, true);
		}

		return returnValuesNDFA;
	}

	/**
	 * <p>
	 * This function allow to minimize DFA
	 * </p>
	 */
	public static int[][] simplifyNDFA(ArrayList<int[][][]> result) {

		int[][][] nodes = result.get(0);
		int[][][] dfa = result.get(1);

		int j = 0;
		int i;
		int k = 0;

		int l = NDFALenght;
		int[][] retur = new int[l][259];
		for (i = 0; i < l; i++) {
			for (j = 0; j < 255; j++) {
				for (k = 0; k < l; k++) {
					if (dfa[i][j][0] != 0 && Arrays.equals(dfa[i][j], nodes[k][0])) {
						retur[i][j] = k;
						break;
					}
				}
			}
		}
		return retur;
	}
}
