import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;

public class FileCleaner {

    public static void main(String args[]){
    	File inputFile = null;
    	File outputFile = null;
    	
    	JButton open = new JButton();
    	final JFileChooser fc = new JFileChooser();
    	String genericStart = System.getProperty("user.home");
    	String testStart = "/Users/stevewangen/projects/arpa/epa_frs_data/national_single/national_combined/";
    	fc.setCurrentDirectory(new File(testStart));
    	
    	fc.setDialogTitle("Choose input file...");
    	int result = fc.showOpenDialog(open);
    	if (result == JFileChooser.APPROVE_OPTION) {
    	    inputFile = fc.getSelectedFile();
    	} 
    	
    	fc.setDialogTitle("Choose output file...");
    	result = fc.showOpenDialog(open);
    	if (result == JFileChooser.APPROVE_OPTION) {
    	    outputFile = fc.getSelectedFile();
    	}
    	System.out.println("Processing file " + inputFile.getName() + " to " + outputFile.getName());
    	processFile(inputFile, outputFile);
    	System.out.println("Processing complete!");
    }  
    
    
    public static void processFile(File inFile, File outFile){
    	FileInputStream fstream;
    	FileOutputStream outStream;
    	
		try {
			
			fstream = new FileInputStream(inFile.getAbsolutePath());
			outStream = new FileOutputStream(outFile.getAbsolutePath());

	    	BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
	    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outStream));
	
	    	String currentLine;
	    	String previousLine = null;
	    	
	
	    	//Read File Line By Line
	    	while ((currentLine = br.readLine()) != null)   {
	    	  
	    		// System.out.println("Cleaning extraneous characters from previousLine");
	    		currentLine = cleanLine(currentLine);
	    		
	    		// check if it begins with a double quote
	    		// System.out.println("First char in line = " + currentLine.charAt(0));
	    		if(currentLine.startsWith("\"")){
	    			
	    			if(previousLine != null){
	    				if(previousLine.endsWith("\"")){
	    					// System.out.println("writing current line to file...");
	    					bw.write(previousLine);
	    					bw.newLine();
	    				} else {
	    					currentLine = previousLine + currentLine;
	    				}
	    			}
	    			
	    			previousLine = currentLine;

	    		} else {

	    			
	    			// System.out.println("Appending previous line w/ current line = " + currentLine);
	    			previousLine = previousLine + currentLine;
	    		}
    		}
	    	
	    	
	    	if(previousLine != null){
				bw.write(previousLine);
				bw.newLine();
			}
			
	
	    	//Close the input stream
	    	br.close();
	    	fstream.close();
	    	
	    	
	    	// Close the output stream
	    	bw.close();
	    	outStream.close();
	    	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
    }
    
    
    
    
    
    public static String cleanLine(String line){
    	
    	//for(int i=0; i <= line.length(); i++){
    	//	char currentChar = line.charAt(i);
    	//	System.out.println(currentChar);
    	//}
		
    	if(line.contains("\0")){
    		int position = line.indexOf("\0");
	    	char thisChar = line.charAt(position);
			System.out.println("Found char: " + thisChar + " at position " + position);
			byte[] ba = line.getBytes();
			for(int i=0; i<ba.length; i++){
				if(ba[i] == 0){
					ba[i] = 32;
				}
			}
			line = new String(ba);
    	}
    	
    	if(line.contains("\0")){
    		System.out.println("STILL NULLS");
    	}
    	
		// System.out.println("Found null chars - replacing w/ spaces...");
		// line.replace('\0', ' ');
		String newLine = line.replaceAll("/\0", "");

		// System.out.println("scraping some characters and storing...");
    	if(line.length() > 0){
			int pos = line.length()-1;
			for(; pos >= 0; pos--){
				if(line.charAt(pos) >= (char) 0x20){
					// System.out.println("previousLine.charAt(" + pos + ") = " + String.format("%02X", (int) previousLine.charAt(pos)) + " AND >= (char) 0x20 - time to break!");
					break;
				}
				// System.out.println("Kicking out char: " + String.format("%02X", (int) previousLine.charAt(pos)));
			}
			
			line = line.substring(0, pos+1);
    	}
		return line;
    }
}
