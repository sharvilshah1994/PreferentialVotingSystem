import java.util.*;

/**
 * Created by sharvil on 11/14/16.
 */
public class voting {
    static public int num_cand, win_cand, num_votes, quota, final_quota;
    public static HashMap<String, Integer> candidates = new HashMap<>();
    public static HashMap<String, Integer> new_candidates = new HashMap<>();
    public static ArrayList<String> votes = new ArrayList<>();
    public static ArrayList<String> votes_copy = new ArrayList<>();
    public static ArrayList<String> votes_elimination = new ArrayList<>();
    public static ArrayList<String> winner = new ArrayList<>();
    public static ArrayList<String> eliminated = new ArrayList<>();
    public static int counter_winner =0;
    public static boolean checkifSurplus = false;
    public static int checkIfnextCharElected = 0;

    public static void main(String[] args){

        addcandidates();  //Candidates votemap
        addvotes(); //adding votes

//        Scanner in = new Scanner(System.in);
        num_cand = 5;
        win_cand = 2;
        num_votes = votes.size();

        System.out.println("Number of votes is:" +num_votes);

        quota = ((num_votes * 100)/(win_cand+1))+1;
        final_quota = (((quota + 99) / 100 ) * 100)/100;
        System.out.println("Quota for election is: "+final_quota);
        System.out.println(" ");
        getFirstPref();

        while((winner.size() != win_cand)) {
            getresult();
        }

        while (eliminated.size() != 3){
            getresult();
        }

        System.out.println("****RESULTS*****");
        System.out.println("Elected candidates are:"+winner);
        System.out.println(" ");
        System.out.println("Candidates who lost are:"+eliminated);
        System.out.println(" ");
        System.out.println("****END OF RESULTS*****");

    }

    private static void getFirstPref() {
        //Getting First Pref
        String val;
        for(int i=0; i<votes.size();i++){
            val = String.valueOf(votes.get(i).charAt(0));
            if(candidates.containsKey(val)){
                candidates.put(val, candidates.get(val) + 1);
            }
        }

        System.out.println("***First Pref***");
        Iterator it_candmap = candidates.keySet().iterator();
        while (it_candmap.hasNext()){
            String key = (String) it_candmap.next();
            int value = candidates.get(key);
            System.out.println("Key is:"+key+" and value is:" +value);
        }

        new_candidates = (HashMap) candidates.clone(); //Cloning Hashmap
        System.out.println(" ");
    }


    private static void getresult() {
        int surplus=0, val_countmap, elimination_counter=0;
        double dist_percent;
        String key_countmap="";
        Iterator it_countmap = candidates.keySet().iterator();
        //surplus
        checkSurplus();
        if (checkifSurplus) {
            while (it_countmap.hasNext()) {
                key_countmap = (String) it_countmap.next();
                val_countmap = candidates.get(key_countmap);
                if (val_countmap >= final_quota) {
                    new_candidates.remove(key_countmap);
                    winner.add(key_countmap);
                    surplus = val_countmap - final_quota;
                    dist_percent = (double) ((surplus * 100 / final_quota));
                    counter_winner++;
                    addSurplusToMap(dist_percent);
                }
            }
        }
        //Elimination works here!
        else {
            String temp_keycountmap;
            val_countmap = Collections.min(candidates.values());
            while (it_countmap.hasNext()){
                temp_keycountmap = (String) it_countmap.next();
                int val_countmap1 = candidates.get(temp_keycountmap);
                if(val_countmap == val_countmap1){
                    key_countmap = temp_keycountmap;
                    new_candidates.remove(key_countmap);
                    break;
                }
            }

            for(int m=0; m<votes_copy.size(); m++) {
                if (String.valueOf(votes_copy.get(m).charAt(0)).equals(key_countmap)) {
                    elimination_counter++;
                }
            }

            if(elimination_counter > 0) {
                counter_winner++;
                eliminated.add(key_countmap);
                addEliminationVotes(key_countmap);
                candidates.remove(key_countmap);
            }
            else {
                counter_winner++;
                eliminated.add(key_countmap);
                candidates.remove(key_countmap);
            }

            System.out.println("Vote map after round: "+counter_winner);
            Iterator it_candmap = candidates.keySet().iterator();
            while (it_candmap.hasNext()){
                String key = (String) it_candmap.next();
                int value = candidates.get(key);
                System.out.println("Key is:"+key+" and value is:" +value);
            }
            System.out.println(" ");
        }

    }

