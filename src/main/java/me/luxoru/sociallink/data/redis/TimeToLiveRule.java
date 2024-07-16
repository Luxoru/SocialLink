package me.luxoru.sociallink.data.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Getter
public class TimeToLiveRule {
    public static final TimeToLiveRule FOREVER = new TimeToLiveRule(-1L, TimeUnit.SECONDS);

    /**
     * The amount of time to live for.
     */
    private final long time;

    /**
     * The {@link TimeUnit} to use to determine
     * the amount of time to live for.
     */
    @NonNull
    private final TimeUnit timeUnit;
}
