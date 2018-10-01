package com.company;


public class RandomMembersClustering extends Algorithm {


    public ClassValues[] computeClassAdscription(ClassValues [] classes){
        for (int i = 0; i <classes.length ; i++) {
            classes[i].GroupAdscription = (int)Math.floor(Math.random()*NUMBER_OF_GROUPS);
        }
        return classes;
    }
}
