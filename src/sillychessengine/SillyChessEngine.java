/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sillychessengine;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import static sillychessengine.SillyChessEngine.height;
import static sillychessengine.SillyChessEngine.width;

/**
 *
 * @author Anarghya
 */
public class SillyChessEngine  {

    

//<editor-fold defaultstate="collapsed" desc="houseKeeping">
    /**
     * @param args the command line arguments
     */static int count=0;
     
      //Indicates selection to play whether as black or white
       static int playerColor=-1;//0 is black,1 is white,-1 is uninitialized
     static int counter=0;
     
       //Ignore this shit!!
      static int width;
      static int height;
      
      //the frame that is responsible
      static JFrame frame;
     
      static int movesPlayed=0;//keeps track on the number of moves played
      static boolean checkStatus=false;
      //To constantly keep track of both the kings, 
      //necessary for draw,stalemate,checkmate etc
      static int blackKing,whiteKing;
      //Flag for Castling
      static int whitekingMoved=-1;
static int blackingMoved=-1;//-1 indicates  king not moved,1+ indicates  king moved
      static int blackrookMoved[]={0,0};//first index king side,second index queen side
      static int whiterookMoved[]={0,0};
      //This string contains all set of possible moves at the given moment of time for the computer
      
      
      //To fix miniMax tree depth, increasing this is costly but promotes accuracy of moves
      static int globalDepth=8;
      
      static int stage=1;
      
      //During beginning king's safety is highly prioritised
      static int noChecksty=0;
      
      
      //Movability is the freeness for mobility for pieces without hindering one's own pieces.
      //It is number of squares accessible by a piece*weight/23.76
      static double movability=8*2+3.05*4/23.76;
      
      
      //These are base ratings, get modified by various factors
      //These values are not constant/static they change dynamically throughout This is reference based on valuation done by Max Thomson 1934
      static double pawnWeight=1.00,knightWeight=3.05,bishopWeight=3.58,rookWeight=5.48,queenWeight=9.94,kingWeight=1000000;
      
    private static boolean blackCastled=false;//Castled enhances king's safety
     private static boolean whiteCastled=false;
     //All the image references are dumped here
     
     //</editor-fold>
     
     //<editor-fold defaultstate="collapsed" desc="folder referrences">
     static String nextArrow="next.png";
      static String wallpaper="background.jpg";
      
      
      
      
      
      //</editor-fold>
      
