/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sillychessengine;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import static sillychessengine.SillyChessEngine.chessboard;
import static sillychessengine.UI.heightset;
/**
 *
 * @author Anarghya
 */
public class UI extends JPanel implements MouseListener,MouseMotionListener{
//<editor-fold defaultstate="collapsed" desc="house keeping work">
   static int block=0,wlock=0;
    public static int X,Y,X1,Y1,newX,newY;
    static int p=0;
    static int selected=0;
   String list;
    static int flag =0;
    Graphics g;
    static int goaway1=0,goaway2=0;
    static int pr=-1;
    static final int taskbarHeight=120;
    static int height=SillyChessEngine.height;
    static int width=SillyChessEngine.width;
       static int squareLength=width/16;
    static int squareWidth=height/10;
    static int widthset;
    static int heightset;
    static int borders[];
    //</editor-fold>
    @Override
    public void paintComponent(Graphics g){
          
        super.paintComponent(g);
        Image chessPieces;
        Image square;
        Image map = null;
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
   
   int colors[][]={{66,29,9},{206,186,121}};
   borders= new int[2];
   
   int j=-1,k=-1;
   Graphics2D g1=(Graphics2D)g;
   Composite a=g1.getComposite();
   Composite translucent = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.83f);
   g1.setComposite(translucent);

    map=new ImageIcon("chessboard.jpg").getImage();
    g1.drawImage(map, borders[0], borders[1],getWidth(),getHeight(), this);
    g1.setComposite(a);
   borders[0]=width/16;borders[1]=height/18;
   widthset=(width-borders[0])/8;heightset=(height-borders[1]-taskbarHeight)/8;
    for(int i=0;i<64;i++){
   //<editor-fold defaultstate="collapsed"> 
    switch(SillyChessEngine.chessboard[i/8][i%8])
    {
        case "P":chessPieces=new ImageIcon("white_pawn-128.png").getImage();
                g.drawImage(chessPieces,i%8*widthset+borders[0], i/8*heightset+borders[1],squareLength,squareWidth,this) ;
            break;
        case "R":chessPieces=new ImageIcon("white_rook-128.png").getImage();
                g.drawImage(chessPieces,i%8*widthset+borders[0], i/8*heightset+borders[1],squareLength,squareWidth,this) ;
            break;
        case "N":chessPieces=new ImageIcon("white_knight-128.png").getImage();
                g.drawImage(chessPieces,i%8*widthset+borders[0], i/8*heightset+borders[1],squareLength,squareWidth,this) ;
            break;
        case "Q":chessPieces=new ImageIcon("white_queen-128.png").getImage();
                g.drawImage(chessPieces,i%8*widthset+borders[0], i/8*heightset+borders[1],squareLength,squareWidth,this) ;
            break;
        case "B":chessPieces=new ImageIcon("white_bishop-128.png").getImage();
                g.drawImage(chessPieces,i%8*widthset+borders[0], i/8*heightset+borders[1],squareLength,squareWidth,this) ;
            break;
        case "K":chessPieces=new ImageIcon("white_king-128.png").getImage();
                g.drawImage(chessPieces,i%8*widthset+borders[0], i/8*heightset+borders[1],squareLength,squareWidth,this) ;
            break;
        case "p":chessPieces=new ImageIcon("black_pawn-128.png").getImage();
    g.drawImage(chessPieces, i%8*widthset+borders[0], i/8*heightset+borders[1],squareLength,squareWidth, this);
            
            break;
        case "r":chessPieces=new ImageIcon("black_rook-128.png").getImage();
                g.drawImage(chessPieces,i%8*widthset+borders[0], i/8*heightset+borders[1],squareLength,squareWidth,this) ;
            
            break;
        case "n":chessPieces=new ImageIcon("black_knight-128.png").getImage();
    g.drawImage(chessPieces, i%8*widthset+borders[0], i/8*heightset+borders[1],squareLength,squareWidth, this);
            
            
            break;
        case "b":chessPieces=new ImageIcon("black_bishop-128.png").getImage();
                g.drawImage(chessPieces,i%8*widthset+borders[0], i/8*heightset+borders[1],squareLength,squareWidth,this) ;
            break;
        case "q":chessPieces=new ImageIcon("black_queen-128.png").getImage();
                g.drawImage(chessPieces,i%8*widthset+borders[0], i/8*heightset+borders[1],squareLength,squareWidth,this) ;
            break;
        case "k":chessPieces=new ImageIcon("black_king-128.png").getImage();
                g.drawImage(chessPieces,i%8*widthset+borders[0], i/8*heightset+borders[1],squareLength,squareWidth,this) ;
            break;
    
    }
    //</editor-fold>
    }
    int row,col,nextrow = 0,nextcol = 0;
        
