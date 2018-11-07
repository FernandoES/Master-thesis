package com.company.algorithms;

import com.company.*;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.HierarchicalClusterer;
import weka.core.Instances;


/*
The Hierarchical algorithm from the weka library, which is not part of this project, is used to estimate the groups
 */
public class Hierarchical extends Algorithm {



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
            HierarchicalClusterer clusterer = new HierarchicalClusterer();  // new instance of clusterer
            clusterer.setNumClusters(NUMBER_OF_GROUPS);
            ClusterEvaluation eval = new ClusterEvaluation();
            clusterer.buildClusterer(wekaValues);                           // build clusterer
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
