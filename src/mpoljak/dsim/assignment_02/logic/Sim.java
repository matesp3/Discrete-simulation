package mpoljak.dsim.assignment_02.logic;

import mpoljak.dsim.common.ISimDelegate;

import java.util.ArrayList;
import java.util.List;

public class Sim {
    List<ISimDelegate> delegates;
    private volatile boolean paused;
    private volatile boolean ended;

    public Sim() {
        this.delegates = new ArrayList<ISimDelegate>();
        this.ended = false;
        this.paused = false;
    }

    public synchronized boolean isPaused() {
        return this.paused;
    }

    public synchronized void setPaused(boolean pause) { // monitor is reference of this class instance
        System.out.println(Thread.currentThread().getName() + ": value="+pause);
        this.paused = pause;
        if (!this.paused) {
            this.notify();
        }
    }

    public boolean isEnded() {
        return this.ended;
    }

    public void setEnded(boolean end) {
        System.out.println(Thread.currentThread().getName() + ": value="+end);
        this.ended = end;
        if (end) {
            synchronized (this) {
                this.paused = false;
                this.notifyAll(); // finish work with all threads that have monitor of this class instance
            }
        }
    }

    public void registerDelegate(ISimDelegate delegate) {
        if (delegate == null)
            return;
        for (ISimDelegate d : this.delegates) {
            if (d == delegate)
                return;
        }
        this.delegates.add(delegate);
    }

    public void costlyOperation() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + ": started costly operation");
        if (this.ended) {
            this.ended = false;
            this.paused = false;
        }
        int i = 0;
        while (! this.ended) {
            synchronized (this) {
                while (this.paused) { // while because of spurious wakeup
                    System.out.println(Thread.currentThread().getName() + ": going to sleep...");
                    this.wait(); // going to sleep
                    // after notification, thread is woken up and when it gets monitor, it continues here
                    System.out.println(Thread.currentThread().getName() + ": continues in costly operation");
                }
            }
            Thread.sleep(500);
            this.notifyDelegates(i++);
        }
        System.out.println(Thread.currentThread().getName() + ": ended successfully.");
    }

    private void notifyDelegates(int val) {
        for (ISimDelegate d : this.delegates) {
            d.refresh(val);
        }
    }
}
