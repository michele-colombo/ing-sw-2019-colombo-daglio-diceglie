package it.polimi.ingsw;

import org.junit.Test;
import sun.util.resources.cldr.zh.CalendarData_zh_Hans_SG;

import static org.junit.Assert.*;

public class CashTest {

    @Test
    public void greaterEqual() {
        Cash c1 = new Cash (1,2,3);
        Cash c2 = new Cash(2, 3,4);
        Cash c3 = new Cash(1,1,10);
        Cash c4 = new Cash(0,0,0);
        Cash c5 = new Cash(2, 3, 4);
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
        Cash c1 = new Cash (1,2,3);
        Cash c2 = new Cash(2, 3,4);
        Cash c3 = new Cash(1,1,10);
        Cash c4 = new Cash(0,0,0);
        Cash c5 = new Cash(2, 3, 4);
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
        Cash c1 = new Cash(2, 3,1);
        Cash c2 = new Cash(2, 3, 1);
        assertTrue(c1.isEqual(c2));
        assertTrue(c2.isEqual(c1));
        Cash c3 = new Cash (1, 2, 3);
        assertFalse(c1.isEqual(c3));
        assertFalse(c3.isEqual(c1));
    }

    @Test
    public void subtract() {
        Cash c1 = new Cash (1,2,3);
        Cash c2 = new Cash(2, 3,4);
        Cash c3 = new Cash(1,1,10);
        Cash c4 = new Cash(0,0,0);
        Cash c5 = new Cash(2, 3, 4);
        Cash c6 = new Cash(1,1,1);
        assertTrue(c6.isEqual(c2.subtract(c1)));
        assertTrue(c2.isEqual(new Cash(2, 3,4)));
        assertTrue(c1.isEqual(new Cash(1,2,3)));
        assertTrue(c6.isEqual(c2.subtract(c1)));
        assertTrue(c1.isEqual(c1.subtract(c4)));
        assertTrue(c2.subtract(c5).isEqual(new Cash(0,0,0)));
        assertTrue(c3.subtract(c6).isEqual(new Cash(0,0,9)));
    }

    @Test
    public void sum() {
    }

    @Test
    public void pay() {
        Cash c1 = new Cash (1,2,3);
        Cash c2 = new Cash(2, 3,4);

        assertFalse(c1.pay(c2));
        assertTrue(c1.isEqual(c1));
        assertTrue(c2.isEqual(c2));

        c1 = new Cash (1,2,3);
        c2 = new Cash(2, 3,4);
        Cash c6 = new Cash(1,1,1);

        assertTrue(c2.pay(c1));
        assertTrue(c1.isEqual(c1));
        assertTrue(c2.isEqual(c6));


        c2 = new Cash(2, 3,4);
        Cash c4 = new Cash(0,0,0);
        Cash c5 = new Cash(2, 3, 4);

        assertFalse(c4.pay(c6));

        assertTrue(c5.pay(c2));
        assertTrue(c5.isEqual(c4));
        assertTrue(c2.isEqual(c2));

        c2 = new Cash(2, 3,5);
        c5 = new Cash(2, 3, 4);

        assertFalse(c5.pay(c2));
        assertTrue(c5.isEqual(c5));
        assertTrue(c2.isEqual(c2));

    }

    @Test
    public void deposit() {
        Cash c1 = new Cash (1,2,3);
        Cash c2 = new Cash(2, 3,4);
        Cash c3 = new Cash(1,1,2);
        Cash c4 = new Cash(0,0,0);
        Cash c5 = new Cash(3, 3, 3);
        Cash c6 = new Cash(1,1,1);

        c2.deposit(c4);
        assertTrue(c2.isEqual(new Cash(2,3,3)));
        assertTrue(c4.isEqual(c4));
        c3.deposit(c6);
        assertTrue(c3.isEqual(new Cash(2,2,3)));
        assertTrue(c6.isEqual(c6));
        c5.deposit(c4);
        assertTrue(c5.isEqual(c5));
        assertTrue(c4.isEqual(c4));
        c1.deposit(c4);
        assertTrue(c1.isEqual(c1));
        assertTrue(c4.isEqual(c4));
    }

    @Test
    public void getTotal() {
        Cash c1 = new Cash (1,2,3);
        Cash c2 = new Cash(2, 3,4);
        Cash c3 = new Cash(1,1,2);
        Cash c4 = new Cash(0,0,0);
        assertEquals(c1.getTotal(), 6);
        assertEquals(c2.getTotal(), 9);
        assertEquals(c3.getTotal(), 4);
        assertEquals(c4.getTotal(), 0);
    }

    @Test
    public void setZero() {
        Cash c1 = new Cash (1,2,3);
        assertFalse(c1.isEqual(new Cash(0,0,0)));
        c1.setZero();
        assertTrue(c1.isEqual(new Cash(0,0,0)));
    }
}