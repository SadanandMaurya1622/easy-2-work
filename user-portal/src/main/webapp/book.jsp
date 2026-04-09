<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% final String c = request.getContextPath();
  com.easy2work.core.model.User sessionUser = (com.easy2work.core.model.User) session.getAttribute("user");
  String defaultName = sessionUser != null && sessionUser.getFullName() != null ? sessionUser.getFullName() : "";
  String defaultPhone = sessionUser != null && sessionUser.getPhone() != null ? sessionUser.getPhone() : "";
  String defaultEmail = sessionUser != null && sessionUser.getEmail() != null ? sessionUser.getEmail() : "";
  request.setAttribute("defaultName", defaultName);
  request.setAttribute("defaultPhone", defaultPhone);
  request.setAttribute("defaultEmail", defaultEmail);
  String bookingError = (String) session.getAttribute("bookingError");
  if (bookingError != null) {
    request.setAttribute("bookingError", bookingError);
    session.removeAttribute("bookingError");
  }
  @SuppressWarnings("unchecked")
  java.util.Map<String, String> bookingForm = (java.util.Map<String, String>) session.getAttribute("bookingForm");
  if (bookingForm != null) {
    request.setAttribute("bookingForm", bookingForm);
    session.removeAttribute("bookingForm");
  }
  String effServiceType = "";
  if (bookingForm != null) {
    String st = bookingForm.get("serviceType");
    if (st != null && !st.isBlank()) {
      effServiceType = st.trim().toUpperCase(java.util.Locale.ROOT);
    }
  }
  if (effServiceType.isEmpty()) {
    String q = request.getParameter("serviceType");
    if (q != null && !q.isBlank()) {
      effServiceType = q.trim().toUpperCase(java.util.Locale.ROOT);
    }
  }
  request.setAttribute("effServiceType", effServiceType);
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Book a service – Easy 2 Work</title>
  <base href="<%= c %>/">
  <link rel="icon" href="<%= c %>/images/logo.png" type="image/png">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="<%= c %>/css/style.css" rel="stylesheet">
