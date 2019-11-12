
package pe.unsa.fuzzyclassifier.learner.stage1;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.hadoop.mapreduce.Reducer;

import pe.unsa.fuzzyclassifier.utils.ByteArrayWritable;

/**
 * Reducer class used to gather rules
 * @version 1.0
 */
public class RulesGenerationReducer extends Reducer<ByteArrayWritable, ByteArrayWritable,ByteArrayWritable, ByteArrayWritable> {

	private Iterator<ByteArrayWritable> iterator;
	private HashSet<Byte> aggregatedBytesTmp;
	private ByteArrayWritable currentBytes;
	private byte[] aggregatedBytes;
	private int i;
	
    @Override
    public void reduce(ByteArrayWritable key, Iterable<ByteArrayWritable> values, Context context) throws IOException, InterruptedException {
        
    	iterator = values.iterator(); // class labels
    	
    	// Put all class labels into a single array
    	aggregatedBytesTmp = new HashSet<Byte>();
    	while (iterator.hasNext()){
    		currentBytes = iterator.next();
    		for (byte currentByte:currentBytes.getBytes())
    				aggregatedBytesTmp.add(currentByte);
    	}
    	aggregatedBytes = new byte[aggregatedBytesTmp.size()];
    	i = 0;
    	for (Byte currentByte:aggregatedBytesTmp) {
    		aggregatedBytes[i] = currentByte.byteValue();
    		i++;
    	}
    	
    	/*
    	 * Key: Antecedents of the rule
    	 * Value: Classes of the rule (conflicts)
    	 */
    	context.write(key, new ByteArrayWritable(aggregatedBytes));
        
    }
    
}

