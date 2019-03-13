package kz.yergalizhakhan.kmeans;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Algorithm {

    public static final String FILE_PATH = "/Users/zhakhanyergali/IdeaProjects/kMeans/src/kz/yergalizhakhan/kmeans/data.cvs";

    private double [][] allRecordsDataSet;
    private int [] generatedClusterLabels;

    private double [][] centroids;
    private int numRows, numDis;
    private int numClusters;

    public static void main(String[] args) {

        Algorithm alg = new Algorithm(FILE_PATH);
        alg.clustering(2,10,null);
        alg.printResults();
    }

    public Algorithm(String fileName) {
        BufferedReader reader;
        CsvHelper csv = new CsvHelper();
        ArrayList<String> values;
        // Creates a new KMeans object by reading in all of the records that are stored in a csv file
        try {
            reader = new BufferedReader(new FileReader(fileName));

            // get the number of rows
            numRows = 1;
            values = csv.parseLine(reader);
            numDis = values.size();
            while(reader.readLine()!=null)
                numRows++;
            reader.close();
            System.out.println(numRows + " " + numDis);

            // initialize the allRecordsDataSet variable
            allRecordsDataSet = new double[numRows][];
            for (int i = 0; i< numRows; i++)
                allRecordsDataSet[i] = new double[numDis];

            // read records from the csv file
            reader = new BufferedReader(new FileReader(fileName));
            int nrow=0;
            while ((values = csv.parseLine(reader))!=null){
                double [] dv = new double[values.size()];
                for (int i=0; i< values.size(); i++){
                    dv[i] = Double.parseDouble(values.get(i));
                }
                allRecordsDataSet[nrow] = dv;
                nrow ++;
            }
            reader.close();
            System.out.println("loaded data");
        }
        catch(Exception e) {
            System.out.println( e );
            System.exit( 0 );
        }
    }

    public void clustering(int numClusters, int niter, double [][] centroids) {
        this.numClusters = numClusters;
        if (centroids !=null)
            this.centroids = centroids;
        else{
            // randomly selected centroids
            this.centroids = new double[this.numClusters][];

            ArrayList idx = new ArrayList();
            for (int i=0; i<numClusters; i++){
                int c;
                do{
                    c = (int) (Math.random()* numRows);
                }while(idx.contains(c)); // avoid duplicates
                idx.add(c);

                // copy the value from allRecordsDataSet[c]
                this.centroids[i] = new double[numDis];
                for (int j = 0; j< numDis; j++)
                    this.centroids[i][j] = allRecordsDataSet[c][j];
            }
            System.out.println("selected random centroids");
        }

        double [][] c1 = this.centroids;
        double threshold = 0.001;
        int round=0;

        while (true){
            // update centroids with the last round results
            this.centroids = c1;

            //assign record to the closest centroid
            generatedClusterLabels = new int[numRows];
            for (int i = 0; i< numRows; i++){
                generatedClusterLabels[i] = closest(allRecordsDataSet[i]);
            }

            // recompute centroids based on the assignments
            c1 = updateCentroids();
            round ++;
            if ((niter >0 && round >=niter) || converge(this.centroids, c1, threshold))
                break;
        }

        System.out.println("Clustering converges at round " + round);
    }

    // find the closest centroid for the record v
    private int closest(double [] v){
        double mindist = dist(v, centroids[0]);
        int label =0;
        for (int i = 1; i< numClusters; i++){
            double t = dist(v, centroids[i]);
            if (mindist>t){
                mindist = t;
                label = i;
            }
        }
        return label;
    }

    // compute Euclidean distance between two vectors v1 and v2
    private double dist(double [] v1, double [] v2){
        double sum=0;
        for (int i = 0; i< numDis; i++){
            double d = v1[i]-v2[i];
            sum += d*d;
        }
        return Math.sqrt(sum);
    }

    // according to the cluster labels, recompute the centroids
    // the centroid is updated by averaging its members in the cluster.
    // this only applies to Euclidean distance as the similarity measure.

    private double [][] updateCentroids() {
        // initialize centroids and set to 0
        double [][] newc = new double [numClusters][]; //new centroids
        int [] counts = new int[numClusters]; // sizes of the clusters

        // intialize
        for (int i = 0; i< numClusters; i++){
            counts[i] =0;
            newc[i] = new double [numDis];
            for (int j = 0; j< numDis; j++)
                newc[i][j] =0;
        }


        for (int i = 0; i< numRows; i++){
            int cn = generatedClusterLabels[i]; // the cluster membership id for record i
            for (int j = 0; j< numDis; j++){
                newc[cn][j] += allRecordsDataSet[i][j]; // update that centroid by adding the member data record
            }
            counts[cn]++;
        }

        // finally get the average
        for (int i = 0; i< numClusters; i++){
            for (int j = 0; j< numDis; j++){
                newc[i][j]/= counts[i];
            }
        }

        return newc;
    }

    // check convergence condition
    // max{dist(c1[i], c2[i]), i=1..numClusters < threshold
    private boolean converge(double [][] c1, double [][] c2, double threshold){
        // c1 and c2 are two sets of centroids
        double maxv = 0;
        for (int i = 0; i< numClusters; i++){
            double d= dist(c1[i], c2[i]);
            if (maxv<d)
                maxv = d;
        }

        if (maxv <threshold)
            return true;
        else
            return false;

    }

    public void printResults(){
        System.out.println("Label:");
        for (int i = 0; i< numRows; i++)
            System.out.println(generatedClusterLabels[i]);
        System.out.println("Centroids:");
        for (int i = 0; i< numClusters; i++){
            for(int j = 0; j< numDis; j++)
                System.out.print(centroids[i][j] + " ");
            System.out.println();
        }
    }
}
