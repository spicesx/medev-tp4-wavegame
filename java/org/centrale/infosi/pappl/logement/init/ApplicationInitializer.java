/* -----------------------------------------
 * Projet Kepler
 *
 * Ecole Centrale Nantes
 * Jean-Yves MARTIN
 * ----------------------------------------- */
package org.centrale.infosi.pappl.logement.init;


import java.util.Collection;
import jakarta.servlet.*;
import jakarta.persistence.*;


/**
 * Initialize data for Kepler server<br/>
 * Launched at startup
 *
 * @author kwyhr
 */
public class ApplicationInitializer implements ServletContextListener {

    private static final String PUNAME = "org.centrale.infosi.pappl.Mission_Logement_1.0PU";
    private static EntityManager em;

    /**
     * Initialise
     *
     * @param event
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();

        // Connect to the persistence manager
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PUNAME);
        if (emf != null) {
            // Empty cache to restart from new data
            Cache theCache = emf.getCache();
            theCache.evictAll();

            // Create entity manager
            em = emf.createEntityManager();
            if (em != null) {
                
                // close
                em.close();
            }
            emf.close();
        }
    }

    /**
     * End context
     *
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    /* ----------------------------------------------------------------------- */
    /**
     * Create default values in Database. This is called once, when application
     * starts
     */

    /* ----------------------------------------------------------------------- */
    private Object getItemFromInt(int id, String requestName, Class classType, String fieldName) {
        Object item = null;
        try {
            Query theQuery = em.createNamedQuery(requestName, classType);
            theQuery.setParameter(fieldName, id);
            item = theQuery.getSingleResult();
        } catch (NoResultException ex) {
        }
        return item;
    }

    private Collection getCollection(String requestName, Class classType) {
        Collection item = null;
        try {
            Query theQuery = em.createNamedQuery(requestName, classType);
            item = theQuery.getResultList();
        } catch (NoResultException ex) {
        }
        return item;
    }

    private Object getItemFromString(String value, String requestName, Class classType, String fieldName) {
        Object item = null;
        try {
            Query theQuery = em.createNamedQuery(requestName, classType);
            theQuery.setParameter(fieldName, value);
            item = theQuery.getSingleResult();
        } catch (NoResultException ex) {
        }
        return item;
    }

    private void resetTable(String tableName) {
        resetTable(tableName, (String)null);
    }

    private void resetTable(String tableName, boolean withSequence) {
        if (withSequence) {
            String sequenceName = "public." + tableName.toLowerCase() + "_" + tableName.toLowerCase() + "_id_seq";
            resetTable(tableName, sequenceName);
        } else {
            resetTable(tableName, null);
        }
    }

    private void resetTable(String tableName, String sequenceName) {
        Query theQuery = em.createNativeQuery("TRUNCATE public." + tableName + " CASCADE");
        theQuery.executeUpdate();
        if (sequenceName != null) {
            theQuery = em.createNativeQuery("SELECT pg_catalog.setval('" + sequenceName + "', 1, false)");
            theQuery.getResultList();
        }
        em.flush();
    }

    /* ----------------------------------------------------------------------- */
}
