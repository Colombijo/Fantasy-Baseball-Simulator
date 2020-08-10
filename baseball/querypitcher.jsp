<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<sql:setDataSource var="ds" dataSource="jdbc/TestDB" />
<sql:query sql="select * from pitchers" var="rs" dataSource="${ds}" />

<html>
<head><title>Test Database Connection Pooling</title></head>
<body>
  <h2>Results</h2>
  <table>
    <form method="get" action="http://localhost:9999/baseball/querypitcher">
    <c:forEach var="row" items="${rs.rows}">
    <input type="checkbox" name="player" value="${row.pid}">${row.firstName} ${row.lastName}
    </c:forEach>

    <input type="checkbox" name="stat" value="W">Wins
    <input type="checkbox" name="stat" value="IP">Innings Pitched
    <input type="checkbox" name="stat" value="SO">Strikeouts
    <input type="checkbox" name="stat" value="BB">Base on Balls
    <input type="submit" value="Search">
  </form>

  </table>


</body>
</html>
