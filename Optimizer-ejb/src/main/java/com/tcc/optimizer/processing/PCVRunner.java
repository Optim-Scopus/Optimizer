/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcc.optimizer.processing;

import com.neo.commons.NeoLogger;
import com.tcc.pcv_ejb.AG;
import com.tcc.pcv_ejb.GerenciadorCategoria;
import com.tcc.pcv_ejb.dto.Cidade;
import com.tcc.pcv_ejb.PCVStrategy;
import com.tcc.pcv_ejb.Populacao;
import com.tcc.pcv_ejb.Tour;
import com.tcc.pcv_ejb.calculadores_peso.CalculadorPeso_Regressao;
import com.tcc.pcv_ejb.dto.Categoria;
import com.tcc.pcv_ejb.geradores_individuos.GeradorIndividuo_Default;
import com.tcc.pcv_ejb.geradores_crossover.GeradorCrossover_Default;
import com.tcc.pcv_ejb.mutadores.Mutador_Default;
import java.util.List;

/**
 *
 * @author luiz
 */
public class PCVRunner implements Runnable {
    private static final NeoLogger LOGGER = NeoLogger.getLogger(PCVRunner.class);

    private PCVStrategy strat;
    
    private GerenciadorCategoria gc;
    private Populacao pop;
    
    private boolean done = false;
    
    public PCVRunner(List<List<Cidade>> categories, double timeArrival, Cidade userLocal){
        gc = new GerenciadorCategoria();
        int nCat = categories.size();
        
        strat = new PCVStrategy(
            new Mutador_Default(), 
            new CalculadorPeso_Regressao(timeArrival, userLocal), 
            new GeradorIndividuo_Default(nCat, gc),
            new GeradorCrossover_Default(),
            nCat
        );
        
        for (List<Cidade> cityList : categories) {
            Categoria categorie = new Categoria();
            categorie.addCidades(cityList);
            LOGGER.info("Categoria: " + categorie.toString());
            gc.addCategoria(categorie);
        }
        
        pop = new Populacao(1000, true, strat);
    }
    
    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; i++) {
                pop = AG.evolvePopulacao(pop, strat);
            }
        } catch (Exception ex) {
            LOGGER.error("Não entendeu", ex);
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
