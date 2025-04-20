package com.example.sbase.Service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class CountryService {
    public List<String> getAllCountries(){
        String[] countries = Locale.getISOCountries();
        List<String> countryList = new ArrayList<>();
        for(String country : countries){
            Locale locale = new Locale("", country);
            String countryName = locale.getDisplayCountry();
            if (!countryName.isEmpty()) {
                countryList.add(countryName);
            }
        }
        return countryList;
    }
}
