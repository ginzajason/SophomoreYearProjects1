 import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class GraphSetupPanel extends JPanel{
	private JTextField verticiesT;
	private JLabel verticiesF;
	private JRadioButton[][] radioButtonList;
	private ArrayList <JLabel> rowLabelList;
	private ArrayList <JLabel> colLabelList;
	private int verticies;
	private JButton enter, randomize, fillButton;
	private ArrayList<Point> pointList;
	private boolean random = false;
	private boolean fill = false;
	private Random rand = new Random();
	private ArrayList<GraphVertex> graphVertexList;
	public GraphSetupPanel() {
		setPreferredSize(new Dimension(1350, 625));
    	setLayout(null);
    	
    	
    	verticies = 5;
    	rowLabelList = new ArrayList<JLabel>();
		colLabelList = new ArrayList<JLabel>();
		graphVertexList = new ArrayList<GraphVertex>();
    	setupComponents();

    	this.setBackground(Color.yellow);
	}
	
	public int getVerticies() {
		return verticies;
	}
	
	public ArrayList<Point> getpointList(){
		ArrayList<Point> points = new ArrayList<Point>();

		for(int row = 0; row < radioButtonList.length; row++) {
			for(int col = 0; col < radioButtonList[row].length; col++) {
				if(radioButtonList[row][col].isSelected() && row != col) {
					Point temp = new Point(row+1, col+1);
					boolean contains = false;
					Point reverseTemp = new Point(col+1, row+1);
					for(int scan = 0; scan < points.size(); scan++) {
						if(points.get(scan).equals(reverseTemp)) {
							contains = true;
							break;
						}
					}
					if(contains == false) {
						points.add(temp);
					}
					setupVertexList(temp);
				}
			}
		}
		
		pointList = points;
		return points;
	}
	
	private void setupVertexList(Point point) {
		boolean contains = false;
		
		for(int scan = 0; scan < graphVertexList.size(); scan++) {
			if(graphVertexList.get(scan).getVertex() == point.x) {
				contains = true;
				graphVertexList.get(scan).addVertex(point.y);
				break;
			}
		}
		if(contains == false) {
			GraphVertex tempV = new GraphVertex(point.x);
			tempV.addVertex(point.y);
			graphVertexList.add(tempV);
		}
	}
	
	public ArrayList<GraphVertex> getGraphVertex(){
		graphVertexList.clear();
		getpointList();
		return graphVertexList;
	}
	
	private void setupComponents() {
		verticiesT = new JTextField("5");
		verticiesF = new JLabel("Verticies:");
		enter = new JButton("Clear");
		randomize = new JButton("Randomize");
		fillButton = new JButton("Fill");
		
    	verticiesF.setBounds(25, 25, 75, 25);
    	verticiesT.setBounds(100, 25, 75, 25);
    	enter.setBounds(200, 25, 75, 25);
    	randomize.setBounds(300, 25, 100, 25);
    	fillButton.setBounds(425, 25, 75, 25);
    	
    	enter.addActionListener(new ButtonListener());
    	randomize.addActionListener(new ButtonListener());
    	fillButton.addActionListener(new ButtonListener());
    	
    	add(verticiesF);
    	add(verticiesT);
    	add(enter);
    	add(randomize);
    	add(fillButton);
    	
    	random = true;
    	setupradioButtons();
	}
	
	private void setupradioButtons() {
		
		if(radioButtonList != null) {
			for(int row = 0; row < radioButtonList.length; row++) {
				for(int col = 0; col < radioButtonList[row].length; col++) {
					remove(radioButtonList[row][col]);
				}
			}
		}
		
		
		for(int scan = 0; scan < rowLabelList.size(); scan ++) {
			remove(rowLabelList.get(scan));
		}
		
		for(int scan = 0; scan < colLabelList.size(); scan ++) {
			remove(colLabelList.get(scan));
		}
		
		radioButtonList = new JRadioButton[verticies][verticies];
		rowLabelList = new ArrayList<JLabel>();
		colLabelList = new ArrayList<JLabel>();
		for(int row = 0; row < radioButtonList.length; row++) {
			JLabel rowLabel = new JLabel((row + 1) + "");
			rowLabel.setBounds(0, 75+row*25, 25, 25);
			add(rowLabel);
			rowLabelList.add(rowLabel);
			for(int col = 0; col < radioButtonList[row].length; col++) {
				JLabel colLabel = new JLabel((col+1) + "");
				colLabel.setBounds(30+col*75, 50, 25, 25);
				add(colLabel);
				colLabelList.add(colLabel);
				JRadioButton radio = new JRadioButton((col+1) + "," + (row+1));
				radio.setBounds(25+row*75, 75+col*25, 75, 25);
				if(random == true) {
					radio.setSelected(rand.nextBoolean());
				}
				if(fill == true) {
					radio.setSelected(true);
				}
				add(radio);
				radioButtonList[row][col] = radio;
			}
		}
		
		setPreferredSize(new Dimension(100 + 75*verticies, 100 + 25*verticies));
		super.updateUI();
	}
	
	
	
	private class ButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			if(event.getSource() == enter) {
				verticies = Integer.parseInt(verticiesT.getText());
				random = false;
				fill = false;
				setupradioButtons();
			}
			if(event.getSource() == randomize) {
				verticies = Integer.parseInt(verticiesT.getText());
				random = true;
				fill = false;
				setupradioButtons();
			}
			if(event.getSource() == fillButton) {
				verticies = Integer.parseInt(verticiesT.getText());
				random = false;
				fill = true;
				setupradioButtons();
			}
		}
	}
}
