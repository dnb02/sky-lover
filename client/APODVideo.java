import com.google.gson.*;
import java.io.*;
public class APODVideo extends APOD implements Serializable{

  private String title;
  private String explanation;
  private String date;
  //private String hdurl;
  private String serviceVersion;
  //private String copyright;
  private String mediaType;
  private String url;

  public APODVideo(String title, String explanation, String date,String hdurl, String serviceVersion, String copyright, String mediaType, String url) {
    super(title, explanation, date, url, serviceVersion, "", mediaType, url);
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


  public String getServiceVersion() {
    return serviceVersion;
  }

  public void setServiceVersion(String serviceVersion) {
    this.serviceVersion = serviceVersion;
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

  public String toJson() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }
}


