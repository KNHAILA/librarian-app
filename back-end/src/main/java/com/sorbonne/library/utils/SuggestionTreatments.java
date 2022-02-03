package com.sorbonne.library.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.sorbonne.library.config.Constants.*;


public class SuggestionTreatments {

    public static Set<Integer> suggestion(Set<Integer> books) {
        Set<Integer> suggestion1=new HashSet<>();
        Set<Integer> suggestion2=new HashSet<>();
        try {
            Map<Integer,Set<Integer>>  jaccardGraph = BinarySerialization.loadGraph(new File(ABSOLUTE_PATH+JACCARD+GRAPH+TXT_EXTENSION));
            for(Integer book : books){
                if(!suggestion1.isEmpty() && !suggestion2.isEmpty())
                    break;
                if(suggestion1.isEmpty()) {
                    suggestion1 = jaccardGraph.get(book);
                    continue;
                }
                if(suggestion2.isEmpty())
                    suggestion2=jaccardGraph.get(book);
            }
            if(suggestion1.isEmpty()){
                if( suggestion2.isEmpty()){
                    return new HashSet<>();
                }
                return suggestion2;
            }
            suggestion1.addAll(suggestion2);
            } catch (IOException e) {
            e.printStackTrace();
        }
        return suggestion1;
    }
}
