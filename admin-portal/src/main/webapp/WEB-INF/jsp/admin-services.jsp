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
      --admin-bg: #f3f5fb;
      --admin-text: #0f172a;
      --admin-muted: #64748b;
      --admin-card-border: #e2e8f0;
      --admin-card-shadow: 0 10px 28px rgba(15, 23, 42, 0.08);
    }
    body {
      background: var(--admin-bg);
      color: var(--admin-text);
    }
    .navbar-admin {
      background: linear-gradient(135deg, var(--admin-primary) 0%, var(--admin-accent) 100%);
      box-shadow: 0 8px 18px rgba(90, 103, 216, 0.2);
    }
    .admin-layout {
      min-height: calc(100vh - 56px);
    }
    .page-header {
      margin-bottom: 0.8rem;
    }
    .page-title {
      font-weight: 700;
      letter-spacing: -0.02em;
      margin-bottom: 0.2rem;
    }
    .page-subtitle {
      color: var(--admin-muted);
      margin: 0;
      font-size: 0.95rem;
    }
    .sidebar {
      background: #111827;
      color: #cbd5e1;
      border-radius: 14px;
      padding: 1rem;
      position: sticky;
      top: 1rem;
      box-shadow: 0 16px 28px rgba(2, 6, 23, 0.2);
    }
    @media (min-width: 992px) {
      .sidebar {
        min-height: calc(100vh - 2rem);
      }
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
      min-height: 52px;
      display: flex;
      align-items: center;
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
    .admin-card {
      border: 1px solid var(--admin-card-border);
      border-radius: 14px;
      box-shadow: var(--admin-card-shadow);
      overflow: hidden;
    }
    .admin-card .card-header {
      background: #fff;
      border-bottom: 1px solid var(--admin-card-border);
      padding: 0.85rem 1rem;
      font-size: 0.95rem;
    }
    .admin-card .card-body {
      background: #fff;
    }
    .form-shell {
      border: 1px solid #e2e8f0;
      border-radius: 12px;
      background: linear-gradient(180deg, #ffffff 0%, #fcfdff 100%);
      padding: 0.9rem;
    }
    .form-section {
      border: 1px solid #e7ecf5;
      border-radius: 10px;
      background: #f8fafc;
      padding: 0.75rem;
      margin-bottom: 0.75rem;
    }
    .form-section-title {
      font-size: 0.73rem;
      font-weight: 700;
      color: #475569;
      text-transform: uppercase;
      letter-spacing: 0.03em;
      margin-bottom: 0.55rem;
    }
    .admin-label {
      font-size: 0.78rem;
      text-transform: uppercase;
      letter-spacing: 0.02em;
      color: #475569;
      font-weight: 700;
      margin-bottom: 0.3rem;
    }
    .admin-help {
      color: #64748b;
      font-size: 0.76rem;
      font-weight: 500;
      text-transform: none;
      letter-spacing: 0;
    }
    .form-control {
      border-color: #dbe2ef;
      border-radius: 10px;
      min-height: 38px;
      font-size: 0.9rem;
      box-shadow: none;
    }
    textarea.form-control {
      min-height: auto;
    }
    .form-control:focus {
      border-color: #8b99ff;
      box-shadow: 0 0 0 3px rgba(90, 103, 216, 0.15);
    }
    .btn-admin-primary {
      border: none;
      border-radius: 10px;
      padding: 0.62rem 0.9rem;
      font-weight: 600;
      background: linear-gradient(135deg, var(--admin-primary), var(--admin-accent));
      box-shadow: 0 8px 18px rgba(92, 87, 232, 0.25);
    }
    .btn-admin-primary:hover {
      filter: brightness(1.03);
    }
    .btn-admin-primary:disabled {
      opacity: 0.9;
    }
    .save-hint {
      font-size: 0.74rem;
      color: #64748b;
      text-align: center;
      margin-top: 0.55rem;
    }
    .admin-table thead th {
      background: #f8fafc;
      color: #334155;
      text-transform: uppercase;
      letter-spacing: 0.02em;
      font-size: 0.75rem;
      font-weight: 700;
      padding-top: 0.75rem;
      padding-bottom: 0.75rem;
      white-space: nowrap;
    }
    .admin-table td {
      color: #1e293b;
      padding-top: 0.7rem;
      padding-bottom: 0.7rem;
      vertical-align: middle;
    }
    .service-img {
      width: 56px;
      height: 42px;
      object-fit: cover;
      border-radius: 8px;
      border: 1px solid #e2e8f0;
      background: #f8fafc;
    }
    .empty-state {
      text-align: center;
      color: #64748b;
      padding: 1.2rem;
      font-size: 0.9rem;
    }
  </style>
</head>
<body>
<nav class="navbar navbar-dark navbar-admin">
  <div class="container-fluid">
    <span class="navbar-brand mb-0 h1">Easy 2 Work Admin Portal</span>
    <div>
      <a class="btn btn-sm btn-light" href="<%= c %>/logout">Logout</a>
    </div>
  </div>
</nav>

<div class="container-fluid py-4 px-3 px-md-4 admin-layout">
  <div class="page-header">
    <h1 class="h4 page-title">Service Management</h1>
    <p class="page-subtitle">Create and maintain the services shown to customers.</p>
  </div>
  <div class="row g-3">
    <div class="col-12 col-lg-3 col-xl-2">
      <aside class="sidebar">
        <div class="sidebar-title">Admin Menu</div>
        <nav class="nav flex-column">
          <a class="nav-link" href="<%= c %>/dashboard">Dashboard</a>
          <a class="nav-link" href="<%= c %>/users">User Management</a>
          <a class="nav-link active" href="<%= c %>/services">Service Add</a>
          <a class="nav-link" href="<%= c %>/services-list">Service List</a>
          <a class="nav-link" href="<%= c %>/reviews">Reviews</a>
          <a class="nav-link" href="<%= c %>/settings">Settings</a>
          <a class="nav-link" href="<%= c %>/logout">Logout</a>
        </nav>
      </aside>
    </div>
    <div class="col-12 col-lg-9 col-xl-10">
      <div class="row g-4">
        <div class="col-12">
          <div class="card admin-card">
            <div class="card-header fw-semibold">Add New Service</div>
            <div class="card-body">
              <form id="adminServiceForm" class="form-shell" method="post" action="<%= c %>/services" enctype="multipart/form-data">
                <input type="hidden" name="action" value="add" />

                <div class="form-section">
                  <div class="form-section-title">Basic Information</div>
                  <div class="mb-2">
                    <label class="admin-label">Title</label>
                    <input class="form-control form-control-sm" name="title" placeholder="Plumbing Service" required>
                  </div>
                  <div class="row g-2">
                    <div class="col-12 col-md-6">
                      <label class="admin-label">Code <span class="admin-help">(optional)</span></label>
                      <input class="form-control form-control-sm" name="code" placeholder="PLUMBING">
                    </div>
                    <div class="col-12 col-md-6">
                      <label class="admin-label">Base Price</label>
                      <input class="form-control form-control-sm" name="basePrice" placeholder="From 299">
                    </div>
                  </div>
                  <div class="mt-2">
                    <label class="admin-label">Description</label>
                    <textarea class="form-control form-control-sm" name="description" rows="3" placeholder="Short service description"></textarea>
                  </div>
                </div>

                <div class="form-section">
                  <div class="form-section-title">Pricing & Scope</div>
                  <div class="mb-2">
                    <label class="admin-label">Price Detail <span class="admin-help">(optional)</span></label>
                    <textarea class="form-control form-control-sm" name="priceDetail" rows="2" placeholder="Final price depends on job scope and location."></textarea>
                  </div>
                  <div class="mb-2">
                    <label class="admin-label">What we'll do for you <span class="admin-help">(optional, one line per point)</span></label>
                    <textarea class="form-control form-control-sm" name="weProvide" rows="3" placeholder="Inspection and scope confirmation&#10;Basic service delivery"></textarea>
                  </div>
                  <div class="mb-0">
                    <label class="admin-label">What we need from user side <span class="admin-help">(optional, one line per point)</span></label>
                    <textarea class="form-control form-control-sm" name="fromYou" rows="3" placeholder="Share exact requirement and preferred timing"></textarea>
                  </div>
                </div>

                <div class="form-section">
                  <div class="form-section-title">Service Experience</div>
                  <div class="mb-2">
                    <label class="admin-label">Usually not included <span class="admin-help">(optional, one line per point)</span></label>
                    <textarea class="form-control form-control-sm" name="notIncluded" rows="3" placeholder="Material costs and heavy rework are charged separately"></textarea>
                  </div>
                  <div class="mb-0">
                    <label class="admin-label">Visit Steps <span class="admin-help">(optional, one line per step)</span></label>
                    <textarea class="form-control form-control-sm" name="visitSteps" rows="3" placeholder="Book the service&#10;Admin/pro confirms availability&#10;Service is delivered"></textarea>
                  </div>
                </div>

                <div class="form-section mb-0">
                  <div class="form-section-title">Media</div>
                  <label class="admin-label">Image</label>
                  <input type="file" class="form-control form-control-sm" name="image" accept="image/*">
                </div>

                <button id="addServiceBtn" class="btn btn-admin-primary btn-sm w-100 mt-2" type="submit">Add Service</button>
                <div class="save-hint">Service will be visible on user portal immediately after save.</div>
              </form>
            </div>
          </div>
          <div class="alert alert-info mt-3 small mb-0">
            Added services are saved and visible on user portal/API.
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<script>
  (function () {
    var form = document.getElementById('adminServiceForm');
    if (form) {
      form.addEventListener('submit', function (e) {
        e.preventDefault();
        var btn = document.getElementById('addServiceBtn');
        if (btn) {
          btn.disabled = true;
          btn.textContent = 'Saving...';
        }
        var fd = new FormData(form);
        fetch('<%= c %>/api/admin/services', {
          method: 'POST',
          body: fd,
          credentials: 'same-origin'
        })
          .then(function (r) { return r.json(); })
          .then(function (body) {
            if (!body || !body.ok) {
              throw new Error((body && body.error) ? body.error : 'Unable to add service');
            }
            window.location.reload();
          })
          .catch(function (err) {
            alert(err.message || 'Unable to add service');
          })
          .finally(function () {
            if (btn) {
              btn.disabled = false;
              btn.textContent = 'Add Service';
            }
          });
      });
    }

  })();
</script>
</body>
</html>
