import com.google.gson.*;
import java.io.*;
public class APOD implements Serializable{

  private String title;
  private String explanation;
  private String date;
  private String hdurl;
  private String serviceVersion;
  private String copyright;
  private String mediaType;
  private String url;

  public APOD(String title, String explanation, String date, String hdurl, String serviceVersion, String copyright, String mediaType, String url) {
    this.title = title;
    this.explanation = explanation;
    this.date = date;
    this.hdurl = hdurl;
    this.serviceVersion = serviceVersion;
    this.copyright = copyright;
    this.mediaType = mediaType;
    this.url = url;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getExplanation() {
    return explanation;
  }

  public void setExplanation(String explanation) {
    this.explanation = explanation;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getHdurl() {
    return hdurl;
  }

  public void setHdurl(String hdurl) {
    this.hdurl = hdurl;
  }

  public String getServiceVersion() {
    return serviceVersion;
  }

  public void setServiceVersion(String serviceVersion) {
    this.serviceVersion = serviceVersion;
  }

  public String getCopyright() {
    return copyright;
  }

  public void setCopyright(String copyright) {
    this.copyright = copyright;
  }

  public String getMediaType() {
    return mediaType;
  }

  public void setMediaType(String mediaType) {
    this.mediaType = mediaType;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  // You can add a method to convert the object to JSON format using a library like Gson
  public String toJson() {
    // Implementation using Gson library (assuming you have it included)
    Gson gson = new Gson();
    return gson.toJson(this);
  }
}


