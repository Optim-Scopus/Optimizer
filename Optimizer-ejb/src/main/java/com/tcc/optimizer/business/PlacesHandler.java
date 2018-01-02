/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcc.optimizer.business;

import com.tcc.optimizer.dao.PlacesDaoLocal;
import com.tcc.optimizer.entity.places;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author luiz
 */
@Stateless
public class PlacesHandler implements PlacesHandlerLocal {
    
    @EJB
    private PlacesDaoLocal placesDao;

    @Override
    public places getPlaceById(Long id) {
        return placesDao.getPlaceById(id);
    }

    @Override
    public String getFullNameById(Long id) {
        return placesDao.getPlaceById(id).getFullname();
    }

    @Override
    public List<places> getAllPlaces() {
        return placesDao.readAll();
    }
}
