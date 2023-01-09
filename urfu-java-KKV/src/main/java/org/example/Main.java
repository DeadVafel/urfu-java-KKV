package org.example;

import org.example.Entity.GrantService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class Main {

    static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Имя файла (Гранты.csv) ");
        try {
            String path = br.readLine();
            GrantService grantService = new GrantService(path);
            grantService.graph();
            grantService.second();
            grantService.third();
        } catch (IOException e) {
            logger.info("Error in reading the line");
        }
    }
}