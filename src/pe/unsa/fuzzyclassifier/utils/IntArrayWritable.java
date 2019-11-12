package pe.unsa.fuzzyclassifier.utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

import org.apache.hadoop.io.Writable;

/**
 * Implementation of a serializable int array
 */
public class IntArrayWritable implements Writable, Serializable {
    
	private int[] data;
	
	/**
     * Default constructor
     */
    public IntArrayWritable() {}

	/**
     * Constructs a new serializable array from the input int array
     * @param data input int array
     */
    public IntArrayWritable(int[] data) {
        this.data = data;
    }

    /**
     * Returns the int array
     * @return int array
     */
    public int[] getData() {
        return data;
    }
    
    @Override
    public void readFields(DataInput in) throws IOException {
    	
        int length = in.readInt();

        data = new int[length];

        for(int i = 0; i < length; i++)
            data[i] = in.readInt();
        
    }

    /**
     * Sets the int array
     * @param data input int array
     */
    public void setData(int[] data) {
        this.data = data;
    }

    @Override
    public String toString(){
    	
    	String output = "data: ";
    	
    	for (int i = 0; i < data.length; i++)
    		output += data[i] + " | ";
    	
    	return output;
    	
    }
    
    @Override
    public void write(DataOutput out) throws IOException {
    	
        int length = 0;
        
        if(data != null)
            length = data.length;

        out.writeInt(length);

        for(int i = 0; i < length; i++)
            out.writeInt(data[i]);
        
    }
    
}
