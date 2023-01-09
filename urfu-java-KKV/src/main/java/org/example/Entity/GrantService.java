package org.example.Entity;

import com.opencsv.bean.CsvToBeanBuilder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class GrantService {
    private Statement statement;
    private final Logger logger = Logger.getLogger(GrantService.class.getName());

    public GrantService(String path){
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:grant.db");
            statement = connection.createStatement();
            logger.info("Successful connection to the database.");
        } catch (ClassNotFoundException | SQLException e) {
            logger.info("Failed to connect to the database.");
            System.exit(0);
        }
        createTableInDB();
        getDataCSV(path);
    }

    private void createTableInDB(){
        String drop = "DROP TABLE IF EXISTS grants";
        String create = "CREATE TABLE IF NOT EXISTS grants(\n" +
                "id INT AUTO_INCREMENT,\n" +
                "name_company VARCHAR(250),\n" +
                "name_street VARCHAR(250),\n" +
                "grant_amount INT,\n" +
                "year_grant INT,\n" +
                "type_business VARCHAR(250),\n" +
                "number_jobs INT\n" +
                ");";
        try {
            statement.execute(drop);
            statement.execute(create);
            logger.info("Successful creation of the table");
        } catch (SQLException e) {
            throw new RuntimeException("Unsuccessful table creation");
        }
    }

    private void getDataCSV(String path){
        try {
            List<Grant> grants = new CsvToBeanBuilder(new FileReader(path))
                    .withType(Grant.class)
                    .build()
                    .parse();
            insertValuesInDB(grants);
            logger.info("Data successfully added");
        } catch (FileNotFoundException e) {
            logger.info("Error in adding data");
        }
    }

    private void insertValuesInDB(List<Grant> grants){
        for (Grant grant : grants) {
            try {
            String query = "INSERT INTO grants (name_company, name_street, grant_amount, " +
                    "year_grant, type_business, number_jobs) VALUES " +
                    "('"+grant.getName_company().replaceAll("'", "")+"', " +
                    "'"+grant.getName_street().replaceAll("'", "")+"', " +
                    ""+Double.parseDouble(grant.getGrant_amount().replace("$", "").replace(",", ""))+", " +
                    ""+grant.getYear_grant()+", " +
                    "'"+grant.getType_business().replaceAll("'", "")+"', " +
                    ""+grant.getNumber_jobs()+")";
                statement.execute(query);
            } catch (SQLException | NumberFormatException ignored) {
            }
        }
    }

    private DefaultCategoryDataset getDataForGraph(){
        Map<String, Double> data = new HashMap<>();
        try {
            ResultSet rs = statement.executeQuery("SELECT year_grant as 'year_grant', AVG(number_jobs) as 'number_jobs' " +
                    "FROM grants " +
                    "GROUP BY year_grant");
            while (rs.next()) {
                data.put(String.valueOf(rs.getInt("year_grant")), rs.getDouble("number_jobs"));
            }
            logger.info("The data has been uploaded to the graph successfully. Check the panel.");
        } catch (SQLException e) {
            logger.info("Error in graph data");
        }
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (var entry : data.entrySet()) {
            dataset.addValue(entry.getValue(), entry.getKey(), "Year");
        }
        return dataset;
    }

    public void showGraph(){
        var dataset = getDataForGraph();
        JFreeChart chart = ChartFactory.createBarChart(
                "Average number of jobs",
                "Year",
                "Average number of jobs",
                dataset);
        chart.setBackgroundPaint(Color.white);

        JFrame frame =
                new JFrame("Graph");
        frame.getContentPane()
                .add(new ChartPanel(chart));
        frame.setSize(1000, 800);
        frame.setVisible(true);
    }

    public void secondMission(){
        String sql = "SELECT AVG(grant_amount) as 'grant', type_business FROM grants WHERE type_business = 'Salon/Barbershop'";
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                logger.info("The SQL query for the second task worked correctly");
                Thread.sleep(200);
                System.out.println("The average grant for a "
                        +resultSet.getString("type_business") +" is " + resultSet.getDouble("grant"));
            }
        } catch (SQLException | InterruptedException e) {
            logger.info("Error in the SQL request for the second task");
        }
    }

    public void thirdMission(){
        double max = Double.MIN_VALUE;
        String type_business = "";
        String sql = "SELECT type_business, SUM(number_jobs) as 'number_jobs' FROM grants " +
                "WHERE grant_amount <= 55000 " +
                "GROUP BY type_business";
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                if (max < resultSet.getDouble("number_jobs")){
                    max = resultSet.getDouble("number_jobs");
                    type_business = resultSet.getString("type_business");
                }
            }
            logger.info("The SQL query for the third task worked correctly");
            Thread.sleep(200);
            System.out.println(type_business + " provided the largest number of jobs");
        } catch (SQLException | InterruptedException e) {
            logger.info("Error in the SQL request for the third task");
        }
    }
}
