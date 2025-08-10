import java.util.List;
import java.util.Set;

public class RtpSimulator {

    private final int maxAvalancheRounds = 100;
    private final GridGenerator gridGenerator;
    private final ClusterFinder clusterFinder;
    private final AvalancheProcess avalancheProcessor;
    private final PayoutCalculator payoutCalculator;
    private final double betAmount;

    public RtpSimulator(GridGenerator gridGenerator, ClusterFinder clusterFinder,
                        AvalancheProcess avalancheProcessor, PayoutCalculator payoutCalculator,
                        double betAmount) {
        this.gridGenerator = gridGenerator;
        this.clusterFinder = clusterFinder;
        this.avalancheProcessor = avalancheProcessor;
        this.payoutCalculator = payoutCalculator;
        this.betAmount = betAmount;
    }

    public void runRtpSimulation(int spins) {
        double totalWins = 0.0;
        double totalBets = spins * betAmount;

        for (int i = 0; i < spins; i++) {  //spin loop
            Symbol[][] grid = gridGenerator.generateGrid();
            double spinWin = 0.0;

            int roundCount = 0;

            while (true) {   //avalanche loop
                if (++roundCount > maxAvalancheRounds) break;

                List<Set<int[]>> clusters = clusterFinder.findClusters(grid);
                if (clusters.isEmpty()) break;

                double roundWin = payoutCalculator.calculateTotalWin(clusters, grid, betAmount);
                spinWin += roundWin;

                avalancheProcessor.removeClusters(grid, clusters);
                avalancheProcessor.applyAvalanche(grid);
            }

            totalWins += spinWin;
        }

        double rtp = (totalWins / totalBets) * 100;
        System.out.printf("After %d spins, Total Bets: %.2f EUR, Total Wins: %.2f EUR%n", spins, totalBets, totalWins);
        System.out.printf("Estimated RTP = %.2f%%\n", rtp);
    }
}
