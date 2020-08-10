// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//to compile: javac -cp .;c:\myWebProject\tomcat\lib\servlet-api.jar QueryBatterServlet.java

@WebServlet("/query")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class QueryBatterServlet extends HttpServlet {

   // The doGet() runs once per HTTP GET request to this servlet.
   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
      // Set the MIME type for the response message
      response.setContentType("text/html");
      // Get a output writer to write the response message into the network socket
      PrintWriter out = response.getWriter();

      // Print an HTML page as the output of the query
      out.println("<!DOCTYPE html>");
      out.println("<html>");
      out.println("<head><title>Query Response</title></head>");
      out.println("<body>");

      try (
         // Step 1: Allocate a database 'Connection' object
         Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:3306/baseball?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "myuser", "xxxx");   // For MySQL
               // The format is: "jdbc:mysql://hostname:port/databaseName", "username", "password"

         // Step 2: Allocate a 'Statement' object in the Connection
         Statement stmt = conn.createStatement();

      ) {
         // Step 3: Execute a SQL SELECT query
         String sqlStr = "select * from batters where pid = "
               + "'" + request.getParameter("player") + "'";   // Single-quote SQL string

         out.println("<h3>Thank you for your query.</h3>");
         out.println("<p>Your SQL statement is: " + sqlStr + "</p>"); // Echo for debugging
         ResultSet rset = stmt.executeQuery(sqlStr);  // Send the query to the server

         // Step 4: Process the query result set
         String[] statArray = {"PA", "AB", "H", "2B", "3B", "HR", "BB", "SO", "HBP"};
         String[] statValue = new String[statArray.length];
         String pid = "";
         String firstName = "";
         String lastName = "";
         String position = "";
         String year = "";
         while(rset.next()) {
            // Print a paragraph <p>...</p> for each record
            out.println("<p>" + rset.getString("pid")
                  + ", " + rset.getString("firstName")
                  + " " + rset.getString("LastName") + "</p>");

        String urlName = "https://www.baseball-reference.com/players/" + request.getParameter("player").charAt(0) + "/" + rset.getString("pid") + ".shtml";
        URL website = new URL(urlName);

        BufferedReader in = new BufferedReader(new InputStreamReader(website.openStream()));

        String inputLine = "";
        String text = "";

        while ((inputLine = in.readLine()) != null) {
            text += inputLine + "\n";
        }
        in.close();

        //System.out.println(text);

        // id="batting_standard.2019" class="full" ><th scope="row" class="left " data-stat="year_ID" csk="2019" >2019<span
        year = request.getParameter("year");
        Pattern pattern = Pattern.compile("id=\"batting_standard." + year + "\" class=\"full\" ><th scope=\"row\" class=\"left \" data-stat=\"year_ID\" csk=\"" + year + "\" >" + year + "(.*)<");
        Matcher matcher = pattern.matcher(text);
        String stats = "";
        while (matcher.find()) {
            stats = matcher.group(1);
        }

        for (int i = 0; i < statArray.length; i++) {
            pattern = Pattern.compile("data-stat=\"" + statArray[i] +  "\"(.*?)>(\\d{1,5})<");
            matcher = pattern.matcher(stats);
            while (matcher.find()) {
                statValue[i] = matcher.group(2);
                out.println(statArray[i] + ": " + statValue[i]);
            }
            out.println();
        }

        pid = rset.getString("pid");
        firstName = rset.getString("firstName");
        lastName = rset.getString("lastName");
        position = rset.getString("position");

    }
    int yearValue = Integer.parseInt(year);
    String sqlInsert = "insert into batterStats values (" + "'" + pid + "'" + "," + "'" + firstName + "'" + "," +
    "'" + lastName + "'" + "," + "'" + position + "'" + "," + yearValue +  "," + statValue[0] + "," + statValue[1] + "," + statValue[2] + "," + statValue[3]
    + "," + statValue[4] + "," + statValue[5] + "," + statValue[6] + "," + statValue[7] + "," + statValue[8] + ")";
    out.println("The insert SQL statement is: " + sqlInsert + "\n");  // Echo for debugging
    int countInserted = stmt.executeUpdate(sqlInsert);

      } catch(Exception ex) {
         out.println("<p>Error: " + ex.getMessage() + "</p>");
         out.println("<p>Check Tomcat console for details.</p>");
         ex.printStackTrace();
      }  // Step 5: Close conn and stmt - Done automatically by try-with-resources (JDK 7)

      out.println("</body></html>");
      out.close();
   }
}
