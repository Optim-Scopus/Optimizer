/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcc.optimizer.dao;

import com.neo.commons.NeoLogger;
import com.tcc.optimizer.entity.places;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author luiz
 */
@Stateless
public class PlacesDao extends GenericDaoImpl<places, Long> implements PlacesDaoLocal {
    
    private static final NeoLogger LOGGER = NeoLogger.getLogger(PlacesDao.class);

    @Override
    public places getPlaceById(Long id) {
        Query q = this.getEntityManager().createQuery(
                "select x from " + this.getPersistentClass().getSimpleName() + " as x WHERE "
                        + "x.id = ?1"
                        + " ORDER BY x.id");
        q.setParameter("1", id);
        try {
            return (places) q.getSingleResult();
        } catch (Exception ex) {
            LOGGER.error("No account found with specified password", ex);
            return null;
        }
    }
}
