package jp.urgm.radishtainer.test;

import javax.inject.Singleton;

import jp.urgm.radishtainer.event.Observes;

@Singleton
public class Ooo2 extends Ooo1 {

    public boolean withAtObserversSub;

    public boolean noAtObserversSub;

    public boolean noOverridedSub;

    @Override
    public void handle1(Aaa event) {
        noAtObserversSub = true;
    }

    @Override
    public void handle2(@Observes Aaa event) {
        withAtObserversSub = true;
    }

    public void handle4(@Observes Aaa event) {
        noOverridedSub = true;
    }
}
