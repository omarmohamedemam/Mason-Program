	import org.jgrapht.Graph;
	import org.jgrapht.GraphPath;
	import org.jgrapht.alg.cycle.TarjanSimpleCycles;
	import org.jgrapht.alg.shortestpath.AllDirectedPaths;
	import org.jgrapht.graph.*;

	import java.util.ArrayList;
	import java.util.List;
public class Mason {

	
	    private Graph<String, DefaultWeightedEdge> graph;
	    private String inputNode;
	    private String outputNode;
	    private ArrayList<VertixPluaGain> allForwardPaths;
	    private ArrayList<VertixPluaGain> allIndividualLoops;
	    private ArrayList<ArrayList<ArrayList<VertixPluaGain>>> allNonTouchingLoops;
	    private Double delta;
	    private ArrayList<Double> allDeltasOfForwardPaths;
	    private Double totalTransferFunction;

	    public Mason(ArrayList<String> vertices, ArrayList<DirectedEdge> edges , String ip_node , String op_node) {
	        this.inputNode = ip_node;
	        this.outputNode = op_node;
	        System.out.println(" Input Node :"+inputNode + " " +"Output Node :"+ outputNode);
	        this.graph = new DirectedWeightedPseudograph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
	        for(String vertex : vertices){
	            graph.addVertex(vertex);
	        }
	        for(DirectedEdge edgeData : edges){
	                    DefaultWeightedEdge edge = graph.addEdge(edgeData.getFrom(), edgeData.getTo());
	            graph.setEdgeWeight(edge, edgeData.getWeight());
	        }
	    }

	    public ArrayList<VertixPluaGain> getAllForwardPaths(){
	        if(allForwardPaths != null){
	            return allForwardPaths;
	        }

	        if(graph == null)
	            return null;

	        ArrayList<VertixPluaGain> forwardPaths = new ArrayList<>();

	        AllDirectedPaths<String, DefaultWeightedEdge> pathsFinder = new AllDirectedPaths<>(graph);
	        List<GraphPath<String, DefaultWeightedEdge> > graphPaths = pathsFinder.getAllPaths(inputNode, outputNode, true, Integer.MAX_VALUE);

	        for(GraphPath<String, DefaultWeightedEdge> graphPath : graphPaths){
	            double gain = 1;
	            List<DefaultWeightedEdge> edgeList = graphPath.getEdgeList();
	           
	            for(DefaultWeightedEdge edge : edgeList){
	                
	                gain *= graph.getEdgeWeight(edge);
	            }
	       
	            List<String> vertices = graphPath.getVertexList();

	            forwardPaths.add(new VertixPluaGain(vertices, gain));
	        }

	        this.allForwardPaths = forwardPaths;

	        return forwardPaths;
	    }

	    public ArrayList<VertixPluaGain> getAllIndividualLoops(){
	        if(allIndividualLoops != null){
	            return allIndividualLoops;
	        }

	        ArrayList<VertixPluaGain> individualLoops = new ArrayList<>();

	        TarjanSimpleCycles<String, DefaultWeightedEdge> cycleDetector
	                = new TarjanSimpleCycles<String, DefaultWeightedEdge>(graph);

	        List<List<String>> cycleVertices = cycleDetector.findSimpleCycles();

	        for(List<String> cycle : cycleVertices){
	            double gain = getGain(cycle);
	        
	            individualLoops.add(new VertixPluaGain(cycle, gain));
	        }

	        this.allIndividualLoops = individualLoops;

	        return individualLoops;

	    }

	    public ArrayList<ArrayList<ArrayList<VertixPluaGain>>> getAllNonTouchingLoops(){
	        if(allNonTouchingLoops != null){
	            return allNonTouchingLoops;
	        }

	        ArrayList<VertixPluaGain> individualLoops = getAllIndividualLoops();
	        ArrayList<ArrayList<ArrayList<VertixPluaGain>>> allNonTouchingLoops = new ArrayList<>();
	        boolean stop = false;
	        for(int n=2; !stop; n++){
	            ArrayList<ArrayList<VertixPluaGain>> nCombinations = Combine.getAllCombinations(individualLoops, n);
	            ArrayList<ArrayList<VertixPluaGain>> nNonTouchingLoops = new ArrayList<>();

	            for(ArrayList<VertixPluaGain> combination : nCombinations){
	                if(isNonTouchingLoops(combination)){
	                    nNonTouchingLoops.add(combination);
	                }
	            }

	            if(nNonTouchingLoops.isEmpty()){
	                stop = true;
	                continue;
	            }

	            allNonTouchingLoops.add(nNonTouchingLoops);

	        }

	        this.allNonTouchingLoops = allNonTouchingLoops;

	        return allNonTouchingLoops;
	    }

