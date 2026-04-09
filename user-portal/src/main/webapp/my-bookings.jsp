<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% final String c = request.getContextPath(); %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>My bookings – Easy 2 Work</title>
  <base href="<%= c %>/">
  <link rel="icon" href="<%= c %>/images/logo.png" type="image/png">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="<%= c %>/css/style.css" rel="stylesheet">
</head>
<body data-ctx="<%= c %>">
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

  <main class="my-bookings-page container py-5">
    <h1 class="mb-2">My bookings</h1>
    <p class="text-muted mb-4">Your bookings are linked to your account phone number. Only your own bookings are shown here.</p>

    <div class="card border-0 shadow-sm mb-5" style="max-width: 480px; border-radius: 16px;">
      <div class="card-body p-4">
        <form id="bookingLookupForm" method="post" action="<%= c %>/my-bookings" class="mb-0">
          <label class="form-label fw-semibold" for="phone">Account mobile number</label>
          <input type="tel" class="form-control form-control-lg mb-3" id="phone" name="phone" required maxlength="20"
                 placeholder="e.g. 9876543210"
                 value="<c:out value='${phoneInput}'/>" autocomplete="tel" readonly>
          <button type="submit" class="btn btn-success w-100 btn-lg">Show my bookings</button>
        </form>
        <c:if test="${not empty lookupError}">
          <div class="alert alert-warning mt-3 mb-0"><c:out value="${lookupError}"/></div>
        </c:if>
      </div>
    </div>

    <div id="bookingsApiMount">
    <c:if test="${not empty phoneKey and empty lookupError}">
      <c:choose>
        <c:when test="${empty bookingsAll}">
          <div class="alert alert-light border">No bookings found for your account number. <a href="<%= c %>/book.jsp">Book a service</a> first.</div>
        </c:when>
        <c:otherwise>

          <div class="d-flex flex-wrap align-items-center justify-content-between gap-2 mb-3">
            <h2 class="h5 mb-0">Your bookings</h2>
            <span class="text-muted small">Total: <strong><c:out value="${bookingCount}"/></strong></span>
          </div>

          <div class="table-responsive mb-4">
            <table class="table table-hover align-middle my-bookings-table my-bookings-table-full">
              <thead class="table-light">
                <tr>
                  <th scope="col">Ref</th>
                  <th scope="col">Booking date</th>
                  <th scope="col">Service name</th>
                  <th scope="col">Address</th>
                  <th scope="col">Mobile</th>
                  <th scope="col">Status</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach var="b" items="${bookingsAll}">
                  <tr>
                    <td class="fw-semibold text-nowrap">#<c:out value="${b.id}"/></td>
                    <td class="text-nowrap"><c:out value="${b.bookedAtDisplay}"/></td>
                    <td><span class="fw-semibold"><c:out value="${b.serviceTitle}"/></span><br><span class="small text-muted">(<c:out value="${b.serviceType}"/>)</span></td>
                    <td class="my-bookings-address"><c:out value="${b.address}"/></td>
                    <td class="text-nowrap"><c:out value="${b.phone}"/></td>
                    <td>
                      <c:choose>
                        <c:when test="${b.statusKind == 'done'}"><span class="badge bg-success">Completed</span></c:when>
                        <c:when test="${b.statusKind == 'cancelled'}"><span class="badge bg-secondary">Cancelled</span></c:when>
                        <c:otherwise><span class="badge bg-warning text-dark"><c:out value="${b.status}"/></span></c:otherwise>
                      </c:choose>
                    </td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>

          <a href="<%= c %>/book.jsp" class="btn btn-success">Book another service</a>
        </c:otherwise>
      </c:choose>
    </c:if>
    </div>
  </main>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>

  <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
  <script src="<%= c %>/js/easy2work-api.js"></script>
  <script src="<%= c %>/js/my-bookings-page.js"></script>
</body>
</html>
