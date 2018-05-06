package evolution;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

//Genome class
class Genome{
	//Data
	private double dScore;
	private String sDNA;
	
	//Setters & Getters
	public void setScore(double dScore) {
		this.dScore = dScore;
	}

	public void setDNA(String sDNA) {
		this.sDNA = sDNA;
	}

	public double getScore() {
		return this.dScore;
	}
	
	public String getDNA() {
		return this.sDNA;
	}
	
	//Constructor
	public Genome(String sDNA, double dScore) {
		this.dScore = dScore;
		this.sDNA = sDNA;
	}
}
	
// Calculations class
class Calculations {
	//Generate initial population
	protected ArrayList<Genome> generatePopulation(ArrayList<Genome> caGenome, int iPop, int iProtoScore) {
		// iPop <- population size
		// iProtoScore <- prototype score/length
		// sbGenome <- String builder for the genomes
		// saPop <- Local array for the species
		for (int i=0; i<iPop; i++) {
			StringBuilder sbGenome = new StringBuilder(iProtoScore);
			
			for (int j=0; j<iProtoScore; j++) { 
				sbGenome.append((char) (Math.floor(Math.random() * (126 - 32)) + 32));
			}
			caGenome.add(new Genome(sbGenome.toString(), 0));
		}
		return caGenome;
	}
	
	//Method for calculating the score
	private double formula(double dFitness, int iPow) {
		// dFitness <- input fitness score
		// iPow <- the exponent of the pow function
		dFitness = Math.pow(dFitness, iPow);
		return dFitness;
	}
	
	//Method for calculating the fitness scores
	protected ArrayList<Genome> calculateScores(int iPop, int iProtoScore, String sProto, ArrayList<Genome> caGenome) {
		// iPop <- population size
		// iProtoScore <- Maximum score / total length of the prototype
		// saSpecies <- Array containing the speciment of the species
		// iScore <- score counter for each genomes
		// dScore <- temporaty score for Genome
		// dSumScore <- sum of scores
		char cProto[] = sProto.toCharArray();
		double dSumScore = 0;
		int iScore;
		double dScore;
		
		for (int i=0; i<iPop; i++) {
			iScore = 0;
			char cGenome[] = caGenome.get(i).getDNA().toCharArray();

			for (int j=0; j<iProtoScore; j++) { 
				if (cProto[j] == cGenome[j]){
					iScore += 1; 
				}
			}

			caGenome.get(i).setScore(formula(iScore, 2));
			dSumScore += caGenome.get(i).getScore();
		}

		for (int i=0; i<iPop; i++) {
			dScore = caGenome.get(i).getScore();
			caGenome.get(i).setScore(dScore / dSumScore);
		}
		return caGenome;
	}
	
	//Method for procreating a new generation of string species
	protected ArrayList<Genome> crossover(double dMutaProb, int iProtoScore, int iPop, ArrayList<Genome> caGenome) {
		// dMutaProb <- Probability of random mutation during the reproduction
		// iProtoScore <- Maximum score / total length of the prototype
		// iPop <- population size
		// contestants <- ArrayList for the possible parents
		// slParents <- selected parents
		// caChildren <- new generation of genomes
		ArrayList<Genome> caChildren = new ArrayList<Genome>();

		for (int child=0; child<iPop; child++) {			
			ArrayList<String> slParents = new ArrayList<String>();
			StringBuilder sChild = new StringBuilder(iProtoScore);

			do {
				for (int i=0; i<iPop; i++) {
					double iTHold = Math.random() * (1 - 0);

					if (caGenome.get(i).getScore() > iTHold){
						slParents.add(caGenome.get(i).getDNA());

					}
				}
			} while(slParents.size() < 2);

			for (int cell=0; cell<iProtoScore; cell++) {
				int mask = (int) (Math.floor(Math.random() * (2 - 0)));
				
				if ((Math.random() * (1 - 0)) <= dMutaProb) { 
					sChild.append((char) (Math.floor(Math.random() * (126 - 32)) + 32)); 
				} else {
					sChild.append(slParents.get(mask).charAt(cell));
				}
			}
			caChildren.add(new Genome(sChild.toString(), 0));
		}
		Collections.sort(caChildren, Comparator.comparingDouble(Genome ::getScore));
		return caChildren;
	}
	
	//Constructor
	public Calculations() {
    }
}

//Main class
public class StringGE{
	
	//Input variables
	protected String sProto;
	protected int iPop;
	protected double dMutaProb;
	protected int iProtoScore;
	
	//Array for the species population
	protected ArrayList<Genome> caGenome = new ArrayList<Genome>();
	
	//Genearation counter
	protected int iGen = 0; 

	//Variable for top genomes
	private int iTop = 20;
	
	//Create new Calculations object
	protected Calculations calcBackEnd = new Calculations();
	
	//Create the frame
	private JFrame frameGui = new JFrame();
	
	//Width & height
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    //Frame position X, Y
    
    private static final int POS_X = 400;
    private static final int POS_Y = 100;
    
	//Create and add the buttons to the buttonsPanel
	protected JButton buttonStart = new JButton("Start");

