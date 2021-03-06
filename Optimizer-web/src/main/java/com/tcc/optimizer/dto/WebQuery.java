/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcc.optimizer.dto;

import java.util.List;

/**
 *
 * @author luiz
 */
public class WebQuery {
    
    private String option = null;
    
    private int groupSize;
    
    private int groceriesSize;
    
    private int task;
    
    private int issue;
    
    private int specialDate;
    
    private double timeArrival;
    
    private Float x;
    
    private Float y;
    
    private List<List<Long>> listOfCategoriesOfIds;

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }

    public int getGroceriesSize() {
        return groceriesSize;
    }

    public void setGroceriesSize(int groceriesSize) {
        this.groceriesSize = groceriesSize;
    }

    public int getTask() {
        return task;
    }

    public void setTask(int task) {
        this.task = task;
    }

    public int getIssue() {
        return issue;
    }

    public void setIssue(int issue) {
        this.issue = issue;
    }

    public int getSpecialDate() {
        return specialDate;
    }

    public void setSpecialDate(int specialDate) {
        this.specialDate = specialDate;
    }

    public double getTimeArrival() {
        return timeArrival;
    }

    public void setTimeArrival(double timeArrival) {
        this.timeArrival = timeArrival;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public List<List<Long>> getListOfCategoriesOfIds() {
        return listOfCategoriesOfIds;
    }

    public void setListOfCategoriesOfIds(List<List<Long>> listOfCategoriesOfIds) {
        this.listOfCategoriesOfIds = listOfCategoriesOfIds;
    }

    @Override
    public String toString() {
        return "WebQuery{" + "option=" + option + ", groupSize=" + groupSize + ", groceriesSize=" + groceriesSize + ", task=" + task + ", issue=" + issue + ", specialDate=" + specialDate + ", timeArrival=" + timeArrival + ", x=" + x + ", y=" + y + ", listOfCategoriesOfIds=" + listOfCategoriesOfIds + '}';
    }
    
}
