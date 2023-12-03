package AI;
import model.Cell;
import model.Node;
import java.util.ArrayList;



class Result {
    public int distance;
    public boolean goal;

    public Result(int distance, boolean goal) {
        this.distance = distance;
        this.goal = goal;
    }

    public String toString() {
        return this.distance + "  " + this.goal;
    }
}



public class IDAS {
    public Node start;
    public Node goal;


    public IDAS(Node start, Node goal) {
        this.start = start;
        this.goal = goal;
    }

    public void search() {
        int cuttoff = heuristic(start);
        boolean found = false;

        while (!found) {
            Result result = iterativeDeepening(start, cuttoff);
            if (result.goal || result.distance == Integer.MIN_VALUE) {
                found = true;
            } else {
                cuttoff = result.distance;
            }
        }

    }

    public Result iterativeDeepening(Node node, int cutoff) {

        node.setHn(heuristic(node));
        node.setGn(getPathCost(node));
        node.setFn(node.getGn() + node.getHn());

        if (node.equals(goal) && node.sum >= node.getGoalValue()) {
            path(node);

            return new Result(node.getGn(), true);
        }

        if (node.getFn() < cutoff) {
            return new Result(node.getFn(), false);
        }

        int max = Integer.MIN_VALUE;

        for (Node successor : node.successor()) {
            Result result = iterativeDeepening(successor, cutoff);

            if (result.goal) {
                return result;
            } else if (result.distance > max) {
                max = result.distance;
            }
        }

        return new Result(max, false);
    }



    public int heuristic(Node node) {
        int goalI = this.goal.currentCell.i;
        int goalj = this.goal.currentCell.j;
        int nodeI = node.currentCell.i;
        int nodeJ = node.currentCell.j;
        return (Math.abs(goalI-nodeI) + Math.abs(goalj-nodeJ));
    }

    public void path(Node node) {
        int sum = node.sum;
        if (node != null) {
            ArrayList<Node> result = new ArrayList<>();
            while (true) {
                result.add(node);
                if (node.parent == null) {
                    break;
                } else {
                    node = node.parent;
                }
            }
            printPath(result, sum);

        }

    }

    public void printPath(ArrayList<Node> result, int sum) {
        int pathCost = 0;
        for(int i = result.size()-1 ; i>=0 ; i--) {
            if(!result.get(i).equals(this.start)){
                pathCost ++;
            }
            
            System.out.println(result.get(i));    
        }
        System.out.println("path cost : " + pathCost); 
        System.out.println("sum : " + sum); 
    }


    public int getPathCost(Node node){
        int count = 0;
        while (true) {
            count++;
            if (node.parent == null) {
                break;
            } else {
                node = node.parent;
            }
        }
        return count;
    }

    public int calculate(Node node, int sum){
        Cell cell = node.currentCell;
        return switch (cell.getOperationType()) {
            case MINUS -> sum - cell.getValue();
            case ADD -> sum + cell.getValue();
            case POW -> (int) Math.pow(sum, cell.getValue());
            case MULT -> sum * cell.getValue();
            default -> sum;
        };
    }

    public int calculateSum(Node interact, int sum) {
        while (true) {
            sum = calculate(interact, sum);
            if (interact.parent == null) {
                break;
            } else {
                interact = interact.parent;
            }
        }
        return sum;
    }

}
