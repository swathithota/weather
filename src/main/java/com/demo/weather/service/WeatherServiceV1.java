package com.demo.weather.service;

import com.demo.weather.controller.WeatherControllerV1;
import com.demo.weather.exception.CustomRequestException;
import com.demo.weather.model.Data;
import com.demo.weather.model.WarningType;
import com.demo.weather.model.WeatherDTO;
import com.demo.weather.model.WeatherResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


@Service
public class WeatherServiceV1 {

    private  static final Logger LOGGER = LoggerFactory.getLogger(WeatherServiceV1.class);

    @Value("${api.key}")
    private  String API_KEY;

    @Value("${api.uri}")
    private String API_URI;


     @Autowired
     RestTemplate restTemplate;

    @Autowired
    WeatherServiceImpV1 weatherServiceImpV1;

    public ResponseEntity<WeatherResponse> getWeatherForecast(Data data){

        String location =data.getServiceAttributes().getLocation();
        String cnt= String.valueOf(data.getMaxRecordsDetails().getMaxCount());

        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        WeatherResponse weatherResponse = new WeatherResponse();
        try{
            ResponseEntity<String> responseEntity = restTemplate.exchange(API_URI, HttpMethod.GET,entity, String.class,location,API_KEY,cnt
            );
            JSONObject jsonObject = new JSONObject(responseEntity.getBody());
            JSONArray forecasts = jsonObject.getJSONArray("list");

            List<WeatherDTO> weatherDTOList = new ArrayList<>();

            for(int i = 0; i<forecasts.length() && i < 3; i++){
                JSONObject forcast = forecasts.getJSONObject(i);

                long timestamp = forcast.getLong("dt")*1000;

                WeatherDTO weatherDTO = new WeatherDTO();
                Date date = new Date(timestamp);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                double tempHigh = forcast.getJSONObject(("main")).getDouble("temp_max");
                double tempLow= forcast.getJSONObject(("main")).getDouble("temp_min");

                boolean rain =forcast.has("rain");
                double windSpeed = forcast.getJSONObject("wind").getDouble("speed");
                boolean thunderstorm = forcast.getJSONArray("weather").getJSONObject(0).getString("main").equals("Thunderstorm");

                if(rain){
                    weatherServiceImpV1.setWeatherProcessingStrategy(new RainProcessingStrategy());
                }
                else if(tempHigh>40){
                 weatherServiceImpV1.setWeatherProcessingStrategy(new HighTemperatureProcessingStrategy());
                }
                else if(windSpeed >10){
                    weatherServiceImpV1.setWeatherProcessingStrategy(new HighWindsProcessingStrategy());
                }
                else if(thunderstorm){
                    weatherServiceImpV1.setWeatherProcessingStrategy(new ThunderstormProcessingStrategy());
                }
                weatherDTO.setDate(simpleDateFormat.format(date));
                weatherDTO.setMaxTemperature(String.valueOf(tempHigh));
                weatherDTO.setMinTemperature(String.valueOf(tempLow));

                String message =weatherServiceImpV1.processWeatherData();
                if(!message.isEmpty()){
                    weatherDTO.setAdvisoryMessage(message);
                }
                LOGGER.info(weatherDTO.toString());
                weatherDTOList.add(weatherDTO);

            }
            weatherResponse.setWeatherDTOList(weatherDTOList);

        }catch (Exception e){
            throw new CustomRequestException(new WarningType("Exception occured","500",e.getMessage(),"exception"));
        }

        return ResponseEntity.ok(weatherResponse);

    }
}
