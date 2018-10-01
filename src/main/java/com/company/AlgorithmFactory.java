package com.company;

public class AlgorithmFactory {

    public Algorithm getAlgorithm(String algorithmName, int numberOfGroups){

        Algorithm algorithm = selectAlgorithm(algorithmName);

        algorithm.NUMBER_OF_GROUPS = numberOfGroups;

        return algorithm;
    }

    private static Algorithm selectAlgorithm(String algorithmName) {
        if (algorithmName.equalsIgnoreCase("KMeansInternal")) {
            return new KMeansInternal();
        }else if (algorithmName.equalsIgnoreCase("KMeansExternal")) {
            return new KMeansExternal();
        }else if (algorithmName.equalsIgnoreCase("KMeansWeka")) {
            return new KMeansWeka();
        }else if (algorithmName.equalsIgnoreCase("RandomCentersClustering")) {
            return new RandomCentersClustering();
        } else if (algorithmName.equalsIgnoreCase("RandomMembersClustering")) {
            return new RandomMembersClustering();
        } else if (algorithmName.equalsIgnoreCase("ExpectationMaximization")) {
            return new ExpectationMaximization();
        } else if (algorithmName.equalsIgnoreCase("Cobweb")) {
            return  new CobwebAlgorithm();
        } else if (algorithmName.equalsIgnoreCase("Hierachical")){
            return new Hierachical();
        }
        else {
            return null;
        }
    }
}

