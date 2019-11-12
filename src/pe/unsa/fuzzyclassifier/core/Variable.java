
package pe.unsa.fuzzyclassifier.core;

import java.io.Serializable;

/**
 * Represents a variable of the problem
 *
 * @version 1.0
 */
public abstract class Variable implements Serializable {
    
    /**
     * Variable name
     */
    private String name;
    
    /**
     * Creates a new variable
     * @param name variable name
     */
    protected Variable (String name){
    	
    	this.name = name;
    	
    }
    
    /**
     * Returns the variable label index corresponding to the input value
     * @param inputValue input value
     * @return Variable label index corresponding to the input value
     */
    public abstract byte getLabelIndex(String inputValue);
    
    /**
     * Returns the variable name
     * @return variable name
     */
    public String getName (){
        return name;
    }

}
