(function ($) {
  'use strict';

  function esc(s) {
    if (s == null || s === undefined) {
      return '';
    }
    return String(s)
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/"/g, '&quot;');
  }

  function statusBadge(b) {
    if (b.statusKind === 'done') {
      return '<span class="badge bg-success">Completed</span>';
    }
    if (b.statusKind === 'cancelled') {
      return '<span class="badge bg-secondary">Cancelled</span>';
    }
    return '<span class="badge bg-warning text-dark">' + esc(b.status || 'PENDING') + '</span>';
  }

  function renderTable(data) {
    var rows = data.all || [];
    var base = Easy2WorkApi.contextPath();
    if (!rows.length) {
      return '<div class="alert alert-light border">Is number par koi booking record nahi mila. Pehle <a href="' + esc(base) + '/book.jsp">book</a> karein ya number check karein.</div>';
    }
    var h = '';
    h += '<div class="d-flex flex-wrap align-items-center justify-content-between gap-2 mb-3">';
    h += '<h2 class="h5 mb-0">Aapki saari bookings</h2>';
    h += '<span class="text-muted small">Total: <strong>' + rows.length + '</strong></span></div>';
    h += '<div class="table-responsive mb-4"><table class="table table-hover align-middle my-bookings-table my-bookings-table-full">';
    h += '<thead class="table-light"><tr><th>Ref</th><th>Booking date</th><th>Service name</th><th>Address</th><th>Mobile</th><th>Status</th></tr></thead><tbody>';
    rows.forEach(function (b) {
      h += '<tr>';
      h += '<td class="fw-semibold text-nowrap">#' + esc(b.id) + '</td>';
      h += '<td class="text-nowrap">' + esc(b.bookedAt) + '</td>';
      h += '<td><span class="fw-semibold">' + esc(b.serviceTitle) + '</span><br><span class="small text-muted">(' + esc(b.serviceType) + ')</span></td>';
      h += '<td class="my-bookings-address">' + esc(b.address) + '</td>';
      h += '<td class="text-nowrap">' + esc(b.phone) + '</td>';
      h += '<td>' + statusBadge(b) + '</td>';
      h += '</tr>';
    });
    h += '</tbody></table></div>';
    h += '<a href="' + esc(base) + '/book.jsp" class="btn btn-success">Nayi service book karein</a>';
    return h;
  }

  $(function () {
    if (!window.Easy2WorkApi) {
      return;
    }

    var $form = $('#bookingLookupForm');
    var $mount = $('#bookingsApiMount');
    if (!$form.length || !$mount.length) {
      return;
    }

    $form.on('submit', function (e) {
      e.preventDefault();
      var phone = $.trim($('#phone').val());
      if (!phone) {
        return;
      }

      var $btn = $form.find('button[type="submit"]').prop('disabled', true);
      $mount.html('<p class="text-muted mb-0">Loading…</p>');
      $form.find('.booking-lookup-error').remove();

      Easy2WorkApi.bookings(phone)
        .done(function (data) {
          if (!data || data.ok === false) {
            $mount.empty();
            var err = data && data.error ? data.error : 'Error';
            $form.append('<div class="alert alert-warning booking-lookup-error mt-3">' + esc(err) + '</div>');
            return;
          }
          $mount.html(renderTable(data));
        })
        .fail(function (xhr) {
          $mount.empty();
          var msg = 'Could not load bookings';
          if (xhr.responseJSON && xhr.responseJSON.error) {
            msg = xhr.responseJSON.error;
          }
          $form.append('<div class="alert alert-warning booking-lookup-error mt-3">' + esc(msg) + '</div>');
        })
        .always(function () {
          $btn.prop('disabled', false);
        });
    });
  });
})(jQuery);
