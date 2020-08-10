<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<sql:setDataSource var="ds" dataSource="jdbc/batters" />
<sql:query sql="select * from batters order by case when
  position = 'C' then 1 when position = '1B' then 2 when position = '2B' then 3
  when position = '3B' then 4 when position = 'SS' then 5 when position = 'Lf' then 6
  when position = 'CF' then 7 when position = 'RF' then 8 end asc" var="rs" dataSource="${ds}" />

<html>
<head><title>Position Player Roster</title></head>
<body>
  <h2>Position Player Roster</h2>
  <table>
    <form method="get" action="http://localhost:9999/baseball/query">
    <strong>Select a player:</strong>
    <br></br>
    <c:forEach var="row" items="${rs.rows}">
    <input type="radio" name="player" value="${row.pid}">${row.position}: ${row.firstName} ${row.lastName}
    <br></br>
    </c:forEach>
    <p>
    <strong>Select a year:</strong>
    <br></br>
    <input type="radio" name="year" value="2010">2010
    <input type="radio" name="year" value="2011">2011
    <input type="radio" name="year" value="2012">2012
    <input type="radio" name="year" value="2013">2013
    <input type="radio" name="year" value="2014">2014
    <input type="radio" name="year" value="2015">2015
    <input type="radio" name="year" value="2016">2016
    <input type="radio" name="year" value="2017">2017
    <input type="radio" name="year" value="2018">2018
    <input type="radio" name="year" value="2019">2019
    </p>

    <input type="radio" name="team" value="batterStats">batterStats
    <input type="radio" name="team" value="teamTwo">teamTwo

    <input type="submit" value="ADD TO TEAM">
  </form>

  </table>


</body>
</html>
