package io.covid.coronavirustracker.controller;

import io.covid.coronavirustracker.models.LocationStats;
import io.covid.coronavirustracker.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class SortbyCases implements Comparator<LocationStats>
{
    @Override
    public int compare(LocationStats a, LocationStats b){
        return b.getDiffFromPrevDay()-a.getDiffFromPrevDay();
    }


}
@Controller
public class HomeController {
    @Autowired
    CoronaVirusDataService coronaVirusDataService;

    @GetMapping("/")

    public String home(Model model){
         List<LocationStats> allStats=coronaVirusDataService.getAllStats();
         List<LocationStats> toBeSortedCases=new ArrayList<LocationStats>(allStats);
         Collections.sort(toBeSortedCases,new SortbyCases());
         List<LocationStats> topTenHit=new ArrayList<LocationStats>();
         for(int i=0;i<10;i++){
             topTenHit.add(toBeSortedCases.get(i));
         }
         int totalReportedCases=allStats.stream().mapToInt(stat->stat.getLatestTotalCases()).sum();
         int totalNewCases=allStats.stream().mapToInt(stat->stat.getDiffFromPrevDay()).sum();
         model.addAttribute( "locationStats" ,allStats);
         model.addAttribute( "topTenHit" ,topTenHit);
         model.addAttribute( "totalReportedCases" ,totalReportedCases);
         model.addAttribute( "totalNewCases" ,totalNewCases);
         return "home";
    }
}
