package org.miu.mppproject.librarysystem.libraryapp.presentation.shared;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public abstract class MviViewModel<S, I extends MviIntent> {
    private final BehaviorSubject<S> state = BehaviorSubject.create();

    public abstract void processIntent(I intent);

    public Observable<S> getState() {
        return state.hide();
    }

    protected void setState(S newState) {
        state.onNext(newState);
    }
}
