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

function message(server, status) {
  var play = false;
  if (status === 'critical' && priority >= 4) {
    notify(name, 'Critical status for ' + server + '!');
    play = true;
  }
  else if (status === 'warning' && priority >= 3) {
    notify(name, 'Warning status for ' + server + '.');
    play = true;
  }
  else if (status === 'offline' && priority >= 2) {
    notify(name, server + ' just went offline.');
    play = true;
  }
  else if (status === 'good' && priority >= 1) {
    notify(name, server + ' is healthy again.');
    play = true;
  }

  // Now play the sound (if enabled).
  if (playSound && play) {
    var audio = new Audio('../internal/media/notification.mp3');
    audio.play();
  }
}