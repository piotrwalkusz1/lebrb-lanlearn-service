package com.piotrwalkusz.lebrb.lanlearnservice.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

/**
 * RowWordsCounterResult
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-24T23:16:06.014Z")

public class RowWordsCounterResult {
  @JsonProperty("ori")
  private String ori = null;

  @JsonProperty("tra")
  private String tra = null;

  @JsonProperty("num")
  private Integer num = null;

  public RowWordsCounterResult ori(String ori) {
    this.ori = ori;
    return this;
  }

   /**
   * Original word
   * @return ori
  **/
  @ApiModelProperty(value = "Original word")


  public String getOri() {
    return ori;
  }

  public void setOri(String ori) {
    this.ori = ori;
  }

  public RowWordsCounterResult tra(String tra) {
    this.tra = tra;
    return this;
  }

   /**
   * Translated word
   * @return tra
  **/
  @ApiModelProperty(value = "Translated word")


  public String getTra() {
    return tra;
  }

  public void setTra(String tra) {
    this.tra = tra;
  }

  public RowWordsCounterResult num(Integer num) {
    this.num = num;
    return this;
  }

   /**
   * Number of occurences of the word in the text
   * minimum: 0
   * @return num
  **/
  @ApiModelProperty(value = "Number of occurences of the word in the text")

@Min(0)
  public Integer getNum() {
    return num;
  }

  public void setNum(Integer num) {
    this.num = num;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RowWordsCounterResult rowWordsCounterResult = (RowWordsCounterResult) o;
    return Objects.equals(this.ori, rowWordsCounterResult.ori) &&
        Objects.equals(this.tra, rowWordsCounterResult.tra) &&
        Objects.equals(this.num, rowWordsCounterResult.num);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ori, tra, num);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RowWordsCounterResult {\n");
    
    sb.append("    ori: ").append(toIndentedString(ori)).append("\n");
    sb.append("    tra: ").append(toIndentedString(tra)).append("\n");
    sb.append("    num: ").append(toIndentedString(num)).append("\n");
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