      public static void setFrame(int h, int w){
      width=(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
      height=(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
      System.err.print("width: "+width+" Height:"+height);}
      
      //The crux dataStructure,why String array, well this will be enough though!!:)
      //Note: Later this whole code is going to be converted to bitboards.Still in the works
       static String [][]chessboard={
          {"r","n","b","q","k","b","n","r"},
          {"p","p","p","p","p","p","p","p"},
          {" "," "," "," "," "," "," "," "},
          {" "," "," "," "," "," "," "," "},
          {" "," "," "," "," "," "," "," "},
          {" "," "," "," "," "," "," "," "},
          {"P","P","P","P","P","P","P","P"},
          {"R","N","B","Q","K","B","N","R"}
      };
       
       static boolean pawn[][]={{}};
       static boolean knight[][]={{}};
       static boolean bishop[][]={{}};
       static boolean rook[][]={{}};
       static boolean queen[][]={{}};
       static boolean king[][]={{}};
   
       //<editor-fold defaultstate="collapsed" desc="Initial Values of the board">
     static int pawnBoard[][]={//attribute to http://chessprogramming.wikispaces.com/Simplified+evaluation+function
        { 0,  0,  0,  0,  0,  0,  0,  0},
        {50, 50, 50, 50, 50, 50, 50, 50},
        {10, 10, 20, 30, 30, 20, 10, 10},
        { 5,  5, 10, 25, 25, 10,  5,  5},
        { 0,  0,  0, 20, 20,  0,  0,  0},
        { 5, -5,-10,  0,  0,-10, -5,  5},
        { 5, 10, 10,-20,-20, 10, 10,  5},
        { 0,  0,  0,  0,  0,  0,  0,  0}};
    static int rookBoard[][]={
        { 0,  0,  0,  0,  0,  0,  0,  0},
        { 5, 10, 10, 10, 10, 10, 10,  5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        { 0,  0,  0,  5,  5,  0,  0,  0}};
    static int knightBoard[][]={
        {-50,-40,-30,-30,-30,-30,-40,-50},
        {-40,-20,  0,  0,  0,  0,-20,-40},
        {-30,  0, 10, 15, 15, 10,  0,-30},
        {-30,  5, 15, 20, 20, 15,  5,-30},
        {-30,  0, 15, 20, 20, 15,  0,-30},
        {-30,  5, 10, 15, 15, 10,  5,-30},
        {-40,-20,  0,  5,  5,  0,-20,-40},
        {-50,-40,-30,-30,-30,-30,-40,-50}};
    static int bishopBoard[][]={
        {-20,-10,-10,-10,-10,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5, 10, 10,  5,  0,-10},
        {-10,  5,  5, 10, 10,  5,  5,-10},
        {-10,  0, 10, 10, 10, 10,  0,-10},
        {-10, 10, 10, 10, 10, 10, 10,-10},
        {-10,  5,  0,  0,  0,  0,  5,-10},
        {-20,-10,-10,-10,-10,-10,-10,-20}};
    static int queenBoard[][]={
        {-20,-10,-10, -5, -5,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5,  5,  5,  5,  0,-10},
        { -5,  0,  5,  5,  5,  5,  0, -5},
        {  0,  0,  5,  5,  5,  5,  0, -5},
        {-10,  5,  5,  5,  5,  5,  0,-10},
        {-10,  0,  5,  0,  0,  0,  0,-10},
        {-20,-10,-10, -5, -5,-10,-10,-20}};
    static int kingMidBoard[][]={
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-20,-30,-30,-40,-40,-30,-30,-20},
        {-10,-20,-20,-20,-20,-20,-20,-10},
        { 20, 20,  0,  0,  0,  0, 20, 20},
        { 20, 30, 10,  0,  0, 10, 30, 20}};
    static int kingEndBoard[][]={
        {-50,-40,-30,-20,-20,-30,-40,-50},
        {-30,-20,-10,  0,  0,-10,-20,-30},
        {-30,-10, 20, 30, 30, 20,-10,-30},
        {-30,-10, 30, 40, 40, 30,-10,-30},
        {-30,-10, 30, 40, 40, 30,-10,-30},
        {-30,-10, 20, 30, 30, 20,-10,-30},
        {-30,-30,  0,  0,  0,  0,-30,-30},
        {-50,-30,-30,-30,-30,-30,-30,-50}};
    
    //</editor-fold>
   
    //<editor-fold defaultstate="collapsed" desc="constants">
    
    
    //</editor-fold>
    
    public static void main(String[] args) throws IOException {
        
        
        //<editor-fold defaultstate="collapsed" desc="set parameters">
          height=(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
         width= (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
         
        
         
         
       for(int i=0;i<64;i++){
    if(chessboard[i/8][i%8].equals("k"))blackKing=i;
    if(chessboard[i/8][i%8].equals("K"))whiteKing=i;
}
         //</editor-fold>
         
         
     JFrame startFrame= new JFrame("Welcome to AJ Chess Engine!!") { 
         //initialize the start page
        //<editor-fold defaultstate="collapsed">
  private Image backgroundImage = ImageIO.read(new File(wallpaper));
  private Image next=ImageIO.read(new File(nextArrow));
  public void paint( Graphics g ) { 
    super.paint(g);
    
    g.drawImage(backgroundImage,0,0,width,height,null);
    g.drawImage(next,(int) (width*.72),(int)(height*.24),400,400,null);
    g.drawImage(next,(int) (width*.72),(int)(height*.61),400,400,null);
    g.setFont(new Font("Serif",Font.BOLD,36));
  g.drawString("Play Chess!", (int) (width*.78),(int)(height*.8) );
  g.drawString("Import PGN", (int) (width *.78), (int)(height*.45));//</editor-fold>
    
  }
  
};
     
     
        
     startFrame.setExtendedState(startFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
     startFrame.setVisible(true);
     startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     
     startFrame.addMouseListener(new MouseListener() {
             @Override
             public void mouseClicked(MouseEvent e) {
                 
                if(e.getX()>width-400&&e.getX()<width&&e.getY()>height-250&&e.getY()<height){changeFrame();startFrame.dispose();}
                 if(e.getX()>width-400&&e.getX()<width&&e.getY()>height-500&&e.getY()<height-300){startFrame.dispose();}
                
             }

             @Override
             public void mousePressed(MouseEvent e) {}

             @Override
             public void mouseReleased(MouseEvent e){}

             @Override
             public void mouseEntered(MouseEvent e) {}

             @Override
             public void mouseExited(MouseEvent e ){}
         });
            
        
      
           
    }
    public static void changeFrame(){
       
           frame=new JFrame("Play Chess!");
           frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
           setFrame(frame.getHeight(),frame.getWidth());
           UI map=new UI();
           frame.add(map);
           frame.setVisible(true);
           
           Object[] options={"Black","White"};
           playerColor=JOptionPane.showOptionDialog(null,"Select to Play as White or Black", "Color Selection", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null, options,options[0]);
           if(playerColor==0){
               
                   makeMove(ab(globalDepth, 1000000,-1000000,"", 1,1));//display();
                   movesPlayed++;
                   
                   new Thread(new Painter(false)).start();
           }
    }
    public static String ab(int depth,int beta,int alpha,String move,int player,int miniMax){
 
        //player=1,means player is white and vice versa
        
        String list=possibleMoves(player);
        checkmateStalemate(list, player,depth);
        if (depth==0 || list.length()==0) {return move+(rating(depth,player,list.length()));}
        player=1-player;
        miniMax=1-miniMax;
        for (int i=0;i<list.length();i+=5) {
            
            makeMove(list.substring(i,i+5));
            String returnString=ab(depth-1, beta,alpha, list.substring(i,i+5),player,miniMax);
            int value=Integer.valueOf(returnString.substring(5));
            undoMove(list.substring(i,i+5));
            if (miniMax==0) {if (value<beta) {beta=value; if (depth==globalDepth) {move=returnString.substring(0,5);}}
            } 
            else if(miniMax==1){
                if (value>alpha) {alpha=value; if (depth==globalDepth) {move=returnString.substring(0,5);}}
            }
            if (alpha>=beta) {
                
                if (miniMax==0) {return move+beta;}  return move+alpha;
            }
            } 
        if (miniMax==0) {return move+beta;}return move+alpha;
                
        
    }
    
    public static void flipboard(){
        String temp="";
    for(int i=0;i<32;i++){
    
    temp=chessboard[i/8][i%8];
    chessboard[i/8][i%8]=chessboard[(63-i)/8][(63-i)%8];
    chessboard[7-(i/8)][7-(i%8)]=temp;
    
    }
   
    
    
    }
    
    
    
    public static void makeMove(String move){
        int x1,y1,x2,y2;
        if(move.length()>=4){
        x1=Character.getNumericValue(move.charAt(0));
        x2=Character.getNumericValue(move.charAt(2));
        y1=Character.getNumericValue(move.charAt(1));
        y2=Character.getNumericValue(move.charAt(3));
      
        //illegal move, gets terminated here
        if(!(move.contains("C")||move.contains("Z"))&&" ".equals(chessboard[x1][y1])||"".equals(chessboard[x1][y1])){ return;}
        
        if(!(move.contains("Z")||move.contains("C"))){
        //<editor-fold defaultstate="collapsed" desc="normal moves">
            
    chessboard[x2][y2]=chessboard[x1][y1];
    chessboard[x1][y1]=" ";
    if ("K".equals(chessboard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))])) {
                whiteKing=8*Character.getNumericValue(move.charAt(2))+Character.getNumericValue(move.charAt(3));
                whitekingMoved++;
                
            }
    if ("k".equals(chessboard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))])) {
                blackKing=8*Character.getNumericValue(move.charAt(2))+Character.getNumericValue(move.charAt(3));
                blackingMoved++;
            }
    
    //</editor-fold>
    }
        
    else{
        
        //<editor-fold defaultstate="collapsed" desc="castling or Queen promotion">
        if(move.contains("C")){
            //castling
            if(chessboard[x1][y1].equals("k"))
            //black side
            {
              char side=move.charAt(2);
              switch(side){
                case 'K':chessboard[0][4]=" ";
                          chessboard[0][7]=" ";
                           chessboard[0][6]="k";
                           chessboard[0][5]="r";
                           blackrookMoved[0]++;
                           blackingMoved++;
                           blackKing=6;
                           blackCastled=true;
                    break;
                case 'Q':chessboard[0][4]=" ";
                          chessboard[0][0]=" ";
                           chessboard[0][2]="k";
                           chessboard[0][3]="r";
                           blackrookMoved[1]++;
                           blackingMoved++;
                           blackKing=2;
                           blackCastled=true;
                    
                    break;
            }
            }
            if(chessboard[x1][y1]=="K"){
                
            char side=move.charAt(2);
            switch(side){
                case 'K':chessboard[7][4]=" ";
                          chessboard[7][7]=" ";
                           chessboard[7][6]="K";
                           chessboard[7][5]="R";
                           whiterookMoved[0]++;
                           whiteKing=7*8+6;
                           whitekingMoved++;
                           whiteCastled=true;
                    break;
                case 'Q':chessboard[7][4]=" ";
                          chessboard[7][0]=" ";
                           chessboard[7][2]="K";
                           chessboard[7][3]="R";
                           whiterookMoved[1]++;
                           whitekingMoved++;
                           whiteKing=7*8+2;
                           whiteCastled=true;
                    break;
            
            
            }
        
        }}
        
      if(move.contains("Z")){ 
          
           if(Character.isLowerCase(chessboard[6][x1].charAt(0))){
               
     String oldpiece=Character.toString(move.charAt(2));
     String promoted=Character.toString(move.charAt(3));
    chessboard[7][y1]=promoted;
    chessboard[6][x1]=" ";}
        if(Character.isUpperCase(chessboard[1][x1].charAt(0))){
        String oldpiece=Character.toString(move.charAt(2));
     String promoted=Character.toString(move.charAt(3));
    chessboard[0][y1]=promoted;
    chessboard[1][x1]=" ";
        
        
        }
  
    }
      //</editor-fold>
    }
        }
        display();
    }
    
     public static void undoMove(String move){
      int x1,y1,x2,y2;
      String oldpiece=" ";
   if(move.length()>=4){if(move.length()==5){oldpiece=move.substring(4);} 
    x1=Character.getNumericValue(move.charAt(0));
        x2=Character.getNumericValue(move.charAt(2));
        y1=Character.getNumericValue(move.charAt(1));
        y2=Character.getNumericValue(move.charAt(3));
       
        String promoted="",oldp="";
       
        if(!(move.contains("Z")||move.contains("C"))){ 
            
        String piece=chessboard[x2][y2];
        chessboard[x2][y2]=oldpiece;
        chessboard[x1][y1]=piece;
        if("K".equals(chessboard[x1][y1])){whiteKing=x1*8+y1;if(x1==7 && y1==4)whitekingMoved=-1;}
        if("k".equals(chessboard[x1][y1])){whiteKing=x1*8+y1;if(x1==0 && y1==4)blackingMoved=-1;}
        }
        
        else{
            
            if(move.contains("C")){
                char side=move.charAt(2);
              
            if(x1==0){
            switch(side){
                
                case 'K':chessboard[0][4]="k";
                        chessboard[0][7]="r";
                        chessboard[0][6]=" ";
                          chessboard[0][5]=" ";
                          blackCastled=false;
                          blackingMoved=-1;
                          blackKing=4;
                          blackrookMoved[0]=0;
                    break;
                case 'Q':
                    
                        chessboard[0][4]="k";
                        chessboard[0][0]="r";
                        chessboard[0][2]=" ";
                          chessboard[0][3]=" ";
                          blackCastled=false;
                          blackingMoved=-1;
                          blackKing=4;
                          blackrookMoved[1]=0;
                    break;
            }}
            if(x1==7){
                
             switch(side){
                 case 'K':chessboard[7][4]="K";
                        chessboard[7][7]="R";
                        chessboard[7][6]=" ";
                          chessboard[7][5]=" ";
                          whiteCastled=false;
                          whitekingMoved=-1;
                          whiteKing=8*7+4;
                          whiterookMoved[0]=0;
                     break;
                case 'Q':chessboard[7][4]="K";
                        chessboard[7][0]="R";
                        chessboard[7][2]=" ";
                          chessboard[7][3]=" ";
                          whiteCastled=false;
                          whitekingMoved=-1;
                          whiteKing=8*7+4;
                          whiterookMoved[1]=0;
                    break;
             
             }   
                
            }}
            
            
      if(move.contains("Z")){
           oldp=move.substring(2,3);
     promoted=move.substring(3, 4);
      
       if(Character.isLowerCase(promoted.charAt(0))){
            
      
    chessboard[7][y1]=oldp;
    chessboard[6][x1]="p";}
        if(Character.isUpperCase(promoted.charAt(0))){
        
    chessboard[0][y1]=oldp;
    chessboard[1][x1]="P";
        
        
        }
      
      
      }
        }
        
   }
   display();
     }
     
     
     public static String sortMoves(String list) {
        
        int[] score=new int [list.length()/5];
        for (int i=0;i<list.length();i+=5) {
            
            makeMove(list.substring(i, i+5));
            score[i/5]=-rating(1,1,list.length());
            undoMove(list.substring(i, i+5));
        }
        String newListA="", newListB=list;
        for (int i=0;i<Math.min(6, list.length()/5);i++) {//first few moves only
            int max=-1000000, maxLocation=0;
            for (int j=0;j<list.length()/5;j++) {
                if (score[j]>max) {max=score[j]; maxLocation=j;}
            }
            score[maxLocation]=-1000000;
            newListA+=list.substring(maxLocation*5,maxLocation*5+5);
            newListB=newListB.replace(list.substring(maxLocation*5,maxLocation*5+5), "");
        }
        return newListA+newListB;
    
     }
     public static int materialCount(int color){
         int wbishopCount=0,bBishopCount=0;
         int white=0,black=0;
         
             for(int i=0;i<64;i++){
                 switch(chessboard[i/8][i%8]){
                     case "p":black+=pawnWeight*1000 ;break;
                     case "n":black+=knightWeight*1000;break;
                     case "b":black+=bishopWeight*1000;break;
                     case "r":black+=rookWeight*1000;break;
                     case "q":black+=queenWeight*1000;break;
                     case "P":white+=pawnWeight*1000;break;
                     case "N":white+=knightWeight*1000;break;
                     case "B":white+=bishopWeight*1000;break;
                     case "R":white+=rookWeight*1000;break;
                     case "Q":white+=queenWeight*1000;break;}}
             
             
             
          if(playerColor==1){return white;}
          return black;
     }
     public static void changeMaterialValue(int stage){
     // Beginning of the game m
     switch (stage){
     
         case 1:if(blackingMoved==1&&playerColor==0)noChecksty-=50;if(blackingMoved==1&&playerColor==0)noChecksty-=50;break;
                  
         case 2: knightWeight+=0.98;bishopWeight-=.23;break;
         case 3: pawnWeight+=2.15;queenWeight-=3.56;break;
         default: break;
     
     
     }
     
     
     }
     private static float rateCenter(){
     float temp=0;
     float whitecount=0,blackcount=0;
     for(int i=0;i<64;i++){
         if( i>24&&Character.isLowerCase(chessboard[i/8][i%8].charAt(0))){blackcount++;if(i%8>2&&i%8<4)blackcount++;}
         if( i< 39&&Character.isUpperCase(chessboard[i/8][i%8].charAt(0))){whitecount++;if(i%8>2&&i%8<4)whitecount++;}
         
     }
     if(playerColor==1)temp=blackcount-whitecount;else temp=whitecount-blackcount;
     return temp;}
     
     private static float rateThreat(int depth,int color){
     
      int white=0,black=0;
        int tempPositionC;
        depth+=1;
        if(color==1){
        tempPositionC=whiteKing;
        for (int i=0;i<64;i++) {
            switch (chessboard[i/8][i%8]) {
                case "p": {whiteKing=i; if (!noChecks("w")) {white-=6.4;}}
                    break;
                case "r": {whiteKing=i; if (!noChecks("w")) {white-=50.0;}}
                    break;
                case "k": {whiteKing=i; if (!noChecks("w")) {white-=30.0;}}
                    break;
                case "b": {whiteKing=i; if (!noChecks("w")) {white-=30.0;}}
                    break;
                case "q": {whiteKing=i; if (!noChecks("w")) {white-=90.0;}}
                    break;
            }
        }
        whiteKing=tempPositionC;
        if (!noChecks("w")) {white-=200;}
        white= white/2*depth;
        }
        else{
        tempPositionC=blackKing;
        for (int i=0;i<64;i++) {
            switch (chessboard[i/8][i%8]) {
                case "P": {blackKing=i; if (!noChecks("b")) {black-=6.4;}}
                    break;
                case "R": {blackKing=i; if (!noChecks("b")) {black-=50.0;}}
                    break;
                case "K": {blackKing=i; if (!noChecks("b")) {black-=30.0;}}
                    break;
                case "B": {blackKing=i; if (!noChecks("b")) {black-=30.0;}}
                    break;
                case "Q": {blackKing=i; if (!noChecks("b")) {black-=90.0;}}
                    break;
            }
        }
        blackKing=tempPositionC;
        if (!noChecks("b")) {black-=200;}
        black=black/2*depth;
        
        
        }
        
        if(color==1){return white-black;}
        return black-white;
     }
     private static float rateStructure(int color){
     
         // This function calculates structure anomalies such as double pawn,triple pawn,loose pieces etc.
         
         int loosePieces=0,otherAnomalies=0;
         //number of loose pieces should bring down the rating ,care is taken if 
     for(int i=0;i<64;i++){
     switch(chessboard[i/8][i%8])
     {
         case "p":break;
         case "n":break;
         case "r":break;
         case "b":break;
         
     
     }
     
     }
     return 0;
     
     }
     
     
    public static int rating(int depth,int color,int length){
        
       int rating = 0;
       float centerCoefficient=rateCenter();
       float movabilityCoefficient=length;
       //this will handle only threat to the king.
       float ThreatCoefficient=rateThreat(depth,color);
     //this will handle basic support,lines files.  
       float pawnStructure=rateStructure(color);
       float positionalCoefficient;
       
       float noCheckstyCoefficient=(float) (noChecksty*0.54);
       
       rating=materialCount(color)+(int)centerCoefficient;
       
        stage=determineStage();
        changeMaterialValue(stage);
        switch(stage){
        
            case 1: centerCoefficient=20;break;
            case 2: centerCoefficient=10;break;
            case 3:centerCoefficient=5;break;}
            
                            
        
        
        
        
   
        
    return rating;
    }
    
    
    
    
    
    
    public static int determineStage(){
    int counter=0;
    for(int i=0;i<64;i++){
    switch(chessboard[i/8][i%8]){
    
        case "p":count++;break;
        case "n":count++;break;
        case "b":count++;break;
        case "P":count++;break;
        case "N":count++;break;
        case   "B":count++;break;
        
        
    }
    }
       if(count>20)return 1;
       if(count>8)return 2;
       else return 3;
    }
    
    
    
    
     public static void display(){
         System.out.println("\n");
         for(int i=0;i<8;i++){
         System.out.println(Arrays.toString(chessboard[i]));
         }
     
     }
     
     public static void checkmateStalemate(String list,int color,int depth){
      if(list.length()<4&&depth==globalDepth){
    //checkmate or stalemate
    Object[] options={"Play Again","Close"};
        String message;
        String dummyArg[]={"Repeat Match","welcome"};
        int res;
        
        if(noChecks(color==1?"w":"b"))
        {
            if(color==1&&playerColor==1){
            
        message="Stalemate!! Match drawn! You were made to draw!";}
        else{
        message="Stalemate!! Match drawn! You managed a draw Congrats!";
        }}
        
        else {message="Checkmate!!";
        if(playerColor==0&&color==0||(playerColor==1&&color==1))message+=" You Lose :(";
        else message+=" You Won!! :)";}
        
        res=JOptionPane.showOptionDialog(null,message, "Match Ended!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null, options,options[1]);
        if(res==0){
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            
        try {
            SillyChessEngine.main(dummyArg);
        } catch (IOException ex) {
            Logger.getLogger(SillyChessEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        }
    System.exit(0);
    }
     
     }
    
    
    public static String possibleMoves(int color){
        
    String list="";
    String candidate="";
    for(int i=0;i<64;i++)
    {
        if(color==0){
        switch(chessboard[i/8][i%8])
        {
            case "p":list+=pawnPossibility(i,"b");
                        break;
            
            case "n":list+=knightPossibility(i,"b");break;
        
            
            case "b":list+=bishopPossibility(i,"b");break;
    
            
            case "r":list+=rookPossibility(i,"b");break;
           
            
            case "q":list+=queenPossibility(i,"b");break;
     
            
            case "k":list+=kingPossibility(i,"b");blackKing=i; break;
            
            
            default:break;
        }}
        
        if(color==1){
            switch(chessboard[i/8][i%8])
        {
           
            case "P":list+=pawnPossibility(i,"w"); break;
            
           
            case "N":list+=knightPossibility(i,"w"); break;
            
          
            case "B":list+=bishopPossibility(i,"w"); break;
            
         
            case "R": list+=rookPossibility(i,"w"); break;
            
           
            case "Q":list+=queenPossibility(i,"w"); break;
            
            
            case "K":list+=kingPossibility(i,"w");whiteKing=i; break;
            
            default:break;
        }
        
        
        
        
        }
    
    }

        
    return list;
}
    
   
public static String pawnPossibility(int i,String color)
{
    int c;
    c=color.equals("b")?0:1;
    int temp=c^playerColor;
    //avoiding some serious calculations
    if(i/8==1&&temp==1){
        if(i%8==0||i%8==7)
            return "";
    }
    if(i/8==6&&temp==1){
     if(i%8==0||i%8==7)
            return "";
    }
    
    
    String pawnMoves="";
    String oldpiece;
    String promotedw[]={"Q","N","B","R"};
    String promotedb[]={"q","n","b","r"};
     int row=i/8,col=i%8;
    for(int j=-1;j<=1;j+=2){
        
        //Capture moves for black PAwn
    if(color.equals("b")&&i<48){
    try{
    if(Character.isUpperCase(chessboard[row+1][col+j].charAt(0))){
    oldpiece=chessboard[row+1][col+j];
    chessboard[row][col]=" ";
    chessboard[row+1][col+j]="p";
    if(noChecks("b")){pawnMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row+1)+Integer.toString(col+j)+oldpiece; }
    chessboard[row+1][col+j]=oldpiece;
    chessboard[row][col]="p";
    }
    
    
    }catch(Exception e){}
    
    }
   
    //Capture moves for white pawn
    if(color.equals("w")&&i>15){
    try{
    if(Character.isLowerCase(chessboard[row-1][col+j].charAt(0))){
    
    oldpiece=chessboard[row-1][col+j];
    chessboard[row][col]=" ";
    chessboard[row-1][col+j]="P";
    if(noChecks("w")){pawnMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row-1)+Integer.toString(col+j)+oldpiece; }
    chessboard[row-1][col+j]=oldpiece;
    chessboard[row][col]="P";
    }
    
    }catch(Exception e){}
    
    }

     //Promotion moves for black pawn   
    if(color.equals("b")&&i>=48){
    try{
             for(int k=0;k<4;k++){
    
      //Capture and promote           
      if(Character.isUpperCase(chessboard[row+1][col+j].charAt(0))){
    oldpiece=chessboard[row+1][col+j];
    chessboard[row][col]=" ";
    chessboard[row+1][col+j]=promotedb[k];
    if(noChecks("b")){pawnMoves+=Integer.toString(col)+Integer.toString(col+j)+oldpiece+promotedb[k]+"Z"; }
    chessboard[row+1][col+j]=oldpiece;
    chessboard[row][col]="p";
    }
    //Direct promotion
       if(" ".equals(chessboard[row+1][col])){
       chessboard[row][col]=" ";
       chessboard[row+1][col]=promotedb[k];
        if(noChecks("b")){pawnMoves+=Integer.toString(col)+Integer.toString(col)+" "+promotedb[k]+"Z"; }
    chessboard[row+1][col]=" ";
    chessboard[row][col]="p";
       }     
            }
    
    
    }catch(Exception e){}
    
    }
    
    
   // promotion moves for white pawn
    if(color.equals("w")&&i<16){
    try{
         for(int k=0;k<4;k++){
    if(Character.isLowerCase(chessboard[row-1][col+j].charAt(0))){
    
    oldpiece=chessboard[row-1][col+j];
    chessboard[row][col]=" ";
    chessboard[row-1][col+j]=promotedw[k];
    if(noChecks("w")){pawnMoves+=Integer.toString(col)+Integer.toString(col+j)+oldpiece+promotedw[k]+"Z"; }
    chessboard[row-1][col+j]=oldpiece;
    chessboard[row][col]="P";
    }
         
    //Direct promotion
       if(" ".equals(chessboard[row-1][col])){
       chessboard[row][col]=" ";
       chessboard[row-1][col]=promotedw[k];
        if(noChecks("w")){pawnMoves+=Integer.toString(col)+Integer.toString(col)+" "+promotedw[k]+"Z"; }
    chessboard[row-1][col]=" ";
    chessboard[row][col]="P";
       }     
         
         }
    
    
    }catch(Exception e){}
    
    }
    }
    if(color.equals("b")&& i<48){
        //Single movement black pawn
        try{
    if(" ".equals(chessboard[row+1][col])){
    chessboard[row][col]=" ";
    chessboard[row+1][col]="p";
    if(noChecks("b")){pawnMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row+1)+Integer.toString(col)+" ";}
    chessboard[row+1][col]=" ";
    chessboard[row][col]="p";
    }}catch(Exception e){}
    //Double movement black pawn
     try{
    if(row==1&&" ".equals(chessboard[row+2][col])&&" ".equals(chessboard[row+1][col])){
    chessboard[row][col]=" ";
    chessboard[row+2][col]="p";
    if(noChecks("b")){pawnMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row+2)+Integer.toString(col)+" ";}
    chessboard[row+2][col]=" ";
    chessboard[row][col]="p";
    }}catch(Exception e){}
    
    }
    if(color.equals("w")&& i>15){
        try{
            
              //Single movement white pawn
    if(" ".equals(chessboard[row-1][col])){
    chessboard[row][col]=" ";
    chessboard[row-1][col]="P";
    if(noChecks("w")){pawnMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row-1)+Integer.toString(col)+" ";}
    chessboard[row-1][col]=" ";
    chessboard[row][col]="P";
    }}catch(Exception e){}
        
        //Double movement white
     try{
    if(row==6&&" ".equals(chessboard[row-2][col])&&" ".equals(chessboard[row-1][col])){
    chessboard[row][col]=" ";
    chessboard[row-2][col]="P";
    if(noChecks("w")){pawnMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row-2)+Integer.toString(col)+" ";}
    chessboard[row-2][col]=" ";
    chessboard[row][col]="P";
    }}catch(Exception e){}   
        
        
    }
  
