package context.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.JTextComponent;

/**
 * A java swing application to use the ConTex algorithm.
 * 
 * @author Oscar Ferrandez-Escamez
 * Department of Biomedical Informatics, University of Utah, 2011
 */
public class smallFrame extends JFrame implements ActionListener
{
 
	  JButton Button = new JButton("close");
	  JTextArea TextA = new JTextArea(); 
	  JPanel bottomPanel = new JPanel();
	  JPanel holdAll = new JPanel();
	 
	  
	  //private subclass -- highlight painter
	  Highlighter.HighlightPainter appHighlightPainter = (HighlightPainter) new AppHighlightPainter(Color.pink);

	  //private subclass -- highlight painter
	  class AppHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
	      public AppHighlightPainter(Color color) {
	          super(color);
	      }
	  }
	
	  

	  public smallFrame(String s, String[] cp)
	  {
		  TextA.setText(s);
		  for(int i=0;i<cp.length;i++)
				this.highlight(TextA, "'"+cp[i].toLowerCase()+"'");

		  TextA.setEditable(false);
		  TextA.setLineWrap(true);
		  TextA.setWrapStyleWord(true);
		  JScrollPane scroll = new JScrollPane(TextA);
	    
		  bottomPanel.setLayout(new FlowLayout());
	    bottomPanel.add(Button);
	    
	    JLabel a = new JLabel();
	    a.setText("CONTEXT ALGORITHM RESULTS");
	    JPanel upper = new JPanel();
	    upper.setLayout(new FlowLayout());
	    upper.add(a);
	 
	 
	    holdAll.setLayout(new BorderLayout());
	    holdAll.add(upper, BorderLayout.NORTH);
	    holdAll.add(bottomPanel, BorderLayout.SOUTH);
	    holdAll.add(scroll, BorderLayout.CENTER);
	    holdAll.setBorder(new LineBorder(Color.white,10));
	 
	    getContentPane().add(holdAll, BorderLayout.CENTER);
	 
	    Button.addActionListener(this);
	 
	    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	    
	    this.setLocation(200, 200);
	    this.setSize(500, 300);
	    this.setVisible(true);
	  }
	 
	  
	  public void actionPerformed(ActionEvent e)
	  {
	    if (e.getSource() == Button)
	    {
	    	this.setVisible(false);
	    }
	  }
	  
	  //highlight the concepts in the results frame
	  public void highlight(JTextComponent textComp, String pattern) {
	      //remove old
		  try {
	          Highlighter hilite = textComp.getHighlighter();
	          Document doc = textComp.getDocument();
	          String text = doc.getText(0, doc.getLength()).toLowerCase();
	          int pos = 0;

	          // Search for concepts and add into the highlighter
	          while ((pos = text.indexOf(pattern, pos)) >= 0) {
	              hilite.addHighlight(pos, pos+pattern.length(), appHighlightPainter);
	              pos += pattern.length();
	          }
	      } catch (BadLocationException e) {
	      }
	  }
	}
