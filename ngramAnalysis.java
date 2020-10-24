import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

public class ngramAnalysis {

	static NumberFormat formatter = new DecimalFormat("#0.0000");

	public static List<String> ngrams(int n, String _content) {
		List<String> ngrams = new ArrayList<String>();

		String[] words = _content.replaceAll("  ", " ").split(" ");
		for (int i = 0; i < words.length - n + 1; i++)
			ngrams.add(concat(words, i, i + n));
		return ngrams;
	}

	public static String concat(String[] words, int start, int end) {
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < end; i++)
			sb.append((i > start ? " " : "") + words[i]);
		return sb.toString();
	}

	public static String readFileAsString(String fileName) throws IOException {
		StringBuffer text = new StringBuffer();
		try {
			File fileDir = new File(fileName);
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "ISO-8859-1"));
			String line;
			while ((line = in.readLine()) != null) {
				text.append(line + "\r\n");
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return text.toString();
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		GUI gui = new GUI();//create gui
		
		gui.b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				long startTime = System.currentTimeMillis();//set the timer
		        String novelPath = gui.novelPath;//get novel
		        int n = gui.ngramMethode;//get ngram method
		        
		        
		        if (novelPath != null && n != -1) {
		          String path = "./Novel-Samples/" + novelPath + ".txt";

		          // update label and reset area and bar
		          gui.l1.setText("Current novel is: " + gui.novelPath + " and current N-Gram methode is: " + gui.ngramMethodeAString);
		          gui.area1.setText(" * * * TOP 34 * * * " + "\n");
		          gui.area2.setText(" * * * TOP 35-67 * * * " + "\n");
		          gui.area3.setText(" * * * TOP 68-100 * * * " + "\n");
		          gui.num = 0;
			  
			
		    String context = " ";
			try {
				context = readFileAsString(path);//read the file
			} catch (IOException er) {
				// TODO Auto-generated catch block
				er.printStackTrace();
			}
			
		    context = context.replace('ý', 'ı');//replace some letters with appropriate Turkish letters.
		    context = context.replace('þ', 'ş');
		    context = context.replace('ð', 'ğ');
		    
	        List<ngram> ngramList = new ArrayList<ngram>();//creating ngramList.
	        
	        for (String ngramString : ngrams(n, context)) {
	          boolean isFound = false;
	          for (int k = 0; k < ngramList.size(); k++) {
	            ngram s = ngramList.get(k);
	            if (ngramString.equals(s.ngram)) {//if found, increase the counter of the word.
	            	isFound = true;
	            	s.count++;
	            }
	          }
	          if (!isFound)//if not found, create new one
	            ngramList.add(new ngram(1, ngramString));
	        }
	        
	        Collections.sort(ngramList, new Sortbycount());//Sort the ngramList by count.	
	        
	        for (int i = 0; i < 34; i++) {//print screen.
	            ngram s = ngramList.get(i);
	            gui.area1.append((i+1) + ")" + s.ngram.replaceAll("\r\n", "") + "--> " + s.count  + "\n");
	          }
	          for (int i = 34; i < 67; i++) {
	            ngram s = ngramList.get(i);
	            gui.area2.append((i+1) + ")" + s.ngram.replaceAll("\r\n", "") + "--> " + s.count + "\n");
	          }
	          for (int i =  67; i < 100; i++) {
	            ngram s = ngramList.get(i);
	            gui.area3.append((i+1) + ")" + s.ngram.replaceAll("\r\n", "") + "--> " + s.count + "\n");
	          }

	          
	          long estimatedTime = System.currentTimeMillis() - startTime;
	          double seconds = (double) estimatedTime / 1000;
	          gui.l2.setText("Total estimated time: " + seconds);
		}
		        
		    else
		       gui.l1.setText("Please select BOTH novel and ngram methode to show top100 values!!" );

	}
});
		
}



	
}

class ngram {
	int count;
	String ngram;

