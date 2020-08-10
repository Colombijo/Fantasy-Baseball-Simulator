// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\PlayGame.java".
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
import java.util.Random;

//to compile: javac -cp .;c:\myWebProject\tomcat\lib\servlet-api.jar PlayGame.java

@WebServlet("/game")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class PlayGame extends HttpServlet {

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
      out.println("<head><title>Play Ball!</title></head>");
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
        String sqlStr = "select * from batterstats";   // Single-quote SQL string

        out.println("<h3>Here is how your team did:</h3>");
        ResultSet rset = stmt.executeQuery(sqlStr);  // Send the query to the server
        String[] playerNames = new String[9];
        int[][] playerStats = new int[9][9];
        String[] gameStatsKey = new String[9];
        gameStatsKey[0] = "pa"; gameStatsKey[1] = "ab"; gameStatsKey[2] = "H"; gameStatsKey[3] = "2B";
        gameStatsKey[4] = "3B"; gameStatsKey[5] = "HR"; gameStatsKey[6] = "BB"; gameStatsKey[7] = "SO";
        gameStatsKey[8] = "HBP";
        int playerCount = 0;
         while(rset.next()) {
           playerNames[playerCount] = rset.getString("firstName") + " " + rset.getString("lastName");
           for (int i = 0; i < gameStatsKey.length; i++) {
             playerStats[playerCount][i] = rset.getInt(gameStatsKey[i]);
           }
           playerCount++;

        }
        int[][] gameStats = new int[9][9];
        int[] rbis = new int[9];
        int outs = 0;
        int inning = 1;
        boolean runnerOnFirst = false;
        boolean runnerOnSecond = false;
        boolean runnerOnThird = false;
        int runs = 0;
        int batterNumber = 0;
        int runsThisInning = 0;
        while (inning < 10) {
          while (batterNumber < 9 && inning < 10) {
            String ABResult = atBatResult(playerStats, batterNumber);
            gameStats[batterNumber][1]++;
            if (ABResult.equals("out")) {
              outs++;
              if (outs == 3) {
                outs = 0;
                runnerOnFirst = false;
                runnerOnSecond = false;
                runnerOnThird = false;
                out.println("End of inning " + inning);
                out.println("We scored " + runsThisInning + " runs");
                out.println("<br></br>");
                inning++;
                runs += runsThisInning;
                runsThisInning = 0;
              }
            } else {

              if (ABResult.equals("single") || ABResult.equals("walk") || ABResult.equals("hit by pitch")) {
                if (ABResult.equals("single")) {
                  gameStats[batterNumber][0]++;
                }
                if (runnerOnThird) {
                  runsThisInning++;
                  rbis[batterNumber]++;
                  runnerOnThird = false;
                }
                if (runnerOnSecond) {
                  runnerOnThird = true;
                }
                if (runnerOnFirst) {
                  runnerOnSecond = true;
                }
                runnerOnFirst = true;
              } else if (ABResult.equals("double")) {
                gameStats[batterNumber][0]++;
                gameStats[batterNumber][3]++;
                if (runnerOnThird) {
                  runsThisInning++;
                  rbis[batterNumber]++;
                  runnerOnThird = false;
                }
                if (runnerOnSecond) {
                  runsThisInning++;
                  rbis[batterNumber]++;
                  runnerOnSecond = false;
                }
                if (runnerOnFirst) {
                  runnerOnThird = true;
                  runnerOnFirst = false;
                }
                runnerOnSecond = true;
              } else if (ABResult.equals("triple")) {
                gameStats[batterNumber][0]++;
                gameStats[batterNumber][4]++;
                if (runnerOnThird) {
                  runsThisInning++;
                  rbis[batterNumber]++;
                  runnerOnThird = false;
                }
                if (runnerOnSecond) {
                  runsThisInning++;
                  rbis[batterNumber]++;
                  runnerOnSecond = false;
                }
                if (runnerOnFirst) {
                  runsThisInning++;
                  rbis[batterNumber]++;
                  runnerOnFirst = false;
                }
                runnerOnThird = true;
              } else { // home run
                gameStats[batterNumber][0]++;
                gameStats[batterNumber][5]++;
                if (runnerOnThird) {
                  runsThisInning++;
                  rbis[batterNumber]++;
                  runnerOnThird = false;
                }
                if (runnerOnSecond) {
                  runsThisInning++;
                  rbis[batterNumber]++;
                  runnerOnSecond = false;
                }
                if (runnerOnFirst) {
                  runsThisInning++;
                  rbis[batterNumber]++;
                  runnerOnFirst = false;
                }
              runsThisInning++;
              rbis[batterNumber]++;

            }
          }
            batterNumber++;
          }
          batterNumber = 0;
        }

        out.println("We scored " + runs + " runs");
        out.println("<br></br>");
        int pogIndex = 0;
        int maxGameValue = 0;
        int currentGameValue = 0;
        for (int i = 0; i < playerNames.length; i++) {
          currentGameValue += gameStats[i][2] + (gameStats[i][3] * 2) + (gameStats[i][4] * 3)
          + (gameStats[i][5] * 4) + (rbis[i] * 2);
          if (currentGameValue > maxGameValue) {
            pogIndex = i;
            maxGameValue = currentGameValue;
          }
          currentGameValue = 0;

          out.println(i + 1 + ": " + playerNames[i] + ": " + gameStats[i][0] + " for " + gameStats[i][1]);
          out.println("<br></br>");
          out.println(gameStatsKey[3] + ": " + gameStats[i][3] + ", " + gameStatsKey[4] + ": " + gameStats[i][4] +
          ", " + gameStatsKey[5] + ": " + gameStats[i][5] + ", " + gameStatsKey[6] + ": " + gameStats[i][6] +
          "  RBIS: " + rbis[i]);
          out.println("<br></br>");
        }

        out.println("The player of the game is: " + playerNames[pogIndex]);


       } catch(Exception ex) {
          out.println("<p>Error: " + ex.getMessage() + "</p>");
          out.println("<p>Check Tomcat console for details.</p>");
          ex.printStackTrace();
       }  // Step 5: Close conn and stmt - Done automatically by try-with-resources (JDK 7)

       out.println("</body></html>");
       out.close();
    }

    public String atBatResult(int[][] playerStats, int i) {
      Random r = new Random();
      double onBaseOrOut = r.nextDouble();
      int totalOnBase = playerStats[i][2] + playerStats[i][6] + playerStats[i][8];
      double OBP = (double) totalOnBase / (double) playerStats[i][0];
      double hitPercentage = (double) playerStats[i][2] / (double) totalOnBase;
      double walkPercentage = (double) playerStats[i][6] / (double) totalOnBase;
      double HBPPercentage = (double) playerStats[i][8] / (double) totalOnBase;
      double doublePercentage = (double) playerStats[i][3] / (double) playerStats[i][2];
      double triplePercentage = (double) playerStats[i][4] / (double) playerStats[i][2];
      double homeRunPercentage = (double) playerStats[i][5] / (double) playerStats[i][2];
      double xbhPercent = doublePercentage + triplePercentage + homeRunPercentage;
      if (onBaseOrOut > OBP) {
        return "out";
      } else {
          double onBaseResult = r.nextDouble();
          if (onBaseResult <= hitPercentage) {
              double hitResult = r.nextDouble();
              if (hitResult > xbhPercent) {
                return "single";
              } else if (hitResult > xbhPercent - homeRunPercentage) {
                  return "home run";
              } else if (hitResult > xbhPercent - homeRunPercentage - triplePercentage) {
                  return "triple";
              } else {
                  return "double";
              }

          } else if (onBaseResult <= hitPercentage + walkPercentage) {
              return "walk";
          } else {
              return "hit by pitch";
          }
      }
    }

 }
