package pe.unsa.fuzzyclassifier.learner.stage1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import pe.unsa.fuzzyclassifier.core.Mediator;
import pe.unsa.fuzzyclassifier.utils.ByteArrayWritable;

/**
 * Modela el primer MapReduce que genera la base de reglas inicial (sin pesos de reglas)
 * @version 1.0
 */
public class Stage1 {
    
    /**
	 * Runs Stage 2
	 * @param args application arguments
	 * @param conf configuration object
	 * @version 1.0
	 */
    public static void runStage1 () throws Exception {
        
    	Configuration conf = Mediator.getConfiguration();
        
        /*
         * Prepare and run the job
         */
        Job job = Job.getInstance(conf);

        job.setJarByClass(Stage1.class);
        job.setMapperClass(RulesGenerationMapper.class);
        job.setCombinerClass(RulesGenerationReducer.class);
        job.setReducerClass(RulesGenerationReducer.class);
        job.setInputFormatClass(KeyValueTextInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        /*SequenceFileOutputFormat.setCompressOutput(job, true);
        SequenceFileOutputFormat.setOutputCompressorClass(job, BZip2Codec.class);
        SequenceFileOutputFormat.setOutputCompressionType(job, CompressionType.BLOCK);*/
        job.setOutputKeyClass(ByteArrayWritable.class);
        job.setOutputValueClass(ByteArrayWritable.class);
        FileInputFormat.addInputPath(job, new Path(Mediator.getHDFSLocation()+"/"+Mediator.getLearnerInputPath()));
        FileInputFormat.setMaxInputSplitSize(job, Mediator.computeHadoopSplitSize(
    		Mediator.getHDFSLocation()+"/"+Mediator.getLearnerInputPath(), Mediator.getHadoopNumMappers()));
        FileInputFormat.setMinInputSplitSize(job, Mediator.computeHadoopSplitSize(
    		Mediator.getHDFSLocation()+"/"+Mediator.getLearnerInputPath(), Mediator.getHadoopNumMappers()));
        FileOutputFormat.setOutputPath(job, new Path(Mediator.getHDFSLocation()+Mediator.getLearnerStage1OutputPath()));
        
        job.waitForCompletion(true);
        
        // Save rule base size
        long ruleBaseSize = job.getCounters().findCounter("org.apache.hadoop.mapred.Task$Counter", "REDUCE_OUTPUT_RECORDS").getValue();
        Mediator.saveLearnerRuleBaseSize((int)ruleBaseSize);
        
    }
    
}

