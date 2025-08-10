import java.util.*;

public class ClusterFinder {

    private final int ROWS;
    private final int COLS;
    private final int[][] NEIGHBORS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    private final Map<Symbol, int[]> payoutTable;

    public ClusterFinder(int rows, int cols, Map<Symbol, int[]> payoutTable) {
        this.ROWS = rows;
        this.COLS = cols;
        this.payoutTable = payoutTable;
    }

    public List<Set<int[]>> findClusters(Symbol[][] grid) {
        boolean[][] visited = new boolean[ROWS][COLS];
        List<Set<int[]>> clusters = new ArrayList<>();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (!visited[row][col] && grid[row][col] != Symbol.BK) {
                    Symbol base = grid[row][col];

                    // If wildcard, decide base symbol from first non-wild neighbor (if any)
                    if (base == Symbol.WR) {
                        base = detectClusterBaseSymbol(grid, row, col);
                    }

                    Set<int[]> cluster = bfsCluster(grid, row, col, base, visited);

                    if (cluster.size() >= 5) {
                        addAdjacentBlockers(grid, cluster);
                        clusters.add(cluster);
                    }
                }
            }
        }

        return clusters;
    }

    private Symbol detectClusterBaseSymbol(Symbol[][] grid, int row, int col) {
        for (int[] n : NEIGHBORS) {  //checks for the neighbours which are symbols
            int nr = row + n[0], nc = col + n[1];
            if (nr >= 0 && nr < ROWS && nc >= 0 && nc < COLS) {
                if (grid[nr][nc] != Symbol.BK && grid[nr][nc] != Symbol.WR) {
                    return grid[nr][nc]; //if it's not blocer or wild it returns a symbol
                }
            }
        }
        return Symbol.WR; // if not it returns wild
    }

    private Set<int[]> bfsCluster(Symbol[][] grid, int startRow, int startCol, Symbol target, boolean[][] visited) {
        Set<int[]> cluster = new HashSet<>();
        Queue<int[]> queue = new LinkedList<>();

        queue.add(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int r = cell[0], c = cell[1];
            Symbol current = grid[r][c];

            if (current == target || current == Symbol.WR) {
                cluster.add(cell);

                for (int[] n : NEIGHBORS) {
                    int nr = r + n[0], nc = c + n[1];
                    if (nr >= 0 && nr < ROWS && nc >= 0 && nc < COLS) {
                        if (!visited[nr][nc] && (grid[nr][nc] == target || grid[nr][nc] == Symbol.WR)) {
                            queue.add(new int[]{nr, nc});
                            visited[nr][nc] = true;
                        }
                    }
                }
            }
        }
        return cluster;
    }

    private void addAdjacentBlockers(Symbol[][] grid, Set<int[]> cluster) {
        Set<int[]> blockersToAdd = new HashSet<>();

        for (int[] cell : cluster) {
            int r = cell[0];
            int c = cell[1];

            for (int[] n : NEIGHBORS) {
                int nr = r + n[0];
                int nc = c + n[1];
                if (nr >= 0 && nr < ROWS && nc >= 0 && nc < COLS) {
                    if (grid[nr][nc] == Symbol.BK) {
                        blockersToAdd.add(new int[]{nr, nc});
                    }
                }
            }
        }

        cluster.addAll(blockersToAdd);
    }

}