    if(flag!=0){
        
    for(int i=0;i<list.length();i+=5){
        
        // this highlites all the possible moves selected forr a piece
        if(!(list.substring(i,i+5).contains("Z")||list.substring(i,i+5).contains("C"))){
            //normal moves
    row=Integer.valueOf(list.charAt(i+0));
     row=row-48;
     col=Integer.valueOf(list.charAt(i+1));col-=48;
     nextrow=Integer.valueOf(list.charAt(i+2));nextrow-=48;
     nextcol=Integer.valueOf(list.charAt(i+3));nextcol-=48;
       }
        
        else{
            if(list .substring(i,i+5).contains("C")){
             row=Integer.valueOf(list.charAt(i))-48;
             nextrow=row;
             col=Integer.valueOf(list.charAt(i+1))-48;
             nextcol=Integer.valueOf(list.charAt(i+3))-48;
             
               
             
            }
            if(list .substring(i,i+5).contains("Z")){
         col=Integer.valueOf(list.charAt(i))-48;
         nextcol=Integer.valueOf(list.charAt(i+1))-48;
     
        if(SillyChessEngine.playerColor==0)
        { row=6;nextrow=7;}
        
        else{row=1;nextrow=0;}
        
        
        }
       }
       try{ 
       square=new ImageIcon("sq.png").getImage();
    translucent = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.65f);
   g1.setComposite(translucent); 
      g1.setComposite(translucent);
    g1.drawImage(square,nextcol*(widthset-14)+borders[0]+40,nextrow*heightset+borders[1],squareLength-10,squareWidth-10,this);
       }catch(Exception e){}
        
        
        
    }}
    
    if(SillyChessEngine.checkStatus==true){
        Font f=new Font("Arial",Font.BOLD,42);
        
    g.setColor(Color.red);
    g.setFont(f);
    g.drawString("CHECK", 880,992);
    
    
    }
    
    }
    
    

    
