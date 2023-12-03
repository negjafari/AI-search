package AI;
import model.Node;
import model.Cell;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;


import java.util.ArrayList;
import java.util.*;


class Interaction {
    public Node node;
    public boolean isVisited;

    public Interaction(Node node) {
        this.node = node;
        this.isVisited = false;
    }

    public void isVisited() {
        this.isVisited = true;
    }

    public String toString() {
        return this.node.toString();
    }
}

class BDS_helper{
    public Queue<Node> frontier;
    public Hashtable<String, Boolean> inFrontier;
    public Hashtable<String, Boolean> explored;
    public List<Interaction> visited;



    public BDS_helper(){
        this.frontier = new LinkedList<Node>();
        this.inFrontier = new Hashtable<>();
        this.explored = new Hashtable<>();
        this.visited = new ArrayList<>();
 

    }


    public void initialization(Node node){
        this.frontier.add(node);
        this.inFrontier.put(node.hash(), true);
    }


}

public class BDS {
    Node start;
    Node goal;
    BDS_helper forward;
    BDS_helper backward;

    public BDS(Node start, Node goal){
        this.start = start;
        this.goal = goal;
        this.forward = new BDS_helper();
        this.backward = new BDS_helper();
    }


    public void search(){

        forward.initialization(this.start);
        backward.initialization(this.goal);
        boolean interaction = false;

        while (!forward.frontier.isEmpty() && !backward.frontier.isEmpty() && !interaction) {
            bfs(start, forward);
            bfs(goal, backward);
            interaction = intersect(this.goal, this.start);
        }



    }



    public void bfs(Node startNode, BDS_helper helper){


        Node temp = helper.frontier.poll();


        helper.inFrontier.remove(temp.hash());
        helper.visited.add(new Interaction(temp));
        helper.explored.put(temp.hash(), true);

        ArrayList<Node> children = temp.successor();

        for (Node child : children) {
            if (!(helper.inFrontier.containsKey(child.hash())) && !(helper.explored.containsKey(child.hash()))) {
                helper.frontier.add(child);
                helper.inFrontier.put(child.hash(), true);
            }
        }
        

    }




    public boolean intersect(Node goal, Node start){


        for(Interaction for_current : this.forward.visited){
            for(Interaction back_current : this.backward.visited) {

                if ((!for_current.isVisited && !back_current.isVisited) && for_current.node.equals(back_current.node)) {

                    
                    int forward_sum = for_current.node.sum;
                    
                    if (back_current.node.equals(goal)){
                        if(forward_sum >= back_current.node.getGoalValue() && duplication(for_current.node,  back_current.node)) {
                            path(for_current.node, back_current.node, forward_sum);
                            return true;
                        }
                    }
                    else {
                        int sum = calculateBackwardSum(back_current.node.parent, forward_sum);

                        if (sum >= for_current.node.getGoalValue() && duplication(for_current.node, back_current.node)) {
                            path(for_current.node, back_current.node, sum);
                            return true;
                        }
                        else {
                            for_current.isVisited();
                            back_current.isVisited();

                        }
                    }

                }
            }
        }


        return false;
      
    }


    public boolean duplication(Node forward, Node backward) {

        List<Node> forward_path = new ArrayList<>();
        List<Node> backward_path = new ArrayList<>();

        while (true) {
            forward_path.add(forward);
            if (forward.parent == null) {
                break;
            } else {
                forward = forward.parent;
            }
        }

        if(backward.parent != null){

            backward = backward.parent;
            while (true) {
                backward_path.add(backward);
                if (backward.parent == null) {
                    break;
                } else {
                    backward = backward.parent;
                }
            }
        }

    

        for(int i=0 ; i<forward_path.size() ; i++){
            for(int j=0 ; j<backward_path.size() ; j++) {
                if(forward_path.get(i).equals(backward_path.get(j))){
                    return false;
                }
            }
        }

        return true;

        
        // Set<Node> set = new HashSet<Node>(forward_path);

        // if(set.size() < forward_path.size()){
        //     return false;
        // }
        // else {
        //     return true;
        // }
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

    public int calculateBackwardSum(Node interact, int sum) {
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

    public void path(Node start, Node goal, int sum) {

        ArrayList<Node> forward_path = new ArrayList<>();

        while (true) {
            forward_path.add(start);
            if (start.parent == null) {
                break;
            } else {
                start = start.parent;
            }
        }



        for (int i=forward_path.size()-1 ; i>=0 ; i--) {
            if (i==0) {
                System.out.println(forward_path.get(i) + " (interaction)");
            }
            else {
                System.out.println(forward_path.get(i));
            }
        }

        goal = goal.parent;
        while (true) {
            System.out.println(goal);
            if (goal.parent == null) {
                break;
            } else {
                goal = goal.parent;
            }
        }

        System.out.println("sum : " + sum);


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





}
