package me.luxoru.sociallink.data.redis.commands;

import com.google.gson.Gson;
import lombok.NonNull;
import me.luxoru.databaserepository.impl.redis.RedisMessenger;
import me.luxoru.sociallink.data.redis.RedisDatabaseAdapter;

import java.util.*;

public class RedisCommandManager {

    private final Map<String, Class<? extends RedisCommand>> commands = Collections.synchronizedMap(new HashMap<>());
    private final Map<Class<? extends RedisCommand>, Set<RedisCommandCallback<?>>> callbacks = Collections.synchronizedMap(new HashMap<>());
    private static final Gson GSON = new Gson();

    @SuppressWarnings({"rawtypes", "unchecked"})
    public RedisCommandManager() {
        RedisDatabaseAdapter.INSTANCE.getDatabase().getMessangerService().addSubscriber(new RedisMessenger() {

            @Override
            public void onPMessage(String pattern,String channel, String message) {

                String command = channel.split(":")[1];
                Class<? extends RedisCommand> commandClazz = commands.get(command);
                if(commandClazz == null) return;

                RedisCommand redisCommand = GSON.fromJson(message, commandClazz);

                for (RedisCommandCallback callback : callbacks.getOrDefault(commandClazz, Collections.emptySet())) {
                    callback.handle(redisCommand);
                }

            }

            @Override
            public String[] getChannels() {
                return new String[]{"sociallink:*"};
            }

            @Override
            public boolean isUsingPatterns() {
                return true;
            }
        });
    }


    public synchronized void addCommand(@NonNull Class<? extends RedisCommand> command) {
        commands.put(command.getSimpleName(), command);
    }


    public synchronized <T extends RedisCommand> void addCallback(@NonNull Class<T> clazz, @NonNull RedisCommandCallback<T> callback) {
        Set<RedisCommandCallback<?>> callbacks = Collections.synchronizedSet(this.callbacks.getOrDefault(clazz, new HashSet<>()));
        callbacks.add(callback);
        this.callbacks.put(clazz, callbacks);
    }


    public synchronized void dispatch(RedisCommand command) {
        String commandName = command.getClass().getSimpleName();
        if (commands.get(commandName) == null) { // If the command is not registered, don't dispatch it
            System.err.println("Could not find Redis command \"" + commandName + "\", not dispatching");
            return;
        }
        RedisDatabaseAdapter.INSTANCE.getDatabase().getMessangerService().dispatch("sociallink" + ":" + commandName, GSON.toJson(command));
    }

}