    public static void addEliminationVotes(String val){
        int value;
        String key, vote;
        HashMap<String, Integer> countmap = new HashMap<>();

        for(int i=0; i<votes_copy.size();i++) {
            vote = votes_copy.get(i);
            //If first pref is winner and size of vote <= rankmap.size
            if ((String.valueOf(vote.charAt(0)).equals(val)) && (vote.length() <= 1)) {
                votes.remove(vote);
            }
        }

        for(int i=0; i<votes.size();i++){
            vote = votes.get(i);
            key = String.valueOf(vote.charAt(0));
            putInCountMap(i,val,key,vote, countmap);
        }
        Iterator it_countmap = countmap.keySet().iterator();
        while (it_countmap.hasNext()){
            key = (String) it_countmap.next();
            value = countmap.get(key);
            if(candidates.containsKey(key)) {
                candidates.put(key, candidates.get(key) + value);
            }
            System.out.println("Key is: "+key+" value is:"+value);
        }

        addvotes();
    }

    public static void putInCountMap(int i, String val, String key, String vote, HashMap<String, Integer> countmap){
        String key1 = "";
        if(val.equals(key)){
            checkIfNextElectedSurplus(vote);
            key1 = String.valueOf(votes.get(i).charAt(checkIfnextCharElected));
            if(countmap.containsKey(key1)) {
                countmap.put(key1, countmap.get(key1)+1);
            }
            else {
                countmap.put(key1,1);
            }
        }
    }

    //Would work for both Elimination & Surplus counting
    private static void checkIfNextElectedSurplus(String vote) {
        int count=1;
        for(int j=0; j<winner.size(); j++){
            if(String.valueOf(vote.charAt(count)).equals(winner.get(j))) {
                count++;
            }
            else {
                checkIfnextCharElected = count;
            }
        }
        checkIfNextElectedElimination(count, vote);
    }
    private static void checkIfNextElectedElimination(int count, String vote) {
        for(int l=0; l<eliminated.size(); l++){
            if(count<vote.length()) {
                if (String.valueOf(vote.charAt(count)).equals(eliminated.get(l))) {
                    count++;
                } else {
                    checkIfnextCharElected = count;
                }
            }
        }
    }

    public static void checkSurplus() {
        Iterator it_countmap = new_candidates.keySet().iterator();
        while (it_countmap.hasNext()) {
            String key_countmap = (String) it_countmap.next();
            int val_countmap = candidates.get(key_countmap);
            if (val_countmap >= final_quota) {
                checkifSurplus = true;
                break;
            }
            else {
                checkifSurplus = false;
            }
        }

    }

    //Called from getResult. Makes countmap & gives count result after each surplus round
    private static void addSurplusToMap(double dist_percent) {
        String val = winner.get(counter_winner-1);
        String vote, key, key1;
        int value;

        for(int i=0; i<votes_copy.size();i++) {
            vote = votes_copy.get(i);
            //If first pref is winner and size of vote <= rankmap.size
            if ((String.valueOf(vote.charAt(0)).equals(val)) && (vote.length() <= 1)) {
                votes.remove(vote);
            }
        }

        HashMap<String, Integer> countmap = new HashMap<>();
        for(int j=0; j<votes.size();j++){
            vote = votes.get(j);
            key = String.valueOf(votes.get(j).charAt(0));
            putInCountMap(j,val, key, vote,countmap);
        }
        Iterator it_countmap = countmap.keySet().iterator();
        while (it_countmap.hasNext()){
            key = (String) it_countmap.next();
            value = countmap.get(key);
            if(candidates.containsKey(key)) {
                candidates.put(key, candidates.get(key) + (int) Math.ceil((value * dist_percent) / 100));
            }
            System.out.println("Key is: "+key+" value is:"+value);
        }


        System.out.println("Vote map after round: "+counter_winner);
        Iterator it_candmap = candidates.keySet().iterator();
        while (it_candmap.hasNext()){
            key = (String) it_candmap.next();
            value = candidates.get(key);
            System.out.println("Key is:"+key+" and value is:" +value);
        }
        System.out.println(" ");
        addvotes();
    }





    private static void addcandidates() {
        candidates.put("A",0);
        candidates.put("B",0);
        candidates.put("C",0);
        candidates.put("D",0);
        candidates.put("E",0);
    }

    private static void addvotes() {
        votes.clear();
        votes.add("ABCDE");
        votes.add("BC");
        votes.add("BDE");
        votes.add("CED");
        votes.add("AED");
        votes.add("ABC");
        votes.add("A");
        votes.add("B");
        votes.add("CDE");
        votes.add("AB");
        votes.add("BCE");
        votes.add("ADE");
        votes.add("AED");
        votes.add("C");
        votes.add("BD");
        votes.add("CD");
        votes.add("ABC");
        votes.add("ABC");
        votes.add("ABC");
        votes.add("ABC");
        votes_copy = (ArrayList<String>) votes.clone();
        votes_elimination = (ArrayList<String>) votes.clone();
    }
}
