<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% final String c = request.getContextPath(); %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Privacy Policy – Easy 2 Work</title>
  <base href="<%= c %>/">
  <link rel="icon" href="<%= c %>/images/logo.png" type="image/png">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="<%= c %>/css/style.css" rel="stylesheet">
  <style>
    .legal-page { padding: 5rem 0 4rem; max-width: 800px; margin: 0 auto; }
    .legal-page h1 { font-size: 1.75rem; margin-bottom: 1.5rem; font-weight: 700; }
    .legal-page h2 { font-size: 1.15rem; margin-top: 1.5rem; margin-bottom: 0.5rem; font-weight: 600; }
    .legal-page p, .legal-page li { color: #444; line-height: 1.7; margin-bottom: 0.75rem; }
    .legal-page ul { padding-left: 1.25rem; }
    .legal-back { display: inline-block; margin-bottom: 1.5rem; color: var(--pronto-green, #0a5f38); text-decoration: none; font-weight: 500; }
    .legal-back:hover { text-decoration: underline; }
  </style>
</head>
<body data-ctx="<%= c %>">
<%@ include file="/WEB-INF/jspf/navbar.jspf" %>

  <main class="legal-page container">
    <a href="<%= c %>/" class="legal-back">&larr; Back to Home</a>
    <h1>Privacy Policy</h1>
    <p><strong>Last updated:</strong> 2026</p>
    <p>Easy 2 Work (&ldquo;we&rdquo;, &ldquo;us&rdquo;) respects your privacy. This policy describes how we collect, use and protect your information when you use our on-demand home service platform.</p>

    <h2>Information we collect</h2>
    <p>We may collect:</p>
    <ul>
      <li>Name, phone number, email and address when you register or book a service.</li>
      <li>Service details (type, description, timing) and communication with engineers.</li>
      <li>Device and usage data (e.g. app version, IP) to improve our services.</li>
    </ul>

    <h2>How we use it</h2>
    <p>We use your information to:</p>
    <ul>
      <li>Provide, schedule and complete home services.</li>
      <li>Connect you with verified professionals and manage bookings.</li>
      <li>Send updates, support and important notices.</li>
      <li>Improve our app, safety and user experience.</li>
    </ul>

    <h2>Sharing and security</h2>
    <p>We do not sell your personal data. We may share information only with assigned engineers and as needed for the service, or when required by law. We use reasonable measures to keep your data secure.</p>

    <h2>Your choices</h2>
    <p>You can update your profile, opt out of marketing messages where offered, and request access or correction of your data by contacting us.</p>

    <h2>Contact</h2>
    <p>For privacy-related questions, contact us through the Easy 2 Work app or website.</p>
  </main>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>

  <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
