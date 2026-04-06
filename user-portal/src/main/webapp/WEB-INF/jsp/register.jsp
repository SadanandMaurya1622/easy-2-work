<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% final String c = request.getContextPath(); %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Register - Easy 2 Work</title>
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
    .register-card {
      background: white;
      border-radius: 15px;
      box-shadow: 0 10px 40px rgba(0,0,0,0.2);
      padding: 3rem;
      max-width: 500px;
      width: 100%;
    }
    .register-title {
      color: #667eea;
      font-weight: 700;
      margin-bottom: 1.5rem;
    }
    .btn-register {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border: none;
      color: white;
      padding: 0.75rem;
      font-weight: 600;
      border-radius: 50px;
      transition: transform 0.2s;
    }
    .btn-register:hover {
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
  <div class="register-card">
    <h1 class="register-title text-center">Create Account</h1>
    <p class="text-center text-muted mb-4">Join Easy 2 Work today</p>

    <% if (request.getAttribute("error") != null) { %>
      <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error") %>
      </div>
    <% } %>

    <form method="post" action="<%= c %>/register">
      <div class="row mb-3">
        <div class="col-md-6">
          <label for="firstName" class="form-label">First Name</label>
          <input type="text" class="form-control" id="firstName" name="firstName" required>
        </div>
        <div class="col-md-6">
          <label for="lastName" class="form-label">Last Name</label>
          <input type="text" class="form-control" id="lastName" name="lastName" required>
        </div>
      </div>

      <div class="mb-3">
        <label for="email" class="form-label">Email Address</label>
        <input type="email" class="form-control" id="email" name="email" required>
      </div>

      <div class="mb-3">
        <label for="phone" class="form-label">Phone Number</label>
        <input type="tel" class="form-control" id="phone" name="phone" placeholder="Optional">
      </div>

      <div class="mb-3">
        <label for="password" class="form-label">Password</label>
        <input type="password" class="form-control" id="password" name="password" minlength="6" required>
        <div class="form-text">Must be at least 6 characters</div>
      </div>

      <div class="mb-3">
        <label for="confirmPassword" class="form-label">Confirm Password</label>
        <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
      </div>

      <div class="d-grid mb-3">
        <button type="submit" class="btn btn-register">Create Account</button>
      </div>
    </form>

    <hr class="my-4">

    <div class="text-center">
      <p class="mb-0">Already have an account? <a href="<%= c %>/login" class="text-decoration-none fw-bold" style="color: #667eea;">Sign In</a></p>
      <p class="mt-2 mb-0"><a href="<%= c %>/" class="text-decoration-none text-muted">Back to Home</a></p>
    </div>
  </div>
</body>
</html>
