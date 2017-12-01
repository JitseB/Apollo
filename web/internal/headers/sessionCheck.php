<?php
  if (!defined('APPLICATION') || APPLICATION !== true) {
    die('Unauthorized access of '. pathinfo(__FILE__)['filename'] .'.php.');
  }

  session_start();
  if (is_null($_SESSION['ApolloEmail']) || is_null($_SESSION['ApolloUsername'])) {
    header('Location: ../404');
    die();
  }
 ?>
