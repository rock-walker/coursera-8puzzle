import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.princeton.cs.algs4.StdRandom;

public class Board {
    
    private final int[][] blocks;
    private Board twinBoard;
    private int zeroX = 0;
    private int zeroY = 0;

    public Board(int[][] blocks) {
        this.blocks = blocks;
        this.twinBoard = null;
    }
                                           
    public int dimension() {
        return blocks[0].length;
    }

    public int hamming() {
        int dimension = dimension();
        int currentPlace = 1;
        int wrongPositions = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (blocks[i][j] != currentPlace++) {
                    wrongPositions++;
                }
            }
        }
        
        return wrongPositions - 1; // exclude last zero
    }
    
    public int manhattan() {
        int dimension = blocks[0].length;
        int currentPlace = 1;
        int wrongPositions = 0;
        
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int cur = blocks[i][j];
                if (cur != currentPlace && cur != 0) {
                    int curY = cur / dimension;
                    int modder = cur % dimension;
                    int curX = 0;
                    
                    if (modder == 0) {
                        curX = curY;
                        curY--;
                    }
                    
                    if (cur > dimension && curX == 0) {
                        curX = modder - 1;
                    }                
                    
                    else if (cur == dimension) {
                        curX = dimension - 1;
                    }
                    
                    else if (cur < dimension) {
                        curX = dimension % cur;
                    }
                    
                    int deltaY = Math.abs(i - curY);
                    int deltaX = Math.abs(j - curX);
                    
                    wrongPositions += deltaY + deltaX;
                }
                currentPlace++;
            }
        }
        
        return wrongPositions;
    } // sum of Manhattan distances between blocks and goal
    
    public boolean isGoal() {
        int dimension = blocks[0].length;
        int currentPlace = 1;
        for (int i = 0; i < dimension; i++) {
            int lastRow = dimension;
            if (i == dimension - 1) {
                lastRow = i;
            }
            for (int j = 0; j < lastRow; j++) {
                if (blocks[i][j] != currentPlace++) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public Board twin() {
        if (twinBoard != null) {
            return twinBoard;
        }

        int x;
        int y;
        int[][] twinBlocks = cloneArray(blocks);
        boolean isSwapped = false;
        do {
                x = StdRandom.uniform(twinBlocks.length);
                y = StdRandom.uniform(twinBlocks.length);
                if (x == y && twinBlocks.length == 2 && twinBlocks[x][x] != 0) {
                    y = (x == 1) ? 0 : 1;
                    if (twinBlocks[y][y] != 0) {
                        int leftVal = twinBlocks[x][x];
                        twinBlocks[x][x] = twinBlocks[y][y];
                        twinBlocks[y][y] = leftVal;
                        isSwapped = true;
                        break;
                    }
                }
        } while (x == y || twinBlocks[x][y] == 0 || twinBlocks[y][x] == 0);

        if (!isSwapped) {
            commonSwap(twinBlocks, x, y);
        }

        twinBoard = new Board(twinBlocks);
        return twinBoard;
    } // a board that is obtained by exchanging any pair of blocks
    
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }

        if (y.getClass() == this.getClass()) {
            Board b = (Board) y;
            return Arrays.deepEquals(blocks, b.blocks);
        }
        else {
            return false;
        }
    }
    
    public Iterable<Board> neighbors() {
        findZero();
        List<Board> neighbors = new ArrayList<Board>();
        int dimension = dimension();
        
        if (zeroX - 1 > -1) {
            int[][] top = cloneArray(blocks);
            swap(top, zeroX - 1, zeroY);
            neighbors.add(new Board(top));
        }
        
        if (zeroX + 1 != dimension) {
            int[][] bottom = cloneArray(blocks);
            swap(bottom, zeroX + 1, zeroY);
            neighbors.add(new Board(bottom));
        }
        
        if (zeroY - 1 > -1) {
            int[][] left = cloneArray(blocks);
            swap(left, zeroX, zeroY - 1);
            neighbors.add(new Board(left));
        }
        
        if (zeroY + 1 != dimension) {
            int[][] right = cloneArray(blocks);
            swap(right, zeroX, zeroY + 1);
            neighbors.add(new Board(right));
        }
        
        return neighbors;
    } // all neighboring boards
    
    private int[][] cloneArray(int[][] source) {
        int[][] result = source.clone();
        for (int i = 0; i < result.length; i++) {
            result[i] = result[i].clone();
        }
        
        return result;
    }

    private void commonSwap(int[][] sourceBlocks, int x, int y) {
        int leftVal = sourceBlocks[x][y];
        sourceBlocks[x][y] = sourceBlocks[y][x];
        sourceBlocks[y][x] = leftVal;
    }
    
    private void swap(int[][] sourceBlocks, int x, int y) {
        int leftVal = sourceBlocks[x][y];
        sourceBlocks[x][y] = 0;
        sourceBlocks[zeroX][zeroY] = leftVal;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(blocks.length + "\n");

        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                sb.append(String.format("%2d ", blocks[i][j]));
            }
            sb.append("\n");
        }
        
        return sb.toString();
    } // string representation of this board (in the output format specified below)

    private void findZero() {
        int dimension = dimension();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (blocks[i][j] == 0) {
                    zeroX = i;
                    zeroY = j;
                    break;
                }
            }
        }
    }
    
    public static void main(String[] args) {
        
    }
}