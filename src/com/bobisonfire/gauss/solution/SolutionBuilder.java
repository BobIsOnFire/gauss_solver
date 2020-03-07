package com.bobisonfire.gauss.solution;

public class SolutionBuilder {
    private Solution solution;

    public static SolutionBuilder instance() {
        SolutionBuilder builder = new SolutionBuilder();
        builder.solution = new Solution();
        return builder;
    }

    public SolutionBuilder infiniteSolutions(boolean i) {
        solution.setInfiniteSolutions(i);
        return this;
    }

    public SolutionBuilder noSolutions(boolean i) {
        solution.setNoSolutions(i);
        return this;
    }

    public SolutionBuilder freeMembers(double[] freeMembers) {
        solution.setFreeMembers(freeMembers);
        return this;
    }

    public SolutionBuilder isAny(boolean[] isAny) {
        solution.setIsAny(isAny);
        return this;
    }

    public SolutionBuilder constants(double[][] constants) {
        solution.setConstants(constants);
        return this;
    }

    public SolutionBuilder remainders(double[] remainders) {
        solution.setRemainders(remainders);
        return this;
    }

    public SolutionBuilder variableNames(String[] variableNames) {
        solution.setVariableNames(variableNames);
        return this;
    }

    public Solution get() {
        if (solution.hasSolutions() &&
                (solution.getFreeMembers() == null || solution.getRemainders() == null || solution.getVariableNames() == null))
            throw new SolutionException();

        if (solution.isInfiniteSolutions() && (solution.getConstants() == null || solution.getIsAny() == null))
            throw new SolutionException();

        return solution;
    }
}
