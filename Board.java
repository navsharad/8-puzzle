import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.StdOut;

// Models a board in the 8-puzzle game or its generalization.
public class Board {
    private int N;
    private int[][] tiles;
    private int hamming;
    private int manhattan;
    private boolean done = false; //used in manhattan()

    // Construct a board from an N-by-N array of tiles, where
    // tiles[i][j] = tile at row i and column j, and 0 represents the blank
    // square.
    public Board(int[][] tiles) {
        N = tiles.length;
        this.tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    // Tile at row i and column j.
    public int tileAt(int i, int j) { //done
        return tiles[i][j];
    }

    // Size of this board.
    public int size() { //done
        return N;
    }

    // Number of tiles out of place.
    public int hamming() { //maybe done
        hamming = 0;
        int correctPosition = 1;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tileAt(i, j) != 0 && tileAt(i, j) != correctPosition) {
                    hamming++;
                }
                correctPosition++;
            }
        }
        return hamming;
    }

    // Sum of Manhattan distances between tiles and goal.
    public int manhattan() { //maybe done
        if (done)
            return manhattan;
        else {
            int correctPosition = 1;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (tileAt(i, j) != 0 && tileAt(i, j) != correctPosition) {
                        manhattan += Math.abs(i - ((tileAt(i, j) - 1) / N)) + Math.abs(j - ((tileAt(i, j) - 1) - ((tileAt(i, j) - 1) / N) * N));
                    }
                    correctPosition++;
                }
            }
            done = true;
            return manhattan;
        }
    }

    // Is this board the goal board?
    public boolean isGoal() { //done
        return hamming() == 0;
    }

    // Is this board solvable?
    public boolean isSolvable() { // double check
        if (N % 2 == 0) {
            int n = ((blankPos() - 1) / N) + inversions();
            if (n % 2 == 0) {
                return false;
            }
            return true;
        } else {
            if (inversions() % 2 == 0) {
                return true;
            }
            return false;
        }
    }

    // Does this board equal that?
    public boolean equals(Board that) { //probably done
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (this.tileAt(i, j) != that.tileAt(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    // All neighboring boards.
    public Iterable<Board> neighbors() { // prob done
        LinkedQueue<Board> q = new LinkedQueue<>();
        int[][] neighbor;
        int blank = blankPos();
        int blankRow = blank / N;
        int blankCol = blank % N;
        int row;
        int col;
        int sum;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                row = i - blankRow;
                col = j - blankCol;
                sum = Math.abs(col) + Math.abs(row);

                if (sum == 1) {
                    neighbor = cloneTiles();
                    neighbor[blankRow][blankCol] = neighbor[blankRow + row][blankCol + col];
                    neighbor[blankRow + row][blankCol + col] = 0;
                    q.enqueue(new Board(neighbor));
                }
            }

        }
        return q;
    }

    // String representation of this board.
    public String toString() { //done
        String s = N + "\n";
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s += String.format("%2d", tiles[i][j]);
                if (j < N - 1) {
                    s += " ";
                }
            }
            if (i < N - 1) {
                s += "\n";
            }
        }
        return s;
    }

    // Helper method that returns the position (in row-major order) of the
    // blank (zero) tile.
    private int blankPos() { //prob done
        int position = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] == 0) {
                    return position;
                }
                position++;
            }
        }
        return -1;
    }

    // Helper method that returns the number of inversions.
    private int inversions() { //specified on rubric that only isSolvable can have greater than N^2 time, this method is a helper for that method so that's why it has greater time
        int inversions = 0;
        int num1 = 0;
        int num2 = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                num1++;
                for (int k = 0; k < N; k++) {
                    for (int l = 0; l < N; l++) {
                        num2++;
                        if (tiles[i][j] == 0 || tiles[k][l] == 0) {
                            continue;
                        } else if (num1 < num2 && tiles[i][j] > tiles[k][l]) {
                            inversions++;
                        }
                    }
                }
                num2 = 0;
            }
        }
        return inversions;
    }

    // Helper method that clones the tiles[][] array in this board and
    // returns it.
    private int[][] cloneTiles() { //probably done
        int[][] clonedTiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                clonedTiles[i][j] = tiles[i][j];
            }
        }
        return clonedTiles;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board board = new Board(tiles);
        StdOut.println(board.hamming());
        StdOut.println(board.manhattan());
        StdOut.println(board.isGoal());
        StdOut.println(board.isSolvable());
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
        }
    }
}
