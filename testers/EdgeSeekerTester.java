package testers;

import controller.DatabaseThread;
import controller.EdgeSeeker;
import model.Constants;
import model.Graph;
import model.Vertex;
import util.VertexInvalidException;
import util.VertexUnreachableException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by extradikke on 4-11-14.
 */
public class EdgeSeekerTester {
    private static ExecutorService executor = Executors.newFixedThreadPool(2); // Here, define some nice way of using a thread pool
    private static Graph internetModel = new Graph();
    private static DatabaseThread databaseThread= new DatabaseThread(internetModel);

    public static void main(String[] args) {
        EdgeSeeker edgeSeeker = null;
        try {
            edgeSeeker = new EdgeSeeker(internetModel, new Vertex(Constants.DEFAULT_START), executor,databaseThread);
        } catch (VertexUnreachableException e) {
            e.printStackTrace();
        } catch (VertexInvalidException e) {
            e.printStackTrace();
        }
        executor.execute(edgeSeeker);
    }
}
