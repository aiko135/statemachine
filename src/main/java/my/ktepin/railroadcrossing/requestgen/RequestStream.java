/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.ktepin.railroadcrossing.requestgen;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author ktepin
 * Генератор заявок - отдельный поток
 */
public class RequestStream{
    //Миллисекунды - интенсивность потока заявок
    private String generatorId;
    private long generateFreq = -1;
    private IRequestGenerateHandler onGenerateHandler;
    
    private Random rand = new Random(System.currentTimeMillis());
    private Timer timer = new Timer();
   
    //Создание нового потока
    class Task extends TimerTask {
        @Override
        public void run() {
              onTimerGone();
              timer.schedule(new Task(), getRandomTime());
        }
    }
    
    public RequestStream(){}
    
    public synchronized void startGeneration(
            String generatorId,
            long generateFreq,
            IRequestGenerateHandler onGenerateHandler
    ){
        this.generatorId = generatorId;
        this.generateFreq = generateFreq;
        this.onGenerateHandler = onGenerateHandler;
        
        timer.schedule(new Task(), getRandomTime());
    }
    
    private synchronized void onTimerGone(){
        this.onGenerateHandler.onRequestGenerated(this.generatorId);
    }
    
    private long getRandomTime(){
      
        boolean isNeg = rand.nextBoolean();
        long diverse = (long)((rand.nextLong()) % (this.generateFreq * 0.3f));
        long result = this.generateFreq; 
        return isNeg ? result-diverse : result+diverse ;
    }
}
