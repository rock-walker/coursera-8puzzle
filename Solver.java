import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;

import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;


public class Solver {

    private boolean isSolvable = true;
    private int moves = 0;
    public Board initialBoard;
    private Stack<SearchNode> solution = new Stack<SearchNode>();

    private Comparator<SearchNode> boardComparator = new Comparator<SearchNode>() {

        public int compare(SearchNode b1, SearchNode b2) {
            int priority1 = b1.priority + b1.move;
            int priority2 = b2.priority + b2.move;
            return (priority1 < priority2) ? -1 : 1;
        }
    };

    private class SearchNode {
        SearchNode(int priority, int move, Board board) {
            this.priority = priority;
            this.move = move;
            this.board = board;
        }
        public int priority;
        public int move;
        public Board board;
    }

    public Solver(Board initial) {
        if (initial == null){
            throw new java.lang.IllegalArgumentException();
        }
        initialBoard = initial;
        SearchNode snInitial = new SearchNode(0, moves, initial);

        SearchNode current = snInitial;
        SearchNode previous = current;

        int shortPath;
        int prevShortPath = Integer.MAX_VALUE;

        MinPQ<SearchNode> queue = new MinPQ<SearchNode>(boardComparator);
        //queue.insert(snInitial);
        solution.push(snInitial);

        while (!current.board.isGoal()){
            current = findNext(current, previous, queue, Integer.MAX_VALUE);
        }
/*
        while (!current.board.isGoal()) {
            moves++;
            // SearchNode minPriorSn = queue.delMin();
            // solution.push(minPriorSn);

            shortPath = Integer.MAX_VALUE;

            for (Board b : current.board.neighbors()) {
                if (b.equals(previous.board) || b.equals(initial)) {
                    continue;
                }
                int priority = b.manhattan();
                SearchNode sn = new SearchNode(priority, moves, b);
                queue.insert(sn);

                int c = maxPriority(shortPath, priority);
                if (c > 0) {
                    shortPath = c;
                }
            }

            previous = current;
            current = queue.delMin();

            if (current.move != moves) {
                shortPath = Integer.MAX_VALUE;
            }


            if (prevShortPath < shortPath) {

                while (!queue.isEmpty() && queue.min().move != 1) {
                   queue.delMin();
                }

                current = queue.delMin();
                moves = 1;

                while (!queue.isEmpty() && queue.min().move != 1) {
                    queue.delMin();
                }

                while (solution.size() != moves) {
                    solution.pop();
                }

                prevShortPath = Integer.MAX_VALUE; // current.move + current.priority;
                StdOut.println("wrong path");
            } else {
                prevShortPath = shortPath;
                StdOut.println(current.board);
            }

            solution.push(current);
        }
*/
    } // find a solution to the initial board (using the A* algorithm)

    private SearchNode findNext(SearchNode current, SearchNode previous, MinPQ<SearchNode> queue, int shortPath) {
        moves++;
        for (Board b : current.board.neighbors()) {
            if (b.equals(previous.board) || b.equals(initialBoard)) {
                continue;
            }
            int priority = b.manhattan();
            SearchNode sn = new SearchNode(priority, moves, b);
            queue.insert(sn);

            int c = maxPriority(shortPath, priority);
            if (c > 0) {
                shortPath = c;
            }
        }

        previous = current;
        current = queue.delMin();

        if (current.move != moves) {
            shortPath = Integer.MAX_VALUE;
            solution.pop();
            moves--;
        }

        solution.push(current);

        if (!current.board.isGoal()) {
            return findNext(current, previous, queue, shortPath);
        }
        else {
            return current;
        }
    }

    private int maxPriority(int currMax, int priority) {
        int prior = priority + moves;
        if (prior <= currMax) {
            return prior;
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
        List<Board> list = new ArrayList<Board>();
        while(!solution.isEmpty()) {
            list.add(solution.pop().board);
        }
        // List<Board> shallowCopy = list.subList(0, list.size());
        Collections.reverse(list);

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