package com.company;

import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
Class that deletes every entry in the class list that does not contain values for every single metric in order to
avoid failures or wrong values.
It is currently not used since the clean file was already used.
 */
public class Cleaner {

    public static String clean(String CSVPath) {
        String cleanName = getCleanName(CSVPath);

        copyCleanLines(CSVPath,cleanName);
        return cleanName;
    }

    private static String getCleanName(String CSVPath){

        Pattern pattern = Pattern.compile("(.+\\/)\\w+\\.\\w+$");
        Matcher matcher = pattern.matcher(CSVPath);
        File fileToTest;
        try{
            if (!matcher.find( )) {
                throw new Exception();
            }
            fileToTest = new File(matcher.group(1)+"cleanCSV"+".txt");
            if(!fileToTest.exists()){
                return fileToTest.getAbsolutePath();
            }
            for(int i=0;i<10000;i++){
                fileToTest = new File(matcher.group(1)+"cleanCSV-"+i+".txt");
                if(!fileToTest.exists()){
                    return fileToTest.getAbsolutePath();
                }
            }
            throw new Exception();
        }
        catch (Exception e){
            System.out.println("Correct name of a file not found");
            System.exit(1);
            return "fail";
        }
    }

    private static void copyCleanLines(String CSVPath, String cleanName){
        BufferedReader buffer;
        BufferedWriter writer;
        String line;
        String cleanText = "";

        try {
            buffer = new BufferedReader(new FileReader(CSVPath));

            while ((line = buffer.readLine()) != null ) {
                if(!line.contains("-")  ){
                    cleanText = cleanText.concat(line+"\n");
                }
            }
            writer = new BufferedWriter(new FileWriter(cleanName));
            writer.write(cleanText);

            writer.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
