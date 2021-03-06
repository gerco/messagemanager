package nl.queuemanager.debug;

import java.awt.*;

public class TracingEventQueue extends EventQueue {

   private TracingEventQueueThread tracingThread;

   public TracingEventQueue() {
      this.tracingThread = new TracingEventQueueThread(500);
      this.tracingThread.start();
   }

   @Override
   protected void dispatchEvent(AWTEvent event) {
      this.tracingThread.eventDispatched(event);
      super.dispatchEvent(event);
      this.tracingThread.eventProcessed(event);
   }
}