package tideman;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Ballot implements Serializable{
    private static final long serialVersionUID = 293469394952L;

    // public static Map<Integer, Candidate> ballotCandies = new HashMap<Integer, Candidate>();
    public Map<Integer, Integer> ballotVote = new HashMap<>(); // Rank, Candidate ID;

    // public static void setBallotCandies() {
    //     for (Candidate c : Candidate.candidates) {
    //         ballotCandies.put(c.id, c);
    //     }
    // }
}
