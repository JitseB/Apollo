<?php
  if (!defined('APPLICATION') || APPLICATION !== true) {
    die('Unauthorized access of '. pathinfo(__FILE__)['filename'] .'.php.');
  }

  $Config = array(
    // [General]
    'name' => 'Apollo',
    'network_name' => 'SpaceMine Ltd.',
    'style' => '//bootswatch.com/4/cosmo/bootstrap.min.css',
    
    // [Notifications]
    // Priority = 0 -> no notifications
    // Priority = 1 -> message on (now) online
    // Priority = 2 -> message on (now) online and offline
    // Priority = 3 -> message on (now) online, offline and warning
    // Priority = 4 -> message on (now) online, offline, warning and critical
    'notification_priority' => 4,
    'notification_sound' => true,

    // [Version]
    'version' => '0.1',
    'check_for_updates' => true,

    // [MySQL]
    'mysql_host' => 'localhost',
    'mysql_port' => 3306,
    'mysql_username' => 'root',
    'mysql_password' => 'password',
    'mysql_database' => 'projects',

    // [Google reCAPTCHA]
    'recaptcha_enabled' => false,
    'recaptcha_site_key' => '6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI',
    'recaptcha_secret_key' => '6LeIxAcTAAAAAGG-vFI1TnRWxMZNFuojJ4WifJWe',
    'recaptcha_theme' => 'light',

    // [Socket]
    'socket_host' => 'localhost',
    'socket_port' => 8193
  );
 ?>
