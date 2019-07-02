package it.polimi.ingsw.server;

import java.util.HashMap;
import java.util.Map;

/**
 * util to get arguments from commandline. Linux style
 */
public class ArgumentNavigator {
    /**
     * map of argument-keys and argument-values
     * (fieldPrefix)argumentKew argumentValue
     */
    private Map<String, String> argumentMap;

    public ArgumentNavigator(String[] args, String fieldPrefix){
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

    /**
     *
     * @param fieldKey argument kew
     * @param def default return value
     * @return the argument value
     * @throws NumberFormatException if the argument value is not a integer
     */
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


    /**
     *
     * @param fieldKey argument kew
     * @param def default return value
     * @return argument value
     */
    public String getFieldAsStringorDefault(String fieldKey, String def){
        return argumentMap.getOrDefault(fieldKey, def);
    }

}


