/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runtime;


import java.io.IOException;




import javax.swing.JOptionPane;


import view.FrmLogin;
/**
 *
 * @author ljourn
 */
public class Main {
    
    
    public static void main(String[] args) throws IOException, Exception {

    	try {

            FrmLogin mv = new FrmLogin();
            mv.setVisible(true);
            
    	} catch (Exception e) {
    		
    		JOptionPane.showMessageDialog(null, e.toString());
    		
    	}
  
    }
    
   
    
}
