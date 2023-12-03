package AI;
import java.util.ArrayList;

import model.Node;
import java.util.List;



public class IDS {
    public int depth;

    public IDS(int depth) {
        this.depth = depth;
    }




    public void search(Node start, Node goal) {

        for (int i = 0; i < this.depth ; i++) {
            boolean result = DLS(start, goal, this.depth);
            if (result) {
                return;
            }
        }
    }

    public boolean DLS(Node node, Node goal, int limit) {
        if (node.equals(goal) && node.sum >= node.getGoalValue()) {
            path(node);
            return true;
        }

        if (limit < 1) {
            return false;
        }

        List<Node> childern = node.successor();
        for (Node child : childern) {
            if (DLS(child,goal, limit - 1)) {
                return true;
            }
        }

        return false;

    }    


    public void path(Node node) {
        ArrayList<Node> result = new ArrayList<>();
        while (true) {
            result.add(node);
            if (node.parent == null) {
                break;
            } else {
                node = node.parent;
            }
        }
        printPath(result);
    }

    public void printPath(ArrayList<Node> result) {
        for(int i = result.size()-1 ; i>=0 ; i--) {
            System.out.println(result.get(i));    
        }
        System.out.println("sum : " + result.get(0).sum);
    }

}

