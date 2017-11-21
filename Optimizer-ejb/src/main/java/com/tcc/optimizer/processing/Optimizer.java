/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcc.optimizer.processing;

import com.neo.commons.NeoLogger;
import com.neo.commons.Utils;
import com.tcc.optimizer.business.PlacesHandlerLocal;
import com.tcc.optimizer.dto.HistoryDto;
import com.tcc.optimizer.entity.places;
import com.tcc.pcv_ejb.cities_info.Cidade;
import com.tcc.pcv_ejb.Tour;
import com.tcc.pcv_ejb.cities_info.CidadeType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author luiz
 */
@Stateless
public class Optimizer implements OptimizerLocal {
    
    private static final NeoLogger LOGGER = NeoLogger.getLogger(Optimizer.class);
    
    @EJB
    private PlacesHandlerLocal placesHandler;

    private List<List<Long>> permutations = null;
    private List<PCVRunner> runners;

    @Override
    public List<Long> optimize(List<List<Long>> listOfCategoriesOfIds, HistoryDto parameters) {
        runners = new ArrayList<>();
        LOGGER.info("Optimizing, permuting");
        permutations = permute(listOfCategoriesOfIds);

        LOGGER.info("Optimizing, starting threads");
        for (List<Long> permutation : permutations) {
            List<Cidade> cidades = getCidadesByIdsList(permutation, parameters);
            PCVRunner runner = new PCVRunner(cidades);
            runners.add(runner);
            Thread t = new Thread(runner);
            t.start();
        }

        waitCalcs();

        LOGGER.info("Optimizing, ended threads");
        return findFittest().getCitiesAsIdsList();

    }

    private List<List<Long>> permute(List<List<Long>> list) {
        List<List<Long>> result = new ArrayList<>();
        int numSets = list.size();
        Long[] tmpResult = new Long[numSets];

        permute(list, 0, tmpResult, result);

        return result;
    }

    private void permute(List<List<Long>> list, int n, Long[] tmpResult, List<List<Long>> result) {
        if (n == list.size()) {
            result.add(new ArrayList<>(Arrays.asList(tmpResult)));
            return;
        }

        for (Long i : list.get(n)) {
            tmpResult[n] = i;
            permute(list, n + 1, tmpResult, result);
        }
    }

    private void waitCalcs() {
        int n = permutations.size();

        while (true) {
            int dones = 0;
            for (PCVRunner runner : runners) {
                if (runner.isDone()) {
                    dones++;
                }
            }
            if (dones == n) {
                break;
            } else {
                Utils.wait(100);
            }
        }
    }

    private Tour findFittest() {
        Tour fittest = runners.get(0).getFittestTour();
        for (PCVRunner runner : runners) {
            // Loop through runners to find fittest
            if (fittest.getFitness() <= runner.getFittestTour().getFitness()) {
                fittest = runner.getFittestTour();
            }

        }
        return fittest;

    }
    
    private List<Cidade> getCidadesByIdsList(List<Long> citiesIds, HistoryDto parameters) {
        List<Cidade> returnList = new ArrayList<>();
        
        for (Long cityId : citiesIds) {
            returnList.add(getCityWithId(cityId, parameters));
        }
        
        return returnList;
    }
    
    private Cidade getCityWithId(Long id, HistoryDto parameters) {
        
        places place = placesHandler.getPlaceById(id);
        
        Cidade returnCity = null;
        switch (place.getCategory().intValue()) {
            case 2: // Restaurant
                returnCity = new Cidade(place.getId(), CidadeType.Restaurant, 
                        place.getLatitude().intValue(), place.getLongitude().intValue(), 
                        dayOfWeek(), parameters.getGroupSize(), parameters.getIssue(), parameters.getSpecialDate());
                break;
            case 3: // Bank
                returnCity = new Cidade(place.getId(), CidadeType.Bank, 
                        place.getLatitude().intValue(), place.getLongitude().intValue(),
                        dayOfWeek(), parameters.getTask(), parameters.getIssue(), true);
                break;
            case 4: // Grocery
                returnCity = new Cidade(place.getId(), CidadeType.Groceries, 
                        place.getLatitude().intValue(), place.getLongitude().intValue(),
                        dayOfWeek(),parameters.getGroceriesSize(), parameters.getIssue());
                break;
            default:
                break;
        }
        
        return returnCity;
    }
    
    private int dayOfWeek() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK);
    }
}
