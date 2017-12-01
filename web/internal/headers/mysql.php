<?php
  if (!defined('APPLICATION') || APPLICATION !== true) {
    die('Unauthorized access of '. pathinfo(__FILE__)['filename'] .'.php.');
  }

  $MySQL = new mysqli($Config['mysql_host'], $Config['mysql_username'], $Config['mysql_password'], $Config['mysql_database'], $Config['mysql_port'])
  OR die('Could not connect to MySQL: ' . mysqli_connect_error());
 ?>
