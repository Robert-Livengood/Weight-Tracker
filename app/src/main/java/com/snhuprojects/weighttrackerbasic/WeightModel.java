package com.snhuprojects.weighttrackerbasic;

public class WeightModel {
    private int id;
    private String name;
    private String date;
    private int weight;
    private int delta;
    private int goalWeight;
    private String workoutType;

    // constructors

    // without delta
    public WeightModel(int id, String name, String date, int weight, String workoutType) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.weight = weight;
        this.delta = 0;
        this.goalWeight = 0;
        this.workoutType = workoutType;
    }

    public WeightModel() {
    }

    // with delta
    public WeightModel(int id, String name, String date, int weight, int delta, String workoutType) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.weight = weight;
        this.delta = delta;
        this.goalWeight = 0;
        this.workoutType = workoutType;
    }

    // toString for printing and class object
    @Override
    public String toString() {
        return "WeightModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", weight=" + weight +
                ", delta=" + delta +
                ", goalWeight=" + goalWeight +
                ", workoutType=" + workoutType +
                '}';
    }

    // getters and setters

    public int getGoalWeight() {
        return goalWeight;
    }

    public void setGoalWeight(int goalWeight) {
        this.goalWeight = goalWeight;
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getWorkoutType() { return workoutType; }

    public void setWorkoutType(String workoutType) { this.workoutType = workoutType; }
}
