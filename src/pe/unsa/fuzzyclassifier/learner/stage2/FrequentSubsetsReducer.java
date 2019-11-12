package pe.unsa.fuzzyclassifier.learner.stage2;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;

import pe.unsa.fuzzyclassifier.core.Mediator;
import pe.unsa.fuzzyclassifier.utils.FrequentSubsetWritable;

/**
 * Clase reductora que agrega el número de ocurrencias de un subconjunto dado
 */
public class FrequentSubsetsReducer extends Reducer<FrequentSubsetWritable, LongWritable, FrequentSubsetWritable, LongWritable> {

	private Iterator<LongWritable> valueIterator;
	private long count;
	
    @Override
    public void reduce(FrequentSubsetWritable key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
        
    	valueIterator = values.iterator();
    	
    	count = 0;
    	while (valueIterator.hasNext())
    		count += valueIterator.next().get();
    	
    	System.out.println(key);
    	if (count >= Mediator.getMinFreqSubsetOccurrence())
    		context.write(key,new LongWritable(count));
        
    }
    
    @Override
	protected void setup(Context context) throws InterruptedException, IOException{

		super.setup(context);
		
		try {
			Mediator.setConfiguration(context.getConfiguration());
			Mediator.readLearnerConfiguration();
		}
		catch(Exception e){
			System.err.println("\nSTAGE 2: ERROR READING CONFIGURATION\n");
			e.printStackTrace();
			System.exit(-1);
		}
		
    }
    
}
