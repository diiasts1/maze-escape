/**
 * Mission 3. Escape: DFS from cell 0 to the last cell (rows * cols - 1) in the maze of Mission 1.
 * Two neighbouring cells are connected when the wall between them is broken.
 * The neighbours are tried in this order: up, right, down, left (cell - cols, cell + 1, cell + cols, cell - 1).
 * The ready method Graphs.open(w, broken) gives you the open neighbours of every cell, in no particular order:
 * to visit them in the required order, go through the four cells above one by one and take those that are in the list.
 * The program must not overflow the stack on a maze of 1000 by 1000 cells.
 */
public class Escape {

    /**
     * path: the cells of the way out, from 0 to the last cell.
     * visited: how many cells are marked as visited at the moment the exit is marked (the start and the exit are counted).
     * deadEnds: how many cells of the maze have exactly one open neighbour.
     */
    public record Result(int[] path, int visited, int deadEnds) {}

    public static Result escape(Input.Walls w, int[] broken) {
        int rows = w.rows();
        int cols = w.cols();
        int n = rows * cols;

        int[][] open = Graphs.open(w, broken);

        int deadEnds = 0;
        for (int i = 0; i < n; i++) {
            if (open[i].length == 1) {
                deadEnds++;
            }
        }

        boolean[] seen = new boolean[n];
        int[] parent = new int[n];
        int[] nextDirection = new int[n];

        java.util.Arrays.fill(parent, -1);

        int[] stack = new int[n];
        int top = 0;
        stack[0] = 0;

        seen[0] = true;
        int visited = 1;

        int[] dr = {-1, 0, 1, 0};
        int[] dc = {0, 1, 0, -1};

        while (top >= 0) {
            int current = stack[top];

            if (current == n - 1) {
                break;
            }

            boolean moved = false;

            while (nextDirection[current] < 4) {
                int d = nextDirection[current]++;
                int r = current / cols + dr[d];
                int c = current % cols + dc[d];

                if (r < 0 || r >= rows || c < 0 || c >= cols) {
                    continue;
                }

                int next = r * cols + c;

                boolean connected = false;
                for (int neighbor : open[current]) {
                    if (neighbor == next) {
                        connected = true;
                        break;
                    }
                }

                if (connected && !seen[next]) {
                    seen[next] = true;
                    parent[next] = current;
                    visited++;

                    stack[++top] = next;
                    moved = true;
                    break;
                }
            }

            if (!moved) {
                top--;
            }
        }

        if (!seen[n - 1]) {
            return new Result(new int[0], visited, deadEnds);
        }

        int length = 0;
        for (int v = n - 1; v != -1; v = parent[v]) {
            length++;
        }

        int[] path = new int[length];
        int v = n - 1;

        for (int i = length - 1; i >= 0; i--) {
            path[i] = v;
            v = parent[v];
        }

        return new Result(path, visited, deadEnds);
    }
}