	public ngram(int _count, String _ngram) {
		this.count = _count;
		this.ngram = _ngram;
	}

	public int getCount() {
		return count;
	}
}

class Sortbycount implements Comparator<ngram> {
	// Used for sorting in descending order for count.
	public int compare(ngram a, ngram b) {
		return b.count - a.count;
	}
}

class GUI implements ActionListener {
	  JFrame f;
	  int num = 0;
	  JButton b, b1, b2, b3, b4, b5, b6, b7, b8;
	  JProgressBar pb;
	  JTextArea area1, area2, area3;
	  JLabel l1, l2;

	  int ngramMethode = -1;
	  String ngramMethodeAString = null;
	  String novelPath = null;

	  GUI() {
	    f = new JFrame();// creating instance of JFrame
	    f.setTitle("Mustafa Özsaraç - 2016510086");

	    // files `Button`
	    b1 = new JButton("BİLİM İŞ BAŞINDA");// creating instance of JButton
	    b2 = new JButton("BOZKIRDA");
	    b3 = new JButton("DEĞİŞİM");
	    b4 = new JButton("DENEMELER");
	    b5 = new JButton("UNUTULMUŞ DİYARLAR");
	    b1.setBounds(50, 50, 150, 40);// x axis, y axis, width, height
	    b2.setBounds(200, 50, 150, 40);
	    b3.setBounds(350, 50, 150, 40);
	    b4.setBounds(500, 50, 150, 40);
	    b5.setBounds(650, 50, 150, 40);
	    b6 = new JButton("Unigram");
	    b7 = new JButton("Bigram");
	    b8 = new JButton("Trigram");
	    b6.setBounds(50, 100, 150, 40);
	    b7.setBounds(200, 100, 150, 40);
	    b8.setBounds(350, 100, 150, 40);
	    f.add(b1);
	    f.add(b2);
	    f.add(b3);
	    f.add(b4);
	    f.add(b5);
	    f.add(b6);
	    f.add(b7);
	    f.add(b8);
	    b1.addActionListener(this);
	    b2.addActionListener(this);
	    b3.addActionListener(this);
	    b4.addActionListener(this);
	    b5.addActionListener(this);
	    b6.addActionListener(this);
	    b7.addActionListener(this);
	    b8.addActionListener(this);
	    b = new JButton("Print");
	    b.setBounds(500, 100, 150, 40);
	    f.add(b);
	    b.setBackground(Color.BLUE);
	    b.setForeground(Color.WHITE);

	    area1 = new JTextArea();
	    area2 = new JTextArea();
	    area3 = new JTextArea();
	    area1.setBounds(50, 150, 250, 600);
	    area2.setBounds(300, 150, 250, 600);
	    area3.setBounds(550, 150, 250, 600);
	    area1.setEditable(false);
	    area2.setEditable(false);
	    area3.setEditable(false);
	    f.add(area1);
	    f.add(area2);
	    f.add(area3);
	    
	    
	    l1 = new JLabel();
	    l1.setBounds(50, 20, 600, 15);
	    l1.setText("Please select a novel and N-gram method, then press print button to show TOP100 words." );
	    f.add(l1);
	    l2 = new JLabel();
	    l2.setBounds(650, 110, 445, 15);
	    l2.setForeground(Color.RED);
	    f.add(l2);

	    f.setSize(850, 800);
	    f.setLayout(null);
	    f.setVisible(true);
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  }

	  public void actionPerformed(ActionEvent e) {
		  if(e.getSource() == b6 ) {
			  ngramMethode = 1;
			  ngramMethodeAString = "Unigram";
		  }
		  else if(e.getSource() == b7) {
			  ngramMethode = 2;
			  ngramMethodeAString = "Bigram";
		  }
		  else if(e.getSource() == b8) {
			  ngramMethode = 3;
			  ngramMethodeAString = "Trigram";
		  }
		  else {
			  novelPath = e.getActionCommand().toString();
		  }
	    
	  }

	}