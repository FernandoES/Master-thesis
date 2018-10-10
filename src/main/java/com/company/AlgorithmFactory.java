package com.company;

import java.lang.reflect.*;
import java.util.*;
import com.company.algorithms.*;

public class AlgorithmFactory {

    public Algorithm getAlgorithm(String algorithmName, int numberOfGroups){

        Algorithm algorithm = selectAlgorithm(algorithmName);

        algorithm.NUMBER_OF_GROUPS = numberOfGroups;

        return algorithm;
    }

    public static String[] availableAlgorithms = {"KMeansInternal","KMeansExternal","KMeansWeka","RandomCentersClustering",
            "RandomMembersClustering","RandomMembersClustering","ExpectationMaximization","Cobweb","Hierarchical"};

    private static Algorithm selectAlgorithm(String algorithmName) {
        Map<String, String> availableArrays = new HashMap<String, String>();

        for (int i = 0; i <availableAlgorithms.length ; i++) {
            availableArrays.put(availableAlgorithms[i].toLowerCase() , availableAlgorithms[i]);
        }

        return getAlgorithmByName(availableArrays.get(algorithmName.toLowerCase()));
    }

    private static Algorithm getAlgorithmByName(String objectName){
        try {
            String constructorWithPackage = "com.company.algorithms." + objectName;

            Class algorithmName = Class.forName(constructorWithPackage);
            Constructor constructor = algorithmName.getConstructor();
            Algorithm algorithm = (Algorithm) constructor.newInstance();
            return algorithm;
        }
        catch(Exception e){
            System.out.println("Creation of class went wrong");
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }
}

