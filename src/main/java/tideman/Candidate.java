package tideman;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Candidate implements Comparable<Candidate>, Serializable {
    private final static long serialVersionUID = 73895729471L;
    public static List<Candidate> candidates = new ArrayList<Candidate>();

    public String fname;
    public String lname;
    public int id; 

    public int[] rankedVotes;
    
    public void setRankedVotes() {
        this.rankedVotes = new int[candidates.size()]; 
    }

    @Override
    public int compareTo(Candidate other) {
        return this.fname.compareTo(other.fname + other.lname);
    }

    public static Candidate CreateCandidate(String fname, String lname) throws Exception {
        if (fname == null || lname == null) {
            throw new Exception("Invalid candidate name");
        }

        for (Candidate c : candidates) {
            if (c.fname.equals(fname) && c.lname.equals(lname)) {
                throw new Exception("Candidate already exists");
            }
        }

        Candidate c = new Candidate();
        c.fname = fname;
        c.lname = lname;
        c.id = candidates.size() + 1;
        candidates.add(c);
        return c;
    }
}