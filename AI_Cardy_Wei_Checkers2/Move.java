public class Move{
    private Coordinate start,end;
 
    public Move(Coordinate s, Coordinate e){
	start = s;
	end = e;
    }
    public Coordinate getStart(){
	return start;
    }
    public Coordinate getEnd(){
	return end;
    }
}
