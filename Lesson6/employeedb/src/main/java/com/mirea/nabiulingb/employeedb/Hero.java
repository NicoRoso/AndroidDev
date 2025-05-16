package com.mirea.nabiulingb.employeedb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "heroes")
public class Hero {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String superpower;
    public int powerLevel;

    public Hero(String name, String superpower, int powerLevel) {
        this.name = name;
        this.superpower = superpower;
        this.powerLevel = powerLevel;
    }
}