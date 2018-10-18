package com.github.dhaval_mehta.savetogoogledrive.task;

import java.util.concurrent.Callable;

public interface TransferTask<V> extends Callable<V> {
}
