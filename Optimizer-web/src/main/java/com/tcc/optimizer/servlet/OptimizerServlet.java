/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcc.optimizer.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neo.commons.NeoLogger;
import com.tcc.optimizer.business.PlacesHandlerLocal;
import com.tcc.optimizer.constants.Options;
import com.tcc.optimizer.dto.Parameters;
import com.tcc.optimizer.dto.WebQuery;
import com.tcc.optimizer.processing.Optimizer;
import com.tcc.optimizer.processing.OptimizerLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;

/**
 *
 * @author luiz
 */
public class OptimizerServlet extends HttpServlet {

    private static final NeoLogger LOGGER = NeoLogger.getLogger(OptimizerServlet.class);
    
    private final Gson gson = new GsonBuilder().create();

    @EJB
    private PlacesHandlerLocal placesHandler;
    
    @EJB
    private OptimizerLocal optim;
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { 
        LOGGER.info("Incoming Request from: " + request.getRemoteHost() + "; at: " + request.getRemoteAddr());
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            try {
                if (request.getMethod().equals(HttpMethod.POST)) {
                    WebQuery webQuery = gson.fromJson(request.getReader(), WebQuery.class);
                    LOGGER.info("Request: " + webQuery.toString());

                    if(webQuery.getOption() != null && webQuery.getOption().equals(Options.OPTIMIZE)) {
                        /* TODO output your page here. You may use following sample code. */

                        /*List<List<Long>> listOfCategoriesOfIds = new ArrayList<>();
                        List<Long> restaurantList = new ArrayList<>();
                        List<Long> bankList = new ArrayList<>();
                        List<Long> groceryList = new ArrayList<>();
                        restaurantList.add(1L);
                        listOfCategoriesOfIds.add(restaurantList);*/

                        Parameters parameters = new Parameters();
                        parameters.setGroceriesSize(webQuery.getGroceriesSize());
                        parameters.setGroupSize(webQuery.getGroupSize());
                        parameters.setIssue(webQuery.getIssue());
                        parameters.setSpecialDate(webQuery.getSpecialDate());
                        parameters.setTask(webQuery.getTask());
                        parameters.setTimeArrival(webQuery.getTimeArrival());
                        parameters.setX(webQuery.getX());
                        parameters.setY(webQuery.getY());

                        List<Long> result = optim.optimize(webQuery.getListOfCategoriesOfIds(), parameters);

                        gson.toJson(result, out);
                    }
                } else {
                    if (request.getParameter("option").equals(Options.PLACES)) {
                        gson.toJson(placesHandler.getAllPlaces(), out);
                    }
                }
            } catch (Exception ex) {
                LOGGER.error("Ruim", ex);
                out.write("Falhou; provavelmente requisição mal formatada");
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
