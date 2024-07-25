package me.luxoru.sociallink.message;

import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
public class MessageManager {

    private Set<Message> messages;

    public MessageManager() {
        messages = new HashSet<>();
    }

    public void addMessage(final Message message) {
        messages.add(message);
    }

    public void removeMessage(final UUID messageUUID) {
        messages.removeIf(message -> message.getMessageUUID().equals(messageUUID));
    }

    public Set<Message> getMessages() {
        return Set.copyOf(messages);
    }
}
