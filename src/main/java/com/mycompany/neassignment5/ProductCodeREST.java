/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.neassignment5;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author c0711874
 */

// http://localhost:8080/NEAssignment5/api/productCode

@Path("productCode")
@ApplicationScoped
public class ProductCodeREST {

    @PersistenceContext(unitName = "builditNE5")
    private EntityManager em;
    
    @Inject
    private UserTransaction transaction;

    // http://localhost:8080/NEApplication5/api/productCode
    /**
     * Uses a JPA Query to return the entire list as JSON.
     * @return List of ProductCodes
     */
    @GET
    @Produces({"application/json"})
    public List<ProductCode> getAll() {
        List<ProductCode> productCodes = em.createQuery("SELECT p FROM ProductCode p").getResultList();
        return productCodes;
    }

    /**
     * Uses a JPA Named Query to return a specific entity as a list.
     * @param id the Product Code (ID)
     * @return 
     */
    @GET
    @Path("{id}")
    @Produces({"application/json"})
    public List<ProductCode> getOne(@PathParam("id") String id) {
        Query q = em.createNamedQuery("findOne");
        q.setParameter("prodCode", id);
        List<ProductCode> productCodes = q.getResultList();
        return productCodes;
    }

    /**
     * Saves an object received as a JSON payload.
     * @param productCode 
     */
    @POST
    @Consumes("application/json")
    public void addOne(ProductCode productCode) {
        try {
            transaction.begin();
            em.persist(productCode);
            transaction.commit();
        } catch (Exception ex) {
            Logger.getLogger(ProductCodeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Updates an existing Product Code based on an incoming JSON payload.
     * @param productCode
     * @param id 
     */
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public void editOne(ProductCode productCode, @PathParam("id") String id) {
        try {
            Query q = em.createQuery("SELECT p FROM ProductCode p WHERE p.prodCode = :id");
            q.setParameter("id", id);
            ProductCode savedPC = (ProductCode) q.getSingleResult();
            savedPC.setDescription(productCode.getDescription());
            savedPC.setDiscountCode(productCode.getDiscountCode());
            transaction.begin();
            em.merge(savedPC);
            transaction.commit();
        } catch (Exception ex) {
            Logger.getLogger(ProductCodeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Finds and deletes an existing record using the JPA's find method.
     * @param id 
     */
    @DELETE
    @Path("{id}")
    public void deleteOne(@PathParam("id") String id) {
        try {
            transaction.begin();
            ProductCode found = em.find(ProductCode.class, id);
            em.remove(found);
            transaction.commit();
        } catch (Exception ex) {
            Logger.getLogger(ProductCodeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
