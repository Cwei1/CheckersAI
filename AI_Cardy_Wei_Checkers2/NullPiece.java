public class NullPiece extends Piece{
    public NullPiece(Coordinate location){
	   super(location);
    }
    public NullPiece(){
	   this(new Coordinate(0,0));
    }
    public String toString(){
	   return "_";
    }
    public void setImage(){
	   super.setImage("Pics/null.png","Pics/null.png");
    }

} 