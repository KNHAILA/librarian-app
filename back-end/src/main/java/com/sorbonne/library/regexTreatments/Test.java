package com.sorbonne.library.regexTreatments;

import java.util.ArrayList;
import java.util.Scanner;

import static com.sorbonne.library.config.Constants.*;

public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}
	
	
	public static void main(String arg[]) {
		String regEx="happies";
		String fileName = "";
		  System.out.println("Welcome to Our First Project");
		    /*if (arg.length!=0) {
		      //regEx = arg[0];
		    } else {*/
		      //Scanner scanner = new Scanner(System.in);
		      //System.out.print("  >> Please enter a regEx: ");
		      //regEx = scanner.next();
		      
		      //Scanner scanner1 = new Scanner(System.in);
		      //System.out.print("  >> Please enter a book name: ");
		      fileName = ABSOLUTE_PATH+BOOKS+"46.txt";
		    //}
		if (regEx.length() < 1) {
			System.err.println("  >> ERROR: empty regEx.");
		} else {
			try {
				RegExTree ret = RegEx.parse(regEx);
				RegEx.calculateFinalStateNumbers(ret);
				NFA.buildNfaMatrix(ret);	
				ArrayList<int[][][]> output = DFA.makeDFA(NFA.matASCI, NFA.matInitTermEps);
				int[][] s=DFA.simplifyNDFA(output);
				//fileName = ".\\testbeds\\"+fileName+".txt";
				System.out.println(RegEx.find("happiest",s, DFA.acceptArray));
				//RegEx.readFile(s, DFA.acceptArray,fileName);
			} catch (Exception e) {
				System.err.println("  >> ERROR: syntax error for regEx \"" + regEx + "\".");
			}
		}
	}
}
