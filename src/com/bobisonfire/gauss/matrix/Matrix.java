package com.bobisonfire.gauss.matrix;

import java.util.StringJoiner;

public class Matrix {
    private double[][] model;
    private int rows;
    private int cols;

    public static Matrix from(double[][] model) {
        if (model.length == 0 || model[0].length == 0) throw new MatrixException();
        Matrix m = new Matrix();

        m.model = model;
        m.rows = model.length;
        m.cols = model[0].length;

        return m;
    }

    public static Matrix unary(int size) {
        double[][] model = new double[size][size];
        for (int i = 0; i < size; i++) {
            model[i][i] = 1d;
        }

        return from(model);
    }

    private static double[][] cloneModel(double[][] model) {
        int rows = model.length;
        if (rows == 0) return model;

        int cols = model[0].length;
        double[][] newModel = new double[rows][cols];

        for (int i = 0; i < rows; i++)
            System.arraycopy(model[i], 0, newModel[i], 0, cols);

        return newModel;
    }



    public double[][] getModel() {
        return model;
    }

    public double[] getRow(int i) {
        return model[i];
    }

    public double[] getCol(int j) {
        double[] col = new double[rows];
        for (int i = 0; i < rows; i++) col[i] = model[i][j];
        return col;
    }

    public double get(int i, int j) {
        return model[i][j];
    }



    public Matrix add(Matrix x) {
        if (rows != x.rows || cols != x.cols) throw new MatrixException();

        double[][] newModel = new double[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                newModel[i][j] = model[i][j] + x.model[i][j];

        return from(newModel);
    }

    public Matrix multiply(double x) {
        double[][] newModel = new double[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                newModel[i][j] = model[i][j] * x;

        return from(newModel);
    }

    public Matrix multiply(Matrix x) {
        if (cols != x.rows) throw new MatrixException();

        double[][] newModel = new double[rows][x.cols];

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < x.cols; j++)
                for (int k = 0; k < cols; k++)
                    newModel[i][j] = newModel[i][j] + model[i][k] * x.model[k][j];

        return from(newModel);
    }

    public Matrix swapRows(int i1, int i2) {
        double[][] newModel = cloneModel(model);
        if (i1 == i2) return from(newModel);

        for (int j = 0; j < cols; j++) {
            double r = newModel[i1][j];
            newModel[i1][j] = newModel[i2][j];
            newModel[i2][j] = r;
        }

        return from(newModel);
    }

    public Matrix swapCols(int j1, int j2) {
        double[][] newModel = cloneModel(model);
        if (j1 == j2) return from(newModel);

        for (int i = 0; i < rows; i++) {
            double r = newModel[i][j1];
            newModel[i][j1] = newModel[i][j2];
            newModel[i][j2] = r;
        }

        return from(newModel);
    }

    public Matrix multiplyRow(int i, double r) {
        double[][] newModel = cloneModel(model);
        if (r == 0) throw new MatrixException();
        if (r == 1) return from(newModel);

        for (int j = 0; j < cols; j++)
            newModel[i][j] = newModel[i][j] * r;

        return from(newModel);
    }

    public Matrix multiplyCol(int j, double r) {
        double[][] newModel = cloneModel(model);
        if (r == 0) throw new MatrixException();
        if (r == 1) return from(newModel);

        for (int i = 0; i < rows; i++) {
            newModel[i][j] = newModel[i][j] * r;
        }

        return from(newModel);
    }

    public Matrix addRow(int from, int to, double k) {
        double[][] newModel = cloneModel(model);
        if (k == 0) return from(newModel);

        for (int j = 0; j < cols; j++) {
            newModel[to][j] = newModel[to][j] + newModel[from][j] * k;
        }

        return from(newModel);
    }

    public Matrix addCol(int from, int to, double k) {
        double[][] newModel = cloneModel(model);
        if (k == 0) return from(newModel);

        for (int i = 0; i < cols; i++) {
            newModel[i][to] = newModel[i][to] + newModel[i][from] * k;
        }

        return from(newModel);
    }

    @Override
    public String toString() {
        StringJoiner lineJoiner = new StringJoiner("\n");

        for (double[] row : model) {
            StringJoiner elementJoiner = new StringJoiner("\t");
            for (double el : row) elementJoiner.add(Double.toString(el));
            lineJoiner.add(elementJoiner.toString());
        }

        return lineJoiner.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if ( !(obj instanceof Matrix) ) return false;

        Matrix m = (Matrix) obj;
        if (rows != m.rows || cols != m.cols) return false;

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                if ( !( get(i, j) == m.get(i, j) ) ) return false;

        return true;
    }
}
