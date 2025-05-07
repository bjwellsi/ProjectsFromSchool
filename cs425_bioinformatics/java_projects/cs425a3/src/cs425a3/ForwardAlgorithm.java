package cs425a3;

public class ForwardAlgorithm {

	static double emissionProbs[][] = new double[3][3];
	static double transitionProbs[][] = new double[3][3];
	static int hyper = 1;
	static int hypo = 2;
	static int high = 1;
	static int low = 2;

	public static void main(String [] args) {
		emissionProbs[hyper][high] = 0.8;
		emissionProbs[hyper][low] = 0.2;
		emissionProbs[hypo][high] = 0.2;
		emissionProbs[hypo][low] = 0.8;
		transitionProbs[hyper][hyper] = 0.9;
		transitionProbs[hyper][hypo] = 0.1;
		transitionProbs[hypo][hypo] = 0.9;
		transitionProbs[hypo][hyper] = 0.1;
		transitionProbs[0][hyper] = 0.5;
		transitionProbs[0][hypo] = 0.5;
		double observed[] = { 0.1, 0.3, 0.2, 0.4, 0.7, 0.9, 1, 1, 0.8, 0.9 };
		
		int n = observed.length;
		double[][] probs = new double[3][n + 1];
		probs[0][0] = 1;
		for(int i = 1; i < 3; i++) {
			probs[0][i] = 0;
			probs[i][0] = 0;
		}
		// m,n = p(observed given n) * sum(n-1, all m) *
		probs[hyper][1] = 0.5 * emissionProb(hyper, observed[0]);
		probs[hypo][1] = 0.5 * emissionProb(hypo, observed[0]);
		for (int prob = 2; prob < n + 1; prob++) {
			for (int state = 1; state < 3; state++) {
				double sumProbs = 0;
				for(int i = 1; i < 3; i++) {
					sumProbs += transitionProbs[i][state] * probs[i][prob - 1];
				}
				probs[state][prob] = sumProbs * emissionProb(state, observed[prob - 1]);
			}
		}
		System.out.println("P(x):" + probs[hypo][n] + probs[hyper][n]);
		System.out.println("ln of previous result:" + Math.log(probs[hypo][n] + probs[hyper][n]));
		
		double backward [][] = new double[3][n + 1];
		for(int i = 0; i < n  + 1; i++) {
			backward[0][i] = 0;
		}
		for(int i = 0; i < 3; i++) {
			backward[i][n] = 1;
		}
		
		for(int col = n-1/*because last col is all ones*/; col > 0; col--) {
			for(int state = 1; state < 3; state++) {
				double sum = 0;
				for(int stated = 1; stated < 3; stated++) {
					sum += emissionProb(stated, observed[col]) * backward[stated][col+1] * transitionProbs[state][stated];
				}
				backward[state][col] = sum;
			}
		}
		
		double px = 0;
		for (int r = 1; r < 3; r++) {
			px += transitionProbs[0][r] * emissionProb(r,observed[0]) * backward[r][1];
		}
		
		System.out.println("Probability of emitting 5th cytosine:" + backward[hyper][6] * probs[hyper][6] / px);
		System.out.println("ln of previous result:" + Math.log(backward[hyper][5] * probs[hyper][5] / px));
	}
	
	

	public static double emissionProb(int state, double emission) {
		if(emission < 0.5)
			return emissionProbs[state][low];
		else return emissionProbs[state][high];
	}
	
	public static void backward() {
		
	}
}
