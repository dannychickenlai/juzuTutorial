package org.juzu.tutorial.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class Secret extends Model {
  private String message;
  private String imageURL;
  private Set<String> likes;
  private List<Comment> comments;
  
  public Secret() {}

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getImageURL() {
    return imageURL;
  }

  public void setImageURL(String imageURL) {
    this.imageURL = imageURL;
  }

  public List<Comment> getComments() {
    return comments;
  }

  public void setComments(List<Comment> comments) {
    this.comments = comments;
  }

  public Set<String> getLikes() {
    return likes;
  }

  public void setLikes(Set<String> likes) {
    this.likes = likes;
  }
  
  
}
