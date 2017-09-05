package PSO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.moeaframework.Executor;
import org.moeaframework.Instrumenter;
import org.moeaframework.analysis.collector.Accumulator;
import org.moeaframework.analysis.plot.Plot;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;

public class RunNSGAIIproblem {

	public static void main(String[] args) {
		//simulation times
		for(int i=0;i<1;i++){
			System.out.println("run: "+i);
			run();
		}
	}

	static void writeResults(String filename,String line){
		try {
			Writer output;
			output = new BufferedWriter(new FileWriter(filename, true));  //clears file every time
			output.append(line);
			output.close();
		} catch (IOException e) {
			System.err.println("Writing data error");
			// why does the catch need its own curly?
		}

	}

	static void run(){
		/**
		 * NSGAII
		 */
			Instrumenter instrumenter = new Instrumenter()
			.withProblemClass(NSGAIIproblem.class)
			.withFrequency(500)
			.attachAllMetricCollectors()
			.attachElapsedTimeCollector();
			
			NondominatedPopulation result = new Executor()
			.withAlgorithm("NSGAII")
			.withProblemClass(NSGAIIproblem.class)
			.withProperty("populationSize", 50)
			.withProperty("withReplacement", true)
			.withProperty("sbx.rate", 0.95)
			.withProperty("sbx.distributionIndex", 15.0)
			.withProperty("pm.rate", 0.05)
			.withProperty("pm.distributionIndex", 20.0)
			.distributeOnAllCores()
			.withInstrumenter(instrumenter)
			.withMaxEvaluations(5000)
			.run();


			Accumulator accumulator = instrumenter.getLastAccumulator();

			

			double[] objective1=new double[result.size()];
			double[] objective2=new double[result.size()];

			//Objectives results
			System.out.println("NSGAII:");
			for (Solution s : result){
				System.out.println(s.getVariable(0));
				objective1[result.indexOf(s)]=s.getObjective(0);
				objective2[result.indexOf(s)]=s.getObjective(1);
				System.out.println(s.getObjective(0)+","+s.getObjective(1)+"\n");
				writeResults("PSO/results/NSGA1",s.getObjective(0)+","+s.getObjective(1)+"\n");

			}

			//Algorithms Execution time
			for (int i=0; i<accumulator.size("NFE"); i++) {
				  System.out.println("NSGAII:"+accumulator.get("Elapsed Time", i).toString());
				  System.out.println("NSGAII:"+accumulator.get("GenerationalDistance", i).toString());
				  
				  
				  writeResults("PSO/results/NSGA1time",accumulator.get("Elapsed Time", i).toString()+"\n");

				}

			Plot p =new Plot();
			p.scatter("NSGAII", objective1, objective2);
			p.setXLabel("Cost");
			p.setYLabel("Response time");
			p.show();
	}
}