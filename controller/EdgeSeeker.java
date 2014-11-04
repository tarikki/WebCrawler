package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import model.Graph;
import model.Vertex;
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
 * <p/>
 * Remember, a Graph uses a list of unique Vertices, so adding an already existing vertex
 * should not cause duplicates. But neither should it throw exceptions
 * <p/>
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
    private static Set<Vertex> alreadyUnderExamination = Collections.synchronizedSet(new HashSet<Vertex>());

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
        // The core code goes here. Make it work!
        ArrayList<String> anchors = null;
        try {
            System.out.println(source.getName());
            anchors = URLUtil.getAnchors(source.getName());
            for (String anchor : anchors) {
                boolean validAddress = URLUtil.isReachableURL(anchor);
                if (validAddress) {
                    Vertex newVertex = new Vertex(anchor);
                    internetModel.addVertex(newVertex);
                    internetModel.addEdge(source, newVertex);
                }
            }
        } catch (IOException | VertexInvalidException | VertexUnreachableException e) {
            e.printStackTrace();
        }


        // Loop through all anchors found

        // Create the vertex. If this fails, do nothing with it. Otherwise, add the edge


        // Now the magic follows. For each successfully added target, start another thread to examine it.
    }
}