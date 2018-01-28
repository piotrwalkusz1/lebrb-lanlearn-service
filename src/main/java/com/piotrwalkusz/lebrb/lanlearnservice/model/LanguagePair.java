package com.piotrwalkusz.lebrb.lanlearnservice.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

/**
 * LanguagePair
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-24T23:16:06.014Z")

public class LanguagePair   {
  @JsonProperty("from")
  private Language from = null;

  @JsonProperty("to")
  private Language to = null;

  public LanguagePair(Language from, Language to) {
    this.from = from;
    this.to = to;
  }

  public LanguagePair from(Language from) {
    this.from = from;
    return this;
  }

   /**
   * Get from
   * @return from
  **/
  @ApiModelProperty(value = "")

  @Valid

  public Language getFrom() {
    return from;
  }

  public void setFrom(Language from) {
    this.from = from;
  }

  public LanguagePair to(Language to) {
    this.to = to;
    return this;
  }

   /**
   * Get to
   * @return to
  **/
  @ApiModelProperty(value = "")

  @Valid

  public Language getTo() {
    return to;
  }

  public void setTo(Language to) {
    this.to = to;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LanguagePair languagePair = (LanguagePair) o;
    return Objects.equals(this.from, languagePair.from) &&
        Objects.equals(this.to, languagePair.to);
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, to);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LanguagePair {\n");
    
    sb.append("    from: ").append(toIndentedString(from)).append("\n");
    sb.append("    to: ").append(toIndentedString(to)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

