package pe.unsa.fuzzyclassifier.learner.stage2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import pe.unsa.fuzzyclassifier.core.Mediator;
import pe.unsa.fuzzyclassifier.utils.FrequentSubsetWritable;

/**
 * Modela el segundo MapReduce que calcula los subconjuntos más frecuentes de antecedentes
 */
public class Stage2 {
    
    /**
	 * Runs Stage 2
	 * @param args application arguments
	 * @param conf configuration object
	 * @version 1.0
	 */
    public static void runStage2 () throws Exception {
        
    	Configuration conf = Mediator.getConfiguration();
        
        /*
         * Prepare and run the job
         */
        Job job = Job.getInstance(conf);

        job.setJarByClass(Stage2.class);
        job.setMapperClass(FrequentSubsetsMapper.class);
        job.setCombinerClass(FrequentSubsetsCombiner.class);
        job.setReducerClass(FrequentSubsetsReducer.class);
        job.setInputFormatClass(SequenceFileInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        /*SequenceFileOutputFormat.setCompressOutput(job, true);
        SequenceFileOutputFormat.setOutputCompressorClass(job, BZip2Codec.class);
        SequenceFileOutputFormat.setOutputCompressionType(job, CompressionType.BLOCK);*/
        job.setOutputKeyClass(FrequentSubsetWritable.class);
        job.setOutputValueClass(LongWritable.class);
        job.setNumReduceTasks(1);
        FileInputFormat.addInputPath(job, new Path(Mediator.getHDFSLocation()+"/"+Mediator.getLearnerRuleBaseTmpPath()));
        FileOutputFormat.setOutputPath(job, new Path(Mediator.getHDFSLocation()+Mediator.getLearnerStage2OutputPath()));
        
        job.waitForCompletion(true);
        
    }
    
}

