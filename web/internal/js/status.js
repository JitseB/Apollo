function startStatusUpdating() {
  getNewStatuses();
  setInterval(getNewStatuses(), 5000);
}

function getNewStatuses() {
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
      if (this.readyState == 4) {
        if (this.status == 200) {
          var json = JSON.parse(this.responseText);
          setAllStatuses(json);
        } else {
          document.getElementById('auth-text').innerText = 'Failed';
          document.getElementById('auth-text').parentElement.className = "col-md-4 py-4 text-danger";
          document.getElementById('sessions-text').innerText = 'Failed';
          document.getElementById('sessions-text').parentElement.className = "col-md-4 py-4 text-danger";
          document.getElementById('api-text').innerText = 'Failed';
          document.getElementById('api-text').parentElement.className = "col-md-4 py-4 text-danger";
          notify(name, 'Failed to fetch Mojang statuses.');
        }
    }
  };
    xhttp.open('GET', 'https://status.mojang.com/check', true);
    xhttp.send();
}

function setAllStatuses(json) {
  var auth = getStatus(json, 'auth.mojang.com');
  var authElement = document.getElementById('auth-text');
  var authNewDescription = getDescription(auth);
  if (notifyStatusUpdates && authElement.innerText != 'Pending...' && authElement.innerText != authNewDescription) {
    notify(name, 'Mojang auth service ' + getNotificationMessage(auth));
  }
  authElement.innerText = authNewDescription;
  authElement.parentElement.className = "col-md-4 py-4 " + getColor(auth);

  var sessions = getStatus(json, 'session.minecraft.net');
  var sessionsElement = document.getElementById('sessions-text');
  var sessionsNewDescription = getDescription(sessions);
  if (notifyStatusUpdates && sessionsElement.innerText != 'Pending...' && sessionsElement.innerText != sessionsNewDescription) {
    notify(name, 'Minecraft sessions service ' + getNotificationMessage(sessions));
  }
  sessionsElement.innerText = sessionsNewDescription;
  sessionsElement.parentElement.className = "col-md-4 py-4 " + getColor(sessions);

  var api = getStatus(json, 'api.mojang.com');
  var apiElement = document.getElementById('api-text');
  var apiNewDescription = getDescription(api);
  if (notifyStatusUpdates && apiElement.innerText != 'Pending...' && apiElement.innerText != apiNewDescription) {
    notify(name, 'Mojang API service ' + getNotificationMessage(api));
  }
  apiElement.innerText = apiNewDescription;
  apiElement.parentElement.className = "col-md-4 py-4 " + getColor(api);
}

function getStatus(json, url) {
  for (var i = 0; i < json.length; i++) {
    if (json[i][url] != undefined) {
      return json[i][url];
    }
  }
  return undefined;
}

function getColor(status) {
  switch(status) {
    case 'green': return 'text-success';
    case 'yellow': return 'text-warning';
    case 'red': return 'text-danger';
    default: return undefined;
  }
}

function getDescription(status) {
  switch(status) {
    case 'green': return 'Service is online';
    case 'yellow': return 'Trouble keeping up';
    case 'red': return 'Service is offline';
    default: return undefined;
  }
}

function getNotificationMessage(status) {
  switch(status) {
    case 'green': return 'is back online!';
    case 'yellow': return 'has trouble keeping up!';
    case 'red': return 'just went offline!';
    default: return undefined;
  }
}