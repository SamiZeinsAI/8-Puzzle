import edu.princeton.cs.algs4.MinPQ;
import java.util.ArrayList;
import java.util.Comparator;

public class Solver {
    private class SearchNodeComparator implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode node1, SearchNode node2) {
            return Integer.compare(node1.priority, node2.priority);
        }
    }

    private class SearchNode {
        Board board;
        SearchNode prev;
        int moves;
        int priority;

        public SearchNode(Board board, SearchNode prev, int moves) {
            this.board = board;
            this.prev = prev;
            this.moves = moves;
            priority = moves + board.manhattan();

        }
    }

    private ArrayList<Board> solution = new ArrayList<>();
    private int moves = -1;
    private boolean isSolvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        MinPQ<SearchNode> pq = new MinPQ<>(new SearchNodeComparator());
        MinPQ<SearchNode> pqTwin = new MinPQ<>(new SearchNodeComparator());

        SearchNode min = new SearchNode(initial, null, 0);
        SearchNode twin = new SearchNode(initial.twin(), null, 0);
        pq.insert(min);
        pqTwin.insert(twin);
        while (!pq.isEmpty()) {
            min = pq.delMin();
            if (min.board.isGoal()) {
                isSolvable = true;
                break;
            }
            Iterable<Board> boards = min.board.neighbors();
            for (Board board : boards) {
                if (min.prev == null || !board.equals(min.prev.board)) {
                    SearchNode neighbor = new SearchNode(board, min, min.moves + 1);
                    pq.insert(neighbor);
                }
            }
            min = pqTwin.delMin();
            if (min.board.isGoal()) {
                isSolvable = false;
                return;
            }
            boards = min.board.neighbors();
            for (Board board : boards) {
                if (min.prev == null || !board.equals(min.prev.board)) {
                    SearchNode neighbor = new SearchNode(board, min, min.moves + 1);
                    pqTwin.insert(neighbor);
                }
            }
        }
        moves = min.moves;
        while (min != null) {
            solution.add(min.board);
            min = min.prev;
        }
        for (int i = 0; i < (moves + 1) / 2; i++) {
            Board temp = solution.get(i);
            solution.set(i, solution.get(moves - i));
            solution.set(moves - i, temp);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        return solution;
    }

    // test client (see below)
    public static void main(String[] args) {
        return;
    }

}