	//Create Panel for list of Top
	private JPanel panelTop = new JPanel();
	
	//Create labels
	private JLabel labelParagon = new JLabel("");
	private JLabel labelParagonText = new JLabel("Paragon:");
	private JLabel labelGeneration = new JLabel("");
	private JLabel labelGenerationText = new JLabel("Current Generation:");
	private JLabel labelInfoText = new JLabel("Information:");
	private JLabel labelInfo = new JLabel("");
	private JLabel labelTopText = new JLabel("Top 20 Genomes");
	private JLabel[] laTop = new JLabel[iTop];
	
	//Set starting parameters of JFrame
	private void setFrame() {
		frameGui.setTitle("String Species with Genetic Algorithm");
		frameGui.setSize(WIDTH, HEIGHT);
		frameGui.setLocation(POS_X, POS_Y);
		frameGui.setVisible(true);
		frameGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameGui.setLayout(null);
	}		
	
	//Set the parameters of the panels
	private void setElements(){
		buttonStart.setBounds(50, 50, 150, 50);
		
		labelParagonText.setBounds(50, 120, 200, 50);
		labelParagon.setBounds(50, 150, 300, 50);
		
		labelInfoText.setBounds(50, 220, 200, 50);
		labelInfo.setBounds(50, 250, 200, 80);
		
		labelGenerationText.setBounds(250, 30, 200, 50);
		labelGeneration.setBounds(300, 50, 200, 50);
		
		labelTopText.setBounds(475, 30, 200, 50);
		panelTop.setBounds(400, 70, 250, 700);
	}

	//Add the elements to the Frame
	private void addElements() {
		//Add labels
		frameGui.add(buttonStart);

		//Add labels
		frameGui.add(labelParagonText);
		frameGui.add(labelParagon);
		frameGui.add(labelInfo);
		frameGui.add(labelInfoText);
		frameGui.add(labelGeneration);
		frameGui.add(labelGenerationText);
		frameGui.add(labelTopText);
		
		//Add panel for top
		frameGui.add(panelTop);
	}
	
	//Create and add the labels for the top Genomes
	private void createDefaultLabels() {
		for (int i=0; i<iTop; i++) {
			laTop[i] = new JLabel();
			panelTop.add(laTop[i]);
		}
	}

	//Set new value for paragon label
	public void setParagon(String sParagon) {
		labelParagon.setText(sParagon);
	}

	//Set new value for generaion label
	public void setGeneration(String sGen) {
		labelGeneration.setText(sGen);
	}	
	
	//Set New values for the i-th Top Genopme
	public void setTop(int iInd, String sGenome) {
		laTop[iInd].setText(sGenome);
	}
	
	//Set new values for the information labels
	public void setInformation(String sInfo) {
		labelInfo.setText(sInfo);
	}
	
	
	//Print final assessment of the evolution on the GUI
	private void result() {
		String sInfo = (
		"<html>" + "Population size: " + iPop + "<br>" +
		"Speciment size: " + iProtoScore + "<br>" +
		"Mutation Probability: " + Double.toString(dMutaProb * 100) + "%" + "</html>");
		setInformation(sInfo);
		setParagon(caGenome.get(0).getDNA());
	}
	
	//Print the top 20 genomes on the GUI
	private void top() {
		for (int t=1; t<=20; t++) {
			setTop((t-1), caGenome.get(t).getDNA());
		}
	}
	
	//Engine for the program
	public void engine() {
		while (caGenome.get(0).getDNA().equals(sProto) == false) {
			iGen += 1;

			caGenome = calcBackEnd.calculateScores(iPop, iProtoScore, sProto, caGenome);
			caGenome = calcBackEnd.crossover(dMutaProb, iProtoScore, iPop, caGenome);

			if (iGen % 2 == 0){
				String sParagon = caGenome.get(0).getDNA();
				
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						setGeneration(Integer.toString(iGen));
						setParagon(sParagon);
						top();						
					}
				});
			}
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				result();
			}
		});
	}
	
	//Constructor
	public StringGE(int iPop, double dMutaProb, String sProto) {
		//Assign input variables
		//Save population number
		this.iPop = iPop;
		
		//Save mutation probability
		this.dMutaProb = dMutaProb;
		
		//Set prototype
		this.sProto = sProto;
		
	    //Set protoScore
	    this.iProtoScore = sProto.length();
		
		//Create the frame
		setFrame();

		//Sets the params of elements
		setElements();
		
		//Add the rest of the elements
		addElements();
		
		//Add the default values
		createDefaultLabels();
		
	    //Generate initial population
	    this.caGenome = calcBackEnd.generatePopulation(this.caGenome, this.iPop, this.iProtoScore);    
		
		//Action listener for the Start button
		buttonStart.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e)
		    {
				new SwingWorker<Object, Object>() {
					
					@Override
					protected Object doInBackground() throws Exception{
						engine();
						return null;
					}
				}.execute();
		    }
		});
	}	
	
	//Main
	public static void main(String[] args) {
		new StringGE(1000, 0.01, "Searching with Genetic Algorithm.");
	}
}