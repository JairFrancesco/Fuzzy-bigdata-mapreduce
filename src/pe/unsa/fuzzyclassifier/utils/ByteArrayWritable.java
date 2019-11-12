package pe.unsa.fuzzyclassifier.utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

import org.apache.hadoop.io.WritableComparable;

/**
 * Implementation of a serializable byte array
 */
public class ByteArrayWritable implements WritableComparable<ByteArrayWritable>, Serializable {
    
	private byte[] bytes;
	private int hash;
	
	/**
     * Default constructor
     */
    public ByteArrayWritable() {
    	this.hash = -1;
    }
    
    /**
     * Constructs a new serializable array of one element
     * @param data input byte
     */
    public ByteArrayWritable(byte data) {
        this.bytes = new byte[]{data};
        this.hash = -1;
    }

	/**
     * Constructs a new serializable array from the input byte array
     * @param data input byte array
     */
    public ByteArrayWritable(byte[] data) {
        this.bytes = data;
        this.hash = -1;
    }
    
    /*
    @Override
	public int compareTo(ByteArrayWritable o) {
    	return new String(bytes).compareTo(new String(o.getBytes()));
	}
	*/
    
    @Override
	public int compareTo(ByteArrayWritable o) {
    	int i = 0;
    	if (bytes.length == o.getBytes().length){
			while (i < bytes.length && bytes[i]==o.getBytes()[i])
				i++;
			if (i >= bytes.length)
				return 0;
		}
    	else if (bytes.length < o.getBytes().length){
			while (i < bytes.length && bytes[i]==o.getBytes()[i])
				i++;
			if (i >= bytes.length)
				return -1;
		}
		else{
			while (i < o.getBytes().length && bytes[i]==o.getBytes()[i])
				i++;
			if (i >= o.getBytes().length)
				return 1;
		}
		if (bytes[i] > o.getBytes()[i])
			return 1;
		else
			return -1;
	}
    
    @Override
	public boolean equals (Object obj){
    	
    	if (obj == this)
    		return true;
    	if (obj == null || obj.getClass() != this.getClass())
    		return false;
    	
    	int i = 0;
    	ByteArrayWritable o = (ByteArrayWritable)obj;
    	if (bytes.length == o.getBytes().length){
			while (i < bytes.length && bytes[i]==o.getBytes()[i])
				i++;
			if (i >= bytes.length)
				return true;
			else
				return false;
		}
    	else
    		return false;
    	
    }

    /**
     * Returns the byte array
     * @return byte array
     */
    public byte[] getBytes() {
        return bytes;
    }
    
    @Override
    public int hashCode(){
    	if (hash == -1)
    		hash =  new String(bytes).hashCode();
		return hash;
    }
    
    @Override
    public void readFields(DataInput in) throws IOException {
    	
        int length = in.readInt();

        bytes = new byte[length];

        for(int i = 0; i < length; i++)
            bytes[i] = in.readByte();
        
    }

    /**
     * Sets the byte array
     * @param data input byte array
     */
    public void setData(byte[] data) {
        this.bytes = data;
    }

    @Override
    public String toString(){
    	
    	String output = "Bytes: ";
    	
    	for (int i = 0; i < bytes.length; i++)
    		output += bytes[i] + " | ";
    	
    	return output;
    	
    }
    
    @Override
    public void write(DataOutput out) throws IOException {
    	
        int length = 0;
        
        if(bytes != null)
            length = bytes.length;

        out.writeInt(length);

        for(int i = 0; i < length; i++)
            out.writeByte(bytes[i]);
        
    }
    
}
