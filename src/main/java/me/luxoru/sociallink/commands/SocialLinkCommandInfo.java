package me.luxoru.sociallink.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SocialLinkCommandInfo {
    String name();
    String[] aliases() default {};
    boolean isSubComand() default false;
}
