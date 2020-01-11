package org.weather.service.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;

public class WeatherServiceRest {

    public static final String BASE_WEATHER_URI = "http://api.openweathermap.org/data/2.5/weather";
    public static final String QUESTION_MARK = "?q=";
    public static final String MEASUREMENT_UNIT = "&units=";
    public static final String APP_ID = "&APPID=6cc5c2ff37a5f06b2cfcb04279b7306b";

    public static final String CELSIUS = "celsius";

    HttpURLConnection httpURLConnection;
    JsonParser jsonParser;

    public StringBuilder getWeatherData(List<String> restboundQueryParameters) {
        BufferedReader bufferedReader = null;
        StringBuilder responseData = new StringBuilder();
        String responseString = "";

        try {
            String cityName = restboundQueryParameters.get(1);
            String countryName = restboundQueryParameters.get(2);
            String inputUnit = restboundQueryParameters.get(0);

            String measurementUnit = "imperial";
            if (CELSIUS.equals(inputUnit))
                measurementUnit = "metric";

            URL getTemperatureByCityUrl = new URL(
                    BASE_WEATHER_URI + QUESTION_MARK + cityName + "," + countryName
                            + MEASUREMENT_UNIT + measurementUnit + APP_ID);

            /*
             * Proxy systemProxy = new Proxy(Proxy.Type.HTTP,
             * new InetSocketAddress("proxy.lbs.alcatel-lucent.com", 8000));
             */

            try {
                httpURLConnection =
                        (HttpURLConnection) getTemperatureByCityUrl.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setConnectTimeout(5 * 1000);
                httpURLConnection.setReadTimeout(5 * 1000);

                if (httpURLConnection.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + httpURLConnection.getResponseCode());
                } else {
                    bufferedReader = new BufferedReader(new InputStreamReader(
                            (httpURLConnection.getInputStream())));
                    try {
                        while ((responseString = bufferedReader.readLine()) != null)
                            responseData.append(responseString);
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                    httpURLConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return responseData;
    }
}
