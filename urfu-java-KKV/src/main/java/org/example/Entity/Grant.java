package org.example.Entity;

import com.opencsv.bean.CsvBindByName;

public class Grant {
    @CsvBindByName(column = "Название компании")
    private String name_company;
    @CsvBindByName(column = "Название улицы")
    private String name_street;
    @CsvBindByName(column = "Размер гранта")
    private String grant_amount;
    @CsvBindByName(column = "Фискальный год")
    private Integer year_grant;
    @CsvBindByName(column = "Тип бизнеса")
    private String type_business;
    @CsvBindByName(column = "Количество рабочих мест")
    private Integer number_jobs;

    @Override
    public String toString() {
        return "Grant{" +
                "name_company='" + name_company + '\'' +
                ", name_street='" + name_street + '\'' +
                ", grant_amount='" + grant_amount + '\'' +
                ", year_grant=" + year_grant +
                ", type_business='" + type_business + '\'' +
                ", number_jobs=" + number_jobs +
                '}';
    }

    public String getName_company() {
        return name_company;
    }

    public void setName_company(String name_company) {
        this.name_company = name_company;
    }

    public String getName_street() {
        return name_street;
    }

    public void setName_street(String name_street) {
        this.name_street = name_street;
    }

    public String getGrant_amount() {
        return grant_amount;
    }

    public void setGrant_amount(String grant_amount) {
        this.grant_amount = grant_amount;
    }

    public Integer getYear_grant() {
        return year_grant;
    }

    public void setYear_grant(Integer year_grant) {
        this.year_grant = year_grant;
    }

    public String getType_business() {
        return type_business;
    }

    public void setType_business(String type_business) {
        this.type_business = type_business;
    }

    public Integer getNumber_jobs() {
        return number_jobs;
    }

    public void setNumber_jobs(Integer number_jobs) {
        this.number_jobs = number_jobs;
    }

}
