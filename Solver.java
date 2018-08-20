import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;

import java.util.List;
import java.util.Comparator;
import java.util.ArrayList;


public class Solver {

    private boolean isSolvable = true;
    private int moves = 0;
    private Board initialBoard;
    private Board twinInitialBoard;
    private Stack<SearchNode> solution = new Stack<SearchNode>();

    private Comparator<SearchNode> boardComparator = new Comparator<SearchNode>() {

        public int compare(SearchNode b1, SearchNode b2) {
            int priority1 = b1.priority + b1.move;
            int priority2 = b2.priority + b2.move;
            return (priority1 < priority2) ? -1 : 1;
        }
    };

    private class SearchNode {
        SearchNode(int priority, int move, Board board, SearchNode parent) {
            this.priority = priority;
            this.move = move;
            this.board = board;
            this.parent = parent;
        }
        public int priority;
        public int move;
        public Board board;
        public SearchNode parent;
    }

    public Solver(Board initial) {
        if (initial == null){
            throw new java.lang.IllegalArgumentException();
        }

        initialBoard = initial;
        SearchNode current = new SearchNode(0, moves, initial, null);
        MinPQ<SearchNode> queue = new MinPQ<SearchNode>(boardComparator);

        twinInitialBoard = initial.twin();
        SearchNode twinCurrent = new SearchNode(0, moves, initial, null);
        MinPQ<SearchNode> twinQueue = new MinPQ<SearchNode>(boardComparator);

        while (!current.board.isGoal() && !twinCurrent.board.isGoal()) {
            current = findNext(current, queue);
            twinCurrent = findNext(twinCurrent, twinQueue);
        }

        isSolvable = current.board.isGoal();

        if (isSolvable()) {
            moves = current.move;
            while (current.parent != null) {
                solution.push(current);
                current = current.parent;
            }

            solution.push(current);
        }

    } // find a solution to the initial board (using the A* algorithm)

    private SearchNode findNext(SearchNode current, MinPQ<SearchNode> queue) {
        for (Board b : current.board.neighbors()) {
            if ((current.parent != null && b.equals(current.parent.board)) || b.equals(initialBoard))
            {
                continue;
            }
            int priority = b.manhattan();
            SearchNode sn = new SearchNode(priority, current.move + 1, b, current);
            queue.insert(sn);
        }
        return queue.delMin();
    }

    public boolean isSolvable() {
        return isSolvable;
    } // is the initial board solvable?
    
    public int moves() {
        return moves;
    } // min number of moves to solve initial board; -1 if unsolvable
    
    public Iterable<Board> solution() {
        List<Board> list = new ArrayList<Board>();
        while(!solution.isEmpty()) {
            list.add(solution.pop().board);
        }

        return list;
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