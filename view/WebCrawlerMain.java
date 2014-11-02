package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

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
 *
 */

public class WebCrawlerMain {
	private static ExecutorService executor = null; // Here, define some nice way of using a thread pool
	private static Graph internetModel = new Graph();
	private static DatabaseThread databaseThread= new DatabaseThread(internetModel);
	public static final int DEFAULT_WIDTH = 700;
	public static final int DEFAULT_HEIGHT = 550;
	
	public static void main(String[] args) {
		new WebCrawlerMain();
	}
	
	public WebCrawlerMain() {
		// Here, create a new Runnable to insert into the EventQueue
		// The Runnable should create the actual frame and set it to visible
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
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
	}
	
	public boolean readWorkAtHand() throws Exception  {	
		
		boolean result = false;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(model.Constants.WORK_AT_HAND_FILENAME));
			Set<String> cleaning = new HashSet<String>();
			String numberOfNodes = reader.readLine();
			int maxToRead = Integer.parseInt(numberOfNodes);
			for (int i=0; i<maxToRead; i++) {
				String name = reader.readLine();
				cleaning.add(name);
			}
			for (String name: cleaning) {
				// Insert the stored job into the thread pool here
			}
		}
		catch (IOException e) {
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
		internetModel.writeGraph();
		databaseThread.writeAllWorkAtHand();

	}
	
	class VerticesFrame extends JFrame {
		// A JFrame used to host the Statistics Panel
		
		private static final long serialVersionUID = 1L;
		
		public VerticesFrame() {
			// Create the frame, add the right panel. Use a BorderLayout. Set the title.
						
			// The following code can be kept here. It makes sure refresh is called every three seconds.
			java.util.Timer timer = new java.util.Timer();
		    timer.schedule(new TimerTask() {
		        @Override
		        public void run() {
		        	// Insert a call to the refresh method here. Make sure to do the casting.
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
	 *
	 */
	
	class StatisticsPanel extends JPanel{
		private JTable vertexList;
		
		public StatisticsPanel() {
			this.setLayout(new BorderLayout());
			// Create button subpanel
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout());

			// Add a stop button. It should stop crawling, then exit the application
			
			// Add a refresh button. Make sure it calls refresh.

			 
			// Create vertex table. Create Statistics Overview panel.
			// Use a Grid Layout to put the Statistics Overview on. I used
			// 5 by 5 to have some layouting.
			
		}
		
		public void refresh() {
			// Refresh the content of the table. The call the TablePacker.
			
        	new TablePacker(TablePacker.VISIBLE_ROWS, true).pack(vertexList);
		}
	}
	
}