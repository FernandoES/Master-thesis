package com.company;

public abstract class Algorithm {



    /* Some algorithm allow the user to decide the number of groups created, it is determined by this variable*/
    public  int NUMBER_OF_GROUPS ;

    /*
    This method determines the groups to whom every class belongs, according to each algorithm criteria
     */
    public abstract ClassValues[] computeClassAdscription(ClassValues [] classes);

    /*
    The estimated bug proneness of each test class is determined and then compared to its real number of
    bugs
     */
    public void test(ClassValues[] classesToTrain, ClassValues[] classesToTest){

        double[] meanOfBugs = computeTrainingBugMean(classesToTrain, classesToTest);
        double[] bugsEstimationErrorinEachClass = computeError(classesToTest, meanOfBugs);
        double[] squareEstimationError = computeSquareError(classesToTest,meanOfBugs);
        double[] meanErrorInEachGroup = computeEstimationErrorMean( bugsEstimationErrorinEachClass, classesToTest, classesToTrain);
        double[] meanSquareErrorInGroup = computeEstimationErrorMean( squareEstimationError, classesToTest, classesToTrain);
        int[] numberOfMembersInEachGroup = computeNumberOfMembers(classesToTrain, classesToTest);
        double meanError = getMean(meanErrorInEachGroup);
        double meanSquareError =  Math.sqrt(getMean(meanSquareErrorInGroup));

        plotInfo(meanErrorInEachGroup, meanOfBugs, numberOfMembersInEachGroup );
    }

    /*
    Method that computes the mean of the error and the square error of bug computing of a given algorithm
     */
    public double[] computeAlgorithmError(ClassValues[] classesToTrain, ClassValues[] classesToTest){
        double[] meanOfBugs = computeTrainingBugMean(classesToTrain, classesToTest);
        double[] bugsEstimationErrorinEachClass = computeError(classesToTest, meanOfBugs);
        double[] squareEstimationError = computeSquareError(classesToTest,meanOfBugs);
        double[] meanErrorInEachGroup = computeEstimationErrorMean( bugsEstimationErrorinEachClass, classesToTest, classesToTrain);
        double[] meanSquareErrorInGroup = computeEstimationErrorMean( squareEstimationError, classesToTest, classesToTrain);
        double meanError = getMean(meanErrorInEachGroup);
        double meanSquareError =  Math.sqrt(getMean(meanSquareErrorInGroup));
        double[] errors = {meanError,meanSquareError};
        return errors;
    }

    public double computeSingleError(ClassValues[] classesToTrain, ClassValues[] classesToTest){



        double[] meanOfBugs = computeTrainingBugMean(classesToTrain, classesToTest);
        double[] bugsEstimationErrorinEachClass = computeError(classesToTest, meanOfBugs);
        double[] meanErrorInEachGroup = computeEstimationErrorMean( bugsEstimationErrorinEachClass, classesToTest, classesToTrain);
        double meanError = getMean(meanErrorInEachGroup);
        return meanError;
    }

    /*
    The estimated bug proneness of each analyzed class is calculated
     */
    public ClassValues[] apply(ClassValues[] classesToTrain, ClassValues[] classesToEvaluate){

        double[] meanOfBugs = computeTrainingBugMean(classesToTrain, classesToEvaluate);
        for (int i = 0; i <classesToEvaluate.length ; i++) {
            classesToEvaluate[i].numberOfBugs = meanOfBugs[classesToEvaluate[i].GroupAdscription];
        }
        return classesToEvaluate;

    }

    /*
    Mean of values given in the array is computed
     */
    private double getMean(double[] values){
        double mean = 0;
        for (int i = 0; i <values.length ; i++) {
            mean += values[i];
        }
        mean /= values.length;
        return mean;
    }


    /*
    The metrics contained in the class objects are extracted and contained in a double array for utility reasons
     */
    protected static double[][] extractMetricsFromClass(ClassValues[] classes){
        double [][] metricValues = new double[classes.length][classes[0].metricValues.length];
        for (int i = 0; i <metricValues.length ; i++) {
            for (int j = 0; j <metricValues[i].length ; j++) {
                metricValues[i][j] = classes[i].metricValues[j];
            }
        }
        return metricValues;
    }

    /*
    The information achieved in the testing about the algorithm quality and accuracy is shown
     */
    protected  void plotInfo(double[] meanErrorInEachGroup, double[] meanOfBugs, int[] numberOfMembers){
        System.out.println("------------------------------------"+
                "Algorithm: "+this.getClass().getSimpleName());

        for (int i = 0; i <meanOfBugs.length ; i++) {
            System.out.println("Group: "+i+
                    "\nNumber of members: "+numberOfMembers[i]+
                    "\nMean of estimated bugs in group: "+meanOfBugs[i]+
                    "\nMean of error estimation in group: "+meanErrorInEachGroup[i]);
        }

    }

    /*
    The mean of estimation error in bug proneness in every group for the test classes is estimated
     */
    private double[] computeEstimationErrorMean(double[] estimationErrorClass , ClassValues[] classesToTest, ClassValues[] classesToTrain){
        //The clases to be tested are also required since it could be that some groups only have them and should count in the number
        // of groups
        int numberGroups = getNumberOfGroups(classesToTest,classesToTrain);

        double[] meanError = new double[numberGroups] ;
        int[] membersEachGroup = computeNumberOfMembers(classesToTest);
        int[] membersGroupWithEmpty = new int[numberGroups];//This class also include groups with only test classes
        for (int i = 0; i <membersEachGroup.length ; i++) {
            membersGroupWithEmpty[i] = membersEachGroup[i];
        }
        for (int i = 0; i <estimationErrorClass.length ; i++) {
            meanError[classesToTest[i].GroupAdscription] += estimationErrorClass[i];
        }
        for (int i = 0; i <meanError.length ; i++) {
            if(membersGroupWithEmpty[i]> 0){
                meanError[i] /=membersGroupWithEmpty[i];
            }
        }
        return meanError;
    }

