package com.melnikov.searching;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Component
@Scope("prototype")
public class Searcher implements Runnable{

    private Queue queue;

    private String searchString;

    private final String STRING_FOUND_FORMAT = "The string has been found in %s: file name:%s ,path:%s ";

    @Autowired
    public Searcher(Queue queue){
        this.queue = queue;
    }

    public void setSearchString(String searchString){
        this.searchString = searchString;
    }


    public void run() {
        try {
            if(this.searchString == null || this.searchString.equals("")) wait();
            while (true) {
                File f = queue.get();
                if(!f.canRead()) continue;
                if (f.getName().toLowerCase().contains(searchString)) System.out.printf(STRING_FOUND_FORMAT, "the filename", f.getName(), f.getAbsolutePath());
                if (f.isDirectory()) {
                    File[] fileList = f.listFiles();
                    if(fileList == null) continue;
                    for (File file : fileList) {
                        queue.put(file);
                    }
                }else{
                    if(checkContainFile(f)) System.out.printf(STRING_FOUND_FORMAT, "the file content",f.getName(),f.getAbsolutePath());
                }

            }
        } catch (InterruptedException | IOException e) {
           e.printStackTrace();
        }
    }

    private boolean checkContainFile(File f) throws IOException{
        BufferedReader bufferedReader = new BufferedReader(new FileReader(f.getAbsolutePath()));
        String str;
        while((str = bufferedReader.readLine()) != null){
            if(str.toLowerCase().contains(searchString)) return true;
        }
        return false;
    }
}
