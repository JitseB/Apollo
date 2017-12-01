function load(socket) {
  socket.on('connect', function() {
    log('[SOCKET] Established connection.', '#27ae60');
    socket.emit('server_list');
  });

  // Data callbacks.
  socket.on('server_list', function(error, data) {
    if (error) {
      log('[SOCKET] Server list callback error: ' + error + '.', '#c0392b');
      return;
    }
    var servers = JSON.parse(data);
    log('[SOCKET] Recieved server data for ' + servers.length + ' server(s).' + error, '#2980b9');
    for (var i = 0; i < servers.length; i++) {
      createServer(servers[i]);
    }

    // Now let's show the page!
    document.getElementById('body').style.display = 'block';
    document.getElementById('loader').style.display = 'none';
    log('[APOLLO] Showing panel.', '#27ae60');
  });

  socket.on('pong', (latency) => {
    document.getElementById('ping').innerText = latency;
  });

  // Error catching.
  socket.on('connect_error', function(error) {
    document.getElementById('overlay-text').innerText = 'The socket server appears to be offline!';
      log('[SOCKET] Socket server is offline.', '#c0392b');
  });

  socket.on('connect_timeout', function(timeout) {
    document.getElementById('overlay-text').innerText = 'The socket connection timed out!';
      log('[SOCKET] Socket connection timed out.', '#c0392b');
  });

  socket.on('error', function(error) {
    document.getElementById('overlay-text').innerText = 'An error occured in your socket connection!';
      log('[SOCKET] Error occured in the socket connection, error: ' + error + '.', '#c0392b');
  });
};

function createServer(serverData) {
  var status = calculateStatus(serverData);

  var server = document.createElement('a');
  server.href = '/server/?id=' + serverData['Port'];
  server.className = 'server';
  server.id = serverData['Port']; //Todo: Change to propper identifiers.

  var nameSpan = document.createElement('span');
  nameSpan.className = 'name';
  nameSpan.innerText = serverData['Name'];
  server.appendChild(nameSpan);

  var statusSpan = document.createElement('span');
  statusSpan.className = 'status';
  statusSpan.innerHTML = 'Status: <span class="status-value">' + (status == 'good' ? 'online' : status) + '</span>';
  server.appendChild(statusSpan);

  server.classList += ' ' + status;

  // Todo calculate server status.

  document.getElementById('server-container').appendChild(server);
}

function calculateStatus(server) {
  if (!server['Online']) {
    return 'offline';
  }
  if (server['TPS'] <= 17.40) {
    return 'critical';
  }
  if (server['TPS'] <= 19.20) {
    return 'warning';
  }
  // If RAM usage is above 50%.
  if (server['usedMemory'] / server['maxMemory'] > 0.5) {
    return 'warning';
  }
  // If RAM usage is above 75%.
  if (server['usedMemory'] / server['maxMemory'] > 0.75) {
    return 'critical';
  }
  else {
    return 'good';
  }
}