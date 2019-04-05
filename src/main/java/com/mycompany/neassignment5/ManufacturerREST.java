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

// http://localhost:8080/NEAssignment5/api/manufacturer

@Path("manufacturer")
@ApplicationScoped
public class ManufacturerREST {

    @PersistenceContext(unitName = "builditNE5")
    private EntityManager em;
    
    @Inject
    private UserTransaction transaction;

    @GET
    @Produces({"application/json"})
    public List<Manufacturer> getAll() {
        List<Manufacturer> manufacturers = em.createQuery("SELECT m FROM Manufacturer m").getResultList();
        return manufacturers;
    }

    @GET
    @Path("{id}")
    @Produces({"application/json"})
    public List<Manufacturer> getOne(@PathParam("id") int id) {
        Query q = em.createNamedQuery("findOneM");
        q.setParameter("manufacturerId", id);
        List<Manufacturer> manufacturers = q.getResultList();
        return manufacturers;
    }

    @POST
    @Consumes("application/json")
    public void addOne(Manufacturer manufacturer) {
        try {
            transaction.begin();
            em.persist(manufacturer);
            transaction.commit();
        } catch (Exception ex) {
            Logger.getLogger(ManufacturerREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public void editOne(Manufacturer manufacturer, @PathParam("id") int id) {
        try {
            Query q = em.createQuery("SELECT m FROM Manufacturer m WHERE m.manufacturerId = :id");
            q.setParameter("id", id);
            Manufacturer savedM = (Manufacturer) q.getSingleResult();
            savedM.setName(manufacturer.getName());
            savedM.setName(manufacturer.getPhone());
            transaction.begin();
            em.merge(savedM);
            transaction.commit();
        } catch (Exception ex) {
            Logger.getLogger(ManufacturerREST.class.getName()).log(Level.SEVERE, null, ex);
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
            Manufacturer found = em.find(Manufacturer.class, id);
            em.remove(found);
            transaction.commit();
        } catch (Exception ex) {
            Logger.getLogger(ManufacturerREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
