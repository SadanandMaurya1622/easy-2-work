<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% final String c = request.getContextPath(); %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Admin - Reports</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container-fluid py-4">
  <div class="row">
    <div class="col-lg-2">
      <div class="list-group">
        <a class="list-group-item list-group-item-action" href="<%= c %>/bookings">Dashboard</a>
        <a class="list-group-item list-group-item-action" href="<%= c %>/bookings">Bookings</a>
        <a class="list-group-item list-group-item-action" href="<%= c %>/users">User Management</a>
        <a class="list-group-item list-group-item-action" href="<%= c %>/services">Service Add / Manage</a>
        <a class="list-group-item list-group-item-action" href="<%= c %>/reviews">Reviews</a>
        <a class="list-group-item list-group-item-action active" href="<%= c %>/reports">Reports</a>
        <a class="list-group-item list-group-item-action" href="<%= c %>/settings">Settings</a>
      </div>
    </div>
    <div class="col-lg-10">
      <h3>Reports</h3>
      <c:if test="${!dbConfigured}">
        <div class="alert alert-warning">Database not configured. Live reports are unavailable.</div>
      </c:if>
      <c:if test="${not empty reportsError}">
        <div class="alert alert-danger"><c:out value="${reportsError}"/></div>
      </c:if>
      <div class="row g-3">
        <div class="col-md-4"><div class="card"><div class="card-body"><div class="text-muted">Bookings</div><div class="h3 mb-0"><c:out value="${bookingCount}"/></div></div></div></div>
        <div class="col-md-4"><div class="card"><div class="card-body"><div class="text-muted">Users</div><div class="h3 mb-0"><c:out value="${userCount}"/></div></div></div></div>
        <div class="col-md-4"><div class="card"><div class="card-body"><div class="text-muted">Reviews</div><div class="h3 mb-0"><c:out value="${reviewCount}"/></div></div></div></div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
