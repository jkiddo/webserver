package com.sorskod.webserver.entities;

import com.fasterxml.jackson.annotation.*;

import java.util.Map;

/**
 * @author Aleksandar Babic <salebab@gmail.com>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(
    getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY,
    isGetterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY,
    setterVisibility = JsonAutoDetect.Visibility.NONE,
    creatorVisibility = JsonAutoDetect.Visibility.NONE,
    fieldVisibility = JsonAutoDetect.Visibility.NONE
)
@JsonPropertyOrder({"code", "message"})
public class Error {

  private final int code;
  private final String message;
  private Map<String, Object> details;

  public Error(int code, String message) {
    this.code = code;
    this.message = message;
  }

  @JsonCreator
  public Error(@JsonProperty(required = true) int code,
               @JsonProperty(required = true) String message,
               @JsonProperty Map<String, Object> details) {
    this(code, message);
    this.details = details;
  }

  @JsonProperty
  public int getCode() {
    return code;
  }

  @JsonProperty
  public String getMessage() {
    return message;
  }

  @JsonProperty
  public Map<String, Object> getDetails() {
    return details;
  }
}