	    public double getDelta(){
	        if(delta != null){
	            return delta;
	        }

	        double sumOfIndividualLoopGains = 0;
	        for(VertixPluaGain loop : getAllIndividualLoops()){
	            sumOfIndividualLoopGains += loop.getGain();
	        }

	        int sign = 1;
	        double resultOfAllNonTouchingLoops = 0;

	        for(ArrayList<ArrayList<VertixPluaGain>> nNonTouchingLoops : getAllNonTouchingLoops()){
	            double sumOfGainProductsOfNNonTouchingLoops = 0;
	            for(ArrayList<VertixPluaGain> combination : nNonTouchingLoops){
	                double gainProductOfCombination = 1;
	                for(VertixPluaGain loop : combination){
	                    gainProductOfCombination *= loop.getGain();
	                }
	                sumOfGainProductsOfNNonTouchingLoops += gainProductOfCombination;
	            }
	            resultOfAllNonTouchingLoops += sign * sumOfGainProductsOfNNonTouchingLoops;
	            sign *= -1;
	        }

	        this.delta = 1 - sumOfIndividualLoopGains + resultOfAllNonTouchingLoops;

	        return this.delta;
	    }

	    public ArrayList<Double> getAllDeltasOfForwardPaths(){
	        if(allDeltasOfForwardPaths != null){
	            return allDeltasOfForwardPaths;
	        }

	        allDeltasOfForwardPaths = new ArrayList<>();

	        for(VertixPluaGain forwardPath : getAllForwardPaths()){
	            double sumOfIndividualLoopGains = 0;
	            for(VertixPluaGain loop : getAllIndividualLoops()){
	                if(isNonTouchingWithForwardPath(forwardPath, loop)) {
	                    sumOfIndividualLoopGains += loop.getGain();
	                }
	            }

	            int sign = 1;
	            double resultOfAllNonTouchingLoops = 0;

	            for(ArrayList<ArrayList<VertixPluaGain>> nNonTouchingLoops : getAllNonTouchingLoops()){
	                double sumOfGainProductsOfNNonTouchingLoops = 0;
	                for(ArrayList<VertixPluaGain> combination : nNonTouchingLoops){
	                    if(isNonTouchingWithForwardPath(forwardPath, combination)) {
	                        double gainProductOfCombination = 1;
	                        for (VertixPluaGain loop : combination) {
	                            gainProductOfCombination *= loop.getGain();
	                        }
	                        sumOfGainProductsOfNNonTouchingLoops += gainProductOfCombination;
	                    }
	                }
	                resultOfAllNonTouchingLoops += sign * sumOfGainProductsOfNNonTouchingLoops;
	                sign *= -1;
	            }

	            double deltaI = 1 - sumOfIndividualLoopGains + resultOfAllNonTouchingLoops;
	            allDeltasOfForwardPaths.add(deltaI);
	        }

	        return allDeltasOfForwardPaths;
	    }

	    public double getTotalTransferFunction(){
	        if(totalTransferFunction != null){
	            return totalTransferFunction;
	        }

	        double sumOfProducts = 0;
	        int i=0;

	        for(VertixPluaGain forwardPath : getAllForwardPaths()){
	            sumOfProducts += forwardPath.getGain() * getAllDeltasOfForwardPaths().get(i);
	            i++;
	        }

	        totalTransferFunction = sumOfProducts / getDelta();

	        return totalTransferFunction;
	    }

	    private boolean isNonTouchingWithForwardPath(VertixPluaGain forwardPath, VertixPluaGain loop){

	        for(String vertex : forwardPath.getVertices()){
	            if(loop.getVertices().contains(vertex))
	                return false;
	        }
	        return true;
	    }

	    private boolean isNonTouchingWithForwardPath(VertixPluaGain forwardPath, ArrayList<VertixPluaGain> nonTouchingCombination){
	        ArrayList<String> allVerticesInCombination = new ArrayList<>();
	        for(VertixPluaGain loop : nonTouchingCombination){
	            allVerticesInCombination.addAll(loop.getVertices());
	        }
	        for(String vertex : forwardPath.getVertices()){
	            if(allVerticesInCombination.contains(vertex))
	                return false;
	        }
	        return true;
	    }

	    private boolean isNonTouchingLoops(ArrayList<VertixPluaGain> combination){
	        for(int i=0; i<combination.size(); i++){
	            for(int j=0; j<combination.get(i).getVertices().size(); j++){
	                String vertex = combination.get(i).getVertices().get(j);
	                for(int k=i+1; k<combination.size(); k++){
	                    if(combination.get(k).getVertices().contains(vertex)){
	                        return false;
	                    }
	                }
	            }
	        }
	        return true;
	    }

	    private double getGain(List<String> cycle) {
	        double gain = 1;
	        for(int i=0; i<cycle.size()-1; i++){
	            DefaultWeightedEdge edge = graph.getEdge(cycle.get(i), cycle.get(i+1));
	            gain *= graph.getEdgeWeight(edge);
	        }
	        DefaultWeightedEdge edge = graph.getEdge(cycle.get(cycle.size()-1), cycle.get(0));
	        gain *= graph.getEdgeWeight(edge);
	        return gain;
	    }
	}


