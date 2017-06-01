/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import calc.CalcOperations;
import calc.OperationType;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Tim
 */
public class CalcServlet extends HttpServlet {

    private ArrayList<String> listOperations = new ArrayList<String>();

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

        ServletContext context = getServletContext();

        Map<String, ArrayList<String>> history = new HashMap<>();

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet CalcServlet</title>");
        out.println("</head>");
        out.println("<body>");

        try {

            // считывание параметров
            double one = Double.valueOf(request.getParameter("one").toString()).doubleValue();
            double two = Double.valueOf(request.getParameter("two").toString()).doubleValue();
            String opearation = String.valueOf(request.getParameter("operation"));

            // определение или создание сессии
            HttpSession session = request.getSession(true);

            // получение типа операции
            OperationType operType = OperationType.valueOf(opearation.toUpperCase());

            // калькуляция
            double result = calcResult(operType, one, two);

            // для новой сессии создаем новый список
            if (session.isNew()) {
                listOperations.clear();
            }
//            else { // иначе получаем список из атрибутов сессии
//                listOperations = (ArrayList<String>) session.getAttribute("formula");
//            }

            // добавление новой операции в список и атрибут сессии
            listOperations.add(one + " " + operType.getStringValue() + " " + two + " = " + result);
            session.setAttribute("formula", listOperations);

            // вывод всех операций
            out.println("<h1>" + session.getId() + "</h1>");
//            out.println("<h3>Список операций (всего:" + listOperations.size() + ") </h3>");

            for (String oper : listOperations) {
                out.println("<h3>" + oper + "</h3>");
            }

            //вывод истории остальных сервлетов
            Map<String, ArrayList<String>> historyFromContext
                    = (Map<String, ArrayList<String>>) context.getAttribute("history");
            if (historyFromContext != null) {
                Iterator iterator = historyFromContext.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry pair = (Map.Entry) iterator.next();
                    if (!pair.getKey().equals(session.getId())) {
                        out.println("<h1>" + pair.getKey() + "</h1>");
                        ArrayList<String> historyListOfOperations = (ArrayList<String>) pair.getValue();
                        for (String s : historyListOfOperations) {
                            out.println("<h3>" + s + "</h3>");
                        }
                    }
                }
            }

            //добавление всей истории в контекст
            history.put(session.getId(), listOperations);
            context.setAttribute("history", history);

        } catch (Exception ex) {
            // предупреждение пользователю в случае ошибки
            out.println("<h3 style=\"color:red;\">Возникла ошибка. Проверьте параметры</h3>");
            out.println("<h3>Имена параметров должны быть one, two, operation</h3>");
            out.println("<h3>Operation должен принимать 1 из 4 значений: add, subtract, multiply, divide</h3>");
            out.println("<h3>Значения one и two должны быть числами</h3>");
            out.println("<h1>Пример</h1>");
            out.println("<h3>?one=1&two=3&operation=add</h3>");

        } finally {
            out.println("</body>");
            out.println("</html>");
            out.close();
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
        return "Сервлет - калькулятор";
    }// </editor-fold>

    // калькуляция
    private double calcResult(OperationType operType, double one, double two) {
        double result = 0;
        switch (operType) {
            case ADD: {
                result = CalcOperations.add(one, two);
                break;
            }
            case SUBTRACT: {
                result = CalcOperations.subtract(one, two);
                break;
            }

            case DIVIDE: {
                result = CalcOperations.divide(one, two);
                break;
            }

            case MULTIPLY: {
                result = CalcOperations.multiply(one, two);
                break;
            }

        }

        return result;
    }
}
