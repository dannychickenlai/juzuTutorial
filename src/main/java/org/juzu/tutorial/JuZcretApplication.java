package org.juzu.tutorial;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.juzu.tutorial.services.SecretService;

import juzu.Action;
import juzu.Path;
import juzu.Response;
import juzu.View;

public class JuZcretApplication {

  @Inject
  SecretService secretService;
  
  @Inject
  @Path("secretWall.gtmpl")
  org.juzu.tutorial.templates.secretWall secretWall;
  
  @Inject
  @Path("addSecret.gtmpl")
  org.juzu.tutorial.templates.addSecret addSecret;
  
  @View
  public Response.Content index() {
    //return secretWall.with().secretsList("My list of secret").ok();
    
    //Map<String, Object> parameters = new HashMap<String, Object>();
    //parameters.put("secretList", "My list of secret");
    //return secretWall.with(parameters).ok();
    
    return secretWall.with().secretsList(secretService.getSecrets()).ok();
  }
  
  @View
  public Response.Content addSecretForm() {
    return addSecret.ok();
  }
  
  @Action
  public Response.View addSecret(String msg, String imgURL) {
    secretService.addSecret(msg, imgURL);
    return JuZcretApplication_.index();
  }
}
