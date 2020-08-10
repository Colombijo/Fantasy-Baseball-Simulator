<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<sql:setDataSource var="ds" dataSource="jdbc/batterstats" />
<sql:query sql="select * from batterstats where firstName = 'Nick'" var="rs" dataSource="${ds}" />





<html>
<head><title>Display Stats</title></head>
<body>
  <h2>Display Stats</h2>
  <table>
    <c:forEach var="row" items="${rs.rows}">
      <% String firstName = rs.getParameter("firstName"); %>
    <p> ${row.firstName} ${row.lastName} home runs: ${row.HR} + <%= firstName %>

    </c:forEach>

</body>

</html>
