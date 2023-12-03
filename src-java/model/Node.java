package model;

import core.Constants;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;
import java.util.UUID;
import java.util.Comparator;


public class Node implements Comparator<Node>{
    public Board board;
    public int sum = 0;
    public Node parent;
    public Cell currentCell;
    public int i;
    public int j; 
    private Cell[][] cells;
    private int goalValue;
    private Hashtable<String, Boolean> repeatedStates;
    public int fValue;
    public int gValue;
    public int hValue;
    public String uid;



    public Node(Cell currentCell, int currentValue, int goalValue, Board board, Node parent, Hashtable<String, Boolean> repeated) {
        this.currentCell = currentCell;
        this.sum = currentValue;
        this.board = board;
        this.cells = board.getCells();
        this.parent = parent;
        this.goalValue = goalValue;
        Hashtable<String, Boolean> hashtableTemp = new Hashtable<String, Boolean>(repeated);
        hashtableTemp.put(this.toString(), true);
        this.repeatedStates = hashtableTemp;
        this.i = currentCell.i;
        this.j = currentCell.j;
        this.uid = UUID.randomUUID().toString();
        setGoalValue();

        repeated.put(this.toString(), true);
    }


    public ArrayList<Node> successor() {
        ArrayList<Node> result = new ArrayList<Node>();
        if (canMoveRight()) {
            Cell rightCell = this.cells[this.currentCell.i][this.currentCell.j + 1];
            if (isValidMove(rightCell)) {
                int calculatedValue = calculate(rightCell);
                Node rightNode = new Node(rightCell, calculatedValue, goalValue, board, this, repeatedStates);
                result.add(rightNode);
            }
        }
        if (canMoveLeft()) {
            Cell leftCell = this.cells[this.currentCell.i][this.currentCell.j - 1];
            if (isValidMove(leftCell)) {
                int calculatedValue = calculate(leftCell);
                Node leftNode = new Node(leftCell, calculatedValue, goalValue, board, this, repeatedStates);
                result.add(leftNode);
            }
        }
        if (canMoveDown()) {
            Cell downCell = this.cells[this.currentCell.i + 1][this.currentCell.j];
            if (isValidMove(downCell)) {
                int calculatedValue = calculate(downCell);
                Node downNode = new Node(downCell, calculatedValue, goalValue, board, this, repeatedStates);
                result.add(downNode);
            }

        }
        if (canMoveUp()) {
            Cell upCell = this.cells[this.currentCell.i - 1][this.currentCell.j];
            if (isValidMove(upCell)) {
                int calculatedValue = calculate(upCell);
                Node upNode = new Node(upCell, calculatedValue, goalValue, board, this, repeatedStates);
                result.add(upNode);
            }
        }
        return result;
    }

    private boolean canEnterGoal(Cell downCell) {
        if (downCell != Cell.getGoal()) return true;
        else {
            return sum >= goalValue;
        }
    }

    //- a : 1
    // + b : 2
    // * : 5
    // ^ : 11
    // goal : 1

    

    // private void calculateGFunction(Node node) {
    //     Cell nodeCell = this.cells[node.currentCell.i][node.currentCell.j];
    //     switch (nodeCell.getOperationType()) {
    //         case MINUS
    //     }


    // }


    private int calculate(Cell cell) {
        return switch (cell.getOperationType()) {
            case MINUS -> sum - cell.getValue();
            case ADD -> sum + cell.getValue();
            case POW -> (int) Math.pow(sum, cell.getValue());
            case MULT -> sum * cell.getValue();
            default -> sum;
        };

    }



    private boolean isWall(Cell cell) {
        return cell.getOperationType() == OPERATION_TYPE.WALL;
    }

    private boolean canMoveRight() {
        return this.currentCell.j < this.board.getRow() - 1;
    }

    private boolean canMoveLeft() {
        return this.currentCell.j > 0;
    }

    private boolean canMoveUp() {
        return this.currentCell.i > 0;
    }

