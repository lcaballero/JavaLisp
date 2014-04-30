package JavaLisp;

import com.google.common.base.Joiner;
import com.google.inject.Singleton;


@Singleton
public class App {

    private boolean started = false;

    public boolean isStarted() {
        return this.started;
    }

    public void start() {
        started = true;
        System.out.println(Joiner.on(", ").join("Hello", "World"));
    }
}