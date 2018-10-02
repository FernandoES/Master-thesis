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
        }else if (algorithmName.toLowerCase().equalsIgnoreCase("KMeansExternal".toLowerCase())) {
            return new KMeansExternal();
        }else if (algorithmName.toLowerCase().equalsIgnoreCase("KMeansWeka".toLowerCase())) {
            return new KMeansWeka();
        }else if (algorithmName.toLowerCase().equalsIgnoreCase("RandomCentersClustering".toLowerCase())) {
            return new RandomCentersClustering();
        } else if (algorithmName.toLowerCase().equalsIgnoreCase("RandomMembersClustering".toLowerCase())) {
            return new RandomMembersClustering();
        } else if (algorithmName.toLowerCase().equalsIgnoreCase("ExpectationMaximization".toLowerCase())) {
            return new ExpectationMaximization();
        } else if (algorithmName.toLowerCase().equalsIgnoreCase("Cobweb".toLowerCase())) {
            return  new CobwebAlgorithm();
        } else if (algorithmName.toLowerCase().equalsIgnoreCase("Hierarchical".toLowerCase())){
            return new Hierarchical();
        }
        else {
            return null;
        }
    }
}

