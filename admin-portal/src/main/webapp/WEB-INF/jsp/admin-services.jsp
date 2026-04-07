<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% final String c = request.getContextPath(); %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Admin - Services Management</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<nav class="navbar navbar-dark" style="background: linear-gradient(135deg,#5a67d8,#7c3aed);">
  <div class="container-fluid">
    <span class="navbar-brand mb-0 h1">Services Management</span>
    <div>
      <a class="btn btn-sm btn-outline-light me-2" href="<%= c %>/bookings">Bookings</a>
      <a class="btn btn-sm btn-light" href="<%= c %>/logout">Logout</a>
    </div>
  </div>
</nav>

<div class="container py-4">
  <div class="row g-4">
    <div class="col-lg-4">
      <div class="card">
        <div class="card-header fw-semibold">Add New Service</div>
        <div class="card-body">
          <form method="post" action="<%= c %>/services">
            <input type="hidden" name="action" value="add">
            <div class="mb-2">
              <label class="form-label">Code</label>
              <input class="form-control form-control-sm" name="code" placeholder="PLUMBING" required>
            </div>
            <div class="mb-2">
              <label class="form-label">Title</label>
              <input class="form-control form-control-sm" name="title" placeholder="Plumbing Service" required>
            </div>
            <div class="mb-2">
              <label class="form-label">Base Price</label>
              <input class="form-control form-control-sm" name="basePrice" placeholder="From 299">
            </div>
            <div class="mb-3">
              <label class="form-label">Description</label>
              <textarea class="form-control form-control-sm" name="description" rows="3" placeholder="Short service description"></textarea>
            </div>
            <button class="btn btn-primary btn-sm w-100" type="submit">Add Service</button>
          </form>
        </div>
      </div>
      <div class="alert alert-info mt-3 small mb-0">
        This is admin-side runtime management (in-memory for current run).
      </div>
    </div>

    <div class="col-lg-8">
      <div class="card">
        <div class="card-header fw-semibold">Service List</div>
        <div class="table-responsive">
          <table class="table table-sm table-hover align-middle mb-0">
            <thead class="table-light">
            <tr>
              <th>Code</th>
              <th>Title</th>
              <th>Base Price</th>
              <th>Description</th>
              <th></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="s" items="${services}">
              <tr>
                <td class="fw-semibold"><c:out value="${s.code}"/></td>
                <td><c:out value="${s.title}"/></td>
                <td><c:out value="${s.basePrice}"/></td>
                <td class="small"><c:out value="${s.description}"/></td>
                <td class="text-end">
                  <form method="post" action="<%= c %>/services">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="${s.id}">
                    <button type="submit" class="btn btn-sm btn-outline-danger">Delete</button>
                  </form>
                </td>
              </tr>
            </c:forEach>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
