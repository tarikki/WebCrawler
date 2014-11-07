package view;

import controller.DatabaseThread;
import controller.EdgeSeeker;
import model.Graph;
import model.Vertex;
import util.ButtonUtils;
import util.MemoryUtil;
import util.TablePacker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
    private static ExecutorService executor = Executors.newFixedThreadPool(100); // Here, define some nice way of using a thread pool
    private static Graph internetModel = new Graph();
    private static DatabaseThread databaseThread = new DatabaseThread(internetModel);

    //// Changed value of these so we have the same size UI regardless of user's screen.
    public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int DEFAULT_WIDTH = screenSize.width * 2 / 3;
    public static final int DEFAULT_HEIGHT = screenSize.height * 2 / 3;


    public static SortedSet<Vertex> allVertices = new TreeSet<>();
    public String verticesNumber;
    public String edgesNumber;
    public String ratioNumber;
    public String memUsage;
    private JMenu menu;
    private JMenuBar menuBar;

    int height = screenSize.height * 2 / 3;
    //int width = screenSize.width * (2 / 3);

    public static void main(String[] args) {
        new WebCrawlerMain();
    }

    public WebCrawlerMain() {
        // Here, create a new Runnable to insert into the EventQueue
        // The Runnable should create the actual frame and set it to visible
        try {
            startCrawling();
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
        Vertex startVertex = new Vertex("http://www.reddit.com/");
        internetModel.addVertex(startVertex);
        EdgeSeeker edgeSeeker = new EdgeSeeker(internetModel, startVertex, executor, databaseThread);
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
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        databaseThread.writeAllWorkAtHand();
        System.exit(0);
//		internetModel.writeGraph();
//		databaseThread.writeAllWorkAtHand();

    }

    public void copyInfo() {
        allVertices.addAll(internetModel.copyChangedList());
        copyTopInfo();
    }

    public void copyTopInfo() {

        ratioNumber = Float.toString(internetModel.getRatioEV());
        verticesNumber = Integer.toString(internetModel.getNumberOfVertices());
        edgesNumber = Integer.toString(internetModel.getNumberOfEdges());
        memUsage = MemoryUtil.readableFileSize(internetModel.getBandwidthUsed());
    }


    class VerticesFrame extends JFrame {
        // A JFrame used to host the Statistics Panel


        private static final long serialVersionUID = 1L;

        public VerticesFrame() {

            // Create the frame, add the right panel. Use a BorderLayout. Set the title.

            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            this.setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

            createMenuBar();
            createMenuButtons();

            this.setTitle("Web Crawler");
            this.setLayout(new BorderLayout());

            final StatisticsPanel statisticsPanel = new StatisticsPanel(); /// Needs to be final so we can use it with the timer below
            statisticsPanel.setVisible(true);
            this.add(statisticsPanel, BorderLayout.CENTER); //// Add the actual UI to the center of the main frame


            // The following code can be kept here. It makes sure refresh is called every three seconds.
            java.util.Timer timer = new java.util.Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    /// Refresh every 10 seconds
                    statisticsPanel.refresh(); // Insert a call to the refresh method here. Make sure to do the casting.
                }
            }, 10000, 10000);


            /// Timer for stats overview
            java.util.Timer timer2 = new java.util.Timer();
            timer2.schedule(new TimerTask() {
                @Override
                public void run() {

                    /// Refresh every 2 seconds
                    statisticsPanel.refreshUp(); // Insert a call to the refresh method here. Make sure to do the casting.

                }
            }, 2000, 2000);


        }

        public void createMenuBar() {
            menu = new JMenu("Menu");
            menu.setBackground(Color.black);
            menu.setForeground(Color.white);
            menuBar = new JMenuBar();
            menuBar.add(menu);
            menuBar.setBackground(Color.black);
            menuBar.setForeground(Color.white);
            menuBar.setVisible(true);
            this.setJMenuBar(menuBar);
        }

        public void createMenuButtons() {

            /// Starting site
            JMenuItem startingSite = new JMenuItem("Select starting web site");
            startingSite.setBackground(Color.black);
            startingSite.setForeground(Color.white);
            menu.add(startingSite);
            startingSite.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    createStartingSiteOptionPane();
                }
            });


            /// Path to save
            JMenuItem pathtoSave = new JMenuItem("Select path to save sites to");
            pathtoSave.setBackground(Color.black);
            pathtoSave.setForeground(Color.white);

            pathtoSave.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    createPathOptionPane();
                }
            });
            menu.add(pathtoSave);
        }

        public void createPathOptionPane() {
            String path;
            JOptionPane setPathToSave = new JOptionPane();
            setPathToSave.setVisible(true);
            path = setPathToSave.showInputDialog(this, "Enter the path to save file to:");


        }

        public void createStartingSiteOptionPane() {
            String startingSite;
            JOptionPane setStartingSite = new JOptionPane();
            setStartingSite.setVisible(true);
            startingSite = setStartingSite.showInputDialog(this, "Enter the starting web site:");
            /// COMMENT FOR GIT!!!!

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

        /// Components to build the table
        private JTable vertexList;
        private DefaultTableModel model; /// Model for the table. Using DefaultTableModel so we don't have to implement an abstract table..
        private JTableHeader header; /// Header of the table (column names)

        //// TextFields for Statistics overview
        private JTextField vertices;
        private JTextField edges;
        private String eVV; //// Edges divided by vertices. Needs a better name!
        private JTextField ev;
        private JTextField threads;
        private JTextField bandwidth;


        /// Panels and their holders.
        private JPanel buttonPanel; /// Panel to hold all the buttons. Positioned at bottom.
        private JScrollPane scrollPane; /// ScrollPane to hold the table of data.
        private JPanel statsPanel;     /// Panel to hold statistics
        private JPanel scrollPaneHolder; /// Panel to hold scrollPane

        public StatisticsPanel() {

            //// Set the proper layout
            this.setLayout(new BorderLayout());


            /// Create the components (call the proper methods)
            createButtons();
            createTable();
            createScrollPanePanel();
            createTextFields();
            createStatsPanel();

            /// Position the components accordingly on the main frame
            this.add(statsPanel, BorderLayout.NORTH); /// Add the statsPanel to the top of the frame
            this.add(scrollPaneHolder, BorderLayout.CENTER); /// Add the ScrollPaneHolder (table) to the center of the frame
            this.add(buttonPanel, BorderLayout.SOUTH); /// Add the buttons at the bottom of the frame
        }


        /// TextFields for Statistics overview
        public void createTextFields() {
            vertices = new JTextField(String.valueOf(internetModel.getNumberOfVertices()));
            edges = new JTextField(String.valueOf(internetModel.getNumberOfEdges()));
            eVV = String.valueOf(internetModel.getNumberOfEdges() / internetModel.getNumberOfVertices());
            ev = new JTextField(eVV);
            threads = new JTextField(String.valueOf(Thread.activeCount()));
            bandwidth = new JTextField(MemoryUtil.readableFileSize((internetModel.getBandwidthUsed())));
        }

        /// Table that stores URL & degrees
        public void createTable() {
            vertexList = new JTable(new DefaultTableModel());
            model = (DefaultTableModel) vertexList.getModel();

            vertexList.setVisible(true);

            /// Column names (first row) find better implementation and try with scrollpane.
            model.addColumn("#");
            model.addColumn("Degree");
            model.addColumn("Uniform Resource Locator");


            /// Get header so we can set column names to visible
            header = vertexList.getTableHeader();
            header.setVisible(true);
        }

        /// Buttons and their functionality
        public void createButtons() {
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());
            buttonPanel.setVisible(true);

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
                    try {
                        stopCrawling();
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    }

                }
            });

            ButtonUtils.addButton(buttonPanel, "Start", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    refresh();
                }
            });
        }

        /// ScrollPane to store the table
        public void createScrollPanePanel() {
            /// New panel for holding scrollpane so we can position things better
            scrollPane = new JScrollPane();
            scrollPaneHolder = new JPanel();

            scrollPaneHolder.setLayout(new BorderLayout());
            scrollPaneHolder.add(header, BorderLayout.NORTH);
            scrollPaneHolder.add(scrollPane, BorderLayout.CENTER);


            scrollPane.add(vertexList); /// Add the table to the scrollable pane
            scrollPane.getViewport().add(vertexList);

        }


        /// Panel to hold the statistics
        public void createStatsPanel() {
            // Create vertex table. Create Statistics Overview panel.
            // Use a Grid Layout to put the Statistics Overview on. I used
            // 5 by 5 to have some layouting.

            statsPanel = new JPanel();
            statsPanel.setVisible(true);
            statsPanel.setLayout(new GridLayout(5, 5));

            /// TextFields for OverView, replace Strings with appropriate getX() methods
            vertices = new JTextField(String.valueOf(verticesNumber));
            edges = new JTextField(String.valueOf(edgesNumber));
            ev = new JTextField(String.valueOf(ratioNumber));
            bandwidth = new JTextField(MemoryUtil.readableFileSize(internetModel.getBandwidthUsed()));

            this.add(statsPanel, BorderLayout.NORTH);


            //// Add new labels and the textfields to the panel
            statsPanel.add(new JLabel("# Vertices"));
            statsPanel.add(vertices);
            statsPanel.add(new JLabel("# Edges"));
            statsPanel.add(edges);
            statsPanel.add(new JLabel("Edge / Vertice"));
            statsPanel.add(ev);
            statsPanel.add(new JLabel("# Threads"));
            statsPanel.add(threads);
            statsPanel.add(new JLabel("Bandwith"));
            statsPanel.add(bandwidth);


            // Create vertex table. Create Statistics Overview panel.
            // Use a Grid Layout to put the Statistics Overview on. I used
            // 5 by 5 to have some layouting.

        }

        /// Refresh UI (table)
        public void refresh() {
            // Refresh the content of the table. The call the TablePacker.
            new TablePacker(TablePacker.VISIBLE_ROWS, true).pack(vertexList);
            copyInfo();
            for (Vertex vertex : allVertices) {
                String[] row = {Integer.toString(vertex.getNumberOfTargetedBys()), Integer.toString(vertex.getNumberOfEdges()), vertex.getName()};
                model.addRow(row);
                model.fireTableDataChanged(); // Update the table!


            }
            new TablePacker(TablePacker.VISIBLE_ROWS, true).pack(vertexList);


            refreshUp();

        }

        public void refreshUp() {
            /// @TODO look for a better way to implement threadcount
            ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
            int noThreads = currentGroup.activeCount();

            copyTopInfo();
            vertices.setText(verticesNumber);
            edges.setText(edgesNumber);
            ev.setText(ratioNumber);
            threads.setText(String.valueOf(noThreads));
            bandwidth.setText(memUsage);

        }


        /// Not working for some reason.. Should realign the columns according to size, not make all of them equal
        /// Probably because of auto-resizing somewhere. Works if you resize window to be smaller. But not in the initial setup..
        /// TEST COMMENT FOR GIT!!!!

    }
}

