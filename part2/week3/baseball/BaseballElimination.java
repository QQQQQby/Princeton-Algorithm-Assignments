import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BaseballElimination {

    private final int n;
    private final HashMap<String, Integer> teamIndexMap;
    private final List<String> teams;
    private final int[] wins, losses, remaining;
    private final int[][] remainingMat;

    private final List<Bag<String>> elimination;

    public BaseballElimination(String filename) { // create a baseball division from given filename in format specified below
        In in = new In(filename);
        n = in.readInt();
        teamIndexMap = new HashMap<>();
        teams = new ArrayList<>(n);
        wins = new int[n];
        losses = new int[n];
        remaining = new int[n];
        remainingMat = new int[n][n];

        for (int i = 0; i < n; ++i) {
            teams.add(in.readString());
            teamIndexMap.put(teams.get(i), i);
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remaining[i] = in.readInt();
            for (int j = 0; j < n; ++j)
                remainingMat[i][j] = in.readInt();
        }
        in.close();

        elimination = new ArrayList<>(n);
        int numGames = (n - 1) * (n - 2) / 2;

        for (int x = 0; x < n; ++x) {

            boolean flag = false;
            for (int i = 0; i < n; ++i) {
                if (i != x && wins[x] + remaining[x] - wins[i] < 0) {
                    Bag<String> certificate = new Bag<>();
                    certificate.add(teams.get(i));
                    for (int j = i + 1; j < n; ++j)
                        if (j != x && wins[x] + remaining[x] - wins[j] < 0)
                            certificate.add(teams.get(j));
                    elimination.add(certificate);
                    flag = true;
                    break;
                }
            }
            if (flag)
                continue;


            FlowNetwork flowNetwork = new FlowNetwork(1 + numGames + n);
            FlowEdge[] gameEdges = new FlowEdge[numGames];

            // Connect the artificial source vertex to each game vertex,
            int t = 0;
            for (int i = 0; i < n; ++i) {
                if (i == x)
                    continue;
                for (int j = i + 1; j < n; ++j)
                    if (j != x) {
                        gameEdges[t] = new FlowEdge(0, 1 + t, remainingMat[i][j]);
                        flowNetwork.addEdge(gameEdges[t++]);
                    }
            }

            // Connect each game vertex with the two opposing team vertices.
            t = 1;
            for (int i = 0; i < n; ++i) {
                for (int j = i + 1; j < n - 1; ++j) {
                    flowNetwork.addEdge(new FlowEdge(t, 1 + numGames + j, Double.POSITIVE_INFINITY));
                    flowNetwork.addEdge(new FlowEdge(t, 1 + numGames + i, Double.POSITIVE_INFINITY));
                    ++t;
                }
            }

            // Connect each team vertex to the artificial sink vertex.
            for (int i = 0; i < n; ++i) {
                if (i != x) {
                    flowNetwork.addEdge(new FlowEdge(
                            1 + numGames + i + (i < x ? 0 : -1),
                            n + numGames,
                            wins[x] + remaining[x] - wins[i]
                    ));
                }
            }

            FordFulkerson maxFlowSolution = new FordFulkerson(flowNetwork, 0, numGames + n);
            boolean canWin = true;
            for (FlowEdge edge : gameEdges)
                if (edge.flow() < edge.capacity())
                    canWin = false;
            if (canWin) {
                elimination.add(null);
            } else {
                Bag<String> certificate = new Bag<>();
                for (int i = 0; i < n - 1; ++i)
                    if (maxFlowSolution.inCut(1 + numGames + i))
                        certificate.add(teams.get(i < x ? i : i + 1));
                elimination.add(certificate);
            }
        }
    }

    public int numberOfTeams() { // number of teams
        return n;
    }

    public Iterable<String> teams() { // all teams
        return teams;
    }

    public int wins(String team) { // number of wins for given team
        return wins[getTeamIndex(team)];
    }

    public int losses(String team) { // number of losses for given team
        return losses[getTeamIndex(team)];
    }

    public int remaining(String team) { // number of remaining games for given team
        return remaining[getTeamIndex(team)];
    }

    public int against(String team1, String team2) { // number of remaining games between team1 and team2
        return remainingMat[getTeamIndex(team1)][getTeamIndex(team2)];
    }

    public boolean isEliminated(String team) { // is given team eliminated?
        return elimination.get(getTeamIndex(team)) != null;
    }

    public Iterable<String> certificateOfElimination(String team) { // subset R of teams that eliminates given team; null if not eliminated
        return elimination.get(getTeamIndex(team));
    }

    private int getTeamIndex(String team) {
        return teamIndexMap.computeIfAbsent(team, k -> {
            throw new IllegalArgumentException();
        });
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
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
