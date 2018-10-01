package com.company;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.Cobweb;
import weka.core.Instances;

public class CobwebAlgorithm extends Algorithm {



    public ClassValues[] computeClassAdscription(ClassValues [] classes){
        Instances wekaValues = Collector.convertClassToWeka(classes );
        int[] classAdscription = getGroupBelogness(wekaValues);
        for (int i = 0; i <classAdscription.length ; i++) {
            classes[i].GroupAdscription = classAdscription[i];
        }

        return classes;
    }



    private int[] getGroupBelogness(Instances wekaValues){

        try {
            Cobweb clusterer = new Cobweb();   // new instance of clusterer
            ClusterEvaluation eval = new ClusterEvaluation();
            clusterer.buildClusterer(wekaValues);                                 // build clusterer
            eval.setClusterer(clusterer);                                   // the cluster to evaluate
            eval.evaluateClusterer(wekaValues);
            double[] clusterAssignments = eval.getClusterAssignments();
            int[] clusterAssignmentsInt = new int[clusterAssignments.length];
            for (int i = 0; i <clusterAssignments.length ; i++) {
                clusterAssignmentsInt[i] = (int)clusterAssignments[i];
            }
            return clusterAssignmentsInt;
        }
        catch(java.lang.Exception e){
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

}
