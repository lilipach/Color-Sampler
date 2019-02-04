/*
 * Liliana Pacheco
 * Color Sampler 5/2/2018
 */

package colorsampler;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.event.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class ColorSampler extends JFrame 
{
    /**
     * @param args the command line arguments
     */
   private class ColorData
   {
      public String ColorName;
      public int red;
      public int blue;
      public int green;
      
      ColorData()
      {
            red = 0;
            green = 0;
            blue = 0;
            ColorName = "";
      }
      public ColorData( ColorData other )
      {
          this.ColorName = other.ColorName;
          this.red = other.red;
          this.green = other.green;
          this.blue = other.blue;
      }
      public String ColorTag()
      {
          return ColorName + " " + red + " " + green + " " + blue;
      }
   } 
   
    protected ColorData ColorSample;
    protected ArrayList<ColorData> FileColors;
    protected String Colors_S[];
    protected ColorDisplay ColorBox;
    
    protected int ColorCount;
    protected int ColorIndex = 0;
   
    protected JList Colors_L;
    
    protected JLabel Red_L;
    protected JLabel Green_L;
    protected JLabel Blue_L;
    
    protected JButton RedSub_B;
    protected JButton RedAdd_B;
    protected JButton BlueSub_B;
    protected JButton BlueAdd_B;
    protected JButton GreenSub_B;
    protected JButton GreenAdd_B; 
    
    protected JButton Save_B;
    protected JButton Reset_B;
    
    protected JTextField RedNum_T;
    protected JTextField BlueNum_T; 
    protected JTextField GreenNum_T;
    
    
    public static void main(String[] args) throws IOException
    {  // TODO code application logic here   
       
        try
        {
            new ColorSampler("Color Sampler");   
        }
        catch(IOException e)
        {
            System.out.println("File not found");
            System.exit(0);
        }
    }

    
    private ColorSampler(String title) throws IOException 
    {
        
        super(title);		// call constructor of base class
        addWindowListener(new WindowDestroyer());   
        
        //read file and save data onto array list of colors 
            File inputFile = new File("input.txt");
            if(!inputFile.exists())
            {
                throw new IOException("File not found");
            }
        
            FileInputStream stream = new FileInputStream(inputFile);  
            InputStreamReader reader = new InputStreamReader(stream); 
            StreamTokenizer tokens = new StreamTokenizer(reader); 
            
            FileColors = new ArrayList<ColorData>();

            while (tokens.nextToken() != tokens.TT_EOF) 
            {     
                ColorSample = new ColorData();
                
                ColorSample.ColorName = (String) tokens.sval; 
                tokens.nextToken(); 
                ColorSample.red = (int) tokens.nval; 
                tokens.nextToken(); 
                ColorSample.green = (int) tokens.nval; 
                tokens.nextToken(); 
                ColorSample.blue = (int) tokens.nval;

                System.out.println(ColorSample.ColorName + " " + 
                                   ColorSample.red + " " + ColorSample.green +
                                   " " + ColorSample.blue);  
                                   //above line is like this for printing
                                   
                FileColors.add(ColorSample);
                ColorCount++;   
            }
            stream.close(); 
        //end of file read
        
        //set default selected color to first item 
        ColorSample = new ColorData(FileColors.get(0));
        /*JButton firstListItem = new JButton(new AbstractAction() 
        {
            public void actionPerformed(ActionEvent evt) 
            {
               Colors_L.setSelectedIndex(0);
            }
        });*/
        
        Colors_S = new String[ColorCount];  //make array strings color names 
        for(int i = 0; i < ColorCount; i++)
        {
            Colors_S[i] = (FileColors.get(i)).ColorName;
        }
        
        //Color Dixplay
        ColorBox = new ColorDisplay();
       
        //Color selection list
        Colors_L = new JList();
        Colors_L.addListSelectionListener(new ColorListHandler() );
        
        //labels
        Red_L = new JLabel("Red: ");
        Green_L = new JLabel("Green: ");
        Blue_L = new JLabel("Blue: ");
        
        //buttons default
        RedSub_B = new JButton("-");
        RedAdd_B = new JButton("+");
        RedSub_B.addActionListener( new ColorSubtraction() );
        RedAdd_B.addActionListener( new ColorAddition() );
            
        GreenSub_B = new JButton("-");
        GreenAdd_B = new JButton("+");
        GreenSub_B.addActionListener( new ColorSubtraction() );
        GreenAdd_B.addActionListener( new ColorAddition() );
        
        BlueSub_B = new JButton("-");
        BlueAdd_B = new JButton("+"); 
        BlueSub_B.addActionListener( new ColorSubtraction() );
        BlueAdd_B.addActionListener( new ColorAddition() );
        
        Save_B = new JButton("Save");
        Reset_B = new JButton("Reset");
        Save_B.addActionListener( new ActionHandler() );
        Reset_B.addActionListener( new ActionHandler() );
 
        //text fields default empty
        RedNum_T = new JTextField(String.valueOf(ColorSample.red));
        GreenNum_T = new JTextField(String.valueOf(ColorSample.green));
        BlueNum_T = new JTextField(String.valueOf(ColorSample.blue));
       
        //input buttons and textfield layout for colors
        getContentPane().setLayout(null);
        getContentPane().add(ColorBox); 
      
        getContentPane().add(Colors_L); //no scroll wanted?
        
        getContentPane().add(Red_L);
	getContentPane().add(RedNum_T);
        getContentPane().add(RedSub_B);
        getContentPane().add(RedAdd_B);
        
	getContentPane().add(Green_L);
        getContentPane().add(GreenNum_T);
        getContentPane().add(GreenSub_B);
        getContentPane().add(GreenAdd_B);
      
        getContentPane().add(Blue_L);
        getContentPane().add(BlueNum_T);
        getContentPane().add(BlueSub_B);
        getContentPane().add(BlueAdd_B);
        
        //save and reset 
        getContentPane().add(Save_B);
        getContentPane().add(Reset_B);
          
        setBounds(200, 200, 450, 390);
        ColorBox.setBounds(10, 10, 240, 150);       //display
        Colors_L.setBounds(260, 10, 150, 305 );     //color list
        
        //labels:
        Red_L.setBounds(10, 160, 60, 30);
        Green_L.setBounds(10, 205, 60, 30);
        Blue_L.setBounds(10, 250, 60, 30);
        
        //Text fields
        RedNum_T.setBounds(70, 165, 55, 30);
        GreenNum_T.setBounds(70, 205, 55, 30);
        BlueNum_T.setBounds(70, 245, 55, 30);
        
        RedSub_B.setBounds(130, 165, 55, 28);
        RedAdd_B.setBounds(190, 165, 55, 28);
        GreenSub_B.setBounds(130, 205, 55, 28);
        GreenAdd_B.setBounds(190, 205, 55, 28);
        BlueSub_B.setBounds(130, 245, 55, 28);
        BlueAdd_B.setBounds(190, 245, 55, 28);              
        
        Save_B.setBounds(30, 285, 90, 30);
        Reset_B.setBounds(125, 285, 90, 30);
        
        
        //add data list of colors to the selection list
        Colors_L.setListData(Colors_S);
        
        setVisible(true);
                
    }    

    // Define window adapter                                       
    private class WindowDestroyer extends WindowAdapter 
    {      
        // implement only the function that you want
        public void windowClosing(WindowEvent e) 
        {    
            //write final results to file
            try
            {
                WriteOutput();
            }
            catch (IOException ex)
            {
                System.out.println("File not found");
            }
            System.exit(0);  
        }                  
        
        public void WriteOutput() throws IOException
        {
            File OutputFile = new File("input.txt");
            
            if(!OutputFile.exists())
                throw new IOException("File not found");
            
            FileOutputStream Ostream = new FileOutputStream(OutputFile);
            PrintWriter writer = new PrintWriter(Ostream, true);
           
            for(int i = 0; i < ColorCount; i++)
            {
                writer.println((FileColors.get(i)).ColorTag());
            }
            Ostream.close();
        
        }
        
    }  

    private class ColorSubtraction implements ActionListener 
    {
        public void actionPerformed(ActionEvent e)
        {
           String ChangedText;
           int ColorVal;
           
           if ( e.getSource() == RedSub_B )
           {
              ChangedText = RedNum_T.getText();
              ColorVal = Integer.parseInt(ChangedText) - 5;
              
              if(ColorVal < 0)
              {
                  return;
              }
              else
              {
                  RedNum_T.setText(String.valueOf(ColorVal));
                  ColorSample.red = ColorVal;
              }
           }
           else if ( e.getSource() == GreenSub_B )
           {
              ChangedText = GreenNum_T.getText();
              ColorVal = Integer.parseInt(ChangedText) - 5;
              
              if(ColorVal < 0)
              {
                  return;
              }
              else
              {
                  GreenNum_T.setText(String.valueOf(ColorVal));
                  ColorSample.green = ColorVal;
              }   
           }
           else if ( e.getSource() == BlueSub_B )
           {
              ChangedText = BlueNum_T.getText();
              ColorVal = Integer.parseInt(ChangedText) - 5;
              
              if(ColorVal < 0)
              {
                  return;
              }
              else
              {
                  BlueNum_T.setText(String.valueOf(ColorVal));
                  ColorSample.blue = ColorVal;
              }
           }
           setTitle("Color Sampler*");
           ColorBox.repaint();
        }    
    }
    
    private class ColorAddition implements ActionListener 
    {
        public void actionPerformed(ActionEvent e)
        {
           String ChangedText;
           int ColorVal;
           if ( e.getSource() == RedAdd_B )
           {
              ChangedText = RedNum_T.getText();
              ColorVal = Integer.parseInt(ChangedText) + 5;
              
              if(ColorVal > 255)
              {
                  return;
              }
              else
              {
                  RedNum_T.setText(String.valueOf(ColorVal));
                  ColorSample.red = ColorVal;
              }    
           }
           else if ( e.getSource() == GreenAdd_B )
           {
              ChangedText = GreenNum_T.getText();
              ColorVal = Integer.parseInt(ChangedText) + 5;
              
              if(ColorVal > 255)
              {
                  return;
              }
              else
              {
                  GreenNum_T.setText(String.valueOf(ColorVal));
                  ColorSample.green = ColorVal;
              }
           }
           else if ( e.getSource() == BlueAdd_B )
           {
              ChangedText = BlueNum_T.getText();
              ColorVal = Integer.parseInt(ChangedText) + 5;
              
              if(ColorVal > 255)
              {
                  return;
              }
              else
              {
                  BlueNum_T.setText(String.valueOf(ColorVal));
                  ColorSample.blue = ColorVal;
              }
           }
           setTitle("Color Sampler*");
           ColorBox.repaint();
        }   
    }

    private class ActionHandler implements ActionListener 
    {
        public void actionPerformed(ActionEvent e)
        {
            if( e.getSource() == Save_B)
            {
                //set new color in array  
                FileColors.set(ColorIndex, new ColorData(ColorSample) );    
            }
            else if( e.getSource() == Reset_B)
            {
                //reset the current color to the original in the array
                ColorSample = new ColorData(FileColors.get(ColorIndex));
                ColorBox.repaint();
                RedNum_T.setText(String.valueOf(ColorSample.red));
                GreenNum_T.setText(String.valueOf(ColorSample.green));
                BlueNum_T.setText(String.valueOf(ColorSample.blue));
            }
            setTitle("Color Sampler");
        }
    }    
                                        
    private class ColorListHandler implements ListSelectionListener 
    {      
        public void valueChanged(ListSelectionEvent e)
        {
            if(e.getSource() == Colors_L)
            {    
              if( !e.getValueIsAdjusting() )
	      {
                //get color to display
                ColorIndex = Colors_L.getSelectedIndex();
                ColorSample = new ColorData(FileColors.get(ColorIndex));
                
                //change color numbers
                RedNum_T.setText(String.valueOf(ColorSample.red));
                GreenNum_T.setText(String.valueOf(ColorSample.green));
                BlueNum_T.setText(String.valueOf(ColorSample.blue));
                
                ColorBox.repaint();
              }   
            }
        }
    }
    
    private class ColorDisplay extends JComponent
    {
        public void paint(Graphics graph)
        {
            Dimension d = getSize();
            
            graph.setColor( new Color(ColorSample.red, ColorSample.green, 
                            ColorSample.blue));
                            //line above is set like that for printing
            
            graph.fillRect(1, 1, d.width-2, d.height-2);
        }
    }
}




