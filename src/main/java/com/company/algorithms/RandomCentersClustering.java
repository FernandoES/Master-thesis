package com.company.algorithms;

import com.company.*;

/*
Random group selection algorithm that decide group centers randomly and assign every class to the group of its closest
center.
 */
public class RandomCentersClustering extends Algorithm {



    public ClassValues[] computeClassAdscription(ClassValues [] classes){
        double[][] metricValues = extractMetricsFromClass(classes);
        double[][] centers = getRandomCenters(metricValues);
        int[] classAdscription = assignToClosestCenter(centers,metricValues);
        for (int i = 0; i <classAdscription.length ; i++) {
            classes[i].GroupAdscription = classAdscription[i];
        }

        return classes;
    }
}
