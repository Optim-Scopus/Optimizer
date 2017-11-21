/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcc.optimizer.business;

import com.tcc.optimizer.entity.places;
import javax.ejb.Local;

/**
 *
 * @author luiz
 */
@Local
public interface PlacesHandlerLocal {
    
    public places getPlaceById(Long id);
    public String getFullNameById(Long id);
}
