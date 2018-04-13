
import java.util.*;
import javax.swing.*;
import java.lang.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Gameboard implements Cloneable, Serializable{
	public String light = "Pics/light.jpg";
    public String dark ="Pics/dark.jpg";
    public String now = light;
    public JButton[][] pattern;
    public Piece[][] board;
    public Piece[][] undoboard;
    public Player p1, p2;
    public boolean jumping=false;
    public boolean jumping2=false;
    public Piece[][] board2;
    public boolean jumped = false;


    public Gameboard(){
    	pattern = new JButton[8][8];
    	board = new Piece[8][8];
    	p1=new Player(true);
    	p2=new Player(false);

    }

    public Gameboard(Piece[][] board){
    	this.board=board;
    	pattern = new JButton[8][8];
    	p1=new Player(true);
    	p2=new Player(false);
    }

    public void initialize(){
	
        for(int y = 0; y < 8; y++){             
		    for(int x = 0; x < 8; x++){
				pattern[y][x] = new JButton();
				pattern[y][x].setIcon(new javax.swing.ImageIcon(getClass().getResource(now)));
				if (now.equals(dark)){
				    now = light;
				}
				else {
					now = dark;
				}
		    }
            if (now.equals(dark)){
				now = light;
            }
            else {
            	now = dark;
            }
		}
		for (int x = 0;x<board.length; x++){
			for (int y = 0; y<board[x].length;y++){
				if ((((x-1)%2==0 && (y)%2==1) || ((x)%2==0 && (y+1)%2==1)) && y != 3 && y!=4 ){
					board[x][y]=new Thing(new Coordinate(x,y), false);
				}
				else{
					board[x][y]=new NullPiece(new Coordinate(x,y));
				}
			}
		}
		iniSide();
		iniImages();
	} 

  public void initialize2(Piece[][] input){
	
         for(int y = 0; y < 8; y++){             
		    for(int x = 0; x < 8; x++){
				pattern[y][x] = new JButton();
				pattern[y][x].setIcon(new javax.swing.ImageIcon(getClass().getResource(now)));
				if (now.equals(dark)){
				    now = light;
				}
				else {
					now = dark;
				}
		    }
            if (now.equals(dark)){
				now = light;
            }
            else {
            	now = dark;
            }
		}
		for (int x = 0;x<board.length; x++){
			for (int y = 0; y<board[x].length;y++){
				board[x][y] = input[x][y];
			}
		}
		iniImages();
	} 

	public void iniSide(){
		for(int i = 0; i < board.length; i++){	    
		    board[i][0].setPlayer(p1);
		    board[i][1].setPlayer(p1);
		    board[i][2].setPlayer(p1);
		    board[i][board.length-1].setPlayer(p2);
		    board[i][board.length-2].setPlayer(p2);
		    board[i][board.length-3].setPlayer(p2);
		}
    }
	// public Piece[][] copyOf2(){
		
	// 	Piece[][] temp = new Piece[board.length][board[0].length];
	// 	for (int i = 0; i < board.length; i++){
	// 	    for (int j = 0; j < board.length; j++){
		    	
	// 		Coordinate c=new Coordinate(i,j);
	// 		if(board[i][j] instanceof Thing){
				
	// 			if (board[i][j].isKinged()){
				
	// 		    	temp[i][j]=new Thing(c, true);

	// 			}
	// 			else{
	// 				temp[i][j]=new Thing(c, false);
	// 			}
				
	// 		}
	// 		else{
	// 		    temp[i][j]=new NullPiece(c);
	// 		}
	// 		temp[i][j].setPlayer(board[i][j].getPlayer());
	// 		temp[i][j].setImage(board[i][j].getAvatar(),board[i][j].getPic());
			
	// 	    }
	// 	}
		
	// 	return temp;
 //    }

    public Piece[][] copyOf(){
    	Piece[][] temp = new Piece[8][8];
		for (int i = 0; i < board.length; i++){
		    for (int j = 0; j < board.length; j++){
		    	temp[i][j] = board[i][j].clone();
		    }
		}
		return temp;
    }

	public JButton[][] getPattern(){
        return pattern;
    }

	public Piece[][] getBoard(){
		return board;
	}

	public void setBoard(Piece[][] another){
		board = another;
	}

	public Piece getPiece(int x, int y){
		
		if (x >= 0 && x <= 7 && y >= 0 && y <= 7){
		    return board[x][y];
		}
		return null;
    }


    public Piece getPiece(Coordinate c){
    	
		return getPiece(c.getx(),c.gety());
    }
    public void setPiece(int x, int y,Piece p){
		board[x][y]=p;
    }

    public boolean movePiece(Coordinate a, Coordinate b){
    	int x1 = a.getx();
    	int y1 = a.gety();
    	int x2 = b.getx();
    	int y2 = b.gety();
    
    	Piece p = getPiece(a);
    	Piece t = getPiece(b);
    	ArrayList<Coordinate> moves = new ArrayList<Coordinate>();
    	moves=p.getMoves(this);
 		System.out.println(moves);
    	for (Coordinate c:moves){

    		if (b.toString().equals(c.toString())){

    			board[x2][y2]=p;
    			board[x1][y1]=new NullPiece(new Coordinate(x1,y1));
    			if (Math.abs(x2-x1)==2 && Math.abs(y2-y1)==2){
    				getPiece((x2+x1)/2, (y1+y2)/2).eliminate(this, (x2+x1)/2, (y1+y2)/2);
    				jumped = true;
    			}
    			else{
    				jumped = false;
    			}
    			getPiece(x2, y2).setxy(x2, y2);
    			
    			if (p.jumpagain(this) && jumped){
    				jumping = true;
    				return false;
    			}
    			else{
    				jumping = false;
    				return true;
    			}
    		}
    	}
    	jumping = false;
    	return false;
    }


    public boolean jumpedmove(){
    	return jumped;
    }

    public void blackMoves(){
    	ArrayList<Coordinate> moves2 = new ArrayList<Coordinate>();
    	for (int x = 0; x<8; x++){
    		for (int y = 0; y< 8; y++){
    			if (!(board[x][y] instanceof NullPiece) && !board[x][y].getPlayer().isWhite()){
    				moves2.addAll(board[x][y].getMoves(this));
    			}

    		}
    	}
    }

    public void whiteMoves(){
    	ArrayList<Coordinate> moves3 = new ArrayList<Coordinate>();
    	for (int x = 0; x<8; x++){
    		for (int y = 0; y< 8; y++){
    			if (!(board[x][y] instanceof NullPiece) && board[x][y].getPlayer().isWhite()){
    				moves3.addAll(board[x][y].getMoves(this));
    			}

    		}
    	}
    }

    public boolean retJump(){
    	return jumping;
    }

    public void upgrade(Piece x){
    	Piece p=new Thing(x.getLocation(), true);
    	p.setPlayer(x.getPlayer());
    	p.setImage();
    	board[x.getx()][x.gety()]=p;
    }

	public void iniImages(){
		for(int y = 0; y < 8; y++){             
		    for(int x = 0; x < 8; x++){
		        board[y][x].setImage(); 
		    }
		}
    }

    public Gameboard clone(){
    	try{
    		return (Gameboard)super.clone();
    	}
    	catch(CloneNotSupportedException e){
    		throw new AssertionError();
    	}
    }


 //    @SuppressWarnings("unchecked")
	// public static  <T> T cloneThroughSerialize(T t) throws Exception {
	//    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	//    //serializeToOutputStream(t, bos);
	//    byte[] bytes = bos.toByteArray();
	//    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
	//    return (T)ois.readObject();
	// }

	// private static void serializeToOutputStream(Serializable ser, OutputStream os) 
	//                                                           throws IOException {
	//    ObjectOutputStream oos = null;
	//    try {
	//       oos = new ObjectOutputStream(os);
	//       oos.writeObject(ser);
	//       oos.flush();
	//    } finally {
	//       oos.close();
	//    }
	// }
	// public Object deepClone() {
	// 	try {
	// 		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	// 		ObjectOutputStream oos = new ObjectOutputStream(baos);
	// 		oos.writeObject(this);

	// 		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
	// 		ObjectInputStream ois = new ObjectInputStream(bais);
	// 		return (Object) ois.readObject();
	// 	} catch (IOException e) {
	// 		return null;
	// 	} catch (ClassNotFoundException e) {
	// 		return null;
	// 	}
	// }

    public void visualize(){
    	for(int y = 0; y < 8; y++){             
		    for(int x = 0; x < 8; x++){
		        System.out.print(board[y][x]+"   ");
		    }
		    System.out.println("\n");
		}
		System.out.println("\n");
    }

}
