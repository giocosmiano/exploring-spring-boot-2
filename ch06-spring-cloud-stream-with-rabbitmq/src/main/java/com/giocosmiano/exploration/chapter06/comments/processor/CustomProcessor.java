package com.giocosmiano.exploration.chapter06.comments.processor;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface CustomProcessor {
    String INPUT = "input";
    String OUTPUT = "emptyOutput";

    /*
     It has two channel names, INPUT and OUTPUT. The INPUT channel uses the same as Processor. To avoid
     colliding with the OUTPUT channel of Source , we create a different channel name, emptyOutput

     The SubscribableChannel is for inputs and a MessageChannel for outputs

     This flags our application as both a Sink as well as a Source for events

     Spring Cloud Stream is Reactor-friendly. When dealing with Reactive Streams, our code
     shouldn't be the termination point for processing. So, receiving an incoming Flux of Comment objects must
     result in an outgoing Flux that the framework can invoke
     */

    @Input(CustomProcessor.INPUT)
    SubscribableChannel input();

    @Output(CustomProcessor.OUTPUT)
    MessageChannel output();
}
