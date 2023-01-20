/* *****************************************************************************
 *  Name: Haozhi Fan
 *  Date: 12/11/2020
 *  Description: use maxflow/mincut to determine which team is mathematically eliminated
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class BaseballElimination {
    private final ArrayList<String> teams;
    private final int[] w, l, r;
    private final int[][] g;
    private final int n;

    // create a baseball division from given filename
    public BaseballElimination(String filename) {
        In in = new In(filename);
        n = Integer.parseInt(in.readLine());
        w = new int[n];
        l = new int[n];
        r = new int[n];
        g = new int[n][n];

        // initialize a symbol table to hold team names and their IDs
        teams = new ArrayList<String>();

        int i = 0;
        while (in.hasNextLine()) {
            String s = in.readLine().trim();
            String[] teamInfo = s.split("\\s+");
            teams.add(teamInfo[0]);
            w[i] = Integer.parseInt(teamInfo[1]);
            l[i] = Integer.parseInt(teamInfo[2]);
            r[i] = Integer.parseInt(teamInfo[3]);
            for (int j = 0; j < n; j++)
                g[i][j] = Integer.parseInt(teamInfo[4 + j]);
            i++;
        }
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        // the trivial elimination case
        for (String s : teams()) {
            if (!s.equals(team) && wins(team) + remaining(team) < wins(s))
                return true;
        }
        // if not trivially eliminated, build a FlowNetwork and compute flow
        Queue<String> eliminators = getEliminators(team);

        if (eliminators == null) return false;
        return true;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        Queue<String> trivialEliminators = new Queue<String>();
        // the trivial elimination case
        for (String s : teams()) {
            if (!s.equals(team) && wins(team) + remaining(team) < wins(s))
                trivialEliminators.enqueue(s);
        }
        if (!trivialEliminators.isEmpty()) return trivialEliminators;
        // if not trivially eliminated, build a FlowNetwork and get eliminators
        return getEliminators(team);
    }

    private Queue<String> getEliminators(String team) {
        Bag<FlowEdge> edges = new Bag<FlowEdge>();
        int teamID = teams.indexOf(team);
        int sID = n;                        // define id of s = n
        int gvID = n + 1;                   // define the first game vertex id = n+1

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (i != teamID && j != teamID && g[i][j] != 0) {
                    // add edge between s and game vertices
                    edges.add(new FlowEdge(sID, gvID, g[i][j]));
                    // add edge between game vertices and team vertices
                    edges.add(new FlowEdge(gvID, i, Integer.MAX_VALUE));
                    edges.add(new FlowEdge(gvID, j, Integer.MAX_VALUE));
                    gvID++;
                }
            }
        }
        // add edge between team vertices and t
        int tID = gvID;
        for (String s : teams()) {
            if (!s.equals(team)) {
                int capacity = wins(team) + remaining(team) - wins(s);
                edges.add(new FlowEdge(teams.indexOf(s), tID, capacity));
            }
        }

        // build FlowNetwork and use FF to compute max flow or min cut
        FlowNetwork FN = new FlowNetwork(tID + 1);
        for (FlowEdge e : edges) FN.addEdge(e);
        FordFulkerson FF = new FordFulkerson(FN, sID, tID);

        // initialize ArrayList to hold teams which eliminate our candidate
        ArrayList<String> eliminator = new ArrayList<String>();
        for (int i = 0; i < n; i++) eliminator.add(null);
        // find teams which eliminate our candidate
        for (String s : teams()) {
            if (!s.equals(team) && FF.inCut(teams.indexOf(s)))
                eliminator.set(teams.indexOf(s), s);
        }

        // rearrange in correct order
        Queue<String> e = new Queue<String>();
        for (String s : eliminator) {
            if (s != null) e.enqueue(s);
        }

        if (e.isEmpty()) return null;
        return e;
    }

    public int numberOfTeams() {
        return n;
    }

    // all teams
    public Iterable<String> teams() {
        return teams;
    }

    // number of wins for given team
    public int wins(String team) {
        if (!teams.contains(team)) throw new IllegalArgumentException("No such team exists!");
        return w[teams.indexOf(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!teams.contains(team)) throw new IllegalArgumentException("No such team exists!");
        return l[teams.indexOf(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!teams.contains(team)) throw new IllegalArgumentException("No such team exists!");
        return r[teams.indexOf(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!teams.contains(team1) || !teams.contains(team2))
            throw new IllegalArgumentException("No such team exists!");
        int id1 = teams.indexOf(team1);
        int id2 = teams.indexOf(team2);
        return g[id1][id2];
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
