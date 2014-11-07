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
//    private static Set<Vertex> alreadyUnderExamination = Collections.synchronizedSet(new HashSet<>());
//    private static SynchornizedHashSet<Vertex> alreadyUnderExamination = new SynchornizedHashSet<>();
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
if (!executor.isShutdown()){
        try {
            ArrayList<String> anchors = URLUtil.getAnchors(source.getName(), internetModel);
            for (String anchor : anchors) {
                String cleanAnchor = URLUtil.stripURL(anchor);
                if (cleanAnchor != null) {
                    //We want these operations to be atomic so only one vertex will be created per webpage
                    synchronized (lock){
                    Vertex newVertex = new Vertex(cleanAnchor);
                    boolean present = internetModel.getVertices().contains(newVertex);
                    if (!alreadyUnderExamination.contains(newVertex)) {
                        if (present) {
                            internetModel.addEdge(source, newVertex);
                        } else {
                            internetModel.addVertex(newVertex);
                            internetModel.addEdge(source, newVertex);



                            EdgeSeeker edgeSeeker = new EdgeSeeker(internetModel, newVertex, executor, databaseThread);
                            alreadyUnderExamination.add(newVertex);

                            //if executor has not shutdown, submit job
                            if (!executor.isShutdown()){
                            executor.execute(edgeSeeker);}}

                        }
                        System.out.println("already under: " + alreadyUnderExamination.size());


                    }
                }

            }         alreadyUnderExamination.remove(source);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }else {
databaseThread.storeWorkAtHand(source);}
    }
}