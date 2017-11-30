public class Player{
    private boolean white;
    public Player(boolean i){
		white=i;
    }
    public String toString(){
		if(white){
		    return "Player 1";
		}else{
		    return "Player 2";
		}
    }
    public boolean isWhite(){
		return white;
    }
}
