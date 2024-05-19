import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public class BoostingAlgorithm {
    Clustering cluster;
    double[] weights;
    int[][] reducedInput;
    int[] labels;
    LinkedList<WeakLearner> learners = new LinkedList<>();

    // create the clusters and initialize your data structures
    public BoostingAlgorithm(int[][] input, int[] labels, Point2D[] locations, int k) {
        int len = input.length;
        this.labels = labels;
        cluster = new Clustering(locations, k);
        weights = new double[len];
        double weight = 1.0 / len;
        reducedInput = new int[len][];
        for (int i = 0; i < len; i++) {
            weights[i] = weight;
            reducedInput[i] = cluster.reduceDimensions(input[i]);
        }
    }

    private void normalize(double[] arr) {
        double sum = 0.0;
        for (double d : arr) {
            sum += d;
        }
        for (int i = 0; i < arr.length - 1; i++) {
            arr[i] /= sum;
        }
    }

    // return the current weight of the ith point
    public double weightOf(int i) {
        return weights[i];
    }

    // apply one step of the boosting algorithm
    public void iterate() {
        WeakLearner learner = new WeakLearner(reducedInput, weights, labels);
        for (int i = 0; i < reducedInput.length; i++) {
            if (learner.predict(reducedInput[i]) != labels[i]) {
                weights[i] *= 2;
            }
        }
        learners.add(learner);
        normalize(weights);
    }

    // return the prediction of the learner for a new sample
    public int predict(int[] sample) {
        int ctr1 = 0;
        int ctr2 = 0;
        for (WeakLearner learner : learners) {
            int prediction = learner.predict(cluster.reduceDimensions(sample));
            if (prediction == 0) {
                ctr1++;
            }
            else {
                ctr2++;
            }
        }
        // System.out.printf("ctr1: %d, ctr2 %d\n", ctr1, ctr2);
        if (ctr1 >= ctr2) {
            return 0;
        }
        else {
            return 1;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        // read in the terms from a file
        DataSet training = new DataSet(args[0]);
        DataSet testing = new DataSet(args[1]);
        int k = Integer.parseInt(args[2]);
        int T = Integer.parseInt(args[3]);

        int[][] trainingInput = training.getInput();
        int[][] testingInput = testing.getInput();
        int[] trainingLabels = training.getLabels();
        int[] testingLabels = testing.getLabels();
        Point2D[] trainingLocations = training.getLocations();

        // train the model
        BoostingAlgorithm model = new BoostingAlgorithm(trainingInput, trainingLabels,
                                                        trainingLocations, k);
        for (int t = 0; t < T; t++)
            model.iterate();

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
}