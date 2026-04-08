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
  <style>
    :root { --admin-primary: #5a67d8; --admin-accent: #7c3aed; }
    body { background: #f3f5fb; }
    .admin-shell { min-height: 100vh; }
    .sidebar {
      background: #111827; color: #cbd5e1; border-radius: 14px; padding: 1rem;
      position: sticky; top: 1rem; min-height: calc(100vh - 2rem);
      box-shadow: 0 16px 28px rgba(2, 6, 23, 0.2); overflow: hidden;
    }
    .sidebar-title { color: #f8fafc; font-weight: 700; font-size: 0.95rem; margin-bottom: 0.75rem; }
    .sidebar .nav-link { color: #cbd5e1; border-radius: 10px; padding: 0.55rem 0.7rem; margin-bottom: 0.2rem; font-size: 0.92rem; min-height: 52px; display: flex; align-items: center; }
    .sidebar .nav-link:hover { background: rgba(255,255,255,0.08); color: #fff; }
    .sidebar .nav-link.active { background: linear-gradient(135deg, var(--admin-primary), var(--admin-accent)); color: #fff; font-weight: 600; }
    .content-panel { height: calc(100vh - 2rem); overflow-y: auto; padding-right: 0.35rem; }
  </style>
</head>
<body>
<div class="container-fluid py-3 admin-shell">
  <div class="row g-3">
    <div class="col-lg-2">
      <aside class="sidebar">
        <div class="sidebar-title">Admin Menu</div>
        <nav class="nav flex-column">
          <a class="nav-link" href="<%= c %>/dashboard">Dashboard</a>
          <a class="nav-link" href="<%= c %>/users">User Management</a>
          <a class="nav-link" href="<%= c %>/services">Service Add / Manage</a>
          <a class="nav-link active" href="<%= c %>/reviews">Reviews</a>
          <a class="nav-link" href="<%= c %>/settings">Settings</a>
          <a class="nav-link" href="<%= c %>/logout">Logout</a>
        </nav>
      </aside>
    </div>
    <div class="col-lg-10 content-panel">
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
