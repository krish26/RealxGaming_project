import java.util.*;

public class Main {

    public static void main(String[] args) {

        double betAmount = 10.0;

        // Initialize payout table
        Map<Symbol, int[]> payoutTable = new HashMap<>();
        payoutTable.put(Symbol.H1, new int[]{5, 6, 7, 8, 10});
        payoutTable.put(Symbol.H2, new int[]{4, 5, 6, 7, 9});
        payoutTable.put(Symbol.H3, new int[]{4, 5, 6, 7, 9});
        payoutTable.put(Symbol.H4, new int[]{3, 4, 5, 6, 7});
        payoutTable.put(Symbol.L5, new int[]{1, 2, 3, 4, 5});
        payoutTable.put(Symbol.L6, new int[]{1, 2, 3, 4, 5});
        payoutTable.put(Symbol.L7, new int[]{1, 2, 3, 4, 5});
        payoutTable.put(Symbol.L8, new int[]{1, 2, 3, 4, 5});
        payoutTable.put(Symbol.WR, null);
        payoutTable.put(Symbol.BK, null);

        // Initialize symbol weights - all 100 for simplicity
        Map<Symbol, Integer> symbolWeights = new HashMap<>();
        for (Symbol s : Symbol.values()) {
            symbolWeights.put(s, 100);
        }

        GridGenerator gridGenerator = new GridGenerator(symbolWeights);
        ClusterFinder clusterFinder = new ClusterFinder(GridGenerator.getRows(), GridGenerator.getCols(), payoutTable);
        AvalancheProcess avalancheProcessor = new AvalancheProcess(GridGenerator.getRows(), GridGenerator.getCols(), gridGenerator.getWeightedSymbols());
        PayoutCalculator payoutCalculator = new PayoutCalculator(payoutTable);

        // Generate initial grid
        Symbol[][] grid = gridGenerator.generateGrid();

        avalancheProcessor.printGrid(grid);

        double totalWin = 0.0;
        int round = 1;

        while (true) {
            System.out.println("\n--- Round " + round + " ---");

            List<Set<int[]>> clusters = clusterFinder.findClusters(grid);
            System.out.println("Clusters found: " + clusters.size());

            if (clusters.isEmpty()) {
                System.out.println("No more clusters found. Stopping.");
                break;
            }

            double roundWin = payoutCalculator.calculateTotalWin(clusters, grid, betAmount);
            System.out.println("Round win: " + roundWin);
            totalWin += roundWin;

            avalancheProcessor.removeClusters(grid, clusters);
            avalancheProcessor.printGridWithRemoved(grid);

            avalancheProcessor.applyAvalanche(grid);
            avalancheProcessor.printGrid(grid);

            round++;
        }

        System.out.println("\nTotal winnings after all avalanches: " + totalWin + " EUR");

        double finalWin = Gamble.gambleWin(totalWin);

        System.out.println("Final payout: " + finalWin + " EUR");

        // Run RTP simulation (can comment out if not needed)
        RtpSimulator rtpSimulator = new RtpSimulator(gridGenerator, clusterFinder, avalancheProcessor, payoutCalculator, betAmount);
        rtpSimulator.runRtpSimulation(1000);
    }
}
