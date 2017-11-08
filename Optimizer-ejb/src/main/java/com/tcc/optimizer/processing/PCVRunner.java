/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcc.optimizer.processing;

import com.tcc.pcv.AG;
import com.tcc.pcv.Cidade;
import com.tcc.pcv.GerenciadorTour;
import com.tcc.pcv.PCVStrategy;
import com.tcc.pcv.Populacao;
import com.tcc.pcv.Tour;
import com.tcc.pcv.calculadores_peso.CalculadorPeso_Default;
import com.tcc.pcv.geradores_individuos.GeradorIndividuo_Default;
import com.tcc.pcv.geradores_populacoes.GeradorPopulacao_Default;
import com.tcc.pcv.mutadores.Mutador_Default;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author luiz
 */
public class PCVRunner implements Runnable {

    private final PCVStrategy strat;
    
    private final GerenciadorTour gt;
    private Populacao pop;
    
    private boolean done = false;
    
    public PCVRunner(List<Integer> citiesIds){
        gt = new GerenciadorTour();
        int popSize = citiesIds.size();
        
        strat = new PCVStrategy(
            new Mutador_Default(), 
            new CalculadorPeso_Default(), 
            new GeradorIndividuo_Default(popSize, gt),
            new GeradorPopulacao_Default(),
            citiesIds.size()
        );
        
        for (Integer cityId : citiesIds) {
            Cidade city = getCityWithId(cityId);
            gt.addCidade(city);
        }
        
        pop = new Populacao(popSize, true, strat);
    }
    
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            pop = AG.evolvePopulacao(strat, pop);
        }
        
        done = true;
    }
    
    public boolean isDone() {
        return done;
    }
    
    public Populacao getPop() {
        return pop;
    }
    
    public Tour getFittestTour(){
        return pop.getFittest();
    }
    
    private Cidade getCityWithId(Integer id) {
        return null ERROR TODO;
    }
    
}
