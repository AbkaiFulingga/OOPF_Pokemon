
import java.util.*;

public class Pokemon {
	public static final String[] types = {"Earth","Fire","Grass","Water","Fighting","Electric"};
	
    private String name;
    private Boolean stuned = false;
    private Boolean disable = false;
    private int hp;
    private int maxHp;
    private int energy = 50;
    public String type;
    private String resistance;
    private String weakness;
    private int numAttacks;
    public ArrayList<Move>moves;

    
    public Pokemon(String line){ //constructor
    	String[]statsArray = line.split(",");
    	name = statsArray[0];
    	hp = Integer.parseInt(statsArray[1]);
    	maxHp = hp;
    	type = statsArray[2];
    	resistance = statsArray[3];
    	weakness = statsArray[4];
    	numAttacks = Integer.parseInt(statsArray[5]);
    	
    	moves = new ArrayList<Move>();
    	for (int i = 0; i < numAttacks; i++){
    		Move temp = new Move(statsArray, i*4);
    		moves.add(temp);
    	}//end for
    }//end constructor
    
   //GETTERS AND SETTERS BELOW HERE
    public String getName(){
    	return name;
    }
    
    public String getType(){
    	return type;
    }
    
    public int getHealth(){
    	return hp;
    }
    
    public int getMaxHealth(){
    	return maxHp;
    }
    public void setHealth(int x){
    	hp = x;
    }
    
    public int getEnergy(){
    	return energy;
    }
    
    public void setEnergy(int x){
    	energy = x;
    }
    
    public String getResistance(){
    	return resistance;
    }
    
    public String getWeakness(){
    	return weakness;
    }
    public String[] getMove(int index){
    	return (moves.get(index)).getAttack();
    }
    
    public String getMoveName(int index){
    	return (moves.get(index)).getAttackName();
    }
    
    public int getMoveCost(int index){
    	return (moves.get(index)).getAttackCost();
    }
    
    public int getNumAttacks(){
    	return numAttacks;
    }
    
    public void setStuned(Boolean x){
    	stuned = x;
    }
    
    public void setDisable(){
    	disable = true;
    }
    
    public Boolean getStuned(){
    	return stuned;
    }
    
    public Boolean getDisable(){
    	return disable;
    }
}//end Pokemon
