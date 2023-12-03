package AI;
import model.Node;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class Astar {
    public Node start;
    public Node goal;
  

    public Astar(Node start, Node goal) {
        this.start = start;
        this.goal = goal;
        
    }


    public Node search() {
        PriorityQueue<Node> open = new PriorityQueue<Node>(1, this.goal);
        HashMap<Node, Integer> set = new HashMap<>();
        HashSet<Node> closed = new HashSet<>();

        this.start.setFn(heuristic(start));
        open.add(this.start);
        set.put(this.start, this.start.getFn());

        

        while (!open.isEmpty()) {
            Node current = open.poll();

            if (current.equals(this.goal) && current.sum >= current.getGoalValue()){
                path(current);
                return current;
            }
                


            closed.add(current);
            List<Node> children = current.successor();

            for (Node child : children) {

                if (closed.contains(child)){
                    continue;
                }

                child.setGn(child.parent.getGn() + child.pathCost());
                child.setHn(heuristic(child));
                child.setFn(child.getGn() + child.getHn());
                

                if (set.containsKey(child) && set.get(child) < child.getFn()){
                    continue;
                }
                    

                open.add(child);
                set.put(child, child.getFn());
            }
        }
        return null;

    }

    public void path(Node node) {
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
            printPath(result);
        }
        else {
            System.out.println("NO SOLUTION FOUND!");
        }
    }
    
    
    public void printPath(ArrayList<Node> result) {
        int pathCost = 0;
        for(int i = result.size()-1 ; i>=0 ; i--) {
            if(!result.get(i).equals(this.start)){
                pathCost += result.get(i).pathCost();
            }
            
            System.out.println(result.get(i));    
        }
        System.out.println("path cost : " + pathCost); 
    }

    


    public int heuristic(Node node) {
        int goalI = this.goal.currentCell.i;
        int goalj = this.goal.currentCell.j;
        int nodeI = node.currentCell.i;
        int nodeJ = node.currentCell.j;
        return (Math.abs(goalI-nodeI) + Math.abs(goalj-nodeJ));
    }


  
  
}



