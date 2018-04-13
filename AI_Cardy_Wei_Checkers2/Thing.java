import java.util.*;
import java.lang.*;

public class Thing extends Piece{

    public boolean pos;
    public boolean again;
    public boolean king;

    public Thing(){}

    public Thing(Coordinate location, boolean isking){
	   super(location);
       king = isking;
    }
    public void setImage(){
        if (isWhite()){
            if(king){
                super.setImage("Pics/wking.png","Pics/wking.png");
            }
	        else{
                super.setImage("Pics/wrook.png","Pics/wrook.png");
            }
        }
        else{
            if(king){
                super.setImage("Pics/bking.png","Pics/bking.png");
            }
            else{
                super.setImage("Pics/brook.png","Pics/brook.png");
            }
        }
    }
    public ArrayList<Coordinate> getMoves(Gameboard g){
        moves = new ArrayList<Coordinate>();
        if (getJumpMoves(g).size()!=0){
            return jumpmoves;
        }
        else{
            gamepiece(g);
          return moves;
        }
        
    }


    public ArrayList<Coordinate> getJumpMoves(Gameboard g){
        jumpmoves=new ArrayList<Coordinate>();
        jumppiece(g);
        return jumpmoves;
    }

    public boolean ispos(){
        return pos;
    }

    public boolean isKinged(){
        return king;
    }

   
    public boolean jumpagain(Gameboard g){
        Piece temp = new NullPiece(getLocation());
        Piece temp2 = new NullPiece(getLocation());
        pos = false;
        if (again){
            if (isWhite()){
                 temp = g.getPiece(getx()-2, gety()+2);
                temp2 = g.getPiece(getx()-1, gety()+1);
                if (temp instanceof NullPiece && !temp2.isWhite()  && !(temp2 instanceof NullPiece)){
                    moves.add(new Coordinate(getx()-2, gety()+2));
                    pos = true;
                }
                temp = g.getPiece(getx()+2, gety()+2);
                temp2 = g.getPiece(getx()+1, gety()+1);
                if (temp instanceof NullPiece && !temp2.isWhite() && !(temp2 instanceof NullPiece)){
                    moves.add(new Coordinate(getx()+2, gety()+2));
                    pos = true;
                }
                if (king){
                    temp = g.getPiece(getx()-2, gety()-2);
                    temp2 = g.getPiece(getx()-1, gety()-1);
                    if (temp instanceof NullPiece && !temp2.isWhite() && !(temp2 instanceof NullPiece)){
                        moves.add(new Coordinate(getx()-2, gety()-2));
                        pos = true;
                    }
                    temp = g.getPiece(getx()+2, gety()-2);
                    temp2 = g.getPiece(getx()+1, gety()-1);
                    if (temp instanceof NullPiece && !temp2.isWhite() && !(temp2 instanceof NullPiece)){
                        moves.add(new Coordinate(getx()+2, gety()-2));
                        pos = true;
                    }
                }
            }   
            else{
                temp = g.getPiece(getx()-2, gety()-2);
                temp2 = g.getPiece(getx()-1, gety()-1);
                if (temp instanceof NullPiece && temp2.isWhite() && !(temp2 instanceof NullPiece)){
                    moves.add(new Coordinate(getx()-2, gety()-2));
                    pos = true;
                }
                temp = g.getPiece(getx()+2, gety()-2);
                temp2 = g.getPiece(getx()+1, gety()-1);
                if (temp instanceof NullPiece && temp2.isWhite() && !(temp2 instanceof NullPiece)){
                    moves.add(new Coordinate(getx()+2, gety()-2));
                    pos = true;
                }
                if(king){
                     temp = g.getPiece(getx()-2, gety()+2);
                    temp2 = g.getPiece(getx()-1, gety()+1);
                    if (temp instanceof NullPiece && temp2.isWhite() && !(temp2 instanceof NullPiece)){
                        moves.add(new Coordinate(getx()-2, gety()+2));
                        pos = true;
                    }
                    temp = g.getPiece(getx()+2, gety()+2);
                    temp2 = g.getPiece(getx()+1, gety()+1);
                    if (temp instanceof NullPiece && temp2.isWhite() && !(temp2 instanceof NullPiece)){
                        moves.add(new Coordinate(getx()+2, gety()+2));
                        pos = true;
                    }
                }
            }
        }
        return pos;
    } 

