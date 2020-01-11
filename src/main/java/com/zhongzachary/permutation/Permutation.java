package com.zhongzachary.permutation;

import com.google.common.collect.ImmutableBiMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.*;

/**
 * Represents a permutation. Permutation is an immutable, value-type object.
 */
public class Permutation {
    
    private static Gson gson = new Gson();
    private static PrintMode PRINT_MODE = PrintMode.CYCLE;
    
    private ImmutableBiMap<Integer, Integer> twoLine;
    
    /**
     * Describes how toString should work. Permutation can be printed in one-line notation or cycle notation.
     */
    public enum  PrintMode {
        ONE_LINE, CYCLE
    }
    
    /**
     * Two permutations are equal if each maps any natural number to the same natural number.
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permutation that = (Permutation) o;
        for (int i = 1; i <= Math.max(this.dim(), that.dim()) ; i++) {
            if(this.mapsTo(i) != that.mapsTo(i)) return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(toCycle());
    }
    
    /**
     * Creates a permutation in one-line notation.
     */
    private Permutation(int... oneLine) {
        ImmutableBiMap.Builder<Integer, Integer> builder = ImmutableBiMap.builder();
        for (int i = 0; i < oneLine.length; i++) {
            if(i+1 != oneLine[i]) builder.put(i+1, oneLine[i]);
        }
        twoLine = builder.build();
        if(!isDomEqualIm()) throw new IllegalArgumentException("Illegal one line notation.");
    }
    
    /**
     * Creates a permutation in disjointed cycle notation, where each cycle is represented by a int[].
     */
    private Permutation(int[]... cycles) {
        ImmutableBiMap.Builder<Integer, Integer> builder = ImmutableBiMap.builder();
        for (int[] cycle : cycles) {
            if(cycle.length <= 1) continue;
            for (int i = 0; i < cycle.length; i++) {
                int from = cycle[i];
                int to = (i == cycle.length - 1) ? cycle[0] : cycle[i + 1];
                builder.put(from, to);
            }
        }
        twoLine = builder.build();
    }
    
    /**
     * Create a permutation using a Map.
     */
    private Permutation(Map<Integer, Integer> twoLine) {
        this.twoLine = ImmutableBiMap.copyOf(twoLine);
        if(!isDomEqualIm()) throw new IllegalArgumentException("Illegal two line notation.");
    }
    
    /**
     * Checking if the domain if equal to the image of a permutation. If not, it is not a valid permutation.
     */
    private boolean isDomEqualIm() {
        return twoLine.keySet().equals(twoLine.values());
    }
    
    /**
     * Returns the largest element this permutation permutes.
     */
    public int dim() {
        return twoLine.isEmpty()? 0 : twoLine.keySet().stream().max(Integer::compareTo).get();
    }
    
    /**
     * Creates permutation using one-line notation
     * @param oneLine a varargs of integer representing one-line notation of a permutation
     * @return a Permutation object
     */
    public static Permutation inOneLineNotation(int... oneLine) {
        return new Permutation(oneLine);
    }
    
    /**
     * Creates permutation using disjointed cycle notation.
     * @param cycles a varargs of int array, each array represents a cycle
     * @return a Permutation object
     */
    public static Permutation inCycleNotation(int[]... cycles) {
        return new Permutation(cycles);
    }
    
    /**
     * Creates an identity permutation.
     * @return a Permutation object
     */
    public static Permutation identity() {
        return new Permutation(ImmutableBiMap.<Integer, Integer>of());
    }
    
    /**
     * Returns the inverse of this permutation
     * @return a new Permutation object
     */
    public Permutation inverse() {
        return new Permutation(twoLine.inverse());
    }
    
    /**
     * Returns what the given integer will be mapped to under this permutation
     * @param k an integer
     * @return the integer k maps to
     */
    public int mapsTo(int k) {
        return twoLine.getOrDefault(k, k);
    }
    
    /**
     * Returns the composition of two permutation.
     * @param second the permutation that will get apply second
     * @param first the permutation that will get apply first
     * @return the composed permutation
     */
    public static Permutation composition(Permutation second, Permutation first) {
        Set<Integer> possibleRange = new HashSet<>();
        possibleRange.addAll(first.twoLine.keySet());
        possibleRange.addAll(second.twoLine.keySet());
        ImmutableBiMap.Builder<Integer, Integer> builder = ImmutableBiMap.builder();
        for (Integer from : possibleRange) {
            int to = second.mapsTo(first.mapsTo(from));
            if(to != from) builder.put(from, to);
        }
        return new Permutation(builder.build());
    }
    
    /**
     * Returns the composition of given permutation
     * @param permutations some permutations, the last will be applied first
     * @return the composed permutation
     */
    public static Permutation composition(Permutation... permutations) {
        if(permutations.length == 0) return Permutation.identity();
        Permutation acc = Permutation.identity();
        for (int i = permutations.length - 1; i >= 0; i--) {
            acc = Permutation.composition(permutations[i], acc);
        }
        return acc;
    }
    
    /**
     * Set how toString work for Permutation.
     * @param printMode one of Permutation.PrintMode.ONE_LINE or Permutation.PrintMode.CYCLE
     */
    public static void setPrintMode(PrintMode printMode) {
        if(printMode == null) throw new NullPointerException();
        PRINT_MODE = printMode;
    }
    
    @Override
    public String toString() {
        switch (PRINT_MODE) {
            case ONE_LINE:
                return toOneLine();
            case CYCLE:
                return toCycle();
            default:
                throw new IllegalStateException();
        }
    }
    
    /**
     * Gets the one-line of this permutation.
     * @return a JSON array of integer
     */
    public String toOneLine() {
        JsonArray output = new JsonArray();
        for (int i = 1; i <= dim(); i++) {
            output.add(mapsTo(i));
        }
        return gson.toJson(output);
    }
    
    /**
     * Get the cycle notation of this permutation.
     * @return a nested JSON array, in which each inner JSON array is a cycle
     */
    public String toCycle() {
        JsonArray output = new JsonArray();
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int i = 1; i < dim(); i++) {
            queue.addLast(i);
        }
        while(!queue.isEmpty()) {
            JsonArray cycle = new JsonArray();
            int sentinel = queue.getFirst();
            int curr = sentinel;
            while (cycle.size() == 0 || curr != sentinel) {
                cycle.add(curr);
                queue.removeFirstOccurrence(curr);
                curr = mapsTo(curr);
            }
            if(cycle.size() > 1) output.add(cycle);
        }
        return gson.toJson(output);
    }
}
