/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.ktepin.railroadcrossing.requestgen;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author ktepin
 * Генератор заявок - отдельный поток
 */
public class RequestStream extends Thread {
    //Миллисекунды - интенсивность потока заявок
    private String generatorId;
    private long generateFreq = -1;
    private IRequestGenerateHandler onGenerateHandler;
    
    private boolean stopFlag = false;
    private boolean waitingForTimer = false;
    
    private Timer timer = new Timer();

   
    public RequestStream(){
    
    }
    
    public synchronized void startGeneration(
            String generatorId,
            long generateFreq,
            IRequestGenerateHandler onGenerateHandler
    ){
        this.generatorId = generatorId;
        this.generateFreq = generateFreq;
        this.onGenerateHandler = onGenerateHandler;
        if(this.generateFreq > 0)
            this.stopFlag = false;
        this.start(); //Запуск нового потока
    }
    
    public synchronized void stopGeneration(){
        this.stopFlag = true;
    }
    
    //Запуск потока
    @Override
    public void run(){
       while(!this.stopFlag){
           
            if(!this.waitingForTimer)
                setTimer(this.generateFreq); //запуск таймера  
       }
    }
    
    //установка таймера
    private synchronized void setTimer(long time){
        this.waitingForTimer = true;
        TimerTask timerTask = new TimerTask() {      
            @Override
            public void run() {
                onTimerGone(); 
            }     
        };
        this.timer.schedule(timerTask,time);
    }
    
    private synchronized void onTimerGone(){
        this.waitingForTimer = false;
        this.onGenerateHandler.onRequestGenerated(this.generatorId);
    }
}
