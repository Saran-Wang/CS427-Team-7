package edu.uiuc.cs427app.Network.Entity;

//credit :https://kylewbanks.com/blog/tutorial-parsing-json-on-android-using-gson-and-volley

public class Forecast {
    private double latitude;
    private double longitude;
    private String timezone;
    private String timezone_abbreviation;
    private  CurrentWeather current;
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTimezone_abbreviation() {
        return timezone_abbreviation;
    }

    public void setTimezone_abbreviation(String timezone_abbreviation) {
        this.timezone_abbreviation = timezone_abbreviation;
    }

    /*
        https://api.open-meteo.com/v1/forecast?latitude=40.114044&longitude=-88.28781&current=temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,wind_speed_10m,wind_direction_10m&timezone=auto
        {
        "latitude":40.114044,
        "longitude":-88.28781,
        "generationtime_ms":1.1049509048461914,
        "utc_offset_seconds":-18000,
        "timezone":"America/Chicago",
        "timezone_abbreviation":"CDT",
        "elevation":225.0,
        "current_units":
            {
                "time":"iso8601",
                "interval":"seconds",
                "temperature_2m":"°C",
                "relative_humidity_2m":"%",
                "apparent_temperature":"°C",
                "weather_code":"wmo code",
                "wind_speed_10m":"km/h",
                "wind_direction_10m":"°"
             },
             "current":
             {
                "time":"2023-11-04T01:45",
                "interval":900,
                "temperature_2m":11.0,
                "relative_humidity_2m":48,
                "apparent_temperature":6.4,
                "weather_code":0,
                "wind_speed_10m":17.5,
                "wind_direction_10m":212
             }
         }
         */

    public CurrentWeather getCurrent() {
        return current;
    }

    public void setCurrent(CurrentWeather current) {
        this.current = current;
    }

    public class CurrentWeather {
        private String time;
        private double temperature_2m;
        private double relative_humidity_2m;
        private int weather_code;
        private double wind_speed_10m;
        private double wind_direction_10m;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public double getTemperature_2m() {
            return temperature_2m;
        }

        public void setTemperature_2m(double temperature_2m) {
            this.temperature_2m = temperature_2m;
        }

        public double getRelative_humidity_2m() {
            return relative_humidity_2m;
        }

        public void setRelative_humidity_2m(double relative_humidity_2m) {
            this.relative_humidity_2m = relative_humidity_2m;
        }

        public String getWeatherCode() {
            /*
            Weather variable documentation
            WMO Weather interpretation codes (WW)
            Code	Description
            0	Clear sky
            1, 2, 3	Mainly clear, partly cloudy, and overcast
            45, 48	Fog and depositing rime fog
            51, 53, 55	Drizzle: Light, moderate, and dense intensity
            56, 57	Freezing Drizzle: Light and dense intensity
            61, 63, 65	Rain: Slight, moderate and heavy intensity
            66, 67	Freezing Rain: Light and heavy intensity
            71, 73, 75	Snow fall: Slight, moderate, and heavy intensity
            77	Snow grains
            80, 81, 82	Rain showers: Slight, moderate, and violent
            85, 86	Snow showers slight and heavy
            95 *	Thunderstorm: Slight or moderate
            96, 99 *	Thunderstorm with slight and heavy hail
            (*) Thunderstorm forecast with hail is only available in Central Europe
             */
            int wmo = getWeather_code();
            if(wmo == 0)
                return "Clear sky";

            if(wmo == 1 || wmo == 2 || wmo == 3)
                return "Mainly clear, partly cloudy, and overcast";

            if(wmo == 45 || wmo == 48)
                return "Fog and depositing rime fog";

            if(wmo == 51 || wmo == 53 || wmo == 55)
                return "Drizzle: Light, moderate, and dense intensity";

            if(wmo == 56 || wmo == 57)
                return "Freezing Drizzle: Light and dense intensity";

            if(wmo == 61 || wmo == 63 || wmo == 65)
                return "Rain: Slight, moderate and heavy intensity";

            if(wmo == 66 || wmo == 67)
                return "Freezing Rain: Light and heavy intensity";

            if(wmo == 71 || wmo == 73 || wmo == 75)
                return "Snow fall: Slight, moderate, and heavy intensity";

            if(wmo == 77)
                return "Snow grains";

            if(wmo == 80 || wmo == 81 || wmo == 82)
                return "Rain showers: Slight, moderate, and violent";

            if(wmo == 85 || wmo == 86)
                return "Snow showers slight and heavy";

            if(wmo == 95)
                return "Thunderstorm: Slight or moderate";

            if(wmo == 96 || wmo == 99)
                return "Thunderstorm with slight and heavy hail";

            return "";
        }
        public int getWeather_code() {
            return weather_code;
        }

        public void setWeather_code(int weather_code) {
            this.weather_code = weather_code;
        }

        public double getWind_speed_10m() {
            return wind_speed_10m;
        }

        public void setWind_speed_10m(double wind_speed_10m) {
            this.wind_speed_10m = wind_speed_10m;
        }

        public double getWind_direction_10m() {
            return wind_direction_10m;
        }

        public void setWind_direction_10m(double wind_direction_10m) {
            this.wind_direction_10m = wind_direction_10m;
        }
        public String getWindDirection() {
            double degree = getWind_direction_10m();

            if (degree > 337.5) {
                return "Northerly";
            }
            if (degree > 292.5) {
                return "North Westerly";
            }
            if (degree > 247.5) {
                return "Westerly";
            }
            if (degree > 202.5) {
                return "South Westerly";
            }
            if (degree > 157.5) {
                return "Southerly";
            }
            if (degree > 122.5) {
                return "South Easterly";
            }
            if (degree > 67.5) {
                return "Easterly";
            }
            if (degree > 22.5) {
                return "North Easterly";
            }
            return "Northerly";
        }

    }
}
