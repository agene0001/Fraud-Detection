import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

public class WeakLearner {
    private int dp = -1;
    private int sp = -1;
    private double vp = -1;

    // train the weak learner
    public WeakLearner(int[][] input, double[] weights, int[] labels) {
        int k = input[0].length;
        int n = input.length;
        // setup for associated attributes of input data. attribute=input[ind1][ind2]\

        double best = Integer.MIN_VALUE;
        for (int i = 0; i < k; i++) {
            ST<Integer, Double> attr = new ST<Integer, Double>();
            for (int j = 0; j < n; j++) {
                double inputVal = input[j][i];
                attr.put(j, inputVal);
            }
            for (int j = 0; j < n; j++) {
                double inputVal = attr.get(j);
                int label = labels[j];
                double ctrSameBelow = weights[j];
                double ctrDiffBelow = 0;
                double ctrSameUp = weights[j];
                double ctrDiffUp = 0;
                int half = 1;
                for (int keys : attr.keys()) {
                    if (keys != j) {
                        if (attr.get(keys) > inputVal) {
                            if (labels[keys] == label) ctrSameUp += weights[keys];
                            else ctrDiffUp += weights[keys];
                        }
                        else {
                            if (attr.get(keys) != inputVal) {
                                if (labels[keys] == label) ctrSameBelow += weights[keys];
                                else ctrDiffBelow += weights[keys];
                            }
                            else {
                                if (labels[keys] == label) ctrSameBelow += weights[keys];
                            }
                        }
                    }
                }
                double temp = Math.max(ctrSameBelow + ctrDiffUp, ctrDiffBelow + ctrSameUp);
                if (temp >= best) {
                    if (temp > best) {
                        vp = inputVal;
                        best = temp;
                        if (temp != ctrDiffBelow + ctrSameUp) sp = label;
                        else {
                            if (label == 1) sp = 0;
                            else sp = 1;
                        }
                        dp = i;
                    }
                    else {
                        if (i < dp) {
                            vp = inputVal;
                            best = temp;
                            if (temp != ctrDiffBelow + ctrSameUp) sp = label;
                            else {
                                if (label == 1) sp = 0;
                                else sp = 1;
                            }
                            dp = i;
                        }
                        if (i == dp) {
                            if (inputVal < vp) {
                                vp = inputVal;
                                best = temp;
                                if (temp != ctrDiffBelow + ctrSameUp) sp = label;
                                else {
                                    if (label == 1) sp = 0;
                                    else sp = 1;
                                }
                                dp = i;
                            }
                        }

                    }
                }
            }
        }
    }

    public void WeakLearner2(int[][] input, double[] weights, int[] labels) {
        int k = input[0].length;
        int n = input.length;
        // setup for associated attributes of input data. attribute=input[ind1][ind2]\

        double best = Integer.MIN_VALUE;
        for (int i = 0; i < k; i++) {
            ST<Integer, Double> attr = new ST<Integer, Double>();
            for (int j = 0; j < n; j++) {
                double inputVal = input[j][i];
                attr.put(j, inputVal);
            }
            for (int j = 0; j < n; j++) {
                double inputVal = attr.get(j);
                int label = labels[j];
                double ctrSameBelow = weights[j];
                double ctrDiffBelow = 0;
                double ctrSameUp = weights[j];
                double ctrDiffUp = 0;
                int half = 1;
                for (int keys : attr.keys()) {
                    if (keys != j) {
                        if (attr.get(keys) > inputVal) {
                            if (labels[keys] == label) ctrSameUp += weights[keys];
                            else ctrDiffUp += weights[keys];
                        }
                        else {
                            if (attr.get(keys) != inputVal) {
                                if (labels[keys] == label) ctrSameBelow += weights[keys];
                                else ctrDiffBelow += weights[keys];
                            }
                            else {
                                if (labels[keys] == label) ctrSameBelow += weights[keys];
                            }
                        }
                    }
                }
                double temp = Math.max(ctrSameBelow + ctrDiffUp, ctrDiffBelow + ctrSameUp);
                if (temp >= best) {
                    if (temp > best) {
                        vp = inputVal;
                        best = temp;
                        if (temp != ctrDiffBelow + ctrSameUp) sp = label;
                        else {
                            if (label == 1) sp = 0;
                            else sp = 1;
                        }
                        dp = i;
                    }
                    else {
                        if (i < dp) {
                            vp = inputVal;
                            best = temp;
                            if (temp != ctrDiffBelow + ctrSameUp) sp = label;
                            else {
                                if (label == 1) sp = 0;
                                else sp = 1;
                            }
                            dp = i;
                        }
                        if (i == dp) {
                            if (inputVal < vp) {
                                vp = inputVal;
                                best = temp;
                                if (temp != ctrDiffBelow + ctrSameUp) sp = label;
                                else {
                                    if (label == 1) sp = 0;
                                    else sp = 1;
                                }
                                dp = i;
                            }
                        }
                    }
                }
            }
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        // read in the terms from a file
        DataSet training = new DataSet(args[0]);
        DataSet testing = new DataSet(args[1]);
        // int k = Integer.parseInt(args[2]);
        int T = Integer.parseInt(args[3]);

        int[][] trainingInput = training.getInput();
        int[][] testingInput = testing.getInput();
        int[] trainingLabels = training.getLabels();
        int[] testingLabels = testing.getLabels();
        Point2D[] trainingLocations = training.getLocations();
        double[] weights = new double[trainingInput.length];
        for (int i = 0; i < trainingInput.length; i++) {
            weights[i] = 1;
        }
        // train the model
        WeakLearner model = new WeakLearner(trainingInput, weights, trainingLabels);


        // calculate the training data set accuracy
        double training_accuracy = 0;
        for (int i = 0; i < training.getN(); i++)
            if (model.predict(trainingInput[i]) == trainingLabels[i])
                training_accuracy += 1;
        training_accuracy /= training.getN();

        // calculate the test data set accuracy
        double test_accuracy = 0;
        for (int i = 0; i < testing.getN(); i++)
            if (model.predict(testingInput[i]) == testingLabels[i])
                test_accuracy += 1;
        test_accuracy /= testing.getN();

        StdOut.println("Training accuracy of model: " + training_accuracy);
        StdOut.println("Test accuracy of model: " + test_accuracy);
    }

    // return the prediction of the learner for a new sample
    public int predict(int[] sample) {
        if (sp == 0) {
            if (sample[dp] <= vp) return 0;
            else return 1;
        }
        else {
            if (sample[dp] <= vp) return 1;
            else return 0;
        }
    }

    // return the dimension the learner uses to separate the data
    public int dimensionPredictor() {
        return dp;
    }

    // return the value the learner uses to separate the data
    public double valuePredictor() {
        return vp;
    }

    // return the sign the learner uses to separate the data
    public int signPredictor() {
        return sp;
    }
}