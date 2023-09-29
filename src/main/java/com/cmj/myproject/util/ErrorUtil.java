package com.cmj.myproject.util;

import org.json.simple.JSONObject;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

public class ErrorUtil {
    public static JSONObject JsonErrorResponse(int errorCode, String errorMessage) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("errorCode", errorCode);
        jsonObject.put("errorMessage", errorMessage);
        return jsonObject;
    }

    public static ModelAndView setErrorModelAndView(Exception e, String url) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", e.getMessage());
        modelAndView.addObject("url", url);
        modelAndView.setViewName("error/error");
        return modelAndView;
    }


}
