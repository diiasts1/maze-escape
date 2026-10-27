/**
 * Missions 1 and 2. Building the maze out of the walls.
 * A wall k joins the cells w.a()[k] and w.b()[k] and costs w.cost()[k]. Indexes start from 0,
 * the line number in walls.txt is the index + 1. The cells are connected by DisjointSet.
 */
public class Maze {

    /**
     * Mission 1. Take the walls in the order of the file. Break a wall when its two cells are in different sets.
     * Returns the indexes of the broken walls, in the order of breaking.
     */
    public static int[] breakWalls(Input.Walls w) {
        int n = w.rows() * w.cols();
        DisjointSet dsu = new DisjointSet(n);

        int[] broken = new int[n - 1];
        int count = 0;

        for (int k = 0; k < w.a().length; k++) {
            int a = w.a()[k];
            int b = w.b()[k];

            if (dsu.union(a, b)) {
                broken[count] = k;
                count++;
            }
        }

        return java.util.Arrays.copyOf(broken, count);
    }

    /**
     * Mission 2. Kruskal: the same, but take the walls from the cheapest to the most expensive.
     * Returns the indexes of the broken walls.
     */
    public static int[] cheapestMaze(Input.Walls w) {
        int n = w.rows() * w.cols();
        int m = w.a().length;

        Integer[] order = new Integer[m];

        for (int i = 0; i < m; i++) {
            order[i] = i;
        }

        java.util.Arrays.sort(order,
                (x, y) -> Integer.compare(w.cost()[x], w.cost()[y]));

        DisjointSet dsu = new DisjointSet(n);

        int[] broken = new int[n - 1];
        int count = 0;

        for (int k : order) {
            if (dsu.union(w.a()[k], w.b()[k])) {
                broken[count++] = k;

                if (count == n - 1) {
                    break;
                }
            }
        }

        return java.util.Arrays.copyOf(broken, count);
    }

    /**
     * Mission 2. Prim with a priority queue. Returns the total cost of the cheapest maze.
     * It must be equal to the total cost of cheapestMaze.
     */
    public static long primCost(Input.Walls w) {
        int n = w.rows() * w.cols();
        int m = w.a().length;

        java.util.ArrayList<Integer>[] adj =
                new java.util.ArrayList[n];

        for (int i = 0; i < n; i++) {
            adj[i] = new java.util.ArrayList<>();
        }

        for (int k = 0; k < m; k++) {
            adj[w.a()[k]].add(k);
            adj[w.b()[k]].add(k);
        }

        boolean[] visited = new boolean[n];

        java.util.PriorityQueue<Integer> pq =
                new java.util.PriorityQueue<>(
                        (x, y) -> Integer.compare(w.cost()[x], w.cost()[y])
                );

        visited[0] = true;

        for (int k : adj[0]) {
            pq.add(k);
        }

        long total = 0;
        int count = 1;

        while (!pq.isEmpty() && count < n) {
            int k = pq.poll();

            int a = w.a()[k];
            int b = w.b()[k];

            if (visited[a] && visited[b]) {
                continue;
            }

            int next = visited[a] ? b : a;

            visited[next] = true;
            total += w.cost()[k];
            count++;

            for (int edge : adj[next]) {
                pq.add(edge);
            }
        }

        return total;
    }
}
