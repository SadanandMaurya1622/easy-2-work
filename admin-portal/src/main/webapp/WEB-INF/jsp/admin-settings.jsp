<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% final String c = request.getContextPath(); %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Admin - Settings</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container-fluid py-4">
  <div class="row">
    <div class="col-lg-2">
      <div class="list-group">
        <a class="list-group-item list-group-item-action" href="<%= c %>/dashboard">Dashboard</a>
        <a class="list-group-item list-group-item-action" href="<%= c %>/users">User Management</a>
        <a class="list-group-item list-group-item-action" href="<%= c %>/services">Service Add / Manage</a>
        <a class="list-group-item list-group-item-action" href="<%= c %>/reviews">Reviews</a>
        <a class="list-group-item list-group-item-action active" href="<%= c %>/settings">Settings</a>
      </div>
    </div>
    <div class="col-lg-10">
      <h3>Settings</h3>
      <div class="card">
        <div class="card-body">
          <p class="mb-2"><strong>Logged in admin:</strong> <c:out value="${adminEmail}"/></p>
          <p class="mb-2"><strong>Server port:</strong> <c:out value="${serverPort}"/></p>
          <p class="mb-0"><strong>Database status:</strong>
            <c:choose>
              <c:when test="${dbConfigured}"><span class="text-success">Connected</span></c:when>
              <c:otherwise><span class="text-warning">Not configured</span></c:otherwise>
            </c:choose>
          </p>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
