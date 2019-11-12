
package pe.unsa.fuzzyclassifier.learner.stage2;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;

import pe.unsa.fuzzyclassifier.utils.FrequentSubsetWritable;

/**
 * Reducer class that adds the number of occurrences of a given subset
 *
 * @version 1.0
 */
public class FrequentSubsetsCombiner extends Reducer<FrequentSubsetWritable, LongWritable, FrequentSubsetWritable, LongWritable> {

	private Iterator<LongWritable> iterator;
	private long count;
	private int i;
	
    @Override
    public void reduce(FrequentSubsetWritable key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
        
    	iterator = values.iterator(); // class labels
    	
    	count = 0;
    	while (iterator.hasNext())
    		count += iterator.next().get();
    	
    	/*
    	 * Key: Subset of antecedents
    	 * Value: Occurrences
    	 */
    	context.write(key, new LongWritable(count));
        
    }
    
}
