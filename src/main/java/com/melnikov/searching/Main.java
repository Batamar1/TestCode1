package com.melnikov.searching;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);

        Thread[] threads = new Thread[6];

        for(int i = 0; i != 6; i++) {
            Searcher searcher = (Searcher)context.getBean("searcher");
            searcher.setSearchString(buildSearchString(args));
            threads[i] = new Thread(searcher);
            threads[i].start();
        }
        checkIsSearching(threads);
    }

    private static String buildSearchString(String[] args){
        StringBuilder searchString = new StringBuilder();
        searchString.append(args[0]);
        for(int i = 1; i < args.length; i++){
            searchString.append(" ").append(args[i]);
        }
        return searchString.toString();
    }

    private static void checkIsSearching(Thread[] threads){
        int count;
        while (true){
            count = 0;
            for(Thread thread : threads){
                if(thread.getState().equals(Thread.State.WAITING)) count++;
                else break;
            }
            if(count == 6) return;
        }
    }

}
