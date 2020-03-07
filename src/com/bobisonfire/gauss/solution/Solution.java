package com.bobisonfire.gauss.solution;

public class Solution {
    private boolean infiniteSolutions;
    private boolean noSolutions;

    private boolean[] isAny;
    private double[] freeMembers;
    private double[][] constants;
    private double[] remainders;

    private String[] variableNames;

    public boolean isInfiniteSolutions() {
        return infiniteSolutions;
    }

    public boolean hasSolutions() {
        return !noSolutions;
    }

    public boolean[] getIsAny() {
        return isAny;
    }

    public double[] getFreeMembers() {
        return freeMembers;
    }

    public double[][] getConstants() {
        return constants;
    }

    public double[] getRemainders() {
        return remainders;
    }

    public String[] getVariableNames() {
        return variableNames;
    }

    void setInfiniteSolutions(boolean infiniteSolutions) {
        this.infiniteSolutions = infiniteSolutions;
    }

    void setNoSolutions(boolean noSolutions) {
        this.noSolutions = noSolutions;
    }

    void setIsAny(boolean[] isAny) {
        this.isAny = isAny;
    }

    void setFreeMembers(double[] freeMembers) {
        this.freeMembers = freeMembers;
    }

    void setConstants(double[][] constants) {
        this.constants = constants;
    }

    void setRemainders(double[] remainders) {
        this.remainders = remainders;
    }

    void setVariableNames(String[] variableNames) {
        this.variableNames = variableNames;
    }

    @Override
    public String toString() {
        if (noSolutions) return "No solutions.";

        if (!infiniteSolutions) {
            StringBuilder sb = new StringBuilder("One solution:\n");
            for (int i = 0; i < freeMembers.length; i++)
                sb.append( String.format("%s = %s", variableNames[i], freeMembers[i]) ).append('\n');
            return sb.toString();
        }

        int k = 0;
        String[] constantNames = new String[freeMembers.length];
        for (int i = 0; i < freeMembers.length; i++) {
            if (isAny[i]) constantNames[i] = "c" + ++k;
        }

        StringBuilder sb = new StringBuilder("Infinite solutions:\n");
        for (int i = 0; i < freeMembers.length; i++) {
            if (isAny[i]) {
                sb.append( String.format("%s = %s", variableNames[i], constantNames[i]) ).append('\n');
                continue;
            }

            sb.append( String.format("%s = %s", variableNames[i], freeMembers[i]) );
            for (int j = 0; j < isAny.length; j++) {
                if (!isAny[j]) continue;

                double r = constants[i][j];
                if (r == 0) continue;

                if (r == 1 || r == -1) {
                    sb.append( String.format(" %s %s", r == 1 ? "+" : "-", constantNames[j]) );
                    continue;
                }

                sb.append( String.format(" %s %s %s", r > 0 ? "+" : "-", Math.abs(r), constantNames[j]) );
            }
            sb.append('\n');
        }

        return sb.toString();
    }
}
