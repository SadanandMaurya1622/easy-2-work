(function ($) {
  'use strict';

  $(function () {
    if (!window.Easy2WorkApi) {
      return;
    }

    var pre = ($('body').attr('data-preselect-service') || '').trim();

    Easy2WorkApi.services()
      .done(function (d) {
        if (!d || !d.services || !d.services.length) {
          return;
        }
        var $sel = $('#serviceType');
        $sel.empty();
        $sel.append($('<option>', { value: '', text: 'Choose…', disabled: true, selected: !pre }));
        d.services.forEach(function (s) {
          var opt = $('<option>', { value: s.code, text: s.title + ' (' + s.code + ')' });
          if (pre && s.code === pre) {
            opt.prop('selected', true);
            $sel.find('option').first().prop('selected', false);
          }
          $sel.append(opt);
        });
      });

    $('#bookingForm').on('submit', function (e) {
      var form = this;
      $(form).addClass('was-validated');
      if (!form.checkValidity()) {
        e.preventDefault();
        e.stopPropagation();
        return;
      }
      e.preventDefault();
      e.stopPropagation();

      var payload = {
        customerName: $('#customerName').val().trim(),
        phone: $('#phone').val().trim(),
        email: $('#email').val().trim(),
        serviceType: $('#serviceType').val(),
        description: $('#description').val().trim(),
        address: $('#address').val().trim(),
        preferredAt: $('#preferredAt').val() || ''
      };

      var $btn = $(form).find('button[type="submit"]').prop('disabled', true);
      Easy2WorkApi.createBooking(payload)
        .done(function (res) {
          if (res && res.ok) {
            window.location.href = Easy2WorkApi.contextPath() + '/?booked=1&ref=' + encodeURIComponent(res.id);
            return;
          }
          window.alert(res && res.error ? res.error : 'Booking failed');
        })
        .fail(function (xhr) {
          var msg = 'Booking failed';
          if (xhr.responseJSON && xhr.responseJSON.error) {
            msg = xhr.responseJSON.error;
          }
          window.alert(msg);
        })
        .always(function () {
          $btn.prop('disabled', false);
        });
    });
  });
})(jQuery);
