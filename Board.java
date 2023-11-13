import java.util.ArrayList;

public class Board {
    private int n;
    private final int[][] board;
    private int zeroRow;
    private int zeroCol;
    private int priority = -1;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        board = new int[n][];
        for (int i = 0; i < n; i++) {
            board[i] = tiles[i].clone();
        }
    }

    private void findZero() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                    return;
                }
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(n).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result.append(" ").append(board[i][j]);
            }
            result.append("\n");
        }
        return result.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int misplaced = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != i * n + j + 1) {
                    misplaced++;
                }
            }
        }
        return misplaced - 1;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (priority != -1) {
            return priority;
        }
        int displacements = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                int tile = board[row][col] - 1;
                if (tile == -1) {
                    continue;
                }
                int goalRow = tile / n;
                int goalCol = tile % n;
                displacements += Math.abs(goalCol - col) + Math.abs(goalRow - row);
            }
        }
        priority = displacements;
        return displacements;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                int tile = row * n + col + 1;
                if (board[row][col] != tile && (row != n - 1 || col != n - 1)) {
                    return false;
                }
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || getClass() != y.getClass()) {
            return false;
        }
        Board other = (Board) y;
        if (other.board.length != board.length || other.board[0].length != board[0].length) {
            return false;
        }
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (board[row][col] != other.board[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<>();
        findZero();
        manhattan();
        if (zeroCol > 0) {
            Board neighbor = new Board(board);
            exch(neighbor, zeroRow, zeroCol, zeroRow, zeroCol - 1);
            int digit = neighbor.board[zeroRow][zeroCol] - 1;
            neighbor.priority = digit % n >= zeroCol ? priority - 1 : priority + 1;
            neighbor.zeroCol--;
            neighbors.add(neighbor);
        }
        if (zeroCol < n - 1) {
            Board neighbor = new Board(board);
            exch(neighbor, zeroRow, zeroCol, zeroRow, zeroCol + 1);
            int digit = neighbor.board[zeroRow][zeroCol] - 1;
            neighbor.priority = digit % n <= zeroCol ? priority - 1 : priority + 1;
            neighbor.zeroCol++;
            neighbors.add(neighbor);
        }
        if (zeroRow > 0) {
            Board neighbor = new Board(board);
            exch(neighbor, zeroRow, zeroCol, zeroRow - 1, zeroCol);
            int digit = neighbor.board[zeroRow][zeroCol] - 1;
            neighbor.priority = digit / n >= zeroRow ? priority - 1 : priority + 1;
            neighbor.zeroRow--;
            neighbors.add(neighbor);
        }
        if (zeroRow < n - 1) {
            Board neighbor = new Board(board);
            exch(neighbor, zeroRow, zeroCol, zeroRow + 1, zeroCol);
            int digit = neighbor.board[zeroRow][zeroCol] - 1;
            neighbor.priority = digit / n <= zeroRow ? priority - 1 : priority + 1;
            neighbor.zeroRow++;
            neighbors.add(neighbor);
        }
        return neighbors;
    }

    private void exch(Board y, int r1, int c1, int r2, int c2) {
        int temp = y.board[r1][c1];
        y.board[r1][c1] = y.board[r2][c2];
        y.board[r2][c2] = temp;

    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twin = new Board(board);
        if (twin.board[0][0] == 0 || twin.board[0][1] == 0) {
            int temp = twin.board[1][0];
            twin.board[1][0] = twin.board[1][1];
            twin.board[1][1] = temp;
        } else {
            int temp = twin.board[0][0];
            twin.board[0][0] = twin.board[0][1];
            twin.board[0][1] = temp;
        }

        return twin;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        return;
    }

}