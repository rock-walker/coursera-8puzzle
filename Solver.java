import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Solver {
    
    private boolean isSolvable = true;
    private int moves = 0;
    private List<Board> solution = new ArrayList<Board>();

    private Comparator<Board> boardComparator = new Comparator<Board>() {

        public int compare(Board b1, Board b2) {
            int priority1 = b1.manhattan();
            int priority2 = b2.manhattan();
            if (priority1 == priority2) {
                return 0;
            }
            else if (priority1 < priority2) {
                return -1;
            }

            return 1;
        }
    };

    public Solver(Board initial) {
        if (initial == null){
            throw new java.lang.IllegalArgumentException();
        }
        Board current = initial;
        Board previous = null;
        int shortPath;

        MinPQ<Board> queue = new MinPQ<Board>(boardComparator);
        queue.insert(initial);
        Board newCurrentBoard = null;
        
        while (!current.isGoal() && moves < 11) {
            moves++;
            solution.add(queue.delMin());
            shortPath = Integer.MAX_VALUE;
            newCurrentBoard = null;
            
            for (Board b : current.neighbors()) {
                if (b.equals(previous)) {
                    continue;
                }
                
                queue.insert(b);
                int c = maxPriority(shortPath, b);
                if (c > 0) {
                    shortPath = c;
                    newCurrentBoard = b;
                }
            }
            previous = current;
            current = newCurrentBoard;
            StdOut.println(current);
        }
        
        if (current.isGoal()) {
            solution.add(newCurrentBoard);
        }
    } // find a solution to the initial board (using the A* algorithm)
    
    private int maxPriority(int currMax, Board b) {
        int prior = b.manhattan() + moves;
        if (prior <= currMax) {
            currMax = prior;
            return currMax;
        }
        return 0;
    }
    
    public boolean isSolvable() {
        return isSolvable;
    } // is the initial board solvable?
    
    public int moves() {
        return moves;
    } // min number of moves to solve initial board; -1 if unsolvable
    
    public Iterable<Board> solution() {
        return solution;
    } // sequence of boards in a shortest solution; null if unsolvable
    
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}