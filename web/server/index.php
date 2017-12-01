<?php
  define('APPLICATION', true);
  require_once '../internal/headers/config.php';
  require_once '../internal/headers/sessionCheck.php';

  if (!isset($_GET['id']) || empty($_GET['id'])) {
    die('Please provide a server ID.');
  }
?>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Server | <?php echo $Config['name'] ?></title>
  </head>
  <body>
    <p>Server ID : <?php echo $_GET['id'] ?></p>
  </body>
</html>
