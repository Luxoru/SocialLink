package me.luxoru.sociallink.message;

import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Manages a collection of {@link Message} instances.
 * Provides methods to add, remove, and retrieve messages.
 */
@Setter
public class MessageManager {

    private Set<Message> messages;

    /**
     * Constructs a new MessageManager with an empty set of messages.
     */
    public MessageManager() {
        messages = new HashSet<>();
    }

    /**
     * Adds a message to the manager.
     *
     * @param message the message to add
     */
    public void addMessage(final Message message) {
        messages.add(message);
    }

    /**
     * Removes a message from the manager based on its UUID.
     *
     * @param messageUUID the UUID of the message to remove
     */
    public void removeMessage(final UUID messageUUID) {
        messages.removeIf(message -> message.getMessageUUID().equals(messageUUID));
    }

    /**
     * Retrieves an unmodifiable set of all messages.
     *
     * @return an unmodifiable set of messages
     */
    public Set<Message> getMessages() {
        return Set.copyOf(messages);
    }
}
