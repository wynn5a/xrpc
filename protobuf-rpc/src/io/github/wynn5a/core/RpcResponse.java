package io.github.wynn5a.core;

import java.io.Serializable;

public class RpcResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private Object result;
  private Throwable error;

  public Object getResult() {
    return result;
  }

  public void setResult(Object result) {
    this.result = result;
  }

  public Throwable getError() {
    return error;
  }

  public void setError(Throwable error) {
    this.error = error;
  }

  @Override
  public String toString() {
    return "RpcResponse{" +
        "result=" + result +
        ", error=" + error +
        '}';
  }
}
