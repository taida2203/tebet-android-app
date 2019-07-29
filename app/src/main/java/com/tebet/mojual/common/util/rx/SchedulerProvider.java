package com.tebet.mojual.common.util.rx;

import io.reactivex.Scheduler;

public interface SchedulerProvider {

    Scheduler computation();

    Scheduler io();

    Scheduler ui();
}
