<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% final String c = request.getContextPath(); %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Admin - Reviews</title>
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
        <a class="list-group-item list-group-item-action active" href="<%= c %>/reviews">Reviews</a>
        <a class="list-group-item list-group-item-action" href="<%= c %>/reports">Reports</a>
        <a class="list-group-item list-group-item-action" href="<%= c %>/settings">Settings</a>
      </div>
    </div>
    <div class="col-lg-10">
      <h3>Customer Reviews</h3>
      <p class="text-muted mb-3">Total reviews: <c:out value="${adminReviewCount}"/></p>
      <c:if test="${!dbConfigured}">
        <div class="alert alert-warning">Database not configured. Reviews are unavailable.</div>
      </c:if>
      <c:if test="${not empty reviewsError}">
        <div class="alert alert-danger"><c:out value="${reviewsError}"/></div>
      </c:if>
      <div class="table-responsive bg-white rounded border">
        <table class="table table-sm mb-0">
          <thead class="table-light">
          <tr><th>ID</th><th>Customer</th><th>Service</th><th>Rating</th><th>Comment</th></tr>
          </thead>
          <tbody>
          <c:forEach var="r" items="${adminReviews}">
            <tr>
              <td><c:out value="${r.id}"/></td>
              <td><c:out value="${r.customerName}"/></td>
              <td><c:out value="${r.serviceType}"/></td>
              <td><c:out value="${r.rating}"/>/5</td>
              <td><c:out value="${r.comment}"/></td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</div>
</body>
</html>
