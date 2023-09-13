package tideman;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Edge implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Edge.class);
    public int key;
    public int val1;
    public int val2;

    public final static Set<Integer> edgeKeys = new HashSet<>();
    public final static Map<Integer, Edge> edges = new HashMap<>(); // edgeKey, Edge

    public Edge(Candidate c1, Candidate c2) {
        this.key = genKey(c1.id, c2.id);
        this.val1 = 0;
        this.val2 = 0;
    }

    public static int genKey(int i, int j) {
        if (i > j) {
            int tmp = i;
            i = j;
            j = tmp;
        }
        return i * 100 + j;
    } 

    public Candidate getCandidate1() {
        return Candidate.candidates.get(this.key / 100 - 1);
    }
    public Candidate getCandidate2() {
        return Candidate.candidates.get(this.key % 100 - 1);
    }

    public static void createEdges() {
        for (int i = 0; i < Candidate.candidates.size(); i++) {
            Candidate ic = Candidate.candidates.get(i);
            for (int j = i + 1; j < Candidate.candidates.size(); j++) {
                Candidate jc = Candidate.candidates.get(j);
                Edge e = new Edge(ic, jc);
                // if (!edgeKeys.contains(e.key)) {
                //     edgeKeys.add(e.key);
                //     edges.add(e);
                // }
                edges.put(e.key, e);
                System.out.println("edge: " + e.key + " c0.name: " + ic.fname + ", " + ic.lname + ", c1.name; " + jc.fname + ", " + jc.lname);
            }
        }
    }

    public static Candidate winner() {
        Candidate result = null;
        List<Edge> sortedEdges = edges.values().stream().sorted((e1, e2) -> {
            int abs1 = Math.abs(e1.val1 - e1.val2);
            int abs2 = Math.abs(e2.val1 - e2.val2);
            if (abs1 > abs2) {
                return -1;
            } else if (abs1 < abs2) {
                return 1;
            } else {
                return 0;
            }
        }).toList();
        Set<Integer> counted = new HashSet<>();
        for (Edge e : sortedEdges) {
            Candidate c1 = e.getCandidate1();
            Candidate c2 = e.getCandidate2();
            logger.info("Edge: " + c1.fname + " " + c1.lname + " " + c2.fname + " " + c2.lname + " " + e.val1 + " " + e.val2);
            if (counted.contains(c1.id) && counted.contains(c2.id)) {
                break;
            } else {
                if (e.val1 > e.val2) {
                    result = c1;
                } else {
                    result = c2;
                }
                counted.add(result.id);
            }
        }
        logger.info("============Winner so far after this ballot: " + result.fname + " " + result.lname);
        return result;
    }
}
