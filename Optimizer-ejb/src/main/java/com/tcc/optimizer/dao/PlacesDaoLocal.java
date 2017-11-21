/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcc.optimizer.dao;

import com.tcc.optimizer.entity.places;
import javax.ejb.Local;

/**
 *
 * @author luiz
 */
@Local
public interface PlacesDaoLocal extends GenericDao<places, Long> {
    
    public places getPlaceById(Long id);
    
}
