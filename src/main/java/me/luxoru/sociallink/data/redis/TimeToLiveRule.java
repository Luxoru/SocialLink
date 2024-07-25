package me.luxoru.sociallink.data.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;


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


    /**
     * The time of expiry
     */
    private final long expiryTime;



    public TimeToLiveRule(long time, TimeUnit timeUnit) {
        this.time = time;
        this.timeUnit = timeUnit;
        this.expiryTime = System.currentTimeMillis() + timeUnit.toMillis(time);
    }


    public boolean isExpired(){
        if(this == TimeToLiveRule.FOREVER)return false;
        return System.currentTimeMillis() > expiryTime;
    }



}
