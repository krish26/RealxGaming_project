import java.util.*;

public class AvalancheProcess {

    private final int ROWS;
    private final int COLS;
    private final List<Symbol> weightedSymbols;

    public AvalancheProcess(int rows, int cols, List<Symbol> weightedSymbols) {
        this.ROWS = rows;
        this.COLS = cols;
        this.weightedSymbols = weightedSymbols;
    }

    public void removeClusters(Symbol[][] grid, List<Set<int[]>> clusters) {
        for (Set<int[]> cluster : clusters) {
            for (int[] cell : cluster) {
                int r = cell[0];
                int c = cell[1];
                grid[r][c] = null;
            }
        }
    }

    public void applyAvalanche(Symbol[][] grid) {
        Random random = new Random();

        for (int col = 0; col < COLS; col++) {
            int writeRow = ROWS - 1;

            for (int row = ROWS - 1; row >= 0; row--) {
                if (grid[row][col] != null) {
                    grid[writeRow][col] = grid[row][col];
                    if (writeRow != row) grid[row][col] = null;
                    writeRow--;
                }
            }

            while (writeRow >= 0) {
                grid[writeRow][col] = weightedSymbols.get(random.nextInt(weightedSymbols.size()));
                writeRow--;
            }
        }
    }

    public void printGridWithRemoved(Symbol[][] grid) {
        System.out.println("\nGrid after removing clusters:");
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (grid[row][col] == null) {
                    System.out.print("-\t");
                } else {
                    System.out.print(grid[row][col] + "\t");
                }
            }
            System.out.println();
        }
    }

    public void printGrid(Symbol[][] grid) {
        System.out.println("\nCurrent Grid:");
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                System.out.print(grid[row][col] + "\t");
            }
            System.out.println();
        }
    }
}
