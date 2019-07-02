package it.polimi.ingsw.server;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public class ArgumentNavigator {
    private String[] args;
    private Map<String, String> argumentMap;

    public ArgumentNavigator(String[] args, String fieldPrefix){
        this.args= args;
        argumentMap= new HashMap<>();

        int i= 0;
        while (i<args.length){
            String s= args[i];

            String field= null;
            String value= null;

            if(s.indexOf(fieldPrefix) == 0 && !s.equals(fieldPrefix)){
                field= s.substring(s.lastIndexOf(fieldPrefix) + 1);
                s= args[i+1];
                value= s;
                i++;
                argumentMap.put(field, value);
            }
            i++;

        }
    }

    public int getFieldAsIntOrDefault(String fieldKey, int def) throws NumberFormatException{
        String value= argumentMap.get(fieldKey);
        if(value != null){
            try{
                int result= Integer.parseInt(value);
                return result;
            }
            catch (NumberFormatException e){
                throw new NumberFormatException();
                //behave as if there isn't
            }
        }

        return def;
    }

    public String getFieldAsStringorDefault(String fieldKey, String def){
        return argumentMap.getOrDefault(fieldKey, def);
    }

}