return pawnMoves;
}
public static String rookPossibility(int i,String color)
{
    String rookMoves="";
    
    String oldpiece="";
    int row=i/8,col=i%8;
    int temp=1;
  
    for(int j=-1;j<=1;j+=2)
    {
          try{
         temp=1;
            
        while(" ".equals(chessboard[row+temp*j][col])){
            //System.out.print(" space ,same column");
        oldpiece=chessboard[row+temp*j][col];
        chessboard[row][col]=" ";
        if(color.equals("b")){
        chessboard[row+temp*j][col]="r";
        if(noChecks("b"))rookMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row+temp*j)+Integer.toString(col)+" ";
        chessboard[row+temp*j][col]=oldpiece;
        chessboard[row][col]="r";
        }
        else{chessboard[row+temp*j][col]="R";
        if(noChecks("w"))rookMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row+temp*j)+Integer.toString(col)+" ";
        chessboard[row+temp*j][col]=oldpiece;
        chessboard[row][col]="R";
        
        }
        temp++;}
        
        if(Character.isLowerCase(chessboard[row+temp*j][col].charAt(0))&& color.equals("w")){
        oldpiece=chessboard[row+temp*j][col];chessboard[row][col]=" ";
        chessboard[row][col]=" ";
        chessboard[row+temp*j][col]="R";
        if(noChecks("w"))rookMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row+temp*j)+Integer.toString(col)+oldpiece;
        chessboard[row+temp*j][col]=oldpiece;
        chessboard[row][col]="R";
        }
        
        if(Character.isUpperCase(chessboard[row+temp*j][col].charAt(0))&& color.equals("b")){
        oldpiece=chessboard[row+temp*j][col];chessboard[row][col]=" ";
        chessboard[row+temp*j][col]="r";
        if(noChecks("b"))rookMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row+temp*j)+Integer.toString(col)+oldpiece;
        chessboard[row+temp*j][col]=oldpiece;
        chessboard[row][col]="r";
        }}
          catch(Exception e){}
        
    
    
    
    
    temp=1;
 try{
   
        while(" ".equals(chessboard[row][col+temp*j])){
        oldpiece=chessboard[row][col+temp*j];chessboard[row][col]=" ";
        //System.out.print(" space ,same row");
        if(color.equals("b")){
        chessboard[row][col+temp*j]="r";
        if(noChecks("b"))rookMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row)+Integer.toString(col+temp*j)+" ";
        chessboard[row][col+temp*j]=" ";
        chessboard[row][col]="r";
        }
        else{chessboard[row][col+temp*j]="R";
        if(noChecks("w"))rookMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row)+Integer.toString(col+temp*j)+" ";
        chessboard[row][col+temp*j]=" ";
        chessboard[row][col]="R";
        
        }
        temp++;}
        
        if(Character.isLowerCase(chessboard[row][col+temp*j].charAt(0))&& color.equals("w")){
        oldpiece=chessboard[row][col+temp*j];chessboard[row][col]=" ";
        chessboard[row][col+temp*j]="R";
        if(noChecks("w"))rookMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row)+Integer.toString(col+temp*j)+oldpiece;
        chessboard[row][col+temp*j]=oldpiece;
        chessboard[row][col]="R";
        }
        
        if(Character.isUpperCase(chessboard[row][col+temp*j].charAt(0))&& color.equals("b")){
        oldpiece=chessboard[row][col+temp*j];chessboard[row][col]=" ";
        chessboard[row][col+temp*j]="r";
        if(noChecks("b"))rookMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row)+Integer.toString(col+temp*j)+oldpiece;
        chessboard[row][col+temp*j]=oldpiece;
        chessboard[row][col]="r";
        }
        
    
    }
 catch(Exception e){}
    
    }
    
    
    

