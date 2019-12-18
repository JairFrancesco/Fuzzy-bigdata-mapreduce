package pe.unsa.fuzzyclassifier.classifier;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.ByteWritable;
import org.apache.hadoop.mapreduce.Reducer;

import pe.unsa.fuzzyclassifier.core.Mediator;
import pe.unsa.fuzzyclassifier.utils.IntArrayWritable;

/**
 * Reducer class used to sum received confusion matrices
 *
 * @version 1.0
 */
public class ConfusionMatrixReducer extends Reducer<ByteWritable, IntArrayWritable,ByteWritable, IntArrayWritable> {

	private Iterator<IntArrayWritable> iterator;
	private IntArrayWritable currentClassRow, classRow; // Each reducer receives only the row of a given class
	private byte classIndex;
	
    @Override
    public void reduce(ByteWritable key, Iterable<IntArrayWritable> values, Context context) throws IOException, InterruptedException {
        
    	iterator = values.iterator();
    	
    	// Sum confusion matrices
    	classRow = new IntArrayWritable(new int[Mediator.getNumClasses()]);
    	while (iterator.hasNext()){
    		currentClassRow = iterator.next();
    		for (classIndex = 0; classIndex < Mediator.getNumClasses(); classIndex++)
    				classRow.getData()[classIndex] = classRow.getData()[classIndex] 
    						+ currentClassRow.getData()[classIndex];
    	}
    	
    	context.write(key, classRow);
        
    }
    
    @Override
	protected void setup(Context context) throws InterruptedException, IOException{

		super.setup(context);
		
		// Read configuration
		try {
			Mediator.setConfiguration(context.getConfiguration());
			Mediator.readClassifierConfiguration();
		}
		catch(Exception e){
			System.err.println("\nHITS REDUCER: ERROR READING CONFIGURATION\n");
			e.printStackTrace();
		}
		
    }
    
}

