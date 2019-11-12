
package pe.unsa.fuzzyclassifier.learner.stage1;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import pe.unsa.fuzzyclassifier.core.FuzzyRule;
import pe.unsa.fuzzyclassifier.core.Mediator;
import pe.unsa.fuzzyclassifier.utils.ByteArrayWritable;

/**
 * Mapper class that generates a single rule for each map
 *
 * @version 1.0
 */
public class RulesGenerationMapper extends Mapper<Text, Text, ByteArrayWritable, ByteArrayWritable>{
    
	private byte[] labels;
	private byte[] antecedents;
	private int i;
	private long startMs, endMs;
	
	@Override
	protected void cleanup (Context context) throws IOException, InterruptedException{
		
		// Write execution time
		endMs = System.currentTimeMillis();
		long mapperID = context.getTaskAttemptID().getTaskID().getId();
		try {
        	FileSystem fs = FileSystem.get(Mediator.getConfiguration());
        	Path file = new Path(Mediator.getHDFSLocation()+"/"+Mediator.getLearnerOutputPath()+"/"+Mediator.TIME_STATS_DIR+"/stage1_mapper"+mapperID+".txt");
        	OutputStream os = fs.create(file);
        	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        	bw.write("Execution time (seconds): "+((endMs-startMs)/1000));
        	bw.close();
        	os.close();
        }
        catch(Exception e){
        	System.err.println("\nSTAGE 1: ERROR WRITING EXECUTION TIME");
			e.printStackTrace();
        }
		
	}
	
	@Override
    public void map(Text key, Text value, Context context) throws IOException, InterruptedException {

        labels = FuzzyRule.getRuleFromExample(key.toString()+value.toString());
        antecedents = new byte[labels.length-1];
        for (i = 0; i < antecedents.length; i++)
        	antecedents[i] = labels[i];
        
        /*
    	 * Key: Antecedents of the rule
    	 * Value: Class of the rule
    	 */
        context.write(new ByteArrayWritable(antecedents), new ByteArrayWritable(labels[labels.length-1]));
        
    }
	
	@Override
	protected void setup(Context context) throws InterruptedException, IOException{

		super.setup(context);
		
		try {
			Mediator.setConfiguration(context.getConfiguration());
			Mediator.readLearnerConfiguration();
		}
		catch(Exception e){
			System.err.println("\nSTAGE 1: ERROR READING CONFIGURATION\n");
			e.printStackTrace();
			System.exit(-1);
		}
		
		startMs = System.currentTimeMillis();
	
	}
    
}
