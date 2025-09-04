package com.isitech;

import com.isitech.bibliotheque.web.BibliothequeServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main {
    public static void main(String[] args) throws Exception {
        // Jetty démarre sur le port 8080
        Server server = new Server(8080);

        // Contexte racine "/"
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        // Ajout de ta servlet
        context.addServlet(new ServletHolder(new BibliothequeServlet()), "/");

        System.out.println("Serveur démarré : http://localhost:8080");

        server.start();
        server.join();
    }
}