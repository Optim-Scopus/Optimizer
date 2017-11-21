/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcc.optimizer.processing;

import com.neo.commons.NeoLogger;
import com.tcc.pcv_ejb.AG;
import com.tcc.pcv_ejb.cities_info.Cidade;
import com.tcc.pcv_ejb.GerenciadorTour;
import com.tcc.pcv_ejb.PCVStrategy;
import com.tcc.pcv_ejb.Populacao;
import com.tcc.pcv_ejb.Tour;
import com.tcc.pcv_ejb.calculadores_peso.CalculadorPeso_Regressao;
import com.tcc.pcv_ejb.geradores_individuos.GeradorIndividuo_Default;
import com.tcc.pcv_ejb.geradores_populacoes.GeradorPopulacao_Default;
import com.tcc.pcv_ejb.mutadores.Mutador_Default;
import java.util.List;

/**
 *
 * @author luiz
 */
public class PCVRunner implements Runnable {
    private static final NeoLogger LOGGER = NeoLogger.getLogger(PCVRunner.class);

    private final PCVStrategy strat;
    
    private final GerenciadorTour gt;
    private Populacao pop;
    
    private boolean done = false;
    
    public PCVRunner(List<Cidade> cities){
        gt = new GerenciadorTour();
        int popSize = cities.size();
        
        strat = new PCVStrategy(
            new Mutador_Default(), 
            new CalculadorPeso_Regressao(), 
            new GeradorIndividuo_Default(popSize, gt),
            new GeradorPopulacao_Default(),
            popSize
        );
        
        for (Cidade city : cities) {
            gt.addCidade(city);
            LOGGER.info("City: " + city.getId());
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
    
}
