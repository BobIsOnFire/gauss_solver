package com.bobisonfire.gauss.provider;

import java.util.Random;
import java.util.Scanner;

public class RandomSystemProvider extends SystemProvider {

    @Override
    protected double[][] parseModel(Scanner scanner) {
        double[][] model = new double[size][size + 1];
        Random r = new Random(System.currentTimeMillis());
        r.nextInt();
        r.nextInt();
        r.nextInt();

        for (double[] row : model) {
            for (int i = 0; i < size + 1; i++) {
                row[i] = (r.nextInt(201) - 100) * 1.0 / (r.nextInt(100) + 1);
            }
        }

        return model;
    }

    @Override
    protected String[] getVariableNames() {
        String[] variableNames = new String[size];
        for (int i = 0; i < size; i++) variableNames[i] = "x" + (i + 1);
        return variableNames;
    }
}
