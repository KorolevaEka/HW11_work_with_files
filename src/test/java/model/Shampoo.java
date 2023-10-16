package model;

import java.util.List;

public class Shampoo {
    private String brand;
    private String name;
    private int releasedYear;
    private List<String> notes;

    public String getBrand() {
        return brand;
    }

    public String getName() {
        return name;
    }

    public int getReleasedYear() {
        return releasedYear;
    }

    public List<String> getNotes() {
        return notes;
    }
}
