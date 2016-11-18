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
    public static ArrayList<String> winner = new ArrayList<>();
    public static HashMap<Integer, String > rankmap = new HashMap<>();
    public static int count_loop, counter_winner =0;
    public static boolean checkifSurplus = false;
    public static int checkIfnextCharElected = 0;

    public static void main(String[] args){

        addcandidates();  //Candidates votemap
        addvotes(); //adding votes

        Scanner in = new Scanner(System.in);
        num_cand = in.nextInt();
        win_cand = in.nextInt();
        num_votes = votes.size();

        System.out.println("Number of votes is:" +num_votes);

        quota = ((num_votes * 100)/(win_cand+1))+1;
        final_quota = (((quota + 99) / 100 ) * 100)/100;
        System.out.println("Quota for election is: "+final_quota);
        System.out.println(" ");
        getFirstPref();

//        while(winner.size() != win_cand && rankmap.size() !=num_cand) {
            getresult();
//        }
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
        int surplus=0;
        double dist_percent;
        //surplus
        checkSurplus();
        if (checkifSurplus) {
            Iterator it_countmap = candidates.keySet().iterator();
            while (it_countmap.hasNext()) {
                String key_countmap = (String) it_countmap.next();
                int val_countmap = candidates.get(key_countmap);
                if (val_countmap >= final_quota) {
                    new_candidates.remove(key_countmap);
                    winner.add(key_countmap);
                    rankmap.put(counter_winner, key_countmap);
                    surplus = val_countmap - final_quota;
                    dist_percent = (double) ((surplus * 100 / final_quota));
                    counter_winner++;
                    addSurplusToMap(dist_percent);
                }
            }
        }
        else {

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
            if(key.equals(val)){
                checkIfNextElected(vote);
                key1 = String.valueOf(votes.get(j).charAt(checkIfnextCharElected));
                if(countmap.containsKey(key1)) {
                    countmap.put(key1, countmap.get(key1)+1);
                }
                else {
                    countmap.put(key1,1);
                }
            }
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
    }

    private static void checkIfNextElected(String vote) {
        int count=1;
        for(int j=0; j<winner.size(); j++){
            if(String.valueOf(vote.charAt(count)).equals(winner.get(j))) {
                count++;
            }
            else {
                checkIfnextCharElected = count;
            }
        }
    }


    private static void addcandidates() {
        candidates.put("A",0);
        candidates.put("B",0);
        candidates.put("C",0);
        candidates.put("D",0);
        candidates.put("E",0);
    }

    private static void addvotes() {
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
    }
}
