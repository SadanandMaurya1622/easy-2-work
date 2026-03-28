/**
 * Easy 2 Work — shared API client (JSP pages use this for /api/* Servlets).
 * Requires jQuery loaded before this file.
 */
(function (global, $) {
  'use strict';

  function ctx() {
    var el = document.body;
    if (!el) {
      return '';
    }
    var v = el.getAttribute('data-ctx');
    return typeof v === 'string' ? v : '';
  }

  global.Easy2WorkApi = {
    contextPath: ctx,

    stats: function () {
      return $.getJSON(ctx() + '/api/stats');
    },

    health: function () {
      return $.getJSON(ctx() + '/api/health');
    },

    services: function () {
      return $.getJSON(ctx() + '/api/services');
    },

    serviceDetail: function (code) {
      return $.getJSON(ctx() + '/api/service-detail', { id: code });
    },

    bookings: function (phone) {
      return $.getJSON(ctx() + '/api/bookings', { phone: phone });
    },

    /** @param {Object} payload — customerName, phone, email, serviceType, description, address, preferredAt */
    createBooking: function (payload) {
      return $.ajax({
        url: ctx() + '/api/booking',
        type: 'POST',
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify(payload)
      });
    }
  };
})(window, jQuery);
