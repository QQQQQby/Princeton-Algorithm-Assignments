import edu.princeton.cs.algs4.*;

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
            FlowNetwork flowNetwork = new FlowNetwork(1 + numGames + n);

            // Connect the artificial source vertex to each game vertex,
            // and connect each game vertex with the two opposing team vertices.
            int gameIndex = 1;
            for (int i = 0; i < n; ++i) {
                if (i == x)
                    continue;
                int shiftI = i < x ? 0 : -1;
                for (int j = i + 1; j < n; ++j) {
                    if (j == x)
                        continue;
                    flowNetwork.addEdge(new FlowEdge(0, gameIndex, remainingMat[i][j]));
                    flowNetwork.addEdge(new FlowEdge(
                            gameIndex,
                            1 + numGames + i + shiftI,
                            Double.POSITIVE_INFINITY
                    ));
                    flowNetwork.addEdge(new FlowEdge(
                            gameIndex,
                            1 + numGames + j + (j < x ? 0 : -1),
                            Double.POSITIVE_INFINITY
                    ));
                    ++gameIndex;
                }
            }

            // Connect each team vertex to the artificial sink vertex.
            for (int i = 0; i < n; ++i) {
                if (i == x) {
                    continue;
                }
                flowNetwork.addEdge(new FlowEdge(
                        1 + numGames + i + (i < x ? 0 : -1),
                        n + numGames,
                        Math.max(wins[x] + remaining[x] - wins[i], 0)
                ));
            }
            System.out.println(x);
            System.out.println(flowNetwork);
            System.out.println(new FordFulkerson(flowNetwork,0,n+numGames).value());
            System.out.println();
            System.out.println();

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
//        if(!teamIndexMap.containsKey(team))
//            throw new IllegalArgumentException();
//        return teamIndexMap.get(team);
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
