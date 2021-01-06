/*


 */

import java.awt.*;
import framebuffer.FrameBuffer;

public class Hw_1
{
    public static void main(String[] args)
    {
        // Check for a file name on the command line.
        if ( 0 == args.length )
        {
            System.err.println("Usage: java Hw_1 <PPM-file-name>");
            System.exit(-1);
        }

        // This framebuffer holds the image that will be embedded
        // within a viewport of our larger framebuffer.
        FrameBuffer fbEmbedded = new FrameBuffer( args[0] );

        /******************************************/

        Color cursor = new Color(192, 56, 14);
        FrameBuffer checkPatternBoard = new FrameBuffer(1000, 600, cursor); // sets checkerBoard to be the darker red color
        cursor = new Color(255, 189, 96); //change cursor to beige color
        //start recoloring the other checkerboxes
        int offset; //offset to determine if row is even or not
        for (int ycounter = 0; ycounter <600; ycounter++)
        {
            if (((ycounter /100) % 2) == 0) //checks if the row is even or not
                offset = 0;
            else offset = 100;
            for (int xcounter = 0; xcounter < 1000; xcounter++)
            {
                checkPatternBoard.setPixelFB(xcounter + offset, ycounter, cursor);
                if ((xcounter % 100 == 99) && xcounter != 0)
                    xcounter += 100;
            }
        }
        // Your code goes here.
        // Create a framebuffer. Fill it with the checkerboard pattern.
        // Create a viewport to hold the given PPM file. Put the PPM file in the viewport.
        FrameBuffer.Viewport rebelTrooper = checkPatternBoard.new Viewport(75, 125, fbEmbedded);
        // Create another viewport and fill it with a flipped copy of the PPM file.

        // switch the pixels in the framebuffer before drawing it in the Viewport
        for (int i = 0; i < 255; i++)
        {
            for(int j = 0; j < 128; j++)
            {
                // write out the x lines in reverse order
                Color tempHold = fbEmbedded.getPixelFB(j,i);
                fbEmbedded.setPixelFB(j,i,fbEmbedded.getPixelFB(256-j,i));
                fbEmbedded.setPixelFB(256-j,i,tempHold);
            }
        }

        FrameBuffer.Viewport reverseTrooper = checkPatternBoard.new Viewport(331, 125, fbEmbedded);

        // Create another viewport and fill it with the striped pattern.

        // make a new FrameBuffer

        FrameBuffer stripesFB = new FrameBuffer(300, 120);
        // Create the new colors + buffer
        int buffer = 0;
        Color red = new Color(241,95,116);
        Color green = new Color (152,203,74);
        Color blue = new Color (84,129,230);
        Color color = red;

        for(int i = 0; i < 120; i++)
        {
            for(int j = 0; j < 300+buffer; j++)
            {
                if(j - buffer > 0)
                    stripesFB.setPixelFB(j - buffer, i, color);
                if((j ) % 30 == 29 && j != 0)
                {
                    if(color == red)
                        color = green;
                    if(color == green)
                        color = blue;
                    if(color == blue)
                        color = red;
                }

            }
            color = red;
            buffer++;
        }

        // create the stripes section
        FrameBuffer.Viewport stripes = stripesFB.new Viewport(601,421,stripesFB);
        Color colorObject = red;
        int buffer2 = 0;

        for(int y = 0; y<120; y++)
        {
            for(int x=0;x<300+buffer2;x++) {
                if (x-buffer2 >= 0)
                {
                    stripesFB.setPixelFB( x-buffer2,y,colorObject);
                }

                if(x%30==0&&x!=0)
                {
                    if(colorObject==blue)
                    {
                        colorObject = red;
                    }
                    else if(colorObject==red)
                    {
                        colorObject=green;
                    }
                    else
                    {
                        colorObject=blue;
                    }
                }
            }
            buffer2++;
            colorObject=red;
        }

        // create a grey border
        FrameBuffer.Viewport border =  .new Viewport()
        // Create another viewport that covers the selected region of the framebuffer.
        // Create another viewport to hold a copy of the selected region.
        // Give this viewport a grayish background color.
        // Create another viewport inside the last one.
        // Copy the selected region's viewport into this last viewport.

        FrameBuffer fb = checkPatternBoard;


        /******************************************/
        // Save the resulting image in a file.
        String savedFileName = "Hw_1.ppm";
        fb.dumpFB2File( savedFileName );
        System.err.println("Saved " + savedFileName);
    }
}
