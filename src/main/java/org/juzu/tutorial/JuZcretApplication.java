package org.juzu.tutorial;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;

import org.json.JSONArray;
import org.json.JSONObject;
import org.juzu.tutorial.models.Comment;
import org.juzu.tutorial.services.SecretService;

import juzu.Action;
import juzu.Mapped;
import juzu.Path;
import juzu.Response;
import juzu.View;
import juzu.bridge.portlet.JuzuPortlet;
import juzu.plugin.ajax.Ajax;
import juzu.plugin.validation.ValidationError;
import juzu.request.RequestContext;
import juzu.request.RequestLifeCycle;
import juzu.request.SecurityContext;

public class JuZcretApplication implements RequestLifeCycle {

  private static final String ANONYMOUS = "Anonymous";
  
  @Inject
  SecretService secretService;
  
  @Inject
  @Path("secretWall.gtmpl")
  org.juzu.tutorial.templates.secretWall secretWall;
  
  @Inject
  @Path("addSecret.gtmpl")
  org.juzu.tutorial.templates.addSecret addSecret;
  
  @Inject
  @Path("editMode.gtmpl")
  org.juzu.tutorial.templates.editMode editMode;
  
  @View
  public Response.Content index(RequestContext context) {
    //return secretWall.with().secretsList("My list of secret").ok();
    
    //Map<String, Object> parameters = new HashMap<String, Object>();
    //parameters.put("secretList", "My list of secret");
    //return secretWall.with(parameters).ok();
    
    //return secretWall.with().secretsList(secretService.getSecrets()).ok();
    
    boolean enableComment = Boolean.parseBoolean(prefs.getValue(ENABLE_COMMENT, "false"));

    if (PortletMode.EDIT.equals(context.getProperty(JuzuPortlet.PORTLET_MODE))) {
      return editMode.with().enableComment(enableComment).ok();
    } else {
      return secretWall.with().enableComment(enableComment).secretsList(secretService.getSecrets()).ok();
    }
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

  @Ajax
  @Resource
  public Response addComment(String secretId, @Mapped @Valid Comment comment, SecurityContext context) {
    comment.setUserId(getCurrentUser(context));
    Comment result = secretService.addComment(secretId, comment);
    if (result != null) {
      return Response.ok(new JSONObject(result).toString()).withMimeType("text/json");
    } else {
      return Response.status(503);
    }
  }
  
  @Ajax
  @Resource
  public Response addLike(String secretId, SecurityContext context) {
    Set<String> likes = secretService.addLike(secretId, getCurrentUser(context));
    if (likes != null) {
      return Response.ok(new JSONArray(likes).toString()).withMimeType("text/json");
    } else {
      return Response.status(503);
    }
  }
  
  private String getCurrentUser(SecurityContext context) {
    Principal user = context.getUserPrincipal();
    if (user == null) {
      return ANONYMOUS;
    } else {
      return user.getName();
    }
  }
  
  // RequestLifeCycle 
  //.............................
  
  @Override
  public void endRequest(RequestContext context) {
      Response response = context.getResponse();
      if (response instanceof ValidationError) {
          ValidationError error = (ValidationError)response;
          Set<ConstraintViolation<Object>> violations = error.getViolations();

          String msg = violations.iterator().next().getMessage();
          response = Response.ok(msg).withMimeType("text/html");
          context.setResponse(response);
      }
  }

  @Override
  public void beginRequest(RequestContext context) {
  }
  
  // Prefs
  //---------------------------
  
  @Inject
  PortletPreferences prefs;

  public static final String ENABLE_COMMENT = "enableComment";
  
  @Action
  public Response.View enableComment(String enableComment) throws ReadOnlyException, ValidatorException, IOException {
      if ("on".equals(enableComment)) {
          enableComment = "true";
      }
      prefs.setValue(ENABLE_COMMENT, enableComment);
      prefs.store();
      return JuZcretApplication_.index().with(JuzuPortlet.PORTLET_MODE, PortletMode.VIEW);
  }
  
  // editMode view
  //------------------------------
  
  
  
}
