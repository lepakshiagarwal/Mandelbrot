package edu.ycp.cs201.mandelbrot;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

// Distribute colors based on an even distribution of iteration counts
// Colors are created using a trigonometric process for all 3 colors
//    blue from cos(0) * 255 to cos(PI/2) * 255
//    red from sin(PI/2) * 255 to sin(0) * 255
//    green from sin(0) * 255 to sin(PI) * 255
public class ExtraColorMappingColorChooser implements ColorChooser {

	// declare map references
	private TreeMap<Integer, Integer> iterCountMap;
	private HashMap<Integer, Integer> iterSpectrumMap;
	private HashMap<Integer, Color>   iterColorMap;
	
	// the max spectrum location - should always be Mandelbrot.WIDTH * Mandelbrot.HEIGHT
	//    but it is calculated in createIterSpectrumMap()
	private int maxLocation;
	
	
	// CONSTRUCTOR: that creates the 3 maps, but doesn't populate them
	// the maps will need to be created separately
	// this allows for separate testing of the constituent map creation methods
	ExtraColorMappingColorChooser() {
		
		// create the Maps, but don't populate them
		iterCountMap    = new TreeMap<Integer, Integer>();
		iterSpectrumMap = new HashMap<Integer, Integer>();
		iterColorMap    = new HashMap<Integer, Color>();
	}	
	
	
	// CONSTRUCTOR: creates all 3 Maps and populates them
	ExtraColorMappingColorChooser(int[][] iterCounts) {
		
		// create the Maps
		iterCountMap    = new TreeMap<Integer, Integer>();
		iterSpectrumMap = new HashMap<Integer, Integer>();
		iterColorMap    = new HashMap<Integer, Color>();		
		
		// populate the Maps from iterCounts arrays
		createIterCountMap(iterCounts);
		createIterSpectrumMap();
		createIterColorMap();
	}

	
	// GET COLOR: returns the Color mapped to the iterCount in iterColorMap
	@Override
	public Color getColor(int iterCount) {
		
		// if invalid iterCount, return BLACK
		if (!iterColorMap.containsKey(iterCount)) {
			System.out.println("Invalid iterCount key: " + iterCount + ", set to BLACK");
			return Color.BLACK;
		}
		
		// otherwise return color from Color Map
		return iterColorMap.get(iterCount);
	}	

	
	// CREATE ITERCOUNT MAP: run through iterCounts array, and accumulate distribution of counts
	// for an 800 x 800 array, the # of points will be 640,000, but there can only be
	// maxCounts different iteration values
	public TreeMap<Integer, Integer> createIterCountMap(int[][] iterCounts) {
		for (int i = 0; i < iterCounts.length; i++) {
			for (int j = 0; j < iterCounts[i].length; j++) { // iterating through the iterCounts array
				if (iterCountMap.containsKey(iterCounts[i][j])) { // if the key already exists
					int count = iterCountMap.get(iterCounts[i][j]); // get count and
					iterCountMap.put(iterCounts[i][j], ++count); // increase count by 1
				} else {
					iterCountMap.put(iterCounts[i][j], 1); // or store element with count 1
				}
			}
		}
		return iterCountMap;//throw new UnsupportedOperationException("TODO - implement");		
	}
	
	
	// CREATE ITER SPECTRUM MAP: run through iterCountMap, and determine the spectrum location (this
	// is not the actual color, but rather its relative location in the color spectrum), based on
	// an even distribution for each iteration count in the iterCountMap
	public HashMap<Integer, Integer> createIterSpectrumMap() {
		int sum = 0;
		for (int key : iterCountMap.keySet()) { // iterating through the key set of the iterCountMap
			sum = sum + iterCountMap.get(key); // keeping a count of sum
			//calculating central position and storing it as value in iterSpectrumMap
			iterSpectrumMap.put(key, sum - iterCountMap.get(key) + (iterCountMap.get(key) / 2) + 1); 
		}
		maxLocation = sum;
		return iterSpectrumMap;
	}//throw new UnsupportedOperationException("TODO - implement");		

	
	
	// CREATE ITER COLOR MAP: run through iterSpectrumMap, and create a color mapping
	// using trig functions to create a smooth transition between RGB color bands
	public HashMap<Integer, Color> createIterColorMap() {
			for (int key : iterSpectrumMap.keySet()) {                            //iterate through keys of iterSpectrumMap
				if (key == Mandelbrot.THRESHOLD) {                                // if key==max iterCounts
					iterColorMap.put(key, Color.BLACK);                           //set color as black and store it with respective key in iterColorMap
				} else {
					double green=0;
					double red = 0;
					double blue=0;
				for(double i=0; i<Math.PI/2; i++) {
					 blue= Math.cos(i)*255;
					 green=Math.sin(i)*255;
				}
				for(double j=Math.PI/2; j>0;j--) {
					 red =Math.sin(j);
				}
					Color color = new Color((int) red, (int) green, (int) blue);                   //create color with rgb value
					iterColorMap.put(key, color);                                                 //store color with respective key in iterColorMap                                            
				}
			}
				return iterColorMap;                                                          //returns iterColorMap
	}
}
