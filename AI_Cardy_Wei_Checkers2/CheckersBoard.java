import javax.swing.*;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.util.concurrent.*;
import java.lang.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class CheckersBoard extends JFrame implements ActionListener{

	private Gameboard board = new Gameboard();
	private String letters="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private JPanel mainboard;

   	public Gameboard tempboard=new Gameboard();
    private JFrame frame=new JFrame();
    private Color black=new Color(139,69,19);
    private Color white=new Color(244,164,96);
    private Color now=white;
    private Scanner s;
    private Coordinate start, end;

    private Piece p1;
    private Piece p2;

    public boolean twoplayer= false;
    public boolean singleblack = false;
    public boolean singlewhite = false;
    public boolean aimatch = false;
    public boolean turn = true;
    public TreeNode<Gameboard> gamestates= new TreeNode<Gameboard>(board);
    public ArrayList<Gameboard> anotherjump;
    public Gameboard bestboard;
    public boolean whitewin = false;
    public boolean blackwin = false;
    public int popup = 0;
    public boolean timeover = false;
    public long starttime;
    public long endtime;
    public boolean startneeded = true;

    public Gameboard bestboard2;

    public boolean depthplay=false;
    public boolean timeplay =false;

    Player player1 = new Player(true);
    Player player2 = new Player(false);
    
    public CheckersBoard(){
        Gameboard temp= new Gameboard();
        String[][] inputarray = new String[8][8];
        Piece[][] piecearray= new Piece[8][8];
         Scanner scanner = new Scanner(System.in);
        System.out.println("Would you like to enter in a custom board? (y/n)");
        String input = scanner.nextLine();
        if (input.equals("y")){
            
            Scanner scanner2 = new Scanner(System.in);
            System.out.println("Please enter the text file containing the board you would like to input");
            String inputfile = scanner.nextLine();
            System.out.println(inputfile);

            try{
                List<String> lines=Files.readAllLines(Paths.get(inputfile));
                for (int j = 0; j<lines.size();j++){
                    for (int i = 0; i<lines.get(j).length();i++){
                        inputarray[j][i] = lines.get(j).charAt(i)+"";
                    }
                }
                for (int i = 0; i<8;i++){
                    for (int j = 0; j<8;j++){
                        if (inputarray[i][j].equals("W")){
                            piecearray[j][7-i] = new Thing(new Coordinate(j,7-i), false);
                            piecearray[j][7-i].setPlayer(player1);
                        }
                        else if (inputarray[i][j].equals("K")){
                            piecearray[j][7-i] = new Thing(new Coordinate(j,7-i), true);
                            piecearray[j][7-i].setPlayer(player1);
                        }
                        else if (inputarray[i][j].equals("L")){
                            piecearray[j][7-i] = new Thing(new Coordinate(j,7-i), true);
                            piecearray[j][7-i].setPlayer(player2);
                        }
                        else if (inputarray[i][j].equals("B")){
                            piecearray[j][7-i] = new Thing(new Coordinate(j,7-i), false);
                            piecearray[j][7-i].setPlayer(player2);
                        }
                        else{
                            piecearray[j][7-i] = new NullPiece(new Coordinate(j,7-i));
                        }
                    }
                }
                temp = new Gameboard(piecearray);
               //temp.visualize();
                 

            }catch(IOException e){
                System.out.println("That is not a valid file!");
            }
            
        }
       
       if (piecearray[0][0]!=null){
        //board = temp.clone();
        board.initialize2(piecearray);
       // board.visualize();
        //initComponents2();
       }
       else{
         board.initialize();
         board.visualize();
         //initComponents();
       }
        //board.initialize();
        initComponents();

        Object[] possibilities = {"Two-Player Mode",  "Single-Player Mode Play as White", "Single-Player Mode Play as Black","AI v AI" };
        String k = (String)JOptionPane.showInputDialog(frame, "Choose Your Preferred Mode of Game:","Game Mode",JOptionPane.PLAIN_MESSAGE,null, possibilities,"Two-Player Mode");
        if (k == null){
        System.exit(0);
        }
        if (k.equals("Two-Player Mode")){
            twoplayer = true;
                     for (int x = 0; x < 8 ; x++){
                for (int y = 0; y < 8; y++){
                     ArrayList<Coordinate> allmovespos = board.getPiece(x,y).getMoves(board);
                 if (allmovespos.size()!=0 && board.getPiece(x,y).isWhite() && turn){
                     board.pattern[x][y].setBackground(Color.GREEN);
                 }
                 // if (allmovespos.size()!=0 && !board.getPiece(x,y).isWhite() && !turn){
                 //     board.pattern[x][y].setBackground(Color.GREEN);
                 // }
             }
         }

        }
        else if (k.equals("Single-Player Mode Play as Black")){
            Object[] possibilities2={"Depth(9)", "Time(5s)"};
               String l = (String)JOptionPane.showInputDialog(frame, "Choose Your Preferred Mode of Game:","Game Mode",JOptionPane.PLAIN_MESSAGE,null, possibilities2,"Two-Player Mode");
           if (l.equals("Depth(9)")){
            depthplay=true;
           }
           else if (l.equals("Time(5s)")){
            timeplay=true;
           }
            singleblack = true;
             expandmaxwhite(board,  turn,5, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,0);
            if (tempboard!=null){
                board.setBoard(bestboard.getBoard());

            }
            else{
                System.out.println("NULLBOARD");
            }
           refresh();
            turn=!turn;
              for (int x = 0; x < 8 ; x++){
                for (int y = 0; y < 8; y++){
                     ArrayList<Coordinate> allmovespos = board.getPiece(x,y).getMoves(board);
                 // if (allmovespos.size()!=0 && board.getPiece(x,y).isWhite() && turn){
                 //     board.pattern[x][y].setBackground(Color.GREEN);
                 // }
                 if (allmovespos.size()!=0 && !board.getPiece(x,y).isWhite() && !turn){
                     board.pattern[x][y].setBackground(Color.GREEN);
                 }
             }
         }

        }
         else if (k.equals("Single-Player Mode Play as White")){
            Object[] possibilities2 = {"Depth(9)", "Time(5s)"};
             String l = (String)JOptionPane.showInputDialog(frame, "Choose Your Preferred Mode of Game:","Game Mode",JOptionPane.PLAIN_MESSAGE,null, possibilities2,"Two-Player Mode");
             if (l.equals("Depth(9)")){
            depthplay=true;
           }
           else if (l.equals("Time(5s)")){
            timeplay=true;
           }
            singlewhite=true;
              for (int x = 0; x < 8 ; x++){
                for (int y = 0; y < 8; y++){
                     ArrayList<Coordinate> allmovespos = board.getPiece(x,y).getMoves(board);
                 if (allmovespos.size()!=0 && board.getPiece(x,y).isWhite() && turn){
                     board.pattern[x][y].setBackground(Color.GREEN);
                 }
                 // if (allmovespos.size()!=0 && !board.getPiece(x,y).isWhite() && !turn){
                 //     board.pattern[x][y].setBackground(Color.GREEN);
                 // }
             }
         }

        }
        else{
            Object[] possibilities2 = {"Depth(9)", "Time(5s)"};
             String l = (String)JOptionPane.showInputDialog(frame, "Choose Your Preferred Mode of Game:","Game Mode",JOptionPane.PLAIN_MESSAGE,null, possibilities2,"Two-Player Mode");
             if (l.equals("Depth(9)")){
            depthplay=true;
           }
           else if (l.equals("Time(5s)")){
            timeplay=true;
           }
            aimatch=true;
            while(!blackwin && !whitewin){
                if (timeplay){
                            if (turn){
                                System.out.println("WHITE MOVE");
                                //  starttime = System.nanoTime();
                                 
                                //  ExecutorService executor = Executors.newSingleThreadExecutor();
                                // Future<String> future = executor.submit(new Task());

                                // try {
                                //     System.out.println("Started..");
                                //     System.out.println(future.get(3, TimeUnit.SECONDS));
                                //     System.out.println("Finished!");
                                // } catch (TimeoutException e) {
                                //     future.cancel(true);
                                //     System.out.println("Terminated!");
                                    
                                // }catch (Exception e) {
                                //     throw new RuntimeException(e);
                                // }

                                // executor.shutdownNow();
                                starttime = System.nanoTime();
                                int depthofsearch = 1;
                                    while (true){
                                        timeover=false;
                                        expandmaxwhite(board,  turn,depthofsearch, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,0);
                                         System.out.println("Searched up to depth: "+depthofsearch);
                                        depthofsearch++;

                                        if (timeover){
                                            break;
                                        }
                                        bestboard2 = bestboard;
                                    }

                                System.out.println("Time taken "+ (endtime-starttime)/1000000);
                                if (bestboard!=null){
                                    board.setBoard(bestboard.getBoard());
                                }
                                else{
                                    System.out.println("NULLBOARD");
                                }
                               refresh();
                                turn=!turn;
                            }
                            else{
                                System.out.println("BLACK MOVE");
                                starttime = System.nanoTime();
                                
                                //  ExecutorService executor = Executors.newSingleThreadExecutor();
                                // Future<String> future = executor.submit(new Task2());

                                // try {
                                //     System.out.println("Started..");
                                //     System.out.println(future.get(3, TimeUnit.SECONDS));
                                //     System.out.println("Finished!");
                                // } catch (TimeoutException e) {
                                //     future.cancel(true);

                                //     System.out.println("Terminated!");
                                    
                                // }catch (Exception e) {
                                //     throw new RuntimeException(e);
                                // }

                                // executor.shutdownNow();
                                
                                 int depthofsearch = 1;
                                    while (true){
                                        timeover=false;
                                        expandmaxblack(board,  turn,depthofsearch, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,0);
                                         System.out.println("Searched up to depth: "+depthofsearch);
                                        depthofsearch++;

                                        if (timeover){
                                            break;
                                        }
                                        bestboard2 = bestboard;
                                    }

                                System.out.println("Time taken "+ (endtime-starttime)/1000000);
                                if (tempboard!=null){
                                    board.setBoard(bestboard.getBoard());

                                }
                                else{
                                    System.out.println("NULLBOARD");
                                }
                               refresh();
                                turn=!turn;
                                
                            }
                            try {
                                 Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                 e.printStackTrace();
                            }
                }
                else if(depthplay){
                      if (turn){
                            System.out.println("WHITE MOVE");
                             starttime = System.nanoTime();
                             expandmaxwhite(board,  turn,9, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,0);
                           
                            System.out.println("Time taken "+ (endtime-starttime)/1000000);
                            if (bestboard!=null){
                                board.setBoard(bestboard.getBoard());
                            }
                            else{
                                System.out.println("NULLBOARD");
                            }
                           refresh();
                            turn=!turn;
                        }
                        else{
                            System.out.println("BLACK MOVE");
                            starttime = System.nanoTime();
                            
                              expandmaxblack(board,  turn,9, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,0);
                            
                            System.out.println("Time taken "+ (endtime-starttime)/1000000);
                            if (tempboard!=null){
                                board.setBoard(bestboard.getBoard());

                            }
                            else{
                                System.out.println("NULLBOARD");
                            }
                           refresh();
                            turn=!turn;
                            
                        }
                        try {
                             Thread.sleep(1000);
                        } catch (InterruptedException e) {
                             e.printStackTrace();
                        }
                }
            }
            win(board);
            if (blackwin){
                 JOptionPane.showMessageDialog(new JFrame(),"BLACK WINS");
            }
            else if (whitewin){
                 JOptionPane.showMessageDialog(new JFrame(),"WHITE WINS");
            }
        }

    	
    }



    class Task implements Callable<String> {
        @Override
        public String call() throws Exception {
            int deep = 1;
               while(!Thread.interrupted()){
                     expandmaxwhite(board,  turn,deep, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,0);
                     System.out.println("Searched up to depth "+ deep);
                     deep ++;
                     
            }
            return "Ready!";
        }
    }

     class Task2 implements Callable<String> {
        @Override
        public String call() throws Exception {
            int deep =1;
             while(!Thread.interrupted()){
                     expandmaxblack(board,  turn,deep, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,0);
                     System.out.println("Searched up to depth "+ deep);
                     deep++;
                     
            }
            return "Ready!";
        }
    }

	private void initComponents() {
		GridLayout grid1=new GridLayout(1, 8);
		GridLayout grid2=new GridLayout(8, 1);
		GridLayout grid3=new GridLayout(8,8);
		JPanel panel1=new JPanel();
		JPanel panel2=new JPanel();
		JPanel panel3=new JPanel();
		JPanel panel4=new JPanel();
		panel1.setLayout(grid1);
		panel2.setLayout(grid2);
		panel3.setLayout(grid1);
		panel4.setLayout(grid2);
		panel1.add(new JPanel());
		panel3.add(new JPanel());	
		mainboard = new JPanel();
		mainboard.setLayout(grid3);
		for (int y =0; y<8; y++){
			for(int x = 0;x < 8; x++){
				ImageIcon icon=board.getBoard()[x][7-y].getAvatar();
				board.pattern[x][7-y].setIcon(icon);
				board.pattern[x][7-y].setPreferredSize(new Dimension(75, 75));
				board.pattern[x][7-y].addActionListener(this);
				board.pattern[x][7-y].setBackground(now);
				if(now.equals(white)){
				    now=black;
				}
				else{
				    now=white;
				}
				mainboard.add(board.pattern[x][7-y]);		
		    }
		    if(now.equals(white)){
				now=black;
		    }
		    else{
				now=white;
		    }
			panel1.add(new JLabel("  "+letters.substring(y,y+1)+"  "));
		    panel2.add(new JLabel(Integer.toString(8-y)));
		    panel3.add(new JLabel("  "+letters.substring(y,y+1)+"  "));
		    panel4.add(new JLabel(Integer.toString(8-y)));
		}
		frame.setTitle("Checkers");
		frame.getContentPane().add(BorderLayout.CENTER, mainboard);
		frame.getContentPane().add(BorderLayout.NORTH, panel1);
		frame.getContentPane().add(BorderLayout.WEST, panel2);
		frame.getContentPane().add(BorderLayout.SOUTH, panel3);
		frame.getContentPane().add(BorderLayout.EAST, panel4);
		frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		frame.setSize(675, 700);
		frame.setLocation(100, 0);
		frame.setVisible(true);
		frame.setResizable(false);
    }  


    public void actionPerformed(ActionEvent event){
        if (twoplayer){
            boolean jumpsonly =false;
        	for (int x = 0; x < 8 ; x++){
        		for (int y = 0; y < 8; y++){
        			if (event.getSource() == board.pattern[x][y]){
        				if (start == null && turn){
        					if (!(board.getPiece(x,y) instanceof NullPiece) && board.getPiece(x,y).isWhite() && startneeded){

        						now=board.pattern[x][y].getBackground();
        						board.pattern[x][y].setBackground(Color.GREEN);
        						start = new Coordinate(x,y);

                                            ArrayList<Coordinate> allmoves = board.getPiece(x,y).getMoves(board);
                            for (int z = 0; z < 8 ; z++){
                                    for (int p = 0; p < 8; p++){
                                        ArrayList<Coordinate> allmoves2=new ArrayList<Coordinate>();
                                        if (board.getPiece(z,p).isWhite()){
                                            allmoves2 = board.getPiece(z,p).getJumpMoves(board);
                                        }
                                        if (allmoves2.size()!=0){
                                            jumpsonly=true;
                                        }
                                    }
                                }
                                 ArrayList<Coordinate> allmoves2 = board.getPiece(x,y).getJumpMoves(board);
                               if (jumpsonly){
                                    for (Coordinate c: allmoves2){
                                        now = board.pattern[c.getx()][c.gety()].getBackground();
                                        board.pattern[c.getx()][c.gety()].setBackground(Color.GREEN);
                                    }
                               }
                              else{
                                for (Coordinate c: allmoves){
                                    now = board.pattern[c.getx()][c.gety()].getBackground();
                                    board.pattern[c.getx()][c.gety()].setBackground(Color.GREEN);
                                }
                           }
        					}
        				}
        				else if(!(board.getPiece(x,y) instanceof NullPiece) && board.getPiece(x,y).isWhite() && turn && !board.retJump() && startneeded){
        					clearBackground();
        					now = board.pattern[x][y].getBackground();
        					board.pattern[x][y].setBackground(Color.GREEN);
        					start = new Coordinate(x,y);

                                        ArrayList<Coordinate> allmoves = board.getPiece(x,y).getMoves(board);
                            for (int z = 0; z < 8 ; z++){
                                    for (int p = 0; p < 8; p++){
                                        ArrayList<Coordinate> allmoves2=new ArrayList<Coordinate>();
                                        if (board.getPiece(z,p).isWhite()){
                                            allmoves2 = board.getPiece(z,p).getJumpMoves(board);
                                        }

                                        if (allmoves2.size()!=0){
                                            System.out.println(allmoves2);
                                            jumpsonly=true;
                                        }
                                    }
                                }
                                 ArrayList<Coordinate> allmoves2 = board.getPiece(x,y).getJumpMoves(board);
                               if (jumpsonly){
                                    for (Coordinate c: allmoves2){
                                        now = board.pattern[c.getx()][c.gety()].getBackground();
                                        board.pattern[c.getx()][c.gety()].setBackground(Color.GREEN);
                                    }
                               }
                              else{
                                for (Coordinate c: allmoves){
                                    now = board.pattern[c.getx()][c.gety()].getBackground();
                                    board.pattern[c.getx()][c.gety()].setBackground(Color.GREEN);
                                }
                           }
        					
        				}
        				else if(!(board.getPiece(x,y) instanceof NullPiece) && !board.getPiece(x,y).isWhite() && !turn && !board.retJump()&& startneeded){
        					clearBackground();
        					now=board.pattern[x][y].getBackground();
        					board.pattern[x][y].setBackground(Color.GREEN);
        					start = new Coordinate(x,y);

                                        ArrayList<Coordinate> allmoves = board.getPiece(x,y).getMoves(board);
                            for (int z = 0; z < 8 ; z++){
                                    for (int p = 0; p < 8; p++){
                                        ArrayList<Coordinate> allmoves2=new ArrayList<Coordinate>();
                                        if (!board.getPiece(z,p).isWhite()){
                                            allmoves2 = board.getPiece(z,p).getJumpMoves(board);
                                        }

                                        if (allmoves2.size()!=0){
                                            System.out.println(allmoves2);
                                            jumpsonly=true;
                                        }
                                    }
                                }
                                 ArrayList<Coordinate> allmoves2 = board.getPiece(x,y).getJumpMoves(board);
                               if (jumpsonly){
                                    for (Coordinate c: allmoves2){
                                        now = board.pattern[c.getx()][c.gety()].getBackground();
                                        board.pattern[c.getx()][c.gety()].setBackground(Color.GREEN);
                                    }
                               }
                              else{
                                for (Coordinate c: allmoves){
                                    now = board.pattern[c.getx()][c.gety()].getBackground();
                                    board.pattern[c.getx()][c.gety()].setBackground(Color.GREEN);
                                }
                           }
                               
        				}
        				else{
        					 if (end == null){
                                board.visualize();
                                if (board.pattern[x][y].getBackground() != Color.GREEN){
                                    System.out.println("Not Legal Move");
                                }
                                else{
                                    //clearBackground();
                                   //System.out.println("WHAAT IS HAPPENING");
                                    end = new Coordinate(x,y);
                                     Move m = new Move(start, end);
                                     System.out.println(start);
                                     System.out.println(end);
                                    if (board.movePiece(m.getStart(), m.getEnd())){
                                        
                                        if (end.gety()==7 && board.getPiece(x,y).isWhite()){
                                            board.upgrade(board.getPiece(x,y));
                                        }
                                        else if (end.gety()==0 && !board.getPiece(x,y).isWhite()){
                                            board.upgrade(board.getPiece(x,y));
                                        }
                                        System.out.println(board.retJump());
                                        if (!board.retJump()){
                                            turn=!turn;
                                            startneeded=true;
                                            if (!turn){
                                                System.out.println("BLACK TURN");
                                            }
                                            else{
                                                System.out.println("WHITE TURN");      
                                            }

                                        }

                                        refresh();
                                      
                                    }
                                    refresh();
                                    if (board.retJump()){
                                        clearBackground();
                                        start = end;
                                        //end= null;
                                        System.out.println("HERE");
                                        ArrayList<Coordinate> tempmoves = board.getPiece(x,y).getJumpMoves(board);
                                        System.out.println(tempmoves);
                                        board.pattern[x][y].doClick();
                                        for(Coordinate c: tempmoves){
                                            board.pattern[c.getx()][c.gety()].setBackground(Color.GREEN);
                                        }
                                        refresh();
                                         startneeded=false;
                                    }
                                }
                            
                            }

                            end = null; 
        					refresh();
        					clearBackground();


        			// 		if (board.retJump()){
        			// 			for (int a = 0; a < 8; a++){
        			// 				for (int b = 0; b < 8; b++){
        			// 					board.pattern[a][b].setEnabled(false);
        			// 				}
        			// 			}
        			// 			clearBackground();
        			// 			for (Coordinate c:board.getPiece(x,y).getJumpMoves(board)){
        			// 				JButton g = board.pattern[c.getx()][c.gety()];
        			// 				g.setEnabled(true);
        			// 			}
    	    		// 			now=board.pattern[x][y].getBackground();
    	    		// 			board.pattern[x][y].setBackground(Color.GREEN);
    	    		// 			start = new Coordinate(x,y);
    	    					
        			// 		}
        			// 		else if (!board.retJump()){
    							// for (int a = 0; a < 8; a++){
        			// 				for (int b = 0; b < 8; b++){
        			// 					board.pattern[a][b].setEnabled(true);
        			// 				}
        			// 			}
        			// 		}
                           
                                 for (int z = 0; z < 8 ; z++){
                                    for (int p = 0; p < 8; p++){
                                         ArrayList<Coordinate> allmoves2 = board.getPiece(z,p).getJumpMoves(board);
                                         //ArrayList<Coordinate> allmoves = board.getPiece(x,y).getMoves(board);
                                        if (allmoves2.size()!=0){
                                            jumpsonly = true;
                                           
                                        }
                                         if (jumpsonly){
                                             // allmoves2 = board.getPiece(z,p).getJumpMoves(board);
                                            allmoves2 = board.getPiece(z,p).getJumpMoves(board);
                                            if (allmoves2.size()!=0 && board.getPiece(z,p).isWhite() && turn){
                                                 now = board.pattern[z][p].getBackground();
                                                 board.pattern[z][p].setBackground(Color.GREEN);
                                            }
                                        }
                                        else{
                                            ArrayList<Coordinate> allmoves = board.getPiece(z,p).getMoves(board);
                                            if (allmoves.size()!=0 && board.getPiece(z,p).isWhite() && turn){
                                                 now = board.pattern[z][p].getBackground();
                                                 board.pattern[z][p].setBackground(Color.GREEN);
                                            }
                                        }
                                         if (jumpsonly){
                                             // allmoves2 = board.getPiece(z,p).getJumpMoves(board);
                                            allmoves2 = board.getPiece(z,p).getJumpMoves(board);
                                            if (allmoves2.size()!=0 && !board.getPiece(z,p).isWhite() && !turn){
                                                 now = board.pattern[z][p].getBackground();
                                                 board.pattern[z][p].setBackground(Color.GREEN);
                                            }
                                        }
                                        else{
                                            ArrayList<Coordinate> allmoves = board.getPiece(z,p).getMoves(board);
                                            if (allmoves.size()!=0 && !board.getPiece(z,p).isWhite() && !turn){
                                                 now = board.pattern[z][p].getBackground();
                                                 board.pattern[z][p].setBackground(Color.GREEN);
                                            }
                                        }
                                    }
                                }
                             if (blackwin && popup == 0){
                                     JOptionPane.showMessageDialog(new JFrame(),"BLACK WINS");
                                     popup++;
                                }
                                else if (whitewin && popup==0){
                                     JOptionPane.showMessageDialog(new JFrame(),"WHITE WINS");
                                     popup++;
                                     break;
                            }
        				}
        			}
        		}
        	}
        }
   else if(singlewhite) {
   
           for (int x = 0; x < 8 ; x++){
                for (int y = 0; y < 8; y++){
                    if (event.getSource() == board.pattern[x][y]){
                                 boolean jumpsonly = false;
                        if (start == null && turn){
                            if (!(board.getPiece(x,y) instanceof NullPiece) && board.getPiece(x,y).isWhite()){
                                // now=board.pattern[x][y].getBackground();
                                // board.pattern[x][y].setBackground(Color.GREEN);
                                start = new Coordinate(x,y);

                                   ArrayList<Coordinate> allmoves = board.getPiece(x,y).getMoves(board);
                            for (int z = 0; z < 8 ; z++){
                                    for (int p = 0; p < 8; p++){
                                        ArrayList<Coordinate> allmoves2=new ArrayList<Coordinate>();
                                        if (board.getPiece(z,p).isWhite()){
                                            allmoves2 = board.getPiece(z,p).getJumpMoves(board);
                                        }
                                        if (allmoves2.size()!=0){
                                            jumpsonly=true;
                                        }
                                    }
                                }
                                 ArrayList<Coordinate> allmoves2 = board.getPiece(x,y).getJumpMoves(board);
                               if (jumpsonly){
                                    for (Coordinate c: allmoves2){
                                        now = board.pattern[c.getx()][c.gety()].getBackground();
                                        board.pattern[c.getx()][c.gety()].setBackground(Color.GREEN);
                                    }
                               }
                              else{
                                for (Coordinate c: allmoves){
                                    now = board.pattern[c.getx()][c.gety()].getBackground();
                                    board.pattern[c.getx()][c.gety()].setBackground(Color.GREEN);
                                }
                           }
                            }
                        }
                        else if(!(board.getPiece(x,y) instanceof NullPiece) && board.getPiece(x,y).isWhite() && turn && !board.retJump() ){

                            System.out.println("GOT HERE");
                            clearBackground();
                            // now = board.pattern[x][y].getBackground();
                            // board.pattern[x][y].setBackground(Color.GREEN);
                            start = new Coordinate(x,y);
                            

                               // ArrayList<Coordinate> allmoves = board.getPiece(x,y).getMoves(board);
                               //  for (Coordinate c: allmoves){
                               //      now = board.pattern[c.getx()][c.gety()].getBackground();
                               //      board.pattern[c.getx()][c.gety()].setBackground(Color.GREEN);
                               //  }
                            for (int z = 0; z < 8 ; z++){
                                    for (int p = 0; p < 8; p++){
                                        ArrayList<Coordinate> allmoves2=new ArrayList<Coordinate>();
                                        if (board.getPiece(z,p).isWhite()){
                                            allmoves2 = board.getPiece(z,p).getJumpMoves(board);
                                        }

                                        if (allmoves2.size()!=0 && board.getPiece(z,p).isWhite()){
                                            jumpsonly=true;
                                        }
                                    }
                                }
                                

                               if (jumpsonly){
                                 ArrayList<Coordinate> allmoves2 = board.getPiece(x,y).getJumpMoves(board);
                                    for (Coordinate c: allmoves2){
                                        now = board.pattern[c.getx()][c.gety()].getBackground();
                                        board.pattern[c.getx()][c.gety()].setBackground(Color.GREEN);
                                    }
                               }
                              else{
                                ArrayList<Coordinate> allmoves = board.getPiece(x,y).getMoves(board);
                                 System.out.println(allmoves);
                                for (Coordinate c: allmoves){
                                    now = board.pattern[c.getx()][c.gety()].getBackground();
                                    board.pattern[c.getx()][c.gety()].setBackground(Color.GREEN);
                                }
                           }
                        }
                        
                        
                        else{
                            if (end == null){
                                board.visualize();
                                if (board.pattern[x][y].getBackground() != Color.GREEN){
                                    System.out.println("Not Legal Move");
                                }
                                else{
                                    //clearBackground();
                                   //System.out.println("WHAAT IS HAPPENING");
                                    end = new Coordinate(x,y);
                                     Move m = new Move(start, end);
                                     System.out.println(start);
                                     System.out.println(end);
                                    if (board.movePiece(m.getStart(), m.getEnd())){
                                        
                                        if (end.gety()==7 && board.getPiece(x,y).isWhite()){
                                            board.upgrade(board.getPiece(x,y));
                                        }
                                        else if (end.gety()==0 && !board.getPiece(x,y).isWhite()){
                                            board.upgrade(board.getPiece(x,y));
                                        }
                                        System.out.println(board.retJump());
                                        if (!board.retJump()){
                                            turn=!turn;
                                            startneeded=true;
                                            if (!turn){
                                                System.out.println("BLACK TURN");
                                            }
                                            else{
                                                System.out.println("WHITE TURN");      
                                            }

                                        }

                                        refresh();
                                      
                                    }
                                    refresh();
                                    if (board.retJump()){
                                        clearBackground();
                                        start = end;
                                        //end= null;
                                        System.out.println("HERE");
                                        ArrayList<Coordinate> tempmoves = board.getPiece(x,y).getJumpMoves(board);
                                        System.out.println(tempmoves);
                                        board.pattern[x][y].doClick();
                                        for(Coordinate c: tempmoves){
                                            board.pattern[c.getx()][c.gety()].setBackground(Color.GREEN);
                                        }
                                        refresh();
                                         startneeded=false;
                                    }
                                }
                            
                            }
                              refresh();
                            end = null;
                            refresh();
                            clearBackground();

                            // if (board.retJump()){
                            //     for (int a = 0; a < 8; a++){
                            //         for (int b = 0; b < 8; b++){
                            //             board.pattern[a][b].setEnabled(false);
                            //         }
                            //     }
                            //     clearBackground();
                            //     for (Coordinate c:board.getPiece(x,y).getJumpMoves(board)){
                            //         JButton g = board.pattern[c.getx()][c.gety()];
                            //         g.setEnabled(true);
                            //     }
                            //     now=board.pattern[x][y].getBackground();
                            //     board.pattern[x][y].setBackground(Color.GREEN);
                            //     start = new Coordinate(x,y);
                                
                            // }
                            // else if (!board.retJump()){
                            //     for (int a = 0; a < 8; a++){
                            //         for (int b = 0; b < 8; b++){
                            //             board.pattern[a][b].setEnabled(true);
                            //         }
                            //     }
                            // }
                            if (!turn){
                             //clearBackground();
                               starttime = System.nanoTime();

                               if (timeplay){
                                    // ExecutorService executor = Executors.newSingleThreadExecutor();
                                    // Future<String> future = executor.submit(new Task2());

                                    // try {
                                    //     System.out.println("Started..");
                                    //     System.out.println(future.get(5, TimeUnit.SECONDS));
                                    //     System.out.println("Finished!");
                                    // } catch (TimeoutException e) {
                                    //     future.cancel(true);
                                    //     System.out.println("Terminated!");

                                    // }catch (final Exception e) {
                                    //     throw new RuntimeException(e);
                                    // }

                                    // executor.shutdownNow();
                                 int depthofsearch = 1;
                                    while (true){
                                        timeover=false;
                                        expandmaxblack(board,  turn,depthofsearch, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,0);
                                         System.out.println("Searched up to depth: "+depthofsearch);
                                        depthofsearch++;

                                        if (timeover){
                                            break;
                                        }
                                        bestboard2 = bestboard;
                                    }


                                }
                                else if(depthplay){
                                    expandmaxblack(board,  turn, 9, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,0);
                                    bestboard2=bestboard;
                                }
                                if (bestboard2!=null){
                                    board.setBoard(bestboard2.getBoard());

                                }
                                else{
                                    System.out.println("NULLBOARD");
                                }
                               refresh();
                                long end = System.nanoTime();
                                System.out.println("time taken: "+(end-starttime)/1000000);
                                System.out.println(turn);
                                turn=!turn;
                                System.out.println("WHITE TURN");
                            }
                            //  ArrayList<Coordinate> allmoves = board.getPiece(x,y).getMoves(board);
                                 clearBackground();
                                 for (int z = 0; z < 8 ; z++){
                                    for (int p = 0; p < 8; p++){
                                         ArrayList<Coordinate> allmoves2 = board.getPiece(z,p).getJumpMoves(board);
                                         //ArrayList<Coordinate> allmoves = board.getPiece(x,y).getMoves(board);
                                        if (allmoves2.size()!=0 && board.getPiece(z,p).isWhite()){
                                            jumpsonly = true;
                                           
                                        }
                                         ArrayList<Coordinate> allmoves = board.getPiece(z,p).getMoves(board);
                                        if (allmoves.size()!=0 && board.getPiece(z,p).isWhite()){
                                                 now = board.pattern[z][p].getBackground();
                                                 board.pattern[z][p].setBackground(Color.GREEN);
                                            }
                                        //  if (jumpsonly){
                                        //      // allmoves2 = board.getPiece(z,p).getJumpMoves(board);
                                        //     allmoves2 = board.getPiece(z,p).getJumpMoves(board);
                                        //     if (allmoves2.size()!=0 && board.getPiece(z,p).isWhite()){
                                        //          now = board.pattern[z][p].getBackground();
                                        //          board.pattern[z][p].setBackground(Color.GREEN);
                                        //     }
                                        // }
                                        // else{
                                        //     ArrayList<Coordinate> allmoves = board.getPiece(z,p).getMoves(board);
                                        //     if (allmoves.size()!=0 && board.getPiece(z,p).isWhite()){
                                        //          now = board.pattern[z][p].getBackground();
                                        //          board.pattern[z][p].setBackground(Color.GREEN);
                                        //     }
                                        // }
                                    }
                                }
                                       
                                



                                //         if (allmoves2.size()!=0){
                                //             jumpsonly=true;
                                //               now = board.pattern[z][p].getBackground();
                                //                 board.pattern[z][p].setBackground(Color.GREEN);
                                //         }
                                //     }
                                // }
                           //       ArrayList<Coordinate> allmoves2 = board.getPiece(x,y).getJumpMoves(board);
                           //     if (jumpsonly){
                           //          for (Coordinate c: allmoves2){
                           //              now = board.pattern[c.getx()][c.gety()].getBackground();
                           //              board.pattern[c.getx()][c.gety()].setBackground(Color.GREEN);
                           //          }
                           //     }
                           //    else{
                           //      for (Coordinate c: allmoves){
                           //          now = board.pattern[c.getx()][c.gety()].getBackground();
                           //          board.pattern[c.getx()][c.gety()].setBackground(Color.GREEN);
                           //      }
                           // }
                            win(board);
                            if (blackwin){
                                 JOptionPane.showMessageDialog(new JFrame(),"BLACK WINS");
                            }
                            else if (whitewin){
                                 JOptionPane.showMessageDialog(new JFrame(),"WHITE WINS");
                            }
                           
                           

                        }
                    }
                }
            }
        }
          else if(singleblack) {
            boolean jumpsonly = false;
           for (int x = 0; x < 8 ; x++){
                for (int y = 0; y < 8; y++){
                    if (event.getSource() == board.pattern[x][y]){
                        if (start == null && !turn){
                            if (!(board.getPiece(x,y) instanceof NullPiece) && !board.getPiece(x,y).isWhite()){
                                // now=board.pattern[x][y].getBackground();
                                // board.pattern[x][y].setBackground(Color.GREEN);
                                start = new Coordinate(x,y);

                                   ArrayList<Coordinate> allmoves = board.getPiece(x,y).getMoves(board);
                            for (int z = 0; z < 8 ; z++){
                                    for (int p = 0; p < 8; p++){
                                        ArrayList<Coordinate> allmoves2=new ArrayList<Coordinate>();
                                        if (!board.getPiece(z,p).isWhite()){
                                            allmoves2 = board.getPiece(z,p).getJumpMoves(board);
                                        }
                                        if (allmoves2.size()!=0){
                                            jumpsonly=true;
                                        }
                                    }
                                }
                                 ArrayList<Coordinate> allmoves2 = board.getPiece(x,y).getJumpMoves(board);
                               if (jumpsonly){
                                    for (Coordinate c: allmoves2){
                                        now = board.pattern[c.getx()][c.gety()].getBackground();
                                        board.pattern[c.getx()][c.gety()].setBackground(Color.GREEN);
                                    }
                               }
                              else{
                                for (Coordinate c: allmoves){
                                    now = board.pattern[c.getx()][c.gety()].getBackground();
                                    board.pattern[c.getx()][c.gety()].setBackground(Color.GREEN);
                                }
                           }
                            }
                        }
                        else if(!(board.getPiece(x,y) instanceof NullPiece) && !board.getPiece(x,y).isWhite() && !turn && !board.retJump() ){
                            clearBackground();
                            // now = board.pattern[x][y].getBackground();
                            // board.pattern[x][y].setBackground(Color.GREEN);
                            start = new Coordinate(x,y);
                            

                               // ArrayList<Coordinate> allmoves = board.getPiece(x,y).getMoves(board);
                               //  for (Coordinate c: allmoves){
                               //      now = board.pattern[c.getx()][c.gety()].getBackground();
                               //      board.pattern[c.getx()][c.gety()].setBackground(Color.GREEN);
                               //  }
                            for (int z = 0; z < 8 ; z++){
                                    for (int p = 0; p < 8; p++){
                                        ArrayList<Coordinate> allmoves2=new ArrayList<Coordinate>();
                                        if (!board.getPiece(z,p).isWhite()){
                                            allmoves2 = board.getPiece(z,p).getJumpMoves(board);
                                        }
                                        if (allmoves2.size()!=0 && !board.getPiece(z,p).isWhite()){
                                            jumpsonly=true;
                                        }
                                    }
                                }
                                 ArrayList<Coordinate> allmoves2 = board.getPiece(x,y).getJumpMoves(board);
                               if (jumpsonly){
                                    for (Coordinate c: allmoves2){
                                        now = board.pattern[c.getx()][c.gety()].getBackground();
                                        board.pattern[c.getx()][c.gety()].setBackground(Color.GREEN);
                                    }
                               }
                              else{
                                ArrayList<Coordinate> allmoves = board.getPiece(x,y).getMoves(board);
                                for (Coordinate c: allmoves){
                                    now = board.pattern[c.getx()][c.gety()].getBackground();
                                    board.pattern[c.getx()][c.gety()].setBackground(Color.GREEN);
                                }
                           }
                        }
                        
                        
                        else{
                            if (end == null){
                                board.visualize();
                                if (board.pattern[x][y].getBackground() != Color.GREEN){
                                    System.out.println("Not Legal Move");
                                }
                                else{
                                    //clearBackground();
                                   //System.out.println("WHAAT IS HAPPENING");
                                    end = new Coordinate(x,y);
                                     Move m = new Move(start, end);
                                     System.out.println(start);
                                     System.out.println(end);
                                    if (board.movePiece(m.getStart(), m.getEnd())){
                                        
                                        if (end.gety()==7 && board.getPiece(x,y).isWhite()){
                                            board.upgrade(board.getPiece(x,y));
                                        }
                                        else if (end.gety()==0 && !board.getPiece(x,y).isWhite()){
                                            board.upgrade(board.getPiece(x,y));
                                        }
                                        System.out.println(board.retJump());
                                        if (!board.retJump()){
                                            turn=!turn;
                                            startneeded=true;
                                            if (!turn){
                                                System.out.println("BLACK TURN");
                                            }
                                            else{
                                                System.out.println("WHITE TURN");      
                                            }

                                        }

                                        refresh();
                                      
                                    }
                                    refresh();
                                    if (board.retJump()){
                                        clearBackground();
                                        start = end;
                                        //end= null;
                                        System.out.println("HERE");
                                        ArrayList<Coordinate> tempmoves = board.getPiece(x,y).getJumpMoves(board);
                                        System.out.println(tempmoves);
                                        board.pattern[x][y].doClick();
                                        for(Coordinate c: tempmoves){
                                            board.pattern[c.getx()][c.gety()].setBackground(Color.GREEN);
                                        }
                                        refresh();
                                         startneeded=false;
                                    }
                                }
                            
                            }
                            

                             refresh();
                           end = null;
                            refresh();
                            clearBackground();

                            // if (board.retJump()){
                            //     for (int a = 0; a < 8; a++){
                            //         for (int b = 0; b < 8; b++){
                            //             board.pattern[a][b].setEnabled(false);
                            //         }
                            //     }
                            //     clearBackground();
                            //     for (Coordinate c:board.getPiece(x,y).getJumpMoves(board)){
                            //         JButton g = board.pattern[c.getx()][c.gety()];
                            //         g.setEnabled(true);
                            //     }
                            //     now=board.pattern[x][y].getBackground();
                            //     board.pattern[x][y].setBackground(Color.GREEN);
                            //     start = new Coordinate(x,y);
                                
                            // }
                            // else if (!board.retJump()){
                            //     for (int a = 0; a < 8; a++){
                            //         for (int b = 0; b < 8; b++){
                            //             board.pattern[a][b].setEnabled(true);
                            //         }
                            //     }
                            // }
                            if (turn){
                             clearBackground();
                               starttime = System.nanoTime();

                               if (timeplay){
                                    //    ExecutorService executor = Executors.newSingleThreadExecutor();
                                    // Future<String> future = executor.submit(new Task());

                                    // try {
                                    //     System.out.println("Started..");
                                    //     System.out.println(future.get(5, TimeUnit.SECONDS));
                                    //     System.out.println("Finished!");
                                    // } catch (TimeoutException e) {
                                    //     future.cancel(true);
                                    //     System.out.println("Terminated!");
                                    // }catch (final Exception e) {
                                    //     throw new RuntimeException(e);
                                    // }

                                    // executor.shutdownNow();
                                int depthofsearch = 1;
                                    while (true){
                                        timeover=false;
                                        expandmaxwhite(board,  turn,depthofsearch, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,0);
                                         System.out.println("Searched up to depth: "+depthofsearch);
                                        depthofsearch++;
                                        if (timeover){
                                            break;
                                        }
                                        bestboard2 = bestboard;
                                    }
                                }
                                else if(depthplay){
                                    expandmaxwhite(board,  turn,9, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,0);
                                    bestboard2=bestboard;
                                }
                                if (bestboard2!=null){
                                    board.setBoard(bestboard2.getBoard());

                                }
                                else{
                                    System.out.println("NULLBOARD");
                                }
                               refresh();
                                long end = System.nanoTime();
                                System.out.println("time taken: "+(end-starttime)/1000000);
                                turn=!turn;
                                System.out.println("BLACK TURN");
                            }
                                clearBackground();
                                 for (int z = 0; z < 8 ; z++){
                                    for (int p = 0; p < 8; p++){
                                         ArrayList<Coordinate> allmoves2 = board.getPiece(z,p).getJumpMoves(board);
                                         //ArrayList<Coordinate> allmoves = board.getPiece(x,y).getMoves(board);
                                        if (allmoves2.size()!=0 && !board.getPiece(z,p).isWhite()){
                                            jumpsonly = true;
                                           
                                        }
                                        ArrayList<Coordinate> allmoves = board.getPiece(z,p).getMoves(board);
                                            if (allmoves.size()!=0 && !board.getPiece(z,p).isWhite()){
                                                 now = board.pattern[z][p].getBackground();
                                                 board.pattern[z][p].setBackground(Color.GREEN);
                                            }
                                        //  if (jumpsonly){
                                        //      // allmoves2 = board.getPiece(z,p).getJumpMoves(board);
                                        //     allmoves2 = board.getPiece(z,p).getJumpMoves(board);
                                        //     if (allmoves2.size()!=0 && !board.getPiece(z,p).isWhite()){
                                        //          now = board.pattern[z][p].getBackground();
                                        //          board.pattern[z][p].setBackground(Color.GREEN);
                                        //     }
                                        // }
                                        // else{
                                        //     ArrayList<Coordinate> allmoves = board.getPiece(z,p).getMoves(board);
                                        //     if (allmoves.size()!=0 && !board.getPiece(z,p).isWhite()){
                                        //          now = board.pattern[z][p].getBackground();
                                        //          board.pattern[z][p].setBackground(Color.GREEN);
                                        //     }
                                        // }
                                    }
                                }
                                
                            win(board);
                            if (blackwin){
                                 JOptionPane.showMessageDialog(new JFrame(),"BLACK WINS");
                            }
                            else if (whitewin){
                                 JOptionPane.showMessageDialog(new JFrame(),"WHITE WINS");
                            }
                           

                        }
                    }
                }
            }
        }
    }


public void iterative(int t, Gameboard realbored, boolean startedturn, double alpha, double beta, int truedepth){
   
}

public double expandmaxblack(Gameboard realbored, boolean startedturn, int depth, double alpha, double beta, int truedepth){
            //System.out.println("Expanding black with depth: "+depth);
             endtime = System.nanoTime();
            if (!timeover){
             if ((endtime-starttime)/1000000 > 5000 && timeplay){
                 timeover = true;
                 
                 return 0;
             }
            if (depth == 0){
                return heuristic(realbored, !startedturn);
            }
            else{
                TreeNode<Gameboard> datboard = new TreeNode<Gameboard>(realbored);
                ArrayList<Gameboard> tempted = new ArrayList<Gameboard>();
                if(!startedturn){
        
                    double lmao = Double.NEGATIVE_INFINITY;
            
                     tempted = blackboardstates(realbored);
                
                     double score = lmao;
                     if (truedepth == 0 && tempted.size()==0){
                        System.out.println("WHITE WINS");
                        blackwin = true;
                     }
                    // bestboard = tempted.get(0);
                    for (Gameboard temptation: tempted){
                        datboard.addChild(temptation);
                        lmao = Math.max(lmao, expandmaxblack(temptation, !startedturn, depth-1, alpha, beta, truedepth+1));
        
                        // if (lmao > score && truedepth == 0){
                        //     score = lmao;
                        //     bestboard = temptation;
            
                        // }
                         if (lmao > alpha && truedepth == 0){
                            score = lmao;
                            bestboard = temptation;
            
                        }
                        alpha = Math.max(alpha, lmao);
                       
                        if (beta <= alpha){
                     
                            break;
                        }
                        
                    }
                    return lmao;   
                }
                else{
              
                    double lmao = Double.POSITIVE_INFINITY;
                    double score = lmao;
                    tempted = whiteboardstates(realbored);
                
                    for (Gameboard temptation: tempted){
                        datboard.addChild(temptation);
                        lmao = Math.min(lmao, expandmaxblack(temptation, !startedturn, depth-1, alpha, beta, truedepth+1));
                        //  if (lmao<beta && truedepth == 0){
                        //     score = lmao;
                        //     bestboard = temptation;
            
                        // }
                        beta = Math.min(beta, lmao);
                         
                        if (beta <= alpha){
            
                            break;
                        }
                        
                    }
                    return  lmao;
                }
            }
        }
        return 0;
     
    }


    public double expandmaxwhite(Gameboard realbored, boolean startedturn, int depth, double alpha, double beta, int truedepth){
       
            endtime = System.nanoTime();  
        if (!timeover){
         if ((endtime-starttime)/1000000 > 5000 && timeplay){
             timeover = true;
             return 0;
         }
            if (depth == 0){
               return heuristic(realbored, !startedturn);
            }
            else{
                TreeNode<Gameboard> datboard = new TreeNode<Gameboard>(realbored);
                ArrayList<Gameboard> tempted = new ArrayList<Gameboard>();
                if(startedturn){
                
                    double lmao = Double.NEGATIVE_INFINITY;
                
                     tempted = whiteboardstates(realbored);
                
                     double score = lmao;
                     if (truedepth == 0 && tempted.size()==0){
                        System.out.println("BLACK WINS");
                        blackwin = true;
                     }
                    // bestboard = tempted.get(0);
                    for (Gameboard temptation: tempted){
                        datboard.addChild(temptation);
                        lmao = Math.max(lmao, expandmaxwhite(temptation, !startedturn, depth-1, alpha, beta, truedepth+1));
                    
                        // if (lmao > score && truedepth == 0){
                        //     score = lmao;
                        //     bestboard = temptation;
                          
                        // }
                         if (lmao > alpha && truedepth == 0){
                            score = lmao;
                            bestboard = temptation;
            
                        }
                        alpha = Math.max(alpha, lmao);
                        if (beta <= alpha){
                        
                            break;
                        }
                        
                    }
                     return lmao;   
                }
                else{
               
                    double lmao = Double.POSITIVE_INFINITY;
        
                    tempted = blackboardstates(realbored);
                
                    for (Gameboard temptation: tempted){
                        datboard.addChild(temptation);
                        lmao = Math.min(lmao, expandmaxwhite(temptation, !startedturn, depth-1, alpha, beta, truedepth+1));
                        beta = Math.min(beta, lmao);
                        if (beta <= alpha){
                     
                            break;
                        }
                        
                    }
                    return  lmao;
                }
            }
        }
        return 0;
    
    }


    public ArrayList<Gameboard> blackboardstates(Gameboard newboard){

        Piece[][] holder = newboard.copyOf();
        Gameboard temp = new Gameboard(holder);
        ArrayList<Gameboard> allboards= new ArrayList<Gameboard>();
        boolean breakable = false;
        for (int x = 0; x<8; x++){
            for (int y = 0; y<8; y++){
                if (!(temp.getPiece(x,y) instanceof NullPiece) && !temp.getPiece(x,y).getPlayer().isWhite()){
                
                    
                    ArrayList<Coordinate> allmoves = temp.getPiece(x,y).getMoves(temp);
                    
                    ArrayList<Coordinate>allmoves2  =temp.getPiece(x,y).getJumpMoves(temp);
                    if (allmoves2.size()!=0){
                        for (Coordinate c: allmoves2){
                         
                                Coordinate tempstart = new Coordinate(x,y);
                                
                                if(temp.movePiece(tempstart, c)){
                                    if (c.gety()==0 && !temp.getPiece(c.getx(),c.gety()).isWhite()){
                                        temp.upgrade(temp.getPiece(c.getx(),c.gety()));
                                
                                    }
                                    if (temp.jumpedmove()){
                                        allboards.clear();   
                                        breakable=true;
                                    }
                                    allboards.add(temp);
                                    holder = newboard.copyOf();
                                    temp = new Gameboard(holder);

                                }
                                else if (temp.retJump() && temp.jumpedmove()){
                                    allboards.addAll(blackboardstatespiece(temp, temp.getPiece(c)));
                                    
                                }   
                                if (breakable){
                                    break;
                                }                   
                            }
                    }
                    else{
                            for (Coordinate c: allmoves){
                         
                                Coordinate tempstart = new Coordinate(x,y);
                                
                                if(temp.movePiece(tempstart, c)){
                                    if (c.gety()==0 && !temp.getPiece(c.getx(),c.gety()).isWhite()){
                                        temp.upgrade(temp.getPiece(c.getx(),c.gety()));
                                
                                    }
                                    if (temp.jumpedmove()){
                                        allboards.clear();   
                                        breakable=true;
                                    }
                                    allboards.add(temp);
                                    holder = newboard.copyOf();
                                    temp = new Gameboard(holder);

                                }
                                else if (temp.retJump() && temp.jumpedmove()){
                                    allboards.addAll(blackboardstatespiece(temp, temp.getPiece(c)));
                                    
                                }   
                                if (breakable){
                                    break;
                                }                   
                            }
                    }

                }
                   if (breakable){
                        break;
                    }  
            }
               if (breakable){
                    break;
                }  
        }

        
        return allboards;
    }

    public ArrayList<Gameboard> blackboardstatespiece(Gameboard newboard, Piece p){
        ArrayList<Gameboard> allboards= new ArrayList<Gameboard>();

        Piece[][] holder = newboard.copyOf();
        Gameboard temp = new Gameboard(holder);
        if (!(p instanceof NullPiece) && !p.getPlayer().isWhite()){
            ArrayList<Coordinate> allmoves = p.getJumpMoves(temp);
        
            for (Coordinate c: allmoves){
                boolean breakable = false;

                Coordinate tempstart = new Coordinate(p.getx(), p.gety());
                if(temp.movePiece(tempstart, c)){
                
                    if (c.gety()==0 && !p.isWhite()){
                        temp.upgrade(temp.getPiece(c.getx(),c.gety()));
                        
                    }
                    if (temp.jumpedmove()){
                        allboards.clear();   
                        breakable=true;
                    }
                    allboards.add(temp);
                    holder = newboard.copyOf();
                    temp = new Gameboard(holder);
                }
                else if(temp.retJump() && temp.jumpedmove()){
                    allboards=blackboardstatespiece(temp, temp.getPiece(c));
                }
                if (breakable){
                    break;
                } 
                
            }
        }
    
        return allboards;
    }

    public ArrayList<Gameboard> whiteboardstates(Gameboard newboard){

        Piece[][] holder = newboard.copyOf();
        Gameboard temp = new Gameboard(holder);
        ArrayList<Gameboard> allboards= new ArrayList<Gameboard>();
        boolean breakable = false;
        for (int x = 0; x<8; x++){
            
            for (int y = 0; y<8; y++){
                
                if (!(temp.getPiece(x,y) instanceof NullPiece) && temp.getPiece(x,y).getPlayer().isWhite()){
                    ArrayList<Coordinate> allmoves = temp.getPiece(x,y).getMoves(temp);
                    ArrayList<Coordinate>allmoves2  =temp.getPiece(x,y).getJumpMoves(temp);

                    if (allmoves2.size()!=0){
                            for (Coordinate c: allmoves2){
                
                                
                                Coordinate tempstart = new Coordinate(x,y);
                                if(temp.movePiece(tempstart, c)){
                                    
                                
                                    if (c.gety()==7 && temp.getPiece(x,y).isWhite()){
                                        temp.upgrade(temp.getPiece(c.getx(), c.gety()));
                                    }
                                    
                                    if (temp.jumpedmove()){
                                        allboards.clear();   
                                        breakable=true;
                                    }
                                    allboards.add(temp);
                                    holder = newboard.copyOf();
                                    temp = new Gameboard(holder);
                                }
                                else if (temp.retJump() && temp.jumpedmove()){
                                    
                                    allboards.addAll(whiteboardstatespiece(temp, temp.getPiece(c)));
                                    
                                    
                                }
                                if (breakable){
                                    break;
                                }   
                                
                            }
                    }
                    else{
                                for (Coordinate c: allmoves){
                    
                                    
                                    Coordinate tempstart = new Coordinate(x,y);
                                    if(temp.movePiece(tempstart, c)){
                                        
                                    
                                        if (c.gety()==7 && temp.getPiece(x,y).isWhite()){
                                            temp.upgrade(temp.getPiece(c.getx(), c.gety()));
                                        }
                                        
                                        if (temp.jumpedmove()){
                                            allboards.clear();   
                                            breakable=true;
                                        }
                                        allboards.add(temp);
                                        holder = newboard.copyOf();
                                        temp = new Gameboard(holder);
                                    }
                                    else if (temp.retJump() && temp.jumpedmove()){
                                        
                                        allboards.addAll(whiteboardstatespiece(temp, temp.getPiece(c)));
                                        
                                        
                                    }
                                    if (breakable){
                                        break;
                                    }   
                                    
                                }
                        }

                    
                }
                   if (breakable){
                        break;
                    }  
                
            }
               if (breakable){
                    break;
                }  

        }


        return allboards;
    }

    public ArrayList<Gameboard> whiteboardstatespiece(Gameboard newboard, Piece p){
          Piece[][] holder = newboard.copyOf();
        Gameboard temp = new Gameboard(holder);
        ArrayList<Gameboard> allboards= new ArrayList<Gameboard>();
        if (!(p instanceof NullPiece) && p.getPlayer().isWhite()){
            ArrayList<Coordinate> allmoves = p.getJumpMoves(temp);

            for (Coordinate c: allmoves){
        
                boolean breakable = false;        
                Coordinate tempstart = new Coordinate(p.getx(), p.gety());
                if(temp.movePiece(tempstart, c)){
    
                    if (c.gety()==7 && p.isWhite()&& !temp.retJump()){
                        temp.upgrade(temp.getPiece(c.getx(),c.gety()));
                    }
                    if (temp.jumpedmove()){
                        allboards.clear();   
                        breakable=true;
                    }
                    allboards.add(temp);
                    holder = newboard.copyOf();
                    temp = new Gameboard(holder);
                }
                else if(temp.retJump()){
                    allboards=whiteboardstatespiece(temp, temp.getPiece(c));
                }
                if (breakable){
                    break;
                } 

            }
        }
    
        return allboards;
    }

    public void win(Gameboard boarded){
         int blackcount = 0;
        int whitecount = 0;
        for (int x = 0; x< 8; x++){
            for (int y = 0; y< 8; y++){
                if (!(boarded.getPiece(x,y) instanceof NullPiece) && boarded.getPiece(x,y).getPlayer().isWhite()){
                    whitecount++;
                }
                else if (!(boarded.getPiece(x,y) instanceof NullPiece) && !boarded.getPiece(x,y).getPlayer().isWhite()){
                    blackcount++;
                }
            }

        }
         if (whitecount<1){
            blackwin = true; 
        }
        else if(blackcount<1){
            whitewin = true;
        }
    }

     public double heuristic(Gameboard boarded, boolean startedturn){
        int blackcount = 0;
        int whitecount = 0;
    	double blackvalue=0;
    	double whitevalue=0;
    	double retval;
    	for (int x = 0; x< 8; x++){
    		for (int y = 0; y< 8; y++){
    			if (!(boarded.getPiece(x,y) instanceof NullPiece) && boarded.getPiece(x,y).getPlayer().isWhite()){
                    whitecount++;
    				whitevalue+=1;
    				if (boarded.getPiece(x,y).isKinged()){
    					whitevalue+=0.75;
    				}
                    if (y!=7 && y!=0 && x!=0 && x!= 7){
                        if (((boarded.getPiece(x-1,y-1) instanceof NullPiece) && !(boarded.getPiece(x+1,y+1).isWhite())) || ((boarded.getPiece(x+1,y-1) instanceof NullPiece) && !(boarded.getPiece(x-1,y+1).isWhite()))){
                            whitevalue-=10;
                        }
                         if (((boarded.getPiece(x-1,y+1) instanceof NullPiece) && !(boarded.getPiece(x+1,y-1).isWhite())&& (boarded.getPiece(x+1,y-1).isKinged())) || ((boarded.getPiece(x+1,y+1) instanceof NullPiece) && !(boarded.getPiece(x-1,y-1).isWhite())&& (boarded.getPiece(x-1,y-1).isKinged()))){
                            whitevalue-=10;
                        }
                        if (((boarded.getPiece(x-1,y-1) instanceof NullPiece) && (boarded.getPiece(x-1,y-1).isWhite()))  || ((boarded.getPiece(x+1,y-1) instanceof NullPiece) && (boarded.getPiece(x+1,y-1).isWhite()))) {
                            whitevalue+=3;
                        }
                    }
                    // if (x==0 || x== 7){
                    //     whitevalue+=0.2;
                    // }
                    // if (y==0){
                    //     whitevalue+=0.2;
                    // }
    			}
    			else if (!(boarded.getPiece(x,y) instanceof NullPiece) && !boarded.getPiece(x,y).getPlayer().isWhite()){
                    blackcount++;
    				blackvalue+=1;
    				if (boarded.getPiece(x,y).isKinged()){
    					blackvalue+=0.75;
    				}
                    if (y!=7 && y!=0 && x!=0 && x!= 7){
                        if (((boarded.getPiece(x-1,y+1) instanceof NullPiece) && (boarded.getPiece(x+1,y-1).isWhite())) || ((boarded.getPiece(x+1,y+1) instanceof NullPiece) && (boarded.getPiece(x-1,y-1).isWhite()))){
                            blackvalue-=10;
                        }
                        if (((boarded.getPiece(x-1,y-1) instanceof NullPiece) && (boarded.getPiece(x+1,y+1).isWhite()) && (boarded.getPiece(x+1,y+1).isKinged())) || ((boarded.getPiece(x+1,y-1) instanceof NullPiece) && (boarded.getPiece(x-1,y+1).isWhite())&& (boarded.getPiece(x-1,y+1).isKinged()))){
                            blackvalue-=10;
                        }
                        if ((!(boarded.getPiece(x-1,y+1) instanceof NullPiece) &&  !(boarded.getPiece(x-1,y+1).isWhite())) || (!(boarded.getPiece(x+1,y+1) instanceof NullPiece) && !(boarded.getPiece(x+1,y+1).isWhite())) ){
                            blackvalue+=3;
                        }
                    }
                    // if (x==0 || x== 7){
                    //     blackvalue+=0.2;
                    // }
                    // if (y==7){
                    //     blackvalue+=0.2;
                    // }

    			}
    		}
    	}
        if (whitecount>blackcount){
            blackvalue-=50;
            whitevalue +=50;
        }
        else if(blackcount>whitecount){
            whitevalue-=50;
            blackvalue+=50;
        }
        else if (whitecount==blackcount && startedturn){
            whitevalue-=2;
        }
        else if(whitecount==blackcount && !startedturn){
            blackvalue-=2;
        }

    	if (startedturn){
    		retval=whitevalue;
    	}
    	else{
    		retval= blackvalue;
    	}
    	return retval;

    }

	public void refresh(){
	    mainboard.removeAll();
	    for(int y = 0; y< 8; y++){             
			for(int x = 0;x < 8; x++){	      
			    ImageIcon icon=board.getBoard()[x][7-y].getAvatar();
			    board.pattern[x][7-y].setIcon(icon);
			    mainboard.add(board.pattern[x][7-y]);
			}
	    }
	
		mainboard.validate();
		mainboard.repaint();
    }  

    public void clearBackground(){
		for (int x=0; x<8;x++){
		    for(int y=0;y<8;y++){
                 ArrayList<Coordinate> allmovespos = board.getPiece(x,y).getMoves(board);
				if (board.pattern[x][y].getBackground().equals(Color.GREEN) && ((board.getPiece(x,y) instanceof NullPiece) || allmovespos.size()==0)){
				    board.pattern[x][y].setBackground(now);
				}
		    }
		}
    }
}