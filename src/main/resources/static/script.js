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
            console.error('Error fecthing weather forecast:', error);
        });
}


function displayForecast(data) {
    var forecastOutput = document.getElementById('forecastOutput');
    forecastOutput.innerHTML = ''; // Clear previous forecast data

    if (data.weatherDTOList && data.weatherDTOList.length > 0) {
        data.weatherDTOList.forEach(day => {
            var forecastText = '';
            forecastText += `<div>Date: ${day.date}</div>`;
            forecastText += `<div>Max Temperature: ${day.maxTemperature}°C</div>`;
            forecastText += `<div>Min Temperature: ${day.minTemperature}°C</div>`;
            if (day.advisoryMessage) {
                forecastText += `<div>Advisory: ${day.advisoryMessage}</div>`;
            }
            forecastOutput.innerHTML += forecastText;
        });
    } else {
        forecastOutput.textContent = 'No forecast data available';
    }
}
