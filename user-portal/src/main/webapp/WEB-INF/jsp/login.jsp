<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% final String c = request.getContextPath(); %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Login - Easy 2 Work</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    body {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 20px;
    }
    .login-card {
      background: white;
      border-radius: 15px;
      box-shadow: 0 10px 40px rgba(0,0,0,0.2);
      padding: 3rem;
      max-width: 450px;
      width: 100%;
    }
    .login-title {
      color: #667eea;
      font-weight: 700;
      margin-bottom: 1.5rem;
    }
    .btn-login {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border: none;
      color: white;
      padding: 0.75rem;
      font-weight: 600;
      border-radius: 50px;
      transition: transform 0.2s;
    }
    .btn-login:hover {
      transform: translateY(-2px);
      box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
      color: white;
    }
    .form-control:focus {
      border-color: #667eea;
      box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
    }
  </style>
</head>
<body>
  <div class="login-card">
    <h1 class="login-title text-center">Welcome Back</h1>
    <p class="text-center text-muted mb-4">Sign in to your account</p>

    <% if (request.getAttribute("error") != null) { %>
      <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
      </div>
    <% } %>

    <form method="post" action="<%= c %>/login">
      <input type="hidden" name="redirect" value="<%= request.getParameter("redirect") != null ? request.getParameter("redirect") : "" %>">

      <div class="mb-3">
        <label for="email" class="form-label">Email Address</label>
        <input type="email" class="form-control" id="email" name="email" required>
      </div>

      <div class="mb-3">
        <label for="password" class="form-label">Password</label>
        <input type="password" class="form-control" id="password" name="password" required>
      </div>

      <div class="d-grid mb-3">
        <button type="submit" class="btn btn-login">Sign In</button>
      </div>
    </form>

    <hr class="my-4">

    <div class="text-center">
      <p class="mb-0">Don't have an account? <a href="<%= c %>/register" class="text-decoration-none fw-bold" style="color: #667eea;">Sign Up</a></p>
      <p class="mt-2 mb-0"><a href="<%= c %>/" class="text-decoration-none text-muted">Back to Home</a></p>
    </div>
  </div>
</body>
</html>