return rookMoves;

}
public static String bishopPossibility(int i,String Color)
{
    String bishopMoves="";
    String oldpiece="";
    int row=i/8,col=i%8;
    int temp=1;
    for(int j=-1;j<=1;j+=2)
    {
        for(int k=-1;k<=1;k+=2)
        {try{
            
            
            while(" ".equals(chessboard[row+temp*j][col+temp*k]))
            {
                if(Color.equals("b")){
                chessboard[row][col]=" ";
                chessboard[row+temp*j][col+temp*k]="b";
                if(noChecks("b"))bishopMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row+temp*j)+Integer.toString(col+temp*k)+" ";
                chessboard[row][col]="b";
                chessboard[row+temp*j][col+temp*k]=" ";
                }if(Color.equals("w")){
                chessboard[row][col]=" ";
                chessboard[row+temp*j][col+temp*k]="B";
                if(noChecks("w"))bishopMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row+temp*j)+Integer.toString(col+temp*k)+" ";
                chessboard[row][col]="B";
                chessboard[row+temp*j][col+temp*k]=" ";
                }
                
                temp++;
            }
            if(Character.isLowerCase(chessboard[row+temp*j][col+temp*k].charAt(0))&&Color.equals("w")){
            oldpiece=chessboard[row+temp*j][col+temp*k];
            chessboard[row+temp*j][col+temp*k]="B";
            if(noChecks("w"))bishopMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row+temp*j)+Integer.toString(col+temp*k)+oldpiece;
            chessboard[row][col]="B";
            chessboard[row+temp*j][col+temp*k]=oldpiece;
            }if(Character.isUpperCase(chessboard[row+temp*j][col+temp*k].charAt(0))&&Color.equals("b")){
            oldpiece=chessboard[row+temp*j][col+temp*k];
            chessboard[row+temp*j][col+temp*k]="b";
            if(noChecks("b"))bishopMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row+temp*j)+Integer.toString(col+temp*k)+oldpiece;
            chessboard[row][col]="b";
            chessboard[row+temp*j][col+temp*k]=oldpiece;
            }
            
            
            
            }catch(Exception e){}
        temp=1;
        }
    }
    
    
    return bishopMoves;
}
public static String knightPossibility(int i,String Color)
{
    String knightMoves="";
    String oldpiece="";
    int row=i/8,col=i%8;
    for(int j=-1;j<=1;j+=2){
    for(int k=-1;k<=1;k+=2){
  
        try{
        if(" ".equals(chessboard[row+j][col+k*2]))
        {
            chessboard[row][col]=" ";oldpiece=" ";
            if(Color.equals("b")){
            chessboard[row+j][col+k*2]="n";
            if(noChecks("b"))knightMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row+j)+Integer.toString(col+k*2)+oldpiece;
            chessboard[row][col]="n";chessboard[row+j][col+k*2]=oldpiece;
                    
            }
            else{
            chessboard[row+j][col+k*2]="N";
            if(noChecks("w"))knightMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row+j)+Integer.toString(col+k*2)+oldpiece;
            chessboard[row][col]="N";chessboard[row+j][col+k*2]=oldpiece;
            }
                
        }
        
        if(Color.equals("b")&&Character.isUpperCase(chessboard[row+j][col+k*2].charAt(0))){
        chessboard[row][col]=" ";oldpiece=chessboard[row+j][col+k*2];
        chessboard[row+j][col+k*2]="n";
        if(noChecks("b"))knightMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row+j)+Integer.toString(col+k*2)+oldpiece;
            chessboard[row][col]="n";chessboard[row+j][col+k*2]=oldpiece;
        }
        if(Color.equals("w")&&Character.isLowerCase(chessboard[row+j][col+k*2].charAt(0))){
        chessboard[row][col]=" ";oldpiece=chessboard[row+j][col+k*2];
        chessboard[row+j][col+k*2]="N";
        if(noChecks("w"))knightMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row+j)+Integer.toString(col+k*2)+oldpiece;
            chessboard[row][col]="N";chessboard[row+j][col+k*2]=oldpiece;
        }
        }catch(Exception e){}
        
        try{
         if(" ".equals(chessboard[row+j*2][col+k]))
        {
            chessboard[row][col]=" ";oldpiece=" ";
            if(Color.equals("b")){
            chessboard[row+j*2][col+k]="n";
            if(noChecks("b"))knightMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row+j*2)+Integer.toString(col+k)+oldpiece;
            chessboard[row][col]="n";chessboard[row+j*2][col+k]=oldpiece;
                    
            }
            else{
            chessboard[row+j*2][col+k]="N";
            if(noChecks("w"))knightMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row+j*2)+Integer.toString(col+k)+oldpiece;
            chessboard[row][col]="N";chessboard[row+j*2][col+k]=oldpiece;
            }
                
        }
        
        if(Color.equals("b")&&Character.isUpperCase(chessboard[row+j*2][col+k].charAt(0))){
        chessboard[row][col]=" ";oldpiece=chessboard[row+j*2][col+k];
        chessboard[row+j*2][col+k]="n";
        if(noChecks("b"))knightMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row+j*2)+Integer.toString(col+k)+oldpiece;
            chessboard[row][col]="n";chessboard[row+j*2][col+k]=oldpiece;
        }
        if(Color.equals("w")&&Character.isLowerCase(chessboard[row+j*2][col+k].charAt(0))){
        chessboard[row][col]=" ";oldpiece=chessboard[row+j*2][col+k];
        chessboard[row+j*2][col+k]="N";
        if(noChecks("w"))knightMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row+j*2)+Integer.toString(col+k)+oldpiece;
            chessboard[row][col]="N";chessboard[row+j*2][col+k]=oldpiece;
        }
        }catch(Exception e){}
    }}
    return knightMoves;
}
public static String kingPossibility(int i,String Color)
{
    
    String kingMoves="";
    String oldpiece="";
    int row=i/8,col=i%8;
    int k;
    
    for(int j=0;j<9;j++)
    {
        try{
     if(j!=4)
         
     {
       
         if(Color.equals("w")&&(Character.isLowerCase(chessboard[row-1+j/3][col-1+j%3].charAt(0))||" ".equals(chessboard[row-1+j/3][col-1+j%3])))
         {
             
             oldpiece=chessboard[row-1+j/3][col-1+j%3];
             chessboard[row][col]=" ";
             chessboard[row-1+j/3][col-1+j%3]="K";
             int kingTemp=whiteKing;
             whiteKing=i+(j/3)*8+j%3-9;
             if(noChecks("w")){
            kingMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row-1+j/3)+Integer.toString(col-1+j%3)+oldpiece;}
             chessboard[row][col]="K";
             whiteKing=kingTemp;
             chessboard[row-1+j/3][col-1+j%3]=oldpiece;
         }
         if(Color.equals("b")&&(Character.isUpperCase(chessboard[row-1+j/3][col-1+j%3].charAt(0))||" ".equals(chessboard[row-1+j/3][col-1+j%3])))
         {
             //System.out.println("Im in black");
             oldpiece=chessboard[row-1+j/3][col-1+j%3];
             chessboard[row][col]=" ";
             chessboard[row-1+j/3][col-1+j%3]="k";
             int kingTemp=blackKing;
                        blackKing=i+(j/3)*8+j%3-9;
             if(noChecks("b")){
             kingMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row-1+j/3)+Integer.toString(col-1+j%3)+oldpiece;}
             chessboard[row][col]="k";
             blackKing=kingTemp;
             chessboard[row-1+j/3][col-1+j%3]=oldpiece;
             
         }
             
     }
    }
    catch(Exception e){
    }
    }
   
    //castling king possibility
    if(Color.equals("b")&&(blackingMoved==-1)){
    if(noChecks("b")){
        //kings side 
        if(blackrookMoved[0]==0&&chessboard[0][7].equals("r")){
            for( k=6;chessboard[0][k].equals(" ");k--);
            
            if(chessboard[0][k].equals("k")){
               
                     
                //castling to kings side
                chessboard[0][6]="k";
                chessboard[0][5]="r";
                chessboard[0][4]=" ";
                chessboard[0][7]=" ";
                int kingtemp=blackKing;
                blackKing=kingtemp/8+6;
                
                if(noChecks("b")){ kingMoves+="04K6C";}
                chessboard[0][4]="k";
                chessboard[0][7]="r";
                chessboard[0][6]=" ";
                chessboard[0][5]=" ";
                blackKing=kingtemp;
            }}
            
            //queens side
             if(blackrookMoved[1]==0&&chessboard[0][0].equals("r")){
            for( k=1;chessboard[0][k].equals(" ");k++);
            if(chessboard[0][k].equals("k")){
                //castling to queens side
                
               chessboard[0][2]="k";
                chessboard[0][3]="r";
                chessboard[0][4]=" ";
                chessboard[0][0]=" ";
                int kingtemp=blackKing;
                blackKing=kingtemp/8+2;
                if(noChecks("b")){kingMoves+="04Q2C";}
                chessboard[0][4]="k";
                chessboard[0][0]="r";
                chessboard[0][2]=" ";
                chessboard[0][3]=" ";  
                blackKing=kingtemp;
                
            }
            }
    
    }
        
    }
     if(Color.equals("w")&&(whitekingMoved==-1)){
        
    if(noChecks("w")){
       
    if(whiterookMoved[0]==0&&chessboard[7][7].equals("R")){
      
         for( k=6;chessboard[7][k].equals(" ");k--);
            if(chessboard[7][k].equals("K")){
             
               
                //castling to kings side
                chessboard[7][6]="K";
                chessboard[7][5]="R";
                chessboard[7][4]=" ";
                chessboard[7][7]=" ";
                int kingtemp=whiteKing;
              
                whiteKing=kingtemp-kingtemp%8+6;
                if(noChecks("w")){kingMoves+="74K6C";}
               
                chessboard[7][4]="K";
                chessboard[7][7]="R";
                chessboard[7][6]=" ";
                chessboard[7][5]=" ";
                whiteKing=kingtemp;
                
            }
        
    }//queen side castling
    if(whiterookMoved[1]==0&&chessboard[7][0].equals("R")){
              

         for( k=1;chessboard[7][k].equals(" ");k++);
            if(chessboard[7][k].equals("K")){
               
                //castling to kings side
                chessboard[7][2]="K";
                chessboard[7][3]="R";
                chessboard[7][4]=" ";
                chessboard[7][0]=" ";
                int kingtemp=whiteKing;
                whiteKing=kingtemp-kingtemp%8+2;
                if(noChecks("w")){kingMoves+="74Q2C";}
                chessboard[7][4]="K";
                chessboard[7][0]="R";
                chessboard[7][2]=" ";
                chessboard[7][3]=" ";
                whiteKing=kingtemp;
            }
        
    }
    
    
    }
    }
  
    return kingMoves;
}
public static String queenPossibility(int i,String Color)
{
    String queenMoves="";
    String oldpiece="";
    int row=i/8,col=i%8;
    int temp=1;
    for(int j=-1;j<=1;j++)
    {
        for(int k=-1;k<=1;k++)
        {try{
            if(j!=0||k!=0){
            
            while(" ".equals(chessboard[row+temp*j][col+temp*k]))
            {
                if(Color.equals("b")){
                chessboard[row][col]=" ";
                chessboard[row+temp*j][col+temp*k]="q";
                if(noChecks("b"))queenMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row+temp*j)+Integer.toString(col+temp*k)+" ";
                chessboard[row][col]="q";
                chessboard[row+temp*j][col+temp*k]=" ";
                }if(Color.equals("w")){
                chessboard[row][col]=" ";
                chessboard[row+temp*j][col+temp*k]="Q";
                if(noChecks("w"))queenMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row+temp*j)+Integer.toString(col+temp*k)+" ";
                chessboard[row][col]="Q";
                chessboard[row+temp*j][col+temp*k]=" ";
                }
                
                temp++;
            }
            if(Character.isLowerCase(chessboard[row+temp*j][col+temp*k].charAt(0))&&Color.equals("w")){
                
              
               
            oldpiece=chessboard[row+temp*j][col+temp*k];
                
            chessboard[row+temp*j][col+temp*k]="Q";
            if(noChecks("w")){queenMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row+temp*j)+Integer.toString(col+temp*k)+oldpiece;}
            chessboard[row][col]="Q";
            chessboard[row+temp*j][col+temp*k]=oldpiece;
            }if(Character.isUpperCase(chessboard[row+temp*j][col+temp*k].charAt(0))&&Color.equals("b")){
            oldpiece=chessboard[row+temp*j][col+temp*k];
            chessboard[row+temp*j][col+temp*k]="q";
            if(noChecks("b")){queenMoves+=Integer.toString(row)+Integer.toString(col)+Integer.toString(row+temp*j)+Integer.toString(col+temp*k)+oldpiece;}
            chessboard[row][col]="q";
            chessboard[row+temp*j][col+temp*k]=oldpiece;
            }
            
            
            
            }}catch(Exception e){temp=1;}
        temp=1;
        }
    }
    
    
 
