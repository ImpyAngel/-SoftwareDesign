package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.DBLayer;
import ru.akirakozov.sd.refactoring.HttpCall;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String command = request.getParameter("command");
        HttpCall call = new HttpCall(new DBLayer(), response);

        if (command != null) {
            switch (command) {
                case "max":
                    call.getMax();
                    break;
                case "min":
                    call.getMin();
                    break;
                case "count":
                    call.getCount();
                    break;
                case "sum":
                    call.getSum();
                    break;
                default:
                    call.unknown(command);
            }
        }
    }
}
