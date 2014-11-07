package testers;

import controller.DatabaseThread;
import controller.EdgeSeeker;
import model.Graph;
import model.Vertex;
import util.ConfigUtil;
import util.MemoryUtil;
import util.VertexInvalidException;
import util.VertexUnreachableException;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by extradikke on 4-11-14.
 */
public class EdgeSeekerTester {
    private static ConfigUtil config = new ConfigUtil();
    private static ExecutorService executor = Executors.newFixedThreadPool(50); // Here, define some nice way of using a thread pool
    private static Graph internetModel = new Graph(config);
    private static DatabaseThread databaseThread = new DatabaseThread(internetModel, config);

    private static Timer timer = new Timer();

    public static void main(String[] args) {
        EdgeSeeker edgeSeeker = null;
        vertexMonitor();

            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
            Vertex startVertex = new Vertex("http://www.marktplaats.nl/");

            internetModel.addVertex(startVertex);
            edgeSeeker = new EdgeSeeker(internetModel, startVertex, executor, databaseThread);
            executor.execute(edgeSeeker);



    }


    public static void vertexMonitor() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (internetModel.getNumberOfVertices() > 5000) {
                    executor.shutdownNow();
//                    try {
//                        executor.awaitTermination(5, TimeUnit.SECONDS);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    timer.cancel();
                    internetModel.dumpSummary();
                    System.out.println(MemoryUtil.readableFileSize(internetModel.getBandwidthUsed()));

                }
            }
        };

        timer.scheduleAtFixedRate(task, 5 * 1000, 5 * 1000);
    }


}

