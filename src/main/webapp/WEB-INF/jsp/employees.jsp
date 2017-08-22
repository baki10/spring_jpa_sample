<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
  <title>Employees</title>
  <!-- Latest compiled and minified CSS -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

  <!-- Optional theme -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">
</head>
<body>

<div class="container">
  <%--add employee form--%>
  <div class="col-md-5">
    <div class="form-area">
      <form:form action="" modelAttribute="employee" method="post">
        <br style="clear:both">
        <div class="form-group">
          <form:input class="form-control" path="name" placeholder="Name"/>
        </div>
        <div class="form-group">
          <form:input type="number" class="form-control" path="salary" placeholder="Salary"/>
        </div>

        <button id="submit" name="submit" class="btn btn-primary pull-right">Add Employee</button>
      </form:form>
      <br>
      <br>
    </div>
  </div>
</div>

<div class="container">

  <%--employee list--%>
  <div class="panel panel-default">
    <!-- Default panel contents -->
    <div class="panel-heading"><span class="lead">Employees list</span></div>

    <div class="tablecontainer">
      <table class="table table-hover">
        <thead>
        <tr>
          <th>ID</th>
          <th>PHOTO</th>
          <th></th>
          <th>NAME</th>
          <th>SALARY</th>
          <th>DELETE</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${emps}" var="emp">
          <tr>
            <td>${emp.id}</td>
            <td>
              <c:if test="${emp.picture!=null}">
                <a href="/emp/image/${emp.id}.png" target="_blank">
                  <img src="/emp/image/${emp.id}" alt="emp_image" style="max-width: 120px; max-height: 120px"/>
                </a>
              </c:if>
            </td>
            <td>
              <form action="/emp/image/${emp.id}" method="post" enctype="multipart/form-data">
                <input type="file" placeholder="Picture" name="file"/>
                <button name="submit" class="btn btn-primary">Upload photo</button>
              </form>
            </td>
            <td>${emp.name}</td>
            <td>${emp.salary}</td>
            <td>
              <a href="/emp/delete?id=${emp.id}"><span class="glyphicon glyphicon-remove-circle"></span></a>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</div>


<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>
</html>
