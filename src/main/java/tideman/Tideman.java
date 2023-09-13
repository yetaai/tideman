package tideman;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class Tideman implements Serializable{
    private static Logger logger = LoggerFactory.getLogger(Tideman.class);
    public static void main(String[] args) {
        prepareCandidates();
        Edge.createEdges();

        Scanner scanner = new Scanner(System.in);
        // BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int ballotRank = 0;
        int ballots = 0;

        topwhile: while (true) {
            System.out.println("Please input nothing or enter 0 to start a new Ballot, 9 to end the vote");
            String line = getNextLine(scanner);
            if (!line.isEmpty()) {
                line = line.trim().toLowerCase();
            }
            System.out.println("=============================your input: " + line);

            if (line.isEmpty() || line.equals("0")) {
                ballotRank = 0;
                Set<Integer> ballotCandies = new HashSet<>();
                ballot: while (ballotRank < Candidate.candidates.size()) {
                    System.out.println(". Please enter candidate first name. 1 to discard. 2 to discard and end the ballot.");
                    String fname = getNextLine(scanner).trim().toLowerCase();
                    if (fname.isEmpty()) {
                        System.out.println("Empty name is not allowed. Try again.");
                        continue;
                    } else if (fname.equals("1")) {
                        continue;
                    } else if (fname.equals("2")) {
                        if (ballotRank > 0) {
                            ballots ++;
                        }
                        break;
                    }
                    System.out.println("Last name: ");
                    String lname = getNextLine(scanner).trim().toLowerCase();
                    if (lname.isEmpty()) {
                        System.out.println("Empty name is not allowed. Try again.");
                        continue;
                    }

                    Candidate candi = findByName(fname, lname);
                    if (candi == null) {
                        System.out.println("Candidate not found. Try again.");
                        continue;
                    }
                    for (Integer ibc : ballotCandies) {
                        Candidate c = Candidate.candidates.get(ibc - 1);
                        if (c.id == candi.id) {
                            System.out.println("Candidate already exists in this ballot. Try again.");
                            continue ballot;
                        }
                    }
                    for (Integer ibc: ballotCandies) {  // All c wins over candi
                        Candidate c = Candidate.candidates.get(ibc - 1);
                            int ekey = Edge.genKey(candi.id, c.id);
                            // System.out.println("---ekey: " + ekey + ". candi.id: " + candi.id + ". c.id: " + c.id);
                            Edge e = Edge.edges.get(ekey);
                            if (c.id < candi.id) {
                                e.val1 ++;
                            } else {
                                e.val2 ++;
                            }
                    }
                    ballotCandies.add(candi.id);
                    ballotRank ++;
                    if (ballotRank == Candidate.candidates.size()) {
                        ballots ++;
                    }
                    System.out.println("Ballots counted: " + ballots + ". Candidates counted in this ballot: " + ballotRank);

                }
                if (ballotRank < Candidate.candidates.size()) {
                    for (Candidate c: Candidate.candidates) {
                        if (!ballotCandies.contains(c.id)) {
                            for (Integer ibc: ballotCandies) {
                                Candidate bc = Candidate.candidates.get(ibc - 1);
                                int ekey = Edge.genKey(c.id, bc.id);
                                Edge e = Edge.edges.get(ekey);
                                if (c.id < bc.id) {
                                    e.val2 ++;
                                } else {
                                    e.val1 ++;
                                }
                            }
                        }
                    }
                }
            } else if (line.equals("9")) {
                System.out.println("=========You input 9. Ending the vote.");
                break topwhile;
            } else {
                System.out.println("Invalid input. Try again.");
                continue topwhile;
            }
            Edge.winner();
        }
        // try {
        //     br.close();
        // } catch (Exception e) {
        //     logger.warn("Cannot close BufferedReader because " + e.getMessage());
        // }
        if (ballots >  0) {
            Edge.winner();
        } else {
            System.out.println("No ballots counted. No winner.");
        }
    }

    public static Candidate findByName(String fname, String lname) {
        for (Candidate c : Candidate.candidates) {
            if (c.fname.toLowerCase().equals(fname) && c.lname.toLowerCase().equals(lname)) {
                return c;
            }
        }
        return null;
    }
    public static void prepareCandidates() {
        try {
            Candidate.CreateCandidate("Ian", "Du");
            Candidate.CreateCandidate("Jone", "Doe");
            Candidate.CreateCandidate("Sarah", "Wheel");
            Candidate.CreateCandidate("Elizabeth", "Smith");
        } catch (Exception e) {
            logger.warn("Cannot create candidate for " + " because " + e.getMessage());
            logger.warn("Please try again. Maybe just name dupicated?");
        }
    }
    private static String getNextLineBr(BufferedReader br) {
        try {
            // BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("========= getNextLine(BufferedReader br) =========");
            return br.readLine();
        } catch (Exception e) {
            return "";
        }
    }
    private static String getNextLine(Scanner scanner) {
        try {
            return scanner.nextLine();
        } catch (Exception e) {
            return "";
        }
    }
}
