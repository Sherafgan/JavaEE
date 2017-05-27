/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import calc.Calculator;
import io.LogsIO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sherafgan
 */
public class Calculate extends HttpServlet {

    private int idOfClient = 0;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            Object obj = request.getSession().getAttribute("idForLogs");

            if (obj == null) {
                request.getSession().setAttribute("idForLogs", idOfClient++);
            }

            int idForLogs = (int) request.getSession().getAttribute("idForLogs");

            LogsIO logsio = new LogsIO();

            float firstNumber = Float.parseFloat(request.getParameter("firstNumber"));
            float secondNumber = Float.parseFloat(request.getParameter("secondNumber"));
            String operation = request.getParameter("operation");

            Float finalResult = null;

            switch (operation) {
                case "add":
                    finalResult = Calculator.add(firstNumber, secondNumber);
                    logsio.put(firstNumber, secondNumber, "add", idForLogs);
                    break;
                case "substract":
                    finalResult = Calculator.substract(firstNumber, secondNumber);
                    logsio.put(firstNumber, secondNumber, "substract", idForLogs);
                    break;
                case "multiply":
                    finalResult = Calculator.multiply(firstNumber, secondNumber);
                    logsio.put(firstNumber, secondNumber, "multiply", idForLogs);
                    break;
                case "divide":
                    finalResult = Calculator.divide(firstNumber, secondNumber);
                    logsio.put(firstNumber, secondNumber, "divide", idForLogs);
                    break;
            }

            List<String> logs = logsio.getLogsFor(idForLogs);

            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Calculate</title>");
            out.println("</head>");
            out.println("<body>");
//            out.println("<h1>Servlet Calculate at " + request.getContextPath() + "</h1>");
            out.println("<h1> Result: " + finalResult + "</h1>");
            out.println("<h2> Your recent operations: </h2>");
            for (String s : logs) {
                out.println("<p>" + s + "</p>");
            }
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
