/**
 * This class starts a timer and can stop it as well,
 * and return the time in either nano time, miliseconds or seconds
 */
public class Stopwatch {
    private long startTime = 0;
    private long stopTime = 0;
    private boolean running = false;

    public void start() {
        this.startTime = System.nanoTime();
        running = true;
    }

    public void stop() {

        this.stopTime = System.nanoTime();
        running = false;
    }

    public long getTime(){
        if(running)
            return (System.nanoTime() - startTime);
        return (stopTime - startTime);
    }

    public long getMillisec(){
        return (long) (getTime()*0.000001);
    }

    public long getSec(){return (long) (getMillisec()*0.001);}
}
