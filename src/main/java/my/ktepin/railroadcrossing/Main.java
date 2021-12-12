/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.ktepin.railroadcrossing;

import my.ktepin.railroadcrossing.requestgen.IRequestGenerateHandler;
import my.ktepin.railroadcrossing.requestgen.RequestStream;

/**
 *
 * @author ktepin
 */
public class Main {
    
    private static long MODELING_TIME = 10000; //время моделирования (мс) 
    
    public static void main(String args[]) {
        
        
        long threadId = Thread.currentThread().getId();
        System.out.println("Thread main " + threadId + " ");
        
        RequestStream rs = new RequestStream();
        rs.startGeneration("Test1", 1000, new IRequestGenerateHandler(){
            @Override
            public void onRequestGenerated(String generatorId){
                long threadId = Thread.currentThread().getId();
                System.out.println("Thread # " + threadId + " is doing this task");
            }
        });
        
        RequestStream rs2 = new RequestStream();
        rs2.startGeneration("Test2", 1000, new IRequestGenerateHandler(){
            @Override
            public void onRequestGenerated(String generatorId){
                long threadId = Thread.currentThread().getId();
                System.out.println("Thread # " + threadId + " is doing this task");
            }
        });
    }
}
