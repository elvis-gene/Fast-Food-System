/*
    Elvis Presley Gene (217304338)
    Nico Fortuin (216237912)
 */

import javax.swing.*;
import java.awt.*;

public class RunFastFoodGui {

    public static void main(String [] args){
            //FastFood fastFood = new FastFood();
            createGui();
    }

    public static void createGui()
    {
        FastFoodGui fastFoodGui = new FastFoodGui();

        fastFoodGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fastFoodGui.setSize(800,600);
        fastFoodGui.setVisible(true);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        fastFoodGui.setLocation(dim.width/2-fastFoodGui.getSize().width/2, dim.height/2-fastFoodGui.getSize().height/2);
    }
}
