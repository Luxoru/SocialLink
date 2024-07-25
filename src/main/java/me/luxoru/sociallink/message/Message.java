package me.luxoru.sociallink.message;

import java.util.UUID;

/**
 * Represents a message that can be sent when a player joins a new server.
 */
public interface Message {

     /**
      * Sends the message. The implementation should define how the message is sent.
      */
     default void send(){};

     /**
      * Gets the unique identifier for the message.
      *
      * @return the UUID of the message
      */
     UUID getMessageUUID();

     /**
      * Checks if the message has expired. By default, this method returns false.
      *
      * @return true if the message has expired, false otherwise
      */
     default boolean hasExpired() {
          return false;
     }
}
