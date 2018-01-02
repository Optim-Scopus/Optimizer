/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcc.optimizer.processing;

import com.neo.commons.NeoLogger;
import com.neo.commons.Utils;
import com.tcc.optimizer.business.PlacesHandlerLocal;
import com.tcc.optimizer.dto.Parameters;
import com.tcc.optimizer.entity.places;
import com.tcc.pcv_ejb.dto.Cidade;
import com.tcc.pcv_ejb.dto.CidadeType;
import com.tcc.pcv_ejb.dto.RegressorInputDto;
import java.util.ArrayList;
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
    


    @Override
    public List<Long> optimize(List<List<Long>> listOfCategoriesOfIds, Parameters parameters) {
        int i = 0;
        List<List<Cidade>> categories = new ArrayList<>();
        for (List<Long> CitiesIdsByCat : listOfCategoriesOfIds) {
            List<Cidade> cidades = getCidadesByIdsList(CitiesIdsByCat, i);
            categories.add(cidades);
            i++;
        }
        
        Cidade userLocal = new Cidade(-1L, parameters.getX(), parameters.getY());
        
        setRegressorDto(parameters);
        
        PCVRunner runner = new PCVRunner(categories, parameters.getTimeArrival(), userLocal);
        LOGGER.info("Optimizing, starting thread");
        runner.run();
        
        //waitCalcs(runner);

        LOGGER.info("Optimizing, ended threads");
        return runner.getFittestTour().getCitiesAsIdsList();

    }

    /*private List<List<Long>> permute(List<List<Long>> list) {
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
    }*/

    /*private void waitCalcs(PCVRunner runner) {
        while (true) {
            if (runner.isDone()) {
                break;
            } else {
                Utils.wait(100);
            }
        }
    }*/

    /*private Tour findFittest() {
        Tour fittest = runners.get(0).getFittestTour();
        for (PCVRunner runner : runners) {
            // Loop through runners to find fittest
            if (fittest.getFitness() <= runner.getFittestTour().getFitness()) {
                fittest = runner.getFittestTour();
            }

        }
        return fittest;

    }*/
    
    private List<Cidade> getCidadesByIdsList(List<Long> citiesIds, int categoriaNLoop) {
        List<Cidade> returnList = new ArrayList<>();
        
        for (Long cityId : citiesIds) {
            returnList.add(getCityWithId(cityId, categoriaNLoop));
        }
        
        return returnList;
    }
    
    private Cidade getCityWithId(Long id, int categoriaNLoop) {
        
        places place = placesHandler.getPlaceById(id);
        
        Cidade returnCity;
        CidadeType type;
        switch (place.getCategory().intValue()) {
                /*returnCity = new Cidade(place.getId(), CidadeType.Restaurant, 
                        place.getLatitude().intValue(), place.getLongitude().intValue(), 
                        dow, parameters.getGroupSize(), parameters.getIssue(), parameters.getSpecialDate());*/
            case 3: // Bank
                type = CidadeType.Bank;
                /*returnCity = new Cidade(place.getId(), CidadeType.Bank, 
                        place.getLatitude().intValue(), place.getLongitude().intValue(),
                        dow, parameters.getTask(), parameters.getIssue(), true);*/
                break;
            case 4: // Grocery
                type = CidadeType.Groceries;
                /*returnCity = new Cidade(place.getId(), CidadeType.Groceries, 
                        place.getLatitude().intValue(), place.getLongitude().intValue(),
                        dow, parameters.getGroceriesSize(), parameters.getIssue());*/
                break;
            case 2: // Restaurant
            default:
                type = CidadeType.Restaurant;
                break;
        }
        returnCity = new Cidade(place.getId(), place.getLatitude(), 
                place.getLongitude(), type, categoriaNLoop);
        
        return returnCity;
    }
    
    private void setRegressorDto(Parameters parameters){
        RegressorInputDto dto = RegressorInputDto.getDto();
        dto.setDow(dayOfWeek());
        dto.setGroceriesSize(parameters.getGroceriesSize());
        dto.setGroupSize(parameters.getGroupSize());
        dto.setIssue(parameters.getIssue());
        dto.setSpecialDate(parameters.getSpecialDate());
        dto.setTask(parameters.getTask());
    }
    
    private int dayOfWeek() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK);
    }
}
