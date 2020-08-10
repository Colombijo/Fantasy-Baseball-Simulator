<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%
//int hr = 0;
String driver = "com.mysql.jdbc.Driver";
String connectionUrl = "jdbc:mysql://localhost:3306/";
String database = "batterstats";
String userid = "myuser";
String password = "xxxx";
try {
Class.forName(driver);
} catch (ClassNotFoundException e) {
e.printStackTrace();
}
//Connection connection = null;
Statement statement = null;
ResultSet resultSet = null;
%>
<!DOCTYPE html>
<html>
<body>
<h1>Your Team</h1>
<table border="1">
<tr>
<td>first name</td>
<td>last name</td>
<td>pid</td>

</tr>
<%
try{
//connection = DriverManager.getConnection(connectionUrl+database, userid, password);
Connection conn = DriverManager.getConnection(
      "jdbc:mysql://localhost:3306/baseball?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
      "myuser", "xxxx");
statement=conn.createStatement();
String sql ="select * from batterstats";
resultSet = statement.executeQuery(sql);
String firstName = "";
while(resultSet.next()){
%>
<tr>
<td><%=resultSet.getString("firstName") %></td>
<td><%=resultSet.getString("lastName") %></td>
<td><%=resultSet.getString("pid") %></td>

</tr>
<%
}

conn.close();
} catch (Exception e) {
e.printStackTrace();
}
%>
</table>

<form method="get" action="http://localhost:9999/baseball/game">
  <input type="submit" value="Play Ball!">
</form>

</body>
</html>
