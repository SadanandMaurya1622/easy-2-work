<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% final String c = request.getContextPath(); %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Terms &amp; Conditions – Easy 2 Work</title>
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
    <h1>Terms &amp; Conditions</h1>
    <p><strong>Last updated:</strong> 2026</p>
    <p>Welcome to Easy 2 Work. By using our platform or booking any home service, you agree to these terms. Please read them carefully.</p>

    <h2>Use of the service</h2>
    <p>Easy 2 Work is an on-demand home service platform. You may use it only for lawful purposes and in line with these terms. You must provide accurate details when registering and booking (name, address, contact, service description).</p>

    <h2>Bookings and payments</h2>
    <p>When you book a service:</p>
    <ul>
      <li>Pricing and payment terms will be shown at the time of booking.</li>
      <li>You are responsible for paying the agreed amount. Cancellation and refund rules will be communicated in the app or at booking.</li>
      <li>We connect you with verified professionals; the actual service is performed by them at your location.</li>
    </ul>

    <h2>Your conduct</h2>
    <p>You agree not to misuse the platform, harass engineers, or provide false information. We may suspend or terminate access if we detect a breach of these terms or misuse.</p>

    <h2>Disclaimer</h2>
    <p>We facilitate bookings between you and service professionals. We do not guarantee outcomes of individual jobs. For any dispute about the quality or result of a service, you may use in-app support or contact us; our liability is limited as permitted by law.</p>

    <h2>Changes</h2>
    <p>We may update these terms from time to time. Continued use of Easy 2 Work after changes means you accept the updated terms.</p>

    <h2>Contact</h2>
    <p>For questions about these terms, contact us through the Easy 2 Work app or website.</p>
  </main>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>

  <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
