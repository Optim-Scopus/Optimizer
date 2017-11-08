/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcc.optimizer.processing;

import com.neo.commons.Utils;
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
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author luiz
 */
public class Optimizer {
    
    private List<List<Integer>> permutations = null;
    private List<PCVRunner> runners;
        
    public List<Integer> optimize(List<List<Integer>> listOfCategoriesOfIds) {
        runners = new ArrayList<>();
        permutations = permute(listOfCategoriesOfIds);
        
        for (List<Integer> permutation : permutations) {
            PCVRunner runner = new PCVRunner(permutation);
            runners.add(runner);
            Thread t = new Thread(runner);
            t.start();
        }
        
        waitCalcs();
        
        return findFittest().getCitiesAsIdsList();
        
    }
    
    private List<List<Integer>> permute(List<List<Integer>> list) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        int numSets = list.size();
        Integer[] tmpResult = new Integer[numSets];

        permute(list, 0, tmpResult, result);

        return result;
    }
    
    private void permute(List<List<Integer>> list, int n, Integer[] tmpResult, List<List<Integer>> result)
    {
        if (n == list.size()) {
            result.add(new ArrayList<>(Arrays.asList(tmpResult)));
            return;
        }

        for (Integer i : list.get(n)) {
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
    
}
