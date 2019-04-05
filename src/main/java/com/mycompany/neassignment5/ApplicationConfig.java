/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.neassignment5;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author c0711874
 */
@ApplicationPath("api")
public class ApplicationConfig extends Application {
    public Set<Class<?>> getClasses() {
        Set<Class<?>> result = new java.util.HashSet<Class<?>>();
        result.add(ProductCodeREST.class);
        result.add(ManufacturerREST.class);
        result.add(ProductREST.class);
        return result;
    }
}
