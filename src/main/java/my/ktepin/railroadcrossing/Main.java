/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.ktepin.railroadcrossing;

import java.util.Timer;
import java.util.TimerTask;
import my.ktepin.railroadcrossing.requestgen.IRequestGenerateHandler;
import my.ktepin.railroadcrossing.requestgen.RequestStream;

/**
 *
 * @author ktepin
 */
public class Main {
    
    //private static long MODELING_TIME = 10000; //время моделирования (мс) 
    private static final long CAR_STREAM1 = 1000;
    private static final long CAR_STREAM2 = 2500;
    private static final long TRAIN_STREAM = 5000;
    
    private static CrossState currentState = CrossState.OPEN;
    
    public static void main(String args[]) {   
        
        RequestStream cars1 = new RequestStream();
        cars1.startGeneration("Car1", CAR_STREAM1, new IRequestGenerateHandler(){
            @Override
            public void onRequestGenerated(String generatorId){
                work(generatorId);
            }
        });
        
        RequestStream cars2 = new RequestStream();
        cars2.startGeneration("Car2", CAR_STREAM2, new IRequestGenerateHandler(){
            @Override
            public void onRequestGenerated(String generatorId){
                work(generatorId);
            }
        });
        
        RequestStream train = new RequestStream();
        train.startGeneration("Train", TRAIN_STREAM, new IRequestGenerateHandler(){
            @Override
            public void onRequestGenerated(String generatorId){
                work(generatorId);
            }
        });
    }
    
    private static void work(String generatorId){
        //Много потоков одновременно используют код
        //Использование объекта в качестве мьютекса - создается неявный монитор
        synchronized(currentState){
            switch(currentState.name()){
                case "CLOSE":
                   if(generatorId != "Train"){
                       log(" !!! Автомобиль встает в очередь");
                   }
                   break;
                
                case "OPEN":
                   if(generatorId == "Train"){
                       close(); //закрыть переезд
                   }
                   else{
                       log("Автомобиль проезжает переезд");
                   }
                   break;
                
                  
            }
        }
    }
    //при приближении поезда переезд закрывается
    private static void close(){
        log(" --- Приближение поезда закрывает переезд");
        currentState = CrossState.CLOSE;
        TimerTask task = new TimerTask() {
            public void run() {
                    currentState = CrossState.OPEN;
                 log(" +++ Поезд освободил переезд.");
            }
        };
        
        Timer timer = new Timer("Timer");
        long delay = 2500L;
        timer.schedule(task, delay);
    }
    
    private static void log(String message){
        long threadId = Thread.currentThread().getId();
        System.out.println("Thread # " + threadId + ": "+message);
    }
}
