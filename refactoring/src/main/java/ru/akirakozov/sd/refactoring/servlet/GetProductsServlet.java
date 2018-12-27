package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.DBLayer;
import ru.akirakozov.sd.refactoring.HttpCall;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        new HttpCall(new DBLayer(), response).getAll();
    }
}