    public void eliminate(Gameboard g, int x, int y){
         Piece temp3= new NullPiece(new Coordinate(x, y));
         g.setPiece(x, y, temp3);
    }

    public void gamepiece(Gameboard g){
        Piece temp = new NullPiece(getLocation());
        Piece temp2 = new NullPiece(getLocation());
        boolean done = false;
        again = false;
        if (isWhite()){
            temp = g.getPiece(getx()+1, gety()+1);
            if (temp instanceof NullPiece){
                moves.add(new Coordinate(getx()+1, gety()+1));
                again = false;
            }
            temp = g.getPiece(getx()-1, gety()+1);
            if (temp instanceof NullPiece){
                moves.add(new Coordinate(getx()-1, gety()+1));
                 again = false;
            }
            temp = g.getPiece(getx()-2, gety()+2);
            temp2 = g.getPiece(getx()-1, gety()+1);
            if (temp instanceof NullPiece && !temp2.isWhite() && !(temp2 instanceof NullPiece)){
                moves.add(new Coordinate(getx()-2, gety()+2));
                again = true;
            }
            temp = g.getPiece(getx()+2, gety()+2);
            temp2 = g.getPiece(getx()+1, gety()+1);
            if (temp instanceof NullPiece && !temp2.isWhite() && !(temp2 instanceof NullPiece)){
                moves.add(new Coordinate(getx()+2, gety()+2));
               
                again = true;
            }
            if (king){
                temp = g.getPiece(getx()+1, gety()-1);
                if (temp instanceof NullPiece){
                    moves.add(new Coordinate(getx()+1, gety()-1));
                     again = false;
                }
                temp = g.getPiece(getx()-1, gety()-1);
                if (temp instanceof NullPiece){
                    moves.add(new Coordinate(getx()-1, gety()-1));
                     again = false;
                } 
                temp = g.getPiece(getx()-2, gety()-2);
                temp2 = g.getPiece(getx()-1, gety()-1);
                if (temp instanceof NullPiece && !temp2.isWhite() && !(temp2 instanceof NullPiece)){
                    moves.add(new Coordinate(getx()-2, gety()-2));
                    
                    again = true;
                }
                temp = g.getPiece(getx()+2, gety()-2);
                temp2 = g.getPiece(getx()+1, gety()-1);
                if (temp instanceof NullPiece && !temp2.isWhite() && !(temp2 instanceof NullPiece)){
                    moves.add(new Coordinate(getx()+2, gety()-2));
                    
                    again = true;
                }
            }
        }
        else{
            temp = g.getPiece(getx()+1, gety()-1);
            if (temp instanceof NullPiece){
                moves.add(new Coordinate(getx()+1, gety()-1));
                 again = false;
            }
            temp = g.getPiece(getx()-1, gety()-1);
            if (temp instanceof NullPiece){
                moves.add(new Coordinate(getx()-1, gety()-1));
                 again = false;
            } 
            temp = g.getPiece(getx()-2, gety()-2);
            temp2 = g.getPiece(getx()-1, gety()-1);
            if (temp instanceof NullPiece && temp2.isWhite() && !(temp2 instanceof NullPiece)){
                moves.add(new Coordinate(getx()-2, gety()-2));
                
                again = true;
            }
            temp = g.getPiece(getx()+2, gety()-2);
            temp2 = g.getPiece(getx()+1, gety()-1);
            if (temp instanceof NullPiece && temp2.isWhite() && !(temp2 instanceof NullPiece)){
                moves.add(new Coordinate(getx()+2, gety()-2));
                
                again = true;
            }
            if (king){
                temp = g.getPiece(getx()+1, gety()+1);
                if (temp instanceof NullPiece){
                    moves.add(new Coordinate(getx()+1, gety()+1));
                     again = false;
                }
                temp = g.getPiece(getx()-1, gety()+1);
                if (temp instanceof NullPiece){
                    moves.add(new Coordinate(getx()-1, gety()+1));
                     again = false;
                }
                temp = g.getPiece(getx()-2, gety()+2);
                temp2 = g.getPiece(getx()-1, gety()+1);
                if (temp instanceof NullPiece && temp2.isWhite() && !(temp2 instanceof NullPiece)){
                    moves.add(new Coordinate(getx()-2, gety()+2));
                    again = true;
                }
                temp = g.getPiece(getx()+2, gety()+2);
                temp2 = g.getPiece(getx()+1, gety()+1);
                if (temp instanceof NullPiece && temp2.isWhite() && !(temp2 instanceof NullPiece)){
                    moves.add(new Coordinate(getx()+2, gety()+2));
                   
                    again = true;
                }
            }
        }
    }

public void jumppiece(Gameboard g){
        Piece temp = new NullPiece(getLocation());
        Piece temp2 = new NullPiece(getLocation());
        boolean done = false;
        again = false;
        if (isWhite()){

            temp = g.getPiece(getx()-2, gety()+2);
            temp2 = g.getPiece(getx()-1, gety()+1);
            if (temp instanceof NullPiece && !temp2.isWhite() && !(temp2 instanceof NullPiece)){
                jumpmoves.add(new Coordinate(getx()-2, gety()+2));
                again = true;
            }
            temp = g.getPiece(getx()+2, gety()+2);
            temp2 = g.getPiece(getx()+1, gety()+1);
            if (temp instanceof NullPiece && !temp2.isWhite() && !(temp2 instanceof NullPiece)){
                jumpmoves.add(new Coordinate(getx()+2, gety()+2));
               
                again = true;
            }
            if (king){

                temp = g.getPiece(getx()-2, gety()-2);
                temp2 = g.getPiece(getx()-1, gety()-1);
                if (temp instanceof NullPiece && !temp2.isWhite() && !(temp2 instanceof NullPiece)){
                    jumpmoves.add(new Coordinate(getx()-2, gety()-2));
                    
                    again = true;
                }
                temp = g.getPiece(getx()+2, gety()-2);
                temp2 = g.getPiece(getx()+1, gety()-1);
                if (temp instanceof NullPiece && !temp2.isWhite() && !(temp2 instanceof NullPiece)){
                    jumpmoves.add(new Coordinate(getx()+2, gety()-2));
                    
                    again = true;
                }
            }
        }
        else{

            temp = g.getPiece(getx()-2, gety()-2);
            temp2 = g.getPiece(getx()-1, gety()-1);
            if (temp instanceof NullPiece && temp2.isWhite() && !(temp2 instanceof NullPiece)){
                jumpmoves.add(new Coordinate(getx()-2, gety()-2));
                
                again = true;
            }
            temp = g.getPiece(getx()+2, gety()-2);
            temp2 = g.getPiece(getx()+1, gety()-1);
            if (temp instanceof NullPiece && temp2.isWhite() && !(temp2 instanceof NullPiece)){
                jumpmoves.add(new Coordinate(getx()+2, gety()-2));
                
                again = true;
            }
            if (king){

                temp = g.getPiece(getx()-2, gety()+2);
                temp2 = g.getPiece(getx()-1, gety()+1);
                if (temp instanceof NullPiece && temp2.isWhite() && !(temp2 instanceof NullPiece)){
                    jumpmoves.add(new Coordinate(getx()-2, gety()+2));
                    again = true;
                }
                temp = g.getPiece(getx()+2, gety()+2);
                temp2 = g.getPiece(getx()+1, gety()+1);
                if (temp instanceof NullPiece && temp2.isWhite() && !(temp2 instanceof NullPiece)){
                    jumpmoves.add(new Coordinate(getx()+2, gety()+2));
                   
                    again = true;
                }
            }
        }
    }
    public String toString(){
	   return "R";
    }
   

}