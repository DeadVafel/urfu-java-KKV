package org.example;

import org.example.Entity.GrantService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class Main {

    static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter the path to the file: ");
        //Гранты.csv
        try {
            String path = bf.readLine();
            GrantService grantService = new GrantService(path);
            grantService.showGraph();
            grantService.secondMission();
            grantService.thirdMission();
        } catch (IOException e) {
            logger.info("Error in reading the line");
        }
    }
}