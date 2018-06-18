/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sillychessengine;

import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author akjantal
 */
class Painter extends JPanel implements Runnable{

   JFrame frame;
    Boolean time;
    public Painter(Boolean playerTrue){
        time=playerTrue;
    this.frame=SillyChessEngine.frame;
    }
    @Override
    public void run() {
            if(time)frame.repaint(100, 0, 0, frame.getWidth(), frame.getHeight());
            else {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Painter.class.getName()).log(Level.SEVERE, null, ex);
                }
frame.repaint();
            }
           //     paintImmediately(new Rectangle((dragMove.charAt(0)-'0')*heightset, (dragMove.charAt(1)-'0')*widthset, (dragMove.charAt(3)-dragMove.charAt(1)-'0'+1)*widthset,(dragMove.charAt(2)-dragMove.charAt(0)-'0'+1)*heightset));
            }



}