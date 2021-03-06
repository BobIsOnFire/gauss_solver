package com.bobisonfire.gauss.matrix;

import java.util.StringJoiner;

public class Matrix {
    private Rational[][] model;
    private int rows;
    private int cols;

    public static Matrix from(Rational[][] model) {
        if (model.length == 0 || model[0].length == 0) throw new MatrixException();
        Matrix m = new Matrix();

        m.model = model;
        m.rows = model.length;
        m.cols = model[0].length;

        return m;
    }

    public static Matrix unary(int size) {
        Rational[][] model = new Rational[size][size];
        for (int i = 0; i < size; i++) {
            model[i][i] = Rational.ONE;
        }

        return from(model);
    }

    private static Rational[][] cloneModel(Rational[][] model) {
        int rows = model.length;
        if (rows == 0) return model;

        int cols = model[0].length;
        Rational[][] newModel = new Rational[rows][cols];

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                newModel[i][j] = Rational.from( model[i][j] );

        return newModel;
    }



    public Rational[][] getModel() {
        return model;
    }

    public Rational[] getRow(int i) {
        return model[i];
    }

    public Rational[] getCol(int j) {
        Rational[] col = new Rational[rows];
        for (int i = 0; i < rows; i++) col[i] = model[i][j];
        return col;
    }

    public Rational get(int i, int j) {
        return model[i][j];
    }



    public Matrix add(Matrix x) {
        if (rows != x.rows || cols != x.cols) throw new MatrixException();

        Rational[][] newModel = new Rational[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                newModel[i][j] = model[i][j].add( x.model[i][j] );

        return from(newModel);
    }

    public Matrix multiply(Rational x) {
        Rational[][] newModel = new Rational[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                newModel[i][j] = model[i][j].multiply(x);

        return from(newModel);
    }

    public Matrix multiply(Matrix x) {
        if (cols != x.rows) throw new MatrixException();

        Rational[][] newModel = new Rational[rows][x.cols];

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < x.cols; j++)
                for (int k = 0; k < cols; k++)
                    newModel[i][j] = newModel[i][j].add( model[i][k].multiply( x.model[k][j] ) );

        return from(newModel);
    }

    public Matrix swapRows(int i1, int i2) {
        Rational[][] newModel = cloneModel(model);
        if (i1 == i2) return from(newModel);

        for (int j = 0; j < cols; j++) {
            Rational r = newModel[i1][j];
            newModel[i1][j] = newModel[i2][j];
            newModel[i2][j] = r;
        }

        return from(newModel);
    }

    public Matrix swapCols(int j1, int j2) {
        Rational[][] newModel = cloneModel(model);
        if (j1 == j2) return from(newModel);

        for (int i = 0; i < rows; i++) {
            Rational r = newModel[i][j1];
            newModel[i][j1] = newModel[i][j2];
            newModel[i][j2] = r;
        }

        return from(newModel);
    }

    public Matrix multiplyRow(int i, Rational r) {
        Rational[][] newModel = cloneModel(model);
        if (r.equals(Rational.ZERO)) throw new MatrixException();
        if (r.equals(Rational.ONE)) return from(newModel);

        for (int j = 0; j < cols; j++)
            newModel[i][j] = newModel[i][j].multiply(r);

        return from(newModel);
    }

    public Matrix multiplyCol(int j, Rational r) {
        Rational[][] newModel = cloneModel(model);
        if (r.equals(Rational.ZERO)) throw new MatrixException();
        if (r.equals(Rational.ONE)) return from(newModel);

        for (int i = 0; i < rows; i++) {
            newModel[i][j] = newModel[i][j].multiply(r);
        }

        return from(newModel);
    }

    public Matrix addRow(int from, int to, Rational k) {
        Rational[][] newModel = cloneModel(model);
        if (k.equals(Rational.ZERO)) return from(newModel);

        for (int j = 0; j < cols; j++) {
            newModel[to][j] = newModel[to][j].add( newModel[from][j].multiply(k) );
        }

        return from(newModel);
    }

    public Matrix addCol(int from, int to, Rational k) {
        Rational[][] newModel = cloneModel(model);
        if (k.equals(Rational.ZERO)) return from(newModel);

        for (int i = 0; i < cols; i++) {
            newModel[i][to] = newModel[i][to].add( newModel[i][from].multiply(k) );
        }

        return from(newModel);
    }

    @Override
    public String toString() {
        StringJoiner lineJoiner = new StringJoiner("\n");

        for (Rational[] row : model) {
            StringJoiner elementJoiner = new StringJoiner("\t");
            for (Rational el : row) elementJoiner.add(el.toString());
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
                if ( !get(i, j).equals(m.get(i, j)) ) return false;

        return true;
    }
}