return queenMoves;
}
public static boolean noChecks(String color){
    int i,j,k;
    int temp=1;
    int brow=blackKing/8,bcol=blackKing%8;
    int wrow=whiteKing/8,wcol=whiteKing%8;
    
    
  
    
    //Check given by Bishop and Queen
    for(i=-1;i<=1;i+=2){
        for(j=-1;j<=1;j+=2)
        {
          try{
              
            while(" ".equals(chessboard[brow+temp*i][bcol+temp*j])&&color.equals("b")){
               temp++;}if(("Q".equals(chessboard[brow+temp*i][bcol+temp*j])&&color.equals("b"))||("B".equals(chessboard[brow+temp*i][bcol+temp*j])&&color.equals("b"))){return false;}
               
          
          } catch(Exception e){temp=1;}
          
              try{
             while(" ".equals(chessboard[wrow+temp*i][wcol+temp*j])&&color.equals("w")){
               temp++;}if(("q".equals(chessboard[wrow+temp*i][wcol+temp*j])&&color.equals("w"))||(chessboard[wrow+temp*i][wcol+temp*j].equals("b")&&color.equals("w"))){
                   return false; }  
               
               }catch(Exception e){temp=1;} temp=1;
        }
    }temp=1;
    
   
    
    //Check Given by Rook and Queen
    for(i=-1;i<=1;i+=2){
    try{
        while(" ".equals(chessboard[brow+i*temp][bcol])&&color.equals("b")){ 
            temp++;}if(chessboard[brow+i*temp][bcol].equals("R")&&color.equals("b")||chessboard[brow+i*temp][bcol].equals("Q")&&color.equals("b")){return false;}
        }catch(Exception e) {temp=1;}    
        temp=1;  
        try{
       while(" ".equals(chessboard[wrow+i*temp][wcol])&&color.equals("w")){ 
            temp++;}if(chessboard[wrow+i*temp][wcol].equals("r")&&color.equals("w")||chessboard[wrow+i*temp][wcol].equals("q")&&color.equals("w")){return false;}   
        }catch(Exception e) {temp=1;}    
       temp=1; 
       try{
       while(" ".equals(chessboard[brow][bcol+i*temp])&&color.equals("b")){ 
            temp++;}if(chessboard[brow][bcol+i*temp].equals("R")&&color.equals("b")||chessboard[brow][bcol+i*temp].equals("Q")&&color.equals("b")){;return false;}
        }catch(Exception e) {temp=1;}    
        temp=1;   
        try{
       while(" ".equals(chessboard[wrow][wcol+i*temp])&&color.equals("w")){ 
            temp++;}if(chessboard[wrow][wcol+i*temp].equals("r")&&color.equals("w")||chessboard[wrow][wcol+i*temp].equals("q")&&color.equals("w")){return false;}       
            
    }catch(Exception e) {temp=1;} 
        temp=1;
      }
    
    temp=1;
   //Check given by Knight
    for( j=-1;j<=1;j+=2){
    for( k=-1;k<=1;k+=2){
        try{
        
        
        if(chessboard[brow+j][bcol+k*2].equals("N")&&color.equals("b")){return false;}
         }catch(Exception e){temp=1;}
        try{
        if(chessboard[wrow+j][wcol+k*2].equals("n")&&color.equals("w")){return false;} }catch(Exception e){temp=1;}
        try{
        if(chessboard[brow+2*j][bcol+k].equals("N")&&color.equals("b")){return false;} }catch(Exception e){temp=1;}
        
        try{if(chessboard[wrow+j*2][wcol+k].equals("n")&&color.equals("w")){return false;} }catch(Exception e){temp=1;}
        
     
    
    }}
    
    
   try{ if(chessboard[brow+1][bcol+1].equals("P")&&color.equals("b"))return false;}catch(Exception e){}
   try{ if(chessboard[wrow-1][wcol+1].equals("p")&&color.equals("w"))return false;}catch(Exception e){}
    try{if(chessboard[brow+1][bcol-1].equals("P")&&color.equals("b"))return false;}catch(Exception e){}
    try{if(chessboard[wrow-1][wcol-1].equals("p")&&color.equals("w"))return false;}catch(Exception e){}
   
    
    //Position Guarded by King
    
    for(i=-1;i<=1;i++){
    for(j=-1;j<=1;j++){
    try{
    if(i!=0||j!=0){
    
    if(color.equals("w")){
    if(chessboard[wrow+i][wcol+j].equals("k"))return false;}
    if(color.equals("b")){
    if(chessboard[brow+i][bcol+j].equals("K"))return false;}
    }
    
    
    }catch(Exception e){}
    
    }
    }
    
    return true;}

}
