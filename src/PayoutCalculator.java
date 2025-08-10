import java.util.*;

public class PayoutCalculator {

    private final Map<Symbol, int[]> payoutTable;

    public PayoutCalculator(Map<Symbol, int[]> payoutTable) {
        this.payoutTable = payoutTable;
    }

    public double calculateTotalWin(List<Set<int[]>> clusters, Symbol[][] grid, double betAmount) {
        double totalWin = 0.0;

        for (Set<int[]> cluster : clusters) {
            Symbol baseSymbol = null;
            for (int[] cell : cluster) {
                Symbol s = grid[cell[0]][cell[1]];
                if (s != null && s != Symbol.WR && s != Symbol.BK) {
                    baseSymbol = s;
                    break;
                }
            }
            if (baseSymbol == null) continue;

            int symbolCount = 0;
            for (int[] cell : cluster) {
                Symbol s = grid[cell[0]][cell[1]];
                if (s != null && s != Symbol.WR && s != Symbol.BK) symbolCount++;
            }

            int payoutIndex = getPayoutIndex(symbolCount);
            int[] payouts = payoutTable.get(baseSymbol);
            if (payouts == null) continue;

            int payoutAmount = payouts[payoutIndex];
            double clusterWin = (payoutAmount / 10.0) * betAmount;
            totalWin += clusterWin;

            System.out.println("Cluster of " + baseSymbol + " with " + symbolCount +
                    " symbols pays: " + clusterWin + " EUR");
        }

        return totalWin;
    }

    private int getPayoutIndex(int clusterSize) {
        if (clusterSize >= 21) return 4;
        else if (clusterSize >= 17) return 3;
        else if (clusterSize >= 13) return 2;
        else if (clusterSize >= 9) return 1;
        else return 0;
    }
}