@Override public void mouseMoved(MouseEvent e){

}
    @Override
    public void mousePressed(MouseEvent e){ 
      
 }
    
    
    
    
    
   
    
    @Override
    public void mouseReleased(MouseEvent e) throws StringIndexOutOfBoundsException{
   if(e.getX()>borders[0]&&e.getX()<width-borders[0]&&e.getY()>borders[1]&&e.getY()<height-borders[1]){
   newX=e.getX();newY=e.getY();
   String possibilities;
   
   if(e.getButton()==MouseEvent.BUTTON1){
         String dragMove ="";
         String response="";
         String oldp="";
         String color="";
         
         //promotion
         
       if((Y-borders[1])/heightset==1&&(newY-borders[1])/heightset==0&&SillyChessEngine.chessboard[(Y-borders[1])/heightset][(X-borders[0])/(widthset)].equals("P")){
           oldp=SillyChessEngine.chessboard[(newY-borders[1])/heightset][(newX-borders[0])/(widthset)];
           //Promotion of white pawn to Queen
           if(SillyChessEngine.playerColor==1&& Character.isUpperCase(SillyChessEngine.chessboard[(Y-borders[1])/heightset][(X-borders[0])/(widthset)].charAt(0))&&goaway1==0){
               String options[]={"Queen","Knight","Rook","Bishop"};goaway1++;
               pr=JOptionPane.showOptionDialog(null,"Select Piece to be Promoted", "Piece Selection", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null, options,options[0]);
              
       dragMove=""+(X-borders[0])/(widthset)+(newX-borders[0])/(widthset)+oldp+options[pr].charAt(0)+"Z";
               
        }}
       
       
       else if((Y-borders[1])/heightset==6&&(newY-borders[1])/heightset==7&&SillyChessEngine.chessboard[(Y-borders[1])/heightset][(X-borders[0])/(widthset)].equals("p")){
           oldp=SillyChessEngine.chessboard[(newY-borders[1])/heightset][(newX-borders[0])/widthset];
           //Promotion of black pawn to Queen
           if(SillyChessEngine.playerColor==0&& Character.isLowerCase(SillyChessEngine.chessboard[(Y-borders[1])/heightset][(X-borders[0])/(widthset)].charAt(0))&&goaway2==0){
               String options[]={"queen","knight","rook","bishop"};goaway2++;
               pr=JOptionPane.showOptionDialog(null,"Select Piece to be Promoted", "Piece Selection", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null, options,options[0]);
       dragMove=""+(X-borders[0])/(widthset)+(newX-borders[0])/(widthset)+oldp+options[pr].charAt(0)+"Z";
                }}
   
       else{
      
           
           //normal move
           
          try{ if((SillyChessEngine.playerColor==1&& Character.isUpperCase(SillyChessEngine.chessboard[(Y-borders[1])/heightset][(X-borders[0])/(widthset)].charAt(0)))||(SillyChessEngine.playerColor==0&& Character.isLowerCase(SillyChessEngine.chessboard[(Y-borders[1])/heightset][(X-borders[0])/(widthset)].charAt(0))))    
       dragMove=""+(Y-borders[1])/heightset+(X-borders[0])/(widthset)+(newY-borders[1])/heightset+(newX-borders[0])/(widthset)+chessboard[(newY-borders[1])/heightset][(newX-borders[0])/(widthset)];}catch(Exception t){}
           
           if(dragMove.equals("7476 ")){dragMove="74K6C";if(wlock==0){wlock++;}}
          if(dragMove.equals("7472 ")){dragMove="74Q2C";if(wlock==0){wlock++;}}
          if(dragMove.equals("0406 ")){dragMove="04K6C";if(block==0){block++;}}
          if(dragMove.equals("0402 ")){dragMove="04Q2C";if(block==0){block++;}}
         
        
       }
       
   
    possibilities=SillyChessEngine.possibleMoves(SillyChessEngine.playerColor);
    if(possibilities.length()<3){SillyChessEngine.checkmateStalemate(possibilities, SillyChessEngine.playerColor, SillyChessEngine.globalDepth);}
        if(!dragMove.equals("")){
           
        if(possibilities.contains(dragMove)){
            //Valid move
            System.out.println("Dragmove:"+dragMove);
                //Castling handling history
       if(!(dragMove.contains("C")||dragMove.contains("Z")))
       { if( SillyChessEngine.chessboard[Integer.valueOf(dragMove.charAt(0))-48][Integer.valueOf(dragMove.charAt(1))-48].equals("R")) {
            //queen side rook 
            if(Integer.valueOf(dragMove.charAt(1))-48==0){SillyChessEngine.whiterookMoved[1]++;}
            //king side rook
            if(Integer.valueOf(dragMove.charAt(1))-48==7){SillyChessEngine.whiterookMoved[0]++;}
            
        }if( SillyChessEngine.chessboard[Integer.valueOf(dragMove.charAt(0))-48][Integer.valueOf(dragMove.charAt(1))-48].equals("r")) {
            //queen side rook
            if(Integer.valueOf(dragMove.charAt(1))-48==0){SillyChessEngine.blackrookMoved[1]++;}
            //king side rook moved
            if(Integer.valueOf(dragMove.charAt(1))-48==7){SillyChessEngine.blackrookMoved[0]++;}
            
        } if(SillyChessEngine.chessboard[Integer.valueOf(dragMove.charAt(0))-48][Integer.valueOf(dragMove.charAt(1))-48].equals("k")){SillyChessEngine.blackingMoved=1;}
        if(SillyChessEngine.chessboard[Integer.valueOf(dragMove.charAt(0))-48][Integer.valueOf(dragMove.charAt(1))-48].equals("K")){SillyChessEngine.whitekingMoved=1;}
       } 
           SillyChessEngine.makeMove(dragMove);
           SillyChessEngine.movesPlayed++;
           color=SillyChessEngine.playerColor==1?"b":"w";
           if(SillyChessEngine.noChecks(color)==false&&SillyChessEngine.checkStatus==false)SillyChessEngine.checkStatus=true;
           color=SillyChessEngine.playerColor==1?"w":"b";
           if(SillyChessEngine.noChecks(color)==true&&SillyChessEngine.checkStatus==true)SillyChessEngine.checkStatus=false;
           SillyChessEngine.display();
           new Thread(new Painter(true)).start();
       
            
            response=SillyChessEngine.ab(SillyChessEngine.globalDepth,100000,-100000,"",1-SillyChessEngine.playerColor,1);
           if(!(response.contains("C")||response.contains("Z"))){
             if( SillyChessEngine.chessboard[Integer.valueOf(response.charAt(0))-48][Integer.valueOf(response.charAt(1))-48].equals("R")) {
            //queen side rook 
            if(Integer.valueOf(response.charAt(1))-48==0){SillyChessEngine.whiterookMoved[1]=1;}
            //king side rook
            if(Integer.valueOf(response.charAt(1))-48==7){SillyChessEngine.whiterookMoved[0]=1;}
            
        }if( SillyChessEngine.chessboard[Integer.valueOf(response.charAt(0))-48][Integer.valueOf(response.charAt(1))-48].equals("r")) {
            //queen side rook
            if(Integer.valueOf(response.charAt(1))-48==0){SillyChessEngine.blackrookMoved[1]=1;}
            //king side rook moved
            if(Integer.valueOf(response.charAt(1))-48==7){SillyChessEngine.blackrookMoved[0]=1;}
            
        } if(SillyChessEngine.chessboard[Integer.valueOf(response.charAt(0))-48][Integer.valueOf(response.charAt(1))-48].equals("k")){SillyChessEngine.blackingMoved=1;}
        if(SillyChessEngine.chessboard[Integer.valueOf(response.charAt(0))-48][Integer.valueOf(response.charAt(1))-48].equals("K")){SillyChessEngine.whitekingMoved=1;}
        }    
            System.err.println("Response:"+response);
             selected =1; 
       SillyChessEngine.makeMove(response);
       
       SillyChessEngine.movesPlayed++;
       color=SillyChessEngine.playerColor==1?"w":"b";
       if(SillyChessEngine.noChecks(color)==false&&SillyChessEngine.checkStatus==false)SillyChessEngine.checkStatus=true;
       color=SillyChessEngine.playerColor==1?"b":"w";
       if(SillyChessEngine.noChecks(color)==true&&SillyChessEngine.checkStatus==true)SillyChessEngine.checkStatus=false;
            new Thread(new Painter(false)).start();
         // SillyChessEngine.display();
            
        }}
   
 }
   }  
   
 }
    


    
    @Override
    public void mouseEntered(MouseEvent e){}
    @Override
    public void mouseDragged(MouseEvent e){}
    @Override
    public void mouseClicked(MouseEvent e)
    {
        
         flag++;
   if(e.getX()>borders[0]&&e.getX()<getWidth()-borders[0]&e.getY()>borders[1]&&e.getY()<getHeight()-borders[1]){

   X=e.getX();Y=e.getY();
try{      
   switch(SillyChessEngine.chessboard[(Y-borders[1])/heightset][(X-borders[0]+40)/(widthset)]){
             case "p":if(SillyChessEngine.playerColor==0)list=""+SillyChessEngine.pawnPossibility((Y-borders[1])/heightset*8+(X-borders[0]+40)/(widthset),"b");break;
             case "P":if(SillyChessEngine.playerColor==1)list=""+SillyChessEngine.pawnPossibility((Y-borders[1])/heightset*8+(X-borders[0]+40)/(widthset),"w");break;
            
            case "n":if(SillyChessEngine.playerColor==0)list=""+SillyChessEngine.knightPossibility((Y-borders[1])/heightset*8+(X-borders[0]+40)/(widthset),"b");break;
            case "N":if(SillyChessEngine.playerColor==1)list=""+SillyChessEngine.knightPossibility((Y-borders[1])/heightset*8+(X-borders[0]+40)/(widthset),"w");break;
          
            
            case "b":if(SillyChessEngine.playerColor==0)list=""+SillyChessEngine.bishopPossibility((Y-borders[1])/heightset*8+(X-borders[0]+40)/(widthset),"b");break;
            case "B":if(SillyChessEngine.playerColor==1)list=""+SillyChessEngine.bishopPossibility((Y-borders[1])/heightset*8+(X-borders[0]+40)/(widthset),"w");break;
            
            case "r":if(SillyChessEngine.playerColor==0)list=""+SillyChessEngine.rookPossibility((Y-borders[1])/heightset*8+(X-borders[0]+40)/(widthset),"b");break;
            case "R":if(SillyChessEngine.playerColor==1)list=""+SillyChessEngine.rookPossibility((Y-borders[1])/heightset*8+(X-borders[0]+40)/(widthset),"w");break;
            
            case "q":if(SillyChessEngine.playerColor==0)list=""+SillyChessEngine.queenPossibility((Y-borders[1])/heightset*8+(X-borders[0]+40)/(widthset),"b");break;
            case "Q":if(SillyChessEngine.playerColor==1)list=""+SillyChessEngine.queenPossibility((Y-borders[1])/heightset*8+(X-borders[0]+40)/(widthset),"w");break;
            
            case "k":if(SillyChessEngine.playerColor==0)list=""+SillyChessEngine.kingPossibility((Y-borders[1])/heightset*8+(X-borders[0]+40)/(widthset),"b");
                    break;
            case "K":if(SillyChessEngine.playerColor==1)list=""+SillyChessEngine.kingPossibility((Y-borders[1])/heightset*8+(X-borders[0]+40)/(widthset),"w");break;
            
            default:break;
   
 }
}catch(Exception t){}
    selected++;    
  new Thread ( new Painter(true)).start();
       
       }
   
   
   
   }
        
     

    
    
    @Override
    public void mouseExited(MouseEvent e){}

}
