package org.generator.exception;

public class LimitReachedException extends Exception {

    Object lastValue;

    public LimitReachedException(Object lastValue){
        super();
        this.lastValue = lastValue;
    }

    public LimitReachedException(Object lastValue, String message){
        super(message);
        this.lastValue = lastValue;
    }
}
