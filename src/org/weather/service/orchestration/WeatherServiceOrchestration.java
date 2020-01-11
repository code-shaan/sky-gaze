package org.weather.service.orchestration;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.json.simple.JSONObject;
import org.weather.service.rest.WeatherServiceRest;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherServiceOrchestration {

    public static final String CELSIUS = "celsius";
    public static final String FAHRENHEIT = "fahrenheit";
    public String MEASUREMENT_UNIT = FAHRENHEIT;
    public static final String EMPTY = "empty";

    public WeatherServiceOrchestration() {}

    public String getWeatherByCity(BufferedReader restboundBufferedReader,
            List<String> restboundParametersToSearchList) {

        String inputIntent = "";

        StringBuilder restboundInputData = new StringBuilder();
        StringBuilder restboundResultData = new StringBuilder();
        StringBuilder servletboundInputData = new StringBuilder();
        StringBuilder servletboundResultData = new StringBuilder();

        JsonParser restboundJsonParser;
        JsonParser servletboundJsonParser;

        // Load predefined intents into map for search and retrieval
        Map<String, List<String>> intentMap = new HashMap<>();
        this.loadIntentMap(intentMap);

        // Code for parsing input from API.ai and forwarding required parameters to REST API
        restboundResultData =
                getInputDataFromApiAiAgent(restboundBufferedReader, restboundInputData);

        restboundJsonParser = getJsonParserFromInputData(restboundResultData);

        StringBuilder restboundInputParameters =
                getParametersFromJsonParser(restboundJsonParser, restboundParametersToSearchList);

        restboundInputParameters =
                restboundInputParameters.deleteCharAt(restboundInputParameters.length() - 1);

        String[] retrievedQueryParameters = new String[7];
        retrievedQueryParameters = restboundInputParameters.toString().split(",");

        List<String> restboundQueryParameters = new ArrayList<>();
        if (restboundInputParameters.toString().toLowerCase().contains(CELSIUS)) {
            MEASUREMENT_UNIT = CELSIUS;
        }

        for (String thisString : retrievedQueryParameters) {
            if (thisString.equals("")) {
                restboundQueryParameters.add(FAHRENHEIT);
                MEASUREMENT_UNIT = FAHRENHEIT;
            } else {
                restboundQueryParameters.add(thisString);
            }
        }

        inputIntent = restboundQueryParameters.get(3);

        boolean isValidCountryName = getCountryNameValidation(restboundQueryParameters.get(2));

        String responseString = "";
        if (isValidCountryName) {
            WeatherServiceRest restboundObject = new WeatherServiceRest();

            servletboundInputData =
                    restboundObject.getWeatherData(restboundQueryParameters);

            // Code for parsing input from REST API and forwarding display output to API.AI
            servletboundJsonParser = getJsonParserFromInputData(servletboundInputData);

            List<String> servletboundParametersToSearchList = intentMap.get(inputIntent);

            servletboundResultData =
                    getParametersFromJsonParser(servletboundJsonParser,
                            servletboundParametersToSearchList);

            String[] servletboundResponseParamaters = {EMPTY};
            if (servletboundResultData.length() != 0) {
                servletboundResultData =
                        servletboundResultData.deleteCharAt(servletboundResultData.length() - 1);
                servletboundResponseParamaters = servletboundResultData.toString().split(",");
            }

            // Selecting display output based on input intent
            switch (inputIntent) {
                case "retrieve.rainfall":
                    responseString = returnRainfallResponse(servletboundResponseParamaters);
                    break;
                case "retrieve.sunrise":
                    responseString = returnSunriseResponse(servletboundResponseParamaters);
                    break;
                case "retrieve.sunset":
                    responseString = returnSunsetResponse(servletboundResponseParamaters);
                    break;
                case "retrieve.temperature":
                    responseString = returnTemperatureResponse(servletboundResponseParamaters);
                    break;
                case "retrieve.weather":
                    responseString = returnWeatherResponse(servletboundResponseParamaters);
                    break;
                case "retrieve.wind":
                    responseString = returnWindResponse(servletboundResponseParamaters);
                    break;
                case "retrieve.clouds":
                    responseString = returnCloudsResponse(servletboundResponseParamaters);
                    break;
                case "retrieve.snow":
                    responseString = returnSnowResponse(servletboundResponseParamaters);
                    break;
            }
            return responseString;
        } else {
            responseString =
                    "{\"speech\": \"I don't know where this country is. Narnia?\"" + ","
                            + "\"displayText\" : \"I don't know where this country is. Narnia?\"}";
            return responseString;
        }
    }

    private boolean getCountryNameValidation(String countryName) {
        final Set<String> ISO_COUNTRIES =
                new HashSet<String>(Arrays.asList(Locale.getISOCountries()));
        Locale obj;
        for (String thisCountry : ISO_COUNTRIES) {
            obj = new Locale("", thisCountry);
            if (countryName.toLowerCase().contains(obj.getDisplayCountry().toLowerCase()))
                return true;
        }
        return false;
    }

    private String returnRainfallResponse(String[] servletboundResponseParamaters) {
        String responseString = "";
        if (servletboundResponseParamaters[0].equals(EMPTY)) {
            responseString =
                    "{\"speech\": \"It's not raining right now\"" + ","
                            + "\"displayText\" : \"It's not raining right now\"}";
        } else {
            responseString =
                    "{\"speech\": \"My masters say this is a work in progress. I can only do what I am taught.\""
                            + ","
                            + "\"displayText\" : \"My masters say this is a work in progress. I can only do what I am taught.\"}";

        }
        return responseString;
    }

    private String returnSunriseResponse(String[] servletboundResponseParamaters) {
        Long epochTime = Long.parseLong(servletboundResponseParamaters[0]);
        Date date = new Date(epochTime * 1000L);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        dateFormat.setTimeZone(TimeZone.getDefault());
        String responseString = "{\"speech\": \"Sunrise is scheduled to be at "
                + dateFormat.format(date).toString() + " your time. " + "\","
                + "\"displayText\" : \"Sunrise is scheduled to be at " +
                dateFormat.format(date).toString() + " your time. " +
                "\"}";
        return responseString;
    }

    private String returnSunsetResponse(String[] servletboundResponseParamaters) {
        Long epochTime = Long.parseLong(servletboundResponseParamaters[0]);
        Date date = new Date(epochTime * 1000L);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        dateFormat.setTimeZone(TimeZone.getDefault());
        String responseString =
                "{\"speech\": \"Sunset will occur at " + dateFormat.format(date).toString()
                        + " your time. " + "\"," + "\"displayText\" : \"Sunset will occur at "
                        + dateFormat.format(date).toString() + " your time. " + "\"}";
        return responseString;
    }

    private String returnTemperatureResponse(String[] servletboundResponseParamaters) {
        String responseString =
                "{\"speech\": \"It's " + servletboundResponseParamaters[0] + " degrees "
                        + MEASUREMENT_UNIT + " right now. Today's minimum will be "
                        + servletboundResponseParamaters[1] +
                        " degrees " + MEASUREMENT_UNIT + " and maximum will be "
                        + servletboundResponseParamaters[2] + " degrees " + MEASUREMENT_UNIT +
                        "\"," + "\"displayText\": \"It's " + servletboundResponseParamaters[0]
                        + " degrees " + MEASUREMENT_UNIT +
                        " right now. Today's minimum will be " + servletboundResponseParamaters[1]
                        + " degrees " + MEASUREMENT_UNIT +
                        " and maximum will be " + servletboundResponseParamaters[2] + " degrees "
                        + MEASUREMENT_UNIT + "\"}";
        return responseString;
    }

    private String returnWeatherResponse(String[] servletboundResponseParamaters) {
        String responseString = "{\"speech\": \"It's " + servletboundResponseParamaters[0]
                + " degrees " + MEASUREMENT_UNIT + " right now with a humidity of " +
                servletboundResponseParamaters[1] + "%. " + "The minimum temperature today will be "
                + servletboundResponseParamaters[2] +
                " degrees " + MEASUREMENT_UNIT + " and maximum " + servletboundResponseParamaters[3]
                + " degrees " + MEASUREMENT_UNIT + ". Cloud coverage is at " +
                servletboundResponseParamaters[4] + "% with a wind speed of "
                + servletboundResponseParamaters[5] + " mph." +
                "\"," + "\"displayText\": \"It's " + servletboundResponseParamaters[0] + " degrees "
                + MEASUREMENT_UNIT +
                " right now with a humidity of " + servletboundResponseParamaters[1] + "%. "
                + "The minimum temperature today will be " +
                servletboundResponseParamaters[2] + " degrees " + MEASUREMENT_UNIT + " and maximum "
                + servletboundResponseParamaters[3] +
                " degrees " + MEASUREMENT_UNIT + ". Cloud coverage is at "
                + servletboundResponseParamaters[4] + "% with a wind speed of " +
                servletboundResponseParamaters[5] + " mph." + "\"}";
        return responseString;
    }

    private String returnWindResponse(String[] servletboundResponseParamaters) {
        String responseString = "{\"speech\": \"Currently, wind is blowing at a speed of "
                + servletboundResponseParamaters[0] + " mph. " +
                "\"," + "\"displayText\" : \"Currently, wind is blowing at a speed of "
                + servletboundResponseParamaters[0] + " mph. " + "\"}";
        return responseString;
    }

    private String returnCloudsResponse(String[] servletboundResponseParamaters) {
        String responseString =
                "{\"speech\": \"We have cloud coverage at " + servletboundResponseParamaters[0]
                        + "%." + "\"," + "\"displayText\" : \"We have cloud coverage at "
                        + servletboundResponseParamaters[0] + "%. " + "\"}";
        return responseString;
    }

    private String returnSnowResponse(String[] servletboundResponseParamaters) {
        String responseString = "";
        if (servletboundResponseParamaters[0].equals(EMPTY)) {
            responseString = "{\"speech\": \"It's not snowing right now\"" + ","
                    + "\"displayText\" : \"It's not snowing right now\"}";
        } else {
            responseString =
                    "{\"speech\": \"My masters say this is a work in progress. "
                            + "I can only do what I am taught.\"" + ","
                            + "\"displayText\" : \"My masters say this is a work in progress. "
                            + "I can only do what I am taught.\"}";
        }
        return responseString;
    }

    private void loadIntentMap(Map<String, List<String>> intentMap) {
        intentMap.put("retrieve.rainfall", Arrays.asList("rain"));
        intentMap.put("retrieve.sunrise", Arrays.asList("sunrise"));
        intentMap.put("retrieve.sunset", Arrays.asList("sunset"));
        intentMap.put("retrieve.temperature", Arrays.asList("temp", "temp_min", "temp_max"));
        intentMap.put("retrieve.weather",
                Arrays.asList("temp", "humidity", "temp_min", "temp_max", "all", "speed"));
        intentMap.put("retrieve.wind", Arrays.asList("speed"));
        intentMap.put("retrieve.clouds", Arrays.asList("all"));
        intentMap.put("retrieve.snow", Arrays.asList("snow"));
    }

    private StringBuilder getInputDataFromApiAiAgent(BufferedReader restboundBufferedReader,
            StringBuilder restboundInputData) {
        String returnString = "";
        try {
            while ((returnString = restboundBufferedReader.readLine()) != null)
                restboundInputData.append(returnString);
        } catch (IOException io) {
            io.printStackTrace();
        }
        return restboundInputData;
    }

    private JsonParser getJsonParserFromInputData(StringBuilder stringBuilder) {
        JsonParser jsonParser = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JSONObject jsonObj = objectMapper.readValue(stringBuilder.toString(), JSONObject.class);
            JsonFactory jsonFactory = new JsonFactory();
            jsonParser = jsonFactory.createParser(jsonObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonParser;
    }

    private StringBuilder getParametersFromJsonParser(JsonParser jsonParser,
            List<String> stringList) {
        StringBuilder stringBuilder = new StringBuilder();
        int parameterCount = stringList.size();
        try {
            Map<String, Integer> intentFoundMap = new HashMap<String, Integer>();
            while (!jsonParser.isClosed() && parameterCount > 0) {
                JsonToken jsonToken = jsonParser.nextToken();
                if (JsonToken.FIELD_NAME.equals(jsonToken)) {
                    String fieldName = jsonParser.getCurrentName();
                    jsonToken = jsonParser.nextToken();
                    for (String eachStringInList : stringList) {
                        if (eachStringInList.equals(fieldName)
                                && !intentFoundMap.containsKey(fieldName)) {
                            intentFoundMap.put(fieldName, 1);
                            stringBuilder.append(jsonParser.getValueAsString() + ",");
                            parameterCount--;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder;
    }
}
