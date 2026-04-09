<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% final String c = request.getContextPath(); %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Admin - Services List</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    :root { --admin-primary: #5a67d8; --admin-accent: #7c3aed; --admin-bg: #f3f5fb; }
    body { background: var(--admin-bg); color: #0f172a; }
    .navbar-admin { background: linear-gradient(135deg, var(--admin-primary), var(--admin-accent)); box-shadow: 0 8px 18px rgba(90, 103, 216, 0.2); }
    .admin-layout { min-height: calc(100vh - 56px); }
    .sidebar { background: #111827; color: #cbd5e1; border-radius: 14px; padding: 1rem; position: sticky; top: 1rem; box-shadow: 0 16px 28px rgba(2, 6, 23, 0.2); }
    @media (min-width: 992px) { .sidebar { min-height: calc(100vh - 2rem); } }
    .sidebar-title { color: #f8fafc; font-weight: 700; font-size: 0.95rem; margin-bottom: 0.75rem; }
    .sidebar .nav-link { color: #cbd5e1; border-radius: 10px; padding: 0.55rem 0.7rem; margin-bottom: 0.2rem; font-size: 0.92rem; min-height: 52px; display: flex; align-items: center; }
    .sidebar .nav-link:hover { background: rgba(255, 255, 255, 0.08); color: #fff; }
    .sidebar .nav-link.active { background: linear-gradient(135deg, var(--admin-primary), var(--admin-accent)); color: #fff; font-weight: 600; }
    .admin-card { border: 1px solid #e2e8f0; border-radius: 14px; box-shadow: 0 10px 28px rgba(15, 23, 42, 0.08); overflow: hidden; background: #fff; }
    .admin-table thead th { background: #f8fafc; color: #334155; text-transform: uppercase; letter-spacing: 0.02em; font-size: 0.75rem; font-weight: 700; white-space: nowrap; }
    .admin-table td { color: #1e293b; vertical-align: middle; }
    .service-img { width: 56px; height: 42px; object-fit: cover; border-radius: 8px; border: 1px solid #e2e8f0; background: #f8fafc; }
    .empty-state { text-align: center; color: #64748b; padding: 1.2rem; font-size: 0.9rem; }
  </style>
</head>
<body>
<nav class="navbar navbar-dark navbar-admin">
  <div class="container-fluid">
    <span class="navbar-brand mb-0 h1">Easy 2 Work Admin Portal</span>
    <a class="btn btn-sm btn-light" href="<%= c %>/logout">Logout</a>
  </div>
</nav>

<div class="container-fluid py-4 px-3 px-md-4 admin-layout">
  <div class="row g-3">
    <div class="col-12 col-lg-3 col-xl-2">
      <aside class="sidebar">
        <div class="sidebar-title">Admin Menu</div>
        <nav class="nav flex-column">
          <a class="nav-link" href="<%= c %>/dashboard">Dashboard</a>
          <a class="nav-link" href="<%= c %>/users">User Management</a>
          <a class="nav-link" href="<%= c %>/services">Service Add</a>
          <a class="nav-link active" href="<%= c %>/services-list">Service List</a>
          <a class="nav-link" href="<%= c %>/reviews">Reviews</a>
          <a class="nav-link" href="<%= c %>/settings">Settings</a>
          <a class="nav-link" href="<%= c %>/logout">Logout</a>
        </nav>
      </aside>
    </div>

    <div class="col-12 col-lg-9 col-xl-10">
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h1 class="h4 mb-0">Service List</h1>
        <a class="btn btn-sm btn-primary" href="<%= c %>/services">+ Add New Service</a>
      </div>

      <div class="admin-card">
        <div class="table-responsive">
          <table class="table table-sm table-hover align-middle mb-0 admin-table">
            <thead class="table-light">
            <tr>
              <th>Code</th>
              <th>Title</th>
              <th>Base Price</th>
              <th>Image</th>
              <th>Description</th>
              <th></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="s" items="${services}">
              <tr>
                <td class="fw-semibold"><c:out value="${s.code}"/></td>
                <td><c:out value="${s.title}"/></td>
                <td><c:out value="${s.priceLabel}"/></td>
                <td>
                  <c:if test="${not empty s.imageDataUrl}">
                    <img class="service-img" src="${s.imageDataUrl}" alt="service">
                  </c:if>
                </td>
                <td class="small"><c:out value="${s.summary}"/></td>
                <td class="text-end">
                  <form class="delete-service-form" method="post" action="<%= c %>/services">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="${s.id}" class="service-id">
                    <button type="submit" class="btn btn-sm btn-outline-danger">Delete</button>
                  </form>
                </td>
              </tr>
            </c:forEach>
            <c:if test="${empty services}">
              <tr>
                <td colspan="6" class="empty-state">No services added yet.</td>
              </tr>
            </c:if>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</div>

<script>
  (function () {
    document.querySelectorAll('.delete-service-form').forEach(function (delForm) {
      delForm.addEventListener('submit', function (e) {
        e.preventDefault();
        var idField = delForm.querySelector('.service-id');
        var serviceId = idField ? idField.value : '';
        if (!serviceId) return;
        fetch('<%= c %>/api/admin/services?id=' + encodeURIComponent(serviceId), {
          method: 'DELETE',
          credentials: 'same-origin'
        })
          .then(function (r) { return r.json(); })
          .then(function (body) {
            if (!body || !body.ok) throw new Error((body && body.error) ? body.error : 'Unable to delete service');
            window.location.reload();
          })
          .catch(function (err) { alert(err.message || 'Unable to delete service'); });
      });
    });
  })();
</script>
</body>
</html>
