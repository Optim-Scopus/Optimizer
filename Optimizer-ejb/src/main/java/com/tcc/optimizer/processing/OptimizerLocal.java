/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcc.optimizer.processing;

import com.tcc.optimizer.dto.HistoryDto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author luiz
 */
@Local
public interface OptimizerLocal {
    public List<Long> optimize(List<List<Long>> listOfCategoriesOfIds, HistoryDto parameters);
}
