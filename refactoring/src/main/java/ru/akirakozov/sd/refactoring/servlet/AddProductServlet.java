package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.DBLayer;
import ru.akirakozov.sd.refactoring.HttpCall;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author akirakozov
 */
public class AddProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        int price = Integer.parseInt(request.getParameter("price"));
        new HttpCall(new DBLayer(), response).add(name, price);
    }
}
