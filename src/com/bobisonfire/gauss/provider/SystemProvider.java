package com.bobisonfire.gauss.provider;

import com.bobisonfire.gauss.GaussSolver;
import com.bobisonfire.gauss.matrix.Matrix;
import com.bobisonfire.gauss.solution.Solution;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

public abstract class SystemProvider {
    private double[][] model;
    protected int size;

    protected abstract double[][] parseModel(Scanner scanner);
    protected abstract String[] getVariableNames();

    public void readAndParse(InputStream in) {
        Scanner scanner = new Scanner(in);

        if (!scanner.hasNextLine()) {
            System.out.println("Heyy what's up with your input stream? I reached unexpected EOF. Gimme yo' bytes!");
            System.exit(0);
        }

        String line = scanner.nextLine().trim();
        if (!line.matches("-?\\d+")) {
            System.out.println("First line should contain the size of system.");
            System.exit(0);
        }

        size = Integer.parseInt(line);
        model = parseModel(scanner);
    }


    public void printSolution(PrintStream out) {
        Matrix matrix = Matrix.from(model);
        out.println("Original matrix:\n" + matrix + "\n");

        GaussSolver solver = new GaussSolver(matrix);
        out.println("Determinant: " + solver.getDeterminant());
        out.println("Triangle matrix:\n" + solver.getTriangleMatrix() + "\n");

        Solution solution = solver.getSolution( getVariableNames() );
        out.println(solution);

        if (solution.hasSolutions()) out.println("Remainders: " + Arrays.toString(solution.getRemainders()));
    }

    protected final double parseNumber(String s) {
        if (s.isEmpty()) throw new NumberFormatException("empty String");

        if (s.matches("-?\\d+")) return Integer.parseInt(s);

        if (s.matches("-?\\d+/-?\\d+")) {
            String[] parts = s.split("/");
            return Integer.parseInt(parts[0]) * 1.0 / Integer.parseInt(parts[1]);
        }

        if (s.matches("-?\\d*\\.\\d*")) return Double.parseDouble(s);

        throw new NumberFormatException("For input string: \"" + s + "\"");
    }
}
