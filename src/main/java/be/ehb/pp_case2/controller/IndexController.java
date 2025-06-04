
package be.ehb.pp_case2.controller;


import be.ehb.pp_case2.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.*;


        @Controller
        public class IndexController {


            @Autowired
            private WeatherService weatherService;


            @GetMapping("/")
            public String index(Model model) {
                // Historische dataset 2005-2024
                List<Map<String, Object>> regenData = new ArrayList<>();


                int[][] data = {
                        {2005, 67, 54, 73, 68, 74, 79, 98, 89, 68, 98, 98, 108},
                        {2006, 66, 45, 61, 57, 70, 82, 85, 78, 77, 94, 100, 103},
                        {2007, 62, 56, 64, 62, 72, 79, 87, 94, 70, 90, 104, 104},
                        {2008, 66, 45, 63, 61, 79, 81, 89, 83, 63, 91, 98, 115},
                        {2009, 67, 46, 72, 58, 72, 83, 95, 90, 66, 93, 102, 115},
                        {2010, 63, 54, 64, 54, 79, 87, 90, 90, 72, 92, 102, 118},
                        {2011, 65, 63, 57, 64, 75, 79, 90, 75, 69, 97, 107, 107},
                        {2012, 61, 52, 75, 62, 72, 83, 90, 90, 66, 93, 98, 103},
                        {2013, 66, 56, 70, 59, 68, 78, 88, 81, 69, 97, 109, 111},
                        {2014, 66, 55, 60, 60, 75, 92, 89, 87, 70, 89, 106, 114},
                        {2015, 69, 50, 77, 53, 78, 91, 85, 82, 70, 92, 92, 110},
                        {2016, 60, 57, 65, 68, 71, 78, 94, 79, 71, 102, 92, 111},
                        {2017, 66, 59, 64, 53, 78, 81, 91, 87, 67, 96, 101, 106},
                        {2018, 74, 57, 64, 63, 70, 84, 96, 81, 75, 97, 104, 119},
                        {2019, 64, 51, 66, 56, 75, 82, 91, 89, 70, 102, 99, 124},
                        {2020, 68, 51, 65, 62, 74, 84, 92, 85, 66, 87, 98, 114},
                        {2021, 66, 49, 71, 62, 71, 81, 90, 79, 72, 98, 105, 115},
                        {2022, 58, 50, 73, 63, 78, 99, 93, 91, 75, 98, 98, 114},
                        {2023, 61, 54, 68, 60, 87, 71, 93, 77, 68, 100, 100, 105},
                        {2024, 61, 58, 66, 61, 75, 77, 101, 88, 60, 96, 97, 114}
                };


                String[] maanden = {"jaar", "jan", "feb", "mrt", "apr", "mei", "jun", "jul", "aug", "sep", "okt", "nov", "dec"};


                for (int[] rij : data) {
                    Map<String, Object> jaarData = new LinkedHashMap<>();
                    for (int i = 0; i < maanden.length; i++) {
                        jaarData.put(maanden[i], rij[i]);
                    }
                    regenData.add(jaarData);
                }
                model.addAttribute("regenData", regenData);


                // Open-Meteo API-voorspelling voor Brussel
                String regenVoorspelling = weatherService.getRegenVooruitzicht();
                model.addAttribute("regenVoorspelling", regenVoorspelling);


                return "index";
            }
        }
