import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;
import com.mxgraph.layout.*;
import com.mxgraph.swing.*;
import org.jgrapht.*;
import org.jgrapht.ext.*;
import org.jgrapht.graph.*;
import java.util.regex.Pattern;

import javax.swing.*;
import java.awt.*;
public class Main extends JApplet {
	private static final long serialVersionUID = 2202072534703043194L;

    private static final Dimension DEFAULT_SIZE = new Dimension(530, 320);

    private JGraphXAdapter<String, DefaultEdge> jgxAdapter;
 public static void main(String[] args) {
	 
	 Main applet = new Main();
     applet.init();

     JFrame frame = new JFrame();
     frame.getContentPane().add(applet);
     frame.setTitle("Graph Represenation");
     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     frame.pack();
     frame.setVisible(true);
}
 @Override
 public void init()
 {
	 
     ListenableGraph<String, DefaultEdge> g =
         new DefaultListenableGraph<>(new DefaultDirectedGraph<>(DefaultEdge.class));
     jgxAdapter = new JGraphXAdapter<>(g);
     setPreferredSize(DEFAULT_SIZE);
     mxGraphComponent component = new mxGraphComponent(jgxAdapter);
     component.setConnectable(false);
     component.getGraph().setAllowDanglingEdges(false);
     getContentPane().add(component);
     resize(DEFAULT_SIZE);

     System.out.println("************************* Mason Formula Programm ******************************");
	System.out.println("                       *****By:Omar Mohamed Emam*****");
	 System.err.println("Instructions\n"+"*  Nodes  can have any name from letters only\n"+"*  Edges must have numerical values\n"
			 			+"*  Graph will apper in popup at the end");
	 ArrayList<String> vertices = new ArrayList<>();
	
	 @SuppressWarnings("resource")
	Scanner scanner = new Scanner( System. in);
	 String inputString = new String() ;
	 	
	  System.out.println(" ******************The Vetices************************:"); 
	  
	 while (inputString.isEmpty())  {
		 System.out.println(" Enter The Vertices Sperated by ';':"); 
		 inputString = scanner. nextLine();
	 }
	 inputString.replaceAll("\\s+","");
	 String[] Verticesin= inputString.split(";");
	 for (int i = 0; i < Verticesin.length; i++) {
		 vertices.add(Verticesin[i]);
		 g.addVertex(Verticesin[i]);
		while(! isAlpha(vertices.get(i)) || isRepeated(Verticesin)) {
			System.err.println("Wrong Input !(Reapted nodes or wrong format)");
			System.out.println(" Enter The Vertices Sperated by ';':"); 
			 inputString = scanner. nextLine();
		}
	}

     ArrayList<DirectedEdge> edges = new ArrayList<>();
     int egdes_num=0;
     String[][] alledges = new String[1000][3];
     System.out.println(" ******************The Edges ************************:"); 
     while(!inputString.equalsIgnoreCase("Exit")) {
    	 System.out.println("Enter the Edge as following ==>  Start;End;Weight (Type Exit to finish )"); 
    	 inputString = scanner. nextLine();
    	 inputString.replaceAll("\\s+","");
    	 String[] edge= inputString.split(";");
    	
    	 if(edge.length==3 && Arrays.asList(Verticesin) .contains(edge[0]) && Arrays.asList(Verticesin) .contains(edge[1])
                  ) {
    		 alledges[egdes_num]=edge;
    		 edges.add(new DirectedEdge(edge[0], edge[1],Integer.parseInt(edge[2]) ));
    		 g.addEdge(edge[0], edge[1]);
    		 egdes_num++;
    	 }
    
    	 
     } 
     String ip = new String();
     String op=new String();
    do {
     System.out.println("Choose Input Node :");
      ip= scanner. nextLine();
     System.out.println("Choose Output Node :");
      op= scanner. nextLine();
    }while(!isAlpha(ip)&& !isAlpha(op)&& !vertices.contains(ip)&&!vertices.contains(op));
   
   Mason ans = new Mason(vertices, edges,ip,op);
System.err.println("********************** Answers**************************");
     System.out.println("forward paths: " );
     ArrayList<VertixPluaGain>FB= ans.getAllForwardPaths();
     if(FB.isEmpty()) {
    	 System.out.println("               No Forowad Paths");
     }
     else {
    	 for (int i = 0; i < FB.size(); i++) {
        	 VertixPluaGain temp = FB.get(i);
        	 for (int j = 0; j < temp.getVertices().size(); j++) {
    			if(j==0) {
    				System.out.print("Path No. "+i+" : "+temp.getVertices().get(j));
    			}
    			else {
    				System.out.print("--->"+temp.getVertices().get(j));
    			}			
    		}
        	 System.out.println();
        	 System.out.println("Gain:"+temp.getGain());
    	}
     }
     
     System.out.println("individual loops: ");
     ArrayList<VertixPluaGain>il=   ans.getAllIndividualLoops();
     if(il.isEmpty()) {
    	 System.out.println("               No Individual Loops");
     }
     else {
    	 for (int i = 0; i < il.size(); i++) {
        	 VertixPluaGain temp = il.get(i);
        	 for (int j = 0; j < temp.getVertices().size(); j++) {
    			if(j==0) {
    				System.out.print("Path No. "+i+" : "+temp.getVertices().get(j));
    			}
    			else {
    				System.out.print("--->"+temp.getVertices().get(j));
    			}			
    		}
        	 System.out.println();
        	 System.out.println("Gain:"+temp.getGain());
    	}
     }
    
     System.out.println("non touching loops: " + ans.getAllNonTouchingLoops());

     System.out.println("delta: " + ans.getDelta());

     System.out.println("All deltas for forward paths( 1st order , 2nd order,...): " + ans.getAllDeltasOfForwardPaths());
     ArrayList<Double>DP=   ans.getAllDeltasOfForwardPaths();
     if(DP.isEmpty()) {
    	 System.out.println("               No Deltas Of Forward Paths");
     }
     else {
    	 for (int i = 0; i < il.size(); i++) {
				System.out.println("Dleta for Path No."+i+":"+DP.get(i));	
		} 
     }
    
    	
	
     System.out.println("Total transfer function: " + ans.getTotalTransferFunction());

   System.out.println("**************************************************************");
     mxCircleLayout layout = new mxCircleLayout(jgxAdapter);

     // center the circle
     int radius = 100;
     layout.setX0((DEFAULT_SIZE.width / 2.0) - radius);
     layout.setY0((DEFAULT_SIZE.height / 2.0) - radius);
     layout.setRadius(radius);
     layout.setMoveCircle(true);

     layout.execute(jgxAdapter.getDefaultParent());
     // that's all there is to it!...
 }
 public boolean isAlpha(String name) {
	    char[] chars = name.toCharArray();

	    for (char c : chars) {
	        if(!Character.isLetter(c)) {
	            return false;
	        }
	    }

	    return true;
	}
 public boolean isRepeated(String[] in) {
	 boolean duplicates=false;
	 for (int j=0;j<in.length;j++)
	   for (int k=j+1;k<in.length;k++)
	     if (k!=j && in[k].equals(in[j]))
	       duplicates=true;
	return duplicates;
 }
}
