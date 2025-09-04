package com.isitech.bibliotheque.web;

import com.isitech.bibliotheque.models.Livre;
import com.isitech.bibliotheque.services.BibliothequeService;

import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

public class BibliothequeServlet extends HttpServlet {
    private BibliothequeService bibliotheque;

    @Override
    public void init() {
        bibliotheque = new BibliothequeService("Bibliothèque Centrale");
        try {
            bibliotheque.ajouterLivre(new Livre("1", "Java Facile", "Auteur A", 300, "Éditions Tech", LocalDate.of(2020, 5, 1)));
            bibliotheque.ajouterLivre(new Livre("2", "Maths pour Tous", "Auteur B", 200, "Éditions Math", LocalDate.of(2019, 3, 15)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<html><head><title>Bibliothèque</title></head><body>");
        out.println("<h1>Livres disponibles</h1><ul>");

        for (Livre livre : bibliotheque.obtenirTousLesLivres()) {
            out.printf("<li>%s - %s</li>%n", livre.getTitre(), livre.getAuteur());
        }

        out.println("</ul></body></html>");
    }
}