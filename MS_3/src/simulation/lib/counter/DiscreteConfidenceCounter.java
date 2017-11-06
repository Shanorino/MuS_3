package simulation.lib.counter;

/**
 * This class implements a discrete time confidence counter
 */
public class DiscreteConfidenceCounter extends DiscreteCounter{
    /*
     * TODO Problem 3.1.2 - implement this class according to the given class diagram!
     * Hint: see section 4.4 in course syllabus
     */
	private double alpha; // probability of true value lying outside the calculated interval
    /*	Row 1: degrees of freedom
     *  Row 2: alpha 0.01
     *  Row 3: alpha 0.05
     *  Row 4: alpha 0.10
     */
	
    private double tAlphaMatrix[][] = new double[][]{
            {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 1000000},
            {63.657, 9.925, 5.841, 4.604, 4.032, 3.707, 3.499, 3.355, 3.250, 3.169, 2.845, 2.750, 2.704, 2.678, 2.660, 2.648, 2.639, 2.632, 2.626, 2.576},
            {12.706, 4.303, 3.182, 2.776, 2.571, 2.447, 2.365, 2.306, 2.262, 2.228, 2.086, 2.042, 2.021, 2.009, 2.000, 1.994, 1.990, 1.987, 1.984, 1.960},
            {6.314, 2.920, 2.353, 2.132, 2.015, 1.943, 1.895, 1.860, 1.833, 1.812, 1.725, 1.697, 1.684, 1.676, 1.671, 1.667, 1.664, 1.662, 1.660, 1.645}};

    // Constructor with only a String
    public DiscreteConfidenceCounter (String variable) {
    	super(variable);
    	this.alpha = 0.05;
    }
    
    // Constructor with String and alpha
    public DiscreteConfidenceCounter (String variable, double alpha) {
    	super(variable);
    	this.alpha = alpha;
    }
    public DiscreteConfidenceCounter (String variable, String type, double alpha) {
    	super(variable, type);
    	this.alpha=alpha;
    }
    // for degsOfFreedom >=1
    public double getT(long degsOfFreedom) {
    	long numSamples = degsOfFreedom+1;
    	double tdistr;
    	int row = getRow();
    	int i;
    	for (i = 0; i<tAlphaMatrix[0].length; i++) {
    		if (degsOfFreedom==tAlphaMatrix[0][i])
    			return tAlphaMatrix[row][i];
    		if (degsOfFreedom < tAlphaMatrix[0][i]) {
    			break;
    		}
    	}
        if (degsOfFreedom > tAlphaMatrix[0][tAlphaMatrix.length-1] || degsOfFreedom < tAlphaMatrix[0][0]) 
        	return tAlphaMatrix[row][tAlphaMatrix.length-1];
        return linearInterpol(tAlphaMatrix[0][i-1],
        					tAlphaMatrix[0][i], 
        					tAlphaMatrix[row][i-1], 
        					tAlphaMatrix[row][i],
        					degsOfFreedom);
    	
    }
    
    private double linearInterpol(double dflow, double dfhigh, double tlow, double thigh, double degsOfFreedom) {
		double deldf = dfhigh-dflow;
		double dOFperc=(degsOfFreedom-dflow)/deldf;
		double delt = thigh-tlow;
		return tlow+delt*dOFperc;
	}

	private int getRow () {
    	if (alpha<0.05) return 1;
    	else if (alpha<0.1) return 2;
    	return 3;

    }
	public double getLowerBound() {
		double barx = sumPowerOne/getNumSamples();
		return barx-getBound();
	}
	public double getUpperBound() {
		double barx = sumPowerOne/getNumSamples();
		return barx+getBound();
	}
	public  double getBound() {
		long sampleNum=getNumSamples();
		//double ssqr = sumPowerTwo/(sampleNum-1);
		double var=getVariance();
		return getT(sampleNum-1)*Math.sqrt(var/sampleNum);
	}
    /**
     * @see Counter#report()
     * Uncomment this function when you have implemented this class for reporting.
     */
@Override
    public String report() {
        String out = super.report();
        out += ("" + "\talpha = " + alpha + "\n" +
                "\tt(1-alpha/2) = " + getT(getNumSamples() - 1) + "\n" +
                "\tlower bound = " + getLowerBound() + "\n" +
                "\tupper bound = " + getUpperBound());
        return out;
    }

    /**
     * @see Counter#csvReport(String)
     * Uncomment this function when you have implemented this class for reporting.
     */
    /*@Override
    public void csvReport(String outputdir) {
        String content = observedVariable + ";" + getNumSamples() + ";" + getMean() + ";" + getVariance() + ";" + getStdDeviation() + ";" +
                getCvar() + ";" + getMin() + ";" + getMax() + ";" + alpha + ";" + getT(getNumSamples() - 1) + ";" + getLowerBound() + ";" +
                getUpperBound() + "\n";
        String labels = "#counter ; numSamples ; MEAN; VAR; STD; CVAR; MIN; MAX;alpha;t(1-alpha/2);lowerBound;upperBound\n";
        writeCsv(outputdir, content, labels);
    }*/

}
