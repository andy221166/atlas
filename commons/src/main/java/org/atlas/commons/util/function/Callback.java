package org.atlas.commons.util.function;

public interface Callback<R> {

  void onSuccess(R result);

  void onFailure(Throwable e);
}
