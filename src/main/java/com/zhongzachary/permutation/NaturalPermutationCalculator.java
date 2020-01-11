package com.zhongzachary.permutation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class NaturalPermutationCalculator {
    private HashMap<String, Permutation> allPermutations = new HashMap<>();
    
    public static void main(String[] args) {
        printIntro();
        
        NaturalPermutationCalculator npc = new NaturalPermutationCalculator();
        Scanner scanner = new Scanner(System.in);
        while(true) {
            try {
                String line = scanner.nextLine();
                if (line.contains("=")) {
                    System.out.println(npc.defineNewPermutation(line) ? "Successful" : "Failed");
                } else {
                    try {
                        Permutation result = npc.calculatePermutation(line);
                        System.out.println(result.toCycle());
                    } catch (Throwable e) {
                        System.out.println(e.getMessage());
                    }
                }
            } catch (NoSuchElementException e) {
                break;
            }
        }
    }
    
    /**
     * Prints natural_intro.txt to console.
     */
    private static void printIntro() {
        InputStream intro = NaturalPermutationCalculator.class.getResourceAsStream("/natural_intro.txt");
        BufferedReader introReader = new BufferedReader(new InputStreamReader(intro));
        introReader.lines().forEach(System.out::println);
        try {
            introReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    private boolean defineNewPermutation(String s) {
        int equalSignAt = s.indexOf("=");
        String lhs = s.substring(0,equalSignAt).trim();
        String rhs = s.substring(equalSignAt + 1).trim();
        if(lhs.endsWith("'")) return false;
        try {
            allPermutations.put(lhs, calculatePermutation(rhs));
            return true;
        } catch (Throwable e) {
            return false;
        }
    }
    
    private Permutation calculatePermutation(String s) {
        Scanner scanner = new Scanner(s);
        scanner.useDelimiter("\\*");
        List<String> allPermStrings = new ArrayList<>();
        while(scanner.hasNext()) {
            allPermStrings.add(scanner.next().trim());
        }
        Permutation result = Permutation.composition(convertToPermutationArray(allPermStrings));
        scanner.close();
        return result;
    }
    
    private Permutation[] convertToPermutationArray(List<String> allPermStrings) {
        Permutation[] result = new Permutation[allPermStrings.size()];
        for (int i = 0; i < allPermStrings.size(); i++) {
            result[i] = parsePermutation(allPermStrings.get(i));
        }
        return result;
    }
    
    private Permutation parsePermutation(String s) {
        if (s.equals("[]")) {
            return Permutation.identity();
        } else if (s.startsWith("[[")) {
            return parseCyclePat(JsonParser.parseString(s).getAsJsonArray());
        } else if (s.startsWith("[")) {
            return parseOneLinePat(JsonParser.parseString(s).getAsJsonArray());
        } else {
            try {
                return get(s);
            } catch (Throwable e) {
                throw new IllegalArgumentException("Unrecognized pattern: " + s);
            }
        }
        
    }
    
    private Permutation parseCyclePat(JsonArray cyclePat) {
        List<int[]> cycles = new ArrayList<>();
        for (JsonElement element : cyclePat) {
            JsonArray cycleJson = element.getAsJsonArray();
            int[] cycle = parseAsIntArray(cycleJson);
            cycles.add(cycle);
        }
        return Permutation.inCycleNotation(cycles.toArray(new int[0][0]));
    }
    
    private static int[] parseAsIntArray(JsonArray cycle) {
        int[] output = new int[cycle.size()];
        for (int i = 0; i < cycle.size(); i++) {
            output[i] = cycle.get(i).getAsInt();
        }
        return output;
    }
    
    private Permutation parseOneLinePat(JsonArray oneLinePat) {
        return Permutation.inOneLineNotation(parseAsIntArray(oneLinePat));
    }
    
    private Permutation get(String name) {
        if (name.endsWith("'")) {
            return get(name.substring(0, name.length() - 1)).inverse();
        } else {
            return allPermutations.get(name);
        }
    }
}
