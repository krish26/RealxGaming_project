import java.util.*;

public class GridGenerator {

    private static final int ROWS = 8;
    private static final int COLS = 8;

    private final List<Symbol> weightedSymbols;

    public GridGenerator(Map<Symbol, Integer> symbolWeights) {
        weightedSymbols = new ArrayList<>();
        for (Symbol s : Symbol.values()) {
            int weight = symbolWeights.getOrDefault(s, 100);
            for (int i = 0; i < weight; i++) {
                weightedSymbols.add(s);
            }
        }
    }

    public Symbol[][] generateGrid() {
        Symbol[][] grid = new Symbol[ROWS][COLS];
        Random random = new Random();

        for (int col = 0; col < COLS; col++) {
            for (int row = 0; row < ROWS; row++) {
                grid[row][col] = weightedSymbols.get(random.nextInt(weightedSymbols.size()));
            }
        }

        return grid;
    }

    public static int getRows() {
        return ROWS;
    }

    public static int getCols() {
        return COLS;
    }

    public List<Symbol> getWeightedSymbols() {
        return Collections.unmodifiableList(weightedSymbols);
    }

}
