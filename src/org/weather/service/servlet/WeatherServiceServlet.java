package org.weather.service.servlet;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.weather.service.orchestration.WeatherServiceOrchestration;

public class WeatherServiceServlet extends HttpServlet {

    private static final long serialVersionUID = -6433624830644926297L;

    public void init() throws ServletException {}

    /*
     * public void doPost(HttpServletRequest httpServletRequest, HttpServletResponse
     * httpServletResponse)
     * 
     * Servlet method called by API.AI when an intent to retrieve weather by city name is matched.
     * API.AI initiates a REST call (POST) which is intercepted back to this Servlet method.
     * Input is a JSON object with query details from API.AI
     * Output is a JSON response with the text to be displayed on user's device
     * 
     */
    public void doPost(HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        try {
            BufferedReader bufferedReader = httpServletRequest.getReader();
            WeatherServiceOrchestration wsOrchestrationObj = new WeatherServiceOrchestration();
            List<String> restboundParametersToSearchList = new ArrayList<>();
            String responseValue = wsOrchestrationObj.getWeatherByCity(bufferedReader,
                    loadParametersToSearchList(restboundParametersToSearchList));
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write(responseValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * private List<String> loadParametersToSearchList(List<String> restboundParametersToSearchList)
     * 
     * Helper method to load a list of parameters to search for in the POST body initiated by API.AI
     * 
     */
    private List<String> loadParametersToSearchList(List<String> restboundParametersToSearchList) {
        restboundParametersToSearchList.add("geo-city");
        restboundParametersToSearchList.add("geo-country");
        restboundParametersToSearchList.add("unit-information");
        restboundParametersToSearchList.add("intentName");
        return restboundParametersToSearchList;
    }
}
