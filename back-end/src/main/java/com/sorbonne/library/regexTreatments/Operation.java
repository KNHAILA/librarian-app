package com.sorbonne.library.regexTreatments;

import java.util.Arrays;

/**
* The class <code>DFA</code> a DFA generator component.
*
* <p>Created on : 2021-09-29</p>
* 
* @authors	<a href="maedeh.daemi@etu.sorbonne-universite.fr">DAEMI Maedeh</a>
*          
*/

public class Operation {
	public Operation() {}
	
	/**
	 * <p>
	 * This function allow to find the first free index in one specific row of matrix
	 * </p>
	 */
	static public int findFirstFreeIndex(int[][][] matrix, boolean inColumn, int rowNumber) {
		if (inColumn) {
			for (int z = 1; z < matrix.length; z++) {
				if (matrix[z][rowNumber][0] == 0) {
					return z;
				}
			}
		}

		return 0;
	}
	/**
	 * <p>
	 * This function allow to clear the array
	 * </p>
	 */
	static public int[] emptyArray(int[] array) {
		for (int z = 0; z < array.length; z++) {
			array[z] = 0;
		}
		return array;
	}
	/**
	 * <p>
	 * This function allow to check if two arrays are equal
	 * </p>
	 */
	static public boolean equal(int[][] firstElement, int[][] secondElement, int columnSize, int rowSize) {
		for (int i = 0; i < columnSize; i++) {
			for (int j = 0; j < rowSize; j++) {
				if (secondElement[i][j] == firstElement[i][j]) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * <p>
	 * This function allow to check if one array contain another array
	 * </p>
	 */
	public static boolean contains(final int[] arr, final int key) {
		return Arrays.stream(arr).anyMatch(i -> i == key);
	}
	/**
	 * <p>
	 * This function allow to add one array at the end of the another one
	 * </p>
	 */
	public static void add(int[] array1, int[] array2) {

		int freeIndex = 0;
		for (int i = 0; i < RegEx.MATRIX_BLOCK_SIZE; i++) {
			if (array1[i] == 0) {
				freeIndex = i;
				break;
			}
		}
		for (int i = 0; i < RegEx.MATRIX_BLOCK_SIZE - freeIndex; i++) {
			array1[freeIndex + i] = array2[i];
		}
	}
}
