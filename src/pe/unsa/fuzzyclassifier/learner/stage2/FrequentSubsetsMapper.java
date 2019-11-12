package pe.unsa.fuzzyclassifier.learner.stage2;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

import pe.unsa.fuzzyclassifier.core.Mediator;
import pe.unsa.fuzzyclassifier.utils.ByteArrayWritable;
import pe.unsa.fuzzyclassifier.utils.FrequentSubsetWritable;

/**
 * Mapper class that increments the counter of occurrences of a given subset of antecedents
 */
public class FrequentSubsetsMapper extends Mapper<ByteArrayWritable, ByteArrayWritable, FrequentSubsetWritable, LongWritable>{
    
	private LongWritable one = new LongWritable(1);
	private byte[] antecedents;
	private byte[] antsSubset;
	private int[][] splitsIndices;
	private int[] splitsLength;
	private int i, j, numSplit;
	
	@Override
    public void map(ByteArrayWritable key, ByteArrayWritable value, Context context) throws IOException, InterruptedException {

		antecedents = key.getBytes();
		
		// Split the rule in subsets of antecedents
		for (numSplit = 0; numSplit < Mediator.getNumRuleSplits(); numSplit++){
		
			j = 0;
			antsSubset = new byte[splitsLength[numSplit]];
	        for (i = splitsIndices[numSplit][0]; i <= splitsIndices[numSplit][1]; i++){
	        	antsSubset[j] = antecedents[i];
	        	j++;
	        }
	        
	        /*
	    	 * Key: Subset of antecedents of the rule
	    	 * Value: 1 occurrence
	    	 */
	        context.write(new FrequentSubsetWritable(antsSubset,numSplit), one);
        
		}
        
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
		
		splitsIndices = Mediator.getRuleSplitsIndices();
		splitsLength = new int[Mediator.getNumRuleSplits()];
		for (numSplit = 0; numSplit < Mediator.getNumRuleSplits(); numSplit++)
			splitsLength[numSplit] = splitsIndices[numSplit][1]-splitsIndices[numSplit][0]+1;
		
	}
    
}