</head>
<body data-ctx="<%= c %>" data-preselect-service="<c:out value='${effServiceType}'/>">
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

  <main class="book-page container">
    <div class="book-header">
      <h1>Book a Service</h1>
      <p class="lead">Tell us your requirement in a few steps and we will confirm quickly on call/WhatsApp.</p>
    </div>

    <c:if test="${param.ok == '1'}">
      <div class="alert alert-success" role="alert">
        Thank you! Your booking request was received.
        <c:if test="${not empty param.ref}"> Reference #<c:out value="${param.ref}"/>.</c:if>
      </div>
    </c:if>

    <c:if test="${not empty bookingError}">
      <div class="alert alert-danger" role="alert"><c:out value="${bookingError}"/></div>
    </c:if>

    <div class="card p-4 p-md-5 book-card-premium">
      <form id="bookingForm" method="post" action="<%= c %>/booking" novalidate>
        <div class="book-form-section">
          <div class="book-form-section-title">Contact Details</div>
          <div class="row g-3">
            <div class="col-12">
              <label class="form-label" for="customerName">Full name <span class="text-danger">*</span></label>
              <input type="text" class="form-control" id="customerName" name="customerName" required maxlength="120"
                     value="<c:out value='${not empty bookingForm.customerName ? bookingForm.customerName : defaultName}'/>" autocomplete="name">
            </div>
            <div class="col-12 col-md-6">
              <label class="form-label" for="phone">Phone <span class="text-danger">*</span></label>
              <input type="tel" class="form-control" id="phone" name="phone" required maxlength="20"
                     placeholder="e.g. 9876543210"
                     value="<c:out value='${not empty bookingForm.phone ? bookingForm.phone : defaultPhone}'/>" autocomplete="tel" readonly>
            </div>
            <div class="col-12 col-md-6">
              <label class="form-label" for="email">Email <span class="text-muted small">(optional)</span></label>
              <input type="email" class="form-control" id="email" name="email" maxlength="255"
                     value="<c:out value='${not empty bookingForm.email ? bookingForm.email : defaultEmail}'/>" autocomplete="email" readonly>
            </div>
          </div>
        </div>

        <div class="book-form-section">
          <div class="book-form-section-title">Service Requirement</div>
          <div class="mb-3">
            <label class="form-label" for="serviceType">Service <span class="text-danger">*</span></label>
            <select class="form-select" id="serviceType" name="serviceType" required>
              <option value="" disabled ${empty effServiceType ? 'selected' : ''}>Choose…</option>
            </select>
          </div>
          <div class="mb-0">
            <label class="form-label" for="description">What do you need? <span class="text-danger">*</span></label>
            <textarea class="form-control" id="description" name="description" rows="4" required maxlength="4000"
                      placeholder="Brief details (problem, size of home, preferred time window…)"><c:out value="${bookingForm.description}"/></textarea>
          </div>
        </div>

        <div class="book-form-section">
          <div class="book-form-section-title">Visit Information</div>
          <div class="mb-3">
            <label class="form-label" for="address">Service address <span class="text-danger">*</span></label>
            <textarea class="form-control" id="address" name="address" rows="3" required maxlength="2000"
                      placeholder="Full address with landmark, pincode"><c:out value="${bookingForm.address}"/></textarea>
          </div>
          <div class="mb-0">
            <label class="form-label" for="preferredAt">Preferred date &amp; time <span class="text-muted small">(optional)</span></label>
            <input type="datetime-local" class="form-control" id="preferredAt" name="preferredAt"
                   value="<c:out value='${bookingForm.preferredAt}'/>">
          </div>
        </div>

        <div class="book-submit-wrap">
          <button type="submit" class="btn btn-success btn-lg w-100">Submit booking</button>
          <p class="book-submit-note">You will get a booking reference number after successful submission.</p>
        </div>
      </form>
    </div>
  </main>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>

  <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
  <script src="<%= c %>/js/easy2work-api.js"></script>
  <script>
    (function ($) {
      function loadServiceOptions() {
        if (!window.Easy2WorkApi) {
          return;
        }
        var select = document.getElementById('serviceType');
        if (!select) {
          return;
        }
        var selected = select.value;
        var preferredFromServer = '<c:out value="${effServiceType}"/>';
        window.Easy2WorkApi.services()
          .done(function (res) {
            if (!res || !res.ok || !Array.isArray(res.services)) {
              return;
            }
            res.services.forEach(function (s) {
              if (!s || !s.code) {
                return;
              }
              if (select.querySelector('option[value="' + s.code + '"]')) {
                return;
              }
              var opt = document.createElement('option');
              opt.value = s.code;
              opt.textContent = s.title || s.code;
              select.appendChild(opt);
            });
            if (preferredFromServer && select.querySelector('option[value="' + preferredFromServer + '"]')) {
              select.value = preferredFromServer;
            } else if (selected) {
              select.value = selected;
            }
          });
      }

      loadServiceOptions();
      $('#bookingForm').on('submit', function (e) {
        e.preventDefault();
        $(this).addClass('was-validated');
        if (!this.checkValidity()) {
          e.stopPropagation();
          return;
        }
        if (!window.Easy2WorkApi) {
          this.submit();
          return;
        }
        var payload = {
          customerName: $('#customerName').val() || '',
          phone: $('#phone').val() || '',
          email: $('#email').val() || '',
          serviceType: $('#serviceType').val() || '',
          description: $('#description').val() || '',
          address: $('#address').val() || '',
          preferredAt: $('#preferredAt').val() || ''
        };
        var $btn = $(this).find('button[type="submit"]');
        $btn.prop('disabled', true).text('Submitting...');
        window.Easy2WorkApi.createBooking(payload)
          .done(function (res) {
            if (res && res.ok) {
              var ref = encodeURIComponent(res.id || '');
              window.location.href = '<%= c %>/?booked=1&ref=' + ref;
              return;
            }
            alert((res && res.error) ? res.error : 'Could not save booking.');
          })
          .fail(function (xhr) {
            var msg = 'Could not save booking.';
            try {
              var body = JSON.parse(xhr.responseText);
              if (body && body.error) {
                msg = body.error;
              }
            } catch (ignore) {}
            alert(msg);
          })
          .always(function () {
            $btn.prop('disabled', false).text('Submit booking');
          });
      });
    })(jQuery);
  </script>
</body>
</html>
