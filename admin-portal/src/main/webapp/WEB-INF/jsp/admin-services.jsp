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
  <style>
    :root {
      --admin-primary: #5a67d8;
      --admin-accent: #7c3aed;
    }
    .navbar-admin {
      background: linear-gradient(135deg, var(--admin-primary) 0%, var(--admin-accent) 100%);
    }
    .admin-layout {
      min-height: calc(100vh - 56px);
    }
    .sidebar {
      background: #111827;
      color: #cbd5e1;
      border-radius: 14px;
      padding: 1rem;
      position: sticky;
      top: 1rem;
    }
    .sidebar-title {
      color: #f8fafc;
      font-weight: 700;
      font-size: 0.95rem;
      margin-bottom: 0.75rem;
    }
    .sidebar .nav-link {
      color: #cbd5e1;
      border-radius: 10px;
      padding: 0.55rem 0.7rem;
      margin-bottom: 0.2rem;
      font-size: 0.92rem;
    }
    .sidebar .nav-link:hover {
      background: rgba(255, 255, 255, 0.08);
      color: #fff;
    }
    .sidebar .nav-link.active {
      background: linear-gradient(135deg, var(--admin-primary), var(--admin-accent));
      color: #fff;
      font-weight: 600;
    }
    .sidebar .nav-link.disabled {
      opacity: 0.6;
      cursor: not-allowed;
    }
  </style>
</head>
<body class="bg-light">
<nav class="navbar navbar-dark navbar-admin">
  <div class="container-fluid">
    <span class="navbar-brand mb-0 h1">Easy 2 Work Admin Portal</span>
    <div>
      <a class="btn btn-sm btn-outline-light me-2" href="<%= c %>/bookings">Bookings</a>
      <a class="btn btn-sm btn-light" href="<%= c %>/logout">Logout</a>
    </div>
  </div>
</nav>

<div class="container-fluid py-4 px-3 px-md-4 admin-layout">
  <div class="row g-3">
    <div class="col-12 col-lg-3 col-xl-2">
      <aside class="sidebar">
        <div class="sidebar-title">Admin Menu</div>
        <nav class="nav flex-column">
          <a class="nav-link" href="<%= c %>/bookings">Dashboard</a>
          <a class="nav-link" href="<%= c %>/bookings">Bookings</a>
          <a class="nav-link" href="<%= c %>/users">User Management</a>
          <a class="nav-link active" href="<%= c %>/services">Service Add / Manage</a>
          <a class="nav-link" href="<%= c %>/reviews">Reviews</a>
          <a class="nav-link" href="<%= c %>/reports">Reports</a>
          <a class="nav-link" href="<%= c %>/settings">Settings</a>
          <a class="nav-link" href="<%= c %>/logout">Logout</a>
        </nav>
      </aside>
    </div>
    <div class="col-12 col-lg-9 col-xl-10">
      <div class="row g-4">
        <div class="col-lg-4">
          <div class="card">
            <div class="card-header fw-semibold">Add New Service</div>
            <div class="card-body">
              <form method="post" action="<%= c %>/services" enctype="multipart/form-data">
                <input type="hidden" name="action" value="add">
                <div class="mb-2">
                  <label class="form-label">Code <span class="text-muted small">(optional)</span></label>
                  <input class="form-control form-control-sm" name="code" placeholder="PLUMBING">
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
                <div class="mb-3">
                  <label class="form-label">Image</label>
                  <input type="file" class="form-control form-control-sm" name="image" accept="image/*">
                </div>
                <button class="btn btn-primary btn-sm w-100" type="submit">Add Service</button>
              </form>
            </div>
          </div>
          <div class="alert alert-info mt-3 small mb-0">
            Added services are saved and visible on user portal/API.
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
                    <img src="${s.imageDataUrl}" alt="service" style="width:48px;height:36px;object-fit:cover;border-radius:6px;">
                  </c:if>
                </td>
                <td class="small"><c:out value="${s.summary}"/></td>
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
  </div>
</div>
</body>
</html>
