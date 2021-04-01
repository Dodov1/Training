package com.web.training.helpers.impl;

import com.web.training.helpers.WeightCalculator;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

@Component
public class WeightCalculatorImpl implements WeightCalculator {

    @Override
    public double calculateBmi(double weight, int height) {
        return Double.parseDouble(
                new DecimalFormat("#.#")
                        .format((weight / (Math.pow(((height * 1.0) / 100), 2)))).replace(",", ".")
        );
    }

    @Override
    public String getChange(Double weightOne, Double weightTwo) {
        double change = weightOne - weightTwo;
        String start = "";
        if (change == 0) {
            return "--";
        }
        if (change > 0) {
            start = String.format("+%.2f kg", change);
        } else {
            start = String.format("%.2f kg", change);
        }
        String number = start.split("\\s+")[0];
        if (number.split(",")[1].startsWith("0")) {
            start = start.split(",")[0] + " kg";
        }
        return start;
    }

}
