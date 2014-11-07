package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import model.Graph;
import model.Vertex;
import util.MemoryUtil;
import util.URLUtil;
import util.VertexInvalidException;
import util.VertexUnreachableException;

/**
 * This class might form heart and brains of the application. It is a thread which:
 * - is initiated with a Vertex (being an URL)
 * - It is NOT an active object; it runs in a thread pool instead
 * - Takes out all anchors in the URL, using URLUtil for this
 * - Then offers new threads in the pool, one for each anchor found
 * - And of course it should add a Vertex for every new URL found, and an Edge to reflect
 * the anchor.
 * <p>
 * Remember, a Graph uses a list of unique Vertices, so adding an already existing vertex
 * should not cause duplicates. But neither should it throw exceptions
 * <p>
 * The thread should also do some optimization by not offering vertices already under
 * examination. Several mechanisms could be used for this. Here, I used a Set to keep
 * track of Vertices already offered for examination. Not the most efficient solution,
 * but good enough for now.
 *
 * @author harald.drillenburg
 */

public class EdgeSeeker implements Runnable {
    private Graph internetModel;
    private Vertex source;
    private ExecutorService executor;
    private DatabaseThread databaseThread;
    private static final Lock lock = new ReentrantLock();

    private volatile Set<Vertex> alreadyUnderExamination = new HashSet<Vertex>();


    public EdgeSeeker(Graph internetModel, Vertex source, ExecutorService executor, DatabaseThread databaseThread) {
        super();
        this.internetModel = internetModel;
        this.source = source;
        this.executor = executor;
        this.databaseThread = databaseThread;

    }

    public void storeSource() {
        databaseThread.storeWorkAtHand(source);
    }

    public Vertex getSource() {
        return source;
    }

    @Override
    public void run() {
        // if executor is in shutdown, don't even bother starting this
        if (!executor.isShutdown()) {
            try {
                ArrayList<String> anchors = URLUtil.getAnchors(source.getName(), internetModel);
                for (String anchor : anchors) {
                    String cleanAnchor = URLUtil.stripURL(anchor);
                    if (cleanAnchor != null) {

                        //We want these operations to be atomic so only one vertex will be created per website
                        synchronized (lock) {
                            Vertex newVertex = new Vertex(cleanAnchor);
                            boolean present = internetModel.getVertices().contains(newVertex);
                            // if the graph contains the page, only add a new edge
                            if (!alreadyUnderExamination.contains(newVertex)) {
                                if (present) {

                                    internetModel.addEdge(source, newVertex);
                                } else { // else add the vertex too
                                    internetModel.addVertex(newVertex);
                                    internetModel.addEdge(source, newVertex);

                                    EdgeSeeker edgeSeeker = new EdgeSeeker(internetModel, newVertex, executor, databaseThread);
                                    alreadyUnderExamination.add(newVertex);

                                    //Executor still not shutdown? Submit job!
                                    if (!executor.isShutdown()) {
                                        executor.execute(edgeSeeker);
                                    }
                                }

                            }


                        }
                    }

                }
                // now that all the links are crawled we can remove the page from already under
                alreadyUnderExamination.remove(source);
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            //Oh Executor shutdown while we were doing other stuff?
            // Add the work at hand to the temp storage so it can be taken care of
            databaseThread.storeWorkAtHand(source);
        }
    }

//    public static void setAlreadyUnderExamination(Vertex alreadyUnderExamination) {
//        EdgeSeeker.alreadyUnderExamination.add(alreadyUnderExamination);
//    }
}