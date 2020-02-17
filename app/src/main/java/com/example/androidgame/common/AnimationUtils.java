package com.example.androidgame.common;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.graphics.Bitmap;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;

import java.io.IOException;
import java.io.InputStream;

public class AnimationUtils {

    public static class AnimationTools{

        public static int countSprites(InputStream in)throws XmlPullParserException, IOException{
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();

            myparser.setInput(in,null);

            int event = myparser.getEventType();
            int iterator=0;
            while (event != XmlPullParser.END_DOCUMENT){
                String name = myparser.getName();
                switch (event){

                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.END_TAG:
                        if (name.equals("sprite")){
                            iterator ++;


                        }
                        break;

                }
                event=myparser.next();
            }
            return iterator;
        }

        public static Rect[] readAnimXML(Context context, String fileName){
            Rect[] frame = new Rect[0];
            int spriteCount;
            InputStream targetStream = null;
            try {
                targetStream = context.getAssets().open(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                spriteCount = countSprites(targetStream);
                targetStream.close();
                targetStream = context.getAssets().open(fileName);
                frame = new Rect[spriteCount];
                getFrames(targetStream,frame);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return frame;
        }

        public static void getFrames(InputStream in, Rect[] frame)throws XmlPullParserException, IOException {

            float x = 0,y =0,w=0, h= 0;


            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();

            myparser.setInput(in,null);

            int event = myparser.getEventType();
            int iterator=0;
            while (event != XmlPullParser.END_DOCUMENT){
                String name = myparser.getName();
                switch (event){

                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.END_TAG:
                        if (name.equals("sprite")){
                            x = Float.parseFloat(myparser.getAttributeValue(null,"x"));
                            y = Float.parseFloat(myparser.getAttributeValue(null,"y"));
                            w = Float.parseFloat(myparser.getAttributeValue(null,"w"));
                            h = Float.parseFloat(myparser.getAttributeValue(null,"h"));
                            int scaleW=(int)Math.ceil(1);
                            //Log.d("ScaleW", "ScaleW is "+scaleW);
                            int scaleH=(int)Math.ceil(1);
                            //Log.d("ScaleW", "ScaleH is "+scaleH);

                            frame[iterator]=new Rect((int)x, (int) y,(int)(x + w),(int)(y + h));
                            //frame[iterator]= new Rect((int)x,(int)y,x+w,y+h);
                            iterator ++;

                            //Log.d("XMLREAD","Event is"+x +" Iterator is" + iterator);

                        }
                        break;

                }
                event=myparser.next();
                //Log.d("XMLREAD","Event is" + event + "Iterator is "+ iterator);



            }
        }
    }
}
