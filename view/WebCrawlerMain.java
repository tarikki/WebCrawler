package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.TableColumn;

import model.Constants;
import model.Graph;
import model.Vertex;
import util.ButtonUtils;
import util.MemoryUtil;
import util.TablePacker;
import controller.DatabaseThread;
import controller.EdgeSeeker;
import controller.StatisticsThread;

/**
 * This class is the main Swing class building the GUI
 * Defined are the WebCrawlerMain, the frame and the panel classes
 * Also shown are several methods you might need
 * The implementation you'll have to do yourself
 * A menu is optional
 *
 * @author harald.drillenburg
 */

public class WebCrawlerMain {
    private static ExecutorService executor = Executors.newFixedThreadPool(2); // Here, define some nice way of using a thread pool
    private static Graph internetModel = new Graph();
    private static DatabaseThread databaseThread = new DatabaseThread(internetModel);
    public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int DEFAULT_WIDTH = screenSize.width  * 2/3;
    public static final int DEFAULT_HEIGHT = screenSize.height * 2/3;

    int height = screenSize.height * 2/3;
    //int width = screenSize.width * (2 / 3);

    public static void main(String[] args) {
        new WebCrawlerMain();
    }

    public WebCrawlerMain() {
        // Here, create a new Runnable to insert into the EventQueue
        // The Runnable should create the actual frame and set it to visible
        try {
            startCrawling();
            stopCrawling();
        } catch (Exception e) {
            e.printStackTrace();
        }


        EventQueue.invokeLater(new Runnable() {
            public void run() {
                VerticesFrame verticesFrame = new VerticesFrame();


                verticesFrame.setVisible(true);


            }
        });

    }

    public void stopRequest() throws FileNotFoundException, UnsupportedEncodingException {
        // Implement a neat way to stop
    }

    public void startCrawling() throws Exception {
        // Start the simple statistics viewer

        // Read already existing data


        // If there's already work in the queue, read it and start it as well. Otherwise, use some default site
        // A nice one to use is as newspaper, for example http://www.trouw.nl
        EdgeSeeker edgeSeeker = new EdgeSeeker(internetModel, new Vertex(Constants.DEFAULT_START), executor, databaseThread);
        executor.execute(edgeSeeker);

    }

    public boolean readWorkAtHand() throws Exception {

        boolean result = false;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(model.Constants.WORK_AT_HAND_FILENAME));
            Set<String> cleaning = new HashSet<String>();
            String numberOfNodes = reader.readLine();
            int maxToRead = Integer.parseInt(numberOfNodes);
            for (int i = 0; i < maxToRead; i++) {
                String name = reader.readLine();
                cleaning.add(name);
            }
            for (String name : cleaning) {
                // Insert the stored job into the thread pool here
            }
        } catch (IOException e) {
            //  No sweat, just no work at hand. Return false
            result = false;
        }
        if (reader != null) {
            reader.close();
        }

        return result;
    }

    public void stopCrawling() throws FileNotFoundException, UnsupportedEncodingException {
        // Send a stop signal to the thread pool. Make sure to get an overview of all pending jobs
        // (check the API of Executors to see how) and store them in a file for restarting them
        //
        // After that, write the graph to a file and exit the whole application
        executor.shutdown();
//		internetModel.writeGraph();
//		databaseThread.writeAllWorkAtHand();

    }

    class VerticesFrame extends JFrame {
        // A JFrame used to host the Statistics Panel





        private static final long serialVersionUID = 1L;

        public VerticesFrame() {
            // Create the frame, add the right panel. Use a BorderLayout. Set the title.
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            this.setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
            ///this.setResizable(false);
            System.out.println(height);
            this.setTitle("Web crawler");
            this.setLayout(new BorderLayout());
            StatisticsPanel statisticsPanel = new StatisticsPanel();
            statisticsPanel.setVisible(true);
            this.add(statisticsPanel, BorderLayout.CENTER); //// add the panel to the frame (center)

            // The following code can be kept here. It makes sure refresh is called every three seconds.
            java.util.Timer timer = new java.util.Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                    // asd.refresh(); // Insert a call to the refresh method here. Make sure to do the casting.
                }
            }, 3000, 3000);
        }
    }

    /**
     * The next class defines the actual panel to show all data on
     * It will only be used from the frame class, so no seperate file
     * is necessary
     *
     * @author harald.drillenburg
     */

    class StatisticsPanel extends JPanel {
        private JTable vertexList;

        public StatisticsPanel() {
            this.setLayout(new BorderLayout());
            // Create button subpanel

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());
            buttonPanel.setVisible(true);
            this.add(buttonPanel, BorderLayout.SOUTH); /// Add the buttons at the bottom


            // Add a refresh button. Make sure it calls refresh.

            ButtonUtils.addButton(buttonPanel, "Refresh", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    refresh();
                }
            });
            // Add a stop button. It should stop crawling, then exit the application
            ButtonUtils.addButton(buttonPanel, "Stop", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //// Stop functionality added here
                }
            });


            String[] columnNames = {"#", "Degree", "URL"}; /// WIll be used to name columns later with arraylist & columnNames


            vertexList = new JTable(3, 3); /// Make a table with 3 rows & 5 columns (SHOULD BE DYNAMIC!!!)
            vertexList.setVisible(true);


            ///vertexList.setLayout(new GridLayout(5,5));
            this.add(vertexList, BorderLayout.CENTER); /// add the table to the center of the panel

            Panel statsPanel = new Panel();
            statsPanel.setName("Statistics overview");

            statsPanel.setVisible(true);
            statsPanel.setLayout(new GridLayout(5, 5));

            /// TextFields for OverView, replace Strings with appropriate getX() methods
            JTextField vertices = new JTextField("test");
            JTextField edges = new JTextField("edges");
            JTextField ev = new JTextField("555555");
            JTextField bandwidth = new JTextField("Bandwidth");

            this.add(statsPanel, BorderLayout.NORTH);

            //// Add new labels and the textfields to the panel
            statsPanel.add(new JLabel("# Vertices"));
            statsPanel.add(vertices);
            statsPanel.add(new JLabel("# Edges"));
            statsPanel.add(edges);
            statsPanel.add(new JLabel("Edge / Vertice"));
            statsPanel.add(ev);
            statsPanel.add(new JLabel("Bandwith"));
            statsPanel.add(bandwidth);

            // Create vertex table. Create Statistics Overview panel.
            // Use a Grid Layout to put the Statistics Overview on. I used
            // 5 by 5 to have some layouting.

        }

        public void refresh() {
            // Refresh the content of the table. The call the TablePacker.
            vertexList.revalidate(); /// REFRESH THE LIST (AKA THE TABLE)
            new TablePacker(TablePacker.VISIBLE_ROWS, true).pack(vertexList);

        }

    }
}