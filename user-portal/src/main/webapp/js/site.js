(function ($) {
  'use strict';

  function contextPath() {
    var v = $('body').data('ctx');
    return typeof v === 'string' ? v : '';
  }

  function initMap() {
    var mapEl = document.getElementById('varanasiMap');
    if (!mapEl || typeof L === 'undefined') return;
    var varanasi = [25.3176, 82.9739];
    var map = L.map('varanasiMap').setView(varanasi, 12);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map);
    L.marker(varanasi).addTo(map).bindPopup('<strong>Varanasi</strong><br>Easy 2 Work service area').openPopup();
  }

  function initMarquee() {
    var $marquee = $('#servicesMarquee');
    var $scroll = $marquee.find('.services-scroll').first();
    if (!$marquee.length || !$scroll.length) return;
    var $clone = $scroll.clone();
    $clone.attr('aria-hidden', 'true');
    $marquee.append($clone);
  }

  function animateCount($el, target) {
    var step = Math.max(1, Math.floor(target / 40));
    var current = 0;
    function tick() {
      current = Math.min(current + step, target);
      $el.text(current);
      if (current < target) requestAnimationFrame(tick);
    }
    tick();
  }

  function runStatAnimations() {
    $('.stat-number').each(function () {
      var $el = $(this);
      if ($el.data('animated')) return;
      var target = parseInt($el.attr('data-count'), 10);
      if (isNaN(target)) return;
      $el.data('animated', true);
      animateCount($el, target);
    });
  }

  function initStatObserver() {
    var section = document.getElementById('why-us');
    if (!section) {
      runStatAnimations();
      return;
    }
    var obs = new IntersectionObserver(function (entries) {
      if (entries[0].isIntersecting) {
        runStatAnimations();
        obs.disconnect();
      }
    }, { threshold: 0.3 });
    obs.observe(section);
  }

  function loadStats(done) {
    var req = (window.Easy2WorkApi && typeof Easy2WorkApi.stats === 'function')
      ? Easy2WorkApi.stats()
      : $.getJSON(contextPath() + '/api/stats');
    req
      .done(function (data) {
        if (data && typeof data.homesServiced === 'number') {
          $('#stat-homes').attr('data-count', data.homesServiced);
          $('#stat-hours').attr('data-count', data.hoursSaved);
          $('#stat-pros').attr('data-count', data.verifiedProfessionals);
        }
      })
      .always(function () {
        if (typeof done === 'function') done();
      });
  }

  $(function () {
    initMarquee();
    initMap();
    loadStats(initStatObserver);
  });
})(jQuery);
