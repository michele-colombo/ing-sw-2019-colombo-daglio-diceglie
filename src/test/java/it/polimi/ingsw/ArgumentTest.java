package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

public class ArgumentTest {

    public static final String MERDA = "merda";

    @Test
    public void funziona(){
        ArgumentNavigator a= new ArgumentNavigator(new String[]{"-io", "sono", "-tu", "sei", "-egli", "e"} , "-");
        System.out.println(a.getFieldAsStringorDefault("io", MERDA));
        System.out.println(a.getFieldAsStringorDefault("tu", MERDA));
        System.out.println(a.getFieldAsStringorDefault("egli", MERDA));

        try{
            System.out.println(a.getFieldAsIntOrDefault("tu", -1));
        }
        catch (NumberFormatException e){
            System.out.println("help");
        }

        System.out.println(a.getFieldAsIntOrDefault("noi", 4));
        System.out.println(a.getFieldAsStringorDefault("voi", MERDA));

        ArgumentNavigator b= new ArgumentNavigator(new String[]{"-io", "sono", "-tu", "sei", "-egli", "e"}, "--");
        System.out.println(b.getFieldAsStringorDefault("qualcosa", "nafing"));
    }
}
