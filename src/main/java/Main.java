import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {
    public static void main(String[] args) {
        
        System.out.println("Running");

        Wallpaper img = new Wallpaper();

        try {
            java.net.URI uri = java.net.URI.create(
                    "https://api.open-meteo.com/v1/forecast?latitude=40.8&longitude=-96.667&hourly=temperature_2m,precipitation_probability&current=temperature_2m,apparent_temperature,precipitation&timezone=America%2FChicago&wind_speed_unit=mph&temperature_unit=fahrenheit&precipitation_unit=inch");
            URL url = uri.toURL();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200)
                throw new RuntimeException("HttpResponseCode: " + responseCode);

            StringBuilder informationString = new StringBuilder();
            Scanner s = new Scanner(url.openStream());

            while (s.hasNext()) {
                informationString.append(s.nextLine());
            }

            s.close();

            JSONParser parse = new JSONParser();
            JSONObject dataObject = (JSONObject) parse.parse(informationString.toString());

            JSONObject hourly = (JSONObject) dataObject.get("hourly");
            JSONArray hoursArray = (JSONArray) hourly.get("time");
            JSONArray temperatureArray = (JSONArray) hourly.get("temperature_2m");
            JSONArray precipitationArray = (JSONArray) hourly.get("precipitation_probability");

            StringBuilder out = new StringBuilder();

            String[] forecast = new String[7];

            int hour = 0;
            int day = 0;
            for (int i = 0; i < hoursArray.size(); i++) {
                if (hour == 0)
                    out.append(String.format("%s :  %s°F %3s%%\n", convertTime((String) hoursArray.get(i)),
                            temperatureArray.get(i), precipitationArray.get(i)));
                else
                    out.append(String.format("%s :  %s°F %3s%%\n", convertTimeNoDate((String) hoursArray.get(i)),
                            temperatureArray.get(i), precipitationArray.get(i)));

                if (hour == 23) {
                    hour = 0;
                    forecast[day] = out.toString();
                    out.delete(0, out.length());
                    day++;
                } else
                    hour++;
            }

            img.update(forecast);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String convertTime(String t) {
        String year = t.substring(0, 4);
        String month = t.substring(5, 7);
        String day = t.substring(8, 10);
        String hour = t.substring(11, 13);
        int h = Integer.parseInt(hour);

        Calendar c = Calendar.getInstance();
        c.set(1900 + Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        int weekdayNum = c.get(Calendar.DAY_OF_WEEK);

        String weekday = switch (weekdayNum) {
            case 5 -> "Sunday";
            case 6 -> "Monday";
            case 7 -> "Tuesday";
            case 1 -> "Wednesday";
            case 2 -> "Thursday";
            case 3 -> "Friday";
            case 4 -> "Saturday";
            default -> "Error";
        };

        String time = "AM";
        if (h == 0)
            h = 12;
        else if (h > 12) {
            h -= 12;
            time = "PM";
        }

        return String.format("%s\n%s-%s-%s\n%2d %s", weekday, month, day, year, h, time);
    }

    static String convertTimeNoDate(String t) {
        String hour = t.substring(11, 13);
        int h = Integer.parseInt(hour);
        String time = "AM";
        if (h == 0) {
            h = 12;
        } else if (h > 12) {
            h -= 12;
            time = "PM";
        }
        return String.format("%2d %s", h, time);
    }
}
