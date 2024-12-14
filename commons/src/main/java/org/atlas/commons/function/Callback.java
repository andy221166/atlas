package org.atlas.commons.function;

public interface Callback<R> {

  void onSuccess(R result);

  void onFailure(Throwable e);
}
