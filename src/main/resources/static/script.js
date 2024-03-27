function fetchWeather() {
    var city= document.getElementById('cityInput').value;
    var requestBody = {
                        data: {
                          serviceAttributes: {
                            location: city
                          },
                          maxRecordsDetails: {
                            maxCount: 10
                          }
                        }
                      };


    fetch('/weather/api/weather-prediction/v1/forecast',{

        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody)})
        .then(response => response.json())
        .then(data => {
            displayForecast(data);
        })
        .catch((error) => {
            console.error('Error fetching weather forecast:', error);
        });
}


function displayForecast(data) {
    const forecastOutput = document.getElementById('forecastOutput');
    forecastOutput.innerHTML = ''; // Clear previous content

    if (data.weatherDTOList && data.weatherDTOList.length > 0) {
            data.weatherDTOList.forEach(day => {
                var forecastText = '';
                forecastText += `<div class="forecastDate">Date: ${day.date}</div>`;
                forecastText += `<div class="forecastTemperature">Max Temperature: ${day.maxTemperature}°C</div>`;
                forecastText += `<div class="forecastTemperature">Min Temperature: ${day.minTemperature}°C</div>`;
                if (day.advisoryMessage) {
                    forecastText += `<div class="forecastAdvisory">Advisory: ${day.advisoryMessage}</div>`;
                }
                forecastOutput.innerHTML += forecastText;
            });
       } else {
            forecastOutput.innerHTML = 'No forecast available !';
       }
}

