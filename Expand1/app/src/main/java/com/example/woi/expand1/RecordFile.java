package com.example.woi.expand1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by woi on 2016/10/16.
 */

public class RecordFile {

    private String name;
    private LinkedList<String> tags;
    private LinkedList<RecordPart> content;

    public RecordFile() {
        content = new LinkedList<RecordPart>();
        tags = new LinkedList<String>();
    }

    public void setname(String s) {
        name = s;
    }

    public void addTag(String s) {
        tags.add(s);
    }

    public void addContent(int i,int type,char c,int size,int color) {
        content.add(i,new RecordPart(type,c,size,color));
    }

    public void deleteContent(int i) {
        if(i < content.size()) {
            content.remove(i);
        }
    }

    public int[] getstyle(int i) {
        int[] style = new int[3];
        style[0] = content.get(i).type;
        style[1] = content.get(i).color;
        style[2] = content.get(i).size;
        return style;
    }

    public void savetofile(File file) {
        try {
            RecordPart rp;
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            //rp = content.get(0);
            oos.writeObject(content);
            //fos.write('l');
            fos.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }




}

class RecordPart implements Serializable {
    public int type;
    public char c;
    public int size;
    public int color;

    public RecordPart(int type,char c,int size,int color) {
        this.type = type;
        this.color = color;
        this.c = c;
        this.size = size;
    }

}