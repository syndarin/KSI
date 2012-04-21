/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ksi;

/**
 *
 * @author Syndarin
 */
public class KSI {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                UserForm form=new UserForm();
            }
        });
    }
}
