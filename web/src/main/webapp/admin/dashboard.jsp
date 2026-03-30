<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% final String c = request.getContextPath(); %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Admin – Dashboard</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
  <div class="container py-5">
    <h1 class="h3 mb-3">Admin</h1>
    <p class="text-muted">Use your admin secret in the URL query (<code>?key=...</code>) or <code>X-Admin-Key</code> header.</p>
    <ul>
      <li><a href="<%= c %>/admin/bookings?key=easy2work-local-dev">Bookings (demo key — JDBC off only)</a></li>
      <li><a href="<%= c %>/">Site home</a></li>
    </ul>
  </div>
</body>
</html>
