<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% final String c = request.getContextPath(); %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Admin - User Management</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    :root { --admin-primary: #5a67d8; --admin-accent: #7c3aed; }
    body { background: #f3f5fb; }
    .admin-shell { min-height: 100vh; }
    .sidebar {
      background: #111827;
      color: #cbd5e1;
      border-radius: 14px;
      padding: 1rem;
      position: sticky;
      top: 1rem;
      min-height: calc(100vh - 2rem);
      box-shadow: 0 16px 28px rgba(2, 6, 23, 0.2);
      overflow: hidden;
    }
    .sidebar-title { color: #f8fafc; font-weight: 700; font-size: 0.95rem; margin-bottom: 0.75rem; }
    .sidebar .nav-link {
      color: #cbd5e1; border-radius: 10px; padding: 0.55rem 0.7rem; margin-bottom: 0.2rem; font-size: 0.92rem;
      min-height: 52px; display: flex; align-items: center;
    }
    .sidebar .nav-link:hover { background: rgba(255,255,255,0.08); color: #fff; }
    .sidebar .nav-link.active {
      background: linear-gradient(135deg, var(--admin-primary), var(--admin-accent)); color: #fff; font-weight: 600;
    }
    .content-panel {
      height: calc(100vh - 2rem);
      overflow-y: auto;
      padding-right: 0.35rem;
    }
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
          <a class="nav-link active" href="<%= c %>/users">User Management</a>
          <a class="nav-link" href="<%= c %>/services">Service Add / Manage</a>
          <a class="nav-link" href="<%= c %>/reviews">Reviews</a>
          <a class="nav-link" href="<%= c %>/settings">Settings</a>
          <a class="nav-link" href="<%= c %>/logout">Logout</a>
        </nav>
      </aside>
    </div>
    <div class="col-lg-10 content-panel">
      <h3>User Management</h3>
      <p id="userCountText" class="text-muted mb-3">Total users: 0</p>
      <div id="usersAlertWrap"></div>
      <div class="table-responsive bg-white rounded border">
        <table class="table table-sm mb-0">
          <thead class="table-light">
          <tr><th>ID</th><th>Name</th><th>Email</th><th>Phone</th><th>Role</th></tr>
          </thead>
          <tbody id="usersTableBody">
          <tr><td colspan="5" class="text-center text-muted py-3">Loading users...</td></tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</div>
<script>
  (function () {
    var userCountText = document.getElementById('userCountText');
    var usersAlertWrap = document.getElementById('usersAlertWrap');
    var usersTableBody = document.getElementById('usersTableBody');

    function escapeHtml(value) {
      if (value === null || value === undefined) {
        return '';
      }
      return String(value)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
    }

    function renderRows(users) {
      if (!Array.isArray(users) || users.length === 0) {
        usersTableBody.innerHTML = '<tr><td colspan="5" class="text-center text-muted py-3">No users found.</td></tr>';
        return;
      }
      var html = '';
      users.forEach(function (u) {
        html += '<tr>' +
          '<td>' + escapeHtml(u.id) + '</td>' +
          '<td>' + escapeHtml(u.fullName) + '</td>' +
          '<td>' + escapeHtml(u.email) + '</td>' +
          '<td>' + escapeHtml(u.phone) + '</td>' +
          '<td>' + escapeHtml(u.role) + '</td>' +
          '</tr>';
      });
      usersTableBody.innerHTML = html;
    }

    function showAlert(type, message) {
      usersAlertWrap.innerHTML = '<div class="alert alert-' + type + '">' + escapeHtml(message) + '</div>';
    }

    fetch('<%= c %>/api/admin/users', { credentials: 'same-origin' })
      .then(function (response) { return response.json(); })
      .then(function (data) {
        var count = data && typeof data.count === 'number' ? data.count : 0;
        userCountText.textContent = 'Total users: ' + count;
        renderRows(data && data.users ? data.users : []);
        if (!data || !data.ok) {
          showAlert('danger', (data && data.error) ? data.error : 'Unable to load users.');
          return;
        }
        if (data.dbConfigured === false) {
          showAlert('info', 'Running on local-file mode (DB not configured).');
          return;
        }
        usersAlertWrap.innerHTML = '';
      })
      .catch(function () {
        userCountText.textContent = 'Total users: 0';
        usersTableBody.innerHTML = '<tr><td colspan="5" class="text-center text-muted py-3">Unable to load users.</td></tr>';
        showAlert('danger', 'Unable to load users from API. Please try again.');
      });
  })();
</script>
</body>
</html>
