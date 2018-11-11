# Guide of use

This program was developed for my final thesis of the Informatik Master in the Hochschule Darmstadt in collaboration with MaibornWolff 

https://www.h-da.de  
https://github.com/MaibornWolff/


## Introduction

In this guide the final user of the program will be guided
both to make use of the program and also to implement new algorithms on
it to test them.  
This program can be used as it comes or can modified to add new
algorithm to be tested.

## Installing procedure

### Clone

The program can be found in the git repository:

> *https://github.com/FernandoES/Master-thesis*

It should be downloaded via terminal by typing in a terminal in the
folder in them the user want to create the project:

> *git clone https://github.com/FernandoES/Master-thesis*

This will download the whole project in a new folder called
\(Master-thesis\).

### Prerequisites

Either before of after cloning the project it has to be checked if the
prerequisites are met. For this project both \(Java Development Kit\)
\[1\] and \(Gradle\) \[2\] are required.

### Run and build

Once the program is rewritten as desired it can be directly run by
\(Gradle\) by typing in the terminal in project folder:

> *.\\gradlew run*

If the user wants to export the program there are two options, really
similar among them. They are:  

> *./gradlew installDist*

and:  

> *./gradlew assembleDist*

Both of them create a folder with all the required files to run the
program. This created files are the same in both commands. Both of them
create a folder called \(Build\) that contains everything required to
run the program. The only difference is that \(assembleDist\) creates a
subfolder of \(Build\) called \(distributions\) which contains two
compressed files, a \(.tar\) and a \(.zip\), each of them contains all
the required files to run the program, with the same structure that
contained in:  

> *./build/install/trainClustering/*

In order to run the newly created program the user has to call:  

> *java -jar ./build/install/trainClustering/lib/trainclustering.jar*

It should be taken into account that after every code change this files
get outdated, since they do not implement it. If the user wants to
implement them, the commands should be run again.

## Adding a new algorithm

If the user want to add a new algorithm to test it, it should be
implemented in a new class written in the folder:

> *./src/main/java/com/company/algorithms*

This class should import the package \(com.company.*\) and extend the
class \(Algorithm\). In order to extend this class the abstract method:

> *public ClassValues\[\] computeClassAdscription(ClassValues \[\]
> classes){}*

This method should create groups and add the number of group each
ClassValues object belongs to in the \(int\) field \(GroupAdscription\).
The selection of which object belongs to which group and also how many
groups are created, should be decided by the new created algorithm.  
Once the new algorithm is created the results will be tested by the test
method, but the implementer should not worry about it, since it is
already implemented by \(Algorithm\) class itself.  
Many useful methods from algorithm class can be used to ease the
implementation of the new algorithm.  
After introducing the algorithm, the user has to add the name of the
created class to the variable \(availableAlgorithms\) of the class
\(AlgorithmFactory\), this will allow the system to know that a new
algorithm was added, it will be shown to the user as an option to be
choosen when starting the program and it will also be automatically
added to the list of default algorithms.

#### Weka algorithms

If the user wants to implement an algorithm that uses weka structure, a
method to create weka objects from ClassValue objects is available, its
name is:

> *Collector.convertClassToWeka(classes );{}*

This method returns an array of Instances, implemented by weka in
\(weka.core.Instances\). The user should import this package and also
the weka packages of the desired algorithm.

## Importing a different metric list

The program already includes a CSV file with a list of metric values and
number of found bugs for a set of classes, taken from the
repository\[3\] from Wroklow University. Since some metric values were
not included for some classes this file was filtered by deleting every
not complete class. This was achieved by the method
\(Cleaner.clean(String csvPath)\), which receives the path of the csv
downloaded from the mentioned repository and creates a new file in the
same folder than the original one, with only all the complete lines from
the first one.  
The user can import another CSV file, both if the metrics are the same
or others, even with another number of metrics, the program is adapted
to accept any number of metrics. If some metric values are not found,
the user should only uncoment the calling of the above mentioned method
in the Main class and the program will create a filtered version of the
metrics, whose name is \(cleanCSV-"i".csv\), where "i" is the next
available number.  

## Using the program

Once every modification of algorithm or .csv is done, if desired, the
program has to be started as above mentioned, while starting the run the
user is asked about the desired options, the input is not case
sensitive. Since every question is self described, it is not necessary
to describe it here.  
It should be just specified that the number of groups should be an
integer bigger than one, the introduction of an algorithm name that is
not included in the list will make the program to end and, for "apply"
use the introduction of a wrong path from a local project would have a
similar consequence.

## Required technologies

1.  https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

2.  https://docs.gradle.org/current/userguide/installation.html

3.  http://snow.iiar.pwr.wroc.pl:8080/MetricsRepo/
