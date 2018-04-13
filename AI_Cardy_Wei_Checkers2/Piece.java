import java.util.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.BufferedImage;

public class Piece implements Cloneable, Serializable{
    Player owner;
    public Coordinate location;
    boolean first;
    ArrayList<Coordinate> moves;
    ArrayList<Coordinate> jumpmoves;
    ImageIcon avatar;
    BufferedImage buttonIcon;
    boolean pos;
    boolean king;

    public Piece(){

    }

    public Piece(Player a, Coordinate location){
		setPlayer(a);
		setLocation(location);
        avatar = new ImageIcon();
		first=true;
		//setImage();
    }
    public Piece(Coordinate location){
		this(new Player(true),location);
    }
    public void setFirst(boolean a){
		first=a;
    }
    public boolean isFirst(){
		return first;
    }
    public void setLocation(Coordinate l){
		location=l;
    }

    public void setImage(String bpath, String wpath){
        if(owner.isWhite()){
		    try{
				buttonIcon = ImageIO.read(new File(wpath));
		    } 
		    catch(IOException e){
				System.out.println("File not found: " + e.getMessage());
		    }
		    avatar = new ImageIcon(buttonIcon);
        } 
        else {           
		    try{
		        buttonIcon = ImageIO.read(new File(bpath));
		    } 
		    catch (IOException e){
				System.out.println("File not found: " + e.getMessage());
		    }
		    avatar = new ImageIcon(buttonIcon);
		}
    }


    public ImageIcon getAvatar(){
		return avatar;
    }
    public BufferedImage getPic(){
		return buttonIcon;
    }
    public void setImage(ImageIcon i,BufferedImage b){
		avatar=i;
		buttonIcon=b;
    }

    public boolean isKinged(){
        return king;
    }

    public void setImage(){}

    public boolean isWhite(){
		return owner.isWhite();
    }

    public int getx(){
		return location.getx();
    }

    public int gety(){
		return location.gety();
    }

    public Player getPlayer(){
		return owner;
    }

    public void setxy(int x, int y){
		location.setxy(x,y);
    }

    protected void setPlayer(Player p){
		owner = p;
    }

    public Coordinate getLocation(){
		return location;
    }

    public boolean jumpagain(Gameboard g){
    	return pos;
    }

    public ArrayList<Coordinate> getMoves(Gameboard g){

		moves=new ArrayList<Coordinate>();
        if (getJumpMoves(g).size()!=0){
            return jumpmoves;
        }
        else{
		  return moves;
        }
    }
	public ArrayList<Coordinate> getJumpMoves(Gameboard g){
        jumpmoves=new ArrayList<Coordinate>();
        return jumpmoves;
    }

    public void eliminate(Gameboard g, int x, int y){
        
    }

     public Piece clone(){
    	try{
    		Piece piece = (Piece)super.clone();
    		piece.location = location.clone();
    		return piece;
    	}
    	catch(CloneNotSupportedException e){
    		throw new AssertionError();
    	}
    }
}