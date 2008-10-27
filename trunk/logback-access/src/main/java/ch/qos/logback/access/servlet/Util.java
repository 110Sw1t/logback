package ch.qos.logback.access.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.qos.logback.access.Constants;

public class Util {

  public static boolean isFormUrlEncoded(HttpServletRequest request) {
    if ("POST".equals(request.getMethod())
        && Constants.X_WWW_FORM_URLECODED.equals(request.getContentType())) {
      return true;
    } else {
      return false;
    }
  }
  
  public static boolean isImageResponse(HttpServletResponse response) {
    
    String responseType = response.getContentType();
    
    if (responseType != null && responseType.startsWith(Constants.IMAGE_CONTENT_TYPE)) {
      return true;
    } else {
      return false;
    }
  }
}
