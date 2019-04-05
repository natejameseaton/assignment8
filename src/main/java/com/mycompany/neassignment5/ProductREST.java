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

// http://localhost:8080/NEAssignment5/api/product

@Path("product")
@ApplicationScoped
public class ProductREST {

    @PersistenceContext(unitName = "builditNE5")
    private EntityManager em;
    
    @Inject
    private UserTransaction transaction;

    // http://localhost:8080/NEApplication5/api/product
    /**
     * Uses a JPA Query to return the entire list as JSON.
     * @return List of Products
     */
    @GET
    @Produces({"application/json"})
    public List<Product> getAll() {
        List<Product> products = em.createQuery("SELECT r FROM Product r").getResultList();
        return products;
    }

    /**
     * Uses a JPA Named Query to return a specific entity as a list.
     * @param id the Product Code (ID)
     * @return 
     */
    @GET
    @Path("{id}")
    @Produces({"application/json"})
    public List<Product> getOne(@PathParam("id") int id) {
        Query q = em.createNamedQuery("findOneR");
        q.setParameter("productId", id);
        List<Product> products = q.getResultList();
        return products;
    }

    @POST
    @Consumes("application/json")
    public void addOne(Product product) {
        try {
            transaction.begin();
            em.persist(product);
            transaction.commit();
        } catch (Exception ex) {
            Logger.getLogger(ProductREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public void editOne(Product product, @PathParam("id") int id) {
        try {
            Query q = em.createQuery("SELECT r FROM Product r WHERE r.productId = :id");
            q.setParameter("id", id);
            Product savedR = (Product) q.getSingleResult();
            savedR.setProductCode(product.getProductCode());
            savedR.setDescription(product.getDescription());
            transaction.begin();
            em.merge(savedR);
            transaction.commit();
        } catch (Exception ex) {
            Logger.getLogger(ProductREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Finds and deletes an existing record using the JPA's find method.
     * @param id 
     */
    @DELETE
    @Path("{id}")
    public void deleteOne(@PathParam("id") int id) {
        try {
            transaction.begin();
            Product found = em.find(Product.class, id);
            em.remove(found);
            transaction.commit();
        } catch (Exception ex) {
            Logger.getLogger(ProductREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
