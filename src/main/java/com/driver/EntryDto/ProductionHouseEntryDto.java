package com.driver.EntryDto;

public class ProductionHouseEntryDto {

    private String name;

    public ProductionHouseEntryDto(){}

    //constructor
    public ProductionHouseEntryDto(String name) {
        this.name = name;
    }

    //getter
    public String getName() {
        return name;
    }

    //setter
    public void setName(String name) {
        this.name = name;
    }
}
