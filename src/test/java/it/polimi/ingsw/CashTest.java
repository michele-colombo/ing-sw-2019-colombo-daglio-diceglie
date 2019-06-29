package it.polimi.ingsw;


import it.polimi.ingsw.server.model.Cash;
import it.polimi.ingsw.server.model.enums.AmmoColor;
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


    /**
     * Prepare variables of the test
     */
    @BeforeEach
    public void prepareTest(){
        c1 = new Cash(1,2,3);
        c2 = new Cash(2, 3,4);
        c3 = new Cash(1,1,10);
        c4 = new Cash(0,0,0);
        c5 = new Cash(2, 3, 4);
        c6 = new Cash(0,0,0);
        c7 = new Cash(1,1,1);
    }


    /**
     * Test greaterEqual method when it has to return true
     */
    @Test
    public void greaterEqualTrue() {
        assertTrue(c2.greaterEqual(c1));
        assertTrue(c2.greaterEqual(c5));
        assertTrue(c5.greaterEqual(c2));
        assertTrue(c2.greaterEqual(c2));
        assertTrue(c2.greaterEqual(c4));
        assertTrue(c1.greaterEqual(c4));
    }

    /**
     * Test greaterEqual method when it has to return false
     */
    @Test
    public void greaterEqualFalse(){
        assertFalse(c1.greaterEqual(c2));
        assertFalse(c3.greaterEqual(c2));
        assertFalse(c2.greaterEqual(c3));
    }

    /**
     * Test lessEqual method when it has to return true
     */
    @Test
    public void lessEqualTrue() {
        assertTrue(c1.lessEqual(c2));
        assertTrue(c2.lessEqual(c5));
        assertTrue(c5.lessEqual(c2));
        assertTrue(c2.lessEqual(c2));
        assertTrue(c4.lessEqual(c2));
        assertTrue(c4.lessEqual(c1));
    }

    /**
     * Test lessEqual method when it has to return false
     */
    @Test
    public void lessEqualFalse(){
        assertFalse(c2.lessEqual(c1));
        assertFalse(c3.lessEqual(c2));
        assertFalse(c2.lessEqual(c3));
    }

    /**
     * Test isEqual method when it has to return true
     */
    @Test
    public void isEqualTrue() {
        c2.set(c3);
        assertTrue(c2.isEqual(c3));
        assertTrue(c3.isEqual(c2));
    }

    /**
     * Test isEqual method when it has to return false
     */
    @Test
    public void isEqualFalse(){
        assertFalse(c1.isEqual(c3));
        assertFalse(c3.isEqual(c1));
    }

    /**
     * Test isEqual method when it has to return false
     */
    @Test
    public void subtract() {

        assertTrue(c1.subtract(c6).isEqual(c1));
        assertTrue(c2.subtract(c7).isEqual(c1));
    }

    /**
     * Test sum method
     */
    @Test
    public void sum() {
        assertTrue(c1.sum(c6).isEqual(c1));
        assertTrue(c6.sum(c1).isEqual(c1));
        assertTrue(c1.sum(c7).isEqual(c2));
        assertTrue(c7.sum(c1).isEqual(c2));
    }

    /**
     * Test pay method when it has to return true
     */
    @Test
    public void payTrue() {
        assertTrue(c2.pay(c1));
        assertTrue(c5.pay(c1));
        assertTrue(c1.pay(c1));
    }

    /**
     * Test pay method when it has to return false
     */
    @Test
    public void payFalse() {
        assertFalse(c1.pay(c2));
    }

    /**
     * Test deposit method
     */
    @Test
    public void deposit() {
        c1.deposit(c2);
        assertTrue(c1.isEqual(new Cash(3,3,3)));
    }

    /**
     * Test getTotal method
     */
    @Test
    public void getTotal() {
        assertEquals(6, c1.getTotal());
        assertEquals(9, c2.getTotal());
        assertEquals(12, c3.getTotal());
        assertEquals(0, c4.getTotal());
    }

    /**
     * Test containsColor when it has to return true
     */
    @Test
    public void containsColorTrue(){
        assertTrue(c1.containsColor(AmmoColor.BLUE));
        assertTrue(c1.containsColor(AmmoColor.RED));
        assertTrue(c1.containsColor(AmmoColor.YELLOW));
    }

    /**
     * Test containsColor when it has to return false
     */
    @Test
    public void containsColorFalse(){
        assertFalse(c4.containsColor(AmmoColor.BLUE));
        assertFalse(c4.containsColor(AmmoColor.RED));
        assertFalse(c4.containsColor(AmmoColor.YELLOW));
    }

    /**
     * Test setZero method
     */
    @Test
    public void setZero() {
        Cash c1 = new Cash (1,2,3);
        assertFalse(c1.isEqual(new Cash(0,0,0)));
        c1.setZero();
        assertTrue(c1.isEqual(new Cash(0,0,0)));
    }
}