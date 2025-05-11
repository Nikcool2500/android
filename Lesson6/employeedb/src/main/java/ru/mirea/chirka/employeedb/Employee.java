package ru.mirea.chirka.employeedb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Employee {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public String superpower;
    public int strength;

    public Employee() {}

    public Employee(String name, String superpower, int strength) {
        this.name = name;
        this.superpower = superpower;
        this.strength = strength;
    }
}