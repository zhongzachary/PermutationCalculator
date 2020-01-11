package com.zhongzachary.permutation;

import org.junit.Test;

import static org.junit.Assert.*;

public class PermutationTest {
    
    @Test
    public void dim() {
        Permutation p1 = Permutation.inCycleNotation(new int[]{2,6});
        Permutation p2 = Permutation.inCycleNotation(new int[]{3,6});
        Permutation p3 = Permutation.composition(p2, p1, p2);
        assertEquals(6, p1.dim());
        assertEquals(3, p3.dim());
    }
    
    @Test
    public void inOneLineNotation() {
        Permutation p1 = Permutation.inOneLineNotation(2,4,1,3);
        assertEquals("[[1,2,4,3]]", p1.toCycle());
    }
    
    @Test
    public void inCycleNotation() {
        Permutation p1 = Permutation.inCycleNotation(new int[]{1,2}, new int[]{3,4,5});
        Permutation p2 = Permutation.inOneLineNotation(2,1,4,5,3);
        assertEquals(p2, p1);
        
        Permutation p3 = Permutation.inCycleNotation(new int[]{3,5});
        Permutation p4 = Permutation.inOneLineNotation(1,2,5,4,3);
        assertEquals(p4,p3);
    }
    
    @Test
    public void testToString() {
        Permutation.setPrintMode(Permutation.PrintMode.ONE_LINE);
        Permutation p1 = Permutation.inCycleNotation(new int[]{1,2}, new int[]{3,4,5});
        assertEquals("[2,1,4,5,3]", p1.toString());
        
        Permutation.setPrintMode(Permutation.PrintMode.CYCLE);
        assertEquals("[[1,2],[3,4,5]]", p1.toString());
    }
    
    @Test
    public void toOneLine() {
        Permutation p1 = Permutation.inCycleNotation(new int[]{1,2}, new int[]{3,4,5});
        assertEquals("[2,1,4,5,3]", p1.toString());
    }
    
    @Test
    public void toCycle() {
        Permutation p1 = Permutation.inCycleNotation(new int[]{1,2}, new int[]{3,4,5});
        assertEquals("[[1,2],[3,4,5]]", p1.toCycle());
    }
    
    @Test
    public void composition() {
        Permutation t12 = Permutation.inCycleNotation(new int[]{1,2});
        Permutation c = Permutation.inCycleNotation(new int[]{1,2,3});
        Permutation t23 = Permutation.inCycleNotation(new int[]{2,3});
        Permutation.setPrintMode(Permutation.PrintMode.ONE_LINE);
        assertEquals(t23, Permutation.composition(t12, c));
    }
    
    @Test
    public void testComposition() {
        Permutation t12 = Permutation.inCycleNotation(new int[]{1,2});
        Permutation c = Permutation.inCycleNotation(new int[]{1,2,3});
        Permutation t23 = Permutation.inCycleNotation(new int[]{3,2});
        assertEquals(Permutation.identity(), Permutation.composition(t23, t12, c));
        
    }
    
    @Test
    public void inverse() {
        Permutation p = Permutation.inCycleNotation(new int[]{3,2,4,6,5,7,1,8,9});
        assertEquals(Permutation.identity(), Permutation.composition(p, p.inverse()));
        assertEquals(Permutation.identity(), Permutation.composition(p.inverse(), p));
    }
    
    @Test
    public void invserse2() {
        Permutation s = Permutation.inCycleNotation(new int[]{1,5,6,3}, new int[]{2,7,8,9,4});
        Permutation t = Permutation.inCycleNotation(new int[]{1,2,3,4,5},new int[]{6,7,8,9});
        Permutation result = Permutation.inCycleNotation(new int[]{1,7,4,2}, new int[]{3,8,9,6,5});
        Permutation sti = Permutation.composition(s, t.inverse());
        Permutation tsti = Permutation.composition(t, sti);
        assertEquals(result, Permutation.composition(t, s, t.inverse()));
        assertEquals(result, tsti);
    }
}