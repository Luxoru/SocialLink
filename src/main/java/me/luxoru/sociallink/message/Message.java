package me.luxoru.sociallink.message;


import java.util.UUID;

/**
 * Dummy class
 * All messages sent when a player joins a new server.
 */

public interface Message {

     default void send(){};

     UUID getMessageUUID();

     default boolean hasExpired(){
          return false;
     }

}
