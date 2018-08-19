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
    private Board initialBoard;
    private Stack<SearchNode> solution = new Stack<SearchNode>();
    private List<Board> predecessors = new ArrayList<Board>();

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
        SearchNode snInitial = new SearchNode(0, moves, initial, null);

        SearchNode current = snInitial;
        SearchNode previous = current;

        MinPQ<SearchNode> queue = new MinPQ<SearchNode>(boardComparator);
        solution.push(snInitial);

        findNext(current, previous, queue, Integer.MAX_VALUE);
    } // find a solution to the initial board (using the A* algorithm)

    private void findNext(SearchNode current, SearchNode previous, MinPQ<SearchNode> queue, int shortPath) {
        boolean isSinked = false;

        while (!current.board.isGoal()) {
            moves++;
            for (Board b : current.board.neighbors()) {
                if (b.equals(previous.board) || b.equals(initialBoard))
                {
                    continue;
                }
                int priority = b.manhattan();
                SearchNode sn = new SearchNode(priority, moves, b, current);
                queue.insert(sn);

                int c = maxPriority(shortPath, priority);
                if (c > 0) {
                    shortPath = c;
                }
            }

            previous = current;
            current = queue.delMin();

            isSinked = false;
            if (current.move != moves) {
                shortPath = Integer.MAX_VALUE;
                //if (current.move > moves) {
                    current = queue.delMin();
                //}
                moves = current.move;
                /*
                while (current.move != moves) {
                    moves--;
                    solution.pop();
                    isSinked = true;
                }*/
            }
/*
            if (isSinked) {
                moves--;
                continue;
            }
            if (current.move == 1) {
                solution.push(current);
                continue;
            }

            boolean isNeighbourFound = false;
            //Iterable<Board> neighbours = solution.peek().board.neighbors();
            Board prevBoard = solution.peek().board;

            while (current.parent != null && !isNeighbourFound && current.move > 1 && queue.size() > 0) {
                if (current.parent.equals(prevBoard)) {
                    solution.push(current);
                    isNeighbourFound = true;
                } else {
                    current = queue.delMin();
                }
            }

            /*
            //check neighbours
            while (!isNeighbourFound && current.move > 1 && queue.size() > 0) {
                for (Board b : neighbours) {
                    if (b.equals(current.board)) {
                        isNeighbourFound = true;
                        break;
                    }
                }

                if (isNeighbourFound) {
                    solution.push(current);
                }
                else {
                    current = queue.delMin();
                }
            }

            if (current.move == 1) {
                while (solution.size() != 1) {
                    solution.pop();
                }

                while (queue.size() > 0 && queue.min().move != 1) {
                   queue.delMin();
                }

                moves = 1;
                solution.push(current);
            }*/
        }

        while (current.parent != null) {
            solution.push(current);
            current = current.parent;
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
        //Collections.reverse(list);

        //return list;
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