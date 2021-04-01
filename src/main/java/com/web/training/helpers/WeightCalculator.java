package com.web.training.helpers;

public interface WeightCalculator {

    double calculateBmi(double weight, int userHeight);

    String getChange(Double weightOne,Double weightTwo);
}
