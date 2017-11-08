package study;

import simulation.lib.counter.DiscreteConfidenceCounter;
import simulation.lib.counter.DiscreteCounter;
import simulation.lib.randVars.RandVar;
import simulation.lib.randVars.continous.Exponential;
import simulation.lib.randVars.continous.HyperExponential;
import simulation.lib.randVars.continous.Normal;
import simulation.lib.randVars.continous.Uniform;
import simulation.lib.rng.RNG;
import simulation.lib.rng.StdRNG;

/*
 * TODO Problem 3.1.3 and 3.1.4 - implement this class
 */
public class DCCTest {

    public static void main(String[] args) {
        testDCC();
    }

    public static void testDCC() {
    	for(int xx=0;xx<=2;xx++){ //cvar
    		for(int y=0;y<=3;y++){//nsamples
    	RNG rng = new StdRNG();
    	//RandVar rv = new Normal(rng);
    	int mean = 10;
    	int nexperiments = 500;
    	double[] cvar = new double[] {0.25, 0.5, 1, 2, 4};

    	int[] nsamples = new int[] {5,10,50,100};

    	double[] alphas = new double[] {0.1, 0.05};

    	boolean meanInIntval;
    	int meansInInterval = 0;
    	
    	
    	double currcvar = cvar[xx];
    	int currnsamples = nsamples[y];
    	double curralpha = alphas[0];

    	//RandVar rv= new HyperExponential(rng, mean, currcvar);
    	RandVar rv = new Exponential(rng, mean);
    	rv.setMean(mean);
    	double x=0;

    	DiscreteConfidenceCounter dct = new DiscreteConfidenceCounter ("testing", "uniform", 0.05);
        rv.setCvar(currcvar);
    	for (int j = 0; j < nexperiments; j++) {
            DiscreteConfidenceCounter dcc = new DiscreteConfidenceCounter ("testing", "uniform", curralpha);
    		for (int i=0; i<currnsamples; i++) 	{
        		x=rv.getRV();
        		//System.out.print(x+",");
        		dcc.count(x);
        	}
    		dcc.report();
    		meanInIntval = (dcc.getLowerBound()<= mean && dcc.getUpperBound() >= mean);
        	if (meanInIntval)meansInInterval++;	
        	//System.out.println("---"+dcc.getLowerBound()+"+++"+dcc.getUpperBound()+"..."+meanInIntval+" "+meansInInterval);
    	}
        

    	System.out.println(currcvar + "&" + currnsamples + "&" + (1-curralpha) + "&" + (double)meansInInterval/nexperiments + "\\\\");
    	//System.out.println(dct.getRow());
    	//System.out.println(dc.getNumSamples());

        //System.out.println(dcc.report());
    		}
    	}
    }
}