    private boolean canMoveDown() {
        return this.currentCell.i < this.board.getCol() - 1;
    }

    private Boolean isValidMove(Cell destCell) {
        return destCell != Cell.getStart() && canEnterGoal(destCell) && !isWall(destCell) && !repeatedStates.containsKey(destCell.toString());
    }

    public boolean isGoal() {
        if (currentCell.getOperationType() == OPERATION_TYPE.GOAL) {
            return sum >= goalValue;
        }
        return false;
    }

    public int pathCost() {
        return switch (currentCell.getOperationType()) {
            case MINUS, DECREASE_GOAL -> 1;
            case ADD, INCREASE_GOAL -> 2;
            case MULT -> 5;
            case POW -> 11;
            default -> 1;
        };
    }



    // private int heuristic() {

    //     return 0;
    // }

    public String hash() {
        StringBuilder hash = new StringBuilder();
        hash.append("i:")
                .append(currentCell.i)
                .append("j:")
                .append(currentCell.j)
                .append("sum:")
                .append(sum)
                .append("op:")
                .append(currentCell.op)
                .append("val:")
                .append(currentCell.getValue());

        return hash.toString();
    }

    public void drawState() {

        for (int i = 0; i < board.getRow(); i++) {
            for (int j = 0; j < board.getCol(); j++) {
                if (cells[i][j].getOperationType() == OPERATION_TYPE.GOAL) {
                    System.out.print(Constants.ANSI_BRIGHT_GREEN +
                            OPERATION_TYPE.getOperationTag(cells[i][j].getOperationType())
                            + goalValue + spaceRequired(cells[i][j])
                    );
                    continue;
                }
                if (currentCell.j == j && currentCell.i == i) {
                    System.out.print(Constants.ANSI_BRIGHT_GREEN + Constants.PLAYER + sum + spaceRequired(cells[i][j]));
                } else {
                    System.out.print(Constants.ANSI_BRIGHT_GREEN +
                            OPERATION_TYPE.getOperationTag(cells[i][j].getOperationType())
                            + cells[i][j].getValue() + spaceRequired(cells[i][j])
                    );
                }
            }
            System.out.println();

        }
        System.out.println("-----------------------------------------");

    }

    private void setGoalValue() {
        if (currentCell.getOperationType() == OPERATION_TYPE.DECREASE_GOAL)
            goalValue -= currentCell.getValue();
        if (currentCell.getOperationType() == OPERATION_TYPE.INCREASE_GOAL)
            goalValue += currentCell.getValue();
    }

    private String spaceRequired(Cell cell) {
        int length = String.valueOf(cell.getValue()).length();
        String result = " ".repeat(5 - length);
        if (cell.op.equals("+") || cell.op.equals("-") || cell.op.equals("*") || cell.op.equals("^")) {
            result += " ";
        }
        return result;
    }

    @Override
    public String toString() {
        return "(" + this.currentCell.i + "," + this.currentCell.j + ")";

    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Node)) {
            return false;
        }

        Node castedNode = (Node) obj;

        return ((this.currentCell.i == castedNode.currentCell.i) && (this.currentCell.j == castedNode.currentCell.j));

    }

    


 

    public int getGoalValue(){
        return this.goalValue;
    }

    @Override
    public int hashCode() {
        // final int prime = 31;
        // int result = 1;
        // result = (prime * result + i) + (prime * result + j);
        
        // return result;

        return Objects.hash(this.uid);
    }



    public int getFn(){
        return this.fValue;
    }

    public int setFn(int value){
        return this.fValue = value;
    }

    public int getGn(){
        return this.gValue;
    }

    public int setGn(int value){
        return this.gValue = value;
    }

    public int getHn(){
        return this.hValue;
    }

    public int setHn(int value){
        return this.hValue = value;
    }

    @Override
    public int compare(Node node1, Node node2) {
        return (int) (node1.getFn() - node2.getFn());
    }


}
