package io.github.wynn5a.core;

import java.io.Serializable;
import java.util.Arrays;

public class RpcRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  private String methodName;
  private Class<?>[] parameterTypes;
  private Object[] arguments;

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public Class<?>[] getParameterTypes() {
    return parameterTypes;
  }

  public void setParameterTypes(Class<?>[] parameterTypes) {
    this.parameterTypes = parameterTypes;
  }

  public Object[] getArguments() {
    return arguments;
  }

  public void setArguments(Object[] arguments) {
    this.arguments = arguments;
  }

  @Override
  public String toString() {
    return "RpcRequest{" +
        "methodName='" + methodName + '\'' +
        ", parameterTypes=" + Arrays.toString(parameterTypes) +
        ", arguments=" + Arrays.toString(arguments) +
        '}';
  }
}
