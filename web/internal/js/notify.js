function notify(app, message) {
  Push.create(app, {
    body: message,
    icon: '../internal/icons/favicon.ico',
    timeout: 5000,
    onClick: function () {
      window.focus();
      this.close();
    }
  });
}