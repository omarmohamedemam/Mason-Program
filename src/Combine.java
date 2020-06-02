import java.util.ArrayList;

public class Combine {

    private static ArrayList<VertixPluaGain> combination;
    private static ArrayList<ArrayList<VertixPluaGain>> combinations;
    private static boolean[] used;

    public static ArrayList<ArrayList<VertixPluaGain>>
                    getAllCombinations(ArrayList<VertixPluaGain> arr, int length){

        combination = new ArrayList<>();
        combinations = new ArrayList<>();
        used = new boolean[arr.size()];

        getAllCombinationsRecurse(0, arr, length);
        return combinations;
    }

    private static void getAllCombinationsRecurse(int index, ArrayList<VertixPluaGain> arr, int length){

        if(combination.size() == length){
         
            ArrayList<VertixPluaGain> aCombination = new ArrayList<>();
            aCombination.addAll(combination);
            combinations.add(aCombination);
            
            return;
        }

        if(index == arr.size())
            return;

        // take it
        combination.add(arr.get(index));
        getAllCombinationsRecurse(index+1, arr, length);

        //leave it
        combination.remove(arr.get(index));
        getAllCombinationsRecurse(index+1, arr, length);
    }

}
