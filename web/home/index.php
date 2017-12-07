<?php
  define('APPLICATION', true);
  require_once '../internal/headers/config.php';
  require_once '../internal/headers/sessionCheck.php';
?>

<!DOCTYPE html>
<html>
  <head>
    <?php include '../internal/include/head.inc.php'; ?>
    
    <title>Panel | <?php echo $Config['name'] ?></title>
    <link rel="stylesheet" href="../internal/css/home.css">
    <link rel="stylesheet" href="../internal/css/loader.css">
  </head>
  <body> 
    <div id="body">
      <div class="container">
        <nav class="navbar navbar-toggleable-md navbar-expand-lg navbar-faded navbar-light bg-faded bg-light my-4">
        <span class="navbar-brand"><?php echo $Config['name'] ?></span>
          <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button>

          <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
              <li class="nav-item active">
                <a class="nav-link" href="#">Home <span class="sr-only">(current)</span></a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="../profile">Profile</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="../logout">Logout</a>
              </li>
            </ul>
            <form class="form-inline my-2 my-lg-0" action="../server" method="GET">
              <input class="form-control mr-sm-2" type="text" placeholder="Server ID" name="id" required>
              <button class="btn btn-outline-primary my-2 my-sm-0" type="submit">Check</button>
            </form>
          </div>
        </nav>
        <h2>Mojang Status</h2>
        <div class="row text-center">
          <div class="col-md-4 py-4 text-primary">
            <h3>Auth</h3>
            <p id="auth-text">Pending...</p>
          </div>
          <div class="col-md-4 py-4 text-primary">
            <h3>Sessions</h3>
            <p id="sessions-text">Pending...</p>
          </div>
          <div class="col-md-4 py-4 text-primary">
            <h3>API</h3>
            <p id="api-text">Pending...</p>
          </div>
        </div>
        <h2>Online</h2>
        <div class="row text-center">
          <div class="col-md-4 my-4">
            <h3>Player Record</h3>
            <p id="record-value">0</p>
          </div>
          <div class="col-md-4 my-4">
            <h3>Total Playing</h3>
            <p id="total-value">0</p>
          </div>
          <div class="col-md-4 my-4">
            <h3>Growth</h3>
            <p id="growth-value">0</p>
          </div>
        </div>
        <h2 class="mb-4">Servers</h2>
        <div id="server-container"></div>
      </div>
      <footer class="bg-faded bg-light">
        <div class="mx-auto col-md-5 pt-2 pb-4 text-center">
          <p class="mt-4 text-primary">Socket ping: <span id="ping">-</span> ms.</p>
          <?php include '../internal/include/footer.inc.php'; ?>
        </div>
      </footer>
    </div>
    <div id="loader" class="overlay bg-faded bg-light">
      <div class="spinner bg-primary"></div>
      <span class="text-danger" id="overlay-text"></span>
    </div>

    <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.slim.min.js"></script>
    <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js"></script>
    <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.0.0-alpha.4/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/socket.io/2.0.4/socket.io.slim.js"></script>
    <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/flot/0.8.3/jquery.flot.min.js"></script>
    <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/push.js/0.0.8/push.min.js"></script>

    <script type="text/javascript" src="../internal/js/logger.js"></script>
    <script type="text/javascript" src="../internal/js/notify.js"></script>
    <script type="text/javascript" src="../internal/js/status.js"></script>
    <script type="text/javascript" src="../internal/js/home.js"></script>

    <script type="text/javascript">
      // Also used for notify.js
      const name = '<?php echo $Config["name"]; ?>';
      const priority = <?php echo $Config["notification_priority"]; ?>;
      const playSound = <?php echo $Config["notification_sound"]; ?>;
      const notifyStatusUpdates = <?php echo $Config['notification_status'] ?>

      log('[APOLLO] Running version <?php echo $Config["version"]; ?>.', '#f39c12');
      log('[APOLLO] Connecting to socket server...', '#2980b9');

      var socket = io.connect('<?php echo $Config["socket_host"]; ?>:<?php echo $Config["socket_port"]; ?>', { reconnect: true });
      load(socket);
    </script>
  </body>
</html>
