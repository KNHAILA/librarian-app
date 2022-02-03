package com.sorbonne.library.jaccardTreatments;

import java.io.*;

import static com.sorbonne.library.config.Constants.*;
import java.util.HashMap;
import java.util.Map;

public class JaccardMatrix {

    public HashMap<Integer, HashMap<Integer,Double>> matrix= new HashMap<Integer, HashMap<Integer, Double>>();

    public JaccardMatrix(){

    }


    public void addToMatrix(int line, int column, double value){
        if(matrix.get(line)==null){
            HashMap<Integer,Double> val= new HashMap<Integer, Double>();
            matrix.put(line, val);
        }
        if(matrix.get(column)==null){
            HashMap<Integer,Double> val= new HashMap<Integer, Double>();
            matrix.put(column, val);
        }
        if(matrix.get(line).get(column) == null){
            HashMap<Integer,Double> val= matrix.get(line);
            val.put(column,value);
            matrix.put(line,val);
        }
        if(matrix.get(column).get(line) == null){
            HashMap<Integer,Double> val= matrix.get(column);
            val.put(line,value);
            matrix.put(column,val);
        }
    }

    public double getElemMatrix(int line, int column){
        if(matrix.get(line)!=null && matrix.get(line).get(column)!=null )
            return matrix.get(line).get(column);
        else
            return -1.0;
    }

    public static HashMap<Integer,HashMap<Integer,Double>> readMatrixFromFile() throws IOException, ClassNotFoundException {
        File fileToReadObject=new File(ABSOLUTE_PATH+JACCARD+MATRIX+TXT_EXTENSION);
        HashMap<Integer,HashMap<Integer,Double>> ldapContent;
        FileInputStream fileIn = new FileInputStream(fileToReadObject);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        ldapContent= (HashMap<Integer,HashMap<Integer,Double>>) in.readObject();
        in.close();
        fileIn.close();
        return ldapContent;
    }
}