    /*
    The bug proneness of each group is computed as the mean of the bugs of the training classes in that group. If some
    group contains no training class the number of bugs is assigned as -1 to show that it can not be estimated in that
    group.
     */
    protected double[] computeTrainingBugMean(ClassValues[] classesToTrain, ClassValues[] classesToTest){

        int numberOfGroups = getNumberOfGroups(classesToTrain, classesToTest);
        double[] bugMeans = new double[numberOfGroups];
        int[] numberOfmembers = new int[numberOfGroups];
        for (int i = 0; i <classesToTrain.length; i++) {
            bugMeans[classesToTrain[i].GroupAdscription] += classesToTrain[i].numberOfBugs;
            numberOfmembers[classesToTrain[i].GroupAdscription]++;
        }
        for (int i = 0; i <numberOfGroups ; i++) {
            if(numberOfmembers[i] !=0){
                bugMeans[i] /= numberOfmembers[i];
            }
            else{
                ProjectTest.EmptyGroups++;
                bugMeans[i] = -1;
            }
        }
        return bugMeans;
    }

    /*
    This two methods are required when an algorithm does not take the number of created group as an input
     */
    public static  int getNumberOfGroups(ClassValues[] classes){
        int maxValue =classes[0].GroupAdscription;
        for (int i = 1; i <classes.length ; i++) {
            if(maxValue < classes[i].GroupAdscription){
                maxValue = classes[i].GroupAdscription;
            }
        }
        maxValue++;
        return maxValue;
    }


    protected static int getNumberOfGroups(ClassValues[] classes1, ClassValues[] classes2){
        ClassValues[] allClasses = ClassValues.concatArrays(classes1, classes2);;
        return getNumberOfGroups(allClasses);
    }

    /*
    The difference between estimated bug proneness and real number of bugs in a class is computed
     */
    double[] computeError(ClassValues[] classes, double[] meanOfBugs){
        double[] error = new double[classes.length];
        for (int i = 0; i <error.length ; i++) {
            error[i] = Math.abs( classes[i].numberOfBugs - meanOfBugs[classes[i].GroupAdscription]);
        }
        return error;
    }

    /*
    The square difference between estimated bug proneness and real number of bugs in a class is computed
     */
    double[] computeSquareError(ClassValues[] classes, double[] meanOfBugs){
        double[] error = new double[classes.length];
        for (int i = 0; i <error.length ; i++) {
            error[i] = Math.pow( classes[i].numberOfBugs - meanOfBugs[classes[i].GroupAdscription], 2);
        }
        return error;
    }


    /*
    Every member is assignated to the group whose center is closer to. This is only useful for some algorithms
     */
    protected int[] assignToClosestCenter(double[][] centers, double[][] values){
        int[] group = new int[values.length];
        double lowestDistance;
        double newDistance;
        for (int i = 0; i < values.length;i++){
            lowestDistance = getSquareDistance(values[i], centers[0]);
            group[i] = 0;
            for(int j= 1; j< centers.length;j++){
                newDistance = getSquareDistance(values[i],centers[j]);
                if(lowestDistance>newDistance){
                    lowestDistance=newDistance;
                    group[i] = j;
                }
            }
        }
        return group;
    }

    /*
    One class is selected randomly to be used as the center of a group.
    A class is used as a center instead as using random values in order to avoid empty groups
     */
    protected double[][] getRandomCenters(double [][] values){
        double[][] randomCenters = new double[NUMBER_OF_GROUPS][values[0].length];
        int[] randomMember = new int[NUMBER_OF_GROUPS];
        boolean repeated;

        for (int i = 0;i<randomCenters.length;i++){
            do {
                randomMember[i] = (int) Math.floor(Math.random() * values.length);
                repeated = false;
                for (int j = i-1; j >= 0 ; j--) {
                    if(randomMember[i] == randomMember[j]){
                        repeated = true;
                    }
                }
            }while(repeated);
            for (int j = 0; j<randomCenters[i].length;j++){
                randomCenters[i][j] = values[randomMember[i]][j];
            }
        }
        return randomCenters;
    }



    protected double getSquareDistance(double[] vector, double[] center){
        double distance = 0;
        for(int i = 0; i< vector.length;i++){
            distance += Math.pow((vector[i]-center[i]),2);
        }
        return distance;
    }

    protected static int[] computeNumberOfMembers(ClassValues[] class1, ClassValues[] class2){
        ClassValues[] allClasses  = ClassValues.concatArrays(class1, class2);
        return  computeNumberOfMembers(allClasses);
    }
    protected static int[] computeNumberOfMembers(ClassValues[] allClasses){
        int numberOfGroups = getNumberOfGroups(allClasses);
        int[] numberOfMembers = new int[numberOfGroups];
        for (int i = 0; i <allClasses.length ; i++) {
            numberOfMembers[allClasses[i].GroupAdscription]++;
        }
        return numberOfMembers;
    }

    /*
    The information of an analysed class in the analysis method is shown
     */
    protected void plot(ClassValues[] classes){
        System.out.println("------------------------------------"+
                "Algorithm: "+this.getClass().getSimpleName());

        for (int i = 0; i <classes.length ; i++) {
            System.out.println("Class name: "+classes[i].className);
            System.out.println("Estimated Bugs: "+classes[i].numberOfBugs);
        }
    }


}
