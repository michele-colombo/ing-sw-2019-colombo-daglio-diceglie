package it.polimi.ingsw;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CashTest {
    Cash c1;
    Cash c2;
    Cash c3;
    Cash c4;
    Cash c5;
    Cash c6;
    Cash c7;



    @BeforeEach
    public void prepareTest(){
        c1 = new Cash (1,2,3);
        c2 = new Cash(2, 3,4);
        c3 = new Cash(1,1,10);
        c4 = new Cash(0,0,0);
        c5 = new Cash(2, 3, 4);
        c6 = new Cash(0,0,0);
        c7 = new Cash(1,1,1);
    }


    @Test
    public void greaterEqual() {
        assertTrue(c2.greaterEqual(c1));
        assertTrue(c2.greaterEqual(c5));
        assertTrue(c5.greaterEqual(c2));
        assertTrue(c2.greaterEqual(c2));
        assertTrue(c2.greaterEqual(c4));
        assertTrue(c1.greaterEqual(c4));
        assertFalse(c1.greaterEqual(c2));
        assertFalse(c3.greaterEqual(c2));
        assertFalse(c2.greaterEqual(c3));
    }

    @Test
    public void lessEqual() {
        assertTrue(c1.lessEqual(c2));
        assertTrue(c2.lessEqual(c5));
        assertTrue(c5.lessEqual(c2));
        assertTrue(c2.lessEqual(c2));
        assertTrue(c4.lessEqual(c2));
        assertTrue(c4.lessEqual(c1));
        assertFalse(c2.lessEqual(c1));
        assertFalse(c3.lessEqual(c2));
        assertFalse(c2.lessEqual(c3));
    }

    @Test
    public void isEqual() {
        c2.set(c3);
        c2.isEqual(c3);
        c3.isEqual(c2);

        assertFalse(c1.isEqual(c3));
        assertFalse(c3.isEqual(c1));
    }

    @Test
    public void subtract() {

        assertTrue(c1.subtract(c6).isEqual(c1));
        assertTrue(c2.subtract(c7).isEqual(c1));
    }

    @Test
    public void sum() {
        assertTrue(c1.sum(c6).isEqual(c1));
        assertTrue(c6.sum(c1).isEqual(c1));
        assertTrue(c1.sum(c7).isEqual(c2));
        assertTrue(c7.sum(c1).isEqual(c2));
    }

    @Test
    public void pay() {

        assertFalse(c1.pay(c2));
        assertTrue(c2.pay(c1));
        assertTrue(c5.pay(c1));
        assertTrue(c1.pay(c1));
    }

    @Test
    public void deposit() {
        c1.deposit(c2);
        assertTrue(c1.isEqual(new Cash(3,3,3)));
    }

    @Test
    public void getTotal() {
        assertEquals(6, c1.getTotal());
        assertEquals(9, c2.getTotal());
        assertEquals(12, c3.getTotal());
        assertEquals(0, c4.getTotal());
    }

    @Test
    public void containsColor(){
        assertTrue(c1.containsColor(Color.BLUE));
        assertTrue(c1.containsColor(Color.RED));
        assertTrue(c1.containsColor(Color.YELLOW));
        assertFalse(c4.containsColor(Color.BLUE));
        assertFalse(c4.containsColor(Color.RED));
        assertFalse(c4.containsColor(Color.YELLOW));
    }

    @Test
    public void setZero() {
        Cash c1 = new Cash (1,2,3);
        assertFalse(c1.isEqual(new Cash(0,0,0)));
        c1.setZero();
        assertTrue(c1.isEqual(new Cash(0,0,0)));
    }
}