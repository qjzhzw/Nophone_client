package com.ForestAnimals.nophone.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by dell on 2016/1/20.
 */
public class FileService
{
    public void save(OutputStream outStream,String content) throws IOException
    {
        outStream.write(content.getBytes());
        outStream.close();
    }
    public String read(InputStream inStream) throws IOException
    {
        ByteArrayOutputStream outStream=new ByteArrayOutputStream();
        byte[] buffer=new byte[1024];
        int len;
        while((len=inStream.read(buffer))!=-1)
        {
            outStream.write(buffer,0,len);
        }
        byte[] data=outStream.toByteArray();
        outStream.close();
        inStream.close();
        return new String(data);
    }
}