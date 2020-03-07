package com.bobisonfire.gauss;

import com.bobisonfire.gauss.solution.Solution;
import com.bobisonfire.gauss.solution.SolutionBuilder;
import com.bobisonfire.gauss.matrix.Matrix;

public class GaussSolver {
    private final static double EPSILON_PRECISION = 1E-12;

    private final Matrix matrix;
    private Matrix triangleMatrix = null;

    private final int rows;
    private final int cols;

    private boolean negated = false;

    public GaussSolver(Matrix matrix) {
        this.matrix = matrix;
        this.rows = matrix.getModel().length;
        this.cols = matrix.getRow(0).length;
    }

    public Matrix getTriangleMatrix() {
        if (triangleMatrix != null) return triangleMatrix;
        Matrix t = matrix;

        int steps = Math.min(cols, rows);

        for (int i = 0; i < steps; i++) {
            int j = i;
            while (j < rows && t.get(j, i) == 0) j++;
            if (j == rows) {
                j = i;
                while (j < cols && t.get(i, j) == 0) j++;
                if (j >= cols - 1 || !(t.get(j, j) == 0)) continue;

                t = t.swapRows(i, j);
                negated = !negated;
                continue;
            }

            if (i != j) {
                t = t.swapRows(i, j);
                negated = !negated;
            }

            for (j = i + 1; j < rows; j++) {
                if (t.get(j, i) == 0) continue;
                double mul = - t.get(j, i) / t.get(i, i);
                t = t.addRow(i, j, mul);
            }
        }

        triangleMatrix = castEpsilonsToZero(t);
        return triangleMatrix;
    }

    public double getDeterminant() {
        Matrix t = getTriangleMatrix();
        double det = negated ? 1 : -1;

        for (int i = 0; i < rows; i++) {
            if ( t.get(i, i) == 0 ) return 0;
            det *= t.get(i, i);
        }
        return det;
    }


    public Matrix getPartlyDiagonalMatrix() {
        Matrix t = getTriangleMatrix();

        for (int i = rows - 1; i >= 0; i--) {
            if (t.get(i, i) == 0) continue;

            t = t.multiplyRow( i, 1 / t.get(i, i));

            for (int j = i - 1; j >= 0; j--) {
                if ( t.get(j, i) == 0 ) continue;
                double mul = -t.get(j, i);
                t = t.addRow(i, j, mul);
            }
        }

        return castEpsilonsToZero(t);
    }

    public Solution getSolution(String[] variableNames) {
        Matrix t = getPartlyDiagonalMatrix();

        double[] freeMembers = new double[cols - 1];
        double[][] constants = new double[rows][cols - 1];
        boolean[] isZero = new boolean[cols - 1];
        boolean infiniteSolutions = false;

        for (int i = 0; i < cols - 1; i++) {
            isZero[i] = t.get(i, i) == 0;
            if (!isZero[i]) {
                freeMembers[i] = t.get(i, cols - 1);
                continue;
            }

            freeMembers[i] = 0;

            if ( i < rows && !(t.get(i, cols - 1) == 0) )
                return SolutionBuilder.instance().noSolutions(true).get();

            infiniteSolutions = true;
            for (int j = 0; j < rows; j++) constants[j][i] = -t.get(j, i);
        }

        double[] remainders = new double[rows];
        for (int i = 0; i < rows; i++) {
            double sum = 0;
            for (int j = 0; j < cols - 1; j++) {
                sum += freeMembers[j] * matrix.get(i, j);
            }
            remainders[i] = matrix.get(i, cols - 1) - sum;
        }

        SolutionBuilder builder = SolutionBuilder.instance()
                .freeMembers(freeMembers)
                .remainders(remainders)
                .variableNames(variableNames);

        if (!infiniteSolutions) {
            return builder.get();
        }

        return builder.infiniteSolutions(true)
                .isAny(isZero)
                .constants(constants)
                .get();
    }

    public Solution getSolution() {
        String[] variableNames = new String[cols - 1];
        for (int i = 0; i < cols - 1; i++) variableNames[i] = "x" + (i + 1);
        return getSolution(variableNames);
    }

    private Matrix castEpsilonsToZero(Matrix m) {
        for (double[] row : m.getModel()) {
            for (int i = 0; i < cols; i++) {
                if (Math.abs(row[i]) < EPSILON_PRECISION) row[i] = 0;
            }
        }

        return m;
    }
}
