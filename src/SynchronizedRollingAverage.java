class SynchronizedRollingAverage {
    private double avg = 0;
    private long count = 0;
  
    public synchronized void addValue(double value) {
      avg = ((avg * count) + value) / (++count);
    }
  
    public synchronized double getAverage() {
      return avg;
    }
  }